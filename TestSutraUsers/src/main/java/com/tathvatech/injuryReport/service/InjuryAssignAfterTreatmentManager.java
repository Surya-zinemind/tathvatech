package com.tathvatech.injuryReport.service;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.tathvatech.testsutra.injury.common.InjuryAssignAfterTreatmentBean;
import com.tathvatech.testsutra.injury.common.InjuryAssignAfterTreatmentQuery;
import com.tathvatech.testsutra.injury.common.InjuryOID;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.InjuryAfterTreatmentOID;

public class InjuryAssignAfterTreatmentManager {
    private static Logger logger = Logger.getLogger(InjuryAssignAfterTreatmentManager.class);
    public static InjuryAssignAfterTreatment create(UserContext context,
                                                    InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        InjuryAssignAfterTreatment dbReport = new InjuryAssignAfterTreatment();
        BeanUtils.copyProperties(dbReport, assignAfterTreatmentBean);

        int pk = PersistWrapper.createEntity(dbReport);

        //fetch the new project back
        dbReport = PersistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, pk);
        return dbReport;

    }
    public static InjuryAssignAfterTreatment saveAssignAfterTreatment(UserContext context,
                                                                      InjuryAssignAfterTreatment assignAfterTreatment) throws Exception
    {

        int pk = PersistWrapper.createEntity(assignAfterTreatment);

        //fetch the new project back
        assignAfterTreatment = PersistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, pk);
        return assignAfterTreatment;

    }
    public static InjuryAssignAfterTreatment update(UserContext context,
                                                    InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        InjuryAssignAfterTreatment dbReport = PersistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, assignAfterTreatmentBean.getPk());
        BeanUtils.copyProperties(dbReport, assignAfterTreatmentBean);
        PersistWrapper.update(dbReport);
        //fetch the new project back
        dbReport = PersistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, assignAfterTreatmentBean.getPk());
        return dbReport;
    }
    public static List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentList() throws Exception {
        String sql = InjuryAssignAfterTreatmentQuery.sql;
        PersistWrapper p = new PersistWrapper();
        return p.readList(InjuryAssignAfterTreatmentQuery.class,  InjuryAssignAfterTreatmentQuery.sql +InjuryAssignAfterTreatmentQuery.sqlOrder);
    }
    public static List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentByInjuryPk(int assignAfterTreatmentInjuryPk) throws Exception
    {
        return PersistWrapper.readList(InjuryAssignAfterTreatmentQuery.class, InjuryAssignAfterTreatmentQuery.sql + "and injury_assign_after_treatment.injuryPk=? "+InjuryAssignAfterTreatmentQuery.sqlOrder, assignAfterTreatmentInjuryPk);
    }
    public static List<InjuryAssignAfterTreatmentBean> getAssignAfterTreatmentBeanByInjuryPk(int injuryPk) throws Exception
    {
        return PersistWrapper.readList(InjuryAssignAfterTreatmentBean.class,InjuryAssignAfterTreatmentBean.query + " where injuryPk=? ", injuryPk);
    }
    public static InjuryAssignAfterTreatmentBean getAssignAfterTreatmentBean(InjuryOID injuryOID, InjuryAfterTreatmentOID injuryAfterTreatmentOID) throws Exception
    {
        return PersistWrapper.read(InjuryAssignAfterTreatmentBean.class,InjuryAssignAfterTreatmentBean.query + " where injuryPk=? and afterTreatmentMasterPk=? ", injuryOID.getPk(),injuryAfterTreatmentOID.getPk());
    }

    public static void deleteAssignAfterTreatment(int injuryPk,int afterTreatmentMasterPk) throws Exception
    {
        PersistWrapper.delete("delete from injury_assign_after_treatment where injuryPk=? and afterTreatmentMasterPk=?", injuryPk,afterTreatmentMasterPk);
    }
    public static List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentListByInjuryPk(int injuryPk) throws Exception
    {
        String query ="SELECT "
                + " injury_after_treatment_master.pk as afterTreatmentMasterPk,injury_after_treatment_master.name as afterTreatmentMasterName,"
                + " injury_assign_after_treatment.pk as pk,injury_assign_after_treatment.injuryPk as injuryPk "
                + " FROM injury_after_treatment_master "
                + " join injury_assign_after_treatment "
                + " on injury_assign_after_treatment.afterTreatmentMasterPk=injury_after_treatment_master.pk and injury_after_treatment_master.status='Active' ";

        return PersistWrapper.readList(InjuryAssignAfterTreatmentQuery.class,query + " and injury_assign_after_treatment.injuryPk=? "+InjuryAssignAfterTreatmentQuery.sqlOrder, injuryPk);
    }


}

