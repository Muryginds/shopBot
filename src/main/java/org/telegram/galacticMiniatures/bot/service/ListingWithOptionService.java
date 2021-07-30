package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
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
                                .get("currency_formatted_raw").replace(",", "")))
                        .findFirst().orElse(0D);

                int price = (int) Math.round(rawPrice * 7) * 10;

                Iterator<Map.Entry<String, String>> iterator = options.entrySet().iterator();

                String firstOptionName = "";
                String firstOptionValue = "";
                String secondOptionName = "" ;
                String secondOptionValue = "";

                if (iterator.hasNext()) {
                    var first = iterator.next();
                    firstOptionName = first.getKey();
                    firstOptionValue = first.getValue();
                }

                if (iterator.hasNext()) {
                    var second = iterator.next();
                    secondOptionName = second.getKey();
                    secondOptionValue = second.getValue();
                }

                Optional<ListingWithOption> listingWithOption =
                        findByListingAndParsedOption(entry.getKey(),
                                firstOptionName, firstOptionValue, secondOptionName, secondOptionValue);
                ListingWithOption modifiedListingWithOption =
                        listingWithOption.orElse(new ListingWithOption(entry.getKey(),
                                firstOptionName, firstOptionValue, secondOptionName, secondOptionValue,
                                price, null, null));
                modifiedListingWithOption.setUpdated(LocalDateTime.now());
                modifiedListingWithOption.setActive(true);
                list.add(modifiedListingWithOption);
            }
        }
        listingWithOptionRepository.saveAll(list);
    }

    public Optional<ListingWithOption> findByListingAndParsedOption(
            Listing listing, String firstOptionName, String firstOptionValue,
            String secondOptionName, String secondOptionValue) {
        return listingWithOptionRepository.
                findByListingAndFirstOptionNameAndFirstOptionValueAndSecondOptionNameAndSecondOptionValue(
                        listing, firstOptionName, firstOptionValue, secondOptionName, secondOptionValue);
    }

    public Page<ListingWithOption> findPageOptionByListing(Listing listing, Pageable pageable) {
        return listingWithOptionRepository.findByListingAndActiveTrue(listing, pageable);
    }

    public void modifyExpiredEntities(Integer expirationTime) {
        listingWithOptionRepository.modifyExpiredEntities(expirationTime);

    }
}