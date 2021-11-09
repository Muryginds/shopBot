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

@Repository
public interface ListingWithImageRepository
    extends JpaRepository<ListingWithImage, Integer> {

    List<ListingWithImage> findAllByListing_IdentifierAndActiveTrue(Integer listingId);

    List<ListingWithImage> findAllByListingInAndActiveTrue(Iterable<Listing> list);

    Page<ListingWithImage> findByListingAndActiveTrue(Listing listing, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listings_images SET active = false WHERE EXTRACT(EPOCH FROM updated - " +
            "(SELECT * FROM (SELECT MAX(updated) FROM listings_images) as upd)) " +
            "< -?1", nativeQuery = true)
    void modifyExpiredEntities(Integer expirationTime);
}