package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.CartInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.model.ListingWithOption;
import org.telegram.galacticMiniatures.bot.service.CartService;
import org.telegram.galacticMiniatures.bot.service.ListingWithImageService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final CacheService cacheService;
    private final CartService cartService;
    private final ListingWithImageService listingWithImageService;

    @Override
    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        CartInfo cartInfo = cacheService.get(chatId).getCartInfo();
        Pageable listingPageable = getPageableByScrollerType(cartInfo.getItemPageable(), scrollerType);

        Page<ListingCart> listingPage = cartService.findPageCartByChatId(chatId, listingPageable);

        ListingCart listingCart;
        try {
            listingCart = listingPage.getContent().get(0);
        } catch (IndexOutOfBoundsException ex) {
            log.error("ListingCart for ChatId: " + chatId + " not found. " + ex.getMessage());
            return Optional.empty();
        }

        Listing listing = listingCart.getId().getListing();
        String imageUrl = listingWithImageService.
                findAnyImageByListingIdentifier(listing.getIdentifier()).orElse("");
        ListingWithOption listingWithOption = listingCart.getId().getOption();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        if(listingPage.getTotalPages() > 1) {

            String listingPreviousCommand = Constants.KEYBOARD_CART_OPERATED_CALLBACK;
            if (listingPage.getNumber() > 0) {
                listingPreviousCommand = Constants.KEYBOARD_CART_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_CART_BUTTON_PREVIOUS_NAME, listingPreviousCommand));

            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(listingPage.getNumber() + 1)
                            .append(" / ")
                            .append(listingPage.getTotalPages()).toString(),
                    Constants.KEYBOARD_CART_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_CART_OPERATED_CALLBACK;
            if (listingPage.getNumber() + 1 < listingPage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_CART_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_CART_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }

        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ADD_MINUS_NAME,
                Constants.KEYBOARD_CART_BUTTON_ADD_MINUS_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                listingCart.getQuantity().toString(),
                Constants.KEYBOARD_CART_OPERATED_CALLBACK));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ADD_PLUS_NAME,
                Constants.KEYBOARD_CART_BUTTON_ADD_PLUS_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                "Price:",
                Constants.KEYBOARD_CART_OPERATED_CALLBACK));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                String.format("%d", listingWithOption.getPrice() * listingCart.getQuantity()),
                Constants.KEYBOARD_CART_OPERATED_CALLBACK));
        rowList.add(keyboardButtonsRow3);

        keyboardButtonsRow4.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ORDER_NAME,
                Constants.KEYBOARD_CART_BUTTON_ORDER_COMMAND));
        keyboardButtonsRow4.add(createInlineKeyboardButton(
                "Total: " + cartService.getCartSummaryByChatId(chatId).orElse(0),
                Constants.KEYBOARD_CART_OPERATED_CALLBACK));
        rowList.add(keyboardButtonsRow4);

        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_NAME,
                Constants.KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_NAME,
                Constants.KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_CART_BUTTON_EXIT_NAME,
                Constants.KEYBOARD_CART_BUTTON_EXIT_COMMAND));
        rowList.add(keyboardButtonsRow2);
        keyboardMarkup.setKeyboard(rowList);

        cartInfo.setItemPageable(listingPageable);
        cacheService.add(chatId, cartInfo);

        StringBuilder optionsText = new StringBuilder();
        if (!"".equals(listingWithOption.getFirstOptionName())) {
            optionsText
                .append("<b>")
                .append("\n\n")
                .append("[")
                .append(listingWithOption.getFirstOptionName())
                .append(": ")
                .append(listingWithOption.getFirstOptionValue())
                .append("]");

            if (!"".equals(listingWithOption.getSecondOptionName())) {
                optionsText.append(", ")
                    .append("[")
                    .append(listingWithOption.getSecondOptionName())
                    .append(": ")
                    .append(listingWithOption.getSecondOptionValue())
                    .append("]");
            }
            optionsText.append("</b>");
        }

        StringBuilder caption = new StringBuilder()
                .append(listing.getTitle())
                .append(optionsText)
                .append("\n\n<b>Price: ")
                .append(listingWithOption.getPrice())
                .append("</b>");

        InputFile inputFile = new InputFile();
        inputFile.setMedia(imageUrl);
        return getSendPhoto(chatId, keyboardMarkup, caption, inputFile);
    }
}