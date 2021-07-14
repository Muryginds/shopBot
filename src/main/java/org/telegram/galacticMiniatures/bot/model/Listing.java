package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "listings")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Listing extends AbstractEntity {

    @Column(name = "title")
    String title;

    @Column(name = "listing_identifier")
    Integer identifier;

    @Column(name = "description")
    String description;

    @Column(name = "price")
    Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    Section section;

    @Column(name = "last_modified")
    LocalDateTime lastModified;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id")
    List<ListingWithImage> listingsImages;

    public Listing(String title, Integer identifier, String description,
                   Double price, Section section, LocalDateTime lastModified) {
        this.title = title;
        this.identifier = identifier;
        this.description = description;
        this.price = price;
        this.section = section;
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "identifier=" + identifier +
                ", price=" + price +
                '}';
    }
}