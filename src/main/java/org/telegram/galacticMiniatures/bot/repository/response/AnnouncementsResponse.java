package org.telegram.galacticMiniatures.bot.repository.response;


import java.math.BigInteger;

public interface AnnouncementsResponse {
    String getChatId();
    Integer getOrderId();
    BigInteger getSum();
}
