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
import com.tathvatech.unit.request.UnitFilterBean;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public  List<UnitQuery> getUnitsBySerialNos(String[] serialNos, ProjectOID projectOID)
    {
        return unitService.getUnitsBySerialNos(serialNos, projectOID);
    }

    public  List<UnitQuery> getUnitsByPks(int[] unitPks, ProjectOID projectOID)
    {
        return unitService.getUnitsByPks(unitPks, projectOID);
    }

    public  List<UnitQuery> getUnitsForProject(UnitFilterBean unitFilter, UnitOID parent) throws Exception
    {
        // return unitService.getUnitsForProject(unitFilter, parent);
        return unitService.getUnitsForProjectNew(unitFilter, parent);
    }

    /**
     * returns the list of Open units under the project
     *
     * @param
     * @return
     * @throws Exception
     */
    public  List<UnitQuery> getUnitsForProjectLinear(UnitFilterBean unitFilter) throws Exception
    {
        // return unitService.getUnitsForProjectLinear(unitFilter, false);
        return unitService.getUnitsForProjectNew(unitFilter, null);
    }
    public  void createUnit(UserContext context, int projectPk, UnitBean unit, boolean createAsPlannedUnit)
            throws Exception
    {
        createUnit(context, projectPk, unit, createAsPlannedUnit, false);
    }

    public UnitBean createUnit(UserContext context, int projectPk, UnitBean unitBean, boolean createAsPlannedUnit, boolean pendingReview)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

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
                con.commit();
                // this starts a new transaction. DONT REMOVE this commit above.
                //if removed, the transaction from second thread will not see the parent unit changes. as the first thread
                // made these changes after the start of the transaction by the second thread.
                // thus we will get non-unique records with 2 valid records in the unit_project_ref_h table.

                uobj = unitService.createUnit(context, projectPk, unitBean, createAsPlannedUnit, pendingReview);
                con.commit();
            }

            return uobj.getUnitBean(new ProjectOID(projectPk));
        }
        catch (Exception ex)
        {
            if(con != null)
                con.rollback();
            throw ex;
        }
        finally
        {
            if(con != null)
                con.rollback();
        }
    }
    public  UnitBean createUnitAtWorkstation(UserContext context, ProjectOID projectOID,
                                             WorkstationOID workstationOID, UnitBean unit, boolean copyPartSpecificFormsToWorkstation,
                                             boolean pendingReview)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            UnitObj unitObj = unitService.createUnitAtWorkstation(context, projectOID, workstationOID, unit, copyPartSpecificFormsToWorkstation, pendingReview);
            return unitObj.getUnitBean(projectOID);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void updateUnit(UserContext context, UnitObj unit) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.updateUnit(context, unit);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void updateUnit(UnitObj unit) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.updateUnit(unit);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  UnitObj getUnitByPk(UnitOID unitOID)
    {
        return unitService.getUnitByPk(unitOID);
    }

    public  UnitQuery getUnitQueryByPk(int unitPk, ProjectOID projectOID)
    {
        return unitService.getUnitQueryByPk(unitPk, projectOID);
    }
    public  TestProcOID addFormToUnit(UserContext context, ProjectForm projectForm, int unitPk, ProjectOID projectOID,
                                      WorkstationOID workstationOID, int formPk, String testName, boolean makeWorkstationInProgress, boolean reviewPending) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            UnitChangeSynchronizer sync = UnitChangeSynchronizer.getInstance();
            UnitOID lockableOID = sync.getLocableUnitOID(unitPk);

            TestProcOID testProcOID = null;
            synchronized (lockableOID)
            {
                con.commit();
                // this starts a new transaction. DONT REMOVE this commit above.
                //if removed, the transaction from second thread will not see the unit changes (adding unitworkstation and unitlocationEntries to unit).
                //as the first thread made these changes after the start of the transaction by the second thread.
                // thus we will get non-unique records with 2 valid records in the unitworkstation and unitlocation table.

               // Fix after doing form
               // testProcOID = unitService.addFormToUnit(context, projectForm, unitPk, projectOID,
                      //  workstationOID, formPk, testName, makeWorkstationInProgress, reviewPending);
                con.commit();
            }

            return testProcOID;
        }
        catch (Exception ex)
        {
            if(con != null)
                con.rollback();
            throw ex;
        }
        finally
        {
            if(con != null)
                con.rollback();
        }
    }

    public  void removeAllFormsFromUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                              WorkstationOID workstationOID) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.removeAllFormsFromUnit(context, unitPk, projectOID, workstationOID);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void deleteFormFromUnit(UserContext context, TestProcOID testProcOid) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.deleteTestProcFromUnit(context, testProcOid);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
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
    public  List<User> getUsersForUnit(ProjectOID projectOID, int unitPk, WorkstationOID workstationOID)
            throws Exception
    {
        if (dummyWorkstation.getPk() == workstationOID.getPk())
        {
            return projectService.getUsersForProject((int) projectOID.getPk(), workstationOID);
        } else
        {
            return unitService.getUsersForUnit(unitPk, projectOID, workstationOID);
        }
    }

    public  List<User> getUsersForUnitInRole(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID,
                                                   String roleName) throws Exception
    {
        if (dummyWorkstation.getPk() == workstationOID.getPk())
        {
            return unitService.getUsersForProjectInRole((int) projectOID.getPk(), workstationOID, roleName);
        } else
        {
            return unitService.getUsersForUnitInRole(unitPk, projectOID, workstationOID, roleName);
        }
    }
    public  boolean isUsersForUnitInRole(ProjectOID projectOID, int userPk, int unitPk,
                                               WorkstationOID workstationOID, String roleName)
    {
        if (dummyWorkstation.getPk() == workstationOID.getPk())
        {
            return projectService.isUsersForProjectInRole(userPk, projectOID, workstationOID, roleName);
        } else
        {
            return unitService.isUsersForUnitInRole(userPk, unitPk, projectOID, workstationOID, roleName);
        }
    }
    public  void removeAllUsersFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID,
                                              WorkstationOID workstationOID) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.removeAllUsersFromUnit(context, unitOID, projectOID, workstationOID);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  void addTesterToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                       WorkstationOID workstationOID, int userPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.addTesterToUnit(context, unitPk, projectOID, workstationOID, userPk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void addVerifierToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                         WorkstationOID workstationOID, int userPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.addVerifierToUnit(context, unitPk, projectOID, workstationOID, userPk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void addApproverToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                         WorkstationOID workstationOID, int userPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.addApproverToUnit(context, unitPk, projectOID, workstationOID, userPk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void addReadonlyUserToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                             WorkstationOID workstationOID, int userPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.addReadonlyUserToUnit(context, unitPk, projectOID, workstationOID, userPk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID)
            throws Exception
    {
        return unitService.getUnitLocationHistory(unitOID, projectOID);
    }

    public  List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID,
                                                                 boolean includeChildren) throws Exception
    {
        return unitService.getUnitLocationHistory(unitOID, projectOID, includeChildren);
    }
    public  void removeUnitFromProject(UserContext context, UnitOID unitOID, ProjectOID projectOID,
                                             CommonEnums.DeleteOptionEnum deleteUnitOption) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.removeUnitFromProject(context, unitOID, projectOID, deleteUnitOption);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  void openUnit(UserContext context, UnitBean rootUnitToOpen, List<UnitBean> unitBeanAndChildrenList,
                                ProjectOID lastOpenProjectOID, ProjectOID destinationProjectOID) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.openUnit(context, rootUnitToOpen, unitBeanAndChildrenList, lastOpenProjectOID,
                    destinationProjectOID);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void removeUserFromUnit(int userPk, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID,
                                          String role) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.removeUserFromUnit(userPk, unitPk, projectOID, workstationOID, role);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  float getUnitPercentComplete(UserContext context, int unitPk, ProjectOID projectOID,
                                               boolean includeChildren) throws Exception
    {
        return unitService.getUnitPercentComplete(context, unitPk, projectOID, includeChildren);
    }
    public  HashMap<ProjectOID, Integer> getUnitCount(List<Integer> projectPks, boolean includeChildren)
            throws Exception
    {
        return unitService.getUnitCount(projectPks, includeChildren);
    }

    public  int getUnitCount(int projectPk, boolean includeChildren) throws Exception
    {
        return unitService.getUnitCount(projectPk, includeChildren);
    }
    public  List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, WorkstationOID workstationOID)
    {
        return unitService.getUnitsWithWorkstationsAssigned(projectOID, workstationOID);
    }

    public  List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, ProjectPartOID projectPartOID,
                                                                   WorkstationOID workstationOID)
    {
        return unitService.getUnitsWithWorkstationsAssigned(projectOID, projectPartOID, workstationOID);
    }
    public  void addUnitToProject(UserContext context, ProjectOID sourceProjectOID,
                                        ProjectOID destinationProjectOID, UnitBean rootUnitBean, List<UnitBean> unitBeanAndChildrenList,
                                        boolean addAsPlannedUnit) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.addUnitToProject(context, sourceProjectOID, destinationProjectOID, rootUnitBean,
                    unitBeanAndChildrenList, addAsPlannedUnit);

            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }
    public  void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                              WorkstationQuery workstationQuery, Integer[] selectedUnits) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.copyWorkstationToUnits(context, projectQuery, workstationQuery, selectedUnits);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }



    public  void setWorkstationProjectPartTeamSetupToUnits(UserContext context, ProjectQuery projectQuery,
                                                                 WorkstationQuery workstationQuery, ProjectPartOID projectPartOID, Integer[] selectedUnits,
                                                                 boolean copyDefaultTeamIfNoProjectPartTeamIsSet) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.setWorkstationProjectPartTeamSetupToUnits(context, projectQuery, workstationQuery,
                    projectPartOID, selectedUnits, copyDefaultTeamIfNoProjectPartTeamIsSet);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void cascadeWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                                 WorkstationQuery workstationQuery, Integer[] selectedUnitsForForm, Integer[] selectedUnitsForTeam)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.cascadeWorkstationToUnits(context, projectQuery, workstationQuery, selectedUnitsForForm,
                    selectedUnitsForTeam);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void deleteWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                          WorkstationQuery workstationQuery, Integer[] selectedUnits) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            // delete of workstation happens at the unit level where as the
            // cascade of forms happen at the car level also,
            // they use the same inteface where units are captured as int pks.
            // so, here I am filtering the unit ones only.
            // we change something in the UI to make it generic. dont know at
            // this point.
            List<UnitObj> units = new ArrayList();
            for (int x = 0; x < selectedUnits.length; x++)
            {
                UnitObj unit = unitService.getUnitByPk(new UnitOID(selectedUnits[x]));
                if (unit != null)
                {
                    units.add(unit);
                }
            }

            unitService.deleteWorkstationToUnits(context, projectQuery, workstationQuery,
                    units.toArray(new UnitObj[units.size()]));
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void removeWorkstationFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID,
                                                 WorkstationOID workstationOID) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            unitService.removeWorkstationFromUnit(context, unitOID, projectOID, workstationOID);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

}
