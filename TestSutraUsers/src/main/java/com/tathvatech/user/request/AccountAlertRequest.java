package com.tathvatech.user.request;

import com.tathvatech.user.entity.Account;
import lombok.Data;

@Data
public class AccountAlertRequest {
    private Account account;
    private String text;
}
