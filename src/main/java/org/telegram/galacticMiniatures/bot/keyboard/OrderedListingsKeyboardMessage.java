package org.telegram.galacticMiniatures.bot.keyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.cache.CacheService;
import org.telegram.galacticMiniatures.bot.cache.OrderedListingsInfo;
import org.telegram.galacticMiniatures.bot.enums.ScrollerObjectType;
import org.telegram.galacticMiniatures.bot.enums.ScrollerType;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithOption;
import org.telegram.galacticMiniatures.bot.model.OrderedListing;
import org.telegram.galacticMiniatures.bot.service.ListingWithImageService;
import org.telegram.galacticMiniatures.bot.service.OrderedListingService;
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
public class OrderedListingsKeyboardMessage implements AbstractKeyboardMessage, Scrollable {

    private final CacheService cacheService;
    private final OrderedListingService orderedListingService;
    private final ListingWithImageService listingWithImageService;

    @Override
    public Optional<PartialBotApiMethod<?>> prepareScrollingMessage(Long chatId,
                                                                    ScrollerType scrollerType,
                                                                    ScrollerObjectType scrollerObjectType) {

        OrderedListingsInfo orderedListingsInfo = cacheService.get(chatId).getOrderedListingsInfo();
        Pageable listingPageable;

        if (scrollerType == ScrollerType.NEW) {
            Sort sort = Sort.by("id.listing").and(Sort.by("id.option"));
            listingPageable = getPageableByScrollerType(orderedListingsInfo.getListingPageable(), scrollerType, sort);
        } else {
            listingPageable = getPageableByScrollerType(orderedListingsInfo.getListingPageable(), scrollerType);
        }

        Page<OrderedListing> listingPage =
                orderedListingService.findPageByOrderId(orderedListingsInfo.getOrderId(), listingPageable);

        OrderedListing orderedListing;
        try {
            orderedListing = listingPage.getContent().get(0);
        } catch (IndexOutOfBoundsException ex) {
            log.error("OrderedListing for ChatId: " + chatId + " not found. " + ex.getMessage());
            return Optional.empty();
        }

        Listing listing = orderedListing.getId().getListing();
        String imageUrl = listingWithImageService.
                findAnyImageByListingIdentifier(listing.getIdentifier()).orElse("");
        ListingWithOption listingWithOption = orderedListing.getId().getOption();
        int orderId = orderedListing.getId().getOrder().getId();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        if(listingPage.getTotalPages() > 1) {
            String listingPreviousCommand = Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK;
            if (listingPage.getNumber() > 0) {
                listingPreviousCommand = Constants.KEYBOARD_ORDEREDLISTING_BUTTON_PREVIOUS_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_ORDEREDLISTING_BUTTON_PREVIOUS_NAME, listingPreviousCommand));

            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    new StringBuilder()
                            .append(listingPage.getNumber() + 1)
                            .append(" / ")
                            .append(listingPage.getTotalElements()).toString(),
                    Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK));

            String listingNextCommand = Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK;
            if (listingPage.getNumber() + 1 < listingPage.getTotalPages()) {
                listingNextCommand = Constants.KEYBOARD_ORDEREDLISTING_BUTTON_NEXT_COMMAND;
            }
            keyboardButtonsRow1.add(createInlineKeyboardButton(
                    Constants.KEYBOARD_ORDEREDLISTING_BUTTON_NEXT_NAME, listingNextCommand));
            rowList.add(keyboardButtonsRow1);
        }

        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_ADD_MINUS_NAME,
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_ADD_MINUS_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                orderedListing.getQuantity().toString(),
                Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_ADD_PLUS_NAME,
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_ADD_PLUS_COMMAND));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                "Price:",
                Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK));
        keyboardButtonsRow3.add(createInlineKeyboardButton(
                String.format("%d", listingWithOption.getPrice() * orderedListing.getQuantity()),
                Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK));
        rowList.add(keyboardButtonsRow3);

        keyboardButtonsRow4.add(createInlineKeyboardButton(
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_REMOVE_FROM_ORDER_NAME,
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_REMOVE_FROM_ORDER_COMMAND));
        keyboardButtonsRow4.add(createInlineKeyboardButton(
                "Order Total: " + orderedListingService.getOrderSummary(orderId).orElse(0),
                Constants.KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK));
        rowList.add(keyboardButtonsRow4);

        keyboardButtonsRow2.add(createInlineKeyboardButton(
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_GO_BACK_NAME,
                Constants.KEYBOARD_ORDEREDLISTING_BUTTON_GO_BACK_COMMAND));
        rowList.add(keyboardButtonsRow2);
        keyboardMarkup.setKeyboard(rowList);

        orderedListingsInfo.setListingPageable(listingPageable);
        cacheService.add(chatId, orderedListingsInfo);

        StringBuilder optionsText = new StringBuilder();
        if (!"".equals(listingWithOption.getFirstOptionName())) {
            optionsText
                .append("<b>")
                .append("\n\n")
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
        }

        StringBuilder caption = new StringBuilder()
                .append("EDITING  <b>[ORDER №")
                .append(String.format("%05d" , orderId))
                .append("]</b>\n\n")
                .append(listing.getTitle())
                .append(optionsText)
                .append("\n\n<b>Price: ")
                .append(listingWithOption.getPrice())
                .append("</b>");

        InputFile inputFile = new InputFile();
        inputFile.setMedia(imageUrl);
        return getSendPhoto(chatId, keyboardMarkup, caption, inputFile);
    }
}