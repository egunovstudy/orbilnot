package com.gegunov.billing.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "accounts", schema = "billing")
public class Account {

    @Id
    private UUID id;

    private BigDecimal balance;

}
