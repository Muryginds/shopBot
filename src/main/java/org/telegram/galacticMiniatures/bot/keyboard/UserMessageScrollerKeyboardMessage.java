package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.OrderMessageInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.model.UserMessage;
import org.telegram.galacticMiniatures.bot.service.UserMessageService;
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
public class UserMessageScrollerKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final CacheService cacheService;
    private final UserMessageService userMessageService;

    @Override
    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        OrderMessageInfo orderMessageInfo =
                cacheService.get(chatId).getOrderMessageInfo();
        Pageable messagePageable = orderMessageInfo.getItemPageable();

        messagePageable = getPageableByScrollerType(messagePageable, scrollerType);

        int orderId = orderMessageInfo.getOrderId();
        if (orderId == 0) {
            return Optional.empty();
        }

        Page<UserMessage> messagePage =
                userMessageService.getPageByOrderId(orderId, messagePageable);
        List<UserMessage> userMessages = messagePage.getContent();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        if (messagePage.getTotalElements() > messagePage.getNumberOfElements()) {
            String listingPreviousCommand = Constants.KEYBOARD_USER_MESSAGE_SCROLLER_OPERATED_CALLBACK;
            if (messagePage.getNumber() > 0) {
                listingPreviousCommand = Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_PREVIOUS_NAME, listingPreviousCommand));

            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(messagePage.getTotalPages())
                            .toString(),
                    Constants.KEYBOARD_USER_MESSAGE_SCROLLER_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_USER_MESSAGE_SCROLLER_OPERATED_CALLBACK;
            if (messagePage.getNumber() + 1 < messagePage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }

        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_NAME,
                Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow2);
        keyboardMarkup.setKeyboard(rowList);

        orderMessageInfo.setItemPageable(messagePageable);
        cacheService.add(chatId, orderMessageInfo);

        StringBuilder text = new StringBuilder("* Order [")
                .append(String.format("%05d" , orderId))
                .append("]: *\n\n");
        for (UserMessage userMessage: userMessages) {
            String username;
            if (userMessage.getUser().getChatId().equals(chatId.toString())) {
                username = "You";
            } else {
                username = "Admin";
            }
            text.append("*[")
                    .append(username)
                    .append("]*: ")
                    .append(userMessage.getMessage())
                    .append("\n");
        }

        SendMessage sm = Utils.prepareSendMessage(chatId, text.toString());
        sm.setReplyMarkup(keyboardMarkup);
        return Optional.of(sm);
    }
}