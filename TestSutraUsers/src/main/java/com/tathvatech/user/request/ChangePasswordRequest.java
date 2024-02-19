package com.tathvatech.user.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
}
