package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.OrderedListing;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OrderedListingRepository
    extends JpaRepository<OrderedListing, OrderedListing.Key> {

    List<OrderedListing> findById_Order(Order order);

    Page<OrderedListing> findById_Order_Id(Integer orderId, Pageable pageable);

    @Query(value = "SELECT SUM(lo.price * ol.quantity) FROM ordered_listings ol " +
            "INNER JOIN listings_options lo on ol.option_id = lo.id " +
            "WHERE order_id = :orderId",
            nativeQuery = true)
    Optional<Integer> countOrderSummaryByOrderId(@Param("orderId")int orderId);
}