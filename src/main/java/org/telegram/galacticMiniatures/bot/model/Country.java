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
public class Country extends AbstractEntity {

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