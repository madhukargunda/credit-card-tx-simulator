/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:5:16 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.event;

import io.madhu.creditCardTx.constants.BankTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public final class CreditCardTransactionAggregate {
    private String transactionId;
    private BankTypes bankTypes;
    private String merchantName;
    private String failureReason;
    private LocalDateTime txCompletedTime;
    private String txStatus;
    private String creditCardNumber;
    private String cardHolderName;
    private BigDecimal totalAmount;
    private String merchantId;
    private LocalDateTime txInitiatedTime;
    private LocalDateTime txFailedTime;

    public void apply(CreditCardTransactionInitiatedEvent creditCardTransactionInitiatedEvent) {
        this.transactionId = creditCardTransactionInitiatedEvent.getTransactionId();
        this.creditCardNumber = creditCardTransactionInitiatedEvent.getCreditCardNumber();
        this.cardHolderName = creditCardTransactionInitiatedEvent.getCardHolderName();
        this.totalAmount = creditCardTransactionInitiatedEvent.getTotalAmount();
        this.merchantId = creditCardTransactionInitiatedEvent.getMerchantId();
        this.merchantName = creditCardTransactionInitiatedEvent.getMerchantName();
        this.txInitiatedTime = creditCardTransactionInitiatedEvent.getLocalDateTime();
    }

    public void apply(CreditCardTransactionCompletedEvent creditCardTransactionCompletedEvent) {
        this.bankTypes = creditCardTransactionCompletedEvent.getBankTypes();
        this.txCompletedTime = creditCardTransactionCompletedEvent.getLocalDateTime();
        this.txStatus = "COMPLETED";
    }

    public void apply(CreditCardTransactionFailedEvent creditCardTransactionFailedEvent) {
        this.failureReason = creditCardTransactionFailedEvent.getFailureReason();
        this.txFailedTime = creditCardTransactionFailedEvent.getLocalDateTime();
    }
}
