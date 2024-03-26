package com.tathvatech.unit.service;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.unit.common.UnitQuery;
import com.tathvatech.unit.entity.UnitInProject;
import com.tathvatech.unit.request.UnitInProjectListReportRequest;
import com.tathvatech.workstation.common.UnitInProjectObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class UnitInProjectListReport
{
	private  PersistWrapper persistWrapper;
	private  UnitManager unitManager;
	private UnitInProjectListReportRequest unitFilter;
	public UnitInProjectListReport(PersistWrapper persistWrapper, UnitManager unitManager, UnitInProjectListReportRequest reportRequest)
	{
        this.persistWrapper = persistWrapper;
        this.unitManager = unitManager;
        this.unitFilter = reportRequest;
	}

	public UnitInProjectListReport(UnitInProjectListReportRequest request, PersistWrapper persistWrapper, UnitManager unitManager) {
        this.persistWrapper = persistWrapper;
        this.unitManager = unitManager;
    }

    public UnitInProjectListReport(UnitInProjectListReportRequest request) {
    }


    public List<UnitQuery> runReport()
	{
		QueryObject qb = getSql();
		return persistWrapper.readList(UnitQuery.class, qb.getQuery(), qb.getParams().toArray());
	}
	
	private QueryObject getSql()
	{
		String[] unitStatusInProject = unitFilter.getUnitStatusInProject();
		if(unitStatusInProject == null || unitStatusInProject.length == 0)
		{
			unitStatusInProject = new String[]{UnitInProject.STATUS_OPEN, UnitInProject.STATUS_CLOSED, UnitInProject.STATUS_PLANNED};
		}

		List<Object> params = new ArrayList<>();
		
		StringBuffer sb = new StringBuffer("select u.pk as unitPk, upr.unitOriginType as unitOriginTypeString, u.estatus, "
				+ " part_revision.pk as partRevisionPk, part_revision.revision as partRevision, "
				+ " part.pk as partPk, part.name as partName, part.partNo as partNo, "
				+ " u.supplierFk, u.siteGroupFk, "
				+ " uh.unitName, uh.displayName, uh.serialNo, uh.unitDescription as description, u.lastUpdated as lastUpdatedDate, "
				+ " u.createdDate as createDate, createdByUser.firstName as createdByFirstName, createdByUser.lastName as createdByLastName, "
				+ " uprh.projectPartPk, project_part.name as projectPartName, upr.projectPk, p.projectName, part_type.typeName as partType, uprh.status as status, "
				+ " uprh.orderNo, "
				+ " parentUnitPkRef.unitPk as parentPk, rootParentUnitPkRef.unitPk as rootParentPk, "
				+ " uprh.level, uprh.heiCode, uprh.hasChildren, "
				+ " unitOpenInProjectTab.projectPk as projectPkUnitIsOpen, unitOpenInProjectTab.projectName as projectNameUnitIsOpen, unitOpenInProjectTab.projectDescription as projectDescriptionUnitIsOpen "
				+ " from TAB_UNIT u "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " join TAB_USER createdByUser on u.createdBy=createdByUser.pk "
				+ " join part on u.partPk = part.pk "
				+ " left join part_revision on u.partRevisionPk = part_revision.pk"
				+ " join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk = ? "
				+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
				+ " join TAB_PROJECT p on upr.projectPk = p.pk "
				+ " left outer join (select pk, unitPk from unit_project_ref where projectPk = ? ) parentUnitPkRef on parentUnitPkRef.pk = uprh.parentPk"
				+ " left outer join (select pk, unitPk from unit_project_ref where projectPk = ? ) rootParentUnitPkRef on rootParentUnitPkRef.pk = uprh.rootParentPk "
				+ " left outer join (select unitPk, projectPk, projectName, projectDescription from unit_project_ref upr2, unit_project_ref_h uprh2, TAB_PROJECT p2 where uprh2.unitInProjectPk = upr2.pk and now() between uprh2.effectiveDateFrom and uprh2.effectiveDateTo and upr2.projectPk = p2.pk and uprh2.status = '"+UnitInProject.STATUS_OPEN+"' ) unitOpenInProjectTab on unitOpenInProjectTab.unitPk = u.pk "
				+ " left outer join project_part on uprh.projectPartPk = project_part.pk "
				+ " left outer join part_type on project_part.partTypePk = part_type.pk ");
		params.add(unitFilter.getProjectOID().getPk());
		params.add(unitFilter.getProjectOID().getPk());
		params.add(unitFilter.getProjectOID().getPk());
		
		if(unitFilter.getUnitsAtWorkstationOID() != null)
		{
			sb.append(" join tab_unit_location ul on ul.projectPk = p.pk and ul.unitPk = u.pk and ul.current = 1 and ul.workstationPk = ? ");
			params.add(unitFilter.getUnitsAtWorkstationOID().getPk());
		}

		sb.append(" where 1 = 1 ");

		if(true)
		{
			String sep = " ";
			sb.append(" and uprh.status in (");
			for (int i = 0; i < unitStatusInProject.length; i++) {
				sb.append(sep);
				sb.append("?");
				sep = ",";
			}
			sb.append(") ");
			params.addAll(Arrays.asList(unitStatusInProject));
		}
		
		if(unitFilter.getShowProjectPartsAssignedOnly())
		{
			sb.append(" and uprh.projectPartPk is not null and uprh.projectPartPk != 0 ");
		}
		if(unitFilter.getUnitOriginType() != null)
		{
			sb.append(" and upr.unitOriginType = ? ");
			params.add(unitFilter.getUnitOriginType().name());
		}
		if(unitFilter.getProjectParts() != null && unitFilter.getProjectParts().length > 0)
		{
			String sep = " ";
			sb.append(" and uprh.projectPartPk in (");
			for (int i = 0; i < unitFilter.getProjectParts().length; i++) {
				sb.append(sep);
				sb.append("?");
				sep = ",";
				params.add((unitFilter.getProjectParts()[i].getPk()));
			}
			sb.append(") ");
		}

		if(unitFilter.getUnitName() != null && unitFilter.getUnitName().trim().length() > 0)
		{
			sb.append(" and lower(uh.unitName) like lower(?) ");
			params.add("%"+unitFilter.getUnitName().trim()+"%");
		}
		if(unitFilter.getSerialNo() != null && unitFilter.getSerialNo().trim().length() > 0)
		{
			sb.append(" and lower(uh.serialNo) like lower(?) ");
			params.add("%"+unitFilter.getSerialNo().trim()+"%");
		}
		if(unitFilter.getPartOID() != null)
		{
			sb.append(" and u.partPk = ? ");
			params.add(unitFilter.getPartOID().getPk());
		}
		if (unitFilter.getPartNo() != null && unitFilter.getPartNo().trim().length() > 0)
		{
			sb.append(" and lower(part.partNo) like lower(?) ");
			params.add("%" + unitFilter.getPartNo().trim() + "%");
		}

		if(unitFilter.getUnitOID() != null)
		{
			if(unitFilter.isAllChildrenRecursive())
			{
				UnitInProjectObj upr = unitManager.getUnitInProject(unitFilter.getUnitOID(), unitFilter.getProjectOID());
				sb.append(" and ( (u.pk = ?) or (uprh.rootParentPk = ? and uprh.heiCode like ?) ) ");
				params.add(unitFilter.getUnitOID().getPk());
				params.add(upr.getRootParentPk());
				params.add(upr.getHeiCode()+".%");

			}
			else
			{
				sb.append(" and u.pk = ? ");
				params.add(unitFilter.getUnitOID().getPk());
			}
		}
		if (unitFilter.getUnitPks() != null && unitFilter.getUnitPks().length > 0)
		{
			String psep = " ";
			sb.append(" and u.pk in (");
			for (int i = 0; i < unitFilter.getUnitPks().length; i++)
			{
				sb.append(psep);
				sb.append(unitFilter.getUnitPks()[i]);
				psep = ",";
			}
			sb.append(") ");
		}
		if(unitFilter.getParentUnitOID() != null)
		{
			UnitInProjectObj parentUPR = unitManager.getUnitInProject(unitFilter.getParentUnitOID(), unitFilter.getProjectOID());
			if(unitFilter.isAllChildrenRecursive())
			{
				sb.append(" and  (uprh.rootParentPk = ? and uprh.heiCode like ?)  ");
				params.add(parentUPR.getRootParentPk());
				params.add(parentUPR.getHeiCode()+".%");
			}
			else
			{
				sb.append(" and uprh.parentPk = ? ");
				params.add(parentUPR.getPk());
			}
		}
		if(unitFilter.getShowRootUnitsOnly() == true)
		{
			sb.append(" and uprh.parentPk is null " );
		}
		
		if(unitFilter.getShowRootUnitsOnly() == true)
		{
			// looking for root level so desc is the sort
			sb.append(" order by uprh.orderNo desc ");
		}
		else
		{
			// desc sort for the root level and asc sort for the rest.
			// so we are doing a trick with the level and order 
			
			sb.append(" order by "
					+ " case "
					+ " when uprh.level  = 0 then uprh.level*1000 + (uprh.orderNo*-1) "
					+ " when uprh.level != 0 then uprh.level*1000 + (uprh.orderNo) "
					+ " end asc ");
		}
		
		return new QueryObject(sb.toString(), params);
	}
	
}
