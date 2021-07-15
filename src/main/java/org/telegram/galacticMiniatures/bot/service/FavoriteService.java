package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.repository.ListingFavoriteRepository;

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

    public Page<ListingFavorite> getPageFavoriteByChatId(String chatId, Pageable pageable) {
        return listingFavoriteRepository.getById_User_ChatId(chatId, pageable);
    }
}