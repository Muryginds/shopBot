package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.OrderMessageInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.UserMessageScrollerKeyboardMessage;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMessagesCallbackHandler implements AbstractHandler {

    private final CacheService cacheService;
    private final UserMessageScrollerKeyboardMessage userMessageScrollerKeyboardMessage;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Message message = callbackQuery.getMessage();
        Integer messageId = message.getMessageId();
        Optional<PartialBotApiMethod<?>> sendMethod;
        int orderId;

        if (data.startsWith(Constants.KEYBOARD_USER_MESSAGES_BUTTON_MESSAGES_COMMAND)) {
            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_MESSAGES_COMMAND, ""));
            cacheService.add(chatId, new OrderMessageInfo(orderId));
            sendMethod = userMessageScrollerKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW_MESSAGE_SCROLLER, ScrollerObjectType.ITEM);
            answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
        } else {

                switch (data) {
                    case Constants.KEYBOARD_USER_MESSAGES_BUTTON_CLOSE_COMMAND:
                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        break;
            }
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_USER_MESSAGES_OPERATED_CALLBACK);
    }
}