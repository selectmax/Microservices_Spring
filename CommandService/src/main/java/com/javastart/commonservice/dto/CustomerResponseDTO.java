package com.javastart.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {

    private String name;

    private String phone;

    private String mail;

    private BigDecimal amount;

    private Boolean isOverdraftEnabled;



}
