/**
 * Author: Madhu
 * User:madhu
 * Date:12/10/24
 * Time:10:52 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain.user;

import io.madhu.creditCardTx.constants.CreditStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class UserFinancialSummary {

    public UserFinancialSummary() {
        this.totalSpending = new BigDecimal("0.0");
        this.totalSpending.setScale(2, RoundingMode.HALF_UP);
        this.transactionCount = 0l;
        this.creditStatus = CreditStatus.THRESHOLD_NORMAL;
    }

    private String userId; //ok

    private String userEmail; //ok

    private BigDecimal totalSpending; //ok

    private Long transactionCount; //ok

    private CreditStatus creditStatus; //ok

    private String message; //ok

    private String messageType; //ok

    private boolean limitExceeded; //ok

    private boolean limitApproaching; //ok

    private double creditLimit; //OK

}
