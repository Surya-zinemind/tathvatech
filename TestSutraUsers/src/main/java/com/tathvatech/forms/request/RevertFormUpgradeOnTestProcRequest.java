package com.tathvatech.forms.request;

import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.TestProcOID;
import lombok.Data;

@Data
public class RevertFormUpgradeOnTestProcRequest {
    private TestProcOID testprocOID;
    private FormOID currentFormOID;
   private FormOID revertToFormOID;
}
