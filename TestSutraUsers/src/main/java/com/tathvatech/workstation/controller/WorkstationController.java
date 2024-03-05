package com.tathvatech.workstation.controller;

import ch.qos.logback.classic.Logger;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.ServiceLocator;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Project;
import com.tathvatech.workstation.common.UnitWorkstationQuery;
import com.tathvatech.workstation.entity.UnitWorkstation;
import com.tathvatech.workstation.entity.Workstation;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/workstation")
@RestController
@RequiredArgsConstructor
public class WorkstationController {
    
    private final WorkstationService workstationService;

    public  void createWorkstation(UserContext context, Workstation workstation) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.createWorkstation(context, workstation);
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

    public  void updateWorkstation(UserContext context, Workstation workstation) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.updateWorkstation(context, workstation);
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

    public  List<WorkstationQuery> getWorkstationList() throws Exception
    {
        List<WorkstationQuery> l = workstationService.getWorkstationList();
        return l;
    }

    public  List<WorkstationQuery> getWorkstationsForProject(int projectPk)
    {
        List<WorkstationQuery> l = workstationService.getWorkstationsForProject(projectPk);
        return l;
    }

    public  List<WorkstationQuery> getWorkstations(WorkstationFilter filter)
    {
        return workstationService.getWorkstations(filter);
    }

    public  List<WorkstationQuery> getWorkstationsAssignableForProject(ProjectOID projectOID) throws Exception
    {
        List<WorkstationQuery> l = workstationService.getWorkstationsAssignableForProject(projectOID);
        return l;
    }

    public  List<WorkstationQuery> getWorkstationsForSite(int sitePk) throws Exception
    {
        List<WorkstationQuery> l = workstationService.getWorkstationsForSite(sitePk);
        return l;
    }

    public  List<WorkstationQuery> getWorkstationsForSiteAndProject(int sitePk, int projectPk) throws Exception
    {
        List<WorkstationQuery> l = workstationService.getWorkstationsForSiteAndProject(sitePk, projectPk);
        return l;
    }

    public  Workstation getWorkstation(int workstationPk)
    {
        return workstationService.getWorkstation(new WorkstationOID(workstationPk));
    }

    public  WorkstationQuery getWorkstationQueryByPk(WorkstationOID workstationOID)
    {
        return workstationService.getWorkstationQueryByPk(workstationOID);
    }

    public  void addWorkstationToProject(UserContext context, int projectPk, WorkstationOID workstationOID)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.addWorkstationToProject(context, projectPk, workstationOID);
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

    public  void removeWorkstationFromProject(UserContext context, int projectPk, WorkstationOID workstationOID)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.removeWorkstationFromProject(context, projectPk, workstationOID);
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

    public  void removeAllWorkstationsFromProject(UserContext context, int projectPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.removeAllWorkstationsFromProject(context, projectPk);
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
    public List<UnitWorkstationQuery> getWorkstationsForUnit(int unitPk, ProjectOID projectOID,
                                                             boolean includeChildUnits) throws Exception
    {
        return workstationService.getWorkstationsForUnit(new UnitOID(unitPk, null), projectOID, includeChildUnits);
    }

    public  List<WorkstationQuery> getWorkstationsForUnit(int unitPk, ProjectOID projectOID) throws Exception
    {
        return workstationService.getWorkstationsForUnit(new UnitOID(unitPk, null), projectOID);
    }

    public  void addWorkstationToUnit(UserContext context, ProjectOID projectOID, UnitOID unitOID,
                                            WorkstationOID workstationOID) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.addWorkstationToUnit(context, projectOID, unitOID, workstationOID);
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

            workstationService.removeWorkstationFromUnit(context, unitOID, projectOID, workstationOID);
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

    public  UnitLocation getUnitWorkstation(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID)
            throws Exception
    {
        return workstationService.getUnitWorkstation(unitPk, projectOID, workstationOID);
    }

    /*
     * Not used anywhere. Please check and remove if so.
     */
    @Deprecated
    public  List<UnitLocationQuery> getUnitWorkstationStatus(UnitOID unitOID, ProjectOID projectOID)
            throws Exception
    {
        return workstationService.getUnitWorkstationStatus(unitOID, projectOID);
    }

    public  UnitLocationQuery getUnitWorkstationStatus(int unitPk, ProjectOID projectOID,
                                                             WorkstationOID workstationOID)
    {
        return workstationService.getUnitWorkstationStatus(new UnitOID(unitPk, null), projectOID, workstationOID);
    }

    public  List<UnitLocationQuery> getUnitWorkstationStatusHistory(int unitPk, ProjectOID projectOID,
                                                                          WorkstationOID workstationOID) throws Exception
    {
        return workstationService.getUnitWorkstationStatusHistory(unitPk, projectOID, workstationOID);
    }

    public  void setUnitWorkstationStatus(UserContext userContext, ProjectOID projectOID, int unitPk,
                                                WorkstationOID workstationOID, String status) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.setUnitWorkstationStatus(userContext, unitPk, projectOID, workstationOID, status);

            con.commit();
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
    }

    public  void recordWorkstationFormAccess(TestProcOID testProcOID)
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.recordWorkstationFormAccess(testProcOID);

            con.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        finally
        {
        }
    }

    public  void recordWorkstationSave(TestProcOID testProcOID)
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.recordWorkstationSave(testProcOID);

            con.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        finally
        {
        }
    }

    public  void recordWorkstationFormLock(TestProcOID testProcOID)
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.recordWorkstationFormLock(testProcOID);

            con.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        finally
        {
        }
    }

    public  void recordWorkstationFormUnlock(TestProcOID testProcOID)
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.recordWorkstationFormUnlock(testProcOID);

            con.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        finally
        {
        }
    }
    public  void deleteWorkstation(UserContext context, WorkstationOID workstationOID) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.deleteWorstation(context, workstationOID);
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
    public  void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                              WorkstationQuery workstationQuery, Integer[] selectedUnits) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.copyWorkstationToUnits(context, projectQuery, workstationQuery, selectedUnits);
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

            workstationService.setWorkstationProjectPartTeamSetupToUnits(context, projectQuery, workstationQuery,
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

            workstationService.cascadeWorkstationToUnits(context, projectQuery, workstationQuery, selectedUnitsForForm,
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
                UnitObj unit = workstationService.getUnitByPk(new UnitOID(selectedUnits[x]));
                if (unit != null)
                {
                    units.add(unit);
                }
            }

            workstationService.deleteWorkstationToUnits(context, projectQuery, workstationQuery,
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

    public  List<Project> getProjectsForWorkstation(UserContext context, WorkstationOID workstationOID)
            throws Exception
    {
        return workstationService.getProjectsForWorkstation(context, workstationOID);
    }

    public  float getWorkstationPercentComplete(UserContext context, int unitPk, ProjectOID projectOID,
                                                      WorkstationOID workstationOID, boolean includeChildren) throws Exception
    {
        return workstationService.getWorkstationPercentComplete(context, unitPk, projectOID, workstationOID,
                includeChildren);
    }
    public  void moveWorkstationOrderUp(UserContext context, WorkstationOID workstationOID)
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.moveWorkstationOrderUp(context, workstationOID);
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
                Logger logger = null;
                logger.error("Count not rollback transaction", ex);
            }
        }
        finally
        {
        }
    }

    public  void moveWorkstationOrderDown(UserContext context, WorkstationOID workstationOID)
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.moveWorkstationOrderDown(context, workstationOID);
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
        }
        finally
        {
        }
    }
    public  UnitWorkstation getUnitWorkstationSetting(int unitPk, ProjectOID projectOID,
                                                            WorkstationOID workstationOID)
    {
        return workstationService.getUnitWorkstationSetting(unitPk, projectOID, workstationOID);
    }

    public  UnitWorkstation updateUnitWorkstationSetting(UnitWorkstation unitWorkstation) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            UnitWorkstation w = workstationService.updateUnitWorkstationSetting(unitWorkstation);
            con.commit();
            return w;
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
    public  void copyWorkstationSettings(UserContext context, ProjectOID copyFromProjectOID,
                                               ProjectOID destinationProjectOID, boolean copySites, boolean copyProjectFunctionTeams, boolean copyParts,
                                               boolean copyOpenItemTeam, boolean copyProjectCoordinators, List<Object[]> workstationsToCopy)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            workstationService.copyWorkstationSettings(context, copyFromProjectOID, destinationProjectOID, copySites,
                    copyProjectFunctionTeams, copyParts, copyOpenItemTeam, copyProjectCoordinators, workstationsToCopy);

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



}
