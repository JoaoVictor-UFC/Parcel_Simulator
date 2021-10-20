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

    private BigDecimal inputValue;

    private Integer numberInstallments;

    private BigDecimal interestRate;
}
