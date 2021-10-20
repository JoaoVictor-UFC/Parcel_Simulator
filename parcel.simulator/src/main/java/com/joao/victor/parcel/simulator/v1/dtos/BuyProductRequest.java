package com.joao.victor.parcel.simulator.v1.dtos;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public @Data class BuyProductRequest implements Serializable {

    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotNull
    private double value;

    private PaymentTermsRequest paymentTerms;
}
