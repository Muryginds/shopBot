package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.CartInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.service.CartService;
import org.telegram.galacticMiniatures.bot.service.ListingWithImageService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CartKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final CacheService cacheService;
    private final CartService cartService;
    private final ListingWithImageService listingWithImageService;

    @Override
    public Optional<SendPhoto> prepareSendPhoto(Long chatId, ScrollerType scrollerType, ScrollerObjectType scrollerObjectType) {

        CartInfo cartInfo = cacheService.get(chatId).getCartInfo();
        Pageable listingPageable = getPageableByScrollerType(cartInfo.getListingPageable(), scrollerType);
        Page<ListingCart> listingPage = cartService.getPageCartByChatId(chatId, listingPageable);
        ListingCart listingCart = listingPage.getContent().get(0);
        Listing listing = listingCart.getId().getListing();

        String imageUrl = listingWithImageService.getImageByListingIdentifier(listing.getIdentifier()).orElse("");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        if (listingPage.getNumber() > 0) {
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_CART_BUTTON_PREVIOUS_NAME,
                    Constants.KEYBOARD_CART_BUTTON_PREVIOUS_COMMAND));
        }
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                new StringBuilder().append("Item ")
                        .append(listingPage.getNumber() + 1)
                        .append(" of ")
                        .append(listingPage.getTotalElements()).toString(),
                Constants.KEYBOARD_CART_BUTTON_MIDDLE_COMMAND));
        if (listingPage.getNumber() + 1 < listingPage.getTotalPages()) {
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_CART_BUTTON_NEXT_NAME,
                    Constants.KEYBOARD_CART_BUTTON_NEXT_COMMAND));
        }
        rowList.add(keyboardButtonsRow1);

        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ADD_MINUS_NAME,
                Constants.KEYBOARD_CART_BUTTON_ADD_MINUS_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                "Amount: " + listingCart.getQuantity().toString(),
                Constants.KEYBOARD_CART_BUTTON_ADD_AMOUNT_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ADD_PLUS_NAME,
                Constants.KEYBOARD_CART_BUTTON_ADD_PLUS_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                "Price: " + listing.getPrice() * listingCart.getQuantity(),
                Constants.KEYBOARD_CART_BUTTON_ADD_TOTAL_COMMAND));
        rowList.add(keyboardButtonsRow3);

        keyboardButtonsRow4.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ORDER_NAME,
                Constants.KEYBOARD_CART_BUTTON_ORDER_COMMAND));
        keyboardButtonsRow4.add(createInlineKeyboardButton(
                "Total cart price: " + cartService.getCartSummaryByChatId(chatId.toString()).orElse(0),
                Constants.KEYBOARD_CART_BUTTON_ADD_TOTAL_COMMAND));
        rowList.add(keyboardButtonsRow4);

        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_NAME,
                Constants.KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_NAME,
                Constants.KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_GO_BACK_NAME,
                Constants.KEYBOARD_CART_BUTTON_GO_BACK_COMMAND));
        rowList.add(keyboardButtonsRow2);
        keyboardMarkup.setKeyboard(rowList);

        cartInfo.setListingPageable(listingPageable);
        cacheService.add(chatId, cartInfo);

        String caption = new StringBuilder().append(listing.getTitle())
                .append(". \nPrice: ")
                .append(listing.getPrice()).toString();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(imageUrl);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setCaption(caption);
        sendPhoto.setParseMode("html");
        sendPhoto.setReplyMarkup(keyboardMarkup);
        return Optional.of(sendPhoto);
    }
}