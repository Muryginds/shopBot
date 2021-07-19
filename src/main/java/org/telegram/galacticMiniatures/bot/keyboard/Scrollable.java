package org.telegram.galacticMiniatures.bot.keyboard;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.Optional;

public interface Scrollable {

    Optional<SendPhoto> prepareSendPhoto(Long chatId, ScrollerType scrollerType, ScrollerObjectType scrollerObjectType);

    default Pageable getPageableByScrollerType(Pageable pageable, ScrollerType scrollerType) {
        Pageable newPageable;
        switch (scrollerType) {
            case PREVIOUS:
                newPageable = pageable.previousOrFirst();
                break;
            case NEXT:
                newPageable = pageable.next();
                break;
            case CURRENT:
                newPageable = pageable;
            default:
                newPageable = PageRequest.of(0,1);
        }
        return newPageable;
    }
}