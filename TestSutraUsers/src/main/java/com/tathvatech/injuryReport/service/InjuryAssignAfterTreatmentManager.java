package com.tathvatech.injuryReport.service;


import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.injuryReport.common.InjuryAssignAfterTreatmentBean;
import com.tathvatech.injuryReport.common.InjuryAssignAfterTreatmentQuery;
import com.tathvatech.injuryReport.entity.InjuryAssignAfterTreatment;
import com.tathvatech.injuryReport.oid.InjuryOID;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import com.tathvatech.user.OID.InjuryAfterTreatmentOID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;

@RequiredArgsConstructor
public class InjuryAssignAfterTreatmentManager {
    private final PersistWrapper persistWrapper;
    private static final Logger logger = LoggerFactory.getLogger(InjuryAssignAfterTreatmentManager.class);
    public  InjuryAssignAfterTreatment create(UserContext context,
                                                    InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        InjuryAssignAfterTreatment dbReport = new InjuryAssignAfterTreatment();
        BeanUtils.copyProperties(dbReport, assignAfterTreatmentBean);

        int pk = (int) persistWrapper.createEntity(dbReport);

        //fetch the new project back
        dbReport = (InjuryAssignAfterTreatment) persistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, pk);
        return dbReport;

    }
    public  InjuryAssignAfterTreatment saveAssignAfterTreatment(UserContext context,
                                                                      InjuryAssignAfterTreatment assignAfterTreatment) throws Exception
    {

        int pk = (int) persistWrapper.createEntity(assignAfterTreatment);

        //fetch the new project back
        assignAfterTreatment = (InjuryAssignAfterTreatment) persistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, pk);
        return assignAfterTreatment;

    }
    public  InjuryAssignAfterTreatment update(UserContext context,
                                                    InjuryAssignAfterTreatmentBean assignAfterTreatmentBean) throws Exception
    {
        InjuryAssignAfterTreatment dbReport = (InjuryAssignAfterTreatment) persistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, assignAfterTreatmentBean.getPk());
        BeanUtils.copyProperties(dbReport, assignAfterTreatmentBean);
        persistWrapper.update(dbReport);
        //fetch the new project back
        dbReport = (InjuryAssignAfterTreatment) persistWrapper.readByPrimaryKey(InjuryAssignAfterTreatment.class, assignAfterTreatmentBean.getPk());
        return dbReport;
    }
    public List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentList() throws Exception {
        String sql = InjuryAssignAfterTreatmentQuery.sql;

        return persistWrapper.readList(InjuryAssignAfterTreatmentQuery.class,  InjuryAssignAfterTreatmentQuery.sql +InjuryAssignAfterTreatmentQuery.sqlOrder);
    }
    public  List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentByInjuryPk(int assignAfterTreatmentInjuryPk) throws Exception
    {
        return persistWrapper.readList(InjuryAssignAfterTreatmentQuery.class, InjuryAssignAfterTreatmentQuery.sql + "and injury_assign_after_treatment.injuryPk=? "+InjuryAssignAfterTreatmentQuery.sqlOrder, assignAfterTreatmentInjuryPk);
    }
    public  List<InjuryAssignAfterTreatmentBean> getAssignAfterTreatmentBeanByInjuryPk(int injuryPk) throws Exception
    {
        return persistWrapper.readList(InjuryAssignAfterTreatmentBean.class,InjuryAssignAfterTreatmentBean.query + " where injuryPk=? ", injuryPk);
    }
    public  InjuryAssignAfterTreatmentBean getAssignAfterTreatmentBean(InjuryOID injuryOID, InjuryAfterTreatmentOID injuryAfterTreatmentOID) throws Exception
    {
        return persistWrapper.read(InjuryAssignAfterTreatmentBean.class,InjuryAssignAfterTreatmentBean.query + " where injuryPk=? and afterTreatmentMasterPk=? ", injuryOID.getPk(),injuryAfterTreatmentOID.getPk());
    }

    public  void deleteAssignAfterTreatment(int injuryPk,int afterTreatmentMasterPk) throws Exception
    {
        persistWrapper.delete("delete from injury_assign_after_treatment where injuryPk=? and afterTreatmentMasterPk=?", injuryPk,afterTreatmentMasterPk);
    }
    public  List<InjuryAssignAfterTreatmentQuery> getAssignAfterTreatmentListByInjuryPk(int injuryPk) throws Exception
    {
        String query ="SELECT "
                + " injury_after_treatment_master.pk as afterTreatmentMasterPk,injury_after_treatment_master.name as afterTreatmentMasterName,"
                + " injury_assign_after_treatment.pk as pk,injury_assign_after_treatment.injuryPk as injuryPk "
                + " FROM injury_after_treatment_master "
                + " join injury_assign_after_treatment "
                + " on injury_assign_after_treatment.afterTreatmentMasterPk=injury_after_treatment_master.pk and injury_after_treatment_master.status='Active' ";

        return persistWrapper.readList(InjuryAssignAfterTreatmentQuery.class,query + " and injury_assign_after_treatment.injuryPk=? "+InjuryAssignAfterTreatmentQuery.sqlOrder, injuryPk);
    }


}

