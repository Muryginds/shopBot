package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.*;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.FavoriteKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FavoriteCallbackHandler implements AbstractHandler {

    private final CacheService cacheService;
    private final FavoriteService favoriteService;
    private final FavoriteKeyboardMessage favoriteKeyboardMessage;
    private final ListingWithOptionService listingWithOptionService;
    private final CartService cartService;
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        FavoriteInfo favoriteInfo;
        Pageable pageable;
        Page<ListingFavorite> pageFavorite;
        ListingFavorite listingFavorite;
        Optional<SendPhoto> sendPhoto;

        switch (data) {
            case Constants.KEYBOARD_FAVORITE_BUTTON_EXIT_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_NEXT_COMMAND:

                sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PREVIOUS_COMMAND:

                sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PHOTO_NEXT_COMMAND:

                sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.IMAGE);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PHOTO_PREVIOUS_COMMAND:

                sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.IMAGE);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_OPTION_NEXT_COMMAND:

                sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.OPTION);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_OPTION_PREVIOUS_COMMAND:

                sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.OPTION);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_REMOVE_FROM_FAVORITE_COMMAND:

                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable();
                pageFavorite = favoriteService.findPageFavoriteByChatId(chatId, pageable);

                try {
                    listingFavorite = pageFavorite.getContent().get(0);
                    favoriteService.delete(listingFavorite);
                    answer.add(
                            Utils.prepareAnswerCallbackQuery(
                                    "Removed from favorites", true, callbackQuery));

                    if (favoriteService.countSizeFavoriteByChatId(chatId) > 0) {
                        sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                                chatId, ScrollerType.NEW, ScrollerObjectType.LISTING);
                        answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                    } else {
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_STARTER_FAVORITES_EMPTY));
                    }
                } catch (IndexOutOfBoundsException ex) {
                    answer.add(Utils.prepareAnswerCallbackQuery(
                            Constants.ERROR_RESTART_MENU, true, callbackQuery));
                }
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_ADD_TO_CART_COMMAND:

                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable();
                pageFavorite = favoriteService.findPageFavoriteByChatId(chatId, pageable);
                try {
                    listingFavorite = pageFavorite.getContent().get(0);
                    Listing listing = listingFavorite.getId().getListing();
                    Pageable optionPageable = favoriteInfo.getOptionPageable();
                    Page<ListingWithOption> optionPage = listingWithOptionService.
                            findPageOptionByListing(listing, optionPageable);
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
        return List.of(Constants.KEYBOARD_FAVORITE_OPERATED_CALLBACK);
    }
}