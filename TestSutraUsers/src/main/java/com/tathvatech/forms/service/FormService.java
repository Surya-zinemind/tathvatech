package com.tathvatech.forms.service;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ObjectScheduleRequestBean;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;

import java.util.List;

public interface FormService {
    void saveTestProcSchedule(UserContext context, TestProcOID testProcOID, ObjectScheduleRequestBean objectScheduleRequestBean) throws Exception;

    void saveTestProcSchedules(UserContext context, ProjectOID projectOID, UnitOID rootUnit, List<ObjectScheduleRequestBean> scheduleList) throws Exception;

    void moveTestProcsToUnit(UserContext userContext, List<TestProcOID> testProcsToMove, UnitOID unitOIDToMoveTo, ProjectOID projectOIDToMoveTo) throws Exception;

    void renameTestForms(UserContext userContext, List<TestProcOID> selectedTestProcs, List<OID> referencesToAdd, String name, String renameOption) throws Exception;

    List<UnitFormQuery> getTestProcsByForm(FormQuery formQuery) throws Exception;

    TestProcObj upgradeFormForUnit(UserContext context, TestProcOID testProcOID, int surveyPk) throws Exception;
}
