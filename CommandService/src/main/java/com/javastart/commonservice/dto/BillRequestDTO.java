package com.javastart.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BillRequestDTO {

    private BigDecimal amount;

    private Boolean isOverdraftEnabled;

    private Long accountId;
}
