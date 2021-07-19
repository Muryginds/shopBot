package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.model.ListingFavorite;
import org.telegram.galacticMiniatures.bot.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ListingCartRepository
    extends JpaRepository<ListingCart, ListingCart.Key> {
    Page<ListingCart> findById_User_ChatId(String chatId, Pageable pageable);

    @Query(value = "SELECT SUM(l.price * uc.quantity) FROM user_cart uc " +
            "INNER JOIN users u on uc.user_id = u.id " +
            "INNER JOIN listings l on uc.listing_id = l.id " +
            "WHERE chat_id = :chatId",
            nativeQuery = true)
    Optional<Integer> countCartSummaryByChatId(@Param("chatId")String chatId);

    int countListingCartById_User_ChatId(String chatId);
}