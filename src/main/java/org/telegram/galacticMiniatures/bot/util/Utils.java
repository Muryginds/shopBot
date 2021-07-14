package org.telegram.galacticMiniatures.bot.util;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

public final class Utils {

  private Utils() {}

  public static String getUserName(User user) {
    return (user.getUserName() != null) ? user.getUserName() :
        String.format("%s %s", user.getLastName(), user.getFirstName());
  }

  public static SendMessage prepareSendMessage(Long chatId, String text) {
    SendMessage message = new SendMessage();
    message.enableMarkdown(true);
    message.setChatId(chatId.toString());
    message.setText(text);

    return message;
  }

  public static DeleteMessage prepareDeleteMessage(Long chatId, Integer messageId) {
    DeleteMessage message = new DeleteMessage();
    message.setChatId(chatId.toString());
    message.setMessageId(messageId);

    return message;
  }

  public static AnswerCallbackQuery prepareAnswerCallbackQuery(String text,
             boolean alert, CallbackQuery callbackQuery) {

    AnswerCallbackQuery answerCallbackQuery =
        new AnswerCallbackQuery();
    answerCallbackQuery.setCallbackQueryId(
        callbackQuery.getId());
    answerCallbackQuery.setShowAlert(alert);
    answerCallbackQuery.setText(text);

    return answerCallbackQuery;
  }
}