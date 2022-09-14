package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.SecureUser;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcSecureUserDao implements SecureUserDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcSecureUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SecureUser> listOfOtherUsers() {
        List<SecureUser> users = new ArrayList<>();
        String sql = "SELECT user_id, username FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            SecureUser user = mapRowToSecureUser(results);
            users.add(user);
        }
        return users;
    }

    private SecureUser mapRowToSecureUser(SqlRowSet rowSet) {
        SecureUser user = new SecureUser();
        user.setId(rowSet.getLong("user_id"));
        user.setUserName(rowSet.getString("username"));
        return user;
    }
}
