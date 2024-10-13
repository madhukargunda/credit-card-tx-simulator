/**
 * Author: Madhu
 * User:madhu
 * Date:12/10/24
 * Time:12:19 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain;

import io.madhu.creditCardTx.constants.StoreTypes;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class StoreWiseTransactionSummary {
    private StoreTypes store;
    private BigDecimal totalAmount;
    private Long transactionCount;

    public StoreWiseTransactionSummary() {
        this.totalAmount = new BigDecimal(0.0);
        this.totalAmount.setScale(2, RoundingMode.UP);
        this.transactionCount = 0l;
    }

    public void addTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = this.totalAmount.add(totalAmount);
        this.transactionCount = transactionCount + 1;
    }
}
