package com.techelevator.tenmo.model;

public class SecureUser {

    private Long id;
    private String userName;

    public SecureUser() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public SecureUser(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
