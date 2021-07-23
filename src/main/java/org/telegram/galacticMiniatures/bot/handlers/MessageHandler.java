package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.keyboard.CartKeyboardMessage;
import org.telegram.galacticMiniatures.bot.keyboard.FavoriteKeyboardMessage;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageHandler implements AbstractHandler {

  private final KeyboardService keyboardService;
  private final CartKeyboardMessage cartKeyboardMessage;
  private final CartService cartService;
  private final FavoriteService favoriteService;
  private final FavoriteKeyboardMessage favoriteKeyboardMessage;
  private final UserService userService;
  private final OrderService orderService;

  @Override
  public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
    List<PartialBotApiMethod<?>> answer = new ArrayList<>();
    Message message = ((Message) botApiObject);
    long chatId = message.getChatId();

    if (Constants.BOT_START_COMMAND.equals(message.getText())) {
      answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
      answer.add(keyboardService.getSendMessage(
              KeyboardType.STARTER, chatId, Constants.BOT_START));
      User user = userService.findUser(chatId).orElse(new User(message));
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

        case Constants.KEYBOARD_STARTER_INFORMATION_COMMAND:

        answer.add(Utils.prepareSendMessage(chatId, Constants.BOT_ABOUT));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;

        case Constants.KEYBOARD_STARTER_FAVORITE_COMMAND:

        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
        if (favoriteService.countSizeFavoriteByChatId(chatId) > 0) {
          Optional<SendPhoto> sendPhoto = favoriteKeyboardMessage.prepareSendPhoto(
                  chatId, ScrollerType.NEW, ScrollerObjectType.LISTING);
          sendPhoto.ifPresent(answer::add);
        } else {
          answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_STARTER_CART_EMPTY));
        }
        break;

        case Constants.KEYBOARD_STARTER_ADDRESS_COMMAND:

        answer.add(keyboardService.getSendMessage(
                KeyboardType.ADDRESS, chatId, Constants.BOT_ADDRESS_REQUEST));
        answer.add(Utils.prepareDeleteMessage(chatId, message.getMessageId()));
        break;

      case Constants.KEYBOARD_STARTER_ORDER_COMMAND:

        List<Order> orderList = orderService.findAllByChatId(chatId.toString());
        if (orderList.size() > 0) {
          String result = orderList.stream().map(o -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Order №: ")
                    .append(o.getId())
                    .append(", Summary: ")
                    .append(o.getSummary())
                    .append(", Status: ")
                    .append(o.getStatus());
            return builder;
          }).collect(Collectors.joining("\n"));
          answer.add(Utils.prepareSendMessage(chatId, result));
        } else {
          answer.add(Utils.prepareSendMessage(chatId,"No orders found"));
        }
        answer.add(Utils.prepareDeleteMessage(chatId, messageId));
    }
    return answer;
  }

  @Override
  public List<BotState> getOperatedBotState() {
    return List.of(BotState.WORKING);
  }
}