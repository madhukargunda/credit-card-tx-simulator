/**
 * Author: Madhu
 * User:madhu
 * Date:25/9/24
 * Time:10:23 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.runner;

import io.madhu.creditCardTx.constants.StoreTypes;
import io.madhu.creditCardTx.datagenerator.TransactionGenerator;
import io.madhu.creditCardTx.dispatcher.KafkaEventDispatcher;
import io.madhu.creditCardTx.service.kafka.KafkaReadyService;
import io.madhu.creditCardTx.service.user.RandomUserService;
import io.madhu.creditCardTx.simulator.FraudMultipleTransactionSimulator;
import io.madhu.creditCardTx.simulator.RegularTransactionSimulator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.LongStream;

@Component
@Slf4j
public class TransactionRunner implements CommandLineRunner {

    private static final Long MAX_RETRIES = 10L;

    @Autowired
    private TransactionGenerator transactionGenerator;

    @Autowired
    private KafkaEventDispatcher kafkaEventDispatcher;

    @Autowired
    private RandomUserService randomUser;

    @Autowired
    private KafkaReadyService kafkaReadyService;

    @Override
    public void run(String... args) throws Exception {
        Long FIRST_SLEEP_TIME = 1000L;
        if (isKafkaServiceIsReady(FIRST_SLEEP_TIME)) {
            startProducingCreditCardTransacation();
        } else {
            log.info("Kafka server is not started....");
        }
    }

    private boolean isKafkaServiceIsReady(Long FIRST_SLEEP_TIME) {
        log.info("[Verifying is isKafkaServiceIsReady or not ");
        boolean connected = LongStream.range(0, MAX_RETRIES)
                .mapToObj(retryCount -> {
                    log.info("[Verifying the kafka..... ...]");
                    try {
                        long backoffTime = (long) Math.pow(2, retryCount) * FIRST_SLEEP_TIME;
                        log.info("The backoffTime  is {} ", backoffTime);
                        TimeUnit.MILLISECONDS.sleep(backoffTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return kafkaReadyService.isKafkaReady();
                })
                .filter(isConnected -> isConnected)
                .findFirst().orElse(false);
        return connected;
    }

    private void startProducingCreditCardTransacation() {
        ExecutorService executorService = Executors.newFixedThreadPool(10, new ThreadFactory() {
            private int counter = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread t1 = new Thread(r);
                StoreTypes[] shopNames = StoreTypes.values();
                t1.setName(shopNames[counter++ % shopNames.length].getName());
                return t1;
            }
        });
        List<RegularTransactionSimulator> transactionSimulators = create10WalmartShops();
        transactionSimulators.stream().forEach(shop -> executorService.submit(shop));
        ScheduledExecutorService singleThreadExecutor = Executors.newSingleThreadScheduledExecutor();
        singleThreadExecutor.scheduleAtFixedRate(new FraudMultipleTransactionSimulator(this.transactionGenerator, this.kafkaEventDispatcher, this.randomUser),
                2, 60, TimeUnit.SECONDS);
    }

    private List<RegularTransactionSimulator> create10WalmartShops() {
        List<RegularTransactionSimulator> shops = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            shops.add(new RegularTransactionSimulator(this.transactionGenerator, kafkaEventDispatcher, randomUser));
        }
        return shops;
    }


}
