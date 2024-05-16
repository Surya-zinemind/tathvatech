package com.tathvatech.injuryReport.common;

import java.io.Serializable;
import java.util.Date;



public class InjuryAssignAfterTreatmentBean extends BaseResponseBean implements Serializable
{
    private int pk;
    private int injuryPk;
    private int afterTreatmentMasterPk;
    private Date lastUpdated;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public int getInjuryPk()
    {
        return injuryPk;
    }

    public void setInjuryPk(int injuryPk)
    {
        this.injuryPk = injuryPk;
    }

    public int getAfterTreatmentMasterPk()
    {
        return afterTreatmentMasterPk;
    }

    public void setAfterTreatmentMasterPk(int afterTreatmentMasterPk)
    {
        this.afterTreatmentMasterPk = afterTreatmentMasterPk;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public static String query = "SELECT * FROM injury_assign_after_treatment ";

    public static InjuryAssignAfterTreatmentBean getAssignAfterTreatmentBean(
            InjuryAssignAfterTreatmentQuery assignAfterTreatmentQuery)
    {
        InjuryAssignAfterTreatmentBean assignAfterTreatmentbeam = new InjuryAssignAfterTreatmentBean();
        assignAfterTreatmentbeam.setPk(assignAfterTreatmentQuery.getPk());
        assignAfterTreatmentbeam.setInjuryPk(assignAfterTreatmentQuery.getInjuryPk());
        assignAfterTreatmentbeam.setAfterTreatmentMasterPk(assignAfterTreatmentQuery.getAfterTreatmentMasterPk());
        return assignAfterTreatmentbeam;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        return (((InjuryAssignAfterTreatmentBean) obj).getPk() == getPk());
    }

}

