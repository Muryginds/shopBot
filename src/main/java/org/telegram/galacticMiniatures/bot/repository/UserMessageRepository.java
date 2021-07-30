package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telegram.galacticMiniatures.bot.model.UserMessage;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {

    Page<UserMessage> getPageByUser_ChatIdAndOrderIsNullOrTargetUser_ChatIdAndOrderIsNull(
            String chatId, String targetChatId, Pageable pageable);
    Page<UserMessage> getPageByOrder_Id(Integer orderId, Pageable pageable);

    @Query(value = "SELECT CAST(um.order_id AS CHAR) AS orderId, CAST(SUM(um.created > vu.last_activity) AS CHAR) " +
            "AS sum FROM user_messages um " +
            "JOIN (SELECT uca.order_id AS order_id , uca.last_activity AS last_activity FROM user_chat_activity uca " +
            "JOIN users u on uca.user_id = u.id " +
            "WHERE u.chat_id = ?1) vu ON vu.order_id = um.order_id " +
            "GROUP BY um.order_id " +
            "ORDER BY sum DESC", nativeQuery = true)
    List<Map<String, String>> trackNewMessagesForUser(String chatId);

    @Query(value = "SELECT CAST(um.order_id AS CHAR) AS orderId,\n" +
            "CAST(SUM(um.created > COALESCE (vu.last_activity, '01-01-01')) AS CHAR) as sum " +
            "FROM user_messages um " +
            "LEFT JOIN (SELECT uca.order_id AS order_id , uca.last_activity AS last_activity " +
            "FROM user_chat_activity uca " +
            "JOIN users u on uca.user_id = u.id " +
            "WHERE u.chat_id = ?1) vu ON vu.order_id = um.order_id " +
            "WHERE um.order_id IS NOT NULL " +
            "GROUP BY um.order_id " +
            "ORDER BY sum DESC, orderId DESC", nativeQuery = true)
    List<Map<String, String>> trackNewMessagesForAdmin(String chatId);

    @Query(value = "SELECT vu.chat_id AS chatId, um.order_id AS orderId, " +
            "SUM(um.created > COALESCE (vu.last_activity, '01-01-01')) AS sum " +
            "FROM user_messages um " +
            "LEFT JOIN (SELECT uca.order_id, uca.last_activity, u.chat_id FROM user_chat_activity uca " +
            "JOIN users u on uca.user_id = u.id WHERE uca.last_activity > COALESCE (uca.announced, '01-01-01')) vu " +
            "ON vu.order_id = um.order_id " +
            "WHERE um.order_id IS NOT NULL " +
            "GROUP BY um.order_id, vu.chat_id " +
            "HAVING SUM(um.created > COALESCE (vu.last_activity, '01-01-01')) > 0 " +
            "ORDER BY vu.chat_id", nativeQuery = true)
    List<AnnouncementsResponse> getNewAnnouncements();
}