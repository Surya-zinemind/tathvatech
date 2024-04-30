package com.tathvatech.ncr.entity;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.enums.DBEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "ncr_disposition_master")
@Entity
public class NcrDispositionTypes extends DBEnum   {
    @Id
    private long pk;
    private String name;
    private String description;
    private int estatus;
    private Date createdDate;
    private int createdBy;
    private Date lastUpdated;




    @Override
    public long getPk() {
        return pk;
    }

    @Override
    public void setingPk(int val) {

    }



    public void setPk(long pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }


    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
