package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends AbstractEntity {

    @ManyToOne
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    OrderStatus status;

    @Column(name = "created")
    LocalDateTime created;
}