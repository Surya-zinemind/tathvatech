package com.tathvatech.injuryReport.service;


import com.tathvatech.common.enums.EntityType;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.injuryReport.common.WatcherBean;
import com.tathvatech.injuryReport.common.WatcherQuery;
import com.tathvatech.injuryReport.entity.ItemWatcher;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatcherManager
{
    private static final Logger logger = LoggerFactory.getLogger(WatcherManager.class);
    private final PersistWrapper persistWrapper;
    public  ItemWatcher create(UserContext context,
                                     WatcherBean watcherBean) throws Exception
    {
        ItemWatcher dbReport = new ItemWatcher();
        BeanUtils.copyProperties(dbReport, watcherBean);

        int pk = (int) persistWrapper.createEntity(dbReport);

        //fetch the new project back
        dbReport = (ItemWatcher) persistWrapper.readByPrimaryKey(ItemWatcher.class, pk);
        return dbReport;

    }
    public  WatcherBean createWatcher(UserContext context,
                                            WatcherBean watcherBean) throws Exception
    {
        ItemWatcher dbReport = new ItemWatcher();
        WatcherBean watcher = new WatcherBean();
        BeanUtils.copyProperties(dbReport, watcherBean);

        int pk = (int) persistWrapper.createEntity(dbReport);

        WatcherQuery wl=  getWatcherByPk(pk);
        watcher = WatcherBean.getInjuryBean(wl);
        return watcher;

    }

    public  ItemWatcher saveWatcher(UserContext context,
                                          ItemWatcher watcherList) throws Exception
    {

        int pk = (int) persistWrapper.createEntity(watcherList);

        //fetch the new project back
        watcherList = (ItemWatcher) persistWrapper.readByPrimaryKey(ItemWatcher.class, pk);
        return watcherList;

    }
    public  ItemWatcher update(UserContext context,
                                     WatcherBean watcherBean) throws Exception
    {
        ItemWatcher dbReport = (ItemWatcher) persistWrapper.readByPrimaryKey(ItemWatcher.class, watcherBean.getPk());
        BeanUtils.copyProperties(dbReport, watcherBean);
        persistWrapper.update(dbReport);
        //fetch the new project back
        dbReport = (ItemWatcher) persistWrapper.readByPrimaryKey(ItemWatcher.class, watcherBean.getPk());
        return dbReport;
    }
    public List<WatcherQuery> getWatcherList() throws Exception {
        String sql = WatcherQuery.sql;

        return persistWrapper.readList(WatcherQuery.class,  WatcherQuery.sql );
    }
    public  List<WatcherQuery> getWatcherByObjectTypeAndObjectPk(int objectPk, EntityType objectType) throws Exception
    {
        System.out.println(WatcherQuery.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null  "+WatcherQuery.sqlOrder+objectPk+objectType.getValue());
        return persistWrapper.readList(WatcherQuery.class, WatcherQuery.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null  "+WatcherQuery.sqlOrder, objectPk,objectType.getValue());
    }
    public  List<WatcherBean> getWatcherBeanByObjectTypeAndObjectPk(int objectPk,EntityType objectType) throws Exception
    {
        return persistWrapper.readList(WatcherBean.class, WatcherBean.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null "+WatcherBean.sqlOrder, objectPk,objectType.getValue());
    }
    public  WatcherBean getWatcherBeanByTypeAndUserPk(int objectPk,EntityType objectType,int userPk) throws Exception
    {
        return persistWrapper.read(WatcherBean.class, WatcherBean.sql+ " and item_watcher.objectPk=? and item_watcher.objectType=? and item_watcher.userPk=? where 1=1 and TAB_USER.status='Active' and TAB_USER.email is not null  "+WatcherBean.sqlOrder, objectPk,objectType.getValue(),userPk);
    }
    public void deleteWatcherList(int objectPk,EntityType objectType) throws Exception
    {
        persistWrapper.delete("delete from item_watcher where item_watcher.objectPk=? and item_watcher.objectType=?", objectPk,objectType.getValue());
    }
    public void deleteWatcher(int pk) throws Exception
    {
        persistWrapper.delete("delete from item_watcher where item_watcher.pk=?",pk);
    }
    public  WatcherQuery getWatcherByPk(int watcherPk) throws Exception
    {
        String sql = WatcherQuery.sql;

        return persistWrapper.read(WatcherQuery.class,  WatcherQuery.sql  + WatcherQuery.condition + " and item_watcher.pk=?"+WatcherQuery.sqlOrder,watcherPk);
    }
}

