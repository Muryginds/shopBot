package org.telegram.galacticMiniatures.bot.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessageInfo {
    Integer orderId = 0;
    Pageable itemPageable = PageRequest.of(
            0, 15, Sort.by("created").descending());

    public OrderMessageInfo(Integer orderId) {
        this.orderId = orderId;
    }
}