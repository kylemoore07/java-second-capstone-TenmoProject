package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.SecureUser;
import com.techelevator.util.BasicLogger;
import com.techelevator.util.WebUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public BigDecimal getBalance(Long userId, AuthenticatedUser user) {

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "user/" + userId + "/balance", HttpMethod.GET, makeGetEntity(user), BigDecimal.class);
            return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException ex) {
            String message = WebUtils.getResponseErrorMessage(ex.getMessage());
            BasicLogger.log(message);
            System.out.println(message);
            return null;
        }
    }

    public BigDecimal getUserBalance(Long userId){
        Account account = new Account(userId);
        BigDecimal balance = null;


        return account.getBalance();
    }

    public Long getAccountId(Long userId){
        Account account = new Account(userId);
        return account.getAccountId();
    }



    public SecureUser[] getUserList(){
        try{

            return restTemplate.getForObject(baseUrl + "user/userlist", SecureUser [].class);

        }catch (RestClientResponseException | ResourceAccessException ex) {
            String message = WebUtils.getResponseErrorMessage(ex.getMessage());
            BasicLogger.log(message);
            System.out.println(message);
            return null;
        }
    }

    private HttpEntity<Void> makeGetEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());

        return new HttpEntity<>(headers);


    }
}