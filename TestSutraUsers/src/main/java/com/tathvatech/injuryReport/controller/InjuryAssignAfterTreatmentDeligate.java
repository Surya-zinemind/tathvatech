package com.tathvatech.injuryReport.controller;

import java.sql.Connection;
import java.util.List;

import com.tathvatech.testsutra.injury.common.InjuryAssignAfterTreatmentBean;
import com.tathvatech.testsutra.injury.common.InjuryAssignAfterTreatmentQuery;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;

public class InjuryAssignAfterTreatmentDeligate {
    public static void createAssignAfterTreatment(UserContext context,
                                                  InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            InjuryAssignAfterTreatmentManager.create(context, assignAfterTreatmentBean);
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public static InjuryAssignAfterTreatment saveAssignAfterTreatment(UserContext context, InjuryAssignAfterTreatment assignAfterTreatment) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);


            InjuryAssignAfterTreatment aat = InjuryAssignAfterTreatmentManager.saveAssignAfterTreatment(context, assignAfterTreatment);

            con.commit();

            return aat;
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
    }
    public static InjuryAssignAfterTreatment  UpdateInjuryReport(UserContext context,
                                                                 InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        Connection con = null;
        InjuryAssignAfterTreatment aat=null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            aat=InjuryAssignAfterTreatmentManager.update(context, assignAfterTreatmentBean);

        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
        return aat;
    }
    public static List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentList() throws Exception
    {
        List<InjuryAssignAfterTreatmentQuery> l = InjuryAssignAfterTreatmentManager.getAssignAfterTreatmentList();
        return l;
    }
    public static  List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentByInjuryPk(int AssignAfterTreatmentInjuryPk) throws Exception
    {
        return InjuryAssignAfterTreatmentManager.getAssignAfterTreatmentByInjuryPk(AssignAfterTreatmentInjuryPk);
    }
    public static  List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentListByInjuryPk(int injuryPk) throws Exception
    {
        return InjuryAssignAfterTreatmentManager.getAssignAfterTreatmentListByInjuryPk(injuryPk);
    }
    public static void deleteAssignAfterTreatment(int injuryPk,int AfterTreatmentMasterPk)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            InjuryAssignAfterTreatmentManager.deleteAssignAfterTreatment(injuryPk,AfterTreatmentMasterPk);
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

}

