package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Section {

    @Id
    @SequenceGenerator(name = "section_seq", sequenceName = "sections_id_seq", allocationSize = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "section_seq")
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "section_identifier")
    Integer identifier;

    @Column(name = "updated")
    LocalDateTime updated;

    @Column(name = "active")
    Boolean active;
}