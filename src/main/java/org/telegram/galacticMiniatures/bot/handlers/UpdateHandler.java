package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateHandler {

  private final UserService userService;
  private final List<AbstractHandler> handlers;

  public List<PartialBotApiMethod<?>> handleUpdate(Update update) {

    List<PartialBotApiMethod<?>> result = new ArrayList<>();
    Optional <AbstractHandler> handler = Optional.empty();
    BotApiObject returnBotApiObject = null;

    if(update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      handler = getHandler(callbackQuery.getData());
      returnBotApiObject = callbackQuery;
    } else if(update.hasMessage()) {
      Message message = update.getMessage();
      if (message.hasText()) {
        User user = userService.getUser(message);
        handler = getHandler(user.getBotState());
        returnBotApiObject = message;
      }
    }

    if (handler.isPresent()) {
      result = handler.get().getAnswerList(returnBotApiObject);
    }

    return result;
  }

  private Optional<AbstractHandler> getHandler(BotState botState) {
    return handlers.stream()
        .filter(h -> h.getOperatedBotState().contains(botState))
        .findAny();
  }

  private Optional<AbstractHandler> getHandler(String query) {
    return handlers.stream()
        .filter(h -> h.getOperatedCallBackQuery().stream()
                .anyMatch(query::startsWith))
        .findAny();
  }
}