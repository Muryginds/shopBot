package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMessage extends AbstractEntity {

    @ManyToOne
    User user;

    @ManyToOne
    Order order;

    @Column (name = "message")
    String message;

    @Column(name = "created")
    LocalDateTime created = LocalDateTime.now();
}