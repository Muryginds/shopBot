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
public class AddressKeyboardMessage implements AbstractKeyboardMessage {

    private final UserService userService;
    private final UserInfoService userInfoService;

    @Override
    public SendMessage prepareKeyboardMessage(Long chatId, String text) {

        User user = userService.findUser(chatId).orElse(new User(chatId.toString(), chatId.toString()));
        Optional<UserInfo> userInfoOptional = userInfoService.findByUser(user);
        if (userInfoOptional.isPresent()) {
            UserInfo userInfo = userInfoOptional.get();
            text = new StringBuilder()
                    .append("Full name: ")
                    .append(userInfo.getFullName())
                    .append("\nTown: ")
                    .append(userInfo.getTown())
                    .append("\nAddress: ")
                    .append(userInfo.getAddress())
                    .append("\nPost index: ")
                    .append(userInfo.getPostIndex())
                    .toString();
        }
        SendMessage message = Utils.prepareSendMessage(chatId, text);
        message.setReplyMarkup(formKeyboard(chatId));
        return message;
    }

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(createInlineKeyboardButton(Constants.KEYBOARD_ADDRESS_BUTTON_EDIT_NAME,
                Constants.KEYBOARD_ADDRESS_BUTTON_EDIT_COMMAND));
        keyboardButtonsRow.add(createInlineKeyboardButton(Constants.KEYBOARD_ADDRESS_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_ADDRESS_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}