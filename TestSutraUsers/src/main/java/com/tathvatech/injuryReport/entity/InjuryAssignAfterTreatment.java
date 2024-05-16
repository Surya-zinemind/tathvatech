package com.tathvatech.injuryReport.entity;

import java.io.Serializable;
import java.util.Date;
import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="injury_assign_after_treatment")
public class InjuryAssignAfterTreatment extends AbstractEntity implements Serializable {
    @Id
    private long pk;
    private int injuryPk;
    private int afterTreatmentMasterPk;
    private Date lastUpdated;

    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public int getInjuryPk() {
        return injuryPk;
    }
    public void setInjuryPk(int injuryPk) {
        this.injuryPk = injuryPk;
    }
    public int getAfterTreatmentMasterPk() {
        return afterTreatmentMasterPk;
    }
    public void setAfterTreatmentMasterPk(int afterTreatmentMasterPk) {
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

}

