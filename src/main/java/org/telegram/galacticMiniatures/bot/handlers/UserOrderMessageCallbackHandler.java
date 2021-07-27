package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ChatInfo;
import org.telegram.galacticMiniatures.bot.cache.UserOrderMessageInfo;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.OrderKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.UserOrderMessageKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.User;
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
public class UserOrderMessageCallbackHandler implements AbstractHandler {

    private final CacheService cacheService;
    private final UserOrderMessageKeyboardMessage userOrderMessageKeyboardMessage;
    private final OrderKeyboardMessage orderKeyboardMessage;
    private final UserMessageService userMessageService;
    private final UserService userService;
    private final OrderService orderService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {

        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        if (botApiObject instanceof CallbackQuery) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            answer.addAll(handleCallbackReply(callbackQuery));
        } else if (botApiObject instanceof Message){
            Message message = (Message) botApiObject;
            User user = userService.getUser(message);
            long chatId = message.getChatId();

            String text = message.getText();
            if (!Constants.QUERY_ADD_MESSAGE_CANCEL.equals(text)) {
                ChatInfo chatInfo = cacheService.get(chatId);
                UserOrderMessageInfo userOrderMessageInfo = chatInfo.getUserOrderMessageInfo();

                UserMessage userMessage = new UserMessage();
                Optional<Order> orderOptional = orderService.findById(userOrderMessageInfo.getOrderId());
                if (orderOptional.isPresent()) {
                    userMessage.setOrder(orderOptional.get());
                    userMessage.setUser(user);
                    userMessage.setMessage(message.getText());
                    userMessageService.save(userMessage);
                }
            }

            Optional<PartialBotApiMethod<?>> replyMessage = userOrderMessageKeyboardMessage.prepareScrollingMessage(
                    chatId, ScrollerType.NEW_MESSAGE_SCROLLER, ScrollerObjectType.LISTING);
            answer.addAll(handleOptionalAddMessage(replyMessage, message));
            user.setBotState(BotState.WORKING);
            userService.save(user);
        }
        return answer;
    }

    private List<PartialBotApiMethod<?>> handleCallbackReply(CallbackQuery callbackQuery) {

        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        Optional<PartialBotApiMethod<?>> sendMessage;

        switch (data) {
            case Constants.KEYBOARD_USER_ORDER_MESSAGE_BUTTON_GO_BACK_COMMAND:

                sendMessage = orderKeyboardMessage.
                        prepareScrollingMessage(chatId, ScrollerType.CURRENT, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                break;

            case Constants.KEYBOARD_USER_ORDER_MESSAGE_BUTTON_NEXT_COMMAND:

                sendMessage = userOrderMessageKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                break;

            case Constants.KEYBOARD_USER_ORDER_MESSAGE_BUTTON_PREVIOUS_COMMAND:

                sendMessage = userOrderMessageKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.LISTING);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                break;

            case Constants.KEYBOARD_USER_ORDER_MESSAGE_BUTTON_ADD_MESSAGE_COMMAND:

                User user = userService.getUser(message);
                user.setBotState(BotState.ADDING_ORDER_MESSAGE);
                userService.save(user);
                answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
                answer.add(Utils.prepareAnswerCallbackQuery(
                        Constants.QUERY_ADD_MESSAGE_WARNING,false, callbackQuery));
                break;
        }
        return answer;
    }

    public static List<PartialBotApiMethod<?>> handleOptionalAddMessage(Optional<PartialBotApiMethod<?>> sendMessage,
                                                                        Message message) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        if (sendMessage.isPresent()) {
            answer.add(sendMessage.get());
            answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        } else {
            answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADD_MESSAGE_WARNING));
        }
        return answer;
    }

    @Override
    public List<BotState> getOperatedBotState() {
        return List.of(BotState.ADDING_ORDER_MESSAGE);
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_USER_ORDER_MESSAGE_OPERATED_CALLBACK);
    }
}