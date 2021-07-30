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

    private final SectionsKeyboardMessage sectionsKeyboardMessage;
    private final StarterKeyboardMessage starterKeyboardMessage;
    private final AddressKeyboardMessage addressKeyboardMessage;
    private final DefaultKeyboardMessage defaultKeyboardMessage;
    private final AdminPanelKeyboardMessage adminPanelKeyboardMessage;
    private final UserMessagesKeyboardMessage userMessagesKeyboardMessage;
    private final ModeratorMessagesKeyboardMessage moderatorMessagesKeyboardMessage;

    public SendMessage getSendMessage(KeyboardType keyboardType, long chatId, String text) {
        return getKeyboardMessage(keyboardType).prepareKeyboardMessage(chatId, text);
    }

    public AbstractKeyboardMessage getKeyboardMessage(KeyboardType keyboardType) {
        AbstractKeyboardMessage result;
        switch (keyboardType) {
            case SECTION:
                result = sectionsKeyboardMessage;
                break;
            case STARTER:
                result = starterKeyboardMessage;
                break;
            case ADDRESS:
                result = addressKeyboardMessage;
                break;
            case ADMIN_PANEL:
                result = adminPanelKeyboardMessage;
                break;
            case ADMIN_MESSAGES:
                result = moderatorMessagesKeyboardMessage;
                break;
            case USER_MESSAGES:
                result = userMessagesKeyboardMessage;
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
