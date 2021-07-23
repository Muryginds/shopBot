package org.telegram.galacticMiniatures.bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.galacticMiniatures.bot.enums.BotState;
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

  @Enumerated(EnumType.STRING)
  @Column(name = "bot_state")
  BotState botState = BotState.WORKING;

  public User(String chatId, String name) {
    this.chatId = chatId;
    this.name = name;
  }

  public User(Message message) {
    this.chatId = String.valueOf(message.getChatId());
    this.name = Utils.getUserName(message.getFrom());
  }
}