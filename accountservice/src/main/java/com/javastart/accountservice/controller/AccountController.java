package com.javastart.accountservice.controller;

import com.javastart.accountservice.DTO.AccountRequestDTO;
import com.javastart.accountservice.DTO.AccountResponseDTO;
import com.javastart.accountservice.entiry.Account;
import com.javastart.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public Long saveAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        Account account = new Account(accountRequestDTO.getName(), accountRequestDTO.getPhone(), accountRequestDTO.getMail());
        return accountService.saveAccount(
                account.getName(),
                account.getPhone(),
                account.getMail());
    }

    @GetMapping("/accounts/{id}")
    public AccountResponseDTO getAccount(@PathVariable Long id) {
        return new AccountResponseDTO(accountService.getAccountById(id));

    }

    @GetMapping("/accounts")
    public List<AccountResponseDTO> getAccounts() {
        return accountService.getAccounts()
                .stream()
                .map(AccountResponseDTO::new)
                .collect(Collectors.toList());
    }
}
