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
public class ListingWithImage {

  @Id
  @SequenceGenerator(name = "images_seq", sequenceName = "images_id_seq", allocationSize = 3)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_seq")
  Long id;

  @ManyToOne(cascade = CascadeType.MERGE)
  Listing listing;

  @Column(name = "image_url")
  String imageUrl;

  @Column(name = "updated")
  LocalDateTime updated;

  @Column(name = "active")
  Boolean active;
}