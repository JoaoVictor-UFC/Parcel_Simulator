package com.joao.victor.parcel.simulator.v1.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateInterestForMonth {

    public static BigDecimal calculateInterestForMonth(BigDecimal inputValue, Integer numberInstallments) throws Exception {
        BigDecimal rateForMonth = CheckInterestRateSelic.check().multiply(inputValue).setScale(2, RoundingMode.HALF_UP);
        BigDecimal resultFinal = inputValue.add(rateForMonth.multiply(new BigDecimal(numberInstallments))).setScale(2, RoundingMode.HALF_UP);
        return resultFinal;
    }
}
