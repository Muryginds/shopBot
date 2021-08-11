package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ModeratorOrderInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ModeratorOrdersKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final CacheService cacheService;
    private final OrderService orderService;

    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        ModeratorOrderInfo moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
        List<OrderStatus> statusList = moderatorOrderInfo.getOrderStatusList();
        Pageable listingPageable = getPageableByScrollerType(moderatorOrderInfo.getItemPageable(), scrollerType);

        Page<Order> orderPage = orderService.findPageOrderByStatus(statusList, listingPageable);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        List<Order> orders = orderPage.getContent();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (Order order : orders) {
            sb.setLength(0);
            String sectionCallBackData = sb.append(Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_ORDERS_COMMAND)
                    .append(order.getId()).toString();
            String buttonName;
            if (statusList.size() > 1) {
                buttonName = String.format("%05d [%.3s]", order.getId(), order.getStatus());
            } else {
                buttonName = String.format("%05d", order.getId());
            }
            keyboardButtonsRow.add(createInlineKeyboardButton(buttonName, sectionCallBackData));
            if (count++ % 2 == 1) {
                rowList.add(keyboardButtonsRow);
                keyboardButtonsRow = new ArrayList<>();
            } else if (count == orders.size()) {
                rowList.add(keyboardButtonsRow);
            }
        }

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        if(orderPage.getTotalPages() > 1) {

            String orderPreviousCommand = Constants.KEYBOARD_MODERATOR_ORDER_OPERATED_CALLBACK;
            if (orderPage.getNumber() > 0) {
                orderPreviousCommand = Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_PREVIOUS_NAME, orderPreviousCommand));

            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(orderPage.getNumber() + 1)
                            .append(" / ")
                            .append(orderPage.getTotalPages()).toString(),
                    Constants.KEYBOARD_MODERATOR_ORDER_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_MODERATOR_ORDER_OPERATED_CALLBACK;
            if (orderPage.getNumber() + 1 < orderPage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }

        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_ALL_STATUSES_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_ALL_STATUSES_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_NEW_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_NEW_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_CONFIRMED_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_CONFIRMED_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_PAID_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_PAID_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_PRINTING_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_PRINTING_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_SHIPPED_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_SHIPPED_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_CANCELED_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_CANCELED_COMMAND));
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        keyboardButtonsRow4.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow4);

        keyboardMarkup.setKeyboard(rowList);

        moderatorOrderInfo.setItemPageable(listingPageable);
        cacheService.add(chatId, moderatorOrderInfo);

        String statusName = statusList.size() > 1 ? "All" : statusList.get(0).toString();
        SendMessage sm = Utils.prepareSendMessage(chatId, String.format("*Orders [%s]*", statusName));
        sm.setReplyMarkup(keyboardMarkup);

        return Optional.of(sm);
    }
}