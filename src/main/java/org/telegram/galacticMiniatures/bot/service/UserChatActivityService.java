package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.UserChatActivity;
import org.telegram.galacticMiniatures.bot.repository.UserChatActivityRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserChatActivityService {

    private final UserChatActivityRepository userChatActivityRepository;
    private final UserService userService;
    private final OrderService orderService;

    public void save(UserChatActivity userChatActivity) {
        userChatActivityRepository.save(userChatActivity);
    }

    public Optional<UserChatActivity> findByChatIdAndOrderId(String chatId, Integer orderId) {
        return userChatActivityRepository.findByUser_ChatIdAndOrder_Id(chatId, orderId);
    }

    public void saveNewChatActivity(Long chatId, Integer orderId) {
        Optional<Order> orderOptional = orderService.findById(orderId);
        orderOptional.ifPresent(o -> {
            Optional<UserChatActivity> optionalUCA =
                    findByChatIdAndOrderId(chatId.toString(), orderId);
            UserChatActivity chatActivity = optionalUCA.orElse(
                    new UserChatActivity(userService.getUser(chatId), o, null, null));
            chatActivity.setLastActivity(LocalDateTime.now());
            save(chatActivity);
        });
    }

    public void saveAll(Iterable<UserChatActivity> iterable) {
        userChatActivityRepository.saveAll(iterable);
    }
}