package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.ListingFavorite;

import java.util.List;

@Repository
@Transactional
public interface ListingFavoriteRepository
    extends JpaRepository<ListingFavorite, ListingFavorite.Key> {

    List<ListingFavorite> findAllById_User_ChatId(String chatId);

    Page<ListingFavorite> getById_User_ChatId(String chatId, Pageable pageable);

    int countListingFavoriteById_User_ChatId(String chatId);
}