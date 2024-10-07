package io.madhu.creditCardTx.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum StoreTypes {

    SUPER_CENTER("Supercenter", "Bentonville, AR"),
    NEIGHBOR_HOOD("Neighborhood Market", "Plano, TX"),
    SUPER_STORE("Superstore", "Orlando, FL"),
    EXPRESS("Express", "Phoenix, AZ"),
    HYPER_MART("Hypermart", "Los Angeles, CA"),
    DISCOUNT("Discount Store", "Miami, FL"),
    MARKET("Market", "Charlotte, NC"),
    OUTLET("Global Outlet", "Houston, TX"),
    CITY_CENTER("City Center", "Chicago, IL"),
    RETAIL_HUB("Retail Hub", "Denver, CO");

    private final String name;
    private final String location;

    public static StoreTypes getStoreTypeByStoreName(String storeName) {
       return Arrays.stream(StoreTypes.values())
                .filter(storeTypesLocations -> storeTypesLocations.getName()
                        .equalsIgnoreCase(storeName))
                .findFirst().get();
    }

}
