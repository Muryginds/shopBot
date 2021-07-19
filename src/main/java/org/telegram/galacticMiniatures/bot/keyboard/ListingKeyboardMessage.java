package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ListingKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final ListingService listingService;
    private final CacheService cacheService;
    private final ListingWithImageService listingWithImageService;
    private final ListingWithOptionService listingWithOptionService;

    @Override
    public Optional<SendPhoto> prepareSendPhoto(Long chatId,
                                                ScrollerType scrollerType,
                                                ScrollerObjectType scrollerObjectType) {

        SearchInfo searchInfo = cacheService.get(chatId).getSearchInfo();
        Pageable listingPageable = searchInfo.getListingPageable();
        Pageable imagePageable = searchInfo.getImagePageable();
        Pageable optionPageable = searchInfo.getOptionPageable();

        switch (scrollerObjectType) {
            case LISTING:
                listingPageable = getPageableByScrollerType(listingPageable, scrollerType);
                imagePageable = getPageableByScrollerType(imagePageable, ScrollerType.NEW);
                optionPageable = PageRequest.of(0,1, Sort.by("price").and(Sort.by("firstOptionValue")));
                break;
            case IMAGE:
                imagePageable = getPageableByScrollerType(imagePageable, scrollerType);
                optionPageable = PageRequest.of(0,1, Sort.by("price").and(Sort.by("firstOptionValue")));
                break;
            case OPTION:
                optionPageable = getPageableByScrollerType(optionPageable, scrollerType);
        }

        Page<Listing> listingPage = listingService.getPageListingBySectionIdentifier(
                        searchInfo.getSectionId(), listingPageable);
        Listing listing;
        try {
            listing = listingPage.getContent().get(0);
        } catch (IndexOutOfBoundsException ex) {
            return Optional.empty();
        }
        Page<ListingWithImage> imagePage =
                listingWithImageService.getPageImagesByListing(listing, imagePageable);
        Page<ListingWithOption> listingWithOptionPage =
                listingWithOptionService.getPageOptionByListing(listing, optionPageable);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow0 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();

        Optional<Integer> optionPrice = Optional.empty();
        StringBuilder optionsText = new StringBuilder();
        if (listingWithOptionPage.getTotalElements() > 1) {
            ListingWithOption listingWithOption = listingWithOptionPage.getContent().get(0);
            optionPrice = Optional.of(listingWithOption.getPrice());
            if (listingWithOptionPage.getNumber() > 0) {
                keyboardButtonsRow3.add(createInlineKeyboardButton(
                        Constants.KEYBOARD_LISTING_BUTTON_OPTION_PREVIOUS_NAME,
                        Constants.KEYBOARD_LISTING_BUTTON_OPTION_PREVIOUS_COMMAND));
            }
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
                    new StringBuilder().append("Option ")
                            .append(listingWithOptionPage.getNumber() + 1)
                            .append(" of ")
                            .append(listingWithOptionPage.getTotalElements()).toString(),
                    Constants.KEYBOARD_LISTING_BUTTON_OPTION_MIDDLE_COMMAND));
            if (listingWithOptionPage.getNumber() + 1 < listingWithOptionPage.getTotalPages()) {
                keyboardButtonsRow3.add(createInlineKeyboardButton(
                        Constants.KEYBOARD_LISTING_BUTTON_OPTION_NEXT_NAME,
                        Constants.KEYBOARD_LISTING_BUTTON_OPTION_NEXT_COMMAND));
            }
            rowList.add(keyboardButtonsRow3);
        }

        if (imagePage.getTotalElements() > 1) {
            if (imagePage.getNumber() > 0) {
                keyboardButtonsRow0.add(createInlineKeyboardButton(
                        Constants.KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_NAME,
                        Constants.KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_COMMAND));
            }
            keyboardButtonsRow0.add(createInlineKeyboardButton(
                    new StringBuilder().append("Photo ")
                            .append(imagePage.getNumber() + 1)
                            .append(" of ")
                            .append(imagePage.getTotalElements()).toString(),
                    Constants.KEYBOARD_LISTING_BUTTON_PHOTO_MIDDLE_COMMAND));
            if (imagePage.getNumber() + 1 < imagePage.getTotalPages()) {
                keyboardButtonsRow0.add(createInlineKeyboardButton(
                        Constants.KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_NAME,
                        Constants.KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_COMMAND));
            }
            rowList.add(keyboardButtonsRow0);
        }
        if (listingPage.getNumber() > 0) {
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_NAME,
                    Constants.KEYBOARD_LISTING_BUTTON_PREVIOUS_COMMAND));
        }
        keyboardButtonsRow1.add(createInlineKeyboardButton(
                new StringBuilder().append("Item ")
                        .append(listingPage.getNumber() + 1)
                        .append(" of ")
                        .append(listingPage.getTotalElements()).toString(),
                Constants.KEYBOARD_LISTING_BUTTON_MIDDLE_COMMAND));
        if (listingPage.getNumber() + 1 < listingPage.getTotalPages()) {
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_LISTING_BUTTON_NEXT_NAME,
                    Constants.KEYBOARD_LISTING_BUTTON_NEXT_COMMAND));
        }
        rowList.add(keyboardButtonsRow1);

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

        ListingWithImage listingWithImage = imagePage.getContent().get(0);

        StringBuilder caption = new StringBuilder().append(listing.getTitle())
                .append(optionsText)
                .append("\n\n<b>Price: ")
                .append(optionPrice.orElse(listing.getPrice()))
                .append("</b>");

        InputFile inputFile = new InputFile();
        inputFile.setMedia(listingWithImage.getImageUrl());
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setCaption(caption.toString());
        sendPhoto.setParseMode("html");
        sendPhoto.setReplyMarkup(keyboardMarkup);
        return Optional.of(sendPhoto);
    }
}