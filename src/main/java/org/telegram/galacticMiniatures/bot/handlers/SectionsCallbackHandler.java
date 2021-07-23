package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.SearchInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.ListingKeyboardMessage;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.ListingService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SectionsCallbackHandler implements AbstractHandler {

    private final ListingService listingService;
    private final CacheService cacheService;
    private final ListingKeyboardMessage listingKeyboardMessage;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if (Constants.KEYBOARD_SECTIONS_BUTTON_CLOSE_COMMAND.equals(data)) {
            answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        } else {
            Integer sectionId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_SECTIONS_OPERATED_CALLBACK, ""));
            if (listingService.countSizeActiveBySectionIdentifier(sectionId) > 0) {
                SearchInfo searchInfo = new SearchInfo(sectionId);
                cacheService.add(chatId, searchInfo);
                Optional<SendPhoto> sendPhoto = listingKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.NEW, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
            } else {
                answer.add(Utils.prepareAnswerCallbackQuery(
                        "No items in chosen category", false, callbackQuery));
            }
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_SECTIONS_OPERATED_CALLBACK);
    }
}