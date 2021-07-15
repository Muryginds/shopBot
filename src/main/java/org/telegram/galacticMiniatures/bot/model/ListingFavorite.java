package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "listings_favorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingFavorite {

  @EmbeddedId
  Key id;

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