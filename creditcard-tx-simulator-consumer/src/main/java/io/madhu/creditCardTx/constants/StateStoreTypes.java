package io.madhu.creditCardTx.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StateStoreTypes {
    GET_COUNT_BY_MERCHANT,
    GET_TOTAL_BY_USER;
}
