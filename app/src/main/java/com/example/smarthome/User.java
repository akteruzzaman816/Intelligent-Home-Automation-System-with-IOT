package com.example.smarthome;

public class User {
    String userName;
    String userEmail;
    String userPassword;
    String address;

    public User() {
    }

    public User(String userName, String userEmail, String userPassword, String address) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.address=address;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
