package com.joao.victor.parcel.simulator.v1.dtos;


import com.joao.victor.parcel.simulator.v1.entities.PaymentTermsEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

public @Data class BuyProductRequest implements Serializable {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private BigDecimal value;

    @NotBlank
    private PaymentTermsEntity paymentTerms;
}
