package org.telegram.galacticMiniatures.bot.cache;

import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.enums.BotState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CacheService {

    private final Map<Long, ChatInfo> cache = new HashMap<>();

    public void add(long chatId, SearchInfo searchInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setSearchInfo(searchInfo);
        cache.put(chatId, chatInfo);
    }

    public void add(long chatId, FavoriteInfo favoriteInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setFavoriteInfo(favoriteInfo);
        cache.put(chatId, chatInfo);
    }

    public void add(long chatId, CartInfo cartInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setCartInfo(cartInfo);
        cache.put(chatId, chatInfo);
    }

    public void add(long chatId, BotState botState) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setBotState(botState);
        cache.put(chatId, chatInfo);
    }

    public ChatInfo get(long chatId) {
        Optional<ChatInfo> optional = Optional.ofNullable(cache.get(chatId));
        return optional.orElse(new ChatInfo());
    }

    public void remove(long chatId) {
        cache.remove(chatId);
    }
}