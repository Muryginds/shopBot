package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingCart {

    @EmbeddedId
    Key id;

    @Column(name = "quantity")
    Integer quantity;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Key implements Serializable {

        @ManyToOne(cascade = CascadeType.ALL)
        Listing listing;

        @ManyToOne(cascade = CascadeType.ALL)
        User user;
    }
}