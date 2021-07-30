package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.OrderedListingsInfo;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.OrderKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.OrderedListingsKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.service.*;
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
public class OrderedListingCallbackHandler implements AbstractHandler {

    private final CacheService cacheService;
    private final OrderService orderService;
    private final OrderedListingService orderedListingService;
    private final OrderKeyboardMessage orderKeyboardMessage;
    private final OrderedListingsKeyboardMessage orderedListingsKeyboardMessage;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        int orderId;
        Order order;
        OrderedListingsInfo orderedListingsInfo;
        Pageable pageable;
        Page<OrderedListing> orderedListingsPage;
        OrderedListing orderedListing;
        Optional<PartialBotApiMethod<?>> sendMessage;

        switch (data) {
            case Constants.KEYBOARD_ORDEREDLISTING_BUTTON_GO_BACK_COMMAND:

                sendMessage = orderKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.CURRENT, ScrollerObjectType.ITEM);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                break;

            case Constants.KEYBOARD_ORDEREDLISTING_BUTTON_NEXT_COMMAND:

                sendMessage = orderedListingsKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.NEXT, ScrollerObjectType.ITEM);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                break;

            case Constants.KEYBOARD_ORDEREDLISTING_BUTTON_PREVIOUS_COMMAND:

                sendMessage = orderedListingsKeyboardMessage.prepareScrollingMessage(
                        chatId, ScrollerType.PREVIOUS, ScrollerObjectType.ITEM);
                answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                break;

            case Constants.KEYBOARD_ORDEREDLISTING_BUTTON_REMOVE_FROM_ORDER_COMMAND:

                orderedListingsInfo = cacheService.get(chatId).getOrderedListingsInfo();
                pageable = orderedListingsInfo.getListingPageable();
                orderId = orderedListingsInfo.getOrderId();
                orderedListingsPage =
                        orderedListingService.findPageByOrderId(orderId, pageable);
                orderedListing = orderedListingsPage.getContent().get(0);
                order = orderedListing.getId().getOrder();
                if (order.getStatus().equals(OrderStatus.CREATED)) {
                    orderedListingService.delete(orderedListing);

                    answer.add(
                            Utils.prepareAnswerCallbackQuery("Removed from order", true, callbackQuery));

                    order.setSummary(orderedListingService.getOrderSummary(orderId).orElse(0));
                    orderedListingsPage =
                            orderedListingService.findPageByOrderId(orderId, pageable);
                    if (orderedListingsPage.getTotalElements() > 0) {

                        sendMessage = orderedListingsKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.NEW_LISTING_SCROLLER, ScrollerObjectType.ITEM);
                    } else {
                        answer.add(Utils.prepareAnswerCallbackQuery(
                                Constants.KEYBOARD_ORDEREDLISTING_MESSAGE_ORDER_IS_EMPTY, true, callbackQuery));
                        order.setStatus(OrderStatus.CANCELED);
                        sendMessage = orderKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.NEW_LISTING_SCROLLER, ScrollerObjectType.ITEM);
                    }
                    orderService.save(order);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, new StringBuilder()
                            .append("Order is ")
                            .append(order.getStatus())
                            .append(" you can not modify it").toString()
                    ));
                }
                break;

            case Constants.KEYBOARD_ORDEREDLISTING_BUTTON_ADD_PLUS_COMMAND:

                orderedListingsInfo = cacheService.get(chatId).getOrderedListingsInfo();
                orderId = orderedListingsInfo.getOrderId();
                pageable = orderedListingsInfo.getListingPageable();
                orderedListingsPage = orderedListingService.findPageByOrderId(orderId, pageable);
                orderedListing = orderedListingsPage.getContent().get(0);
                order = orderedListing.getId().getOrder();
                if (order.getStatus().equals(OrderStatus.CREATED)) {
                    orderedListing.setQuantity(orderedListing.getQuantity() + 1);
                    orderedListingService.save(orderedListing);
                    order.setSummary(orderedListingService.getOrderSummary(orderId).orElse(0));
                    orderService.save(order);
                    sendMessage = orderedListingsKeyboardMessage.prepareScrollingMessage(
                            chatId, ScrollerType.CURRENT, ScrollerObjectType.ITEM);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, new StringBuilder()
                            .append("Order is ")
                            .append(order.getStatus())
                            .append(" you can not modify it").toString()
                    ));
                }
                break;

            case Constants.KEYBOARD_ORDEREDLISTING_BUTTON_ADD_MINUS_COMMAND:

                orderedListingsInfo = cacheService.get(chatId).getOrderedListingsInfo();
                pageable = orderedListingsInfo.getListingPageable();
                orderId = orderedListingsInfo.getOrderId();
                orderedListingsPage = orderedListingService.findPageByOrderId(orderId, pageable);
                orderedListing = orderedListingsPage.getContent().get(0);
                order = orderedListing.getId().getOrder();
                if (order.getStatus().equals(OrderStatus.CREATED)) {
                    int newQuantity = orderedListing.getQuantity() - 1;
                    if (newQuantity == 0) {
                        orderedListingService.delete(orderedListing);
                    } else {
                        orderedListing.setQuantity(newQuantity);
                        orderedListingService.save(orderedListing);
                    }
                    order.setSummary(orderedListingService.getOrderSummary(orderId).orElse(0));

                    if (orderedListingsPage.getTotalPages() - 1 == 0 && newQuantity == 0) {
                        answer.add(Utils.prepareAnswerCallbackQuery(
                                Constants.KEYBOARD_ORDEREDLISTING_MESSAGE_ORDER_IS_EMPTY, true, callbackQuery));
                        order.setStatus(OrderStatus.CANCELED);
                        sendMessage = orderKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.NEW_LISTING_SCROLLER, ScrollerObjectType.ITEM);
                    } else if (newQuantity > 0) {
                        sendMessage = orderedListingsKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.CURRENT, ScrollerObjectType.ITEM);
                    } else {
                        sendMessage = orderedListingsKeyboardMessage.prepareScrollingMessage(
                                chatId, ScrollerType.NEW_LISTING_SCROLLER, ScrollerObjectType.ITEM);
                    }
                    orderService.save(order);
                    answer.addAll(Utils.handleOptionalSendMessage(sendMessage, callbackQuery));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, new StringBuilder()
                            .append("Order is ")
                            .append(order.getStatus())
                            .append(" you can not modify it").toString()
                    ));
                }
                break;
        }
        return answer;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK);
    }
}