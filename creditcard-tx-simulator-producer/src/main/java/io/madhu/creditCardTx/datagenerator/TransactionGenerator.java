/**
 * Author: Madhu
 * User:madhu
 * Date:24/9/24
 * Time:3:29 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.datagenerator;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import io.madhu.creditCardTx.constants.*;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import io.madhu.creditCardTx.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Supplier;

@Component
@Slf4j
public class TransactionGenerator {

    @Autowired
    private CreditCardNumberGenerator creditCardNumberGenerator;

    //Hack for make sure to get the unique data
    private static Set<String> uniqueCreditCardNumbers = new HashSet<String>();
    private static Set<String> uniqueUserEmails = new HashSet<String>();
    private static Set<String> uniqueFullName = new HashSet<String>();

    public CreditCardTransaction generateTransaction() {
        Faker faker = new Faker();
        Random random = new Random();
        CreditCardTransaction creditCardTransaction = new CreditCardTransaction();

        Supplier<String> generateUniqueCreditCardNumbers = getGenerateUniqueCreditCardNumbers();
        Supplier<String> generateUniqueUserFullName = getGenerateUniqueUserFullName();
        Supplier<String> generateUniqueUserEmail = getGenerateUniqueUserEmail(generateUniqueUserFullName);

        //More than 80 means it will fall under 20 percentage category -meaning its fraud transaction
        Boolean isFraudulent = isFraudTransaction();
        //creditCardTransaction.setCreditCardNumber(faker.finance().creditCard(CreditCardType.values()[random.nextInt(CreditCardType.values().length)]));
        creditCardTransaction.setCreditCardNumber(creditCardNumberGenerator
                .generateCreditCardNumber(CreditCardType.values()[random.nextInt(CreditCardType.values().length)].name()));

        //Setting the cardholder details
        creditCardTransaction.setCardHolderName(generateUniqueUserFullName.get());
        creditCardTransaction.setCreditCardType(faker.business().creditCardType());
        creditCardTransaction.setCreditCardNumber(generateUniqueCreditCardNumbers.get());
        creditCardTransaction.setCardExpiryDate(faker.business().creditCardExpiry());
        creditCardTransaction.setCardExpiryDate(this.cardExpiryDate());
        creditCardTransaction.setTransactionId(String.format("Tx-%s", UUID.randomUUID().toString()));

        creditCardTransaction.setUserEmail(generateUniqueUserEmail.get());
        creditCardTransaction.setUserName(faker.name().username());

        creditCardTransaction.setTransactionType(TransactionType.PURCHASE);
        creditCardTransaction.setMerchantType(MerchantType.values()[random.nextInt(MerchantType.values().length)]);
        creditCardTransaction.setIssuingBank(BankTypes.values()[random.nextInt(BankTypes.values().length)]);
        creditCardTransaction.setProductName(faker.commerce().productName());
        creditCardTransaction.setStoreType(StoreTypes.getStoreTypeByStoreName(Thread.currentThread().getName()));

        creditCardTransaction.setTransactionAmount(new BigDecimal(faker.number().randomDouble(2, 10, 5000))
                .setScale(2, RoundingMode.UP));
        creditCardTransaction.setTransactionTime(LocalDateTime.now().minusMinutes(random.nextLong(120)));
        creditCardTransaction.setTransactionLocation(LocationNames.values()[random.nextInt(LocationNames.values().length)].getLocationName());
        if (isFraudulent) {
            applyOneOfFraudCondition(creditCardTransaction);
        }
        return creditCardTransaction;
    }

    public CreditCardTransaction generateTransaction(User user) {
        Faker faker = new Faker();
        Random random = new Random();
        CreditCardTransaction creditCardTransaction = new CreditCardTransaction();

        //More than 80 means it will fall under 20 percentage category -meaning its fraud transaction
        Boolean isFraudulent = isFraudTransaction();

        //1.setting the tx Id
        creditCardTransaction.setTransactionId(String.format("Tx-%s", UUID.randomUUID().toString()));

        //2.setting the card details
        creditCardTransaction.setCreditCardType(CreditCardType.values()[random.nextInt(CreditCardType.values().length)].name());
        creditCardTransaction.setCreditCardNumber(creditCardNumberGenerator
                .generateCreditCardNumber(creditCardTransaction.getCreditCardType()));
        creditCardTransaction.setCardExpiryDate(this.cardExpiryDate());
        //Setting the cardholder details
        creditCardTransaction.setCardHolderName(user.results.stream()
                .findFirst()
                .map(r -> r.getName().getFirst()+" "+r.getName().getLast())
                .orElse(""));

        creditCardTransaction.setUserEmail(user.results.stream()
                .findFirst()
                .map(result -> result.getEmail())
                .orElse(""));

        creditCardTransaction.setUserName(user.results.stream()
                .findFirst()
                .map(u -> u.getLogin().getUsername())
                .orElse(""));

        creditCardTransaction.setTransactionType(TransactionType.PURCHASE);
        creditCardTransaction.setMerchantType(MerchantType.values()[random.nextInt(MerchantType.values().length)]);
        creditCardTransaction.setIssuingBank(BankTypes.values()[random.nextInt(BankTypes.values().length)]);
        creditCardTransaction.setProductName(faker.commerce().productName());
        creditCardTransaction.setStoreType(StoreTypes.getStoreTypeByStoreName(Thread.currentThread().getName()));

        creditCardTransaction.setTransactionAmount(new BigDecimal(faker.number().randomDouble(2, 10, 5000))
                .setScale(2, RoundingMode.UP));
        creditCardTransaction.setTransactionTime(LocalDateTime.now().minusMinutes(random.nextLong(120)));
        creditCardTransaction.setTransactionLocation(LocationNames.values()[random.nextInt(LocationNames.values().length)].getLocationName());
        if (isFraudulent) {
            applyOneOfFraudCondition(creditCardTransaction);
        }
        return creditCardTransaction;
    }

    private Supplier<String> getGenerateUniqueUserEmail(Supplier<String> generateUniqueUserFullName) {
        Faker faker = new Faker();
        Supplier<String> generateUniqueUserEmail = () -> {
            String userEmail = null;
            do {
                userEmail = faker.internet().emailAddress(generateUniqueUserFullName.get());
            } while (!this.uniqueUserEmails.add(userEmail));
            return userEmail;
        };
        return generateUniqueUserEmail;
    }

    private Supplier<String> getGenerateUniqueUserFullName() {
        Faker faker = new Faker();
        Supplier<String> generateUniqueUserFullName = () -> {
            String fullName = null;
            do {
                fullName = faker.name().fullName();
            } while (!this.uniqueFullName.add(fullName));
            return fullName;
        };
        return generateUniqueUserFullName;
    }

    private Supplier<String> getGenerateUniqueCreditCardNumbers() {
        Faker faker = new Faker();
        Supplier<String> generateUniqueCreditCardNumbers = () -> {
            String creditCardNumber = null;
            Faker faker1 = new Faker();
            do {
                creditCardNumber = faker.business().creditCardNumber();
            } while (!this.uniqueCreditCardNumbers.add(creditCardNumber));
            return creditCardNumber;
        };
        return generateUniqueCreditCardNumbers;
    }

    public List<CreditCardTransaction> generateMultipleTransactionsInShortPeriod() {
        CreditCardTransaction baseTransaction = generateTransaction();
        ArrayList<CreditCardTransaction> txList = new ArrayList();
        Random random = new Random();
        Faker faker = new Faker();
        txList.add(baseTransaction);
        int nextInt = random.nextInt(5);
        for (int i = 0; i < nextInt; i++) {
            CreditCardTransaction nextTransaction = SerializationUtils.clone(baseTransaction);
            // Modify necessary fields for each transaction
            nextTransaction.setTransactionId(String.format("Tx-%s", UUID.randomUUID().toString()));
            nextTransaction.setProductName(faker.commerce().productName());
            nextTransaction.setMerchantType(MerchantType.values()[random.nextInt(MerchantType.values().length)]);
            // Increment transaction time based on the specified interval
            nextTransaction.setTransactionTime(baseTransaction.getTransactionTime().plusSeconds(random.nextInt(60)));
            // Optionally, modify the location or any other field to simulate more variations
            nextTransaction.setTransactionLocation(LocationNames.values()[random.nextInt(LocationNames.values().length)].getLocationName());
            txList.add(nextTransaction);
            baseTransaction = nextTransaction;
        }
        return txList;
    }

    public List<CreditCardTransaction> generateMultipleTransactionsInShortPeriod(User user) {
        CreditCardTransaction baseTransaction = generateTransaction(user);
        ArrayList<CreditCardTransaction> txList = new ArrayList();
        Random random = new Random();
        Faker faker = new Faker();
        txList.add(baseTransaction);
        int nextInt = random.nextInt(4);
        for (int i = 0; i < nextInt; i++) {
            CreditCardTransaction nextTransaction = SerializationUtils.clone(baseTransaction);
            // Modify necessary fields for each transaction
            nextTransaction.setTransactionId(String.format("Tx-%s", UUID.randomUUID().toString()));
            nextTransaction.setProductName(faker.commerce().productName());
            nextTransaction.setMerchantType(MerchantType.values()[random.nextInt(MerchantType.values().length)]);
            // Increment transaction time based on the specified interval
            nextTransaction.setTransactionTime(baseTransaction.getTransactionTime().plusSeconds(random.nextInt(60)));
            // Optionally, modify the location or any other field to simulate more variations
            nextTransaction.setTransactionLocation(LocationNames.values()[random.nextInt(LocationNames.values().length)].getLocationName());
            txList.add(nextTransaction);
            baseTransaction = nextTransaction;
        }
        return txList;
    }

    /**
     * If the random value is less than or equal 80 it is normal transaction
     * Otherwise it's a fraud transaction .
     *
     * @return
     */
    private Boolean isFraudTransaction() {
        Random random = new Random();
        //More than 80 means it will fall under 20 percentage category -meaning its fraud transaction
        boolean isFraudulent = random.nextInt(100) >= 80;
        return isFraudulent;
    }

    private String cardExpiryDate() {
        Random random = new Random();
        boolean future = random.nextBoolean();
        if (!future) {
            return LocalDate.now().minusYears(random.nextInt(3)).toString();
        } else
            return LocalDate.now().plusYears(random.nextInt(3)).toString();
    }

    /**
     * @return T
     * this method randomly set the fraud applyUnusualLocation
     */
    private void applyUnusualLocation(CreditCardTransaction creditCardTransaction) {
        Random random = new Random();
        creditCardTransaction.setTransactionLocation(FraudLocation.values()
                [random.nextInt(FraudLocation.values().length)]
                .getLocationName());
    }

    /**
     * Unusual transaction times can be an indicator of fraud. For instance,
     * if you are running this program at midnight, any transactions
     * during that time would obviously be flagged as fraudulent. 😊
     * <p>
     * Transactions happening late at night are often considered suspicious.
     *
     * @return
     */
    private void applyMidnightTransactionTime(CreditCardTransaction creditCardTransaction) {
        Random random = new Random();
        creditCardTransaction.setTransactionTime(LocalDateTime.of(LocalDate.now(),
                LocalTime.of(random.nextInt(3), random.nextInt(0, 59))));
    }

    private void applyLargeTransactionAmount(CreditCardTransaction creditCardTransaction) {
        Faker faker = new Faker();
        creditCardTransaction.setTransactionAmount(new BigDecimal(faker.number()
                .randomDouble(2, 3000, 5000))
                .setScale(2, RoundingMode.UP));
    }

    private void applyUnverifiedMerchantType(CreditCardTransaction creditCardTransaction) {
        creditCardTransaction.setMerchantType(MerchantType.FRAUDULENT_MERCHANT);
    }

    private void applyOneOfFraudCondition(CreditCardTransaction creditCardTransaction) {
        Random random = new Random();
        int i = random.nextInt(1, 5);
        switch (i) {
            case 1:
                applyLargeTransactionAmount(creditCardTransaction);
                break;
            case 2:
                applyMidnightTransactionTime(creditCardTransaction);
                break;
            case 3:
                applyUnusualLocation(creditCardTransaction);
                break;
            case 4:
                applyUnverifiedMerchantType(creditCardTransaction);
            default:
                break;
        }
    }
}
