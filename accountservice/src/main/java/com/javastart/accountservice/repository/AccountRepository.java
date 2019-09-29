package com.javastart.accountservice.repository;

import com.javastart.accountservice.entiry.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {

    List<Account> findAll();
}
