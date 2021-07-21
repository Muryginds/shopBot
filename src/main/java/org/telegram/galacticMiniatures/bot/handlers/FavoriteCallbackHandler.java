package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.*;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.keyboard.FavoriteKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FavoriteCallbackHandler implements AbstractHandler {

    private final KeyboardService keyboardService;
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
        Pageable pageablePhoto;
        Page<ListingFavorite> pageFavorite;
        ListingFavorite listingFavorite;

        switch (data) {
            case Constants.KEYBOARD_FAVORITE_BUTTON_EXIT_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_NEXT_COMMAND:

                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable().next();
                favoriteInfo.setImagePageable(PageRequest.of(0, 1));
                answer.add(favoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PREVIOUS_COMMAND:

                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable().previousOrFirst();
                favoriteInfo.setImagePageable(PageRequest.of(0, 1));
                answer.add(favoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PHOTO_NEXT_COMMAND:

                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable();
                pageablePhoto = favoriteInfo.getImagePageable().next();
                favoriteInfo.setImagePageable(pageablePhoto);
                answer.add(favoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_PHOTO_PREVIOUS_COMMAND:

                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable();
                pageablePhoto = favoriteInfo.getImagePageable().previousOrFirst();
                favoriteInfo.setImagePageable(pageablePhoto);
                answer.add(favoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_REMOVE_FROM_FAVORITE_COMMAND:

                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable();
                pageFavorite = favoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                listingFavorite = pageFavorite.getContent().get(0);
                favoriteService.delete(listingFavorite);
                answer.add(
                        Utils.prepareAnswerCallbackQuery(
                                "Removed from favorites", true, callbackQuery));
                Pageable newPageable = PageRequest.of(0,1);
                favoriteInfo.setImagePageable(newPageable);
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                Page<ListingFavorite> newPageFavorite =
                        favoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                if (newPageFavorite.getTotalElements() > 0) {
                    answer.add(favoriteKeyboardMessage.prepareSendPhoto(
                            newPageable, favoriteInfo, chatId));
                } else {
                    answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                }
                break;

            case Constants.KEYBOARD_FAVORITE_BUTTON_ADD_TO_CART_COMMAND:
                favoriteInfo = cacheService.get(chatId).getFavoriteInfo();
                pageable = favoriteInfo.getListingPageable();
                pageFavorite = favoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
                listingFavorite = pageFavorite.getContent().get(0);
                Listing listing = listingFavorite.getId().getListing();
                Pageable optionPageable = favoriteInfo.getOptionPageable();
                Page<ListingWithOption> optionPage =
                        listingWithOptionService.getPageOptionByListing(listing, optionPageable);
                ListingWithOption listingWithOption = optionPage.getContent().get(0);
                Optional<ListingCart> optionalListingCart =
                        cartService.findById(
                                new ListingCart.Key(listing, getUser(message), listingWithOption));
                ListingCart listingCart = optionalListingCart.
                        orElse(new ListingCart(new ListingCart.Key(listing, getUser(message), listingWithOption),
                                0));
                listingCart.setQuantity(listingCart.getQuantity() + 1);
                cartService.save(listingCart);
                answer.add(
                        Utils.prepareAnswerCallbackQuery("Added to cart", true, callbackQuery));
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
        return List.of(Constants.KEYBOARD_FAVORITE_OPERATED_CALLBACK);
    }
}