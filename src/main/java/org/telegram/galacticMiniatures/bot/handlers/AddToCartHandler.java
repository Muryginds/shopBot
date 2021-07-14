package org.telegram.galacticMiniatures.bot.handlers;

import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.List;

public class AddToCartHandler implements AbstractHandler{
    @Override
    public List<PartialBotApiMethod<?>> getAnswerList(BotApiObject botApiObject) {
        return null;
    }

    @Override
    public BotState getOperatedBotState() {
        return BotState.ADDING_TO_CART;
    }

    @Override
    public List<String> getOperatedCallBackQuery() {
        return List.of(Constants.KEYBOARD_ADD_TO_CART_OPERATED_CALLBACK);
    }
}
