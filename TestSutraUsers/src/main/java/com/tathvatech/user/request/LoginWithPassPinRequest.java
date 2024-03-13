package com.tathvatech.user.request;

import lombok.Data;

@Data
public class LoginWithPassPinRequest {

    private String username;
    private String passPin;
    private String accountNo;
}
