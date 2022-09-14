package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.SecureUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import com.techelevator.util.WebUtils;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;

public class TransferService {

    private final String baseUrl;
    private AuthenticationService authenticationService;
    private final RestTemplate restTemplate = new RestTemplate();
    private AccountService accountService;
    private AuthenticatedUser currentUser;


    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

//    public AccountService(String baseUrl) {
//        this.baseUrl = baseUrl;
//    }


    //TODO
    public void sendBucks(Transfer transfer, Long userId, AuthenticatedUser user) {

        restTemplate.exchange(baseUrl + "/user/" + userId + "/send", HttpMethod.PUT, makeTransferEntity(transfer, user), Transfer.class);

    }

    public void createTransfer(Transfer transfer, Long userId, AuthenticatedUser user ) {
        try {
            restTemplate.exchange(baseUrl + "/user/" + userId + "/transfers", HttpMethod.POST, makeTransferEntity(transfer, user), Transfer.class);

        } catch (RestClientResponseException | ResourceAccessException ex) {
            String message = WebUtils.getResponseErrorMessage(ex.getMessage());
            BasicLogger.log(message);
            System.out.println(message);
        }
    }

    public void validateAndSend(BigDecimal amount, Long accountTo, Long accountFrom, AuthenticatedUser user) {
        AccountService accountService = new AccountService(baseUrl);
        MathContext mc = new MathContext(2);
        Long userId = user.getUser().getId();
        BigDecimal currentUserBalance = accountService.getBalance(userId, user);
        BigDecimal receiverUserBalance = accountService.getUserBalance(accountTo);
        if (currentUserBalance.compareTo(amount) == 0 || currentUserBalance.compareTo(amount) == 1) {
            if(!accountTo.equals(accountFrom)) {
            currentUserBalance.subtract(amount, mc);
            receiverUserBalance.add(amount, mc);
        }
        }
    }


    public Transfer[] getTransferList(Long userId, AuthenticatedUser user) {
        try {
          ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "user/" + userId + "/transfers/list", HttpMethod.GET, makeGetEntity(user), Transfer[].class);

          return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException ex) {
            String message = WebUtils.getResponseErrorMessage(ex.getMessage());
            BasicLogger.log(message);
            System.out.println(message);
            return null;
        }
    }

    public Transfer getTransferDetailsByTransferId(Long userId, Long transferId, AuthenticatedUser user) {
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "user/" + userId + "/transfers/" + transferId, HttpMethod.GET, makeGetEntity(user), Transfer.class);
            return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException ex) {
            String message = WebUtils.getResponseErrorMessage(ex.getMessage());
            BasicLogger.log(message);
            System.out.println(message);
            return null;
        }
    }


    public HttpEntity<Transfer> makeTransferEntity(Transfer transfer, AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());

        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeGetEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());

        return new HttpEntity<>(headers);


    }
}


