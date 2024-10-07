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

@Component
@Slf4j
public class TransactionRunner implements CommandLineRunner {

    @Autowired
    private TransactionGenerator transactionGenerator;

    @Autowired
    private KafkaEventDispatcher kafkaEventDispatcher;

    @Autowired
    private RandomUserService randomUser;

    @Override
    public void run(String... args) throws Exception {
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
