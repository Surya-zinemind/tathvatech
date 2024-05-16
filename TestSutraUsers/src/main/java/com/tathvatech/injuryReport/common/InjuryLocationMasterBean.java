package com.tathvatech.injuryReport.common;

import java.io.Serializable;
import java.util.Date;


public class InjuryLocationMasterBean extends BaseResponseBean implements Serializable {
    private int pk;
    private String name;
    private String status;
    private Integer parentPk;
    private Date createdDate;
    private Date lastUpdated;

    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public Integer getParentPk()
    {
        return parentPk;
    }
    public void setParentPk(Integer parentPk)
    {
        this.parentPk = parentPk;
    }
    @Override
    public int hashCode() {

        if (pk != 0) {
            return pk;
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (pk == 0)
            return super.equals(obj);
        return pk == ((InjuryLocationMasterBean) obj).getPk();
    }

}

