/**
 * Author: Madhu
 * User:madhu
 * Date:5/10/24
 * Time:12:34 PM
 * Project: creditcard-tx-simulator-producer
 */

package io.madhu.creditCardTx.datagenerator;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CreditCardNumberGenerator {

    private static final Random random = new Random();

    public String generateCreditCardNumber(String cardType) {
        String cardNumber = "";

        switch (cardType) {
            case "VISA":
                cardNumber = "4" + generateRandomDigits(14); // 16 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "MASTERCARD":
                cardNumber = generateMasterCardNumber(); // 16 digits total
                break;
            case "DISCOVER":
                cardNumber = "6011" + generateRandomDigits(11); // 16 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "AMERICAN_EXPRESS":
                cardNumber = "34" + generateRandomDigits(12); // 15 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "DINERS_CLUB":
                cardNumber = "300" + generateRandomDigits(10); // 14 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "JCB":
                cardNumber = "353" + generateRandomDigits(12); // 16 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "SWITCH":
                cardNumber = "4903" + generateRandomDigits(11); // 16 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "SOLO":
                cardNumber = "6334" + generateRandomDigits(11); // 16 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "DANKORT":
                cardNumber = "5019" + generateRandomDigits(11); // 16 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "FORBRUGSFORENINGEN":
                cardNumber = "600" + generateRandomDigits(10); // 14 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            case "LASER":
                cardNumber = "6304" + generateRandomDigits(11); // 16 digits total
                cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
                break;
            default:
                throw new IllegalArgumentException("Unknown card type: " + cardType);
        }

        return formatCardNumber(cardNumber);
    }

    private static String generateMasterCardNumber() {
        // MasterCard numbers start with 51-55 and are 16 digits long
        String prefix = String.valueOf(51 + random.nextInt(5)); // random prefix in the range 51-55
        String cardNumber = prefix + generateRandomDigits(13); // 2 digits from prefix + 14 random digits
        cardNumber = cardNumber + calculateLuhnCheckDigit(cardNumber);
        return cardNumber;
    }

    private static String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Append random digit (0-9)
        }
        return sb.toString();
    }

    private static String calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return String.valueOf(checkDigit);
    }

    private static String formatCardNumber(String cardNumber) {
        StringBuilder formattedNumber = new StringBuilder();
        int length = cardNumber.length();
        for (int i = 0; i < length; i++) {
            formattedNumber.append(cardNumber.charAt(i));
            // Add a hyphen after every 4th digit, but not after the last group
            if ((i + 1) % 4 == 0 && i + 1 != length) {
                formattedNumber.append("-");
            }
        }

        return formattedNumber.toString();
    }

}


