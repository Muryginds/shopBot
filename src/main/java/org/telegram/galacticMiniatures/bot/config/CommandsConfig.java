package org.telegram.galacticMiniatures.bot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;

@Configuration
@RequiredArgsConstructor
public class CommandsConfig {

  private final BotCommand helpCommand;
  private final BotCommand startCommand;
  private final BotCommand searchCommand;
  private final BotCommand menuCommand;
  private final BotCommand userCommand;

  @Bean("myBotCommands")
  public BotCommand[] myBotCommands () {
    return new BotCommand[]{helpCommand, startCommand, searchCommand, menuCommand, userCommand};
  }
}