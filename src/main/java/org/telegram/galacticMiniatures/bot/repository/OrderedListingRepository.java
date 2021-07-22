package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.OrderedListing;

import java.util.List;

@Repository
@Transactional
public interface OrderedListingRepository
    extends JpaRepository<OrderedListing, OrderedListing.Key> {

    List<OrderedListing> findById_Order(Order order);
}