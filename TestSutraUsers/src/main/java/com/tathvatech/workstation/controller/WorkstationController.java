package com.tathvatech.workstation.controller;

import ch.qos.logback.classic.Logger;
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
import com.tathvatech.workstation.request.WorkstationFilter;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
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
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WorkstationController.class);

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

    @GetMapping("/getWorkstationsForSiteAndProject/{sitePk}/{projectPk}")
    public List<WorkstationQuery> getWorkstationsForSiteAndProject(@PathVariable("sitePk") int sitePk, @PathVariable("projectPk") int projectPk) throws Exception {
        List<WorkstationQuery> l = workstationService.getWorkstationsForSiteAndProject(sitePk, projectPk);
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

    @PostMapping("/addWorkstationToProject/{projectPk}")
    public void addWorkstationToProject(@PathVariable("projectPk") int projectPk, @RequestBody WorkstationOID workstationOID) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.addWorkstationToProject(context, projectPk, workstationOID);

    }

    @DeleteMapping("/removeWorkstationFromProject/{projectPk}")
    public void removeWorkstationFromProject(@PathVariable("projectPk") int projectPk, @RequestBody WorkstationOID workstationOID)
            throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.removeWorkstationFromProject(context, projectPk, workstationOID);

    }

    @DeleteMapping("/removeAllWorkstationsFromProject/{ projectPk}")
    public void removeAllWorkstationsFromProject(@PathVariable int projectPk) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.removeAllWorkstationsFromProject(context, projectPk);

    }

    @GetMapping("/getWorkstationsForUnit/{unitPk}/{includeChildUnits}")
    public List<UnitWorkstationQuery> getWorkstationsForUnit(@PathVariable("unitPk") int unitPk, @RequestBody ProjectOID projectOID,
                                                             @PathVariable("includeChildUnits") boolean includeChildUnits) throws Exception {
        return workstationService.getWorkstationsForUnit(new UnitOID(unitPk, null), projectOID, includeChildUnits);
    }

    @GetMapping("/getWorkstationsForUnit/{ unitPk}")
    public List<WorkstationQuery> getWorkstationsForUnit(@PathVariable(" unitPk") int unitPk, @RequestBody ProjectOID projectOID) throws Exception {
        return workstationService.getWorkstationsForUnit(new UnitOID(unitPk, null), projectOID);
    }

    @PostMapping("/addWorkstationToUnit")
    public void addWorkstationToUnit(@RequestBody ProjectOID projectOID, @RequestBody UnitOID unitOID,
                                     @RequestBody WorkstationOID workstationOID) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.addWorkstationToUnit(context, projectOID, unitOID, workstationOID);

    }


    @GetMapping("/getUnitWorkstation/{unitPk}")
    public UnitLocation getUnitWorkstation(@PathVariable("unitPk") int unitPk, @RequestBody ProjectOID projectOID, @RequestBody WorkstationOID workstationOID)
            throws Exception {
        return workstationService.getUnitWorkstation(unitPk, projectOID, workstationOID);
    }

    /*
     * Not used anywhere. Please check and remove if so.
     */
    @Deprecated
    @GetMapping("/getUnitWorkstationStatus")
    public List<UnitLocationQuery> getUnitWorkstationStatus(@RequestBody UnitOID unitOID, @RequestBody ProjectOID projectOID)
            throws Exception {
        return workstationService.getUnitWorkstationStatus(unitOID, projectOID);
    }

    @GetMapping("/getUnitWorkstationStatus/{unitPk}")
    public UnitLocationQuery getUnitWorkstationStatus(@PathVariable("unitPk") int unitPk, @RequestBody ProjectOID projectOID,
                                                      @RequestBody WorkstationOID workstationOID) {
        return workstationService.getUnitWorkstationStatus(new UnitOID(unitPk, null), projectOID, workstationOID);
    }

    @GetMapping("/getUnitWorkstationStatusHistory/{unitPk}")
    public List<UnitLocationQuery> getUnitWorkstationStatusHistory(@PathVariable("unitPk") int unitPk, @RequestBody ProjectOID projectOID,
                                                                   @RequestBody WorkstationOID workstationOID) throws Exception {
        return workstationService.getUnitWorkstationStatusHistory(unitPk, projectOID, workstationOID);
    }

    @PutMapping("/setUnitWorkstationStatus/{unitPk}/{status}")
    public void setUnitWorkstationStatus(@RequestBody ProjectOID projectOID, @PathVariable("unitPk") int unitPk,
                                         @RequestBody WorkstationOID workstationOID, @PathVariable("status") String status) throws Exception {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        workstationService.setUnitWorkstationStatus(userContext, unitPk, projectOID, workstationOID, status);

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

    @GetMapping("/getWorkstationPercentComplete/{unitPk}/{includeChildren}")
    public float getWorkstationPercentComplete(@PathVariable("unitPk") int unitPk, @RequestBody ProjectOID projectOID,
                                               @RequestBody WorkstationOID workstationOID, @PathVariable("includeChildren") boolean includeChildren) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return workstationService.getWorkstationPercentComplete(context, unitPk, projectOID, workstationOID,
                includeChildren);
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

    @GetMapping("/getUnitWorkstationSetting/{unitPk}")
    public UnitWorkstation getUnitWorkstationSetting(@PathVariable("unitPk") int unitPk, @RequestBody ProjectOID projectOID,
                                                     @RequestBody WorkstationOID workstationOID) {
        return workstationService.getUnitWorkstationSetting(unitPk, projectOID, workstationOID);
    }

    @PutMapping("/updateUnitWorkstationSetting")
    public UnitWorkstation updateUnitWorkstationSetting(@RequestBody UnitWorkstation unitWorkstation) throws Exception {


        UnitWorkstation w = workstationService.updateUnitWorkstationSetting(unitWorkstation);
        return w;

    }

    @PostMapping("copyWorkstationSettings/{copySites}/{copyProjectFunctionTeams}/{ copyParts}/{copyOpenItemTeam}/{copyProjectCoordinators}")
    public void copyWorkstationSettings(@RequestBody ProjectOID copyFromProjectOID,
                                        @RequestBody ProjectOID destinationProjectOID, @PathVariable("copySites") boolean copySites, @PathVariable("copyProjectFunctionTeams") boolean copyProjectFunctionTeams, @PathVariable(" copyParts") boolean copyParts,
                                        @PathVariable("copyOpenItemTeam") boolean copyOpenItemTeam, @PathVariable("copyProjectCoordinators") boolean copyProjectCoordinators, @RequestBody List<Object[]> workstationsToCopy)
            throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workstationService.copyWorkstationSettings(context, copyFromProjectOID, destinationProjectOID, copySites,
                copyProjectFunctionTeams, copyParts, copyOpenItemTeam, copyProjectCoordinators, workstationsToCopy);


    }
}
