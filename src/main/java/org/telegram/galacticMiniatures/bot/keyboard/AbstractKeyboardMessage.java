package org.telegram.galacticMiniatures.bot.keyboard;

import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface AbstractKeyboardMessage {

  default SendMessage prepareKeyboardMessage(Long chatId, String text) {
    SendMessage message = Utils.prepareSendMessage(chatId, text);
    message.setReplyMarkup(formKeyboard(chatId));
    return message;
  }

  default ReplyKeyboard formKeyboard(Long chatId) {
    return getInlineKeyboardMarkup(chatId);
  }

  default InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {
    return new InlineKeyboardMarkup();
  }

  default InlineKeyboardButton createInlineKeyboardButton(String text,
      String command) {
    InlineKeyboardButton button = new InlineKeyboardButton();
    button.setText(text);
    button.setCallbackData(command);
    return button;
  }
}