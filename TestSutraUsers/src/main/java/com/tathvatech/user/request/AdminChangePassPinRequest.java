package com.tathvatech.user.request;

import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class AdminChangePassPinRequest {
    private User user;
    private String passPin;
    private boolean notifyUser;

}
