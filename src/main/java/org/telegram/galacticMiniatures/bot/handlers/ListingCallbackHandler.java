package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.SearchInfo;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.KeyboardService;
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
    private final ListingWithOptionService listingWithOptionService;
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
        SearchInfo searchInfo;
        Pageable pageable;
        Page<Listing> listingPage;
        Listing listing;
        Optional<PartialBotApiMethod<?>> sendPhoto;

        switch (data) {
            case Constants.KEYBOARD_LISTING_BUTTON_GO_BACK_COMMAND:

                SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_SECTIONS_HEADER);
                sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.SECTION, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(sm);
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_NEXT_COMMAND:

                sendPhoto = listingKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.ITEM);
                answer.addAll(Utils.handleOptionalSendMessage(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_COMMAND:

                sendPhoto = listingKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.ITEM);
                answer.addAll(Utils.handleOptionalSendMessage(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_COMMAND:

                sendPhoto = listingKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.IMAGE);
                answer.addAll(Utils.handleOptionalSendMessage(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_COMMAND:

                sendPhoto = listingKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.IMAGE);
                answer.addAll(Utils.handleOptionalSendMessage(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_OPTION_NEXT_COMMAND:

                sendPhoto = listingKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.OPTION);
                answer.addAll(Utils.handleOptionalSendMessage(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_OPTION_PREVIOUS_COMMAND:

                sendPhoto = listingKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.OPTION);
                answer.addAll(Utils.handleOptionalSendMessage(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_COMMAND:

                searchInfo = cacheService.get(chatId).getSearchInfo();
                pageable = searchInfo.getItemPageable();
                listingPage =
                        listingService.findPageListingActiveBySectionIdentifier(searchInfo.getSectionId(), pageable);
                try {
                    listing = listingPage.getContent().get(0);
                    favoriteService.save(new ListingFavorite(new ListingFavorite.Key(listing,
                            userService.getUser(message))));
                    answer.add(Utils.prepareAnswerCallbackQuery(
                            "Added to favorite", true, callbackQuery));
                } catch (IndexOutOfBoundsException ex) {
                    answer.add(Utils.prepareAnswerCallbackQuery(
                            Constants.ERROR_RESTART_MENU, true, callbackQuery));
                }
                break;

            case Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_CART_COMMAND:

                searchInfo = cacheService.get(chatId).getSearchInfo();
                pageable = searchInfo.getItemPageable();
                listingPage = listingService.findPageListingActiveBySectionIdentifier(
                                searchInfo.getSectionId(), pageable);
                try {
                    listing = listingPage.getContent().get(0);
                    Pageable optionPageable = searchInfo.getOptionPageable();
                    Page<ListingWithOption> optionPage =
                            listingWithOptionService.findPageOptionByListing(listing, optionPageable);
                    ListingWithOption listingWithOption = optionPage.getContent().get(0);

                    var key = new ListingCart.Key(listing, userService.getUser(message), listingWithOption);
                    Optional<ListingCart> optionalListingCart = cartService.findById(key);
                    ListingCart listingCart = optionalListingCart.orElse(new ListingCart(key,0));
                    listingCart.setQuantity(listingCart.getQuantity() + 1);
                    cartService.save(listingCart);

                    answer.add(
                            Utils.prepareAnswerCallbackQuery("Added to cart", true, callbackQuery));
                } catch (IndexOutOfBoundsException ex) {
                    answer.add(Utils.prepareAnswerCallbackQuery(
                            Constants.ERROR_RESTART_MENU, true, callbackQuery));
                }
                break;
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_LISTING_OPERATED_CALLBACK);
    }
}