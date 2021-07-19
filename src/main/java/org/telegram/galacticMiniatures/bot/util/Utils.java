package org.telegram.galacticMiniatures.bot.util;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  public static List<PartialBotApiMethod<?>> handleOptionalSendPhoto(Optional<SendPhoto> sendPhoto,
                                                                     CallbackQuery callbackQuery) {
    List<PartialBotApiMethod<?>> answer = new ArrayList<>();
    Message message = callbackQuery.getMessage();
    Long chatId = message.getChatId();
    Integer messageId = message.getMessageId();
    if (sendPhoto.isPresent()) {
      answer.add(sendPhoto.get());
      answer.add(prepareDeleteMessage(chatId, messageId));
    } else {
      answer.add(prepareAnswerCallbackQuery(Constants.ERROR_RESTART_MENU, true, callbackQuery));
    }
    return answer;
  }
}