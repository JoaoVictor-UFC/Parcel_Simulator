package com.joao.victor.parcel.simulator.v1.dtos;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public @Data class PaymentTermsRequest implements Serializable {

    @NotBlank
    private double inputValue;

    private Integer numberInstallments;

}
