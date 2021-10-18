package com.joao.victor.parcel.simulator.errorExceptions;

import lombok.Data;

@Data
public class ErrorObject {

    private String message;
    private String field;
    private Object parameter;
}
