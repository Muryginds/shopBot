package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ModeratorOrderInfo;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.ModeratorOrdersKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.UserMessage;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.service.UserMessageService;
import org.telegram.galacticMiniatures.bot.service.UserService;
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
public class ModeratorEditOrderStatusCallbackHandler implements AbstractHandler {

    private final ModeratorOrdersKeyboardMessage moderatorOrdersKeyboardMessage;
    private final CacheService cacheService;
    private final OrderService orderService;
    private final UserService userService;
    private final UserMessageService userMessageService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        Optional<PartialBotApiMethod<?>> sendMessage;

        if (data.startsWith(Constants.KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CHANGE_STATUS_COMMAND)) {
            String result =
                    data.replace(Constants.KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CHANGE_STATUS_COMMAND, "");
            ModeratorOrderInfo moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
            Optional<Order> optional = orderService.findById(moderatorOrderInfo.getOrderId());
            if (optional.isPresent()) {
                OrderStatus status = OrderStatus.valueOf(result);
                Order order = optional.get();
                order.setStatus(status);
                orderService.save(order);
                UserMessage userMessage = new UserMessage();
                userMessage.setUser(userService.getUser(chatId));
                userMessage.setOrder(order);
                userMessage.setMessage(String.format("Order %05d status changed to %s", order.getId(), status));
                userMessageService.save(userMessage);
            }
            sendMessage = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
            answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
        } else {
            if (Constants.KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CLOSE_COMMAND.equals(data)) {
                sendMessage = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.CURRENT, ScrollerObjectType.ITEM);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
            }
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_OPERATED_CALLBACK);
    }
}