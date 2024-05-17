package com.tathvatech.injuryReport.controller;


import com.tathvatech.injuryReport.common.WatcherBean;
import com.tathvatech.injuryReport.common.WatcherQuery;
import com.tathvatech.injuryReport.entity.ItemWatcher;
import com.tathvatech.injuryReport.request.GetWatcherByObjectTypeAndObjectPkRequest;
import com.tathvatech.injuryReport.service.WatcherManager;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watcher")
@RequiredArgsConstructor
public class WatcherController {
    private  final Logger logger = LoggerFactory.getLogger(WatcherController.class);
    private final WatcherManager watcherManager;
    @PostMapping("/create")
    public  void create(@RequestBody WatcherBean watcherBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            watcherManager.create(context, watcherBean);


    }
    @PostMapping("/createNewWatcher")
    public  WatcherBean createNewWatcher(@RequestBody WatcherBean watcherBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            WatcherBean wl = watcherManager.createWatcher(context, watcherBean);
            return wl;

    }
    @PostMapping("/saveWatcher")
    public ItemWatcher saveWatcher(@RequestBody ItemWatcher watcherList) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            ItemWatcher wl = watcherManager.saveWatcher(context, watcherList);


            return wl;


    }
    @PutMapping("/updateWatcher")
    public  ItemWatcher  UpdateWatcher(@RequestBody WatcherBean watcherBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ItemWatcher wl=null;

            wl=watcherManager.update(context, watcherBean);


        return wl;
    }
    @GetMapping("/getWatcherList")
    public  List<WatcherQuery> getWatcherList() throws Exception
    {
        List<WatcherQuery> l = watcherManager.getWatcherList();
        return l;
    }
    @GetMapping("/getWatcherByObjectTypeAndObjectPk")
    public  List<WatcherQuery> getWatcherByObjectTypeAndObjectPk(@RequestBody GetWatcherByObjectTypeAndObjectPkRequest getWatcherByObjectTypeAndObjectPkRequest) throws Exception
    {
        return watcherManager.getWatcherByObjectTypeAndObjectPk( getWatcherByObjectTypeAndObjectPkRequest.getObjectPk(), getWatcherByObjectTypeAndObjectPkRequest.getObjectType());
    }
    @DeleteMapping("/deleteWatcherList")
    public  void deleteWatcherList(@RequestBody GetWatcherByObjectTypeAndObjectPkRequest getWatcherByObjectTypeAndObjectPkRequest)throws Exception
    {

            watcherManager.deleteWatcherList(getWatcherByObjectTypeAndObjectPkRequest.getObjectPk(), getWatcherByObjectTypeAndObjectPkRequest.getObjectType());

    }
    @DeleteMapping("/deleteWatcher/{pk}")
    public void deleteWatcher(@PathVariable("pk") int pk)throws Exception
    {

            watcherManager.deleteWatcher(pk);

    }
}

