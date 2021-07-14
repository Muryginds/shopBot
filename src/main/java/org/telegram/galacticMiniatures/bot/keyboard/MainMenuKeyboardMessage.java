package org.telegram.galacticMiniatures.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuKeyboardMessage implements AbstractKeyboardMessage {

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CATALOGUE_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CATALOGUE_COMMAND));
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CONTACTS_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CONTACTS_COMMAND));
        rowList.add(keyboardButtonsRow1);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_FAVORITE_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_FAVORITE_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CART_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CART_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}