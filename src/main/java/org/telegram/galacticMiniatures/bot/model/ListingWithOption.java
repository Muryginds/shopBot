package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listings_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingWithOption extends AbstractEntity {

  @ManyToOne(cascade = CascadeType.MERGE)
  Listing listing;

  @Column(name = "option1_name")
  String firstOptionName;

  @Column(name = "option1_value")
  String firstOptionValue;

  @Column(name = "option2_name")
  String secondOptionName;

  @Column(name = "option2_value")
  String secondOptionValue;

  @Column(name = "price")
  Integer price;

  @Column(name = "updated")
  LocalDateTime updated;

  @Column(name = "active")
  Boolean active;
}