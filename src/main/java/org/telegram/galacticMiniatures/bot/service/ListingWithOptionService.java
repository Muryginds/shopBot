package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;
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
        List<ListingWithOption> updatedList = new ArrayList<>();
        Map<String, List<ListingWithOption>> current = findByIdentifiers(listingsMap.keySet()).stream()
                .collect(Collectors.groupingBy(l -> l.getListing().getSkuNumber()));
        for (Map.Entry<Listing, List<ParsedOption>> entry : listingsMap.entrySet()) {
            Listing listing = entry.getKey();
            String skuNumber = listing.getSkuNumber();
            Map<String, ListingWithOption> map = new HashMap<>();
            if (current.containsKey(skuNumber)) {
                map = current.get(skuNumber).stream()
                        .collect(Collectors.toMap(k ->
                                new StringBuilder()
                                        .append(k.getFirstOptionName())
                                        .append(k.getFirstOptionValue())
                                        .append(k.getSecondOptionName())
                                        .append(k.getSecondOptionValue())
                                        .toString(), l -> l));
            }
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

                String key = new StringBuilder()
                        .append(firstOptionName)
                        .append(firstOptionValue)
                        .append(secondOptionName)
                        .append(secondOptionValue)
                        .toString();

                ListingWithOption listingWithOption = map.getOrDefault(key,
                        new ListingWithOption(null, listing,
                                firstOptionName, firstOptionValue, secondOptionName, secondOptionValue,
                                 null, null, null));
                listingWithOption.setPrice(price);
                listingWithOption.setUpdated(LocalDateTime.now());
                listingWithOption.setActive(true);
                updatedList.add(listingWithOption);
            }
        }
        listingWithOptionRepository.saveAll(updatedList);
    }

    public List<ListingWithOption> findByIdentifiers(Iterable<Listing> listings) {
        return listingWithOptionRepository.findAllByListingIn(listings);
    }

    public Page<ListingWithOption> findPageOptionByListing(Listing listing, Pageable pageable) {
        return listingWithOptionRepository.findByListingAndActiveTrue(listing, pageable);
    }

    public void modifyExpiredEntities(Integer expirationTime) {
        listingWithOptionRepository.modifyExpiredEntities(expirationTime);
    }
}