package com.joao.victor.parcel.simulator.v1.dtos;

import lombok.Data;

import java.io.Serializable;

public @Data class InterestRateResponse implements Serializable {

    private String date;

    private String value;
}
