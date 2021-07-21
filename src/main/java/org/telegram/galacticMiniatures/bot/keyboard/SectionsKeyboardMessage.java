package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.service.SectionService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SectionsKeyboardMessage implements AbstractKeyboardMessage {

    private final SectionService sectionService;

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup(Long chatId) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        List<Section> sections = sectionService.getActiveSections();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (Section section : sections) {
            sb.setLength(0);
            String sectionCallBackData = sb.append(Constants.KEYBOARD_SECTIONS_OPERATED_CALLBACK)
                    .append(section.getIdentifier()).toString();
            keyboardButtonsRow.add(createInlineKeyboardButton(section.getName(), sectionCallBackData));
            if (count % 2 == 1) {
                rowList.add(keyboardButtonsRow);
                keyboardButtonsRow = new ArrayList<>();
            }
            count++;
        }
        InlineKeyboardButton goBackButton = new InlineKeyboardButton();
        goBackButton.setText(Constants.KEYBOARD_SECTIONS_BUTTON_GO_BACK_NAME);
        goBackButton.setCallbackData(Constants.KEYBOARD_SECTIONS_BUTTON_GO_BACK_COMMAND);
        rowList.add(new ArrayList<>(List.of(goBackButton)));
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}