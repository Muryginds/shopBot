package org.telegram.galacticMiniatures.bot.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class MenuCommand extends ServiceCommand {

  private final KeyboardService keyboardService;

  public MenuCommand(@Value("menu") String identifier,
                     @Value("Menu") String description, KeyboardService keyboardService) {
    super(identifier, description);
    this.keyboardService = keyboardService;
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
    String userName = Utils.getUserName(user);
    SendMessage message = keyboardService.getSendMessage(KeyboardType.MAIN_MENU, chat.getId(),
            Constants.KEYBOARD_MAIN_MENU_HEADER);
    executeMethod(absSender, message, this.getCommandIdentifier(), userName);
  }
}