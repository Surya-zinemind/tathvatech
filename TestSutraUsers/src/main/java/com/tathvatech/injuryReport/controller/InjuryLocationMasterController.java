package com.tathvatech.injuryReport.controller;

import com.tathvatech.injuryReport.common.InjuryLocationMasterBean;
import com.tathvatech.injuryReport.common.InjuryLocationMasterQuery;
import com.tathvatech.injuryReport.entity.InjuryLocationMaster;
import com.tathvatech.injuryReport.service.InjuryLocationMasterManager;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/injury/locationmaster")
@RequiredArgsConstructor
public class InjuryLocationMasterController {
    private  final Logger logger = LoggerFactory.getLogger(InjuryLocationMasterController.class);
    private final InjuryLocationMasterManager injuryLocationMasterManager;

    @PostMapping("/createInjuryLocationMaster")
    public  InjuryLocationMaster createInjuryLocationMaster(@RequestBody InjuryLocationMasterBean injuryLocationMasterBean) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return injuryLocationMasterManager.create(context, injuryLocationMasterBean);

    }
   @PutMapping("/updateInjuryReport")
   public  InjuryLocationMaster  UpdateInjuryReport(@RequestBody  InjuryLocationMasterBean injuryLocationMasterBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InjuryLocationMaster aat=null;

            aat=injuryLocationMasterManager.update(context, injuryLocationMasterBean);


        return aat;
    }
    @GetMapping("/getAllList/{search}")
    public  List<InjuryLocationMasterQuery> getAllList(@PathVariable("search") String search) throws Exception
    {
        List<InjuryLocationMasterQuery> l = injuryLocationMasterManager.getAllList(search);
        return l;
    }
    @GetMapping("/getInjuryLocationMasterList")
    public  List<InjuryLocationMasterQuery> getInjuryLocationMasterList() throws Exception
    {
        List<InjuryLocationMasterQuery> l = injuryLocationMasterManager.getInjuryLocationManagerList();
        return l;
    }
    @GetMapping("/getInjuryLocationMasterChildList/{parentId}")
    public  List<InjuryLocationMasterQuery> getInjuryLocationMasterChildList(@PathVariable("parentId") int parentId) throws Exception
    {
        List<InjuryLocationMasterQuery> l = injuryLocationMasterManager.getInjuryLocationMasterChildList(parentId);
        return l;
    }
    @GetMapping("/getInjuryLocationMasterByInjuryPk/{pk}")
    public   InjuryLocationMasterQuery getInjuryLocationMasterByInjuryPk(@PathVariable("pk") int pk) throws Exception
    {
        return injuryLocationMasterManager.getInjuryLocationManagerByPk(pk);
    }
    @DeleteMapping("/deleteInjuryLocationMaster/{pk}")
    public void deleteInjuryLocationMaster(@PathVariable("pk") int pk)throws Exception
    {

            injuryLocationMasterManager.deleteInjuryLocationMaster(pk);

    }
}

