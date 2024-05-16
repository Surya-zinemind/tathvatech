package com.tathvatech.injuryReport.common;

import java.io.Serializable;
import java.util.Date;



public class WatcherQuery implements Serializable
{
    private int pk;
    private Integer objectPk;
    private String objectType;
    private Integer createdBy;
    private Date createdDate;
    private String status;
    private Date lastUpdated;
    private String name;
    private int userPk;
    private Integer sitePk;
    private String siteName;
    private String displayString;

    public int getUserPk() {
        return userPk;
    }

    public void setUserPk(int userPk) {
        this.userPk = userPk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public Integer getObjectPk() {
        return objectPk;
    }

    public void setObjectPk(Integer objectPk) {
        this.objectPk = objectPk;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static String getSql() {
        return sql;
    }

    public static void setSql(String sql) {
        WatcherQuery.sql = sql;
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




    public String getDisplayString()
    {
        return getName()+" / "+getSiteName();
    }




    public static String sql = "SELECT "
            +" item_watcher.pk,item_watcher.objectPk,item_watcher.objectType,item_watcher.userPk,item_watcher.createdBy,item_watcher.createdDate,item_watcher.status,item_watcher.lastUpdated"
            +",concat(TAB_USER.firstName, ' ', TAB_USER.lastName) as name, TAB_USER.pk as userpk,TAB_USER.sitePk as sitePk,site.name as siteName"
            +" FROM TAB_USER "
            +" LEFT JOIN site ON TAB_USER.sitePk=site.pk"
            +" LEFT JOIN item_watcher ON TAB_USER.pk = item_watcher.userPk ";



    public static String condition =" where 1 = 1  ";
    public static String sqlOrder = " order by name";
}

