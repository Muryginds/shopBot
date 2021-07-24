package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

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

    @ManyToOne
    Country country;

    @Column(name = "town")
    String town;

    @Column(name = "address")
    String address;

    @Column(name = "post_index")
    String postIndex;

    public UserInfo(User user) {
        this.user = user;
    }
}