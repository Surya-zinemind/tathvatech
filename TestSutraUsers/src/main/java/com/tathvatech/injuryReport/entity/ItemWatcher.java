package com.tathvatech.injuryReport.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.common.enums.VersionableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;




@Entity
@Table(name="item_watcher")
public class ItemWatcher extends AbstractEntity implements Serializable
{
    @Id
    private long pk;
    private int objectPk;
    private int objectType;
    private int userPk;
    private Integer createdBy;
    private Date createdDate;
    private String status;
    private Date lastUpdated;

    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }


    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public int getObjectPk() {
        return objectPk;
    }

    public void setObjectPk(int objectPk) {
        this.objectPk = objectPk;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public int getUserPk() {
        return userPk;
    }

    public void setUserPk(int userPk) {
        this.userPk = userPk;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

