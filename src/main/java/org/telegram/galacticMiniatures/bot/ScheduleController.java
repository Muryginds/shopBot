package org.telegram.galacticMiniatures.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.repository.AnnouncementsResponse;
import org.telegram.galacticMiniatures.bot.service.UserMessageService;
import org.telegram.galacticMiniatures.parser.ShopParserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleController {

    private final ShopParserService shopParserService;
    private final UserMessageService userMessageService;
    private final Bot bot;

    @Scheduled(fixedRateString = "${schedule.parsePeriod}")
    private void parseInfo() {
        shopParserService.parseInfo();
    }

    @Scheduled(fixedRateString = "PT5M")
    private void announceNewMessages() {
        List<AnnouncementsResponse> response = userMessageService.getNewAnnouncements();
        //bot.execute();
    }
}
