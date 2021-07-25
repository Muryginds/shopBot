package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.repository.ListingRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedListing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;

    public List<Listing> saveAllByParsedListingMap(Map<Section, List<ParsedListing>> listingsMap) {
        List<Listing> listings = new ArrayList<>();
        for (Map.Entry<Section, List<ParsedListing>> entry : listingsMap.entrySet()) {
            for (ParsedListing parsedListing : entry.getValue()) {
                Optional<Listing> listing = findByIdentifier(parsedListing.getId());
                Listing modifiedListing = listing.orElse(new Listing());
                modifiedListing.setTitle(parsedListing.getTitle());
                modifiedListing.setIdentifier(parsedListing.getId());
                modifiedListing.setPrice((int)(parsedListing.getPrice() * 7) * 10);
                modifiedListing.setSection(entry.getKey());
                modifiedListing.setSkuNumber(
                        parsedListing.getSku().stream().reduce((s, s2) -> s + " " + s2).orElse(""));
                modifiedListing.setUpdated(LocalDateTime.now());
                modifiedListing.setActive(true);
                listings.add(modifiedListing);
            }
        }
       return listingRepository.saveAll(listings);
    }

    public Optional<Listing> findByIdentifier(Integer identifier) {
        return listingRepository.findByIdentifier(identifier);
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