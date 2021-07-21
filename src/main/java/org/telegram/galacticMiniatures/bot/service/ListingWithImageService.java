package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.repository.ListingWithImageRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedImage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListingWithImageService {

    private final ListingWithImageRepository listingWithImageRepository;

    @Transactional
    public void saveAllByParsedImageMap(Map<Listing, List<ParsedImage>> listingsMap) {
        List<ListingWithImage> list = new ArrayList<>();
        for (Map.Entry<Listing, List<ParsedImage>> entry : listingsMap.entrySet()) {
            for (ParsedImage parsedImage : entry.getValue()) {
                Optional<ListingWithImage> listingWithImage =
                        getByListingAndImageUrl(entry.getKey(), parsedImage.getImageUrl());
                ListingWithImage modifiedListingWithImage =
                        listingWithImage.orElse(new ListingWithImage(entry.getKey(),
                                parsedImage.getImageUrl(), null, null));
                modifiedListingWithImage.setUpdated(LocalDateTime.now());
                modifiedListingWithImage.setActive(true);
                list.add(modifiedListingWithImage);
            }
        }
        listingWithImageRepository.saveAll(list);
    }

    public Optional<ListingWithImage> getByListingAndImageUrl(Listing listing, String url) {
        return listingWithImageRepository.findByListingAndImageUrl(listing, url);
    }

    public List<ListingWithImage> getActiveByListingIdentifier(Integer listingId) {
        return listingWithImageRepository.findAllByListing_IdentifierAndActiveTrue(listingId);
    }

    public Optional<String> getImageByListingIdentifier(Integer listingId) {
        return getActiveByListingIdentifier(listingId).stream()
                .map(ListingWithImage::getImageUrl).findAny();
    }

    public Page<ListingWithImage> getPageImagesActiveByListing(Listing listing, Pageable pageable) {
        return listingWithImageRepository.findByListingAndActiveTrue(listing, pageable);
    }

    public void modifyExpiredEntities(Integer expirationTime) {
        listingWithImageRepository.modifyExpiredEntities(expirationTime);
    }
}