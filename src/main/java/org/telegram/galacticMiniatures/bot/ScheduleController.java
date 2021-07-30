package org.telegram.galacticMiniatures.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.UserChatActivity;
import org.telegram.galacticMiniatures.bot.repository.AnnouncementsResponse;
import org.telegram.galacticMiniatures.bot.service.UserChatActivityService;
import org.telegram.galacticMiniatures.bot.service.UserMessageService;
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
public class ScheduleController {

    private final ShopParserService shopParserService;
    private final UserMessageService userMessageService;
    private final UserChatActivityService userChatActivityService;
    private final Bot bot;

    @Scheduled(fixedRateString = "${schedule.parsePeriod}")
    private void parseInfo() {
        //shopParserService.parseInfo();
    }

    @Scheduled(fixedRateString = "${schedule.announcementPeriod}")
    private void announceNewMessages() {
        List<AnnouncementsResponse> responses = userMessageService.getNewAnnouncements();
        Map<String, Integer> summary = responses.stream()
                .collect(Collectors.groupingBy(AnnouncementsResponse::getChatId,
                        Collectors.summingInt(v -> Integer.parseInt(v.getSum()))));
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
                .flatMap(a -> userChatActivityService.findByChatIdAndOrderId(a.getChatId(), a.getOrderId()).stream())
                .peek(u -> u.setAnnounced(LocalDateTime.now()))
                .collect(Collectors.toList());
        userChatActivityService.saveAll(announcements);
    }
}
