package com.gegunov.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationStatusEvent {
    private UUID accountId;
    private String orderNumber;
    private State state;

    public enum State{
        SUCCESS, FAILED
    }
}
