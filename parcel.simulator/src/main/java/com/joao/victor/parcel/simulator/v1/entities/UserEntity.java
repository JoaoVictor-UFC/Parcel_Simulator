package com.joao.victor.parcel.simulator.v1.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "sale_user")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public @Data class UserEntity extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
