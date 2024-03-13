package com.tathvatech.user.request;

import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class ChangeAddonUserPasswordRequest {

   private User user;
   private String password;
}
