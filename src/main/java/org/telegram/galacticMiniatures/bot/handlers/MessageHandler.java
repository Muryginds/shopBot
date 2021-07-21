package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.FavoriteInfo;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.CartKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.FavoriteKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.ListingFavorite;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.service.CartService;
import org.telegram.galacticMiniatures.bot.service.FavoriteService;
import org.telegram.galacticMiniatures.bot.service.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MessageHandler implements AbstractHandler {

  private final KeyboardService keyboardService;
  private final CartKeyboardMessage cartKeyboardMessage;
  private final CartService cartService;
  private final FavoriteService favoriteService;
  private final FavoriteKeyboardMessage favoriteKeyboardMessage;
  private final UserService userService;

  @Override
  public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
    List<PartialBotApiMethod<?>> answer = new ArrayList<>();
    Message message = ((Message) botApiObject);
    long chatId = message.getChatId();

    if (Constants.BOT_START_COMMAND.equals(message.getText())) {
      answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
      answer.add(keyboardService.getSendMessage(
              KeyboardType.STARTER, chatId, Constants.BOT_START));
      User user = userService.getUser(chatId).orElse(new User(message));
      userService.add(user);
    } else {
      answer.addAll(handleStarterMenuReply(message));
    }

    return answer;
  }

  private List<PartialBotApiMethod<?>> handleStarterMenuReply(Message message) {
    String text = message.getText();
    List<PartialBotApiMethod<?>> answer = new ArrayList<>();
    Long chatId = message.getChatId();
    int messageId = message.getMessageId();
    switch (text) {
      case Constants.KEYBOARD_STARTER_SHOP_COMMAND:
        answer.add(keyboardService.getSendMessage(KeyboardType.SECTION, chatId, Constants.KEYBOARD_SECTIONS_HEADER));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;
      case Constants.KEYBOARD_STARTER_CART_COMMAND:
        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        if (cartService.countSizeCartByChatId(chatId) > 0) {
          Optional<SendPhoto> sendPhoto = cartKeyboardMessage.prepareSendPhoto(
                  chatId, ScrollerType.NEW, ScrollerObjectType.LISTING);
          sendPhoto.ifPresent(answer::add);
        } else {
          answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_STARTER_CART_EMPTY));
        }
        break;
      case Constants.KEYBOARD_STARTER_ABOUT_COMMAND:
        answer.add(Utils.prepareSendMessage(chatId, Constants.BOT_ABOUT));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;
      case Constants.KEYBOARD_STARTER_FAVORITE_COMMAND:
        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        Pageable pageable = PageRequest.of(0, 1);
        Page<ListingFavorite> listingPage =
                favoriteService.getPageFavoriteByChatId(chatId.toString(), pageable);
        if(listingPage.getTotalElements() > 0) {
          FavoriteInfo favoriteInfo = new FavoriteInfo();
          answer.add(favoriteKeyboardMessage.prepareSendPhoto(pageable, favoriteInfo, chatId));
        } else {
          answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_STARTER_FAVORITES_EMPTY));
        }
        break;
      case Constants.KEYBOARD_STARTER_ADDRESS_COMMAND:
        answer.add(Utils.prepareSendMessage(chatId, "User address here"));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;
      case Constants.KEYBOARD_STARTER_SHIPPING_COMMAND:
        answer.add(Utils.prepareSendMessage(chatId, Constants.BOT_SHIPPING));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;
    }
    return answer;
  }

  @Override
  public BotState getOperatedBotState() {
    return BotState.WORKING;
  }
}