package io.madhu.creditCardTx.event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class CreditCardTransactionBaseEvent {
    private String transactionId;
}
