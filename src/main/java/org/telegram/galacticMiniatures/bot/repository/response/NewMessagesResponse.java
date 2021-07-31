package org.telegram.galacticMiniatures.bot.repository.response;


import java.math.BigInteger;

public interface NewMessagesResponse {
    Integer getOrderId();
    BigInteger getSum();
}
