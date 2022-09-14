package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Long getAccountByUserId(Long userId);

    BigDecimal getAccountBalance(String username);

    Long getUserIdByUserAndAccountId(Long userId, Long accountId);
}
