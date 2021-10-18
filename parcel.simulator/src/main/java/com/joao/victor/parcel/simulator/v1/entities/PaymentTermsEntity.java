package com.joao.victor.parcel.simulator.v1.entities;

import com.joao.victor.parcel.simulator.v1.enums.TypeBuy;
import com.joao.victor.parcel.simulator.v1.utils.CheckInterestRateSelic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "payment_terms")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public @Data class PaymentTermsEntity extends AbstractEntity<Long> implements Serializable {

    private CheckInterestRateSelic checkInterestRateSelic;

    private BigDecimal inputValue;

    private Integer numberInstallments;

    private BigDecimal interestRate;

    public int checkInCash(BigDecimal inputValue, BigDecimal valueProd){

        if (inputValue.equals(valueProd)){
            return TypeBuy.INSTALLMENTS.getCod();
        }
        return TypeBuy.IN_CASH.getCod();
    }

    public BigDecimal calculateInterestForMonth(BigDecimal inputValue, Integer numberInstallments) throws Exception {
        BigDecimal resultFinal = inputValue.add(checkInterestRateSelic.check().multiply(inputValue));
        return resultFinal;
    }
}
