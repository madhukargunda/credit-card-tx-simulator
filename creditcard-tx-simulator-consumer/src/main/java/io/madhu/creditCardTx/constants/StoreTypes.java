package io.madhu.creditCardTx.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum StoreTypes {

    SUPER_CENTER("Supercenter", "Bentonville, AR", "SUPER_CENTER-1"),
    NEIGHBOR_HOOD("Neighborhood Market", "Plano, TX", "NEIGHBOR_HOOD-2"),
    SUPER_STORE("Superstore", "Orlando, FL", "SUPER_STORE-3"),
    EXPRESS("Express", "Phoenix, AZ", "EXPRESS-4"),
    HYPER_MART("Hypermart", "Los Angeles, CA", "HYPER_MART-5"),
    DISCOUNT("Discount Store", "Miami, FL", "DISCOUNT-6"),
    MARKET("Market", "Charlotte, NC", "MARKET-7"),
    OUTLET("Global Outlet", "Houston, TX", "OUTLET-8"),
    CITY_CENTER("City Center", "Chicago, IL", "CITY_CENTER-9"),
    RETAIL_HUB("Retail Hub", "Denver, CO", "RETAIL_HUB-10");

    private final String name;
    private final String location;
    private final String storeId;

    public static StoreTypes getStoreTypeByStoreName(String storeName) {
        return Arrays.stream(StoreTypes.values())
                .filter(storeTypesLocations -> storeTypesLocations.getName()
                        .equalsIgnoreCase(storeName))
                .findFirst().get();
    }

    public static String getStoreLocation(String storeName) {
        StoreTypes storeType = getStoreTypeByStoreName(storeName);
        return storeType.getLocation().split(",")[1].trim();
    }
}
