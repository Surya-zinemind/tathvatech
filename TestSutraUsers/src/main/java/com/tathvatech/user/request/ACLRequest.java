package com.tathvatech.user.request;

import lombok.Data;

@Data
public class ACLRequest {

   private int pk;
   private int objectTypeProject;
   private String roleManager;
}
