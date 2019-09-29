package com.javastart.accountservice.DTO;

import com.javastart.accountservice.entiry.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponseDTO {

    private String name;

    private String phone;

    private String mail;

    public AccountResponseDTO(Account account) {
        this.name = account.getName();
        this.phone = account.getPhone();
        this.mail = account.getMail();
    }
}
