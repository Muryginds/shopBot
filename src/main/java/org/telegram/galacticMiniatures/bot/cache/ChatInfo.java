package org.telegram.galacticMiniatures.bot.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.galacticMiniatures.bot.enums.BotState;
import org.telegram.galacticMiniatures.bot.model.Listing;

import java.util.LinkedHashMap;
import java.util.Map;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChatInfo {

    BotState botState = BotState.WORKING;
    SearchInfo searchInfo;
    FavoriteInfo favoriteInfo;
    Map<Listing, Integer> cart = new LinkedHashMap<>();
}