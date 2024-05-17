package com.tathvatech.injuryReport.controller;

import com.tathvatech.injuryReport.common.InjuryAssignAfterTreatmentBean;
import com.tathvatech.injuryReport.entity.InjuryAssignAfterTreatment;
import com.tathvatech.injuryReport.service.InjuryAssignAfterTreatmentManager;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.injuryReport.common.InjuryAssignAfterTreatmentQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class InjuryAssignAfterTreatmentController {
    private  final Logger logger = LoggerFactory.getLogger(InjuryAssignAfterTreatmentController.class);
    private final InjuryAssignAfterTreatmentManager injuryAssignAfterTreatmentManager;
    public  void createAssignAfterTreatment(UserContext context,
                                                  InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {


            injuryAssignAfterTreatmentManager.create(context, assignAfterTreatmentBean);

    }
    public  InjuryAssignAfterTreatment saveAssignAfterTreatment(UserContext context, InjuryAssignAfterTreatment assignAfterTreatment) throws Exception
    {
        InjuryAssignAfterTreatment aat = injuryAssignAfterTreatmentManager.saveAssignAfterTreatment(context, assignAfterTreatment);
            return aat;

    }
    public  InjuryAssignAfterTreatment  UpdateInjuryReport(UserContext context,
                                                                 InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {

        InjuryAssignAfterTreatment aat=null;
        aat=injuryAssignAfterTreatmentManager.update(context, assignAfterTreatmentBean);
        return aat;
    }
    public  List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentList() throws Exception
    {
        List<InjuryAssignAfterTreatmentQuery> l = injuryAssignAfterTreatmentManager.getAssignAfterTreatmentList();
        return l;
    }
    public   List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentByInjuryPk(int AssignAfterTreatmentInjuryPk) throws Exception
    {
        return injuryAssignAfterTreatmentManager.getAssignAfterTreatmentByInjuryPk(AssignAfterTreatmentInjuryPk);
    }
    public   List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentListByInjuryPk(int injuryPk) throws Exception
    {
        return injuryAssignAfterTreatmentManager.getAssignAfterTreatmentListByInjuryPk(injuryPk);
    }
    public  void deleteAssignAfterTreatment(int injuryPk,int AfterTreatmentMasterPk)throws Exception
    {

            injuryAssignAfterTreatmentManager.deleteAssignAfterTreatment(injuryPk,AfterTreatmentMasterPk);

    }

}

