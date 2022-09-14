package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.NotAccountOwnerException;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountService {

private AccountDao accountDao;
private UserDao userDao;
private UserService userService;

public AccountService(AccountDao accountDao, UserDao userDao, UserService userService){
    this.userService = userService;
    this.accountDao = accountDao;
    this.userDao = userDao;
    }

    public Long getAccountByUserId(Long userId, String username){
    validateAccountOwner(userId, username);
    return accountDao.getAccountByUserId(userId);
    }

    public BigDecimal getAccountBalance(Long userId, String username){
    validateAccountOwner(userId, username);
    return accountDao.getAccountBalance(username);
    }

    public void validateAccountOwner(Long userId, String username) {
    try{
        Long loggedInUserId = userDao.findIdByUsername(username);
        if(loggedInUserId != null){
            if(!userId.equals(loggedInUserId)){
                throw new NotAccountOwnerException("You must be the account owner to perform this operation.");
            }
        }
    }catch (NotAccountOwnerException ex){
        throw new NotAccountOwnerException("You must be the Account Owner to perform this operation.");
    }
    }

}
