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
public class AdminPanelKeyboardMessage implements AbstractKeyboardMessage {

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow0.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_ADMIN_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_ADMIN_COMMAND));
        keyboardButtonsRow0.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_ADMIN_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_ADMIN_COMMAND));
        rowList.add(keyboardButtonsRow0);
        keyboardButtonsRow1.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_MODERATOR_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_MODERATOR_COMMAND));
        keyboardButtonsRow1.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_MODERATOR_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_MODERATOR_COMMAND));
        rowList.add(keyboardButtonsRow1);
        keyboardButtonsRow2.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_CLOSE_MENU_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_CLOSE_MENU_COMMAND));
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}