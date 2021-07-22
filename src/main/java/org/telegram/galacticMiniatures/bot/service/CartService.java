package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.repository.ListingCartRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ListingCartRepository listingCartRepository;

    public void save(ListingCart listingCart) {
        listingCartRepository.save(listingCart);
    }

    public void delete(ListingCart listingCart) {
        listingCartRepository.deleteById(listingCart.getId());
    }

    public Optional<ListingCart> findById(ListingCart.Key key) {
        return listingCartRepository.findById(key);
    }

    public List<ListingCart> findAllByUser(User user) {
        return listingCartRepository.findAllById_User(user);
    }

    public Page<ListingCart> findPageCartByChatId(Long chatId, Pageable pageable) {
        return listingCartRepository.findById_User_ChatId(chatId.toString(), pageable);
    }

    public int countSizeCartByChatId(Long chatId) {
        return listingCartRepository.countListingCartById_User_ChatId(chatId.toString());
    }

    public Optional<Integer> getCartSummaryByChatId(Long chatId) {
        return listingCartRepository.countCartSummaryByChatId(chatId.toString());
    }

    public void deleteAllByChatId(User user) {
        listingCartRepository.deleteAllById_User(user);
    }
}