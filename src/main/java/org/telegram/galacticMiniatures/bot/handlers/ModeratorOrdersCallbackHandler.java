package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ModeratorOrderInfo;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.ModeratorEditOrderKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.ModeratorOrdersKeyboardMessage;
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
public class ModeratorOrdersCallbackHandler implements AbstractHandler {

    private final CacheService cacheService;
    private final ModeratorOrdersKeyboardMessage moderatorOrdersKeyboardMessage;
    private final ModeratorEditOrderKeyboardMessage moderatorEditOrderKeyboardMessage;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Message message = callbackQuery.getMessage();
        Integer messageId = message.getMessageId();
        Optional<PartialBotApiMethod<?>> sendMethod;
        ModeratorOrderInfo moderatorOrderInfo;
        int orderId;

        if (data.startsWith(Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_ORDERS_COMMAND)) {
            orderId = Integer.parseInt(
                    data.replace(Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_ORDERS_COMMAND, ""));
            moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
            moderatorOrderInfo.setOrderId(orderId);
            SendMessage sendMessage = moderatorEditOrderKeyboardMessage.prepareKeyboardMessage(chatId, "Orders");
            answer.add(sendMessage);
            answer.add(Utils.prepareDeleteMessage(chatId, messageId));

        } else {

            switch (data) {
                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_CLOSE_COMMAND:

                    answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_NEXT_COMMAND:

                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEXT, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_PREVIOUS_COMMAND:

                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.PREVIOUS, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_CONFIRMED_COMMAND:

                    moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
                    moderatorOrderInfo.setOrderStatusList(List.of(OrderStatus.CONFIRMED));
                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_PAID_COMMAND:

                    moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
                    moderatorOrderInfo.setOrderStatusList(List.of(OrderStatus.PAID));
                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;


                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_PRINTING_COMMAND:

                    moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
                    moderatorOrderInfo.setOrderStatusList(List.of(OrderStatus.PRINTING));
                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_NEW_COMMAND:

                    moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
                    moderatorOrderInfo.setOrderStatusList(List.of(OrderStatus.NEW));
                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_CANCELED_COMMAND:

                    moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
                    moderatorOrderInfo.setOrderStatusList(List.of(OrderStatus.CANCELED));
                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_SHIPPED_COMMAND:

                    moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
                    moderatorOrderInfo.setOrderStatusList(List.of(OrderStatus.SHIPPED));
                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;

                case Constants.KEYBOARD_MODERATOR_ORDERS_BUTTON_ALL_STATUSES_COMMAND:

                    List<OrderStatus> allStatuses = List.of(OrderStatus.NEW,
                            OrderStatus.CANCELED,
                            OrderStatus.PRINTING,
                            OrderStatus.CONFIRMED,
                            OrderStatus.SHIPPED,
                            OrderStatus.PAID);
                    moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
                    moderatorOrderInfo.setOrderStatusList(allStatuses);
                    sendMethod = moderatorOrdersKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMethod, callbackQuery));
                    break;
            }
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_MODERATOR_ORDERS_OPERATED_CALLBACK);
    }
}