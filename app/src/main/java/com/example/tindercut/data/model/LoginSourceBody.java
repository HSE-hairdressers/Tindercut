package com.example.tindercut.data.model;

/**
 * Supporting class for creating login request body
 */
public class LoginSourceBody {

    private String username;
    private String password;

    public LoginSourceBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
