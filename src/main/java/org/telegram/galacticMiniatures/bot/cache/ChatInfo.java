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

    BotState botState = BotState.WORKING;
    SearchInfo searchInfo;
    FavoriteInfo favoriteInfo;
    CartInfo cartInfo;
}