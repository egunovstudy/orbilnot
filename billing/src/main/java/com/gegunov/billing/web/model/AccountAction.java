package com.gegunov.billing.web.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountAction {
    private BigDecimal amount;
    private AccountActionType actionType;
    private UUID accountId;
}
