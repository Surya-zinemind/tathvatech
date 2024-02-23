package com.tathvatech.user.request;

import com.tathvatech.user.entity.User;
import lombok.Data;

import java.util.Collection;

@Data
public class UserPermissionsRequest {

    private Integer entityPk;
    private Integer entityType;
    private Collection<User> userList;
    private String role;
}
