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
import org.telegram.galacticMiniatures.bot.model.Listing;
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
    private final ListingWithImageService listingWithImageService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (data) {
            case Constants.KEYBOARD_SECTIONS_BUTTON_GO_BACK_COMMAND:
                EditMessageText backToMainMenu = new EditMessageText();
                backToMainMenu.setText(Constants.KEYBOARD_MAIN_MENU_HEADER);
                backToMainMenu.setMessageId(messageId);
                backToMainMenu.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.MAIN_MENU));
                backToMainMenu.setChatId(chatId.toString());
                answer.add(backToMainMenu);
                break;
            default:
                Integer sectionId = Integer.parseInt(
                        data.replace(Constants.KEYBOARD_SECTIONS_OPERATED_CALLBACK, ""));
                Pageable pageable = PageRequest.of(0, 1);
                Page<Listing> listingPage = listingService.getPageListingBySectionIdentifier(sectionId, pageable);
                if(listingPage.getTotalElements() > 0) {
                    cacheService.add(chatId, new SearchInfo(sectionId, listingPage.getPageable()));
                    answer.add(prepareSendPhoto(pageable, sectionId, chatId));
                    answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                } else {
                    answer.add(Utils.prepareAnswerCallbackQuery(
                            "No items in chosen category", false, callbackQuery));
                }
        }
        return answer;
    }

    private SendPhoto prepareSendPhoto(Pageable pageable, Integer sectionId, Long chatId) {

        Page<Listing> listingPage =
                listingService.getPageListingBySectionIdentifier(sectionId, pageable);
        InlineKeyboardMarkup keyboardMarkup =
                keyboardService.getInlineKeyboardMarkup(KeyboardType.LISTING);
        keyboardMarkup.getKeyboard().get(0).get(1)
                .setText(listingPage.getNumber() + 1 + " of " + listingPage.getTotalElements());
        if (listingPage.getNumber() + 1 >= listingPage.getTotalPages()) {
            keyboardMarkup.getKeyboard().get(0).remove(2);
        }
        if (listingPage.getNumber() == 0) {
            keyboardMarkup.getKeyboard().get(0).remove(0);
        }

        Listing listing = listingPage.getContent().get(0);
        List<String> pics = listingWithImageService.
                getImagesByListingIdentifier(listing.getIdentifier());
        StringBuilder sb = new StringBuilder();
        if (pics.size() > 1) {
            for (int i = 1; i < pics.size(); i++) {
                sb.append("<a href=\"")
                        .append(pics.get(i))
                        .append("\">picture ")
                        .append(i)
                        .append("</a>")
                        .append("\n");
            }
        }

        String caption = sb.append(listing.getTitle())
                .append("\nprice: ")
                .append(listing.getPrice()).toString();

        InputMediaPhoto photo = new InputMediaPhoto();
        photo.setMedia(pics.get(0));
        InputFile inputFile = new InputFile();
        inputFile.setMedia(pics.get(0));
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setCaption(caption);
        sendPhoto.setParseMode("html");
        sendPhoto.setReplyMarkup(keyboardMarkup);
        return sendPhoto;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_SECTIONS_OPERATED_CALLBACK);
    }
}
