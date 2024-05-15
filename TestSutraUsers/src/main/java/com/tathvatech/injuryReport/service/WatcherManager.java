package com.tathvatech.injuryReport.service;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.tathvatech.testsutra.injury.common.WatcherBean;
import com.tathvatech.testsutra.injury.common.WatcherQuery;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.EntityType;

public class WatcherManager
{
    private static Logger logger = Logger.getLogger(WatcherManager.class);
    public static ItemWatcher create(UserContext context,
                                     WatcherBean watcherBean) throws Exception
    {
        ItemWatcher dbReport = new ItemWatcher();
        BeanUtils.copyProperties(dbReport, watcherBean);

        int pk = PersistWrapper.createEntity(dbReport);

        //fetch the new project back
        dbReport = PersistWrapper.readByPrimaryKey(ItemWatcher.class, pk);
        return dbReport;

    }
    public static WatcherBean createWatcher(UserContext context,
                                            WatcherBean watcherBean) throws Exception
    {
        ItemWatcher dbReport = new ItemWatcher();
        WatcherBean watcher = new WatcherBean();
        BeanUtils.copyProperties(dbReport, watcherBean);

        int pk = PersistWrapper.createEntity(dbReport);

        WatcherQuery wl=  getWatcherByPk(pk);
        watcher = WatcherBean.getInjuryBean(wl);
        return watcher;

    }

    public static ItemWatcher saveWatcher(UserContext context,
                                          ItemWatcher watcherList) throws Exception
    {

        int pk = PersistWrapper.createEntity(watcherList);

        //fetch the new project back
        watcherList = PersistWrapper.readByPrimaryKey(ItemWatcher.class, pk);
        return watcherList;

    }
    public static ItemWatcher update(UserContext context,
                                     WatcherBean watcherBean) throws Exception
    {
        ItemWatcher dbReport = PersistWrapper.readByPrimaryKey(ItemWatcher.class, watcherBean.getPk());
        BeanUtils.copyProperties(dbReport, watcherBean);
        PersistWrapper.update(dbReport);
        //fetch the new project back
        dbReport = PersistWrapper.readByPrimaryKey(ItemWatcher.class, watcherBean.getPk());
        return dbReport;
    }
    public static List<WatcherQuery> getWatcherList() throws Exception {
        String sql = WatcherQuery.sql;
        PersistWrapper p = new PersistWrapper();
        return p.readList(WatcherQuery.class,  WatcherQuery.sql );
    }
    public static List<WatcherQuery> getWatcherByObjectTypeAndObjectPk(int objectPk,EntityType objectType) throws Exception
    {
        System.out.println(WatcherQuery.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null  "+WatcherQuery.sqlOrder+objectPk+objectType.getValue());
        return PersistWrapper.readList(WatcherQuery.class, WatcherQuery.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null  "+WatcherQuery.sqlOrder, objectPk,objectType.getValue());
    }
    public static List<WatcherBean> getWatcherBeanByObjectTypeAndObjectPk(int objectPk,EntityType objectType) throws Exception
    {
        return PersistWrapper.readList(WatcherBean.class, WatcherBean.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null "+WatcherBean.sqlOrder, objectPk,objectType.getValue());
    }
    public static WatcherBean getWatcherBeanByTypeAndUserPk(int objectPk,EntityType objectType,int userPk) throws Exception
    {
        return PersistWrapper.read(WatcherBean.class, WatcherBean.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? and item_watcher.userPk=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null  "+WatcherBean.sqlOrder, objectPk,objectType.getValue(),userPk);
    }
    public static void deleteWatcherList(int objectPk,EntityType objectType) throws Exception
    {
        PersistWrapper.delete("delete from item_watcher where item_watcher.objectPk=? and item_watcher.objectType=?", objectPk,objectType.getValue());
    }
    public static void deleteWatcher(int pk) throws Exception
    {
        PersistWrapper.delete("delete from item_watcher where item_watcher.pk=?",pk);
    }
    public static WatcherQuery getWatcherByPk(int watcherPk) throws Exception
    {
        String sql = WatcherQuery.sql;
        PersistWrapper p = new PersistWrapper();
        return PersistWrapper.read(WatcherQuery.class,  WatcherQuery.sql  + WatcherQuery.condition + " and item_watcher.pk=?"+WatcherQuery.sqlOrder,watcherPk);
    }
}

