package com.javastart.billservice.service;

import com.javastart.billservice.entity.Bill;
import com.javastart.billservice.exceptions.BillNotFoundException;
import org.springframework.stereotype.Service;
import com.javastart.billservice.repository.BillRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill getBillById(Long billId){
        return billRepository.findById(billId).orElseThrow(() ->
                new BillNotFoundException("Can't find bill with id: " + billId));
    }

    public Long saveBill(BigDecimal amount, Boolean isOverdraftEnabled, Long accountId){
        Bill bill = new Bill(amount, isOverdraftEnabled, accountId);
        return billRepository.save(bill).getId();
    }

    public Bill getBillByAccountId(Long accountId){
        return billRepository.findBillByAccountId(accountId).orElseThrow(()
                -> new BillNotFoundException("Can't find bill with accountId: " + accountId));
    }

    public List<Bill> getBills(){
        return billRepository.findAll();
    }
}
