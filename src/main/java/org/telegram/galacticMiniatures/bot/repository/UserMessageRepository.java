package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telegram.galacticMiniatures.bot.model.UserMessage;
import org.telegram.galacticMiniatures.bot.repository.response.AnnouncementsResponse;
import org.telegram.galacticMiniatures.bot.repository.response.NewMessagesResponse;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {

    Page<UserMessage> getPageByUser_ChatIdAndOrderIsNullOrTargetUser_ChatIdAndOrderIsNull(
            String chatId, String targetChatId, Pageable pageable);
    Page<UserMessage> getPageByOrder_Id(Integer orderId, Pageable pageable);

    @Query(value =
            "SELECT o.id AS orderId, CAST(COALESCE(SUM(um.created > uca.last_activity), 0) AS UNSIGNED) AS sum " +
            "FROM orders o " +
            "JOIN users u ON o.user_id = u.id " +
            "LEFT JOIN user_chat_activity uca ON uca.order_id = o.id AND o.user_id = uca.user_id " +
            "LEFT JOIN user_messages um ON o.id = um.order_id " +
            "WHERE u.chat_id = ?1 " +
            "GROUP BY o.id " +
            "ORDER BY SUM DESC, o.id DESC", nativeQuery = true)
    List<NewMessagesResponse> trackMessagesForUser(String chatId);

    @Query(value =
            "SELECT um.order_id AS orderId, " +
            "CAST(SUM(um.created > COALESCE (vu.last_activity, '01-01-01')) AS UNSIGNED) AS sum " +
            "FROM user_messages um " +
            "LEFT JOIN (SELECT uca.order_id AS order_id, uca.last_activity AS last_activity " +
            "FROM user_chat_activity uca " +
            "JOIN users u on uca.user_id = u.id " +
            "WHERE u.chat_id = ?1) vu ON vu.order_id = um.order_id " +
            "WHERE um.order_id IS NOT NULL " +
            "GROUP BY um.order_id " +
            "ORDER BY sum DESC, um.order_id DESC",
            countQuery =
            "SELECT COUNT(*) FROM (SELECT um.order_id, " +
            "SUM(um.created > COALESCE (vu.last_activity, '01-01-01')) as sum " +
            "FROM user_messages um " +
            "LEFT JOIN (SELECT uca.order_id AS order_id, uca.last_activity AS last_activity " +
            "FROM user_chat_activity uca " +
            "JOIN users u on uca.user_id = u.id " +
            "WHERE u.chat_id = ?1) vu ON vu.order_id = um.order_id " +
            "WHERE um.order_id IS NOT NULL " +
            "GROUP BY um.order_id) v",
            nativeQuery = true)
    Page<NewMessagesResponse> trackMessagesForModerator(String chatId, Pageable pageable);

    @Query(value =
            "SELECT v.chatId, v.orderId, CAST(SUM(v.calculated) AS UNSIGNED) AS sum FROM " +
            "(SELECT u.chat_id AS chatId, um.order_id AS orderId, " +
            "um.created > IF(uca.last_activity > " +
            "uca.announced, uca.last_activity, uca.announced) AS calculated " +
            "FROM user_messages um " +
            "LEFT JOIN user_chat_activity uca ON um.user_id = uca.user_id AND um.order_id = uca.order_id " +
            "JOIN users u ON um.user_id = u.id " +
            "WHERE um.order_id IS NOT NULL " +
            "UNION ALL " +
            "SELECT u.chat_id, um.order_id, um.created > COALESCE (IF(COALESCE (uca.last_activity, '01-01-01') > " +
            "COALESCE (uca.announced, '01-01-01'), uca.last_activity, uca.announced), '01-01-01') AS calculated " +
            "FROM users u " +
            "JOIN user_messages um ON um.user_id <> u.id " +
            "LEFT JOIN user_chat_activity uca on u.id = uca.user_id AND um.order_id = uca.order_id " +
            "WHERE u.is_moderator = true AND um.order_id IS NOT NULL) v " +
            "GROUP BY v.chatId, v.orderId " +
            "HAVING sum > 0",
            nativeQuery = true)
    List<AnnouncementsResponse> getNewAnnouncements();
}