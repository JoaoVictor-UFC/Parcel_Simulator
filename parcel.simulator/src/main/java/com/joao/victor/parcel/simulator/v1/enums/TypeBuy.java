package com.joao.victor.parcel.simulator.v1.enums;

public enum TypeBuy {

    IN_CASH(0, "in_cash"),
    INSTALLMENTS(1, "installments") ;

    private Integer cod;

    private String description;

    TypeBuy(Integer cod, String description) {
        this.cod = cod;
        this.setDescription(description);
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
