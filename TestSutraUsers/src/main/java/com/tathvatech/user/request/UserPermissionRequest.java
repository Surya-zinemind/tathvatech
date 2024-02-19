package com.tathvatech.user.request;

import lombok.Data;

import java.util.List;

@Data
public class UserPermissionRequest {
   private int userPk;
    private List permsList;
}
