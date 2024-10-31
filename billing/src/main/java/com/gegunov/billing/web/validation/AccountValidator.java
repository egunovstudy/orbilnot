package com.gegunov.billing.web.validation;

import java.math.BigDecimal;

public class AccountValidator {

    public static void validateBalance(BigDecimal currentBalance, BigDecimal withdrawingAmount) {
        if (currentBalance.compareTo(withdrawingAmount) < 0) {
            throw new NotEnoughMoneyException();
        }
    }
}
