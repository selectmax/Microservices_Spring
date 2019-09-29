package com.javastart.accountservice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequestDTO {

    private String name;

    private String phone;

    private String mail;

    public AccountRequestDTO(String name, String phone, String mail) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
    }
}
