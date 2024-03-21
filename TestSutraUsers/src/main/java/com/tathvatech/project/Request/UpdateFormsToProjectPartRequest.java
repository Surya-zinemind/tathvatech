package com.tathvatech.project.Request;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class UpdateFormsToProjectPartRequest {
    private ProjectOID projectOID;
    private List<ProjectPartOID> projectPartList;
    private WorkstationOID workstationOID;
    private Collection<FormQuery> selectedFormList;
    private String testName;
}
