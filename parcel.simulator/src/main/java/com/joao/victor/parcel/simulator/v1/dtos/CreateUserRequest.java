package com.joao.victor.parcel.simulator.v1.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public @Data class CreateUserRequest implements Serializable {

    @NotNull(message = "Required field - Name")
    private String name;

    @NotNull(message = "Required field - Login")
    private String login;

    @NotNull(message = "Required field - Password")
    private String password;
}
