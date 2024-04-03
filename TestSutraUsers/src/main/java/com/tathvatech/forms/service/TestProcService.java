package com.tathvatech.forms.service;

import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;

import java.util.List;

public interface TestProcService {
    void activateTestProcs(UserContext userContext,
                           List<UnitFormQuery> formsToActivate) throws Exception;

    void notifyWorkstationFormActivated(UserContext userContext, UnitFormQuery unitFormQuery) throws Exception;

    TestProcObj getTestProc(int testProcPk) throws Exception;

    UnitFormQuery getTestProcQuery(int testProcPk);

    List<UnitFormQuery> getTestProcsForItem(UserContext context, int entityPk)throws Exception;

    List<UnitFormQuery> getTestProcsForItem(UserContext context, int entityPk, ProjectOID projectOID, boolean includeChildren)throws Exception;

    List<UnitFormQuery> getTestProcsForItem(UserContext context, int entityPk, ProjectOID projectOID, WorkstationOID workstationOID, boolean includeChildren)throws Exception;

    List<UnitFormQuery> getTestProcsForItemImpl(UserContext context, int entityPk, ProjectOID projectOID, WorkstationOID workstationOID, boolean includeChildren)throws Exception;

    TestProcFormAssign getCurrentTestProcFormEntity(TestProcOID testProcOID);

    List<TestProcFormAssignBean> getTestProcFormUpgradeHistory(TestProcOID testProcOID) throws Exception;

    TestProcSectionObj getTestProcSection(TestProcOID testProcOID, FormSectionOID formSectionOID);

    TestProcSectionObj getTestProcSection(TestProcSectionOID testProcSectionOID);

    TestProcFormAssign getTestProcFormEntity(TestProcOID testProcOID, FormOID formOID);

    TestProcObj getTestProc(TestProcSectionOID testProcSectionOID) throws Exception;
}
