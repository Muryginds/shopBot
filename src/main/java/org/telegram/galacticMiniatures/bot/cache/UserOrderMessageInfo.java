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
public class UserOrderMessageInfo {
    int pageSize = 15;
    Integer orderId = 0;
    Pageable messagePageable = PageRequest.of(
            0, pageSize, Sort.by("created").descending());

    public UserOrderMessageInfo(Integer orderId) {
        this.orderId = orderId;
    }
}