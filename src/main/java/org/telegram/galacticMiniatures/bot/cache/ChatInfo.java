package org.telegram.galacticMiniatures.bot.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.galacticMiniatures.bot.enums.BotState;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChatInfo {
    SearchInfo searchInfo = new SearchInfo();
    FavoriteInfo favoriteInfo = new FavoriteInfo();
    CartInfo cartInfo = new CartInfo();
    OrderInfo orderInfo = new OrderInfo();
    OrderedListingsInfo orderedListingsInfo = new OrderedListingsInfo();
    UserChatMessageInfo userChatMessageInfo = new UserChatMessageInfo();
    UserOrderMessageInfo userOrderMessageInfo = new UserOrderMessageInfo();
}