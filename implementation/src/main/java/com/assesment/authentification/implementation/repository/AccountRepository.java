package com.assesment.authentification.implementation.repository;

import com.assesment.authentification.implementation.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
