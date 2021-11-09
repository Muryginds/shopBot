package org.telegram.galacticMiniatures.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.model.UserChatActivity;
import org.telegram.galacticMiniatures.bot.repository.response.AnnouncementsResponse;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.service.UserChatActivityService;
import org.telegram.galacticMiniatures.bot.service.UserMessageService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.galacticMiniatures.parser.ShopParserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class ScheduleController {

    private final ShopParserService shopParserService;
    private final CacheService cacheService;
    private final UserService userService;
    private final OrderService orderService;
    private final UserMessageService userMessageService;
    private final UserChatActivityService userChatActivityService;
    private final Bot bot;

    @Scheduled(fixedRateString = "${schedule.parsePeriod}")
    public void parseSectionsAndListings() {
        shopParserService.parseSections();
        shopParserService.parseListings();
        shopParserService.parseListingImages();
        shopParserService.parseListingOptions();
    }

//    @Scheduled(fixedRateString = "${schedule.parsePeriod}", initialDelayString = "PT10M")
//    public void parseImages() {
//        shopParserService.parseListingImages();
//    }
//
//    @Scheduled(fixedRateString = "${schedule.parsePeriod}", initialDelayString = "PT10M")
//    public void parseOptions() {
//        shopParserService.parseListingOptions();
//    }

    @Async
    @Scheduled(fixedRateString = "${schedule.announcementPeriod}")
    public void announceNewMessages() {
        List<AnnouncementsResponse> responses = userMessageService.getNewAnnouncements();
        Map<String, Integer> summary = responses.stream()
                .collect(Collectors.groupingBy(AnnouncementsResponse::getChatId,
                        Collectors.summingInt(v -> v.getSum().intValue())));
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            SendMessage sm = Utils.prepareSendMessage(entry.getKey(),
                    String.format("You received %d new message(s)", entry.getValue()));
            try {
                bot.executeAsync(sm);
            } catch (TelegramApiException e) {
                log.error("Scheduled task: Announcements failed: " + e.getMessage());
            }
        }

        List<UserChatActivity> announcements = responses.stream()
                .map(a -> userChatActivityService.findByChatIdAndOrderId(a.getChatId(), a.getOrderId())
                        .orElse(new UserChatActivity(
                                userService.getUser(a.getChatId()),
                                orderService.findById(a.getOrderId()).orElseThrow(),
                                LocalDateTime.of(2001, 1, 1, 0,0),
                                null)))
                .peek(u -> u.setAnnounced(LocalDateTime.now()))
                .collect(Collectors.toList());
        userChatActivityService.saveAll(announcements);
    }

    @Async
    @Scheduled(fixedRateString = "${schedule.clearCache}")
    public void clearCache() {

        cacheService.getCache().entrySet().stream()
                .filter(l -> l.getValue().getLastModified().plusHours(24).isBefore(LocalDateTime.now()))
                .forEach(l -> cacheService.remove(l.getKey()));
    }
}