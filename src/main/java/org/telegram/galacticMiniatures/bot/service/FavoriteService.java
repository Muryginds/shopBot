package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.repository.ListingFavoriteRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final ListingFavoriteRepository listingFavoriteRepository;

    public void save(ListingFavorite listingFavorite) {
        listingFavoriteRepository.save(listingFavorite);
    }

    public void delete(ListingFavorite listingFavorite) {
        listingFavoriteRepository.delete(listingFavorite);
    }

    public Page<ListingFavorite> findPageFavoriteByChatId(Long chatId, Pageable pageable) {
        return listingFavoriteRepository.findById_User_ChatId(chatId.toString(), pageable);
    }

    public Optional<Integer> countSizeFavoriteByChatId(Long chatId) {
        return listingFavoriteRepository.countListingFavoriteById_User_ChatId(chatId.toString());
    }
}