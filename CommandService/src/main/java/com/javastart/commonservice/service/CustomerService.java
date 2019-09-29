package com.javastart.commonservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.commonservice.dto.AccountDTO;
import com.javastart.commonservice.dto.CustomerResponseDTO;
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

    @Autowired
    public CustomerService(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public String saveCustomer(String name, String phone,
                             String mail, BigDecimal amount, Boolean isOverdraftEnabled){
        String accountResponse = getAccountResponse(name, phone, mail);

        return accountResponse;
    }

    public CustomerResponseDTO getCustomerById(Long accountId, Long billId){
        String url = ACCOUNT_URL + "/" + accountId;
        AccountDTO accountDTO = restTemplate.getForEntity(url, AccountDTO.class).getBody();
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(accountDTO.getName(), accountDTO.getPhone(),
                accountDTO.getMail(), null, null);
        return customerResponseDTO;

    }

    private String getAccountResponse(String name, String phone, String mail) {
        ResponseEntity<String> accountResponse = getStringResponseEntity(name, phone, mail);
        Optional<String> accountBody = Optional.of(accountResponse).map(ResponseEntity::getBody);
                accountResponse.getBody();
        ResponseEntity<String> billResponse = getBillResponse(amount, isOverdraftEnabled, );

        return accountResponse == null ? "-1" : accountResponse.getBody();
    }

    private ResponseEntity<String> getStringResponseEntity(String name, String phone, String mail) {
        AccountDTO accountDTO = new AccountDTO(name, phone, mail);

        String accountJson = null;

        try {
            accountJson = objectMapper.writeValueAsString(accountDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(accountJson, headers);

        ResponseEntity<String> stringResponseEntity = null;
        if (accountJson != null) {
            stringResponseEntity =
                    restTemplate.postForEntity(ACCOUNT_URL, entity, String.class);
        }
        return stringResponseEntity;
    }
}
