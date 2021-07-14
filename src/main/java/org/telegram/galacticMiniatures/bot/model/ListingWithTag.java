package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "listings_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingWithTag {

  @EmbeddedId
  Key id;

  @Column(name = "last_modified")
  LocalDateTime lastModified;

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
    Tag tag;
  }
}