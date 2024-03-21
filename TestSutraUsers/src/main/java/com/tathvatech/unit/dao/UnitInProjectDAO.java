package com.tathvatech.unit.dao;

import java.util.Date;
import java.util.List;


import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.unit.entity.EntityActions;
import com.tathvatech.unit.entity.UnitInProject;
import com.tathvatech.unit.entity.UnitInProjectH;
import com.tathvatech.unit.enums.Actions;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.unit.oid.UnitInProjectOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.utils.DateUtils;
import com.tathvatech.workstation.common.UnitInProjectObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UnitInProjectDAO
{
	private Date now ;
	@Autowired
	private  EntityActions entityActions;
	@Autowired
	private PersistWrapper persistWrapper;
	public UnitInProjectDAO()
	{

        now = DateUtils.getNowDateForEffectiveDateFrom();
	}
	
	public UnitInProjectObj getUnitInProject(UnitInProjectOID oid)
	{
		return persistWrapper.read(UnitInProjectObj.class, fetchSql + " and upr.pk = ?", oid.getPk());
	}
	
	public UnitInProjectObj getUnitInProject(UnitOID unitOID, ProjectOID projectOID)
	{
		return persistWrapper.read(UnitInProjectObj.class, fetchSql + " and upr.unitPk = ? and upr.projectPk=?", unitOID.getPk(), projectOID.getPk());
	}
	
	public UnitInProjectObj getPreviousOneInOrder(ProjectOID projectOID, UnitInProjectObj obj)
	{
		if(obj.getParentPk() != null && obj.getParentPk() != 0)
		{
			return persistWrapper.read(UnitInProjectObj.class, 
					fetchSql + " and upr.projectPk = ? and uprh.orderNo < ? and uprh.parentPk = ? order by uprh.orderNo desc limit 0, 1", 
					projectOID.getPk(), obj.getOrderNo(), obj.getParentPk());
		}
		else
		{
			return persistWrapper.read(UnitInProjectObj.class, 
					fetchSql + " and upr.projectPk = ? and uprh.orderNo < ? and uprh.parentPk is null order by uprh.orderNo desc limit 0, 1", 
					projectOID.getPk(), obj.getOrderNo());
		}
		
	}
	
	public UnitInProjectObj getNextOneInOrder(ProjectOID projectOID, UnitInProjectObj obj)
	{
		if(obj.getParentPk() != null && obj.getParentPk() != 0)
		{
			return persistWrapper.read(UnitInProjectObj.class, 
					fetchSql + " and upr.projectPk = ? and uprh.orderNo > ? and uprh.parentPk = ? order by uprh.orderNo desc limit 0, 1", 
					projectOID.getPk(), obj.getOrderNo(), obj.getParentPk());
		}
		else
		{
			return persistWrapper.read(UnitInProjectObj.class, 
					fetchSql + " and upr.projectPk = ? and uprh.orderNo > ? and uprh.parentPk is null order by uprh.orderNo desc limit 0, 1", 
					projectOID.getPk(), obj.getOrderNo());
		}
		
	}
	public  UnitInProjectObj saveUnitInProject(UserContext context, UnitInProjectObj obj) throws Exception
	{
		return saveUnitInProject(context, obj, null);
	}
	
	public  UnitInProjectObj saveUnitInProject(UserContext context, UnitInProjectObj obj, Actions[]  actions) throws Exception
	{
		UnitInProject upr = null;
		if(obj.getPk() > 0)
			upr = (UnitInProject) persistWrapper.readByPrimaryKey(UnitInProject.class, obj.getPk());
		
		if(upr == null)
			upr = persistWrapper.read(UnitInProject.class, 
				"select * from unit_project_ref where unitPk = ? and projectPk = ? ", obj.getUnitPk(), obj.getProjectPk());
		
		
		if(upr == null)
		{
			upr = new UnitInProject();
			upr.setCreatedBy(obj.getCreatedBy());
			upr.setCreatedDate(obj.getCreatedDate());
			upr.setProjectPk(obj.getProjectPk());
			upr.setUnitOriginType(obj.getUnitOriginType());
			upr.setUnitPk(obj.getUnitPk());
			int pk = (int) persistWrapper.createEntity(upr);
			upr.setPk(pk);
			
			
			EntityActions act = null;
			if(actions != null)
				act = entityActions.createAction(context, new UnitInProjectOID(pk), actions);

			UnitInProjectH uprhNew = new UnitInProjectH();
			if(act != null)
				uprhNew.setActionPk((int) act.getPk());
			uprhNew.setCreatedBy((int) context.getUser().getPk());
			uprhNew.setCreatedDate(new Date());
			uprhNew.setEffectiveDateFrom(now);
			uprhNew.setEffectiveDateTo(DateUtils.getMaxDate());
			uprhNew.setHasChildren(obj.getHasChildren());
			uprhNew.setHeiCode(obj.getHeiCode());
			uprhNew.setLevel(obj.getLevel());
			uprhNew.setOrderNo(obj.getOrderNo());
			uprhNew.setParentPk(obj.getParentPk());
			uprhNew.setProjectPartPk(obj.getProjectPartPk());
			uprhNew.setRootParentPk(obj.getRootParentPk());
			uprhNew.setStatus(obj.getStatus());
			uprhNew.setUnitInProjectPk((int) upr.getPk());
			persistWrapper.createEntity(uprhNew);
		}
		else
		{
			UnitInProjectH uprhCurrent = persistWrapper.read(UnitInProjectH.class, 
					"select * from unit_project_ref_h where unitInProjectPk = ? and now() between effectiveDateFrom and effectiveDateTo ", obj.getPk());
			
			
			if(uprhCurrent.getEffectiveDateFrom().equals(now))
			{
				// we have to do an update of the h record here.. since it is from the same DAO context as the now date is the same.
				uprhCurrent.setEffectiveDateFrom(now);
				uprhCurrent.setEffectiveDateTo(DateUtils.getMaxDate());
				uprhCurrent.setHasChildren(obj.getHasChildren());
				uprhCurrent.setHeiCode(obj.getHeiCode());
				uprhCurrent.setLevel(obj.getLevel());
				uprhCurrent.setOrderNo(obj.getOrderNo());
				uprhCurrent.setParentPk(obj.getParentPk());
				uprhCurrent.setProjectPartPk(obj.getProjectPartPk());
				uprhCurrent.setRootParentPk(obj.getRootParentPk());
				uprhCurrent.setStatus(obj.getStatus());
				uprhCurrent.setUnitInProjectPk((int) upr.getPk());
				persistWrapper.update(uprhCurrent); 
			}
			else
			{
				uprhCurrent.setEffectiveDateTo(new Date(now.getTime() - 1000));
				persistWrapper.update(uprhCurrent);

				EntityActions act = null;
				if(actions != null)
					act = entityActions.createAction(context, new UnitInProjectOID(obj.getPk()) ,actions);

				UnitInProjectH uprhNew = new UnitInProjectH();
				if(act != null)
					uprhNew.setActionPk((int) act.getPk());
				uprhNew.setCreatedBy((int) context.getUser().getPk());
				uprhNew.setCreatedDate(new Date());
				uprhNew.setEffectiveDateFrom(now);
				uprhNew.setEffectiveDateTo(DateUtils.getMaxDate());
				uprhNew.setHasChildren(obj.getHasChildren());
				uprhNew.setHeiCode(obj.getHeiCode());
				uprhNew.setLevel(obj.getLevel());
				uprhNew.setOrderNo(obj.getOrderNo());
				uprhNew.setParentPk(obj.getParentPk());
				uprhNew.setProjectPartPk(obj.getProjectPartPk());
				uprhNew.setRootParentPk(obj.getRootParentPk());
				uprhNew.setStatus(obj.getStatus());
				uprhNew.setUnitInProjectPk((int) upr.getPk());
				persistWrapper.createEntity(uprhNew); 
			}
		}

		return getUnitInProject(upr.getOID());
	}
	
	public void removeUnit(UserContext context, UnitInProjectObj unitInProject) throws Exception
	{
		EntityActions act = entityActions.createAction(context, unitInProject.getOID(), new Actions[]{Actions.removeUnitFromProject});

		UnitInProjectH hRec = getHRecord(unitInProject.getOID(), now);
		hRec.setEffectiveDateTo(new Date(now.getTime()-1000));
		persistWrapper.update(hRec);
		
		UnitInProjectH hRecNew = hRec.clone();
		hRecNew.setPk(0);
		hRecNew.setStatus(UnitInProject.STATUS_REMOVED);
		hRecNew.setActionPk((int) act.getPk());
		hRecNew.setCreatedBy((int) context.getUser().getPk());
		hRecNew.setCreatedDate(new Date());
		hRecNew.setEffectiveDateFrom(now);
		hRecNew.setEffectiveDateTo(DateUtils.getMaxDate());
		persistWrapper.createEntity(hRecNew);
	}

	public List<UnitInProjectObj> getDirectChildren(UnitInProjectOID oid)
	{
		UnitInProjectObj uprObj = getUnitInProject(oid);
		return persistWrapper.readList(UnitInProjectObj.class,
				fetchSql + " and upr.projectPk = ? and uprh.parentPk = ?",
				uprObj.getProjectPk(), uprObj.getPk());
				
	}
	
	public List<UnitInProjectObj> getAllChildrenInTree(UnitInProjectOID oid)
	{
		UnitInProjectObj uprObj = getUnitInProject(oid);
		return persistWrapper.readList(UnitInProjectObj.class,
				fetchSql + " and upr.projectPk = ? and uprh.rootParentPk = ? and uprh.level > ? and uprh.heiCode like ?",
				uprObj.getProjectPk(), uprObj.getRootParentPk(), uprObj.getLevel(), uprObj.getHeiCode()+".%");
				
	}
	
	public int getCountDirectChildren(UnitInProjectOID oid)
	{
		String sql = "select count(*) from unit_project_ref upr" 
				+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo " 
				+ " where uprh.parentPk = ? and upr.unitOriginType = ? "; 
		return persistWrapper.read(Integer.class, sql, oid.getPk(), UnitOriginType.Manufactured.name());
	}
	
	private UnitInProjectH getHRecord(UnitInProjectOID oid, Date date)
	{
		return persistWrapper.read(UnitInProjectH.class, 
				"select * from unit_project_ref_h where unitInProjectPk = ? and ? between effectiveDateFrom and effectiveDateTo", oid.getPk(), date);
	}
	
	private static String fetchSql = "select upr.pk, upr.unitPk, upr.projectPk, upr.unitOriginType, upr.createdBy, upr.createdDate, upr.lastUpdated, "
			+ " uprh.projectPartPk, uprh.status, uprh.parentPk, uprh.rootParentPk, uprh.level, uprh.hasChildren, uprh.orderNo, uprh.heiCode "
			+ " from unit_project_ref upr "
			+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo "
			+ " where 1 = 1 ";

}
