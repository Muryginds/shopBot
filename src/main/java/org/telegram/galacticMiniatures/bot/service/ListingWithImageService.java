package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.repository.ListingWithImageRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedImage;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingWithImageService {

    private final ListingWithImageRepository listingWithImageRepository;

    @Transactional
    public void saveAllByParsedImageMap(Map<Listing, List<ParsedImage>> listingsMap) {
        List<ListingWithImage> updatedList = new ArrayList<>();
        Map<String, List<ListingWithImage>> current = findByIdentifiers(listingsMap.keySet()).stream()
                .collect(Collectors.groupingBy(l -> l.getListing().getSkuNumber()));
        for (Map.Entry<Listing, List<ParsedImage>> entry : listingsMap.entrySet()) {
            Listing listing = entry.getKey();
            String skuNumber = listing.getSkuNumber();
            Map<String, ListingWithImage> map = new HashMap<>();
            if (current.containsKey(skuNumber)) {
                map = current.get(skuNumber).stream()
                        .collect(Collectors.toMap(ListingWithImage::getImageUrl, l -> l));
            }
            for (ParsedImage parsedImage : entry.getValue()) {
                String image = parsedImage.getImageUrl();
                ListingWithImage listingWithImage = map.getOrDefault(image,
                        new ListingWithImage(null, listing, image, null, null));
                listingWithImage.setUpdated(LocalDateTime.now());
                listingWithImage.setActive(true);
                updatedList.add(listingWithImage);
            }
        }
        listingWithImageRepository.saveAll(updatedList);
    }

    public Optional<String> findAnyImageByListingIdentifier(Integer listingId) {
        return listingWithImageRepository.findAllByListing_IdentifierAndActiveTrue(listingId).stream()
                .map(ListingWithImage::getImageUrl)
                .findAny();
    }

    private List<ListingWithImage> findByIdentifiers(Iterable<Listing> listings) {
        return listingWithImageRepository.findAllByListingInAndActiveTrue(listings);
    }

    public Page<ListingWithImage> findPageImagesActiveByListing(Listing listing, Pageable pageable) {
        return listingWithImageRepository.findByListingAndActiveTrue(listing, pageable);
    }

    public void modifyExpiredEntities(Integer expirationTime) {
        listingWithImageRepository.modifyExpiredEntities(expirationTime);
    }
}