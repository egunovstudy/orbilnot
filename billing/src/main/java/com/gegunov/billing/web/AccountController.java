package com.gegunov.billing.web;

import com.gegunov.billing.jpa.model.Account;
import com.gegunov.billing.service.AccountService;
import com.gegunov.billing.web.model.AccountAction;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping
    public void changeBalance(@RequestBody AccountAction action) {
        accountService.changeAccountBalance(action);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> createAccount(Authentication authentication, @RequestBody AccountAction action) {
        return ResponseEntity.ok(accountService.createNewAccount(authentication, action));
    }

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<Account> getAccountInfo(@PathVariable("accountId") UUID accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

}
