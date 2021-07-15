package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.repository.ListingCartRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ListingCartRepository listingCartRepository;

    public void save(ListingCart listingCart) {
        listingCartRepository.save(listingCart);
    }

    public void delete(ListingCart listingCart) {
        listingCartRepository.delete(listingCart);
    }

    public Optional<ListingCart> findById(ListingCart.Key key) {
        return listingCartRepository.findById(key);
    }

    public Page<ListingCart> getPageCartByChatId(String chatId, Pageable pageable) {
        return listingCartRepository.findById_User_ChatId(chatId, pageable);
    }

    public int getCartSummaryByChatId(String chatId) {
        return listingCartRepository.countCartSummaryByChatId(chatId);
    }
}