package org.telegram.galacticMiniatures.bot.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;

import java.util.List;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ModeratorOrderInfo {
    Integer orderId;
    List<OrderStatus> orderStatusList = List.of(OrderStatus.NEW);
    Pageable itemPageable = PageRequest.of(0,20, Sort.by("created").descending());
}