package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ModeratorOrderInfo;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModeratorEditOrderStatusKeyboardMessage implements AbstractKeyboardMessage {

    private final CacheService cacheService;
    private final OrderService orderService;

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {

        ModeratorOrderInfo moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
        Optional<Order> optional = orderService.findById(moderatorOrderInfo.getOrderId());
        if (optional.isPresent()) {
            int count = 0;
            List<OrderStatus> statusList = Arrays.stream(OrderStatus.values())
                    .filter(o -> !o.equals(optional.get().getStatus()))
                    .collect(Collectors.toList());
            for (OrderStatus status : statusList) {
                keyboardButtonsRow.add(createInlineKeyboardButton(status.toString(),
                        Constants.KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CHANGE_STATUS_COMMAND + status));
                if (count++ % 2 == 1) {
                    rowList.add(keyboardButtonsRow);
                    keyboardButtonsRow = new ArrayList<>();
                } else if (count == statusList.size()) {
                    rowList.add(keyboardButtonsRow);
                }
            }
        }
        keyboardButtonsRow0.add(createInlineKeyboardButton(Constants.KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CLOSE_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow0);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}