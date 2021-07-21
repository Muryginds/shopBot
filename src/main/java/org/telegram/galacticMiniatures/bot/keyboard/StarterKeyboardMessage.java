package org.telegram.galacticMiniatures.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class StarterKeyboardMessage implements AbstractKeyboardMessage {

  @Override
  public ReplyKeyboard formKeyboard(Long chatId) {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setSelective(true);
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setOneTimeKeyboard(false);
    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow keyboardRow0 = new KeyboardRow();
    keyboardRow0.addAll(List.of(Constants.KEYBOARD_STARTER_SHOP_COMMAND,
            Constants.KEYBOARD_STARTER_CART_COMMAND));
    keyboard.add(keyboardRow0);

    KeyboardRow keyboardRow1 = new KeyboardRow();
    keyboardRow1.addAll(List.of(Constants.KEYBOARD_STARTER_ABOUT_COMMAND,
            Constants.KEYBOARD_STARTER_FAVORITE_COMMAND));
    keyboard.add(keyboardRow1);

    KeyboardRow keyboardRow2 = new KeyboardRow();
    keyboardRow2.addAll(List.of(Constants.KEYBOARD_STARTER_ADDRESS_COMMAND,
            Constants.KEYBOARD_STARTER_SHIPPING_COMMAND));
    keyboard.add(keyboardRow2);

    keyboardMarkup.setKeyboard(keyboard);

    return keyboardMarkup;
  }
}