package org.telegram.galacticMiniatures.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
abstract class ServiceCommand extends BotCommand {

  ServiceCommand(String identifier, String description) {
    super(identifier, description);
  }

  void sendAnswer(AbsSender absSender, Long chatId, String commandName,
                  String userName, String text) {
    SendMessage message = Utils.prepareSendMessage(chatId, text);
    executeMethod(absSender, message, commandName, userName);
  }

  void executeMethod(AbsSender absSender, BotApiMethod<?> method, String commandName,
                     String userName ) {
    try {
      absSender.execute(method);
    } catch (TelegramApiException e) {
      StringBuilder info = new StringBuilder();
      info.append("Command: ").append(commandName).append(" User: ").append(userName);
      log.error(info.toString(), e);
    }
  }
}