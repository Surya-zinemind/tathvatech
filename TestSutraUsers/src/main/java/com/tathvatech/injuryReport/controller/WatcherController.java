package com.tathvatech.injuryReport.controller;

import com.tathvatech.common.enums.EntityType;
import com.tathvatech.injuryReport.common.WatcherBean;
import com.tathvatech.injuryReport.common.WatcherQuery;
import com.tathvatech.injuryReport.entity.ItemWatcher;
import com.tathvatech.injuryReport.service.WatcherManager;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WatcherController {
    private  final Logger logger = LoggerFactory.getLogger(WatcherController.class);
    private final WatcherManager watcherManager;
    public  void create(UserContext context,
                              WatcherBean watcherBean) throws Exception
    {

            watcherManager.create(context, watcherBean);


    }
    public  WatcherBean createNewWatcher(UserContext context,
                                                WatcherBean watcherBean) throws Exception
    {

            WatcherBean wl = watcherManager.createWatcher(context, watcherBean);
            return wl;

    }
    public ItemWatcher saveWatcher(UserContext context, ItemWatcher watcherList) throws Exception
    {



            ItemWatcher wl = watcherManager.saveWatcher(context, watcherList);


            return wl;


    }
    public  ItemWatcher  UpdateWatcher(UserContext context,
                                             WatcherBean watcherBean) throws Exception
    {

        ItemWatcher wl=null;

            wl=watcherManager.update(context, watcherBean);


        return wl;
    }
    public  List<WatcherQuery> getWatcherList() throws Exception
    {
        List<WatcherQuery> l = watcherManager.getWatcherList();
        return l;
    }
    public  List<WatcherQuery> getWatcherByObjectTypeAndObjectPk(int objectPk, EntityType objectType) throws Exception
    {
        return watcherManager.getWatcherByObjectTypeAndObjectPk( objectPk, objectType);
    }
    public  void deleteWatcherList(int objectPk,EntityType objectType)throws Exception
    {

            watcherManager.deleteWatcherList(objectPk, objectType);

    }
    public void deleteWatcher(int pk)throws Exception
    {

            watcherManager.deleteWatcher(pk);

    }
}

