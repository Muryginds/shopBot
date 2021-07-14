package org.telegram.galacticMiniatures.bot.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;


@Component
public class UserCommand extends ServiceCommand {

  public UserCommand(@Value("user") String identifier,
                     @Value("User") String description) {
    super(identifier, description);
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
    String userName = Utils.getUserName(user);
    SendMessage message = Utils.prepareSendMessage(chat.getId(), "User info here");
    executeMethod(absSender, message, this.getCommandIdentifier(), userName);
  }
}