package com.tathvatech.workstation.controller;


import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.unit.common.UnitLocationQuery;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.unit.service.UnitService;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.ServiceLocator;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Project;
import com.tathvatech.workstation.common.UnitWorkstationQuery;
import com.tathvatech.workstation.common.WorkstationQuery;
import com.tathvatech.workstation.entity.UnitWorkstation;
import com.tathvatech.workstation.entity.Workstation;
import com.tathvatech.workstation.request.*;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/workstation")
@RestController
@RequiredArgsConstructor
public class WorkstationController {

    private final UnitService unitService;

    private final WorkstationService workstationService;
    private static final Logger logger = LoggerFactory.getLogger(WorkstationController.class);

    @PostMapping("/createWorkstation")
    public void createWorkstation(@RequestBody Workstation workstation) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.createWorkstation(context, workstation);

    }

    @PutMapping("/updateWorkstation")
    public void updateWorkstation(@RequestBody Workstation workstation) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.updateWorkstation(context, workstation);
    }

    @GetMapping("/getWorkstationList")
    public List<WorkstationQuery> getWorkstationList() throws Exception {
        List<WorkstationQuery> l = workstationService.getWorkstationList();
        return l;
    }

    @GetMapping("/getWorkstationsForProject/{projectPk}")
    public List<WorkstationQuery> getWorkstationsForProject(@PathVariable("projectPk") int projectPk) {
        List<WorkstationQuery> l = workstationService.getWorkstationsForProject(projectPk);
        return l;
    }

    @GetMapping("/getWorkstations")
    public List<WorkstationQuery> getWorkstations(@RequestBody WorkstationFilter filter) {
        return workstationService.getWorkstations(filter);
    }

    @GetMapping("/getWorkstationsAssignableForProject")
    public List<WorkstationQuery> getWorkstationsAssignableForProject(@RequestBody ProjectOID projectOID) throws Exception {
        List<WorkstationQuery> l = workstationService.getWorkstationsAssignableForProject(projectOID);
        return l;
    }

    @GetMapping("/getWorkstationsForSite/{sitePk}")
    public List<WorkstationQuery> getWorkstationsForSite(@PathVariable("sitePk") int sitePk) throws Exception {
        List<WorkstationQuery> l = workstationService.getWorkstationsForSite(sitePk);
        return l;
    }

    @GetMapping("/getWorkstationsForSiteAndProject")
    public List<WorkstationQuery> getWorkstationsForSiteAndProject(@RequestBody WorkstationsForSiteAndProjectRequest workstationsForSiteAndProjectRequest) throws Exception {
        List<WorkstationQuery> l = workstationService.getWorkstationsForSiteAndProject(workstationsForSiteAndProjectRequest.getSitePk(),
                workstationsForSiteAndProjectRequest.getProjectPk());
        return l;
    }

    @GetMapping("/getWorkstation/{workstationPk}")
    public Workstation getWorkstation(@PathVariable("workstationPk") int workstationPk) {
        return workstationService.getWorkstation(new WorkstationOID(workstationPk));
    }

    @GetMapping("/getWorkstationQueryByPk")
    public WorkstationQuery getWorkstationQueryByPk(@RequestBody WorkstationOID workstationOID) {
        return workstationService.getWorkstationQueryByPk(workstationOID);
    }

    @PostMapping("/addWorkstationToProject")
    public void addWorkstationToProject( @RequestBody WorkstationToProjectRequest workstationToProjectRequest) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.addWorkstationToProject(context, workstationToProjectRequest.getProjectPk(),
                workstationToProjectRequest.getWorkstationOID());

    }

    @DeleteMapping("/removeWorkstationFromProject")
    public void removeWorkstationFromProject(@RequestBody WorkstationFromProjectRequest workstationFromProjectRequest )
            throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.removeWorkstationFromProject(context, workstationFromProjectRequest.getProjectPk(),
                workstationFromProjectRequest.getWorkstationOID());

    }

    @DeleteMapping("/removeAllWorkstationsFromProject/{projectPk}")
    public void removeAllWorkstationsFromProject(@PathVariable int projectPk) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.removeAllWorkstationsFromProject(context, projectPk);

    }

    @GetMapping("/getWorkstationsForUnit")
    public List<UnitWorkstationQuery> getWorkstationsForUnit( @RequestBody WorkstationsForUnitRequest workstationsForUnitRequest) throws Exception {
        return workstationService.getWorkstationsForUnit(new UnitOID(workstationsForUnitRequest.getUnitPk(),null) ,
                workstationsForUnitRequest.getProjectOID(),
                workstationsForUnitRequest.isIncludeChildUnits());
    }

    @GetMapping("/getWorkstationsForUnits")
    public List<WorkstationQuery> getWorkstationsForUnit(@RequestBody WorkstationsForUnitbyPkRequest workstationsForUnitbyPkRequest) throws Exception {
        return workstationService.getWorkstationsForUnit(new UnitOID(workstationsForUnitbyPkRequest.getUnitPk(),null),
               workstationsForUnitbyPkRequest.getProjectOID() );
    }

    @PostMapping("/addWorkstationToUnit")
    public void addWorkstationToUnit(@RequestBody WorkstationToUnitRequest workstationToUnitRequest) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.addWorkstationToUnit(context, workstationToUnitRequest.getProjectOID(),
                workstationToUnitRequest.getUnitOID(),
                workstationToUnitRequest.getWorkstationOID());

    }


    @GetMapping("/getUnitWorkstation")
    public UnitLocation getUnitWorkstation(@RequestBody UnitWorkstationRequest unitWorkstationRequest)
            throws Exception {
        return workstationService.getUnitWorkstation(unitWorkstationRequest.getUnitPk(),
                unitWorkstationRequest.getProjectOID(),
                unitWorkstationRequest.getWorkstationOID());
    }

    /*
     * Not used anywhere. Please check and remove if so.
     */
    @Deprecated
    @GetMapping("/getUnitWorkstationStatus")
    public List<UnitLocationQuery> getUnitWorkstationStatus(@RequestBody  UnitWorkstationStatusRequest unitWorkstationStatusRequest)
            throws Exception {
        return workstationService.getUnitWorkstationStatus(unitWorkstationStatusRequest.getUnitOID(),
                unitWorkstationStatusRequest.getProjectOID());
    }

    @GetMapping("/getUnitWorkstationStatuss")
    public UnitLocationQuery getUnitWorkstationStatus( @RequestBody UnitWorkstationStatusbyPkRequest unitWorkstationStatusbyPkRequest) {
        return workstationService.getUnitWorkstationStatus(new UnitOID(unitWorkstationStatusbyPkRequest.getUnitPk(),null)
                , unitWorkstationStatusbyPkRequest.getProjectOID(),
                unitWorkstationStatusbyPkRequest.getWorkstationOID());
    }

    @GetMapping("/getUnitWorkstationStatusHistory")
    public List<UnitLocationQuery> getUnitWorkstationStatusHistory(@RequestBody UnitWorkstationStatusHistoryRequest unitWorkstationStatusHistoryRequest) throws Exception {
        return workstationService.getUnitWorkstationStatusHistory(unitWorkstationStatusHistoryRequest.getUnitPk(),
                unitWorkstationStatusHistoryRequest.getProjectOID(),
                unitWorkstationStatusHistoryRequest.getWorkstationOID());
    }

    @PutMapping("/setUnitWorkstationStatus")
    public void setUnitWorkstationStatus(@RequestBody SetUnitWorkstationStatusRequest setUnitWorkstationStatusRequest) throws Exception {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        workstationService.setUnitWorkstationStatus(userContext,setUnitWorkstationStatusRequest.getUnitPk(),
                setUnitWorkstationStatusRequest.getProjectOID(),
                setUnitWorkstationStatusRequest.getWorkstationOID(),
                setUnitWorkstationStatusRequest.getStatus());

    }

    @PutMapping("/recordWorkstationFormAccess")
    public void recordWorkstationFormAccess(@RequestBody TestProcOID testProcOID) {
        workstationService.recordWorkstationFormAccess(testProcOID);

    }

    @PutMapping("/recordWorkstationSave")
    public void recordWorkstationSave(@RequestBody TestProcOID testProcOID) {


        workstationService.recordWorkstationSave(testProcOID);


    }

    @PutMapping("/recordWorkstationFormLock")
    public void recordWorkstationFormLock(@RequestBody TestProcOID testProcOID) {


        workstationService.recordWorkstationFormLock(testProcOID);


    }

    @PutMapping("/recordWorkstationFormUnlock")
    public void recordWorkstationFormUnlock(@RequestBody TestProcOID testProcOID) {

        workstationService.recordWorkstationFormUnlock(testProcOID);

    }

    @DeleteMapping("/deleteWorkstation")
    public void deleteWorkstation(@RequestBody WorkstationOID workstationOID) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        workstationService.deleteWorstation(context, workstationOID);

    }


    @GetMapping("/getProjectsForWorkstation")
    public List<Project> getProjectsForWorkstation(@RequestBody WorkstationOID workstationOID)
            throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return workstationService.getProjectsForWorkstation(context, workstationOID);
    }

    @GetMapping("/getWorkstationPercentComplete")
    public float getWorkstationPercentComplete(@RequestBody WorkstationPercentCompleteRequest workstationPercentCompleteRequest) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return workstationService.getWorkstationPercentComplete(context, workstationPercentCompleteRequest.getUnitPk(),
                workstationPercentCompleteRequest.getProjectOID(),
                workstationPercentCompleteRequest.getWorkstationOID(),
                        workstationPercentCompleteRequest.isIncludeChildren());
    }

    @PutMapping("/moveWorkstationOrderUp")
    public void moveWorkstationOrderUp(@RequestBody WorkstationOID workstationOID) {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.moveWorkstationOrderUp(context, workstationOID);

    }

    @PutMapping("/moveWorkstationOrderDown")
    public void moveWorkstationOrderDown(@RequestBody WorkstationOID workstationOID) {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.moveWorkstationOrderDown(context, workstationOID);

    }

    @GetMapping("/getUnitWorkstationSetting")
    public UnitWorkstation getUnitWorkstationSetting(@RequestBody UnitWorkstationSettingRequest unitWorkstationSettingRequest) {
        return workstationService.getUnitWorkstationSetting(unitWorkstationSettingRequest.getUnitPk(),unitWorkstationSettingRequest.getProjectOID(),unitWorkstationSettingRequest.getWorkstationOID());
    }

    @PutMapping("/updateUnitWorkstationSetting")
    public UnitWorkstation updateUnitWorkstationSetting(@RequestBody UnitWorkstation unitWorkstation) throws Exception {


        UnitWorkstation w = workstationService.updateUnitWorkstationSetting(unitWorkstation);
        return w;

    }

    @PostMapping("/copyWorkstationSettings")
    public void copyWorkstationSettings(@RequestBody WorkstationSettingsRequest workstationSettingsRequest)
            throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.copyWorkstationSettings(context,workstationSettingsRequest.getCopyFromProjectOID(),
                workstationSettingsRequest.getDestinationProjectOID(),
                workstationSettingsRequest.isCopySites(),
                workstationSettingsRequest.isCopyProjectFunctionTeams(),
                workstationSettingsRequest.isCopyParts(),
                workstationSettingsRequest.isCopyOpenItemTeam(),
                workstationSettingsRequest.isCopyProjectCoordinators(),
                workstationSettingsRequest.getWorkstationsToCopy() );


    }
}

