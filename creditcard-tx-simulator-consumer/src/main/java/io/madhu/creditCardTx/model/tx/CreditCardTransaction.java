/**
 * Author: Madhu
 * User:madhu
 * Date:23/9/24
 * Time:2:07 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.model.tx;

import io.madhu.creditCardTx.constants.BankTypes;
import io.madhu.creditCardTx.constants.MerchantType;
import io.madhu.creditCardTx.constants.StoreTypes;
import io.madhu.creditCardTx.constants.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@NoArgsConstructor
@Data
public class CreditCardTransaction implements Serializable {

    private String cardNumber;
    private String cardHolderName;
    private String creditCardType;
    private String cardExpiryDate;
    private BankTypes issuingBank;

    private String userEmail;
    private String userName;

    private String transactionId;
    private StoreTypes storeType;
    private MerchantType merchantType;
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
    private BigDecimal transactionAmount;

    private String transactionLocation;
    private String productName;

    private Boolean isFraudTransaction;

    @Override
    public String toString() {
        return "CreditCardTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", creditCardNumber='" +  + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", creditCardType='" + creditCardType + '\'' +
                ", cardExpiryDate='" + cardExpiryDate + '\'' +
                ", cardBankTypes=" + issuingBank +
                ", merchantType=" + merchantType +
                ", transactionAmount=" + transactionAmount +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", storeType=" + storeType +
                ", transactionType=" + transactionType +
                ", transactionTime=" + transactionTime +
                ", productName='" + productName + '\'' +
                '}';
    }
}
