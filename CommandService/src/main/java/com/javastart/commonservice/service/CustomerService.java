package com.javastart.commonservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.commonservice.dto.AccountDTO;
import com.javastart.commonservice.dto.BillRequestDTO;
import com.javastart.commonservice.dto.CustomerResponseDTO;
import com.javastart.commonservice.exception.BillServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@PropertySource("classpath:uri_properties.properties")
public class CustomerService {

    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Value("${account.service.uri}")
    public static String ACCOUNT_URL;

    @Value("${bill.service.uri}")
    public static String BILL_URL;

    @Autowired
    public CustomerService(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
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
        StringBuilder accountStringBuilder = new StringBuilder(ACCOUNT_URL);
        accountStringBuilder.append("/");
        accountStringBuilder.append(accountId);

        ResponseEntity<AccountDTO> accountEntity = restTemplate.getForEntity(accountStringBuilder.toString(),
                AccountDTO.class);
        AccountDTO accountDTO = accountEntity.getBody();
        return new CustomerResponseDTO(null, accountDTO);
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

        return getStringResponseEntity(billJson);
    }

    private ResponseEntity<String> getAccountResponse(String name, String phone, String mail) {
        AccountDTO accountDTO = new AccountDTO(name, phone, mail);
        String accountJson = null;
        try {
            accountJson = objectMapper.writeValueAsString(accountDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return getStringResponseEntity(accountJson);
    }
    private ResponseEntity<String> getStringResponseEntity(String billJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(billJson, headers);

        ResponseEntity<String> responseEntity = null;
        if (billJson != null) {
            responseEntity =
                    restTemplate.postForEntity(BILL_URL, entity, String.class);
        }
        return responseEntity;
    }
}
