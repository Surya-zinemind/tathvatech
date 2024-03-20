package com.tathvatech.project.Request;

import com.tathvatech.user.entity.Account;
import lombok.Data;

@Data
public class IsProjectNameExistRequest {
    private Account acc;
    private String projectName;
}
