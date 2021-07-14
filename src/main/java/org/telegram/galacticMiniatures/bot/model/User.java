package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.galacticMiniatures.bot.util.Utils;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractEntity {

  @Column(name = "chat_id")
  String chatId;

  @Column(name = "name")
  String name;

  public User(Message message) {
    this.chatId = String.valueOf(message.getChatId());
    this.name = Utils.getUserName(message.getFrom());
  }
}