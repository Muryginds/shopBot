package org.telegram.galacticMiniatures.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.StarterKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class StarterKeyboardMessage implements AbstractKeyboardMessage {

  @Override
  public ReplyKeyboard formKeyboard(Long chatId) {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setSelective(true);
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setOneTimeKeyboard(false);
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow keyboardRow = new KeyboardRow();
    keyboardRow.addAll(Arrays.stream(StarterKeyboard.values())
            .map(s -> s.toString().toLowerCase(Locale.ROOT))
            .collect(Collectors.toList()));
    keyboard.add(keyboardRow);
    keyboardMarkup.setKeyboard(keyboard);

    return keyboardMarkup;
  }
}