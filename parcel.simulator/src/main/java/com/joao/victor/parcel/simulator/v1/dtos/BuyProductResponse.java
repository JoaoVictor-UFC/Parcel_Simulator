package com.joao.victor.parcel.simulator.v1.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

public @Data class BuyProductResponse implements Serializable {

    private Long id;

    private String name;

    private Integer parcelNumber;

    private BigDecimal value;

    private BigDecimal rateInterestMonth;

}
