package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.repository.ListingFavoriteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingFavoriteService {

    private final ListingFavoriteRepository listingFavoriteRepository;

    public void save(ListingFavorite listingFavorite) {
        listingFavoriteRepository.save(listingFavorite);
    }

    public void delete(ListingFavorite listingFavorite) {
        listingFavoriteRepository.delete(listingFavorite);
    }

    public List<ListingFavorite> getListingsByUser(User user) {
        return listingFavoriteRepository.findAllById_User(user);
    }

    public List<ListingFavorite> getListingsByChatId(String chatId) {
        return listingFavoriteRepository.findAllById_User_ChatId(chatId);
    }

    public Page<ListingFavorite> getPageFavoriteByChatId(String chatId, Pageable pageable) {
        return listingFavoriteRepository.findById_User_ChatId(chatId, pageable);
    }

    public void deleteByUserIdAndListingIdentifier(String chatId, Integer listingIdentifier) {
        listingFavoriteRepository.deleteById_User_ChatIdAndId_Listing_Identifier(chatId, listingIdentifier);
    }
}