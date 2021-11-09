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
public class AddressCallbackHandler implements AbstractHandler {

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final CountryService countryService;

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
        Optional<UserInfo> userInfo = userInfoService.findByUser(user);
        String text = message.getText();

        switch (botState) {
            case FILLING_FULL_NAME:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                userInfo.ifPresent(u -> {u.setFullName(text);
                    userInfoService.save(u);});
                user.setBotState(BotState.FILLING_TOWN);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADDRESS_QUESTION_2));
                break;

            case FILLING_TOWN:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                userInfo.ifPresent(u -> {u.setTown(text); userInfoService.save(u);});
                user.setBotState(BotState.FILLING_ADDRESS);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADDRESS_QUESTION_3));
                break;

            case FILLING_ADDRESS:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                userInfo.ifPresent(u -> {u.setAddress(text); userInfoService.save(u);});
                user.setBotState(BotState.FILLING_POST_INDEX);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADDRESS_QUESTION_4));
                break;

            case FILLING_POST_INDEX:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                userInfo.ifPresent(u -> {u.setPostIndex(text); userInfoService.save(u);});
                user.setBotState(BotState.WORKING);
                userService.save(user);
                answer.add(Utils.prepareSendMessage(chatId, "Address is set!"));
                break;
        }

        return answer;
    }

    private List<PartialBotApiMethod<?>> handleCallBackQuery(CallbackQuery callbackQuery) {

        List<PartialBotApiMethod<?>> answer = new ArrayList<>();
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();

        switch (data) {
            case Constants.KEYBOARD_ADDRESS_BUTTON_EDIT_COMMAND:

                User user = userService.getUser(message);
                user.setBotState(BotState.FILLING_FULL_NAME);
                userService.save(user);
                UserInfo userInfo = userInfoService.findByUser(user).orElse(new UserInfo(user));
                Optional<Country> optional = countryService.findByCountryId(181);
                optional.ifPresent(userInfo::setCountry);
                userInfoService.save(userInfo);
                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                answer.add(Utils.prepareSendMessage(chatId, Constants.QUERY_ADDRESS_QUESTION_1));
                break;

            case Constants.KEYBOARD_ADDRESS_BUTTON_CLOSE_COMMAND:

                answer.add(Utils.prepareDeleteMessage(chatId, messageId));
                break;
        }
        return answer;
    }

    @Override
    public List<BotState> getOperatedBotState() {
        return List.of(BotState.FILLING_ADDRESS,
                        BotState.FILLING_CONTACTS,
                        BotState.FILLING_FULL_NAME,
                        BotState.FILLING_TOWN,
                        BotState.FILLING_POST_INDEX);
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_ADDRESS_OPERATED_CALLBACK);
    }
}