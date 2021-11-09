package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.keyboard.KeyboardService;
import org.telegram.galacticMiniatures.bot.service.UserService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminPanelCallbackHandler implements AbstractHandler {

    private final UserService userService;
    private final KeyboardService keyboardService;

    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {

        List<PartialBotApiMethod<?>> answer = new ArrayList<>();

        if (botApiObject instanceof Message) {
            Message message = ((Message) botApiObject);
            answer.addAll(handleTextMessage(message));

        } else if (botApiObject instanceof CallbackQuery) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            answer.addAll(handleCallBackQuery(callbackQuery));
        }
        return answer;
   }

    private List<PartialBotApiMethod<?>> handleTextMessage(Message message) {

        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        User user = userService.getUser(message);
        BotState botState = user.getBotState();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        String text = message.getText();
        Optional<User> optionalUser;
        User userWithRights;

        switch (botState) {
            case ADMIN_PROMOTE:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                if (!user.getIsAdmin()) {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_NO_RIGHTS));
                    return answer;
                }
                optionalUser = userService.findUser(text);
                if (optionalUser.isPresent() && !optionalUser.get().equals(user)) {
                    userWithRights = optionalUser.get();
                    userWithRights.setIsAdmin(true);
                    userService.save(userWithRights);
                    StringBuilder sb = new StringBuilder()
                            .append("User ")
                            .append(userWithRights.getName())
                            .append(" (")
                            .append(userWithRights.getChatId())
                            .append(") was promoted to Admin");
                    answer.add(Utils.prepareSendMessage(chatId, sb.toString()));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND));
                }
                user.setBotState(BotState.WORKING);
                userService.save(user);
                break;

            case ADMIN_DEMOTE:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                if (!user.getIsAdmin()) {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_NO_RIGHTS));
                    return answer;
                }
                optionalUser = userService.findUser(text);
                if (optionalUser.isPresent() && !optionalUser.get().equals(user)) {
                    userWithRights = optionalUser.get();
                    userWithRights.setIsAdmin(false);
                    userService.save(userWithRights);
                    StringBuilder sb = new StringBuilder()
                            .append("Admin ")
                            .append(userWithRights.getName())
                            .append(" (")
                            .append(userWithRights.getChatId())
                            .append(") was demoted to User");
                    answer.add(Utils.prepareSendMessage(chatId, sb.toString()));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND));
                }
                user.setBotState(BotState.WORKING);
                userService.save(user);
                break;

            case MODERATOR_PROMOTE:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                if (!user.getIsAdmin()) {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_NO_RIGHTS));
                    return answer;
                }
                optionalUser = userService.findUser(text);
                if (optionalUser.isPresent() && !optionalUser.get().equals(user)) {
                    userWithRights = optionalUser.get();
                    userWithRights.setIsModerator(true);
                    userService.save(userWithRights);
                    StringBuilder sb = new StringBuilder()
                            .append("User ")
                            .append(userWithRights.getName())
                            .append(" (")
                            .append(userWithRights.getChatId())
                            .append(") was promoted to Moderator");
                    answer.add(Utils.prepareSendMessage(chatId, sb.toString()));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND));
                }
                user.setBotState(BotState.WORKING);
                userService.save(user);
                break;

            case MODERATOR_DEMOTE:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                if (!user.getIsAdmin()) {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_NO_RIGHTS));
                    return answer;
                }
                optionalUser = userService.findUser(text);
                if (optionalUser.isPresent() && !optionalUser.get().equals(user)) {
                    userWithRights = optionalUser.get();
                    userWithRights.setIsModerator(false);
                    userService.save(userWithRights);
                    StringBuilder sb = new StringBuilder()
                            .append("Moderator ")
                            .append(userWithRights.getName())
                            .append(" (")
                            .append(userWithRights.getChatId())
                            .append(") was demoted to User");
                    answer.add(Utils.prepareSendMessage(chatId, sb.toString()));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND));
                }
                user.setBotState(BotState.WORKING);
                userService.save(user);
        }
        answer.add(keyboardService.getSendMessage(KeyboardType.ADMIN_PANEL, chatId, "Admin panel"));
        return answer;
    }

    private List<PartialBotApiMethod<?>> handleCallBackQuery(CallbackQuery callbackQuery) {

        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        User user = userService.getUser(message);
        Integer messageId = message.getMessageId();

        switch (data) {
            case Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_ADMIN_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                user.setBotState(BotState.ADMIN_PROMOTE);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADMIN_QUESTION_ENTER_CHAT_ID));
                break;

            case Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_ADMIN_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                user.setBotState(BotState.ADMIN_DEMOTE);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADMIN_QUESTION_ENTER_CHAT_ID));
                break;

            case Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_MODERATOR_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                user.setBotState(BotState.MODERATOR_PROMOTE);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADMIN_QUESTION_ENTER_CHAT_ID));
                break;

            case Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_MODERATOR_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                user.setBotState(BotState.MODERATOR_DEMOTE);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADMIN_QUESTION_ENTER_CHAT_ID));
                break;

            case Constants.KEYBOARD_ADMIN_BUTTON_CLOSE_MENU_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;
        }
        return answer;
    }

    @Override
    public List<BotState> getOperatedBotState() {
        return List.of(BotState.ADMIN_PROMOTE, BotState.ADMIN_DEMOTE,
                BotState.MODERATOR_PROMOTE, BotState.MODERATOR_DEMOTE);
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_ADMIN_OPERATED_CALLBACK);
    }
}