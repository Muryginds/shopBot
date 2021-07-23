package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo extends AbstractEntity {

    @ManyToOne
    User user;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "town")
    String town;

    @Column(name = "address")
    String address;

    @Column(name = "post_index")
    String postIndex;

    @Column(name = "contacts")
    String contacts;

    public UserInfo(User user) {
        this.user = user;
    }
}