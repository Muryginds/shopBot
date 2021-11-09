package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.ModeratorOrdersKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModeratorEditOrderCallbackHandler implements AbstractHandler {

    private final ModeratorOrdersKeyboardMessage moderatorOrdersKeyboardMessage;
    private final KeyboardService keyboardService;
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        Optional<PartialBotApiMethod<?>> sendMessage;

        switch (data) {
            case Constants.KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CLOSE_COMMAND:

                sendMessage = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.CURRENT, ScrollerObjectType.ITEM);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                break;

            case Constants.KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_STATUS_COMMAND:

                answer.add(keyboardService.getSendMessage(
                        KeyboardType.MODERATOR_ORDER_STATUS_CHANGE, chatId, "Edit order"));
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;

            case Constants.KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_TRACK_NUMBER_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(keyboardService.getSendMessage(KeyboardType.TRACK_NUMBER, chatId, "Track number"));
                break;
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_MODERATOR_ORDER_EDIT_OPERATED_CALLBACK);
    }
}