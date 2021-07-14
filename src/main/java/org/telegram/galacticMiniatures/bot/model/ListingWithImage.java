package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listings_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingWithImage extends AbstractEntity {

  @ManyToOne(cascade = CascadeType.MERGE)
  Listing listing;

  @Column(name = "image_url")
  String imageUrl;

  @Column(name = "last_modified")
  LocalDateTime lastModified;
}