package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Country {

    @Id
    @SequenceGenerator(name = "country_seq", sequenceName = "countries_id_seq", allocationSize = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_seq")
    Long id;

    @Column(name = "country_id")
    Integer countryId;

    @Column(name = "name")
    String name;

    @Column(name = "ru_name")
    String ruName;

    @Column(name = "code")
    Integer code;

    public Country(Integer countryId) {
        this.countryId = countryId;
    }
}