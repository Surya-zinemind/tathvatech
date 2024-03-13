package com.tathvatech.user.request;

import com.tathvatech.user.OID.UserOID;
import lombok.Data;

@Data
public class UserInRoleRequest {

   private UserOID userOID;
   private int objectPk;
   private int objectType;
   private String[] roles;
}
