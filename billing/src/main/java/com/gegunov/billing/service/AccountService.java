package com.gegunov.billing.service;

import com.gegunov.billing.jpa.model.Account;
import com.gegunov.billing.jpa.model.OrderEvent;
import com.gegunov.billing.jpa.model.OrderEventState;
import com.gegunov.billing.jpa.repository.AccountRepository;
import com.gegunov.billing.jpa.repository.OrderEventRepository;
import com.gegunov.billing.web.model.AccountAction;
import com.gegunov.billing.web.model.AccountActionType;
import com.gegunov.billing.web.validation.AccountValidator;
import com.gegunov.model.OrderEventType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final OrderEventRepository orderEventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOrderEvent(OrderEvent orderEvent) {
        log.info("Processing order event: {}", orderEvent);
        AccountAction action = buildAccountAction(orderEvent);
        try {
            changeAccountBalance(action);
            orderEvent.setOrderEventState(OrderEventState.PAYMENT_CONFIRMED);
            log.info("Order event processed for account {}", orderEvent.getAccountId());
        } catch (Exception e) {
            log.error("Error processing order event for account {}", orderEvent.getAccountId(), e);
            orderEvent.setOrderEventState(OrderEventState.PAYMENT_DENIED);
            orderEvent.setErrorMessage("Not enough balance for commiting payment for order");
        }
        orderEventRepository.save(orderEvent);

    }

    @Transactional
    public UUID createNewAccount(Authentication authentication, AccountAction accountAction) {
        Account account = new Account();
        account.setBalance(accountAction.getAmount());
        account.setId(UUID.fromString(authentication.getName()));
        accountRepository.save(account);
        return account.getId();
    }

    public Account getAccountById(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(EntityNotFoundException::new);
    }

    private AccountAction buildAccountAction(OrderEvent orderEvent) {
        AccountAction action = new AccountAction();
        action.setAmount(orderEvent.getAmount());
        action.setAccountId(orderEvent.getAccountId());
        if (orderEvent.getEventType() == OrderEventType.CREATED) {
            action.setActionType(AccountActionType.WITHDRAW_MONEY);
        } else {
            action.setActionType(AccountActionType.DEPOSIT_MONEY);
        }
        return action;
    }

    public void changeAccountBalance(AccountAction action) {
        Account account = accountRepository.findById(action.getAccountId()).orElseThrow(EntityNotFoundException::new);

        final BigDecimal amount = action.getAmount();
        switch (action.getActionType()) {
        case DEPOSIT_MONEY -> account.setBalance(account.getBalance().add(amount));
        case WITHDRAW_MONEY -> {
            AccountValidator.validateBalance(account.getBalance(), amount);
            account.setBalance(account.getBalance().subtract(amount));
        }
        }
        accountRepository.save(account);
    }

    public List<OrderEvent> getReceivedOrderEvents() {
        return orderEventRepository.findByOrderEventState(OrderEventState.RECEIVED);
    }

    public List<OrderEvent> getPaymentConfirmedOrderEvents() {
        return orderEventRepository.findByOrderEventState(OrderEventState.PAYMENT_CONFIRMED);
    }

    public List<OrderEvent> getPaymentFailedOrderEvents() {
        return orderEventRepository.findByOrderEventState(OrderEventState.PAYMENT_DENIED);
    }

    public void updateOrderEvent(OrderEvent orderEvent) {
        orderEventRepository.save(orderEvent);
    }
}
