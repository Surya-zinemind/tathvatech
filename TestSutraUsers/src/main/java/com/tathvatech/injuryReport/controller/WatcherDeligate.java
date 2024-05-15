package com.tathvatech.injuryReport.controller;

import java.sql.Connection;
import java.util.List;

import com.tathvatech.testsutra.injury.common.WatcherBean;
import com.tathvatech.testsutra.injury.common.WatcherQuery;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.EntityType;

public class WatcherDeligate {
    public static void create(UserContext context,
                              WatcherBean watcherBean) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            WatcherManager.create(context, watcherBean);

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
    public static  WatcherBean createNewWatcher(UserContext context,
                                                WatcherBean watcherBean) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            WatcherBean wl = WatcherManager.createWatcher(context, watcherBean);
            return wl;
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
    public static ItemWatcher saveWatcher(UserContext context, ItemWatcher watcherList) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);


            ItemWatcher wl = WatcherManager.saveWatcher(context, watcherList);

            con.commit();

            return wl;
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
    public static ItemWatcher  UpdateWatcher(UserContext context,
                                             WatcherBean watcherBean) throws Exception
    {
        Connection con = null;
        ItemWatcher wl=null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            wl=WatcherManager.update(context, watcherBean);

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
        return wl;
    }
    public static List<WatcherQuery> getWatcherList() throws Exception
    {
        List<WatcherQuery> l = WatcherManager.getWatcherList();
        return l;
    }
    public static  List<WatcherQuery> getWatcherByObjectTypeAndObjectPk(int objectPk,EntityType objectType) throws Exception
    {
        return WatcherManager.getWatcherByObjectTypeAndObjectPk( objectPk, objectType);
    }
    public static void deleteWatcherList(int objectPk,EntityType objectType)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            WatcherManager.deleteWatcherList(objectPk, objectType);
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
    public static void deleteWatcher(int pk)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            WatcherManager.deleteWatcher(pk);
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

