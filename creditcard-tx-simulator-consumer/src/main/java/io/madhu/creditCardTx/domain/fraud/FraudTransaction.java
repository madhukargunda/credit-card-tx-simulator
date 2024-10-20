/**
 * Author: Madhu
 * User:madhu
 * Date:11/10/24
 * Time:11:21 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain.fraud;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class FraudTransaction {

    private String creditCardNumber;

    private String creditCardHolderName;

    private BigDecimal totalCreditAmount;

    private String transactionId;
}
