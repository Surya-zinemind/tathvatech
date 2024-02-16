package com.tathvatech.user.request;

import lombok.Data;

@Data
public class UserCountByTypeRequest {

   private String userType;
    private String status;
}
