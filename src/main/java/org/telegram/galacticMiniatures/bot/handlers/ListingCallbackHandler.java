package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ChatInfo;
import org.telegram.galacticMiniatures.bot.cache.SearchInfo;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ListingCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
    private final ListingService listingService;
    private final CacheService cacheService;
    private final ListingWithImageService listingWithImageService;
    private final ListingFavoriteService listingFavoriteService;
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        Optional<ChatInfo> optionalChatInfo;

        switch (data) {
            case Constants.KEYBOARD_LISTING_BUTTON_GO_BACK_COMMAND:
                SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_SECTIONS_HEADER);
                sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.SECTION));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(sm);
                break;
            case Constants.KEYBOARD_LISTING_BUTTON_NEXT_COMMAND:

               optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getPageable().next();
                        answer.add(prepareSendPhoto(pageable, searchInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getPageable().previousOrFirst();
                        answer.add(prepareSendPhoto(pageable, searchInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getPageable();
                        Page<Listing> listingPage =
                                listingService.getPageListingBySectionIdentifier(
                                        searchInfo.getSectionId(), pageable);
                        Listing listing = listingPage.getContent().get(0);
                        listingFavoriteService.save(
                                new ListingFavorite(
                                        new ListingFavorite.Key(listing, getUser(message))));
                        answer.add(
                                Utils.prepareAnswerCallbackQuery("Added to favorite", true, callbackQuery));
                    }
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_CART_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getPageable();
                        Page<Listing> listingPage =
                                listingService.getPageListingBySectionIdentifier(
                                        searchInfo.getSectionId(), pageable);
                        Listing listing = listingPage.getContent().get(0);
                        cacheService.add(chatId, Map.of(listing,1));
                        answer.add(
                                Utils.prepareAnswerCallbackQuery("Added to cart", true, callbackQuery));
                    }
                }
                break;

            default:
        }
        return answer;
    }

    private User getUser(Message message) {
        return userService.getUser(message.getChatId())
                .orElseGet(() -> userService.add(new User(message)));
    }

    private SendPhoto prepareSendPhoto(Pageable pageable, SearchInfo searchInfo, Long chatId) {

        Page<Listing> listingPage =
                listingService.getPageListingBySectionIdentifier(
                        searchInfo.getSectionId(), pageable);
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
        searchInfo.setPageable(pageable);
        cacheService.add(chatId, searchInfo);
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
        return List.of(Constants.KEYBOARD_LISTING_OPERATED_CALLBACK);
    }
}
