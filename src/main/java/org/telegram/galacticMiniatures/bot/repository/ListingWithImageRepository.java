package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;
import org.telegram.galacticMiniatures.bot.model.ListingWithTag;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ListingWithImageRepository
    extends JpaRepository<ListingWithImage, Integer> {
    List<ListingWithImage> findAllByListing(Listing listing);
    Optional<ListingWithImage> findByListingAndImageUrl(Listing listing, String url);
    Page<ListingWithImage> findByListing(Listing listing, Pageable pageable);
}