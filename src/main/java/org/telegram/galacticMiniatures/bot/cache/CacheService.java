package org.telegram.galacticMiniatures.bot.cache;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CacheService {

    private final Map<Long, ChatInfo> cache = new HashMap<>();

    public void add(long chatId, SearchInfo searchInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setSearchInfo(searchInfo);
        putNewData(chatId, chatInfo);
    }

    public void add(long chatId, FavoriteInfo favoriteInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setFavoriteInfo(favoriteInfo);
        putNewData(chatId, chatInfo);
    }

    public void add(long chatId, CartInfo cartInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setCartInfo(cartInfo);
        putNewData(chatId, chatInfo);
    }

    public void add(long chatId, OrderedListingsInfo orderedListingsInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setOrderedListingsInfo(orderedListingsInfo);
        putNewData(chatId, chatInfo);
    }

    public void add(long chatId, OrderInfo orderInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setOrderInfo(orderInfo);
        putNewData(chatId, chatInfo);
    }

    public void add(long chatId, OrderMessageInfo orderMessageInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setOrderMessageInfo(orderMessageInfo);
        putNewData(chatId, chatInfo);
    }

    public void add(long chatId, UserChatMessageInfo userChatMessageInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setUserChatMessageInfo(userChatMessageInfo);
        putNewData(chatId, chatInfo);
    }

    public void add(long chatId, MessagesInfo messagesInfo) {
        ChatInfo chatInfo = cache.getOrDefault(chatId, new ChatInfo());
        chatInfo.setMessagesInfo(messagesInfo);
        putNewData(chatId, chatInfo);
    }

    public ChatInfo get(long chatId) {
        Optional<ChatInfo> optional = Optional.ofNullable(cache.get(chatId));
        return optional.orElse(new ChatInfo());
    }

    private void putNewData(Long chatId, ChatInfo chatInfo) {
        chatInfo.setLastModified(LocalDateTime.now());
        cache.put(chatId, chatInfo);
    }

    public Map<Long, ChatInfo> getCache() {
        return cache;
    }

    public void remove(long chatId) {
        cache.remove(chatId);
    }
}