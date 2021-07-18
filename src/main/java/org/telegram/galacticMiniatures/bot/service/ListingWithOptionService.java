package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithOption;
import org.telegram.galacticMiniatures.bot.repository.ListingWithOptionRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedOption;
import org.telegram.galacticMiniatures.parser.entity.ParsedOptionValues;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingWithOptionService {

    private final ListingWithOptionRepository listingWithOptionRepository;

    public void saveAllByParsedOptionMap(Map<Listing, List<ParsedOption>> listingsMap) {
        List<ListingWithOption> list = new ArrayList<>();
        for (Map.Entry<Listing, List<ParsedOption>> entry : listingsMap.entrySet()) {
            for (ParsedOption parsedOption : entry.getValue()) {

                Map<String, String> options = parsedOption.getParsedOptionValues().stream()
                        .collect(Collectors.toMap(
                                ParsedOptionValues::getPropertyName,
                                p -> p.getValues().stream().reduce((s, s2) -> s + " " + s2)
                                        .orElse("")));

                double rawPrice = parsedOption.getParsedOptionOfferings().stream()
                        .map(p -> Double.parseDouble(p.getPrice()
                                .get("currency_formatted_raw")))
                        .findFirst().orElse(0D);

                Integer price = (int) (rawPrice * 7) * 10;

                Iterator<Map.Entry<String, String>> iterator = options.entrySet().iterator();
                var first = iterator.next();

                String firstOptionName = first.getKey();
                String firstOptionValue = first.getValue();
                String secondOptionName = "" ;
                String secondOptionValue = "";

                if (iterator.hasNext()) {
                    var second = iterator.next();
                    secondOptionName = second.getKey();
                    secondOptionValue = second.getValue();
                }

                Optional<ListingWithOption> listingWithOption =
                        getByListingAndParsedOption(entry.getKey(),
                                firstOptionName, firstOptionValue, secondOptionName, secondOptionValue);
                ListingWithOption modifiedListingWithOption =
                        listingWithOption.orElse(new ListingWithOption(entry.getKey(),
                                firstOptionName, firstOptionValue, secondOptionName, secondOptionValue,
                                price, null));
                modifiedListingWithOption.setUpdated(LocalDateTime.now());
                list.add(modifiedListingWithOption);
            }
        }
        listingWithOptionRepository.saveAll(list);
    }

    public Optional<ListingWithOption> getByListingAndParsedOption(
            Listing listing, String firstOptionName, String firstOptionValue,
            String secondOptionName, String secondOptionValue) {
        return listingWithOptionRepository.
                findByListingAndFirstOptionNameAndFirstOptionValueAndSecondOptionNameAndSecondOptionValue(
                        listing, firstOptionName, firstOptionValue, secondOptionName, secondOptionValue);
    }

    public Page<ListingWithOption> getPageImagesByListing(Listing listing, Pageable pageable) {
        return listingWithOptionRepository.findByListing(listing, pageable);
    }
}