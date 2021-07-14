package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ListingKeyboardMessage implements AbstractKeyboardMessage {

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_COMMAND));
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_MIDDLE_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_MIDDLE_COMMAND));
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_NEXT_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_NEXT_COMMAND));
        rowList.add(keyboardButtonsRow1);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_CART_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_CART_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_GO_BACK_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_GO_BACK_COMMAND));
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}