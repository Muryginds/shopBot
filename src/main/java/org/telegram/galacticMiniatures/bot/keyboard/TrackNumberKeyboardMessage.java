package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ModeratorOrderInfo;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.model.UserInfo;
import org.telegram.galacticMiniatures.bot.service.OrderService;
import org.telegram.galacticMiniatures.bot.service.UserInfoService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrackNumberKeyboardMessage implements AbstractKeyboardMessage {

    private final CacheService cacheService;
    private final OrderService orderService;
    @Override
    public SendMessage prepareKeyboardMessage(Long chatId, String text) {

        ModeratorOrderInfo moderatorOrderInfo = cacheService.get(chatId).getModeratorOrderInfo();
        Optional<Order> optional = orderService.findById(moderatorOrderInfo.getOrderId());
        if (optional.isPresent()) {
            Order order = optional.get();
            text = String.format("*Track number: [%s]*", order.getTrackNumber());
        }
        SendMessage message = Utils.prepareSendMessage(chatId, text);
        message.setReplyMarkup(formKeyboard(chatId));
        return message;
    }

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(createInlineKeyboardButton(Constants.KEYBOARD_ADDRESS_BUTTON_EDIT_NAME,
                Constants.KEYBOARD_ADDRESS_BUTTON_EDIT_COMMAND));
        keyboardButtonsRow.add(createInlineKeyboardButton(Constants.KEYBOARD_ADDRESS_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_ADDRESS_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}