/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:11:39 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.mapper;


import io.madhu.creditCardTx.event.CreditCardTransactionInitiatedEvent;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventMapper {

    public CreditCardTransactionInitiatedEvent from(CreditCardTransaction creditCardTransaction){
       return CreditCardTransactionInitiatedEvent.builder()
                .creditCardNumber(creditCardTransaction.getCreditCardNumber())
                .transactionId(creditCardTransaction.getTransactionId())
                .cardHolderName(creditCardTransaction.getCardHolderName())
                .totalAmount(creditCardTransaction.getTransactionAmount())
                .build();
    }
}
