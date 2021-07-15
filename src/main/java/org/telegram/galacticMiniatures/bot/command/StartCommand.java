package org.telegram.galacticMiniatures.bot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;


@Component
public class StartCommand extends ServiceCommand {

  private final KeyboardService keyboardService;
  private final UserService userService;

  public StartCommand(@Value("start") String identifier,
                      @Value("Start") String description, KeyboardService keyboardService, UserService userService) {
    super(identifier, description);
    this.keyboardService = keyboardService;
    this.userService = userService;
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
    String userName = Utils.getUserName(user);
    SendMessage message = keyboardService.getSendMessage(KeyboardType.STARTER, chat.getId(), Constants.BOT_START);
    getUser(chat.getId(), userName);
    executeMethod(absSender, message, this.getCommandIdentifier(), userName);
  }

  private org.telegram.galacticMiniatures.bot.model.User getUser(Long chatId, String name) {
    return userService.getUser(chatId)
            .orElseGet(() -> userService.add(new org.telegram.galacticMiniatures.bot.model.User(chatId.toString(), name)));
  }
}