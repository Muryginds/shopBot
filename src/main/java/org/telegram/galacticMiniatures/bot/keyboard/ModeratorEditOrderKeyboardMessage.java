package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ModeratorOrderInfo;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModeratorEditOrderKeyboardMessage implements AbstractKeyboardMessage {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CacheService cacheService;
    private final OrderService orderService;

    @Override
    public SendMessage prepareKeyboardMessage(Long chatId, String text) {

        ModeratorOrderInfo moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
        Optional<Order> optional = orderService.findById(moderatorOrderInfo.getOrderId());
        if (optional.isPresent()) {
            Order order = optional.get();
                text = new StringBuilder()
                        .append(String.format("Order: %05d\n",order.getId()))
                        .append(String.format("Created: %s\n", order.getCreated().format(DATE_TIME_FORMATTER)))
                        .append(String.format("Summary: %s\n", order.getSummary()))
                        .append(String.format("*Status: %s*", order.getStatus()))
                        .toString();
        }

        SendMessage message = Utils.prepareSendMessage(chatId, text);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_STATUS_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_STATUS_COMMAND));
        keyboardButtonsRow.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);

        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }
}