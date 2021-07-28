package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_chat_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChatActivity extends AbstractEntity {

    @ManyToOne
    User user;

    @ManyToOne
    Order order;

    @Column(name = "last_activity")
    LocalDateTime created = LocalDateTime.now();
}