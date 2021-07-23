package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.OrderedListing;
import org.telegram.galacticMiniatures.bot.repository.OrderedListingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderedListingService {

    private final OrderedListingRepository orderedListingRepository;

    public void saveAll(Iterable<OrderedListing> collection) {
        orderedListingRepository.saveAll(collection);
    }

    public List<OrderedListing> findByOrder(Order order) {
        return orderedListingRepository.findById_Order(order);
    }


}