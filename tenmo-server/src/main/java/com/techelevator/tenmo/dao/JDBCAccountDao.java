package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JDBCAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JDBCAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long getAccountByUserId(Long userId){
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, userId);

        }catch(EmptyResultDataAccessException ex){
            System.out.println("User Id Not found");
        }
        return null;

    }

    public BigDecimal getAccountBalance(String username){
        String sql = "SELECT balance FROM account a JOIN tenmo_user tu ON tu.user_id = a.user_id WHERE username = ?";
        try{
            return jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
        }catch(EmptyResultDataAccessException ex){
            System.out.println("What balance?");
        }
        return null;
    }

    public Long getUserIdByUserAndAccountId(Long userId, Long accountId){
        String sql = "SELECT a.user_id FROM account a JOIN tenmo_user tu ON tu.user_id = a.user_id WHERE tu.user_id = ? AND a.account_id = ?";
        try{
            return jdbcTemplate.queryForObject(sql, Long.class, userId, accountId);

        } catch(EmptyResultDataAccessException ex){
            System.out.println("Nahhh");
        }
        return null;
    }

}
