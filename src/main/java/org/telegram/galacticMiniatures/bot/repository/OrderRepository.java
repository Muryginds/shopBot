package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.model.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByUser_ChatId(String chatId, Pageable pageable);

    Page<Order> findByStatusIn(List<OrderStatus> statusList, Pageable pageable);

    List<Order> findAllByUser_ChatId(String chatId, Sort sort);

    Optional<Integer> countByUser_ChatId(String chatId);
}