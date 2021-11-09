package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.UserMessage;
import org.telegram.galacticMiniatures.bot.repository.response.AnnouncementsResponse;
import org.telegram.galacticMiniatures.bot.repository.response.NewMessagesResponse;
import org.telegram.galacticMiniatures.bot.repository.UserMessageRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMessageService {

    private final UserMessageRepository userMessageRepository;
    private final UserChatActivityService userChatActivityService;
    private final UserService userService;

    public Page<UserMessage> getPageByChatId(Long chatId, Pageable pageable) {
        return userMessageRepository.getPageByUser_ChatIdAndOrderIsNull(chatId.toString(), pageable);
    }

    public Page<UserMessage> getPageByOrderId(Integer orderId, Pageable pageable) {
        return userMessageRepository.getPageByOrder_Id(orderId, pageable);
    }

    public void save(UserMessage userMessage) {
        userMessageRepository.save(userMessage);
    }

    public List<NewMessagesResponse> trackMessagesForUser(Long chatId) {
        return userMessageRepository.trackMessagesForUser(chatId.toString());
    }

    public Page<NewMessagesResponse> trackMessagesForModerator(Long chatId, Pageable pageable) {
        return userMessageRepository.trackMessagesForModerator(chatId.toString(), pageable);
    }

    public void announceNewOrder(Long chatId, Order order) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUser(userService.getUser(chatId));
        userMessage.setOrder(order);
        userMessage.setMessage(new StringBuilder()
                .append("New order ")
                .append(String.format("%05d" , order.getId()))
                .append(" created")
                .toString());
        userMessage.setCreated(LocalDateTime.now());
        save(userMessage);

        userChatActivityService.createNewChatActivity(chatId, order);
    }

    public void announceOrderStatusChanged(Long chatId, Order order) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUser(userService.getUser(chatId));
        userMessage.setOrder(order);
        userMessage.setMessage(new StringBuilder()
                .append("Order ")
                .append(String.format("%05d" , order.getId()))
                .append(" status changed to *")
                .append(order.getStatus())
                .append("*")
                .toString());
        userMessage.setCreated(LocalDateTime.now());
        save(userMessage);

        userChatActivityService.saveChatActivity(chatId, order);
    }

    public List<AnnouncementsResponse> getNewAnnouncements(){
        return userMessageRepository.getNewAnnouncements();
    }
}