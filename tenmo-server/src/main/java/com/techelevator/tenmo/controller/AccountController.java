package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exception.NotAccountOwnerException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.SecureUser;
import com.techelevator.tenmo.service.AccountService;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.file.ProviderNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountService accountService;
    private TransferService transferService;

    public AccountController(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }


//    @GetMapping("/account/{userId}/{accountId}")
//    public Long getAccountByUserId(@PathVariable Long userId, @PathVariable Long accountId, Principal user) throws NotAccountOwnerException{
//        return accountService.getAccountByUserId(userId, user.getName());
//    }




    @GetMapping("user/{userId}/balance")
    public BigDecimal getAccountBalance(@PathVariable Long userId, Principal user) throws  NotAccountOwnerException{
        return accountService.getAccountBalance(userId, user.getName());
    }

    @GetMapping("user/userlist")
    @PreAuthorize("permitAll")
    public List<SecureUser> getUserList() {
        return transferService.secureUserList();
    }



}
