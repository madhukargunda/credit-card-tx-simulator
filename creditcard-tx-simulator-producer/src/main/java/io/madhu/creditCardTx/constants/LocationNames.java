package io.madhu.creditCardTx.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LocationNames {

    NYC("New York, NY"),
    LA("Los Angeles, CA"),
    CHI("Chicago, IL"),
    HOU("Houston, TX"),
    PHX("Phoenix, AZ"),
    PHL("Philadelphia, PA"),
    SAT("San Antonio, TX"),
    SD("San Diego, CA"),
    DAL("Dallas, TX"),
    SJ("San Jose, CA"),
    ATX("Austin, TX"),
    JAX("Jacksonville, FL"),
    FTW("Fort Worth, TX"),
    COL("Columbus, OH"),
    CLT("Charlotte, NC"),
    IND("Indianapolis, IN"),
    SF("San Francisco, CA"),
    SEA("Seattle, WA"),
    DEN("Denver, CO"),
    DC("Washington, DC"),
    BOS("Boston, MA"),
    MIA("Miami, FL"),
    LV("Las Vegas, NV"),
    ATL("Atlanta, GA"),
    ORL("Orlando, FL");

    private final String locationName;
}
