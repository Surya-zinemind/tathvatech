package com.tathvatech.injuryReport.common;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sarvasutra.etest.api.BaseResponseBean;

import net.sf.persist.annotations.NoTable;

@NoTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatcherBean extends BaseResponseBean implements Serializable
{
    private int pk;
    private Integer objectPk;
    private String objectType;
    private int userPk;
    private Integer createdBy;
    private Date createdDate;
    private String status;
    private Date lastUpdated;
    private String name;
    private Integer sitePk;
    private String siteName;
    public int getUserPk()
    {
        return userPk;
    }

    public void setUserPk(int userPk)
    {
        this.userPk = userPk;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public Integer getObjectPk()
    {
        return objectPk;
    }

    public void setObjectPk(Integer objectPk)
    {
        this.objectPk = objectPk;
    }

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType(String objectType)
    {
        this.objectType = objectType;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public Integer getSitePk()
    {
        return sitePk;
    }

    public void setSitePk(Integer sitePk)
    {
        this.sitePk = sitePk;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public static WatcherBean getInjuryBean(WatcherQuery watcherQuery)
    {
        WatcherBean wbean = new WatcherBean();
        wbean.setPk(watcherQuery.getPk());
        wbean.setObjectPk(watcherQuery.getObjectPk());
        wbean.setObjectType(watcherQuery.getObjectType());
        wbean.setUserPk(watcherQuery.getUserPk());
        wbean.setCreatedBy(watcherQuery.getCreatedBy());
        wbean.setCreatedDate(watcherQuery.getCreatedDate());
        wbean.setStatus(watcherQuery.getStatus());
        wbean.setLastUpdated(watcherQuery.getLastUpdated());
        wbean.setName(watcherQuery.getName());
        wbean.setSitePk(watcherQuery.getSitePk());
        wbean.setSiteName(watcherQuery.getSiteName());
        return wbean;
    }

    public static String sql = "SELECT "
            + " item_watcher.pk,item_watcher.objectPk,item_watcher.objectType,item_watcher.userPk,item_watcher.createdBy,item_watcher.createdDate,item_watcher.status,item_watcher.lastUpdated"
            + ",concat(TAB_USER.firstName, ' ', TAB_USER.lastName) as name, TAB_USER.pk as userpk"
            +", site.pk as sitePk"
            +", site.name as siteName "
            + " FROM TAB_USER "
            + " INNER JOIN item_watcher ON TAB_USER.pk = item_watcher.userPk "
            + " INNER JOIN site on site.pk=TAB_USER.sitePk ";

    public static String condition = " where 1 = 1 ";
    public static String sqlOrder = " order by name";

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        return (pk == ((WatcherBean) obj).getPk());
    }

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
