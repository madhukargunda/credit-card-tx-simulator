/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:3:39 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public final class CreditCardTransactionFailedEvent extends CreditCardTransactionBaseEvent {
    private String failureReason;
    private LocalDateTime localDateTime;
}
