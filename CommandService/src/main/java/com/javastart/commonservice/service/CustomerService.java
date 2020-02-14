package com.javastart.commonservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.commonservice.dto.AccountDTO;
import com.javastart.commonservice.dto.BillRequestDTO;
import com.javastart.commonservice.dto.BillResposnseDTO;
import com.javastart.commonservice.dto.CustomerResponseDTO;
import com.javastart.commonservice.exception.BillServiceException;
import com.javastart.commonservice.exception.CommonServiceException;
import com.javastart.commonservice.http.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@PropertySource("classpath:uri_properties.properties")
public class CustomerService {

    private ObjectMapper objectMapper;

    @Value("${account.service.uri}")
    private String accountServiceUrl;

    @Value("${bill.service.uri}")
    private String billServiceUrl;

    @Autowired
    private final RestService restService;

    @Autowired
    public CustomerService(ObjectMapper objectMapper, RestService restService) {
        this.objectMapper = objectMapper;
        this.restService = restService;
    }

    public String saveCustomer(String name, String phone, String mail,
                               BigDecimal amount, Boolean isOverdraftEnabled) {

        ResponseEntity<String> accountResponse = getAccountResponse(name, phone, mail);
        Optional<String> accountBody = Optional.of(accountResponse).map(ResponseEntity::getBody);

        String accountResponseString = accountBody.orElseThrow(() -> new BillServiceException("Can't save bill without account id"));
        ResponseEntity<String> billResponse = getBillResponse(amount, isOverdraftEnabled,
                Long.valueOf(accountResponseString));

        StringBuilder sb = new StringBuilder(accountResponseString);
        sb.append(billResponse.getBody());

        return sb.toString();
    }

    public CustomerResponseDTO getCustomerById(Long accountId, Long billId) {
        StringBuilder accountUrlBuilder = new StringBuilder(accountServiceUrl);
        accountUrlBuilder.append("/");
        accountUrlBuilder.append(accountId);


        AccountDTO accountResponse = getAccount(accountUrlBuilder.toString());

        StringBuilder billUrlBuilder = new StringBuilder(billServiceUrl);
        billUrlBuilder.append("/");
        billUrlBuilder.append(billId);
        BillResposnseDTO billResponse = getBill(billUrlBuilder.toString());

        return new CustomerResponseDTO(billResponse, accountResponse);
    }

    private AccountDTO getAccount(String url) {
        return serialisedAccountDTO(restService.getForEntity(url));
    }

    private BillResposnseDTO getBill(String url) {
        return serialisedBillDTO(restService.getForEntity(url));
    }

    private BillResposnseDTO serialisedBillDTO(ResponseEntity<String> responseEntity) {
        String body = responseEntity.getBody();
        try {
            return objectMapper.readValue(body, BillResposnseDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonServiceException(e.getMessage());
        }
    }

    private AccountDTO serialisedAccountDTO(ResponseEntity<String> responseEntity) {
        String body = responseEntity.getBody();
        try {
            return objectMapper.readValue(body, AccountDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonServiceException(e.getMessage());
        }
    }

    private ResponseEntity<String> getBillResponse(BigDecimal amount,
                                                   Boolean isOverdraftEnabled, Long accountId) {
        BillRequestDTO billRequestDTO = new BillRequestDTO(amount, isOverdraftEnabled, accountId);

        String billJson = null;
        try {
            billJson = objectMapper.writeValueAsString(billRequestDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return restService.postForEntity(billJson, billServiceUrl);
    }

    private ResponseEntity<String> getAccountResponse(String name, String phone, String mail) {
        AccountDTO accountDTO = new AccountDTO(name, phone, mail);
        String accountJson = null;
        try {
            accountJson = objectMapper.writeValueAsString(accountDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return restService.postForEntity(accountJson, accountServiceUrl);
    }
}
