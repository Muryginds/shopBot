package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.repository.ListingRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedListing;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;

    @Transactional
    public List<Listing> saveAllByParsedListingMap(Map<Section, List<ParsedListing>> listingsMap) {
        List<Listing> updatedListings = new ArrayList<>();
        List<Integer> identifiers = listingsMap.values().stream()
                .flatMap(p -> p.stream().map(ParsedListing::getId))
                .collect(Collectors.toList());
        Map<Integer, Listing> map = findByIdentifierList(identifiers).stream()
                .collect(Collectors.toMap(Listing::getIdentifier, l -> l));
        for (Map.Entry<Section, List<ParsedListing>> entry : listingsMap.entrySet()) {
            for (ParsedListing parsedListing : entry.getValue()) {
                Listing updatedListing = map.getOrDefault(parsedListing.getId(), new Listing());
                updatedListing.setTitle(parsedListing.getTitle());
                updatedListing.setIdentifier(parsedListing.getId());
                updatedListing.setPrice((int)(parsedListing.getPrice() * 7) * 10);
                updatedListing.setSection(entry.getKey());
                updatedListing.setSkuNumber(
                        parsedListing.getSku().stream().reduce((s, s2) -> s + ", " + s2).orElse(""));
                updatedListing.setUpdated(LocalDateTime.now());
                updatedListing.setActive(true);
                updatedListings.add(updatedListing);
            }
        }
       return listingRepository.saveAll(updatedListings);
    }

    public Optional<Listing> findByIdentifier(Integer identifier) {
        return listingRepository.findByIdentifier(identifier);
    }

    public List<Listing> findByIdentifierList(List<Integer> identifiers) {
        return listingRepository.findByIdentifierIn(identifiers);
    }

    public Optional<Integer> countSizeActiveBySectionIdentifier(Integer sectionId) {
        return listingRepository.countBySectionIdentifierAndActiveTrue(sectionId);
    }

    public Page<Listing> findPageListingActiveBySectionIdentifier(Integer identifier, Pageable pageable) {
        return listingRepository.findBySection_IdentifierAndActiveTrue(identifier, pageable);
    }

    public void modifyExpiredEntities(Integer expirationTime) {
        listingRepository.modifyExpiredEntities(expirationTime);
    }
}