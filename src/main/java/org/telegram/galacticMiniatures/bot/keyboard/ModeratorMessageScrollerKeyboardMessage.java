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
public class ModeratorMessageScrollerKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final CacheService cacheService;
    private final UserMessageService userMessageService;

    @Override
    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        OrderMessageInfo orderMessageInfo =
                cacheService.get(chatId).getOrderMessageInfo();
        Pageable messagePageable = orderMessageInfo.getItemPageable();
        int totalElementOnPage = orderMessageInfo.getPageSize();

        Sort optionSort = Sort.by("created").descending();
        if (scrollerObjectType == ScrollerObjectType.ITEM) {
            messagePageable = getPageableByScrollerType(messagePageable, scrollerType, optionSort);
        }
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

        if (messagePage.getTotalElements() > totalElementOnPage) {
            String listingPreviousCommand = Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_OPERATED_CALLBACK;
            if (messagePage.getNumber() > 0) {
                listingPreviousCommand = Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_PREVIOUS_NAME, listingPreviousCommand));

            long totalElements = messagePage.getTotalElements();
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(messagePage.getNumber() + 1)
                            .append(" / ")
                            .append(totalElements % totalElementOnPage > 0 ?
                                    totalElements / totalElementOnPage + 1 :
                                    totalElements / totalElementOnPage)
                            .toString(),
                    Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_OPERATED_CALLBACK;
            if (messagePage.getNumber() + 1 < messagePage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }

        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_NAME,
                Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow2);
        keyboardMarkup.setKeyboard(rowList);

        orderMessageInfo.setItemPageable(messagePageable);
        cacheService.add(chatId, orderMessageInfo);

        StringBuilder text = new StringBuilder("* Order [")
                .append(String.format("%05d" , orderId))
                .append("]: *\n\n");
        for (UserMessage userMessage: userMessages) {
            String username;
            String userChatId = userMessage.getUser().getChatId();
            if (userChatId.equals(chatId.toString())) {
                username = "You";
            } else {
                username = userChatId;
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