package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ChatInfo;
import org.telegram.galacticMiniatures.bot.cache.SearchInfo;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.ListingKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.ListingService;
import org.telegram.galacticMiniatures.bot.service.ListingWithImageService;
import org.telegram.galacticMiniatures.bot.service.SectionService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SectionsCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
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

        if (Constants.KEYBOARD_SECTIONS_BUTTON_GO_BACK_COMMAND.equals(data)) {
            SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_MAIN_MENU_HEADER);
            sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.MAIN_MENU, chatId));
            answer.add(Utils.prepareDeleteMessage(chatId, messageId));
            answer.add(sm);
        } else {
            Integer sectionId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_SECTIONS_OPERATED_CALLBACK, ""));
            if (listingService.countSizeBySectionIdentifier(sectionId) > 0) {
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