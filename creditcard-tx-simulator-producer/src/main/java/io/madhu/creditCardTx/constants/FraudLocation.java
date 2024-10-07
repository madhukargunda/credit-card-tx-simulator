package io.madhu.creditCardTx.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FraudLocation {

    NIGERIA("Lagos, Nigeria"),
    RUSSIA("Moscow, Russia"),
    VENEZUELA("Caracas, Venezuela"),
    NORTH_KOREA("Pyongyang, North Korea"),
    SYRIA("Damascus, Syria"),
    IRAQ("Baghdad, Iraq");

    private final String locationName;

    public static boolean isFraudLocation(String location) {
        for (FraudLocation fraudLocation : FraudLocation.values()) {
            if (fraudLocation.getLocationName().equalsIgnoreCase(location)) {
                return true;
            }
        }
        return false;
    }
}
