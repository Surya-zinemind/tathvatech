package com.tathvatech.user.report;

import com.tathvatech.report.request.ReportFilter;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.user.entity.User;

public class UserFilter extends ReportFilter
{
    private int pk;
    private String searchString; // this is usually a user entered string, match this to either firstname or last name or userName in case of User
    private int[] sitePks;
    private String[] status;
    private String[] userType;
    private boolean usersWithEmailOnly = false;
    private String sortColumn;
    private ReportRequest.SortOrder sortOrder;

    public UserFilter()
    {
        this.status = new String[]{User.STATUS_ACTIVE};
    }

    public int getPk()
    {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
    public String getSearchString() {
        return searchString;
    }
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    public int[] getSitePks() {
        return sitePks;
    }
    public void setSitePks(int[] sitePks) {
        this.sitePks = sitePks;
    }
    public String[] getStatus() {
        return status;
    }
    public void setStatus(String[] status) {
        this.status = status;
    }
    public String[] getUserType() {
        return userType;
    }
    public void setUserType(String[] userType) {
        this.userType = userType;
    }
    public boolean getUsersWithEmailOnly()
    {
        return usersWithEmailOnly;
    }

    public void setUsersWithEmailOnly(boolean usersWithEmailOnly)
    {
        this.usersWithEmailOnly = usersWithEmailOnly;
    }

    public String getSortColumn()
    {
        return sortColumn;
    }
    public void setSortColumn(String sortColumn)
    {
        this.sortColumn = sortColumn;
    }
    public ReportRequest.SortOrder getSortOrder()
    {
        return sortOrder;
    }
    public void setSortOrder(ReportRequest.SortOrder sortOrder)
    {
        this.sortOrder = sortOrder;
    }
}

