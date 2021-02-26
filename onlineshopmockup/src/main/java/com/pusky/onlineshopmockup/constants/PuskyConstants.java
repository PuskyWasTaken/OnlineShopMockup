package com.pusky.onlineshopmockup.constants;

import com.pusky.onlineshopmockup.domain.enumeration.CurrencyKeyList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PuskyConstants {
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    public static final String CLIENT_APP_NAME = "client";

    /* Exchange rate for various currencies based on EUR (base currency)
     *  Why use a base currency? See http://www.yacoset.com/how-to-handle-currency-conversions under
     *  Handling currency conversions 1: Use a "base currency" for any amount < $100,000
     *  for my reference.
     */
    public static final String EUR_RATE = "1.00";
    public static final String USD_RATE = "1.21";
    public static final String RON_RATE = "4.87";
    public static final String CAD_RATE = "1.52";
    public static final String YEN_RATE = "128.58";

    public static final Map<CurrencyKeyList, BigDecimal> CURRENCY_KEY_DECIMAL_MAP;
    public static final int BIG_DECIMAL_SCALE = 2;
    public static final int BIG_DECIMAL_PRECISION = 21;

    static {
        CURRENCY_KEY_DECIMAL_MAP = new HashMap<>();
        CURRENCY_KEY_DECIMAL_MAP.put(CurrencyKeyList.EUR, new BigDecimal(EUR_RATE));
        CURRENCY_KEY_DECIMAL_MAP.put(CurrencyKeyList.USD, new BigDecimal(USD_RATE));
        CURRENCY_KEY_DECIMAL_MAP.put(CurrencyKeyList.RON, new BigDecimal(RON_RATE));
        CURRENCY_KEY_DECIMAL_MAP.put(CurrencyKeyList.CAD, new BigDecimal(CAD_RATE));
        CURRENCY_KEY_DECIMAL_MAP.put(CurrencyKeyList.YEN, new BigDecimal(YEN_RATE));
    }
}
