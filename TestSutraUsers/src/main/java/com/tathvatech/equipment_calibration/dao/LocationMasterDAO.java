package com.tathvatech.equipment_calibration.dao;

import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.equipment_calibration.entity.LocationMaster;
import com.tathvatech.equipment_calibration.oid.LocationOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.utils.DateUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Repository
public class LocationMasterDAO {
    private final PersistWrapper persistWrapper;
    Date now;

    public static enum DATALIST {
        PARENT, CHILD, ALL
    }

    public LocationMasterDAO(PersistWrapper persistWrapper)
    {
        this.persistWrapper = persistWrapper;
        now = DateUtils.getNowDateForEffectiveDateFrom();
    }

    public LocationMaster get(int pk)
    {
        return (LocationMaster) persistWrapper.readByPrimaryKey(LocationMaster.class, pk);
    }

    public LocationMaster save(UserContext context, LocationMaster obj) throws Exception
    {

        LocationMaster locationMaster = null;
        if (obj.getPk() > 0)
        {
            locationMaster = (LocationMaster) persistWrapper.readByPrimaryKey(LocationMaster.class, obj.getPk());
            if (locationMaster.getLastUpdated().after(obj.getLastUpdated()))
            {
                throw new AppException("Another user has updated the details, Please reload and try again.");
            }

        } else
        {
            locationMaster = new LocationMaster();
            locationMaster.setCreatedDate(now);
            locationMaster.setCreatedBy((int) context.getUser().getPk());
            locationMaster.setEstatus(EStatusEnum.Active.getValue());
        }
        locationMaster.setLocationTypeFk(obj.getLocationTypeFk());
        locationMaster.setDescription(obj.getDescription());
        locationMaster.setLastUpdated(now);
        locationMaster.setName(obj.getName());
        locationMaster.setParentPk(obj.getParentPk());
        locationMaster.setSiteFk(obj.getSiteFk());
        int pk;
        if (obj.getPk() > 0)
        {
            pk = (int) locationMaster.getPk();
            persistWrapper.update( locationMaster);
        } else
        {
            pk = (int) persistWrapper.createEntity(locationMaster);
            locationMaster = (LocationMaster) persistWrapper.readByPrimaryKey(LocationMaster.class, pk);
        }
        return get(pk);
    }

    public List<LocationMaster> gelocations(List<SiteOID> siteOIDs, List<LocationOID> parentPks, DATALIST dataList,
                                            String name)
    {
        StringBuffer sql = new StringBuffer(fetchSql);
        List<Object> params = new ArrayList<Object>();

        if (siteOIDs != null && siteOIDs.size() > 0)
        {
            sql.append(" and location_master.siteFk in (");
            String ssep = "";
            for (SiteOID aSite : siteOIDs)
            {
                sql.append(ssep).append("?");
                ssep = ",";
                params.add(aSite.getPk());
            }
            sql.append(") ");
        }
        if (parentPks != null && parentPks.size() > 0)
        {
            sql.append(" and location_master.parentPk in (");
            String ssep = "";
            for (LocationOID aParent : parentPks)
            {
                sql.append(ssep).append("?");
                ssep = ",";
                params.add(aParent.getPk());
            }
            sql.append(") ");
        }
        if (DATALIST.PARENT.equals(dataList))
        {
            sql.append(" and location_master.parentPk is null ");
        } else if (DATALIST.CHILD.equals(dataList))
        {
            sql.append(" and location_master.parentPk is not null ");
        }
        sql.append(" and location_master.estatus=? ");
        params.add(EStatusEnum.Active.getValue());

        if (name != null && name.length() > 0)
        {
            sql.append(" and location_master.name like '%?%' ");
            params.add(name);
        }

        sql.append(" order by site.name asc,location_type.name asc, cast(location_master.name  AS unsigned) asc");

        return persistWrapper.readList(LocationMaster.class, sql.toString(),
                (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);
    }

    public List<LocationMaster> gelocations(String name)
    {
        if (name != null && name.length() > 0)
        {
            try {
                return persistWrapper.readList(LocationMaster.class, fetchSql
                                + " and location_master.estatus=? and location_master.name like '%?%' order by site.name asc,location_type.name asc, cast(location_master.name  AS unsigned) asc ",
                        EStatusEnum.Active.getValue(), name);
            }catch (Exception e) {

            }
        } else
        {
            return persistWrapper.readList(LocationMaster.class, fetchSql
                            + " and location_master.estatus=?  order by site.name asc,location_type.name asc, cast(location_master.name  AS unsigned) asc ",
                    EStatusEnum.Active.getValue());
        }

        return null;
    }

    public List<LocationMaster> gelocationsofType(int locationTypeFk)
    {
        return persistWrapper.readList(LocationMaster.class, fetchSql
                        + " and location_master.estatus=? and location_master.locationTypeFk=?   order by site.name asc,location_type.name asc, cast(location_master.name  AS unsigned) asc ",
                EStatusEnum.Active.getValue(), locationTypeFk);
    }

    public void delete(UserContext context, int pk) throws Exception {
        LocationMaster location = get(pk);
        if (location != null)
        {
            location.setEstatus(EStatusEnum.Deleted.getValue());
            persistWrapper.update(location);
        }
    }

    public static final String fetchSql = " SELECT `location_master`.`pk`,`location_master`.`siteFk`,`location_master`.`locationTypeFk`,`location_master`.`parentPk`,`location_master`.`name`,`location_master`.`description`,`location_master`.`estatus`,`location_master`.`createdBy`,`location_master`.`createdDate`,`location_master`.`lastUpdated` FROM `location_master` inner join site on site.pk=location_master.siteFk inner join location_type on location_type.pk=location_master.locationTypeFk  where 1 = 1 ";

}
