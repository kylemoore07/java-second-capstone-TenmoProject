package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserDao userDao;

    public UserService (UserDao userDao){
        this.userDao = userDao;
    }

    public Long findIdByUsername(String username){

        return userDao.findIdByUsername(username);
    }
}
