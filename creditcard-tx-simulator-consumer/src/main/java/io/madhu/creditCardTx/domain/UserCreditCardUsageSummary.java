/**
 * Author: Madhu
 * User:madhu
 * Date:12/10/24
 * Time:10:52 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain;

import io.madhu.creditCardTx.constants.CreditStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class UserCreditCardUsageSummary {

    public UserCreditCardUsageSummary() {
        this.totalSpending = new BigDecimal("0.0");
        this.totalSpending.setScale(2, RoundingMode.HALF_UP);
        this.transactionCount = 0l;
        this.creditStatus = CreditStatus.THRESHOLD_NORMAL;
    }

    private String userId;

    private String userEmail;

    private BigDecimal totalSpending;

    private Long transactionCount;

    private CreditStatus creditStatus;

    private String message;

    private String messageType;

    private boolean limitExceeded;

    private boolean limitApproaching;
}
