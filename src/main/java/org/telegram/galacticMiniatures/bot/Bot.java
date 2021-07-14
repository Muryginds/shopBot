package org.telegram.galacticMiniatures.bot;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.handlers.UpdateHandler;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

  private static final Logger logger = LoggerFactory.getLogger(Bot.class);

  @Getter
  private final String botUsername;
  @Getter
  private final String botToken;

  private final UpdateHandler updateHandler;

  @Autowired
  private BotCommand[] myBotCommands;

  public Bot (@Value("${bot.name}") String userName,
      @Value("${bot.token}") String botToken,
              UpdateHandler updateHandler) {
    super();
    this.botUsername = userName;
    this.botToken = botToken;
    this.updateHandler = updateHandler;
  }

  @PostConstruct
  public void registerCommands() {
    registerAll(myBotCommands);
  }

  @Override
  public void processNonCommandUpdate(Update update) {

    List<PartialBotApiMethod<?>> botApiMethodList
        = updateHandler.handleUpdate(update);
     try {
       for (PartialBotApiMethod<?> botApiMethod: botApiMethodList) {
         if (botApiMethod instanceof EditMessageMedia) {
           execute((EditMessageMedia) botApiMethod);
         } else if (botApiMethod instanceof SendPhoto) {
           execute((SendPhoto) botApiMethod);
         } else {
             execute((BotApiMethod<?>) botApiMethod);
           }
       }
     } catch (TelegramApiException e) {
       String userName = Utils.getUserName(update.getMessage().getFrom());
       logger.error("Error while executing message for: " + userName, e);
     }
  }
}