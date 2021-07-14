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
                Optional<Listing> listing = getByIdentifier(parsedListing.getId());
                Listing modifiedListing = listing.orElse(new Listing(parsedListing.getTitle(), parsedListing.getId(),
                        parsedListing.getDescription(), parsedListing.getPrice(),
                        entry.getKey(), null));
                modifiedListing.setLastModified(LocalDateTime.now());
                listings.add(modifiedListing);
            }
        }
       return listingRepository.saveAll(listings);
    }

    public Optional<Listing> getByIdentifier(Integer identifier) {
        return listingRepository.getByIdentifier(identifier);
    }

    public List<Listing> getListingsBySectionIdentifier(Integer identifier) {
        return listingRepository.findAllBySection_Identifier(identifier);
    }

    public Page<Listing> getPageListingBySectionIdentifier(Integer identifier, Pageable pageable) {
        return listingRepository.findBySection_Identifier(identifier, pageable);
    }
}