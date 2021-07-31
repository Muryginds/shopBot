package org.telegram.galacticMiniatures.bot.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class MessagesInfo {
    int pageSize = 20;
    Pageable itemPageable = PageRequest.of(0,pageSize);
}