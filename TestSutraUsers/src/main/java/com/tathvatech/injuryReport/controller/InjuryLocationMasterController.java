package com.tathvatech.injuryReport.controller;

import com.tathvatech.injuryReport.common.InjuryLocationMasterBean;
import com.tathvatech.injuryReport.common.InjuryLocationMasterQuery;
import com.tathvatech.injuryReport.entity.InjuryLocationMaster;
import com.tathvatech.injuryReport.service.InjuryLocationMasterManager;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InjuryLocationMasterController {
    private  final Logger logger = LoggerFactory.getLogger(InjuryLocationMasterController.class);
    private final InjuryLocationMasterManager injuryLocationMasterManager;

    public  InjuryLocationMaster createInjuryLocationMaster(UserContext context,
                                                                  InjuryLocationMasterBean injuryLocationMasterBean) throws Exception
    {


            return injuryLocationMasterManager.create(context, injuryLocationMasterBean);

    }
    public  InjuryLocationMaster  UpdateInjuryReport(UserContext context,
                                                           InjuryLocationMasterBean injuryLocationMasterBean) throws Exception
    {

        InjuryLocationMaster aat=null;

            aat=injuryLocationMasterManager.update(context, injuryLocationMasterBean);


        return aat;
    }
    public  List<InjuryLocationMasterQuery> getAllList(String search) throws Exception
    {
        List<InjuryLocationMasterQuery> l = injuryLocationMasterManager.getAllList(search);
        return l;
    }
    public  List<InjuryLocationMasterQuery> getInjuryLocationMasterList() throws Exception
    {
        List<InjuryLocationMasterQuery> l = injuryLocationMasterManager.getInjuryLocationManagerList();
        return l;
    }
    public  List<InjuryLocationMasterQuery> getInjuryLocationMasterChildList(int parentId) throws Exception
    {
        List<InjuryLocationMasterQuery> l = injuryLocationMasterManager.getInjuryLocationMasterChildList(parentId);
        return l;
    }
    public   InjuryLocationMasterQuery getInjuryLocationMasterByInjuryPk(int pk) throws Exception
    {
        return injuryLocationMasterManager.getInjuryLocationManagerByPk(pk);
    }
    public void deleteInjuryLocationMaster(int pk)throws Exception
    {

            injuryLocationMasterManager.deleteInjuryLocationMaster(pk);

    }
}

