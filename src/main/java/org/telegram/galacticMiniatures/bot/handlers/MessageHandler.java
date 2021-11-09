package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.ModeratorOrderInfo;
import org.telegram.galacticMiniatures.bot.enums.*;
import org.telegram.galacticMiniatures.bot.keyboard.*;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
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
  private final CacheService cacheService;
  private final OrderService orderService;
  private final UserOrderKeyboardMessage userOrderKeyboardMessage;
  private final ModeratorOrdersKeyboardMessage moderatorOrdersKeyboardMessage;
  private final UserChatMessageKeyboardMessage userChatMessageKeyboardMessage;
  private final ModeratorMessagesKeyboardMessage moderatorMessagesKeyboardMessage;

  @Value("${ADMIN_CHAT_ID}")
  private String adminChatId;

  @Override
  public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
    List<PartialBotApiMethod<?>> answer = new ArrayList<>();
    Message message = ((Message) botApiObject);
    long chatId = message.getChatId();

    if (Constants.BOT_START_COMMAND.equals(message.getText())) {
      User user = userService.getUser(message);
      if (String.valueOf(chatId).equals(adminChatId)) {
        user.setIsModerator(true);
        user.setIsAdmin(true);
      }
      userService.add(user);
      answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
      answer.add(keyboardService.getSendMessage(
              KeyboardType.STARTER, chatId, Constants.BOT_START));
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
    Optional<PartialBotApiMethod<?>> sendMessage;

    switch (text) {
      case Constants.KEYBOARD_STARTER_SHOP_COMMAND:

        answer.add(keyboardService.getSendMessage(KeyboardType.SECTION, chatId, Constants.KEYBOARD_SECTIONS_HEADER));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;

      case Constants.KEYBOARD_STARTER_CART_COMMAND:

        if (cartService.countSizeCartByChatId(chatId).orElse(0) > 0) {
          sendMessage = cartKeyboardMessage.
                  prepareScrollingMessage(chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
          answer.addAll(handleOptionalMessage(sendMessage, message));
        } else {
          answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_STARTER_CART_EMPTY));
          answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        }
        break;

      case Constants.KEYBOARD_STARTER_INFORMATION_COMMAND:

        answer.add(Utils.prepareSendMessage(chatId, Constants.BOT_ABOUT));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;

      case Constants.KEYBOARD_STARTER_FAVORITE_COMMAND:

        if (favoriteService.countSizeFavoriteByChatId(chatId).orElse(0) > 0) {
          sendMessage = favoriteKeyboardMessage.
                  prepareScrollingMessage(chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
          answer.addAll(handleOptionalMessage(sendMessage, message));
        } else {
          answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_STARTER_FAVORITES_EMPTY));
          answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        }
        break;

      case Constants.KEYBOARD_STARTER_ADDRESS_COMMAND:

        answer.add(keyboardService.getSendMessage(
                KeyboardType.ADDRESS, chatId, Constants.BOT_ADDRESS_REQUEST));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;

      case Constants.KEYBOARD_STARTER_ORDER_COMMAND:

        if (orderService.countSizeOrdersByChatId(chatId).orElse(0) > 0) {
          sendMessage = userOrderKeyboardMessage.
                  prepareScrollingMessage(chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
          answer.addAll(handleOptionalMessage(sendMessage, message));
        } else {
          answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_STARTER_ORDERS_EMPTY));
          answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        }
        break;

//      case Constants.KEYBOARD_STARTER_MESSAGES_COMMAND:
//
//        sendMessage = userChatMessageKeyboardMessage
//                .prepareScrollingMessage(chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
//        answer.addAll(handleOptionalMessage(sendMessage, message));
//        break;

      case Constants.KEYBOARD_STARTER_ADMIN_PANEL_COMMAND:

        answer.add(keyboardService.getSendMessage(
                KeyboardType.ADMIN_PANEL, chatId, "Admin panel"));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;

      case Constants.KEYBOARD_STARTER_MODERATOR_MESSAGES_COMMAND:

        sendMessage = moderatorMessagesKeyboardMessage.prepareScrollingMessage(
                chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
        answer.addAll(handleOptionalMessage(sendMessage, message));
        break;

      case Constants.KEYBOARD_STARTER_USER_MESSAGES_COMMAND:

        answer.add(keyboardService.getSendMessage(
                KeyboardType.USER_MESSAGES, chatId, "Message management"));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;

      case Constants.KEYBOARD_STARTER_MODERATOR_ORDERS_COMMAND:

        cacheService.add(chatId, new ModeratorOrderInfo());
        sendMessage = moderatorOrdersKeyboardMessage.
                prepareScrollingMessage(chatId, ScrollerType.NEW, ScrollerObjectType.ITEM);
        answer.addAll(handleOptionalMessage(sendMessage, message));
        break;
    }
    return answer;
  }

  public static List<PartialBotApiMethod<?>> handleOptionalMessage(Optional<PartialBotApiMethod<?>> sendMessage,
                                                            Message message) {
    List<PartialBotApiMethod<?>> answer = new ArrayList<>();
    Long chatId = message.getChatId();
    Integer messageId = message.getMessageId();
    if (sendMessage.isPresent()) {
      answer.add(sendMessage.get());
      answer.add(Utils.prepareDeleteMessage(chatId, messageId));
    } else {
      answer.add(Utils.prepareSendMessage(chatId, Constants.ERROR_RESTART_MENU));
    }
    return answer;
  }

  @Override
  public List<BotState> getOperatedBotState() {
    return List.of(BotState.WORKING);
  }
}