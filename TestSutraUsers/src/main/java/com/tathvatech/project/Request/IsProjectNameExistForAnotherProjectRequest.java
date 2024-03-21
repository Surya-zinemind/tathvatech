package com.tathvatech.project.Request;

import com.tathvatech.user.entity.Account;
import lombok.Data;

@Data
public class IsProjectNameExistForAnotherProjectRequest {

    private String projectName;
    private int projectPk;
    private Account acc;
}
