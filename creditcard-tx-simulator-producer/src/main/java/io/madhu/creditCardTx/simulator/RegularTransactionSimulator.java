/**
 * Author: Madhu
 * User:madhu
 * Date:25/9/24
 * Time:12:20 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.simulator;

import io.madhu.creditCardTx.datagenerator.TransactionGenerator;
import io.madhu.creditCardTx.dispatcher.KafkaEventDispatcher;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import io.madhu.creditCardTx.model.user.User;
import io.madhu.creditCardTx.service.user.RandomUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class RegularTransactionSimulator implements Runnable {

    private final TransactionGenerator transactionGenerator;

    private final KafkaEventDispatcher kafkaEventDispatcher;

    private final RandomUserService randomUserService;

    private static final String PRINT_TX_LOG = "Walmart [%s] , CardHolder: [%s], VisaType: [%s] User: [%s] ,UserEmail:[%s],TransactionID: [%s], Amount: [%s]";

    @Override
    public void run() {
        while (true) {
            try {
                User user = randomUserService.getRandomUser();
                CreditCardTransaction cardTransaction = transactionGenerator.generateTransaction(user);
                log.info(String.format(PRINT_TX_LOG, Thread.currentThread().getName(), cardTransaction.getCreditCardNumber(), cardTransaction.getCreditCardType(), cardTransaction.getUserName(),
                        cardTransaction.getUserEmail(), cardTransaction.getTransactionId(), cardTransaction.getTransactionAmount()));
                TimeUnit.SECONDS.sleep(60);
                kafkaEventDispatcher.dispatch(cardTransaction.getTransactionId(), cardTransaction);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
