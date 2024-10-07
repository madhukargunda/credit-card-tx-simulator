/**
 * Author: Madhu
 * User:madhu
 * Date:4/10/24
 * Time:2:09 PM
 * Project: creditcard-tx-simulator-producer
 */

package io.madhu.creditCardTx.simulator;

import io.madhu.creditCardTx.dispatcher.KafkaEventDispatcher;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import io.madhu.creditCardTx.datagenerator.TransactionGenerator;
import io.madhu.creditCardTx.model.user.User;
import io.madhu.creditCardTx.service.user.RandomUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class FraudMultipleTransactionSimulator implements Runnable {

    private final TransactionGenerator transactionGenerator;

    private final KafkaEventDispatcher kafkaEventDispatcher;

    private final RandomUserService randomUserService;

    @Override
    public void run() {
        while (true) {
            try {
                User randomUser = randomUserService.getRandomUser();
                List<CreditCardTransaction> creditCardTransactionList
                        = transactionGenerator.generateMultipleTransactionsInShortPeriod(randomUser);
                TimeUnit.SECONDS.sleep(6);
                creditCardTransactionList
                        .forEach(creditCardTransaction -> {
                            log.info("x[Fraud Tx] -> {} , Tx -> {}", Thread.currentThread().getName(), creditCardTransaction.getTransactionId());
                            kafkaEventDispatcher.dispatch(creditCardTransaction.getTransactionId(), creditCardTransaction);
                 });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
