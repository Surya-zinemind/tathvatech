package com.tathvatech.unit.request;

import com.tathvatech.project.entity.ProjectForm;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class AddFormToUnitRequest {
    private ProjectForm projectForm;
    private int unitPk;
    private ProjectOID projectOID;

    private WorkstationOID workstationOID;
    private int formPk;
    private String testName;
    private boolean makeWorkstationInProgress;
    private boolean reviewPending;
}
