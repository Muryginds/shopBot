package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ChatInfo;
import org.telegram.galacticMiniatures.bot.cache.FavoriteInfo;
import org.telegram.galacticMiniatures.bot.cache.SearchInfo;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.keyboard.ListingFavoriteKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingFavorite;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.ListingFavoriteService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MainMenuCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
    private final CacheService cacheService;
    private final ListingFavoriteService listingFavoriteService;
    private final ListingFavoriteKeyboardMessage listingFavoriteKeyboardMessage;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        switch (data) {
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_CATALOGUE_COMMAND:
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setText(Constants.KEYBOARD_SECTIONS_HEADER);
                editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
                editMessageText.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.SECTION));
                editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
                answer.add(editMessageText);
                break;
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_CONTACTS_COMMAND:
                break;
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_FAVORITE_COMMAND:
                Pageable pageable = PageRequest.of(0, 1);
                Pageable imagePageable = PageRequest.of(0, 1);
                Page<ListingFavorite> listingPage =
                        listingFavoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                if(listingPage.getTotalElements() > 0) {
                    FavoriteInfo favoriteInfo = new FavoriteInfo(pageable, imagePageable);
                    cacheService.add(chatId, favoriteInfo);
                    answer.add(listingFavoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                    answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                } else {
                    answer.add(Utils.prepareAnswerCallbackQuery(
                            "No items in favorites", false, callbackQuery));
                }
                break;
            case Constants.KEYBOARD_MAIN_MENU_BUTTON_CART_COMMAND:
                boolean cartIsEmpty = true;
                Optional<ChatInfo> optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    Map<Listing, Integer> cart = optionalChatInfo.get().getCart();
                    cartIsEmpty = cart.isEmpty();
                    if (!cartIsEmpty) {
                        answer.add(Utils.prepareSendMessage(chatId, cart.toString()));
                    }
                }
                if (cartIsEmpty) {
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
