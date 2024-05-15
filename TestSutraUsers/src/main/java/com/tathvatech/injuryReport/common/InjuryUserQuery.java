package com.tathvatech.injuryReport.common;

import java.io.Serializable;
import java.util.Date;

import net.sf.persist.annotations.NoTable;

@NoTable
public class InjuryUserQuery implements Serializable {
    private int pk;
    private String name;
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public static String sql_TAB_USER = "SELECT concat(firstname,\" \",lastName) as name,pk as pk,userType as userType  FROM TAB_USER where 1=1 ";
    public static String sql_union = " union ";
    public static String sql_injury_injuryperson = " SELECT injuredPerson as name,0 as pk,'' as userType FROM injury  where 1=1 ";
    public static String sql_injury_treatedBy = " SELECT treatedBy as name,0 as pk,'' as userType FROM injury  where 1=1 ";
    public static String sql_order = " ORDER BY name ASC";
    // public static String sql_supervisor = "SELECT concat(firstname,\"
    // \",lastName) as name,pk FROM TAB_USER where email IS NOT NULL" ;

}
