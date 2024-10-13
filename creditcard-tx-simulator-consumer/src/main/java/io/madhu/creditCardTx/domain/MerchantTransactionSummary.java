/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:11:45 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantTransactionSummary {
    private Integer totalTransactionCount;
    private BigDecimal totalTransactionAmount;

    public MerchantTransactionSummary() {
        this.totalTransactionCount = 0;
        this.totalTransactionAmount = new BigDecimal(0.0);
        this.totalTransactionAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void addMerchantTransactionSummary(BigDecimal totalAmount) {
        this.totalTransactionAmount.add(totalAmount);
        this.totalTransactionCount = this.totalTransactionCount + 1;
    }
}
