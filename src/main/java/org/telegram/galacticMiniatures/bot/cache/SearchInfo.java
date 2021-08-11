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
public class SearchInfo {
    Integer sectionId;
    Pageable itemPageable = PageRequest.of(0,1);
    Pageable imagePageable = PageRequest.of(0,1);
    Pageable optionPageable = PageRequest.of(0, 1, Sort.by("price").and(Sort.by("firstOptionValue")));

    public SearchInfo(Integer sectionId) {
        this.sectionId = sectionId;
    }
}