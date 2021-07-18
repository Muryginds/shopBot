package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.service.CartService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MainMenuKeyboardMessage implements AbstractKeyboardMessage {

    private final CartService cartService;

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CATALOGUE_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CATALOGUE_COMMAND));
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CONTACTS_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CONTACTS_COMMAND));
        rowList.add(keyboardButtonsRow1);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_FAVORITE_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_FAVORITE_COMMAND));
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.KEYBOARD_MAIN_MENU_BUTTON_CART_NAME);
        Optional<Integer> cartSummary = cartService.getCartSummaryByChatId(chatId.toString());
        cartSummary.ifPresent(integer -> sb.append(" (").append(integer).append(")"));
        keyboardButtonsRow2.add(createInlineKeyboardButton(sb.toString(),
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CART_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CLOSE_NAME,
                Constants.KEYBOARD_MAIN_MENU_BUTTON_CLOSE_COMMAND));
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}