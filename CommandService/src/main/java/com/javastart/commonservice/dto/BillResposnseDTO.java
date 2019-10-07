package com.javastart.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BillResposnseDTO {

    private BigDecimal amount;

    private Boolean isOverdraftEnabled;

}
