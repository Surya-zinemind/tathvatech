package com.tathvatech.user.request;

import lombok.Data;

@Data
public class UserDevicesRequest {
   private Integer managerPk;
   private Integer[] userPks;
}
