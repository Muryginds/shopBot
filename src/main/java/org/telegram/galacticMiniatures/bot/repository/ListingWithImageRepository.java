package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ListingWithImageRepository
    extends JpaRepository<ListingWithImage, Integer> {

    List<ListingWithImage> findAllByListing_IdentifierAndActiveTrue(Integer listingId);

    Optional<ListingWithImage> findByListingAndImageUrl(Listing listing, String url);

    Page<ListingWithImage> findByListingAndActiveTrue(Listing listing, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE listings_images SET active = 0 WHERE updated - " +
            "(SELECT * FROM (SELECT updated FROM listings_images ORDER BY updated DESC LIMIT 1) as upd) " +
            "< -?1", nativeQuery = true)
    void modifyExpiredEntities(Integer expirationTime);
}