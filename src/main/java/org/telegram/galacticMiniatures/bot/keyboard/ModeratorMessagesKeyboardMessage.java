package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.MessagesInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.repository.response.NewMessagesResponse;
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
public class ModeratorMessagesKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final UserMessageService userMessageService;
    private final CacheService cacheService;

    @Override
    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        MessagesInfo messagesInfo = cacheService.get(chatId).getMessagesInfo();
        Pageable messagePageable = getPageableByScrollerType(messagesInfo.getItemPageable(), scrollerType);
        Page<NewMessagesResponse> messagePage = userMessageService.trackMessagesForModerator(chatId, messagePageable);
        List<NewMessagesResponse> messagesResponses = messagePage.getContent();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        StringBuilder command = new StringBuilder();
        StringBuilder caption = new StringBuilder();
        int count = 0;
        for (NewMessagesResponse message : messagesResponses) {
            command.setLength(0);
            caption.setLength(0);
            Integer sum = message.getSum().intValue();
            Integer orderId = message.getOrderId();
            String sectionCallBackData = command.append(Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_MESSAGES_COMMAND)
                    .append(orderId)
                    .toString();
            if (sum > 0) {
                caption.append(String.format("%05d", orderId))
                        .append(" [")
                        .append(sum)
                        .append("]");
            } else {
                caption.append(String.format("%05d", orderId));
            }
            keyboardButtonsRow.add(createInlineKeyboardButton(caption.toString(), sectionCallBackData));
            if (count++ % 2 == 1) {
                rowList.add(keyboardButtonsRow);
                keyboardButtonsRow = new ArrayList<>();
            } else if (count == messagesResponses.size()) {
                rowList.add(keyboardButtonsRow);
            }
        }

        if (messagePage.getTotalElements() > messagePage.getNumberOfElements()) {
            String listingPreviousCommand = Constants.KEYBOARD_MODERATOR_MESSAGES_OPERATED_CALLBACK;
            if (messagePage.getNumber() > 0) {
                listingPreviousCommand = Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_PREVIOUS_NAME, listingPreviousCommand));

            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(messagePage.getNumber() + 1)
                            .append(" / ")
                            .append(messagePage.getTotalPages())
                            .toString(),
                    Constants.KEYBOARD_MODERATOR_MESSAGES_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_MODERATOR_MESSAGES_OPERATED_CALLBACK;
            if (messagePage.getNumber() + 1 < messagePage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }

        InlineKeyboardButton goBackButton = new InlineKeyboardButton();
        goBackButton.setText(Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_CLOSE_NAME);
        goBackButton.setCallbackData(Constants.KEYBOARD_MODERATOR_MESSAGES_BUTTON_CLOSE_COMMAND);
        rowList.add(new ArrayList<>(List.of(goBackButton)));
        inlineKeyboardMarkup.setKeyboard(rowList);

        messagesInfo.setItemPageable(messagePageable);
        cacheService.add(chatId, messagesInfo);

        SendMessage sm = Utils.prepareSendMessage(chatId, Constants.KEYBOARD_MODERATOR_MESSAGES_HEADER);
        sm.setReplyMarkup(inlineKeyboardMarkup);

        return Optional.of(sm);
    }
}