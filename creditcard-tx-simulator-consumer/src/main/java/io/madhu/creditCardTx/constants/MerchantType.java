/**
 * Author: Madhu
 * User:madhu
 * Date:28/9/24
 * Time:4:59 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum MerchantType {

    GROCERY_STORES(1),
    DEPARTMENT_STORES(2),
    CLOTHING_STORES(3),
    FURNITURE_STORES(4),
    ELECTRONICS_STORES(5),
    AIRLINES(6),
    CAR_RENTALS(7),
    HOTELS(8),
    TRAVEL_AGENCIES(9),
    CRUISE_LINES(10),
    RAILWAY(11),
    RESTAURANTS(12),
    FAST_FOOD_RESTAURANTS(13),
    BARS_TAVERNS_NIGHTCLUBS(14),
    MOVIE_THEATERS(15),
    SPORTS_AND_RECREATION_CAMPS(16),
    HOSPITALS(17),
    CLINICS(18),
    FRAUDULENT_MERCHANT(420);

    private final Integer id;

    public static Optional<MerchantType> findById(Integer i) {
        for (MerchantType merchantType : MerchantType.values()) {
            if (merchantType.getId().equals(i)) {
                return Optional.of(merchantType);
            }
        }
        return Optional.ofNullable(null);
    }
}
