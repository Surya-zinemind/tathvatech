package com.tathvatech.user.request;

import com.tathvatech.user.entity.Account;
import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class CreateNewAccountRequest {

    private Account account;
    private User user;
}
