/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:3:09 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
public final class CreditCardTransactionInitiatedEvent extends CreditCardTransactionBaseEvent {
    private String creditCardNumber;
    private String cardHolderName;
    private BigDecimal totalAmount;
    private String merchantId;
    private String merchantName;
    private LocalDateTime localDateTime;
}
