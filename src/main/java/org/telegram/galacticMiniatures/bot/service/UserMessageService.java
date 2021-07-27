package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.UserMessage;
import org.telegram.galacticMiniatures.bot.repository.UserMessageRepository;

@Service
@RequiredArgsConstructor
public class UserMessageService {

    private final UserMessageRepository userMessageRepository;

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

}