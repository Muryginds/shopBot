package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.SearchInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;
import org.telegram.galacticMiniatures.bot.model.ListingWithOption;
import org.telegram.galacticMiniatures.bot.service.ListingService;
import org.telegram.galacticMiniatures.bot.service.ListingWithImageService;
import org.telegram.galacticMiniatures.bot.service.ListingWithOptionService;
import org.telegram.galacticMiniatures.bot.util.Constants;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListingKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final ListingService listingService;
    private final CacheService cacheService;
    private final ListingWithImageService listingWithImageService;
    private final ListingWithOptionService listingWithOptionService;

    @Override
    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        SearchInfo searchInfo = cacheService.get(chatId).getSearchInfo();
        Pageable listingPageable = searchInfo.getListingPageable();
        Pageable imagePageable = searchInfo.getImagePageable();
        Pageable optionPageable = searchInfo.getOptionPageable();

        Sort optionSort = Sort.by("price").and(Sort.by("firstOptionValue"));
        switch (scrollerObjectType) {
            case LISTING:
                listingPageable = getPageableByScrollerType(listingPageable, scrollerType);
                imagePageable = getPageableByScrollerType(imagePageable, ScrollerType.NEW_LISTING_SCROLLER);
                optionPageable = getPageableByScrollerType(imagePageable, ScrollerType.NEW_LISTING_SCROLLER, optionSort);
                break;
            case IMAGE:
                imagePageable = getPageableByScrollerType(imagePageable, scrollerType);
                break;
            case OPTION:
                optionPageable = getPageableByScrollerType(optionPageable, scrollerType);
                break;
        }

        Page<Listing> listingPage = listingService.findPageListingActiveBySectionIdentifier(
                        searchInfo.getSectionId(), listingPageable);
        Listing listing;
        try {
            listing = listingPage.getContent().get(0);
        } catch (IndexOutOfBoundsException ex) {
            log.error("Listing for ChatId: " + chatId + " not found. " + ex.getMessage());
            return Optional.empty();
        }

        Page<ListingWithImage> imagePage =
                listingWithImageService.findPageImagesActiveByListing(listing, imagePageable);
        Page<ListingWithOption> listingWithOptionPage =
                listingWithOptionService.findPageOptionByListing(listing, optionPageable);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();

        if (listingPage.getTotalElements() > 1) {
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    "Item", Constants.KEYBOARD_LISTING_OPERATED_CALLBACK));

            String listingPreviousCommand = Constants.KEYBOARD_LISTING_OPERATED_CALLBACK;
            if (listingPage.getNumber() > 0) {
                listingPreviousCommand = Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_NAME, listingPreviousCommand));

            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(listingPage.getNumber() + 1)
                            .append(" / ")
                            .append(listingPage.getTotalElements()).toString(),
                    Constants.KEYBOARD_LISTING_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_LISTING_OPERATED_CALLBACK;
            if (listingPage.getNumber() + 1 < listingPage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_LISTING_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }

        if (imagePage.getTotalElements() > 1) {
            keyboardButtonsRow0.add(createInlineKeyboardButton(
                    "Photo", Constants.KEYBOARD_LISTING_OPERATED_CALLBACK));

            String photoPreviousCommand = Constants.KEYBOARD_LISTING_OPERATED_CALLBACK;
            if (imagePage.getNumber() > 0) {
                photoPreviousCommand = Constants.KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow0.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_NAME, photoPreviousCommand));
            keyboardButtonsRow0.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(imagePage.getNumber() + 1)
                            .append(" / ")
                            .append(imagePage.getTotalElements()).toString(),
                    Constants.KEYBOARD_LISTING_BUTTON_PHOTO_MIDDLE_COMMAND));
            String photoNextCommand = Constants.KEYBOARD_LISTING_OPERATED_CALLBACK;
            if (imagePage.getNumber() + 1 < imagePage.getTotalPages()) {
                photoNextCommand = Constants.KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_COMMAND;
            }
            keyboardButtonsRow0.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_NAME, photoNextCommand));
            rowList.add(keyboardButtonsRow0);
        }

        ListingWithOption listingWithOption;
        try {
            listingWithOption = listingWithOptionPage.getContent().get(0);
        } catch (IndexOutOfBoundsException ex) {
            log.error("Option for ChatId: " + chatId + " for listingId:" + listing.getIdentifier() +
                    " not found. " + ex.getMessage());
            return Optional.empty();
        }


        StringBuilder optionsText = new StringBuilder();
        if (listingWithOptionPage.getTotalElements() > 1) {
            keyboardButtonsRow3.add(createInlineKeyboardButton(
                    "Option", Constants.KEYBOARD_LISTING_OPERATED_CALLBACK));

            String optionPreviousCommand = Constants.KEYBOARD_LISTING_OPERATED_CALLBACK;
            if (listingWithOptionPage.getNumber() > 0) {
                optionPreviousCommand = Constants.KEYBOARD_LISTING_BUTTON_OPTION_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow3.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_OPTION_PREVIOUS_NAME, optionPreviousCommand));
            optionsText
                .append("<b>")
                .append("\n\nOptions: ")
                .append("[")
                .append(listingWithOption.getFirstOptionName())
                .append(": ")
                .append(listingWithOption.getFirstOptionValue())
                .append("]");

            if (!"".equals(listingWithOption.getSecondOptionName())) {
                optionsText.append(", ")
                    .append("[")
                    .append(listingWithOption.getSecondOptionName())
                    .append(": ")
                    .append(listingWithOption.getSecondOptionValue())
                    .append("]");
            }
            optionsText.append("</b>");

            keyboardButtonsRow3.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(listingWithOptionPage.getNumber() + 1)
                            .append(" / ")
                            .append(listingWithOptionPage.getTotalElements()).toString(),
                    Constants.KEYBOARD_LISTING_OPERATED_CALLBACK));
            String optionNextCommand = Constants.KEYBOARD_LISTING_OPERATED_CALLBACK;
            if (listingWithOptionPage.getNumber() + 1 < listingWithOptionPage.getTotalPages()) {
                optionNextCommand = Constants.KEYBOARD_LISTING_BUTTON_OPTION_NEXT_COMMAND;
            }
            keyboardButtonsRow3.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_OPTION_NEXT_NAME, optionNextCommand));
            rowList.add(keyboardButtonsRow3);
        }

        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_CART_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_ADD_TO_CART_COMMAND));
        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_LISTING_BUTTON_GO_BACK_NAME,
                Constants.KEYBOARD_LISTING_BUTTON_GO_BACK_COMMAND));
        rowList.add(keyboardButtonsRow2);
        keyboardMarkup.setKeyboard(rowList);

        searchInfo.setListingPageable(listingPageable);
        searchInfo.setImagePageable(imagePageable);
        searchInfo.setOptionPageable(optionPageable);
        cacheService.add(chatId, searchInfo);

        ListingWithImage listingWithImage;
        try {
            listingWithImage = imagePage.getContent().get(0);
        } catch (IndexOutOfBoundsException ex) {
            log.error("Image for ChatId: " + chatId + " for listingId:" + listing.getIdentifier() +
                    " not found. " + ex.getMessage());
            return Optional.empty();
        }

        StringBuilder caption = new StringBuilder()
                .append(listing.getTitle())
                .append(optionsText)
                .append("\n\n<b>Price: ")
                .append(listingWithOption.getPrice())
                .append("</b>");

        InputFile inputFile = new InputFile();
        inputFile.setMedia(listingWithImage.getImageUrl());
        return getSendPhoto(chatId, keyboardMarkup, caption, inputFile);
    }
}