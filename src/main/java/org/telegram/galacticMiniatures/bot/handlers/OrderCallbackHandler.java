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
public class OrderCallbackHandler implements AbstractHandler {

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
        OrderedListingsInfo orderedListingsInfo;
        int orderId;

        if (data.startsWith(Constants.KEYBOARD_ORDER_BUTTON_EDIT_COMMAND)) {

            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_ORDER_BUTTON_EDIT_COMMAND, ""));
            orderedListingsInfo = new OrderedListingsInfo(orderId);
            cacheService.add(chatId, orderedListingsInfo);
            sendMethod = orderedListingsKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW_LISTING_SCROLLER, ScrollerObjectType.LISTING);
            answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));

        }  else if (data.startsWith(Constants.KEYBOARD_ORDER_BUTTON_CANCEL_ORDER_COMMAND)) {

            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_ORDER_BUTTON_CANCEL_ORDER_COMMAND, ""));
            Optional<Order> orderOptional = orderService.findById(orderId);
            orderOptional.ifPresent(o -> {
                o.setStatus(OrderStatus.CANCELED);
                orderService.save(o);
                answer.add(Utils.prepareAnswerCallbackQuery(
                        "Order canceled", true, callbackQuery));
                answer.addAll(
                        Utils.handleOptionalSendMessage(orderKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.CURRENT, ScrollerObjectType.LISTING), callbackQuery));
            });
        } else if (data.startsWith(Constants.KEYBOARD_ORDER_BUTTON_MESSAGES_COMMAND)) {
            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_ORDER_BUTTON_MESSAGES_COMMAND, ""));
            cacheService.add(chatId, new UserOrderMessageInfo(orderId));
            sendMethod = userOrderMessageKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW_MESSAGE_SCROLLER, ScrollerObjectType.LISTING);
            answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
        } else {

                switch (data) {
                    case Constants.KEYBOARD_ORDER_BUTTON_CLOSE_COMMAND:

                        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                        break;

                    case Constants.KEYBOARD_ORDER_BUTTON_NEXT_COMMAND:

                        sendMethod = orderKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.NEXT, ScrollerObjectType.LISTING);
                        answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                        break;

                    case Constants.KEYBOARD_ORDER_BUTTON_PREVIOUS_COMMAND:

                        sendMethod = orderKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.PREVIOUS, ScrollerObjectType.LISTING);
                        answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                        break;

                    case Constants.KEYBOARD_ORDER_BUTTON_TRACK_COMMAND:

                        answer.add(Utils.prepareAnswerCallbackQuery(
                                "No track number yet", false, callbackQuery));
                        break;
            }
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_ORDER_OPERATED_CALLBACK);
    }
}