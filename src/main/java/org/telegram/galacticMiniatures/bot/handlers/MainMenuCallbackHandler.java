package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.*;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.CartKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.FavoriteKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.ListingFavorite;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.CartService;
import org.telegram.galacticMiniatures.bot.service.FavoriteService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MainMenuCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
    private final CacheService cacheService;
    private final FavoriteService favoriteService;
    private final FavoriteKeyboardMessage favoriteKeyboardMessage;
    private final CartKeyboardMessage cartKeyboardMessage;
    private final CartService cartService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        switch (data) {
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_CATALOGUE_COMMAND:
                SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_SECTIONS_HEADER);
                sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.SECTION, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(sm);
                break;
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_CONTACTS_COMMAND:
                break;
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_FAVORITE_COMMAND:
                Pageable pageable = PageRequest.of(0, 1);
                Page<ListingFavorite> listingPage =
                        favoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                if(listingPage.getTotalElements() > 0) {
                    FavoriteInfo favoriteInfo = new FavoriteInfo();
                    answer.add(favoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                    answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                } else {
                    answer.add(Utils.prepareAnswerCallbackQuery(
                            "No items in favorites", false, callbackQuery));
                }
                break;
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_CART_COMMAND:
                if (cartService.countSizeCartByChatId(chatId) > 0) {
                    Optional<SendPhoto> sendPhoto = cartKeyboardMessage.prepareSendPhoto(
                            chatId, ScrollerType.NEW, ScrollerObjectType.LISTING);
                    sendPhoto.ifPresent(s -> {
                        answer.add(s);
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    });
                } else {
                    answer.add(Utils.prepareAnswerCallbackQuery("Cart is empty", false, callbackQuery));
                }
                break;
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_CLOSE_COMMAND:
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_MAIN_MENU_OPERATED_CALLBACK);
    }
}
