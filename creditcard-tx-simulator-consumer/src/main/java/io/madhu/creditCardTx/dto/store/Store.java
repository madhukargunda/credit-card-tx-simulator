/**
 * Author: Madhu
 * User:madhu
 * Date:26/10/24
 * Time:10:44 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.dto.store;

import lombok.Data;

import java.util.List;

@Data
public class Store {

    private String storeName;
    private String storeType;
    private int transactionCount;
    private double transactionAmount;
    private List<Stall> stalls;

    public Store(String storeName, int transactionCount, double transactionAmount, List<Stall> stalls) {
        this.storeName = storeName;
        // this.storeType = storeType;
        this.transactionCount = transactionCount;
        this.transactionAmount = transactionAmount;
        this.stalls = stalls;
    }
}
