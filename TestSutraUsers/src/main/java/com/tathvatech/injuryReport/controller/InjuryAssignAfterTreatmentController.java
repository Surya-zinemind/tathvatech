package com.tathvatech.injuryReport.controller;

import com.tathvatech.injuryReport.common.InjuryAssignAfterTreatmentBean;
import com.tathvatech.injuryReport.entity.InjuryAssignAfterTreatment;
import com.tathvatech.injuryReport.request.DeleteAssignAfterTreatmentRequest;
import com.tathvatech.injuryReport.service.InjuryAssignAfterTreatmentManager;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.injuryReport.common.InjuryAssignAfterTreatmentQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/injuryassign/aftertreatment")
@RequiredArgsConstructor
public class InjuryAssignAfterTreatmentController {
    private  final Logger logger = LoggerFactory.getLogger(InjuryAssignAfterTreatmentController.class);
    private final InjuryAssignAfterTreatmentManager injuryAssignAfterTreatmentManager;
    @PostMapping("/createAssignAfterTreatment")
    public  void createAssignAfterTreatment(@RequestBody InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            injuryAssignAfterTreatmentManager.create(context, assignAfterTreatmentBean);

    }
    @PostMapping("/saveAssignAfterTreatment")
    public  InjuryAssignAfterTreatment saveAssignAfterTreatment(@RequestBody InjuryAssignAfterTreatment assignAfterTreatment) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InjuryAssignAfterTreatment aat = injuryAssignAfterTreatmentManager.saveAssignAfterTreatment(context, assignAfterTreatment);
            return aat;

    }
    @PutMapping("/updateInjuryReport")
    public  InjuryAssignAfterTreatment  UpdateInjuryReport(@RequestBody InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InjuryAssignAfterTreatment aat=null;
        aat=injuryAssignAfterTreatmentManager.update(context, assignAfterTreatmentBean);
        return aat;
    }
    @GetMapping("/getAssignAfterTreatmentList")
    public  List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentList() throws Exception
    {
        List<InjuryAssignAfterTreatmentQuery> l = injuryAssignAfterTreatmentManager.getAssignAfterTreatmentList();
        return l;
    }
    @GetMapping("/getAssignAfterTreatmentByInjuryPk/{AssignAfterTreatmentInjuryPk}")
    public   List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentByInjuryPk(@PathVariable("AssignAfterTreatmentInjuryPk") int AssignAfterTreatmentInjuryPk) throws Exception
    {
        return injuryAssignAfterTreatmentManager.getAssignAfterTreatmentByInjuryPk(AssignAfterTreatmentInjuryPk);
    }
    @GetMapping("/getAssignAfterTreatmentListByInjuryPk/{injuryPk}")
    public   List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentListByInjuryPk(@PathVariable("injuryPk") int injuryPk) throws Exception
    {
        return injuryAssignAfterTreatmentManager.getAssignAfterTreatmentListByInjuryPk(injuryPk);
    }
    @DeleteMapping("/deleteAssignAfterTreatment")
    public  void deleteAssignAfterTreatment(@RequestBody DeleteAssignAfterTreatmentRequest deleteAssignAfterTreatmentRequest)throws Exception
    {

            injuryAssignAfterTreatmentManager.deleteAssignAfterTreatment(deleteAssignAfterTreatmentRequest.getInjuryPk(),deleteAssignAfterTreatmentRequest.getAfterTreatmentMasterPk());

    }

}

