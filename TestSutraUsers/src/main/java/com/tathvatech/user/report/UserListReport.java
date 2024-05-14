package com.tathvatech.user.report;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.report.response.ReportResponse;
import com.tathvatech.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserListReport
{
    private final PersistWrapper persistWrapper;
    public UserListReport(PersistWrapper persistWrapper)
    {

        this.persistWrapper = persistWrapper;
    }

    public ReportResponse runReport(ReportRequest reportRequest)
    {
        ReportResponse response = new ReportResponse();
        response.setStartIndex(reportRequest.getStartIndex());
        Object[] baseQueryBuild = buildBaseQuery((UserFilter) reportRequest.getFilter());
        StringBuffer sql = (StringBuffer) baseQueryBuild[0];
        List<Object> params = (List<Object>) baseQueryBuild[1];
        if(reportRequest.isFetchRowCount())
        {
            //we need to find the count if the request is newRequest.
            StringBuffer countQB = new StringBuffer("select count(*) from ( ").append(sql).append(" )data");
            long totalRowCount = persistWrapper.read(Long.class, countQB.toString(), (params.size() >0)?params.toArray(new Object[params.size()]):null);
            response.setTotalRows(totalRowCount);
        }

        sql.append(" order by user.firstName ");
        sql.append(" limit ").append(reportRequest.getStartIndex()).append(", ").append(reportRequest.getRowsToFetch());


        List<User> data = persistWrapper.readList(User.class, sql.toString(), (params.size() >0)?params.toArray(new Object[params.size()]):null);

        response.setReportData(data);
        return response;
    }


    private Object[] buildBaseQuery(UserFilter userFilter)
    {
        String[] userTypes = null;
        if(userFilter.getUserType() == null)
        {
            userTypes = new String[] {User.USER_ENGINEER, User.USER_GUEST, User.USER_MANAGER, User.USER_TECHNICIAN};
        }

        StringBuffer sql = new StringBuffer("select user.pk, user.accountPk, user.sitePk, user.userType, "
                + " user.status, user.createDate, user.userName, user.title, user.firstName, user.middleName, user.lastName, "
                + " user.email, user.timeZone from TAB_USER user where 1 = 1");

        List<Object> params = new ArrayList();

        if(userFilter.getSitePks() != null && userFilter.getSitePks().length > 0)
        {
            sql.append(" and user.sitePk in (");
            String sep = "";
            for (int i = 0; i < userFilter.getSitePks().length; i++) {
                sql.append(sep);
                sql.append(userFilter.getSitePks()[i]);
                sep = ",";
            }
            sql.append(")");
        }
        if(userFilter.getPk() != 0)
        {
            sql.append(" and user.pk=?");
            params.add(userFilter.getPk());
        }
        else
        {
            if(userFilter.getSearchString() != null && userFilter.getSearchString().trim().length() > 0)
            {
                sql.append(" and (upper(user.userName) like ? or upper(user.firstName) like ? or upper(user.lastName) like ?)");
                params.add("%"+userFilter.getSearchString().toUpperCase()+"%");
                params.add("%"+userFilter.getSearchString().toUpperCase()+"%");
                params.add("%"+userFilter.getSearchString().toUpperCase()+"%");
            }
            if(userFilter.getStatus() != null && userFilter.getStatus().length > 0)
            {
                sql.append(" and user.status in (");
                String sep = "";
                for (int i = 0; i < userFilter.getStatus().length; i++) {
                    sql.append(sep);
                    sql.append("'").append(userFilter.getStatus()[i]).append("'");
                    sep = ",";
                }
                sql.append(")");
            }

            if(userTypes != null && userTypes.length > 0)
            {
                sql.append(" and user.userType in (");
                String sep = "";
                for (int i = 0; i < userTypes.length; i++) {
                    sql.append(sep);
                    sql.append("'").append(userTypes[i]).append("'");
                    sep = ",";
                }
                sql.append(")");
            }
        }

        return new Object[]{sql, params};
    }
}

