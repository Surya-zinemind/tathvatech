package com.tathvatech.forms.request;

import com.tathvatech.forms.common.ObjectScheduleRequestBean;
import com.tathvatech.user.OID.TestProcOID;
import lombok.Data;

@Data
public class SaveTestProcScheduleRequest {
    private TestProcOID testProcOID;
   private  ObjectScheduleRequestBean objectScheduleRequestBean;

}
