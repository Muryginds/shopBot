package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.Section;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ListingRepository extends JpaRepository<Listing, Integer> {
    Optional<Listing> getByIdentifier(Integer id);
    List<Listing> findAllBySection_Identifier(Integer id);
    Page<Listing> findBySection_Identifier(Integer id, Pageable pageable);
}