package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.ListingWithImage;

import java.util.List;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<ListingWithImage, Integer> {
    //List<ListingWithImage> getByListing(Listing listing);
}