/**
 * Author: Madhu
 * User:madhu
 * Date:25/9/24
 * Time:10:59 AM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.dispatcher;

import io.madhu.creditCardTx.mapper.TransactionEventMapper;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import io.madhu.creditCardTx.service.event.EventStoreDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaEventDispatcher {

    @Value("${spring.kafka.topic}")
    private String TOPIC_NAME;

    @Autowired
    KafkaTemplate<String, CreditCardTransaction> kafkaTemplate;

    @Autowired
    EventStoreDBService eventStoreDBService;

    @Autowired
    TransactionEventMapper transactionEventMapper;

    public void dispatch(String key, CreditCardTransaction creditCardTransaction) {
        CompletableFuture<SendResult<String, CreditCardTransaction>> sendResultCompletableFuture =
                kafkaTemplate.send(TOPIC_NAME, key, creditCardTransaction);
        sendResultCompletableFuture
                .whenComplete(((sendResult, throwable) -> {
            if (Objects.nonNull(throwable)) {
                //eventStoreDBService.appendEvent(CCTxEventTypes.CREDIT_CARD_TX_INITIATED.name(),
                // creditCardTransactionEventMapper.from(creditCardTransaction));
            }
        }));
    }
}
