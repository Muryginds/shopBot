package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.enums.KeyboardType;
import org.telegram.galacticMiniatures.bot.keyboard.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final MainMenuKeyboardMessage mainMenuKeyboardMessage;
    private final SectionsKeyboardMessage sectionsKeyboardMessage;
    private final StarterKeyboardMessage starterKeyboardMessage;
    private final DefaultKeyboardMessage defaultKeyboardMessage;

    public SendMessage getSendMessage(KeyboardType keyboardType, long chatId, String text) {
        return getKeyboardMessage(keyboardType).prepareKeyboardMessage(chatId, text);
    }

    public AbstractKeyboardMessage getKeyboardMessage(KeyboardType keyboardType) {
        AbstractKeyboardMessage result;
        switch (keyboardType) {
            case MAIN_MENU:
                result = mainMenuKeyboardMessage;
                break;
            case SECTION:
                result = sectionsKeyboardMessage;
                break;
            case STARTER:
                result = starterKeyboardMessage;
                break;
            default:
                result = defaultKeyboardMessage;
        }
        return result;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup(KeyboardType keyboardType, long chatId) {
        return getKeyboardMessage(keyboardType).getInlineKeyboardMarkup(chatId);
    }
}
