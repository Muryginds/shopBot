package org.telegram.galacticMiniatures.bot.util;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("tempMessagesControl")
public class TempMessagesControl {

  private final Map<Long, List<Integer>> messagesToBeDeleted =
      new HashMap<>();

  public void add(Message message) {
    long chatId = message.getChatId();
    List<Integer> messages = messagesToBeDeleted.get(chatId);
    if (messages == null) {
      messages = new ArrayList<>();
    }
    messages.add(message.getMessageId());
    messagesToBeDeleted.put(chatId, messages);
  }

  public List<BotApiMethod<?>> removeAllByChatId(long id) {
    List<Integer> list = messagesToBeDeleted.remove(id);
    List<BotApiMethod<?>> answer = new ArrayList<>();
    for (int item : list) {
      answer.add(Utils.prepareDeleteMessage(id, item));
    }
    return answer;
  }
}