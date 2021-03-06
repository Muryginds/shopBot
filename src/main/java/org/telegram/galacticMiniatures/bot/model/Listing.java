package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Listing {

    @Id
    @SequenceGenerator(name = "listing_seq", sequenceName = "listings_id_seq", allocationSize = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listing_seq")
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "listing_identifier")
    Integer identifier;

    @Column(name = "price")
    Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    Section section;

    @Column(name = "updated")
    LocalDateTime updated;

    @Column(name = "sku_number")
    String skuNumber;

    @Column(name = "active")
    Boolean active;

    @Override
    public String toString() {
        return "Listing{" +
                "identifier=" + identifier +
                "skuNumber=" + skuNumber +
                ", price=" + price +
                '}';
    }
}