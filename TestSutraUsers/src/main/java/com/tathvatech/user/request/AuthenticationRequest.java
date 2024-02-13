package com.tathvatech.user.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
    private String accountNo;
}
