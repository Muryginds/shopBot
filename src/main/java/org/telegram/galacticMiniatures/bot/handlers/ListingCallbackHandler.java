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
import org.telegram.galacticMiniatures.bot.keyboard.ListingKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ListingCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
    private final ListingService listingService;
    private final CacheService cacheService;
    private final FavoriteService favoriteService;
    private final ListingKeyboardMessage listingKeyboardMessage;
    private final UserService userService;
    private final CartService cartService;

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
                sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.SECTION, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(sm);
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_NEXT_COMMAND:

               optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getListingPageable().next();
                        searchInfo.setPhotoPageable(PageRequest.of(0, 1));
                        answer.add(listingKeyboardMessage.prepareSendPhoto(pageable, searchInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getListingPageable().previousOrFirst();
                        searchInfo.setPhotoPageable(PageRequest.of(0, 1));
                        answer.add(listingKeyboardMessage.prepareSendPhoto(pageable, searchInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getListingPageable();
                        Pageable pageablePhoto = searchInfo.getPhotoPageable().next();
                        searchInfo.setPhotoPageable(pageablePhoto);
                        answer.add(listingKeyboardMessage.prepareSendPhoto(pageable, searchInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getListingPageable();
                        Pageable pageablePhoto = searchInfo.getPhotoPageable().previousOrFirst();
                        searchInfo.setPhotoPageable(pageablePhoto);
                        answer.add(listingKeyboardMessage.prepareSendPhoto(pageable, searchInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    SearchInfo searchInfo = optionalChatInfo.get().getSearchInfo();
                    if (searchInfo != null) {
                        Pageable pageable = searchInfo.getListingPageable();
                        Page<Listing> listingPage =
                                listingService.getPageListingBySectionIdentifier(
                                        searchInfo.getSectionId(), pageable);
                        Listing listing = listingPage.getContent().get(0);
                        favoriteService.save(
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
                        Pageable pageable = searchInfo.getListingPageable();
                        Page<Listing> listingPage =
                                listingService.getPageListingBySectionIdentifier(
                                        searchInfo.getSectionId(), pageable);
                        Listing listing = listingPage.getContent().get(0);

                        Optional<ListingCart> optionalListingCart =
                                cartService.findById(new ListingCart.Key(listing, getUser(message)));
                        ListingCart listingCart = optionalListingCart.
                                orElse(new ListingCart(new ListingCart.Key(listing, getUser(message)), 0));
                        listingCart.setQuantity(listingCart.getQuantity() + 1);
                        cartService.save(listingCart);

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

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_LISTING_OPERATED_CALLBACK);
    }
}