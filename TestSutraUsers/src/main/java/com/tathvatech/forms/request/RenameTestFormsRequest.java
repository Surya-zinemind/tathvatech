package com.tathvatech.forms.request;

import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.TestProcOID;
import lombok.Data;

import java.util.List;

@Data
public class RenameTestFormsRequest {
    private List<TestProcOID> selectedTestProcs;
    private List<OID> referencesToAdd;
    private String name;
    private String renameOption;
}
