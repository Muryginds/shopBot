package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.StarterKeyboard;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MessageHandler implements AbstractHandler {

  private final KeyboardService keyboardService;

  @Override
  public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
    List<PartialBotApiMethod<?>> answer = new ArrayList<>();
    Message message = ((Message) botApiObject);
    long chatId = message.getChatId();

    String messageText = message.getText().toLowerCase(Locale.ROOT);
    boolean isStarterMenuReply = Arrays.stream(StarterKeyboard.values())
            .anyMatch(s -> s.name().toLowerCase(Locale.ROOT).equals(messageText));

    if (isStarterMenuReply) {
      answer.addAll(handleStarterMenuReply(messageText, message));
    } else {
      answer.add(Utils.prepareSendMessage(chatId, message.getText()));
    }

    return answer;
  }

  private List<BotApiMethod<?>> handleStarterMenuReply(String text, Message message) {
    StarterKeyboard reply = StarterKeyboard.valueOf(text.toUpperCase());
    List<BotApiMethod<?>> answer = new ArrayList<>();
    long chatId = message.getChatId();
    switch (reply) {
      case MENU:
        answer.add(keyboardService.getSendMessage(KeyboardType.MAIN_MENU, chatId, Constants.KEYBOARD_MAIN_MENU_HEADER));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;
      case HELP:
        answer.add(Utils.prepareSendMessage(message.getChatId(), Constants.BOT_HELP));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;
      case USER:
        answer.add(Utils.prepareSendMessage(message.getChatId(), "User info here"));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;
    }
    return answer;
  }

  @Override
  public BotState getOperatedBotState() {
    return BotState.WORKING;
  }
}