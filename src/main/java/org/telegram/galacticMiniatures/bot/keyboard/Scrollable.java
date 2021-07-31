package org.telegram.galacticMiniatures.bot.keyboard;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Optional;

public interface Scrollable {

    Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId, ScrollerType scrollerType,
                                                             ScrollerObjectType scrollerObjectType);

    default Pageable getPageableByScrollerType(Pageable pageable, ScrollerType scrollerType) {
        Pageable newPageable;
        switch (scrollerType) {
            case PREVIOUS:
                newPageable = pageable.previousOrFirst();
                break;
            case NEXT:
                newPageable = pageable.next();
                break;
            case NEW:
                newPageable = PageRequest.of(0,pageable.getPageSize(), pageable.getSort());
                break;
            default:
                newPageable = pageable;
        }
        return newPageable;
    }

    default Optional<PartialBotApiMethod<?>> getSendPhoto(
            Long chatId, InlineKeyboardMarkup keyboardMarkup, StringBuilder optionsText, InputFile inputFile) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setCaption(optionsText.toString());
        sendPhoto.setParseMode("html");
        sendPhoto.setReplyMarkup(keyboardMarkup);
        return Optional.of(sendPhoto);
    }
}