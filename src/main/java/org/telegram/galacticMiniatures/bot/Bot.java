package org.telegram.galacticMiniatures.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.handlers.UpdateHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

  @Getter
  private final String botUsername;
  @Getter
  private final String botToken;

  private final UpdateHandler updateHandler;

  public Bot (@Value("${bot.name}") String userName,
      @Value("${bot.token}") String botToken,
              UpdateHandler updateHandler) {
    super();
    this.botUsername = userName;
    this.botToken = botToken;
    this.updateHandler = updateHandler;
  }

  @Override
  public void onUpdateReceived(Update update) {
    List<PartialBotApiMethod<?>> botApiMethodList
            = updateHandler.handleUpdate(update);
    try {
      for (PartialBotApiMethod<?> botApiMethod: botApiMethodList) {
        if (botApiMethod instanceof SendPhoto) {
          execute((SendPhoto) botApiMethod);
        } else {
          execute((BotApiMethod<?>) botApiMethod);
        }
      }
    } catch (TelegramApiException e) {
      Message message =
              update.getMessage() == null ? update.getCallbackQuery().getMessage() : update.getMessage();
      long chatId = message.getChatId();
      log.error("Error while executing message for chatId: " + chatId, e);
    }
  }
}