package com.tathvatech.project.Request;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class UpdateFormsToProjectPartsRequest {
    private ProjectOID projectOID;
    private List<ProjectPartOID> projectPartList;
    private Collection<FormQuery> selectedFormList;
    private  String testName;
}
