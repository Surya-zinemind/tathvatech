package com.tathvatech.forms.request;

import com.tathvatech.forms.common.ObjectScheduleRequestBean;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import lombok.Data;

import java.util.List;

@Data
public class SaveTestProcSchedulesRequest {
    private ProjectOID projectOID;
    private UnitOID rootUnit;
    private List<ObjectScheduleRequestBean> scheduleList;
}
