package io.madhu.creditCardTx.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StateStoreTypes {
    STORE_TRANSACTION_STORE,
    USER_USAGE_SUMMARY_STORE,
    USER_CREDIT_STATUS_STORE,
    FRAUD_DETECTION,
}
