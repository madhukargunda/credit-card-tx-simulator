/**
 * Author: Madhu
 * User:madhu
 * Date:18/10/24
 * Time:9:49 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.util;


import io.madhu.creditCardTx.constants.LogConstants;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyDescription;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class StreamLoggerUtility {

    private static final String PRINT_TX_LOG = "Walmart [%s] , CardHolder: [%s], VisaType: [%s] User: [%s] ,TransactionID: [%s], Amount: [%s]";

    /**
     * Reusable method to help debugging the
     * topology issues.
     *
     * @param topology
     * @return
     */
    public Topology logTopologyDetails(Topology topology) {
        TopologyDescription description = topology.describe();
        log.info("Topology Description: {}", description);
        // This describes the topology , its helped me in debugging the issues.
        description.subtopologies().forEach(subtopology -> {
            subtopology.nodes().forEach(node -> {
                if (Objects.nonNull(node.name())) {
                    if (node instanceof TopologyDescription.Processor) {
                        TopologyDescription.Processor processor = (TopologyDescription.Processor) node;
                        log.info("Processor {} has the following state stores: {}", processor.name(), processor.stores());
                    }
                    log.info("State stores for node {}: {}", node.name());
                }
            });
        });
        log.info("Topology Description: {}", topology.describe());
        return topology;
    }

    public  void logCreditCardTxKStreamDetails(KStream<String, CreditCardTransaction> creditCardTransactionKStream) {
        creditCardTransactionKStream.mapValues((k, v) ->
                        String.format(LogConstants.PRINT_TX_LOG,
                                v.getStoreType(),
                                v.getCreditCardNumber(),
                                v.getCreditCardType(),
                                v.getUserName(),
                                v.getTransactionAmount(),
                                v.getTransactionAmount()))
                .print(Printed.<String, String>toSysOut()
                        .withLabel("[Walmart:]"));
    }
}
