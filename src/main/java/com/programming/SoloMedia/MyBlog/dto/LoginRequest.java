package com.programming.SoloMedia.MyBlog.dto;


// this class is passed as a parameter when we created a method that authenticates a blogger's login.
// we'll define some fields here that are used as login authentication.
public class LoginRequest {
    private String username;
    private String password;
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
