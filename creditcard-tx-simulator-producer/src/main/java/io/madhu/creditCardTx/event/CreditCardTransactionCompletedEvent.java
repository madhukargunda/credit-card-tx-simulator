/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:3:39 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.event;

import io.madhu.creditCardTx.constants.BankTypes;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public final class CreditCardTransactionCompletedEvent extends CreditCardTransactionBaseEvent {
    private BankTypes bankTypes;
    private String merchantName;
    private LocalDateTime localDateTime;
}
