package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {

    Optional<Listing> findByIdentifier(Integer id);

    List<Listing> findByIdentifierIn(List<Integer> identifiers);

    Page<Listing> findBySection_IdentifierAndActiveTrue(Integer id, Pageable pageable);

    Optional<Integer> countBySectionIdentifierAndActiveTrue(Integer sectionId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listings SET active = false WHERE EXTRACT(EPOCH FROM updated - " +
            "(SELECT * FROM (SELECT MAX(updated) FROM listings) as upd)) " +
            "< -?1", nativeQuery = true)
    void modifyExpiredEntities(Integer expirationTime);
}