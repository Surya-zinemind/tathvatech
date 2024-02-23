package com.tathvatech.user.request;

import com.tathvatech.user.entity.User;
import lombok.Data;

import java.util.Collection;

@Data
public class UserProjectPermissionsRequest {
    private int projectPk;
    private Collection<User>[] userLists;
    private String[] roles;
}
