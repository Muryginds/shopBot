package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.repository.ListingWithImageRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingWithImageService {

    private final ListingWithImageRepository listingWithImageRepository;
    private final ListingService listingService;

    public void saveAllByParsedImageMap(Map<Listing, List<ParsedImage>> listingsMap) {
        List<ListingWithImage> list = new ArrayList<>();
        for (Map.Entry<Listing, List<ParsedImage>> entry : listingsMap.entrySet()) {
            for (ParsedImage parsedImage : entry.getValue()) {
                Optional<ListingWithImage> listingWithImage =
                        getByListingAndImageUrl(entry.getKey(), parsedImage.getImageUrl());
                ListingWithImage modifiedListingWithImage =
                        listingWithImage.orElse(new ListingWithImage(entry.getKey(),
                                parsedImage.getImageUrl(), null));
                modifiedListingWithImage.setLastModified(LocalDateTime.now());
                list.add(modifiedListingWithImage);
            }
        }
        listingWithImageRepository.saveAll(list);
    }

    public Optional<ListingWithImage> getByListingAndImageUrl(Listing listing, String url) {
        return listingWithImageRepository.findByListingAndImageUrl(listing, url);
    }

    public List<ListingWithImage> getByListingIdentifier(Integer listingId) {
        Optional<Listing> result = listingService.getByIdentifier(listingId);
        return listingWithImageRepository.findAllByListing(result.orElse(new Listing()));
    }

    public List<String> getImagesByListingIdentifier(Integer listingId) {
        return getByListingIdentifier(listingId).stream()
                .map(ListingWithImage::getImageUrl).collect(Collectors.toList());
    }

    public Page<ListingWithImage> getPageImagesByListing(Listing listing, Pageable pageable) {
        return listingWithImageRepository.findByListing(listing, pageable);
    }
}