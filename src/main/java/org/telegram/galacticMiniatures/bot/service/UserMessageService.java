package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.UserMessage;
import org.telegram.galacticMiniatures.bot.repository.AnnouncementsResponse;
import org.telegram.galacticMiniatures.bot.repository.UserMessageRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserMessageService {

    private final UserMessageRepository userMessageRepository;
    private final UserService userService;

    public Page<UserMessage> getPageByChatId(Long chatId, Pageable pageable) {
        return userMessageRepository.getPageByUser_ChatIdAndOrderIsNullOrTargetUser_ChatIdAndOrderIsNull(
                chatId.toString(), chatId.toString(), pageable);
    }

    public Page<UserMessage> getPageByOrderId(Integer orderId, Pageable pageable) {
        return userMessageRepository.getPageByOrder_Id(orderId, pageable);
    }

    public void save(UserMessage userMessage) {
        userMessageRepository.save(userMessage);
    }

    public List<Map<String, String>> trackNewMessagesForUser(Long chatId) {
        return userMessageRepository.trackNewMessagesForUser(chatId.toString());
    }

    public List<Map<String, String>> trackNewMessagesForModerator(Long chatId) {
        return userMessageRepository.trackNewMessagesForModerator(chatId.toString());
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
    }

    public List<AnnouncementsResponse> getNewAnnouncements (){
        return userMessageRepository.getNewAnnouncements();
    }
}