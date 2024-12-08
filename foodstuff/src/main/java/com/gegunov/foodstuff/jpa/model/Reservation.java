package com.gegunov.foodstuff.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reservations", schema = "foodstuff_service")
public class Reservation {

    @Id
    private UUID id;
    private String orderNumber;
    private UUID accountId;

    @OneToMany(mappedBy = "reservation")
    private List<ProductStock> productStock;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status{
        ACTIVE, ARCHIVE
    }

}
