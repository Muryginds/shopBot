package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.OrderInfo;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserOrderKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final CacheService cacheService;
    private final OrderService orderService;


    @Override
    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        OrderInfo orderInfo = cacheService.get(chatId).getOrderInfo();
        Pageable listingPageable;
        if (scrollerType == ScrollerType.NEW_LISTING_SCROLLER) {
            Sort sort = Sort.by("created").descending();
            listingPageable = getPageableByScrollerType(orderInfo.getOrderPageable(), scrollerType, sort);
        } else {
            listingPageable = getPageableByScrollerType(orderInfo.getOrderPageable(), scrollerType);
        }

        Page<Order> orderPage = orderService.findPageOrderByChatId(chatId, listingPageable);

        Order order;
        try {
            order = orderPage.getContent().get(0);
        } catch (IndexOutOfBoundsException ex) {
            log.error("Order for ChatId: " + chatId + " not found. " + ex.getMessage());
            return Optional.empty();
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        if(orderPage.getTotalPages() > 1) {

            String orderPreviousCommand = Constants.KEYBOARD_ORDER_OPERATED_CALLBACK;
            if (orderPage.getNumber() > 0) {
                orderPreviousCommand = Constants.KEYBOARD_ORDER_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_ORDER_BUTTON_PREVIOUS_NAME, orderPreviousCommand));

            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(orderPage.getNumber() + 1)
                            .append(" / ")
                            .append(orderPage.getTotalElements()).toString(),
                    Constants.KEYBOARD_ORDER_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_ORDER_OPERATED_CALLBACK;
            if (orderPage.getNumber() + 1 < orderPage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_ORDER_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_ORDER_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }
        if(order.getStatus().equals(OrderStatus.CREATED)) {
            keyboardButtonsRow2.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_ORDER_BUTTON_CANCEL_ORDER_NAME,
                    Constants.KEYBOARD_ORDER_BUTTON_CANCEL_ORDER_COMMAND + order.getId()));
            keyboardButtonsRow2.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_ORDER_BUTTON_EDIT_NAME,
                    Constants.KEYBOARD_ORDER_BUTTON_EDIT_COMMAND + order.getId()));
            rowList.add(keyboardButtonsRow2);
        }
        keyboardButtonsRow4.add(createInlineKeyboardButton(
                Constants.KEYBOARD_ORDER_BUTTON_MESSAGES_NAME,
                Constants.KEYBOARD_ORDER_BUTTON_MESSAGES_COMMAND + order.getId()));
        rowList.add(keyboardButtonsRow4);
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_ORDER_BUTTON_TRACK_NAME,
                Constants.KEYBOARD_ORDER_BUTTON_TRACK_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_ORDER_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_ORDER_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow3);
        keyboardMarkup.setKeyboard(rowList);

        orderInfo.setOrderPageable(listingPageable);
        cacheService.add(chatId, orderInfo);

        String caption = new StringBuilder()
                .append("*[Order №")
                .append(String.format("%05d" , order.getId()))
                .append("]*\nSummary: *")
                .append(order.getSummary())
                .append("*\nCreated: *")
                .append(order.getCreated().format(DATE_FORMATTER))
                .append("*\nStatus: *")
                .append(order.getStatus())
                .append("*")
                .toString();

        SendMessage sm = Utils.prepareSendMessage(chatId, caption);
        sm.setReplyMarkup(keyboardMarkup);

        return Optional.of(sm);
    }
}