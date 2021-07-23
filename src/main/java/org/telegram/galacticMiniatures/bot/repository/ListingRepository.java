package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;

import java.util.Optional;

@Repository
@Transactional
public interface ListingRepository extends JpaRepository<Listing, Integer> {

    Optional<Listing> findByIdentifier(Integer id);

    Page<Listing> findBySection_IdentifierAndActiveTrue(Integer id, Pageable pageable);

    Integer countBySectionIdentifierAndActiveTrue(Integer sectionId);

    @Modifying
    @Query(value = "UPDATE listings SET active = 0 WHERE updated - " +
            "(SELECT * FROM (SELECT updated FROM listings ORDER BY updated DESC LIMIT 1) as upd) " +
            "< -?1", nativeQuery = true)
    void modifyExpiredEntities(Integer expirationTime);
}