package com.tathvatech.unit.dao;

import java.util.Date;


import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.user.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UnitDAO
{
	Date now;

	@Autowired
	private  PersistWrapper persistWrapper;
	public UnitDAO()
	{

        now = DateUtils.getNowDateForEffectiveDateFrom();
	}

	public UnitObj getUnit(long unitPk)
	{
		return persistWrapper.read(UnitObj.class, fetchSql + " and u.pk = ?", unitPk);
	}

	public UnitObj getUnitByName(String unitName)
	{
		// there are few duplicate units in the system now. need to clean up that . so the limit 0, 1
		return persistWrapper.read(UnitObj.class, fetchSql + " and uh.unitName = ? limit 0, 1 ", unitName);
	}
	//Fix this when working on Unit
	/*public  UnitObj saveUnit(UserContext context, UnitObj obj, Actions[]  actions) throws Exception
	{
		Unit u = null;

		if (obj.getPk() <= 0)
		{
			u = new Unit();
			u.setEstatus(obj.getEstatus());
			u.setCreatedBy(obj.getCreatedBy());
			u.setCreatedDate(obj.getCreatedDate());
			u.setPartPk(obj.getPartPk());
			u.setPartRevisionPk(obj.getPartRevisionPk());
			u.setSupplierFk(obj.getSupplierFk());
			u.setSiteGroupFk(obj.getSiteGroupFk());
			int pk = PersistWrapper.createEntity(u);
			u.setPk(pk);
			

			
			EntityActions act = null;
			if(actions != null)
				act = EntityActions.createAction(context, new UnitOID(pk), actions);
			
			UnitH uHNew = new UnitH();
			if(act != null)
				uHNew.setActionPk(act.getPk());
			uHNew.setCreatedBy(context.getUser().getPk());
			uHNew.setCreatedDate(new Date());
			uHNew.setEffectiveDateFrom(now);
			uHNew.setEffectiveDateTo(DateUtils.getMaxDate());
			uHNew.setSerialNo(obj.getSerialNo());
			uHNew.setStatus(obj.getStatus());
			uHNew.setUnitDescription(obj.getUnitDescription());
			uHNew.setUnitName(obj.getUnitName());
			uHNew.setDisplayName(obj.getDisplayName());
			uHNew.setUnitPk(u.getPk());
			PersistWrapper.createEntity(uHNew);
		} 
		else
		{
			u = PersistWrapper.readByPrimaryKey(Unit.class, obj.getPk());
			UnitH uHCurrent = PersistWrapper.read(UnitH.class, 
					"select * from TAB_UNIT_H where unitPk = ? and now() between effectiveDateFrom and effectiveDateTo", obj.getPk());

			if(uHCurrent.getEffectiveDateFrom().equals(now))
			{
				// we have to do an update of the h record here.. since it is from the same DAO context as the now date is the same.
				uHCurrent.setEffectiveDateFrom(now);
				uHCurrent.setEffectiveDateTo(DateUtils.getMaxDate());
				uHCurrent.setSerialNo(obj.getSerialNo());
				uHCurrent.setStatus(obj.getStatus());
				uHCurrent.setUnitDescription(obj.getUnitDescription());
				uHCurrent.setUnitName(obj.getUnitName());
				uHCurrent.setDisplayName(obj.getDisplayName());
				uHCurrent.setUnitPk(u.getPk());
				PersistWrapper.update(uHCurrent);
			}
			else
			{
				uHCurrent.setEffectiveDateTo(new Date(now.getTime() - 1000));
				PersistWrapper.update(uHCurrent);
				

				
				EntityActions act = null;
				if(actions != null)
					act = EntityActions.createAction(context, new UnitOID(obj.getPk()), actions);
				
				UnitH uHNew = new UnitH();
				if(act != null)
					uHNew.setActionPk(act.getPk());
				uHNew.setCreatedBy(context.getUser().getPk());
				uHNew.setCreatedDate(new Date());
				uHNew.setEffectiveDateFrom(now);
				uHNew.setEffectiveDateTo(DateUtils.getMaxDate());
				uHNew.setSerialNo(obj.getSerialNo());
				uHNew.setStatus(obj.getStatus());
				uHNew.setUnitDescription(obj.getUnitDescription());
				uHNew.setUnitName(obj.getUnitName());
				uHNew.setDisplayName(obj.getDisplayName());
				uHNew.setUnitPk(u.getPk());
				PersistWrapper.createEntity(uHNew);
			}
		}
		return getUnit(u.getPk());
	}*/

	public static String fetchSql = "select u.pk, u.partPk, u.partRevisionPk, u.supplierFk, u.siteGroupFk, u.createdBy, u.createdDate, u.lastUpdated, "
			+ " uh.serialNo, uh.unitName, uh.displayName, uh.unitDescription, uh.status " + " from TAB_UNIT u "
			+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
			+ " where 1 = 1 ";

}
