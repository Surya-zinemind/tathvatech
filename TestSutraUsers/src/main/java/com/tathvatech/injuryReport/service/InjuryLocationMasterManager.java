package com.tathvatech.injuryReport.service;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.injuryReport.common.InjuryLocationMasterBean;
import com.tathvatech.injuryReport.common.InjuryLocationMasterFilter;
import com.tathvatech.injuryReport.common.InjuryLocationMasterQuery;
import com.tathvatech.injuryReport.entity.InjuryLocationMaster;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
public class InjuryLocationMasterManager
{

    private static final Logger logger = LoggerFactory.getLogger(InjuryLocationMasterManager.class);

    private final PersistWrapper persistWrapper;
    public static int otherPk = 1;


    public  InjuryLocationMaster create(UserContext context, InjuryLocationMasterBean injuryLocationMasterBean)
            throws Exception
    {
        InjuryLocationMaster dbReport = new InjuryLocationMaster();
        if (injuryLocationMasterBean.getPk() > 0)
        {
            dbReport = (InjuryLocationMaster) persistWrapper.readByPrimaryKey(InjuryLocationMaster.class, injuryLocationMasterBean.getPk());
        } else
        {
            dbReport = new InjuryLocationMaster();
            dbReport.setCreatedDate(new Date());
        }
        dbReport.setName(injuryLocationMasterBean.getName());
        dbReport.setParentPk(injuryLocationMasterBean.getParentPk());
        dbReport.setStatus("Active");
        // BeanUtils.copyProperties(dbReport, injuryLocationMasterBean);
        int pk = 0;
        if (injuryLocationMasterBean.getPk() > 0)
        {
            persistWrapper.update(dbReport);
            pk = injuryLocationMasterBean.getPk();
        } else
        {
            pk = (int) persistWrapper.createEntity(dbReport);
        }
        // fetch the new project back
        dbReport = (InjuryLocationMaster) persistWrapper.readByPrimaryKey(InjuryLocationMaster.class, pk);
        return dbReport;

    }

    public  InjuryLocationMaster update(UserContext context, InjuryLocationMasterBean injuryLocationMasterBean)
            throws Exception
    {
        InjuryLocationMaster dbReport = (InjuryLocationMaster) persistWrapper.readByPrimaryKey(InjuryLocationMaster.class,
                injuryLocationMasterBean.getPk());
        BeanUtils.copyProperties(dbReport, injuryLocationMasterBean);
        persistWrapper.update(dbReport);

        dbReport = (InjuryLocationMaster) persistWrapper.readByPrimaryKey(InjuryLocationMaster.class, injuryLocationMasterBean.getPk());
        return dbReport;
    }

    public List<InjuryLocationMasterQuery> getInjuryLocationManagerList() throws Exception
    {
        String sql = InjuryLocationMasterQuery.sql + " and parentPk is null";

        return persistWrapper.readList(InjuryLocationMasterQuery.class, sql);
    }

    public  List<InjuryLocationMasterQuery> getAllList(String search) throws Exception
    {
        String sql = InjuryLocationMasterQuery.sql + " and name like ? order by pk asc ";

        return persistWrapper.readList(InjuryLocationMasterQuery.class, sql,"%"+search+"%");
    }

    public  List<InjuryLocationMasterQuery> getInjuryLocationMasterChildList(int parentId) throws Exception
    {
        String sql = InjuryLocationMasterQuery.sql + " and parentPk =?";

        return persistWrapper.readList(InjuryLocationMasterQuery.class, sql, parentId);
    }

    public  InjuryLocationMasterQuery getInjuryLocationManagerByPk(int pk) throws Exception
    {
        return persistWrapper.read(InjuryLocationMasterQuery.class, InjuryLocationMasterQuery.sql + "and Pk=? ",
                pk);
    }

    public  InjuryLocationMasterQuery getInjuryLocationMasterQueryByPk(int pk) throws Exception
    {
        return persistWrapper.read(InjuryLocationMasterQuery.class, InjuryLocationMasterQuery.sql + "and Pk=? ", pk);
    }

    public  void deleteInjuryLocationMaster(int pk) throws Exception
    {
        persistWrapper.delete("delete from injury_location_master where pk=?", pk);
    }

    public  List<InjuryLocationMasterQuery> getInjuryLocationMaster(InjuryLocationMasterFilter filter)
            throws Exception
    {
        List<InjuryLocationMasterQuery> othersList = null;

        try
        {
            List<Object> params = new ArrayList<Object>();

            StringBuffer qr = new StringBuffer(
                    "SELECT * FROM injury_location_master where 1 = 1 and parentPk =" + otherPk + "");

            if (filter.getSearchString() != null && filter.getSearchString().trim().length() > 0)
            {
                qr.append(" and injury_location_master.name like ?");
                params.add("%" + filter.getSearchString().trim() + "%");
            }

            if (filter.getFromDate() != null)
            {
                qr.append(" and injury_location_master.createdDate >= ?");
                params.add(DateUtils.getBeginningDay(filter.getFromDate()));
            }
            if (filter.getToDate() != null)
            {
                qr.append(" and injury_location_master. createdDate < ?");
                params.add(DateUtils.getEndOfDay(filter.getToDate()));
            }
            qr.append(" group by injury_location_master.pk ");
            qr.append(" order by createdDate DESC");
            othersList = persistWrapper.readList(InjuryLocationMasterQuery.class, qr.toString(), params.toArray());

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        return othersList;
    }

}

