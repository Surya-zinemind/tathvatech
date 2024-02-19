package com.tathvatech.user.request;

import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class AdminChangePasswordRequest {
    private User user;
    private String password;
    private boolean notifyUser;

}
