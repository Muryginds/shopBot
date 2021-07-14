package org.telegram.galacticMiniatures.bot.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;


@Component
public class HelpCommand extends ServiceCommand {

  public HelpCommand(@Value("help") String identifier,
      @Value("Help") String description) {
    super(identifier, description);
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

    String userName = Utils.getUserName(user);

    sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(),
        userName, Constants.BOT_HELP);
  }
}