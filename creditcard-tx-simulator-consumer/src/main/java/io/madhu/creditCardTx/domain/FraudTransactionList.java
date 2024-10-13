/**
 * Author: Madhu
 * User:madhu
 * Date:11/10/24
 * Time:8:29 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FraudTransactionList {

    List<FraudTransaction> fraudTransactionList = new ArrayList<>();

    @Override
    public String toString() {
        return "FraudTransactionsData{" +
                "fraudTransactionList=" + fraudTransactionList +
                '}';
    }

    public void addFraudTransaction(FraudTransaction fraudTransaction) {
        this.fraudTransactionList.add(fraudTransaction);
    }

    public int size(){
       return this.fraudTransactionList.size();
    }
}
