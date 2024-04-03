package com.tathvatech.forms.controller;

import com.tathvatech.forms.common.ObjectScheduleRequestBean;
import com.tathvatech.forms.service.FormService;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FormController {

   private final FormService formService;
    public  void saveTestProcSchedule(UserContext context, TestProcOID testProcOID,
                                      ObjectScheduleRequestBean objectScheduleRequestBean) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            formService.saveTestProcSchedule(context, testProcOID, objectScheduleRequestBean);

            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  void saveTestProcSchedules(UserContext context, ProjectOID projectOID, UnitOID rootUnit,
                                             List<ObjectScheduleRequestBean> scheduleList) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            formService.saveTestProcSchedules(context, projectOID, rootUnit, scheduleList);

            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  void moveTestProcsToUnit(UserContext userContext, List<TestProcOID> testProcsToMove,
                                           UnitOID unitOIDToMoveTo, ProjectOID projectOIDToMoveTo) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            formService.moveTestProcsToUnit(userContext, testProcsToMove, unitOIDToMoveTo, projectOIDToMoveTo);

            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  void renameTestForms(UserContext userContext, List<TestProcOID> selectedTestProcs,
                                       List<OID> referencesToAdd, String name, String renameOption) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            formService.renameTestForms(userContext, selectedTestProcs, referencesToAdd, name, renameOption);

            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  List<EntityReferenceBean> getReferencesForTestProc(UserContext context, TestProcOID oid)
    {
        List<EntityReferenceBean> returnList = new ArrayList<EntityReferenceBean>();
        List<EntityReference> refList = CommonServicesDelegate.getEntityReferences(oid);
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

                    User user = AccountDelegate.getUser(aRef.getCreatedBy());
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
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            new FormUpgradeRevertProcessor().process(testprocOID, currentFormOID, revertToFormOID);

            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }
    public  List<UnitFormQuery> getTestProcsByForm(FormQuery formQuery) throws Exception
    {
        return formService.getTestProcsByForm(formQuery);
    }

}
