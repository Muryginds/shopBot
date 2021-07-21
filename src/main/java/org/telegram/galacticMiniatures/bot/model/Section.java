package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Section extends AbstractEntity {

    @Column(name = "name")
    String name;

    @Column(name = "section_identifier")
    Integer identifier;

    @Column(name = "updated")
    LocalDateTime updated;

    @Column(name = "active")
    Boolean active;
}