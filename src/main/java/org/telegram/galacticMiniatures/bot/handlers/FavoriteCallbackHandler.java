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
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.ListingFavoriteService;
import org.telegram.galacticMiniatures.bot.service.ListingService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FavoriteCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
    private final ListingService listingService;
    private final CacheService cacheService;
    private final ListingFavoriteService listingFavoriteService;
    private final ListingFavoriteKeyboardMessage listingFavoriteKeyboardMessage;
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
            case Constants.KEYBOARD_FAVORITE_BUTTON_GO_BACK_COMMAND:
                SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_MAIN_MENU_HEADER);
                sm.setReplyMarkup(keyboardService.getInlineKeyboardMarkup(KeyboardType.MAIN_MENU));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(sm);
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_NEXT_COMMAND:

               optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    FavoriteInfo favoriteInfo = optionalChatInfo.get().getFavoriteInfo();
                    if (favoriteInfo != null) {
                        Pageable pageable = favoriteInfo.getFavoritePageable().next();
                        favoriteInfo.setPhotoPageable(PageRequest.of(0, 1));
                        answer.add(listingFavoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PREVIOUS_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    FavoriteInfo favoriteInfo = optionalChatInfo.get().getFavoriteInfo();
                    if (favoriteInfo != null) {
                        Pageable pageable = favoriteInfo.getFavoritePageable().previousOrFirst();
                        favoriteInfo.setPhotoPageable(PageRequest.of(0, 1));
                        answer.add(listingFavoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PHOTO_NEXT_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    FavoriteInfo favoriteInfo = optionalChatInfo.get().getFavoriteInfo();
                    if (favoriteInfo != null) {
                        Pageable pageable = favoriteInfo.getFavoritePageable();
                        Pageable pageablePhoto = favoriteInfo.getPhotoPageable().next();
                        favoriteInfo.setPhotoPageable(pageablePhoto);
                        answer.add(listingFavoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PHOTO_PREVIOUS_COMMAND:

                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    FavoriteInfo favoriteInfo = optionalChatInfo.get().getFavoriteInfo();
                    if (favoriteInfo != null) {
                        Pageable pageable = favoriteInfo.getFavoritePageable();
                        Pageable pageablePhoto = favoriteInfo.getPhotoPageable().previousOrFirst();
                        favoriteInfo.setPhotoPageable(pageablePhoto);
                        answer.add(listingFavoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    }
                }
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_REMOVE_FROM_FAVORITE_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    FavoriteInfo favoriteInfo = optionalChatInfo.get().getFavoriteInfo();
                    if (favoriteInfo != null) {
                        Pageable pageable = favoriteInfo.getFavoritePageable();
                        Page<ListingFavorite> pageFavorite =
                                listingFavoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                        ListingFavorite listingFavorite = pageFavorite.getContent().get(0);
                        listingFavoriteService.deleteByUserIdAndListingIdentifier(chatId.toString(),
                                listingFavorite.getId().getListing().getIdentifier());
                        answer.add(
                                Utils.prepareAnswerCallbackQuery(
                                        "Removed from favorites", true, callbackQuery));
                        Pageable newPageable = PageRequest.of(0,1);
                        favoriteInfo.setPhotoPageable(newPageable);
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        Page<ListingFavorite> newPageFavorite =
                                listingFavoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                        if (newPageFavorite.getTotalElements() > 0) {
                            answer.add(listingFavoriteKeyboardMessage.prepareSendPhoto(
                                    newPageable, favoriteInfo, chatId));
                        } else {
                            answer.add(keyboardService.getSendMessage(
                                    KeyboardType.MAIN_MENU, chatId, Constants.KEYBOARD_MAIN_MENU_HEADER));
                        }
                    }
                }
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_ADD_TO_CART_COMMAND:
                optionalChatInfo = cacheService.get(chatId);
                if (optionalChatInfo.isPresent()) {
                    FavoriteInfo favoriteInfo = optionalChatInfo.get().getFavoriteInfo();
                    if (favoriteInfo != null) {
                        Pageable pageable = favoriteInfo.getFavoritePageable();
                        Page<ListingFavorite> pageFavorite =
                                listingFavoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                        ListingFavorite listingFavorite = pageFavorite.getContent().get(0);
                        Listing listing = listingFavorite.getId().getListing();
                        Optional<Integer> optionalAmount = Optional.ofNullable(optionalChatInfo.get().getCart().get(listing));
                        cacheService.add(chatId, Map.of(listing,  optionalAmount.orElse(0) + 1));
                        answer.add(
                                Utils.prepareAnswerCallbackQuery("Added to cart", true, callbackQuery));
                        }
                }
                break;

            default:
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_FAVORITE_OPERATED_CALLBACK);
    }
}