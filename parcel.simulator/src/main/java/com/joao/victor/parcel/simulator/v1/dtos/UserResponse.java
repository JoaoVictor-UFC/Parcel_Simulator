package com.joao.victor.parcel.simulator.v1.dtos;

import java.io.Serializable;

import lombok.Data;

public @Data class UserResponse implements Serializable {

    private Long id;

    private String name;

    private String login;
}
