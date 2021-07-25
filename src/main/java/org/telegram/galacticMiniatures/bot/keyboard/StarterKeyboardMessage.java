package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StarterKeyboardMessage implements AbstractKeyboardMessage {

  private final UserService userService;

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
    keyboardRow1.addAll(List.of(Constants.KEYBOARD_STARTER_ADDRESS_COMMAND,
            Constants.KEYBOARD_STARTER_ORDER_COMMAND));
    keyboard.add(keyboardRow1);

    KeyboardRow keyboardRow2 = new KeyboardRow();
    keyboardRow2.addAll(List.of(Constants.KEYBOARD_STARTER_INFORMATION_COMMAND,
            Constants.KEYBOARD_STARTER_FAVORITE_COMMAND));
    keyboard.add(keyboardRow2);

    Optional<User> user = userService.findUser(chatId);
    user.ifPresent(u -> {
      if (user.get().getIsAdmin()) {
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.addAll(List.of(Constants.KEYBOARD_STARTER_ADMIN_PANEL));
        keyboard.add(keyboardRow3);
      }
    });

    keyboardMarkup.setKeyboard(keyboard);

    return keyboardMarkup;
  }
}