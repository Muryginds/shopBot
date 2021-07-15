package org.telegram.galacticMiniatures.bot.cache;

import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.enums.BotState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CacheService {

    private final Map<Long, ChatInfo> cache = new HashMap<>();

    public void add(long chatId) {
        cache.put(chatId, new ChatInfo());
    }

    public void add(long chatId, SearchInfo searchInfo) {
        if(cache.containsKey(chatId)) {
            ChatInfo chatInfo = cache.get(chatId);
            chatInfo.setSearchInfo(searchInfo);
        } else {
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setSearchInfo(searchInfo);
            cache.put(chatId, chatInfo);
        }
    }

    public void add(long chatId, FavoriteInfo favoriteInfo) {
        if(cache.containsKey(chatId)) {
            ChatInfo chatInfo = cache.get(chatId);
            chatInfo.setFavoriteInfo(favoriteInfo);
        } else {
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setFavoriteInfo(favoriteInfo);
            cache.put(chatId, chatInfo);
        }
    }

    public void add(long chatId, CartInfo cartInfo) {
        if(cache.containsKey(chatId)) {
            ChatInfo chatInfo = cache.get(chatId);
            chatInfo.setCartInfo(cartInfo);
        } else {
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setCartInfo(cartInfo);
            cache.put(chatId, chatInfo);
        }
    }

    public void add(long chatId, BotState botState) {
        if(cache.containsKey(chatId)) {
            ChatInfo chatInfo = cache.get(chatId);
            chatInfo.setBotState(botState);
        } else {
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setBotState(botState);
            cache.put(chatId, chatInfo);
        }
    }

    public Optional<ChatInfo> get(long chatId) {
        return Optional.ofNullable(cache.get(chatId));
    }

    public void remove(long chatId) {
        cache.remove(chatId);
    }
}