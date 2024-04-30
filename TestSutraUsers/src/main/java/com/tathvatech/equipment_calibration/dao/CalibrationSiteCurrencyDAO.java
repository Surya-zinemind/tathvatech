package com.tathvatech.equipment_calibration.dao;
import java.util.ArrayList;
import java.util.List;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.equipment_calibration.entity.CalibrationSiteCurrency;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.utils.DateUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class CalibrationSiteCurrencyDAO {
    Date now;
    private  CalibrationSiteCurrency calibrationSiteCurrency;
    private final PersistWrapper persistWrapper;
    public CalibrationSiteCurrencyDAO( PersistWrapper persistWrapper)
    {

        this.persistWrapper = persistWrapper;
        now = DateUtils.getNowDateForEffectiveDateFrom();
    }

    public CalibrationSiteCurrency get(int pk)
    {
        return (CalibrationSiteCurrency) persistWrapper.readByPrimaryKey(CalibrationSiteCurrency.class, pk);
    }

    public CalibrationSiteCurrency save(UserContext context, CalibrationSiteCurrency obj) throws Exception
    {


        if (obj.getPk() > 0) {
            if (calibrationSiteCurrency != null) {
                calibrationSiteCurrency = (CalibrationSiteCurrency) persistWrapper.readByPrimaryKey(CalibrationSiteCurrency.class, obj.getPk());
                if (calibrationSiteCurrency.getLastUpdated().after(obj.getLastUpdated())) {
                    throw new AppException("Another user has updated the details, Please reload and try again.");
                }

            } else {
                calibrationSiteCurrency = new CalibrationSiteCurrency();
                calibrationSiteCurrency.setCreatedDate(now);
                calibrationSiteCurrency.setCreatedBy((int) context.getUser().getPk());
                calibrationSiteCurrency.setEstatus(EStatusEnum.Active.getValue());
            }
            calibrationSiteCurrency.setSiteFk(obj.getSiteFk());
            calibrationSiteCurrency.setCurrency(obj.getCurrency());
            calibrationSiteCurrency.setAbbreviation(obj.getAbbreviation());
            calibrationSiteCurrency.setLastUpdated(now);
            int pk;
            if (obj.getPk() > 0) {
                pk = (int) calibrationSiteCurrency.getPk();
                persistWrapper.update(calibrationSiteCurrency);
            } else {
                pk = (int) persistWrapper.createEntity(calibrationSiteCurrency);
            }
            return get(pk);
        }
        return obj;
    }

    public List<CalibrationSiteCurrency> geCurrencies(List<SiteOID> siteOIDs)
    {
        StringBuffer sql = new StringBuffer(fetchSql);
        List<Object> params = new ArrayList<Object>();

        if (siteOIDs != null && siteOIDs.size() > 0)
        {
            sql.append(" and calibration_site_currency_ref.siteFk in (");
            String ssep = "";
            for (SiteOID aSite : siteOIDs)
            {
                sql.append(ssep).append("?");
                ssep = ",";
                params.add(aSite.getPk());
            }
            sql.append(") ");
        }
        sql.append(" and calibration_site_currency_ref.estatus=? ");
        params.add(EStatusEnum.Active.getValue());
        sql.append(" order by site.name asc ");

        return persistWrapper.readList(CalibrationSiteCurrency.class, sql.toString(),
                (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);
    }

    public CalibrationSiteCurrency getCurrency(SiteOID siteOID)
    {
        return persistWrapper.read(CalibrationSiteCurrency.class,
                fetchSql + " and calibration_site_currency_ref.siteFk=? and calibration_site_currency_ref.estatus=? ",
                siteOID.getPk(), EStatusEnum.Active.getValue());
    }

    public void delete(UserContext context, int pk) throws Exception {
        CalibrationSiteCurrency location = get(pk);
        if (location != null)
        {
            location.setEstatus(EStatusEnum.Deleted.getValue());
            persistWrapper.update( location);
        }
    }

    public static final String fetchSql = " SELECT `calibration_site_currency_ref`.`pk`,`calibration_site_currency_ref`.`siteFk`,`calibration_site_currency_ref`.`currency`,`calibration_site_currency_ref`.`abbreviation`,`calibration_site_currency_ref`.`estatus`,`calibration_site_currency_ref`.`createdBy`,`calibration_site_currency_ref`.`createdDate`,`calibration_site_currency_ref`.`lastUpdated` FROM `calibration_site_currency_ref` inner join site on site.pk=calibration_site_currency_ref.siteFk where 1 = 1 ";

}
