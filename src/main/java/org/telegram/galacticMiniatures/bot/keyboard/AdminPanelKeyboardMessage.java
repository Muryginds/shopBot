package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.model.UserInfo;
import org.telegram.galacticMiniatures.bot.service.UserInfoService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminPanelKeyboardMessage implements AbstractKeyboardMessage {

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow0.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_USER_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_USER_COMMAND));
        keyboardButtonsRow0.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_USER_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_USER_COMMAND));
        rowList.add(keyboardButtonsRow0);
        keyboardButtonsRow1.add(createInlineKeyboardButton(Constants.KEYBOARD_ADMIN_BUTTON_CLOSE_MENU_NAME,
                Constants.KEYBOARD_ADMIN_BUTTON_CLOSE_MENU_COMMAND));
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}