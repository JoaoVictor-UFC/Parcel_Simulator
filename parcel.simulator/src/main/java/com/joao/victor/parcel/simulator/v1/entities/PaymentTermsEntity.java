package com.joao.victor.parcel.simulator.v1.entities;

import com.joao.victor.parcel.simulator.v1.enums.TypeBuy;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "payment_terms")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentTermsEntity extends AbstractEntity<Long> implements Serializable {

    private double inputValue;

    private double numberInstallments;

    public int checkInCash(double inputValue, double valueProd){

        if (inputValue < valueProd){
            return TypeBuy.INSTALLMENTS.getCod();
        }
        return TypeBuy.IN_CASH.getCod();
    }
}
