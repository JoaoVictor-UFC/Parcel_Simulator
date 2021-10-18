package com.joao.victor.parcel.simulator.v1.entities;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private BigDecimal value;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_payment_terms", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_payment_terms"))
    private PaymentTermsEntity paymentTermsEntity;
}
