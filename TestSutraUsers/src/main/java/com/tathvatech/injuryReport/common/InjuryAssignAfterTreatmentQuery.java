package com.tathvatech.injuryReport.common;

import java.io.Serializable;




public class InjuryAssignAfterTreatmentQuery extends BaseResponseBean implements Serializable
{
    private int pk;
    private int injuryPk;
    private int afterTreatmentMasterPk;
    private String afterTreatmentMastername;

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

    public String getAfterTreatmentMastername()
    {
        return afterTreatmentMastername;
    }

    public void setAfterTreatmentMastername(String afterTreatmentMastername)
    {
        this.afterTreatmentMastername = afterTreatmentMastername;
    }

    @Override
    public String toString()
    {
        return getAfterTreatmentMastername();
    }

    public static String sql = "SELECT "
            + " injury_after_treatment_master.pk as afterTreatmentMasterPk,injury_after_treatment_master.name as afterTreatmentMasterName"
            + " ,injury_assign_after_treatment.pk as pk,injury_assign_after_treatment.injuryPk as injuryPk "
            + " FROM injury_after_treatment_master " + " left join injury_assign_after_treatment "
            + " on injury_assign_after_treatment.afterTreatmentMasterPk=injury_after_treatment_master.pk and injury_after_treatment_master.status='Active' ";
    public static String sqlOrder = " order by afterTreatmentMasterPk ";
}
