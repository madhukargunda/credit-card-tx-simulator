/**
 * Author: Madhu
 * User:madhu
 * Date:26/10/24
 * Time:10:45 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.dto.store;

import lombok.Data;

@Data
public class Stall {
    private String stallName;
    private int transactionCount;
    private double totalTransactionAmount;

    public Stall(String stallName, int transactionCount, double totalTransactionAmount) {
        this.stallName = stallName;
        this.transactionCount = transactionCount;
        this.totalTransactionAmount = totalTransactionAmount;
    }
}
