package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telegram.galacticMiniatures.bot.model.UserChatActivity;

import java.util.Optional;

@Repository
public interface UserChatActivityRepository extends JpaRepository<UserChatActivity, Integer> {

    Optional<UserChatActivity> findByUser_ChatIdAndOrder_Id(String chatId, Integer orderId);
}