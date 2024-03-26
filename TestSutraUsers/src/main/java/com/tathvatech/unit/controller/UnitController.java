package com.tathvatech.unit.controller;


import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.entity.ProjectForm;
import com.tathvatech.project.enums.ProjectPropertyEnum;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.project.service.ProjectTemplateManager;
import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.unit.common.UnitLocationQuery;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.common.UnitQuery;
import com.tathvatech.unit.dao.UnitInProjectDAO;
import com.tathvatech.unit.enums.CommonEnums;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.unit.request.*;
import com.tathvatech.unit.service.UnitManager;
import com.tathvatech.unit.service.UnitService;
import com.tathvatech.unit.sync.UnitChangeSynchronizer;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.ServiceLocator;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.common.DummyWorkstation;
import com.tathvatech.workstation.common.UnitInProjectObj;
import com.tathvatech.workstation.common.WorkstationQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unit")
public class UnitController {
    private  final Logger logger = LoggerFactory.getLogger(UnitController.class);
    
    private final UnitService unitService;
    private final UnitManager unitManager;
    private final PersistWrapper persistWrapper;
    private final DummyWorkstation dummyWorkstation;
    private final ProjectService projectService;

    @GetMapping("/getUnitsBySerialNos")
    public  List<UnitQuery> getUnitsBySerialNos(@RequestBody  GetUnitsBySerialNosRequest getUnitsBySerialNosRequest)
    {
        return unitService.getUnitsBySerialNos(getUnitsBySerialNosRequest.getSerialNos(), getUnitsBySerialNosRequest.getProjectOID());
    }

   @GetMapping("/getUnitsByPks")
   public  List<UnitQuery> getUnitsByPks(@RequestBody GetUnitsByPksRequest getUnitsByPksRequest)
    {
        return unitService.getUnitsByPks(getUnitsByPksRequest.getUnitPks(), getUnitsByPksRequest.getProjectOID());
    }

   @GetMapping("/getUnitsForProject")
   public  List<UnitQuery> getUnitsForProject(@RequestBody GetUnitsForProjectRequest getUnitsForProjectRequest) throws Exception
    {
        // return unitService.getUnitsForProject(unitFilter, parent);
        return unitService.getUnitsForProjectNew(getUnitsForProjectRequest.getUnitFilter(), getUnitsForProjectRequest.getParent());
    }

    /**
     * returns the list of Open units under the project
     *
     * @param
     * @return
     * @throws Exception
     */
  @GetMapping("/getUnitsForProjectLinear")
   public  List<UnitQuery> getUnitsForProjectLinear(@RequestBody UnitFilterBean unitFilter) throws Exception
    {
        // return unitService.getUnitsForProjectLinear(unitFilter, false);
        return unitService.getUnitsForProjectNew(unitFilter, null);
    }
   @PostMapping("/createUnit")
   public  void createUnit(@RequestBody CreateUnitRequest createUnitRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        createUnit(context, createUnitRequest.getProjectPk(), createUnitRequest.getUnit(), createUnitRequest.isCreateAsPlannedUnit(),false);
    }

   public UnitBean createUnit( UserContext context, int projectPk, UnitBean unitBean, boolean createAsPlannedUnit, boolean pendingReview)
            throws Exception
    {

        UnitOID lockableOID = null;
        if (unitBean.getParentPk() != null && unitBean.getParentPk() != 0)
        {
            UnitOID parentOID = new UnitOID(unitBean.getParentPk());

            UnitInProjectObj parentUnit = unitManager.getUnitInProject(parentOID, new ProjectOID(projectPk));
            Integer rootParentPk = parentUnit.getRootParentPk();

            UnitChangeSynchronizer sync = UnitChangeSynchronizer.getInstance();
            lockableOID = sync.getLocableUnitOID(rootParentPk);
        }
        else
        {
            lockableOID = new UnitOID(0); // just a dummy unitOID to lock on we dont have to lock as this is a root unit we are creating.
        }

        UnitObj uobj;
        synchronized (lockableOID)
        {

            // this starts a new transaction. DONT REMOVE this commit above.
            //if removed, the transaction from second thread will not see the parent unit changes. as the first thread
            // made these changes after the start of the transaction by the second thread.
            // thus we will get non-unique records with 2 valid records in the unit_project_ref_h table.

            uobj = unitService.createUnit(context, projectPk, unitBean, createAsPlannedUnit, pendingReview);

        }

        return uobj.getUnitBean(new ProjectOID(projectPk));
    }
   @PostMapping("/createUnitAtWorkstation")
   public  UnitBean createUnitAtWorkstation( @RequestBody CreateUnitAtWorkstationRequest createUnitAtWorkstationRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UnitObj unitObj = unitService.createUnitAtWorkstation(context, createUnitAtWorkstationRequest.getProjectOID(), createUnitAtWorkstationRequest.getWorkstationOID(), createUnitAtWorkstationRequest.getUnit(), createUnitAtWorkstationRequest.isCopyPartSpecificFormsToWorkstation(),createUnitAtWorkstationRequest.isPendingReview());
            return unitObj.getUnitBean(createUnitAtWorkstationRequest.getProjectOID());
    }

    @PutMapping("/updateUnit")
    public  void updateUnit( @RequestBody UnitObj unit) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        unitService.updateUnit(context, unit);
    }

    @PutMapping("/updateUnits")
    public  void updateUnits(@RequestBody UnitObj unit) throws Exception
    {
        unitService.updateUnit(unit);
    }
    @GetMapping("/getUnitByPk")
    public  UnitObj getUnitByPk(@RequestBody UnitOID unitOID)
    {
        return unitService.getUnitByPk(unitOID);
    }

    @GetMapping("/getUnitQueryByPk")
    public  UnitQuery getUnitQueryByPk(@RequestBody  GetUnitQueryByPkRequest getUnitQueryByPkRequest)
    {
        return unitService.getUnitQueryByPk(getUnitQueryByPkRequest.getUnitPk(), getUnitQueryByPkRequest.getProjectOID());
    }
    @PostMapping("/addFormToUnit")
    public  TestProcOID addFormToUnit(@RequestBody AddFormToUnitRequest addFormToUnitRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UnitChangeSynchronizer sync = UnitChangeSynchronizer.getInstance();
            UnitOID lockableOID = sync.getLocableUnitOID(addFormToUnitRequest.getFormPk());

            TestProcOID testProcOID = null;
            synchronized (lockableOID)
            {

                // this starts a new transaction. DONT REMOVE this commit above.
                //if removed, the transaction from second thread will not see the unit changes (adding unitworkstation and unitlocationEntries to unit).
                //as the first thread made these changes after the start of the transaction by the second thread.
                // thus we will get non-unique records with 2 valid records in the unitworkstation and unitlocation table.

               // Fix after doing form
               // testProcOID = unitService.addFormToUnit(context, projectForm, unitPk, projectOID,
                      //  workstationOID, formPk, testName, makeWorkstationInProgress, reviewPending);

            }
            return testProcOID;
    }

   @DeleteMapping("/removeAllFormsFromUnit")
   public  void removeAllFormsFromUnit( @RequestBody RemoveAllFormsFromUnitRequest removeAllFormsFromUnitRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            unitService.removeAllFormsFromUnit(context, removeAllFormsFromUnitRequest.getUnitPk(), removeAllFormsFromUnitRequest.getProjectOID(), removeAllFormsFromUnitRequest.getWorkstationOID());

    }

   @DeleteMapping("/deleteFormFromUnit")
   public  void deleteFormFromUnit( @RequestBody TestProcOID testProcOid) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        unitService.deleteTestProcFromUnit(context, testProcOid);

    }
  /*  public  List<FormQuery> getAllFormsForUnit(UnitOID unitOID, ProjectOID projectOID) throws Exception
    {
        return unitService.getAllFormsForUnit(unitOID, projectOID);
    }

    public  List<FormQuery> getFormsForUnit(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID)
            throws Exception
    {
        return unitService.getFormsForUnit(new UnitOID(unitPk, null), projectOID, workstationOID);
    }*/
   @GetMapping("/getUsersForUnit")
   public  List<User> getUsersForUnit(@RequestBody GetUsersForUnitRequest getUsersForUnitRequest)
            throws Exception
    {
        if (dummyWorkstation.getPk() == getUsersForUnitRequest.getWorkstationOID().getPk())
        {
            return projectService.getUsersForProject((int) getUsersForUnitRequest.getProjectOID().getPk(), getUsersForUnitRequest.getWorkstationOID());
        } else
        {
            return unitService.getUsersForUnit(getUsersForUnitRequest.getUnitPk(),getUsersForUnitRequest.getProjectOID(), getUsersForUnitRequest.getWorkstationOID());
        }
    }

    @GetMapping("/getUsersForUnitInRole")
    public  List<User> getUsersForUnitInRole(@RequestBody GetUsersForUnitInRoleRequest getUsersForUnitInRoleRequest) throws Exception
    {
        if (dummyWorkstation.getPk() == getUsersForUnitInRoleRequest.getWorkstationOID().getPk())
        {
            return unitService.getUsersForProjectInRole((int) getUsersForUnitInRoleRequest.getProjectOID().getPk(), getUsersForUnitInRoleRequest.getWorkstationOID(), getUsersForUnitInRoleRequest.getRoleName());
        } else
        {
            return unitService.getUsersForUnitInRole(getUsersForUnitInRoleRequest.getUnitPk(), getUsersForUnitInRoleRequest.getProjectOID(), getUsersForUnitInRoleRequest.getWorkstationOID(),getUsersForUnitInRoleRequest.getRoleName());
        }
    }
    @PutMapping("/isUsersForUnitInRole")
    public  boolean isUsersForUnitInRole(@RequestBody IsUsersForUnitInRoleRequest isUsersForUnitInRoleRequest)
    {
        if (dummyWorkstation.getPk() == isUsersForUnitInRoleRequest.getWorkstationOID().getPk())
        {
            return projectService.isUsersForProjectInRole(isUsersForUnitInRoleRequest.getUserPk(), isUsersForUnitInRoleRequest.getProjectOID(),isUsersForUnitInRoleRequest.getWorkstationOID(), isUsersForUnitInRoleRequest.getRoleName());
        } else
        {
            return unitService.isUsersForUnitInRole(isUsersForUnitInRoleRequest.getUserPk(), isUsersForUnitInRoleRequest.getUnitPk(), isUsersForUnitInRoleRequest.getProjectOID(), isUsersForUnitInRoleRequest.getWorkstationOID(), isUsersForUnitInRoleRequest.getRoleName());
        }
    }
   @DeleteMapping("/removeAllUsersFromUnit")
   public  void removeAllUsersFromUnit(@RequestBody RemoveAllUsersFromUnitRequest removeAllUsersFromUnitRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        unitService.removeAllUsersFromUnit(context, removeAllUsersFromUnitRequest.getUnitOID(), removeAllUsersFromUnitRequest.getProjectOID(), removeAllUsersFromUnitRequest.getWorkstationOID());

    }
    @PostMapping("/addTesterToUnit")
    public  void addTesterToUnit(@RequestBody AddTesterToUnitRequest addTesterToUnitRequest) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            unitService.addTesterToUnit(context, addTesterToUnitRequest.getUnitPk(), addTesterToUnitRequest.getProjectOID(), addTesterToUnitRequest.getWorkstationOID(), addTesterToUnitRequest.getUserPk());

    }

   @PostMapping("/addVerifierToUnit")
   public  void addVerifierToUnit(@RequestBody AddVerifierToUnitRequest  addVerifierToUnitRequest) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            unitService.addVerifierToUnit(context, addVerifierToUnitRequest.getUnitPk(), addVerifierToUnitRequest.getProjectOID(), addVerifierToUnitRequest.getWorkstationOID(),addVerifierToUnitRequest.getUserPk());

    }

   @PostMapping("/addApproverToUnit")
   public  void addApproverToUnit(@RequestBody AddApproverToUnitRequest addApproverToUnitRequest) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            unitService.addApproverToUnit(context, addApproverToUnitRequest.getUnitPk(), addApproverToUnitRequest.getProjectOID(), addApproverToUnitRequest.getWorkstationOID(), addApproverToUnitRequest.getUserPk());

    }

  @PostMapping("/addReadonlyUserToUnit")
  public  void addReadonlyUserToUnit(@RequestBody  AddReadonlyUserToUnitRequest addReadonlyUserToUnitRequest) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            unitService.addReadonlyUserToUnit(context, addReadonlyUserToUnitRequest.getUnitPk(), addReadonlyUserToUnitRequest.getProjectOID(), addReadonlyUserToUnitRequest.getWorkstationOID(), addReadonlyUserToUnitRequest.getUserPk());

    }
   @GetMapping("/getUnitLocationHistory")
   public  List<UnitLocationQuery> getUnitLocationHistory(@RequestBody GetUnitLocationHistoryRequest getUnitLocationHistoryRequest)
            throws Exception
    {
        return unitService.getUnitLocationHistory(getUnitLocationHistoryRequest.getUnitOID(), getUnitLocationHistoryRequest.getProjectOID());
    }

   @GetMapping("/getUnitLocationHistorys")
   public  List<UnitLocationQuery> getUnitLocationHistorys(@RequestBody GetUnitLocationHistorysRequest getUnitLocationHistorysRequest) throws Exception
    {
        return unitService.getUnitLocationHistory(getUnitLocationHistorysRequest.getUnitOID(),getUnitLocationHistorysRequest.getProjectOID(), getUnitLocationHistorysRequest.isIncludeChildren());
    }
   @DeleteMapping("/removeUnitFromProject")
   public  void removeUnitFromProject(@RequestBody RemoveUnitFromProjectRequest removeUnitFromProjectRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            unitService.removeUnitFromProject(context, removeUnitFromProjectRequest.getUnitOID(),removeUnitFromProjectRequest.getProjectOID(), removeUnitFromProjectRequest.getDeleteUnitOption());

    }
    public  void openUnit( UnitBean rootUnitToOpen, List<UnitBean> unitBeanAndChildrenList,
                                ProjectOID lastOpenProjectOID, ProjectOID destinationProjectOID) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            unitService.openUnit(context, rootUnitToOpen, unitBeanAndChildrenList, lastOpenProjectOID,
                    destinationProjectOID);

    }

   @DeleteMapping("/removeUserFromUnit")
   public  void removeUserFromUnit(@RequestBody RemoveUserFromUnitRequest removeUserFromUnitRequest) throws Exception
    {


            unitService.removeUserFromUnit(removeUserFromUnitRequest.getUserPk(),removeUserFromUnitRequest.getUnitPk(), removeUserFromUnitRequest.getProjectOID(), removeUserFromUnitRequest.getWorkstationOID(), removeUserFromUnitRequest.getRole());

    }
   @GetMapping("/getUnitPercentComplete")
   public  float getUnitPercentComplete(@RequestBody GetUnitPercentCompleteRequest getUnitPercentCompleteRequest ) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return unitService.getUnitPercentComplete(context,getUnitPercentCompleteRequest.getUnitPk(), getUnitPercentCompleteRequest.getProjectOID(), getUnitPercentCompleteRequest.isIncludeChildren());
    }
   @GetMapping("/getUnitCount")
   public  HashMap<ProjectOID, Integer> getUnitCount(@RequestBody GetUnitCountRequest getUnitCountRequest)
            throws Exception
    {
        return unitService.getUnitCount(getUnitCountRequest.getProjectPks(), getUnitCountRequest.isIncludeChildren());
    }

   @GetMapping("/getUnitCounts")
   public  int getUnitCounts(@RequestBody GetUnitCountsRequest getUnitCountsRequest) throws Exception
    {
        return unitService.getUnitCount(getUnitCountsRequest.getProjectPk(), getUnitCountsRequest.isIncludeChildren());
    }
    @GetMapping("/getUnitsWithWorkstationsAssigned")
    public  List<UnitQuery> getUnitsWithWorkstationsAssigned(@RequestBody GetUnitsWithWorkstationsAssignedRequest getUnitsWithWorkstationsAssignedRequest)
    {
        return unitService.getUnitsWithWorkstationsAssigned(getUnitsWithWorkstationsAssignedRequest.getProjectOID(), getUnitsWithWorkstationsAssignedRequest.getWorkstationOID());
    }

  @GetMapping("/getUnitsWithWorkstationsAssigneds")
  public  List<UnitQuery> getUnitsWithWorkstationsAssigneds(@RequestBody GetUnitsWithWorkstationsAssignedsRequest getUnitsWithWorkstationsAssignedsRequest)
    {
        return unitService.getUnitsWithWorkstationsAssigned(getUnitsWithWorkstationsAssignedsRequest.getProjectOID(), getUnitsWithWorkstationsAssignedsRequest.getProjectPartOID(),getUnitsWithWorkstationsAssignedsRequest.getWorkstationOID());
    }
    @PostMapping("/addUnitToProject")
    public  void addUnitToProject( @RequestBody AddUnitToProjectRequest addUnitToProjectRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            unitService.addUnitToProject(context,addUnitToProjectRequest.getSourceProjectOID(), addUnitToProjectRequest.getDestinationProjectOID(), addUnitToProjectRequest.getRootUnitBean(),
                    addUnitToProjectRequest.getUnitBeanAndChildrenList(), addUnitToProjectRequest.isAddAsPlannedUnit());


    }
    public  void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                              WorkstationQuery workstationQuery, Integer[] selectedUnits) throws Exception
    {


            unitService.copyWorkstationToUnits(context, projectQuery, workstationQuery, selectedUnits);

    }



    @PutMapping("/setWorkstationProjectPartTeamSetupToUnits")
    public  void setWorkstationProjectPartTeamSetupToUnits( @RequestBody  SetWorkstationProjectPartTeamSetupToUnitsRequest setWorkstationProjectPartTeamSetupToUnitsRequest) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            unitService.setWorkstationProjectPartTeamSetupToUnits(context, setWorkstationProjectPartTeamSetupToUnitsRequest.getProjectQuery(), setWorkstationProjectPartTeamSetupToUnitsRequest.getWorkstationQuery(),
                    setWorkstationProjectPartTeamSetupToUnitsRequest.getProjectPartOID(), setWorkstationProjectPartTeamSetupToUnitsRequest.getSelectedUnits(), setWorkstationProjectPartTeamSetupToUnitsRequest.isCopyDefaultTeamIfNoProjectPartTeamIsSet());

    }

    public  void cascadeWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                                 WorkstationQuery workstationQuery, Integer[] selectedUnitsForForm, Integer[] selectedUnitsForTeam)
            throws Exception
    {


            unitService.cascadeWorkstationToUnits(context, projectQuery, workstationQuery, selectedUnitsForForm,
                    selectedUnitsForTeam);

    }

    @DeleteMapping("/deleteWorkstationToUnits")
    public  void deleteWorkstationToUnits( @RequestBody DeleteWorkstationToUnitsRequest deleteWorkstationToUnitsRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // delete of workstation happens at the unit level where as the
            // cascade of forms happen at the car level also,
            // they use the same inteface where units are captured as int pks.
            // so, here I am filtering the unit ones only.
            // we change something in the UI to make it generic. dont know at
            // this point.
            List<UnitObj> units = new ArrayList();
            for (int x = 0; x < deleteWorkstationToUnitsRequest.getSelectedUnits().length; x++)
            {
                UnitObj unit = unitService.getUnitByPk(new UnitOID(deleteWorkstationToUnitsRequest.getSelectedUnits()[x]));
                if (unit != null)
                {
                    units.add(unit);
                }
            }

            unitService.deleteWorkstationToUnits(context, deleteWorkstationToUnitsRequest.getProjectQuery(), deleteWorkstationToUnitsRequest.getWorkstationQuery(),
                    units.toArray(new UnitObj[units.size()]));

    }

    @DeleteMapping("/removeWorkstationFromUnit")
    public  void removeWorkstationFromUnit( @RequestBody RemoveWorkstationFromUnitRequest removeWorkstationFromUnitRequest) throws Exception {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        unitService.removeWorkstationFromUnit(context, removeWorkstationFromUnitRequest.getUnitOID(), removeWorkstationFromUnitRequest.getProjectOID(), removeWorkstationFromUnitRequest.getWorkstationOID());
    }

}
