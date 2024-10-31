package com.gegunov.billing.jpa.repository;

import com.gegunov.billing.jpa.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {
}
