package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.repository.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUser(Long chatId) {
        return userRepository.findByChatId(chatId.toString());
    }

    public Optional<User> findUser(String chatId) {
        return userRepository.findByChatId(chatId);
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    public User getUser(Message message) {
        return findUser(message.getChatId())
                .orElseGet(() -> add(new User(message)));
    }

    public User getUser(Long chatId) {
        return findUser(chatId)
                .orElseGet(() -> add(new User(chatId.toString(), chatId.toString())));
    }

    public User getUser(String chatId) {
        return findUser(chatId)
                .orElseGet(() -> add(new User(chatId, chatId)));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}