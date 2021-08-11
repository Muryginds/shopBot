package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.OrderedListingsInfo;
import org.telegram.galacticMiniatures.bot.cache.OrderMessageInfo;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.UserOrderKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.OrderedListingsKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.UserOrderMessageKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.service.UserMessageService;
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
public class UserOrderCallbackHandler implements AbstractHandler {

    private final CacheService cacheService;
    private final UserOrderKeyboardMessage userOrderKeyboardMessage;
    private final OrderedListingsKeyboardMessage orderedListingsKeyboardMessage;
    private final OrderService orderService;
    private final UserMessageService userMessageService;
    private final UserOrderMessageKeyboardMessage userOrderMessageKeyboardMessage;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Message message = callbackQuery.getMessage();
        Integer messageId = message.getMessageId();
        Optional<PartialBotApiMethod<?>> sendMethod;
        OrderedListingsInfo orderedListingsInfo;
        int orderId;

        if (data.startsWith(Constants.KEYBOARD_USER_ORDER_BUTTON_EDIT_COMMAND)) {

            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_USER_ORDER_BUTTON_EDIT_COMMAND, ""));
            orderedListingsInfo = new OrderedListingsInfo(orderId);
            cacheService.add(chatId, orderedListingsInfo);
            sendMethod = orderedListingsKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
            answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));

        }  else if (data.startsWith(Constants.KEYBOARD_USER_ORDER_BUTTON_CANCEL_ORDER_COMMAND)) {

            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_USER_ORDER_BUTTON_CANCEL_ORDER_COMMAND, ""));
            Optional<Order> orderOptional = orderService.findById(orderId);
            orderOptional.ifPresent(o -> {
                o.setStatus(OrderStatus.CANCELED);
                orderService.save(o);
                userMessageService.announceOrderStatusChanged(chatId, o);
                answer.add(Utils.prepareAnswerCallbackQuery(
                        "Order canceled", true, callbackQuery));
                answer.addAll(
                        Utils.handleOptionalSendMessage(userOrderKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.CURRENT, ScrollerObjectType.ITEM), callbackQuery));
            });
        } else if (data.startsWith(Constants.KEYBOARD_USER_ORDER_BUTTON_MESSAGES_COMMAND)) {
            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_USER_ORDER_BUTTON_MESSAGES_COMMAND, ""));
            cacheService.add(chatId, new OrderMessageInfo(orderId));
            sendMethod = userOrderMessageKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
            answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
        } else {

                switch (data) {
                    case Constants.KEYBOARD_USER_ORDER_BUTTON_CLOSE_COMMAND:

                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        break;

                    case Constants.KEYBOARD_USER_ORDER_BUTTON_NEXT_COMMAND:

                        sendMethod = userOrderKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.NEXT, ScrollerObjectType.ITEM);
                        answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                        break;

                    case Constants.KEYBOARD_USER_ORDER_BUTTON_PREVIOUS_COMMAND:

                        sendMethod = userOrderKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.PREVIOUS, ScrollerObjectType.ITEM);
                        answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                        break;

                    case Constants.KEYBOARD_USER_ORDER_BUTTON_TRACK_COMMAND:

                        answer.add(Utils.prepareAnswerCallbackQuery(
                                "No track number yet", false, callbackQuery));
                        break;
            }
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_USER_ORDER_OPERATED_CALLBACK);
    }
}