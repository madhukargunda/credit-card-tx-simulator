/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:4:36 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.service.event;

import com.eventstore.dbclient.ResolvedEvent;
import io.madhu.creditCardTx.event.CreditCardTransactionAggregate;
import io.madhu.creditCardTx.event.CreditCardTransactionCompletedEvent;
import io.madhu.creditCardTx.event.CreditCardTransactionFailedEvent;
import io.madhu.creditCardTx.event.CreditCardTransactionInitiatedEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AggregateService {

    @Value("${spring.eventStore.streamName}")
    private String STREAM_NAME;

    @Autowired
    private EventStoreDBService eventStoreDBService;

    @Autowired
    private EventSerdeService eventSerdeService;

    @SneakyThrows
    public CreditCardTransactionAggregate aggregator(String transactionId) {
        log.info("Aggregating all the events into one ");
        CreditCardTransactionAggregate aggregate = new CreditCardTransactionAggregate();
        List<ResolvedEvent> resolvedEvents = eventStoreDBService
                .readEvents(String.format(STREAM_NAME, transactionId));
        CreditCardTransactionAggregate creditCardTransactionAggregate = new CreditCardTransactionAggregate();
        resolvedEvents.stream().forEach(resolvedEvent -> {
            String eventType = resolvedEvent.getEvent().getEventType();
            String eventData = new String(resolvedEvent.getEvent().getEventData());
            if (eventType.equals(CreditCardTransactionInitiatedEvent.class.getName())) {
                CreditCardTransactionInitiatedEvent creditCardTransactionInitiatedEvent = eventSerdeService.deserialize(eventData, CreditCardTransactionInitiatedEvent.class);
                creditCardTransactionAggregate.apply(creditCardTransactionInitiatedEvent);
            } else if (eventType.equals(CreditCardTransactionCompletedEvent.class.getName())) {
                CreditCardTransactionCompletedEvent creditCardTransactionCompletedEvent = eventSerdeService.deserialize(eventData, CreditCardTransactionCompletedEvent.class);
                creditCardTransactionAggregate.apply(creditCardTransactionCompletedEvent);
            } else if (eventType.equals(CreditCardTransactionFailedEvent.class.getName())) {
                CreditCardTransactionFailedEvent creditCardTransactionFailedEvent = eventSerdeService.deserialize(eventData, CreditCardTransactionFailedEvent.class);
                creditCardTransactionAggregate.apply(creditCardTransactionFailedEvent);
            }
        });
        return aggregate;
    }
}
