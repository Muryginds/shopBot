package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithTag;
import org.telegram.galacticMiniatures.bot.model.Tag;
import org.telegram.galacticMiniatures.bot.repository.ListingWithTagRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedListing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListingWithTagsService {

    private final ListingService listingService;
    private final TagService tagService;
    private final ListingWithTagRepository listingWithTagRepository;

    public void saveAllByParsedListingCollection(Iterable<ParsedListing> collection) {
        List<ListingWithTag> listingsWithTags = new ArrayList<>();
        for (ParsedListing parsedListing : collection) {
            Optional<Listing> listing = listingService.getByIdentifier(parsedListing.getId());
            if (listing.isPresent()) {
                List<Tag> tags = tagService.getTagsByCollection(parsedListing.getTags());
                for (Tag tag : tags) {
                    ListingWithTag.Key key = new ListingWithTag.Key(listing.get(), tag);
                    Optional<ListingWithTag> listingWithTags = getByKey(key);
                    listingsWithTags.add(
                            listingWithTags.orElse(new ListingWithTag(key, LocalDateTime.now(), true)));
                }
            }
        }
        listingWithTagRepository.saveAll(listingsWithTags);
    }

    public Optional<ListingWithTag> getByKey(ListingWithTag.Key key) {
        return listingWithTagRepository.findById(key);
    }
}