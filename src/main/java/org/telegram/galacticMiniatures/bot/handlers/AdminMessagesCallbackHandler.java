package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.OrderedListingsInfo;
import org.telegram.galacticMiniatures.bot.cache.UserOrderMessageInfo;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.OrderKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.OrderedListingsKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.UserOrderMessageKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminMessagesCallbackHandler implements AbstractHandler {

    private final CacheService cacheService;
    private final OrderKeyboardMessage orderKeyboardMessage;
    private final OrderedListingsKeyboardMessage orderedListingsKeyboardMessage;
    private final OrderService orderService;
    private final UserOrderMessageKeyboardMessage userOrderMessageKeyboardMessage;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Optional<PartialBotApiMethod<?>> sendMethod;
        int orderId;

        if (data.startsWith(Constants.KEYBOARD_ADMIN_MESSAGES_BUTTON_MESSAGES_COMMAND)) {
            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_ADMIN_MESSAGES_BUTTON_MESSAGES_COMMAND, ""));
            cacheService.add(chatId, new UserOrderMessageInfo(orderId));
            sendMethod = userOrderMessageKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW_MESSAGE_SCROLLER, ScrollerObjectType.LISTING);
            if (sendMethod.isPresent()) {
                answer.add(sendMethod.get());
            } else {
                answer.add(Utils.prepareAnswerCallbackQuery(Constants.ERROR_RESTART_MENU, true, callbackQuery));
            }
        } else {

                switch (data) {
                    case Constants.KEYBOARD_ADMIN_MESSAGES_BUTTON_CLOSE_COMMAND:

                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        break;
            }
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_ADMIN_MESSAGES_OPERATED_CALLBACK);
    }
}