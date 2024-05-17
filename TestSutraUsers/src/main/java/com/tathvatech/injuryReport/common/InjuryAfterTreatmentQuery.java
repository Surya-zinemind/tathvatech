package com.tathvatech.injuryReport.common;

import com.tathvatech.user.OID.InjuryAfterTreatmentOID;
import com.tathvatech.user.OID.TSBeanBase;
import java.io.Serializable;
import java.util.Date;



public class InjuryAfterTreatmentQuery extends TSBeanBase implements Serializable
{
    private int pk;
    private String name;
    private String status;
    private Integer createdBy;
    private Date createdDate;

    public long getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public InjuryAfterTreatmentOID getOID()
    {
        return new InjuryAfterTreatmentOID((int) getPk(), getName());
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
        return (((InjuryAfterTreatmentQuery) obj).getPk() == getPk());
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public String getDisplayText()
    {
        return getName();
    }
}
