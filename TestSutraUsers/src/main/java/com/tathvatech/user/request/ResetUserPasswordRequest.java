package com.tathvatech.user.request;

import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class ResetUserPasswordRequest {
    private User user;
    private String verificationKey;
    private String newPassword;
}
