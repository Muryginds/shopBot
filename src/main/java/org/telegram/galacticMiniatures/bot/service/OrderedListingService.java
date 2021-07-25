package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.OrderedListing;
import org.telegram.galacticMiniatures.bot.repository.OrderedListingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderedListingService {

    private final OrderedListingRepository orderedListingRepository;

    public Page<OrderedListing> findPageByOrderId(Integer orderId, Pageable pageable) {
        return orderedListingRepository.findById_Order_Id(orderId, pageable);
    }

    public void saveAll(Iterable<OrderedListing> collection) {
        orderedListingRepository.saveAll(collection);
    }

    public List<OrderedListing> findByOrder(Order order) {
        return orderedListingRepository.findById_Order(order);
    }

    public Optional<Integer> getOrderSummary(Integer orderId) {
        return orderedListingRepository.countOrderSummaryByOrderId(orderId);
    }

    public void delete(OrderedListing orderedListing) {
        orderedListingRepository.delete(orderedListing);
    }

    public void save(OrderedListing orderedListing) {
        orderedListingRepository.save(orderedListing);
    }
}