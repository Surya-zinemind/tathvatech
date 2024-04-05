package com.tathvatech.forms.controller;

import com.tathvatech.common.entity.EntityReference;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ObjectScheduleRequestBean;
import com.tathvatech.forms.service.FormService;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.CommonServicesDelegate;
import lombok.RequiredArgsConstructor;
import com.tathvatech.unit.common.UnitFormQuery;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class FormController {

   private final FormService formService;
   private final CommonServicesDelegate commonServicesDelegate;
   private final AccountService accountService;
    public  void saveTestProcSchedule(UserContext context, TestProcOID testProcOID,
                                      ObjectScheduleRequestBean objectScheduleRequestBean) throws Exception
    {

        formService.saveTestProcSchedule(context, testProcOID, objectScheduleRequestBean);
    }

    public  void saveTestProcSchedules(UserContext context, ProjectOID projectOID, UnitOID rootUnit,
                                       List<ObjectScheduleRequestBean> scheduleList) throws Exception
    {

        formService.saveTestProcSchedules(context, projectOID, rootUnit, scheduleList);

    }

    public  void moveTestProcsToUnit(UserContext userContext, List<TestProcOID> testProcsToMove,
                                           UnitOID unitOIDToMoveTo, ProjectOID projectOIDToMoveTo) throws Exception
    {

        formService.moveTestProcsToUnit(userContext, testProcsToMove, unitOIDToMoveTo, projectOIDToMoveTo);
    }

    public  void renameTestForms(UserContext userContext, List<TestProcOID> selectedTestProcs,
                                 List<OID> referencesToAdd, String name, String renameOption) throws Exception
    {

        formService.renameTestForms(userContext, selectedTestProcs, referencesToAdd, name, renameOption);

    }

    public  List<EntityReferenceBean> getReferencesForTestProc(UserContext context, TestProcOID oid)
    {
        List<EntityReferenceBean> returnList = new ArrayList<EntityReferenceBean>();
        List<EntityReference> refList = commonServicesDelegate.getEntityReferences(oid);
        for (Iterator iterator = refList.iterator(); iterator.hasNext();)
        {
            EntityReference aRef = (EntityReference) iterator.next();

            EntityReferenceBean bean = new EntityReferenceBean();
            aRef.setCreatedDate(aRef.getCreatedDate());

            bean.setReferenceFromOID(oid);

            if (aRef.getReferenceToType() == EntityTypeEnum.NCR.getValue())
            {
                NcrItemQuery ncr = NcrDelegate.getNcrItemQuery(context, new NcrItemOID(aRef.getReferenceToPk()));
                if (ncr != null) // load user and add to list only if the
                // referenced object is there.
                {
                    bean.setReferenceToOID(
                            new NcrItemOID(aRef.getReferenceToPk(), ncr.getNcrGroupNo() + "." + ncr.getNcrNo()));
                    bean.setReferencedObject(ncr);

                    User user = accountService.getUser(aRef.getCreatedBy());
                    bean.setCreatedBy(user);

                    returnList.add(bean);
                }
            }
        }
        return returnList;
    }

    public  void revertFormUpgradeOnTestProc(TestProcOID testprocOID, FormOID currentFormOID,
                                                   FormOID revertToFormOID) throws Exception
    {
        new FormUpgradeRevertProcessor().process(testprocOID, currentFormOID, revertToFormOID);
    }
    public  List<UnitFormQuery> getTestProcsByForm(FormQuery formQuery) throws Exception
    {
        return formService.getTestProcsByForm(formQuery);
    }

}
