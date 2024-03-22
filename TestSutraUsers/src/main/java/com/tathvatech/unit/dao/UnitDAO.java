package com.tathvatech.unit.dao;

import java.util.Date;


import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.entity.EntityActions;
import com.tathvatech.unit.entity.Unit;
import com.tathvatech.unit.entity.UnitH;
import com.tathvatech.unit.enums.Actions;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.UserContext;
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
	public  UnitObj saveUnit(UserContext context, UnitObj obj, Actions[]  actions) throws Exception
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
			int pk = (int) persistWrapper.createEntity(u);
			u.setPk(pk);
			

			
			EntityActions act = null;
			if(actions != null)
				act = createAction(context, new UnitOID(pk), actions);
			
			UnitH uHNew = new UnitH();
			if(act != null)
				uHNew.setActionPk((int) act.getPk());
			uHNew.setCreatedBy((int) context.getUser().getPk());
			uHNew.setCreatedDate(new Date());
			uHNew.setEffectiveDateFrom(now);
			uHNew.setEffectiveDateTo(DateUtils.getMaxDate());
			uHNew.setSerialNo(obj.getSerialNo());
			uHNew.setStatus(obj.getStatus());
			uHNew.setUnitDescription(obj.getUnitDescription());
			uHNew.setUnitName(obj.getUnitName());
			uHNew.setDisplayName(obj.getDisplayName());
			uHNew.setUnitPk((int) u.getPk());
			persistWrapper.createEntity(uHNew);
		} 
		else
		{
			u = (Unit) persistWrapper.readByPrimaryKey(Unit.class, obj.getPk());
			UnitH uHCurrent = persistWrapper.read(UnitH.class, 
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
				uHCurrent.setUnitPk((int) u.getPk());
				persistWrapper.update(uHCurrent);
			}
			else
			{
				uHCurrent.setEffectiveDateTo(new Date(now.getTime() - 1000));
				persistWrapper.update(uHCurrent);
				

				
				EntityActions act = null;
				if(actions != null)
					act = createAction(context, new UnitOID((int) obj.getPk()), actions);
				
				UnitH uHNew = new UnitH();
				if(act != null)
					uHNew.setActionPk((int) act.getPk());
				uHNew.setCreatedBy((int) context.getUser().getPk());
				uHNew.setCreatedDate(new Date());
				uHNew.setEffectiveDateFrom(now);
				uHNew.setEffectiveDateTo(DateUtils.getMaxDate());
				uHNew.setSerialNo(obj.getSerialNo());
				uHNew.setStatus(obj.getStatus());
				uHNew.setUnitDescription(obj.getUnitDescription());
				uHNew.setUnitName(obj.getUnitName());
				uHNew.setDisplayName(obj.getDisplayName());
				uHNew.setUnitPk((int) u.getPk());
				persistWrapper.createEntity(uHNew);
			}
		}
		return getUnit(u.getPk());
	}
	public  EntityActions createAction(UserContext context, OID oid, Actions[] actions) throws Exception
	{
		EntityActions act = new EntityActions();
		act.setObjectPk((int) oid.getPk());
		act.setObjectType(oid.getEntityType().getValue());

		if(actions.length > 0)
			act.setAction1(actions[0].name());
		if(actions.length > 1)
			act.setAction2(actions[1].name());
		if(actions.length > 2)
			act.setAction3(actions[2].name());
		if(actions.length > 3)
			act.setAction4(actions[3].name());
		if(actions.length > 4)
			act.setAction5(actions[4].name());

		act.setPerformedBy((int) context.getUser().getPk());
		act.setPerformedDate(new Date());

		int pk = (int) persistWrapper.createEntity(act);
		return (EntityActions) persistWrapper.readByPrimaryKey(EntityActions.class, pk);
	}

	public static String fetchSql = "select u.pk, u.partPk, u.partRevisionPk, u.supplierFk, u.siteGroupFk, u.createdBy, u.createdDate, u.lastUpdated, "
			+ " uh.serialNo, uh.unitName, uh.displayName, uh.unitDescription, uh.status " + " from TAB_UNIT u "
			+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
			+ " where 1 = 1 ";

}
