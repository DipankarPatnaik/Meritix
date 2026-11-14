package com.srm.dto;

/**
 * DTO for login requests (both Teacher and Student).
 * Sent from the frontend as JSON body.
 */
public class LoginRequest {

    private String loginId;
    private String password;

    public LoginRequest() {}

    public LoginRequest(String loginId, String password) {
        this.loginId  = loginId;
        this.password = password;
    }

    public String getLoginId()               { return loginId; }
    public void setLoginId(String loginId)   { this.loginId = loginId; }

    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }
}
