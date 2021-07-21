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
import org.telegram.galacticMiniatures.bot.model.ListingWithOption;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ListingWithOptionRepository
    extends JpaRepository<ListingWithOption, Integer> {

    Page<ListingWithOption> findByListingAndActiveTrue(Listing listing, Pageable pageable);

    Optional<ListingWithOption>
    findByListingAndFirstOptionNameAndFirstOptionValueAndSecondOptionNameAndSecondOptionValue(
            Listing listing, String firstOptionName, String firstOptionValue,
            String secondOptionName, String secondOptionValue);

    @Modifying
    @Query(value = "UPDATE listings_options SET active = 0 WHERE updated - " +
            "(SELECT * FROM (SELECT updated FROM listings_options ORDER BY updated DESC LIMIT 1) as upd) " +
            "< -?1", nativeQuery = true)
    void modifyExpiredEntities(Integer expirationTime);
}