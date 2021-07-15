package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.*;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
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
        Optional<ChatInfo> optionalChatInfo;

        switch (data) {
            case Constants.KEYBOARD_CART_BUTTON_GO_BACK_COMMAND:
                SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_MAIN_MENU_HEADER);
                sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.MAIN_MENU));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(sm);
                break;

            case Constants.KEYBOARD_CART_BUTTON_NEXT_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable().next();
                        cartInfo.setPhotoPageable(PageRequest.of(0, 1));
                        answer.add(cartKeyboardMessage.prepareSendPhoto(pageable, cartInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_PREVIOUS_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable().previousOrFirst();
                        cartInfo.setPhotoPageable(PageRequest.of(0, 1));
                        answer.add(cartKeyboardMessage.prepareSendPhoto(pageable, cartInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_PHOTO_NEXT_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable();
                        Pageable pageablePhoto = cartInfo.getPhotoPageable().next();
                        cartInfo.setPhotoPageable(pageablePhoto);
                        answer.add(cartKeyboardMessage.prepareSendPhoto(pageable, cartInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_PHOTO_PREVIOUS_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable();
                        Pageable pageablePhoto = cartInfo.getPhotoPageable().previousOrFirst();
                        cartInfo.setPhotoPageable(pageablePhoto);
                        answer.add(cartKeyboardMessage.prepareSendPhoto(pageable, cartInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable();
                        Page<ListingCart> pageCart =
                                cartService.getPageCartByChatId(chatId.toString(), pageable);
                        ListingCart listingCart = pageCart.getContent().get(0);
                        cartService.delete(listingCart);
                        answer.add(
                                Utils.prepareAnswerCallbackQuery(
                                        "Removed from cart", true, callbackQuery));
                        Pageable newPageable = PageRequest.of(0,1);
                        cartInfo.setPhotoPageable(newPageable);
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        Page<ListingCart> newPageCart =
                                cartService.getPageCartByChatId(chatId.toString(), pageable);
                        if (newPageCart.getTotalElements() > 0) {
                            answer.add(cartKeyboardMessage.prepareSendPhoto(
                                    newPageable, cartInfo, chatId));
                        } else {
                            answer.add(keyboardService.getSendMessage(
                                    KeyboardType.MAIN_MENU, chatId, Constants.KEYBOARD_MAIN_MENU_HEADER));
                        }
                    }
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable();
                        Page<ListingCart> pageCart =
                                cartService.getPageCartByChatId(chatId.toString(), pageable);
                        ListingCart listingCart = pageCart.getContent().get(0);
                        Listing listing = listingCart.getId().getListing();
                        favoriteService.save(
                                new ListingFavorite(
                                        new ListingFavorite.Key(listing, getUser(message))));
                        answer.add(
                                Utils.prepareAnswerCallbackQuery("Added to favorite", true, callbackQuery));
                    }
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_ADD_PLUS_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable();
                        Page<ListingCart> pageCart =
                                cartService.getPageCartByChatId(chatId.toString(), pageable);
                        ListingCart listingCart = pageCart.getContent().get(0);
                        listingCart.setQuantity(listingCart.getQuantity() + 1);
                        cartService.save(listingCart);
                        answer.add(cartKeyboardMessage.prepareSendPhoto(pageable, cartInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_CART_BUTTON_ADD_MINUS_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    CartInfo cartInfo = optionalChatInfo.get().getCartInfo();
                    if (cartInfo != null) {
                        Pageable pageable = cartInfo.getCartPageable();
                        Page<ListingCart> pageCart =
                                cartService.getPageCartByChatId(chatId.toString(), pageable);
                        ListingCart listingCart = pageCart.getContent().get(0);
                        int newQuantity = listingCart.getQuantity() - 1;
                        if (newQuantity == 0) {
                            cartService.delete(listingCart);
                            pageable = PageRequest.of(0,1);
                        } else {
                            listingCart.setQuantity(newQuantity);
                            cartService.save(listingCart);
                        }
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        if (pageCart.getTotalPages() - 1 == 0 && newQuantity == 0) {
                            SendMessage mainMenuMessage = Utils.prepareSendMessage(
                                    chatId, Constants.KEYBOARD_MAIN_MENU_HEADER);
                            mainMenuMessage.setReplyMarkup(
                                    keyboardService.getInlineKeyboardMarkup(KeyboardType.MAIN_MENU));
                            answer.add(mainMenuMessage);
                            answer.add(
                                    Utils.prepareAnswerCallbackQuery("Cart is empty", false, callbackQuery));
                        } else {
                            answer.add(cartKeyboardMessage.prepareSendPhoto(pageable, cartInfo, chatId));
                        }
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
        return List.of(Constants.KEYBOARD_CART_OPERATED_CALLBACK);
    }
}