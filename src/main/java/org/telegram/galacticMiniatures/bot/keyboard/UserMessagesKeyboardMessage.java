package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.service.UserMessageService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserMessagesKeyboardMessage implements AbstractKeyboardMessage {

    private final UserMessageService userMessageService;

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {

        StringBuilder command = new StringBuilder();
        StringBuilder caption = new StringBuilder();
        int count = 0;
        List<Map<String, String>> newMessages = userMessageService.trackNewMessagesForUser(chatId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (Map<String, String> messages : newMessages) {
            command.setLength(0);
            caption.setLength(0);
            Integer sum = Integer.parseInt(messages.get("sum"));
            Integer orderId = Integer.parseInt(messages.get("orderId"));
            String sectionCallBackData = command.append(Constants.KEYBOARD_USER_MESSAGES_BUTTON_MESSAGES_COMMAND)
                    .append(orderId)
                    .toString();
            if (sum > 0) {
                caption.append(String.format("%05d", orderId))
                        .append(" [")
                        .append(sum)
                        .append("]");
            } else {
                caption.append("Order ")
                        .append(String.format("%05d", orderId));
            }
            keyboardButtonsRow.add(createInlineKeyboardButton(caption.toString(), sectionCallBackData));
            if (count % 2 == 1) {
                rowList.add(keyboardButtonsRow);
                keyboardButtonsRow = new ArrayList<>();
            }
            count++;
        }

        InlineKeyboardButton goBackButton = new InlineKeyboardButton();
        goBackButton.setText(Constants.KEYBOARD_USER_MESSAGES_BUTTON_CLOSE_NAME);
        goBackButton.setCallbackData(Constants.KEYBOARD_USER_MESSAGES_BUTTON_CLOSE_COMMAND);
        rowList.add(new ArrayList<>(List.of(goBackButton)));
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}