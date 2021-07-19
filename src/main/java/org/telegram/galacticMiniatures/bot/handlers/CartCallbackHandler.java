package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.*;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.CartKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.model.ListingFavorite;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
    private final CacheService cacheService;
    private final FavoriteService favoriteService;
    private final CartKeyboardMessage cartKeyboardMessage;
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
        CartInfo cartInfo;
        Pageable pageable;
        Page<ListingCart> pageCart;
        ListingCart listingCart;
        Optional<SendPhoto> sendPhoto;

        switch (data) {
            case Constants.KEYBOARD_CART_BUTTON_GO_BACK_COMMAND:
                SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_MAIN_MENU_HEADER);
                sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.MAIN_MENU, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(sm);
                break;

            case Constants.KEYBOARD_CART_BUTTON_NEXT_COMMAND:
                sendPhoto = cartKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_CART_BUTTON_PREVIOUS_COMMAND:
                sendPhoto = cartKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_COMMAND:
                cartInfo = cacheService.get(chatId).getCartInfo();
                pageable = cartInfo.getListingPageable();
                pageCart = cartService.getPageCartByChatId(chatId, pageable);
                listingCart = pageCart.getContent().get(0);
                cartService.delete(listingCart);
                answer.add(
                        Utils.prepareAnswerCallbackQuery(
                                "Removed from cart", true, callbackQuery));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                Page<ListingCart> newPageCart =
                        cartService.getPageCartByChatId(chatId, pageable);
                if (newPageCart.getTotalElements() > 0) {
                    sendPhoto = cartKeyboardMessage.prepareSendPhoto(
                            chatId, ScrollerType.NEW, ScrollerObjectType.LISTING);
                    sendPhoto.ifPresent(answer::add);
                } else {
                    answer.add(keyboardService.getSendMessage(
                            KeyboardType.MAIN_MENU, chatId, Constants.KEYBOARD_MAIN_MENU_HEADER));
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_COMMAND:
                cartInfo = cacheService.get(chatId).getCartInfo();
                pageable = cartInfo.getListingPageable();
                pageCart = cartService.getPageCartByChatId(chatId, pageable);
                listingCart = pageCart.getContent().get(0);
                Listing listing = listingCart.getId().getListing();
                favoriteService.save(
                        new ListingFavorite(
                                new ListingFavorite.Key(listing, getUser(message))));
                answer.add(
                        Utils.prepareAnswerCallbackQuery("Added to favorite", true, callbackQuery));
                break;

            case Constants.KEYBOARD_CART_BUTTON_ADD_PLUS_COMMAND:
                cartInfo = cacheService.get(chatId).getCartInfo();
                pageable = cartInfo.getListingPageable();
                pageCart = cartService.getPageCartByChatId(chatId, pageable);
                listingCart = pageCart.getContent().get(0);
                listingCart.setQuantity(listingCart.getQuantity() + 1);
                cartService.save(listingCart);
                sendPhoto = cartKeyboardMessage.prepareSendPhoto(
                        chatId, ScrollerType.CURRENT, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendPhoto(sendPhoto, callbackQuery));
                break;

            case Constants.KEYBOARD_CART_BUTTON_ADD_MINUS_COMMAND:
                cartInfo = cacheService.get(chatId).getCartInfo();
                pageable = cartInfo.getListingPageable();
                pageCart = cartService.getPageCartByChatId(chatId, pageable);
                listingCart = pageCart.getContent().get(0);
                int newQuantity = listingCart.getQuantity() - 1;
                if (newQuantity == 0) {
                    cartService.delete(listingCart);
                } else {
                    listingCart.setQuantity(newQuantity);
                    cartService.save(listingCart);
                }
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));

                if (pageCart.getTotalPages() - 1 == 0 && newQuantity == 0) {
                    SendMessage mainMenuMessage = Utils.prepareSendMessage(
                            chatId, Constants.KEYBOARD_MAIN_MENU_HEADER);
                    mainMenuMessage.setReplyMarkup(
                            keyboardService.getInlineKeyboardMarkup(KeyboardType.MAIN_MENU, chatId));
                    answer.add(mainMenuMessage);
                    answer.add(
                            Utils.prepareAnswerCallbackQuery("Cart is empty", false, callbackQuery));
                } else {
                    sendPhoto = cartKeyboardMessage.prepareSendPhoto(
                            chatId, ScrollerType.NEW, ScrollerObjectType.LISTING);
                    sendPhoto.ifPresent(answer::add);
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
        return List.of(Constants.KEYBOARD_CART_OPERATED_CALLBACK);
    }
}