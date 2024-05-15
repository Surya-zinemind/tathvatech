package com.tathvatech.injuryReport.controller;

import java.sql.Connection;
import java.util.List;

import com.tathvatech.testsutra.injury.common.InjuryLocationMasterBean;
import com.tathvatech.testsutra.injury.common.InjuryLocationMasterQuery;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;

public class InjuryLocationMasterDeligate {
    public static InjuryLocationMaster createInjuryLocationMaster(UserContext context,
                                                                  InjuryLocationMasterBean injuryLocationMasterBean) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            return InjuryLocationMasterManager.create(context, injuryLocationMasterBean);
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
    public static InjuryLocationMaster  UpdateInjuryReport(UserContext context,
                                                           InjuryLocationMasterBean injuryLocationMasterBean) throws Exception
    {
        Connection con = null;
        InjuryLocationMaster aat=null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            aat=InjuryLocationMasterManager.update(context, injuryLocationMasterBean);

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
    public static List<InjuryLocationMasterQuery> getAllList(String search) throws Exception
    {
        List<InjuryLocationMasterQuery> l = InjuryLocationMasterManager.getAllList(search);
        return l;
    }
    public static List<InjuryLocationMasterQuery> getInjuryLocationMasterList() throws Exception
    {
        List<InjuryLocationMasterQuery> l = InjuryLocationMasterManager.getInjuryLocationManagerList();
        return l;
    }
    public static List<InjuryLocationMasterQuery> getInjuryLocationMasterChildList(int parentId) throws Exception
    {
        List<InjuryLocationMasterQuery> l = InjuryLocationMasterManager.getInjuryLocationMasterChildList(parentId);
        return l;
    }
    public static  InjuryLocationMasterQuery getInjuryLocationMasterByInjuryPk(int pk) throws Exception
    {
        return InjuryLocationMasterManager.getInjuryLocationManagerByPk(pk);
    }
    public static void deleteInjuryLocationMaster(int pk)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            InjuryLocationMasterManager.deleteInjuryLocationMaster(pk);
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

