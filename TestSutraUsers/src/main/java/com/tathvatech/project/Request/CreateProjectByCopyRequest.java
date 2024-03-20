package com.tathvatech.project.Request;

import com.tathvatech.project.entity.Project;
import lombok.Data;

@Data
public class CreateProjectByCopyRequest {
    private Project project;
    private  String copyFromProjectPk;
}
