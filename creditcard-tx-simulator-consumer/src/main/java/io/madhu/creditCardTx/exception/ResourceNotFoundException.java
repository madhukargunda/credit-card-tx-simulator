/**
 * Author: Madhu
 * User:madhu
 * Date:20/10/24
 * Time:5:20 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.exception;


public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

