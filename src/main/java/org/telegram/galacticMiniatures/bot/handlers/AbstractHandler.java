package org.telegram.galacticMiniatures.bot.handlers;

import com.google.common.collect.Lists;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.ArrayList;
import java.util.List;

public interface AbstractHandler {

  List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject);

  default List<BotState> getOperatedBotState() {
    return Lists.newArrayList();
  }

  default List<String> getOperatedCallBackQuery() {
    return new ArrayList<>();
  }
}