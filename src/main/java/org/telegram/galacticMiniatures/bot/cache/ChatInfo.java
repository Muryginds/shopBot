package org.telegram.galacticMiniatures.bot.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChatInfo {
    SearchInfo searchInfo = new SearchInfo();
    FavoriteInfo favoriteInfo = new FavoriteInfo();
    CartInfo cartInfo = new CartInfo();
    UserOrderInfo userOrderInfo = new UserOrderInfo();
    ModeratorOrderInfo moderatorOrderInfo = new ModeratorOrderInfo();
    OrderedListingsInfo orderedListingsInfo = new OrderedListingsInfo();
    UserChatMessageInfo userChatMessageInfo = new UserChatMessageInfo();
    OrderMessageInfo orderMessageInfo = new OrderMessageInfo();
    MessagesInfo messagesInfo = new MessagesInfo();
    LocalDateTime lastModified;
}