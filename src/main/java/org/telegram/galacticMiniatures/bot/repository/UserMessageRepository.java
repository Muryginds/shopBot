package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.UserMessage;

@Repository
@Transactional
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {

    Page<UserMessage> getPageByUser_ChatIdAndOrderIsNullOrTargetUser_ChatIdAndOrderIsNull(
            String chatId, String targetChatId, Pageable pageable);
    Page<UserMessage> getPageByOrder_Id(Integer orderId, Pageable pageable);
}