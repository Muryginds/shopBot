package org.telegram.galacticMiniatures.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.model.Country;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.model.UserInfo;
import org.telegram.galacticMiniatures.bot.service.CountryService;
import org.telegram.galacticMiniatures.bot.service.UserInfoService;
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

        switch (botState) {
            case PROMOTE_ADMIN:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                if (!user.getIsAdmin()) {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_NO_RIGHTS));
                    return answer;
                }
                Optional<User> newAdmin = userService.findUser(text);
                if (newAdmin.isPresent() && !newAdmin.get().equals(user)) {
                    User admin = newAdmin.get();
                    admin.setIsAdmin(true);
                    userService.save(admin);
                    StringBuilder sb = new StringBuilder()
                            .append("User ")
                            .append(admin.getName())
                            .append(" (")
                            .append(admin.getChatId())
                            .append(") was promoted to Admin");
                    answer.add(Utils.prepareSendMessage(chatId, sb.toString()));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND));
                }
                user.setBotState(BotState.WORKING);
                userService.save(user);
                break;

            case DEMOTE_ADMIN:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                if (!user.getIsAdmin()) {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_NO_RIGHTS));
                    return answer;
                }
                Optional<User> newUser = userService.findUser(text);
                if (newUser.isPresent() && !newUser.get().equals(user)) {
                    User exAdmin = newUser.get();
                    exAdmin.setIsAdmin(false);
                    userService.save(exAdmin);
                    StringBuilder sb = new StringBuilder()
                            .append("Admin ")
                            .append(exAdmin.getName())
                            .append(" (")
                            .append(exAdmin.getChatId())
                            .append(") was demoted to User");
                    answer.add(Utils.prepareSendMessage(chatId, sb.toString()));
                } else {
                    answer.add(Utils.prepareSendMessage(chatId, Constants.KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND));
                }
                user.setBotState(BotState.WORKING);
                userService.save(user);
        }

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
            case Constants.KEYBOARD_ADMIN_BUTTON_PROMOTE_USER_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                user.setBotState(BotState.PROMOTE_ADMIN);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADMIN_QUESTION_ENTER_CHAT_ID));
                break;

            case Constants.KEYBOARD_ADMIN_BUTTON_DEMOTE_USER_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                user.setBotState(BotState.DEMOTE_ADMIN);
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
        return List.of(BotState.PROMOTE_ADMIN, BotState.DEMOTE_ADMIN);
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_ADMIN_OPERATED_CALLBACK);
    }
}