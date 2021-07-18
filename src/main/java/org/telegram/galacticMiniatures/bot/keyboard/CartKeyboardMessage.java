package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.CartInfo;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;
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
public class CartKeyboardMessage implements AbstractKeyboardMessage {

    private final CacheService cacheService;
    private final CartService cartService;
    private final ListingWithImageService listingWithImageService;

    public SendPhoto prepareSendPhoto(Pageable pageable, CartInfo cartInfo, Long chatId) {

        Page<ListingCart> listingPage =
                cartService.getPageCartByChatId(chatId.toString(), pageable);

        ListingCart listingCart = listingPage.getContent().get(0);
        Pageable imagePageable = cartInfo.getPhotoPageable();
        Page<ListingWithImage> imagePage =
                listingWithImageService.getPageImagesByListing(listingCart.getId().getListing(),
                        imagePageable);
        ListingWithImage listingWithImage = imagePage.getContent().get(0);
        Listing listing = listingWithImage.getListing();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();


/*        if (imagePage.getTotalElements() > 1) {
            if (imagePage.getNumber() > 0) {
                keyboardButtonsRow0.add(createInlineKeyboardButton(
                        Constants.KEYBOARD_CART_BUTTON_PHOTO_PREVIOUS_NAME,
                        Constants.KEYBOARD_CART_BUTTON_PHOTO_PREVIOUS_COMMAND));
            }
            keyboardButtonsRow0.add(createInlineKeyboardButton(
                    new StringBuilder().append("Photo ")
                            .append(imagePage.getNumber() + 1)
                            .append(" of ")
                            .append(imagePage.getTotalElements()).toString(),
                    Constants.KEYBOARD_CART_BUTTON_PHOTO_MIDDLE_COMMAND));
            if (imagePage.getNumber() + 1 < imagePage.getTotalPages()) {
                keyboardButtonsRow0.add(createInlineKeyboardButton(
                        Constants.KEYBOARD_CART_BUTTON_PHOTO_NEXT_NAME,
                        Constants.KEYBOARD_CART_BUTTON_PHOTO_NEXT_COMMAND));
            }
            rowList.add(keyboardButtonsRow0);
        }*/
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

        cartInfo.setCartPageable(pageable);
        cartInfo.setPhotoPageable(imagePageable);
        cacheService.add(chatId, cartInfo);

        String caption = new StringBuilder().append(listing.getTitle())
                .append(". \nPrice: ")
                .append(listing.getPrice()).toString();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(listingWithImage.getImageUrl());
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setCaption(caption);
        sendPhoto.setParseMode("html");
        sendPhoto.setReplyMarkup(keyboardMarkup);
        return sendPhoto;
    }
}