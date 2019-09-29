package com.javastart.billservice.DTO;

import com.javastart.billservice.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDTO {

    private BigDecimal amount;

    private Boolean isOverdraftEnabled;

    public BillResponseDTO(Bill bill) {
        this.amount = bill.getAmount();
        this.isOverdraftEnabled = bill.getIsOverdraftEnabled();
    }
}
