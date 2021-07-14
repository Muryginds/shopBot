package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.ListingFavorite;
import org.telegram.galacticMiniatures.bot.model.User;

import java.util.List;

@Repository
@Transactional
public interface ListingFavoriteRepository
    extends JpaRepository<ListingFavorite, ListingFavorite.Key> {

    List<ListingFavorite> findAllById_User(User user);
    List<ListingFavorite> findAllById_User_ChatId(String chatId);

}