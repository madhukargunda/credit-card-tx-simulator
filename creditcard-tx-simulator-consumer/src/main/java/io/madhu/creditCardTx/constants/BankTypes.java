package io.madhu.creditCardTx.constants;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BankTypes {

    BANK_OF_AMERICA(1001, "Bank of America"),
    CHASE_BANK(1002, "Chase Bank"),
    CITI_BANK(1003, "Citibank"),
    WELLS_FARGO(1004, "Wells Fargo"),
    GOLDMAN_SACHS(1005, "Goldman Sachs"),
    MORGAN_STANLEY(1006, "Morgan Stanley"),
    HSBC(1007, "HSBC"),
    BARCLAYS(1008, "Barclays"),
    UBS(1009, "UBS"),
    DEUTSCHE_BANK(1010, "Deutsche Bank"),
    BNP_PARIBAS(1011, "BNP Paribas"),
    CREDIT_SUISSE(1012, "Credit Suisse"),
    SANTANDER(1013, "Santander"),
    ING_BANK(1014, "ING Bank"),
    SOCIETE_GENERALE(1015, "Société Générale"),
    ROYAL_BANK_OF_SCOTLAND(1016, "Royal Bank of Scotland"),
    TD_BANK(1017, "TD Bank"),
    RBC(1018, "Royal Bank of Canada"),
    SCOTIA_BANK(1019, "Scotiabank"),
    NATIONAL_AUSTRALIA_BANK(1020, "National Australia Bank"),
    WESTPAC(1021, "Westpac"),
    ANZ(1022, "ANZ Bank"),
    COMMONWEALTH_BANK(1023, "Commonwealth Bank"),
    STANDARD_CHARTERED(1024, "Standard Chartered"),
    MITSUBISHI_UFJ_FINANCIAL_GROUP(1025, "Mitsubishi UFJ Financial Group"),
    SUMITOMO_MITSUI_BANKING_CORPORATION(1026, "Sumitomo Mitsui Banking Corporation"),
    MIZUHO_BANK(1027, "Mizuho Bank"),
    STATE_BANK_OF_INDIA(1028, "State Bank of India"),
    ICICI_BANK(1029, "ICICI Bank"),
    HDFC_BANK(1030, "HDFC Bank");

    private final Integer id;
    private final String name;
}
