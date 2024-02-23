package com.tathvatech.user.request;

import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class VerificationCodeRequest {

   private User userToResetPasswordFor;
   private User sendEmailTo;
}
