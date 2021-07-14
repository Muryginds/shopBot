package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
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
}