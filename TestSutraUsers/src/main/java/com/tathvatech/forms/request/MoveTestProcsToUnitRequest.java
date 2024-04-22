package com.tathvatech.forms.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import lombok.Data;

import java.util.List;

@Data
public class MoveTestProcsToUnitRequest {
    private List<TestProcOID> testProcsToMove;
     private UnitOID unitOIDToMoveTo;
     private ProjectOID projectOIDToMoveTo;
}
