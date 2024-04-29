package com.tathvatech.unit.service;

import java.util.ArrayList;
import java.util.List;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.unit.common.UnitEntityQuery;
import com.tathvatech.unit.request.UnitEntityListReportRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class UnitEntityListReport
{
	private UnitEntityListReportRequest unitFilter;


	private final  PersistWrapper persistWrapper;
	public UnitEntityListReport(UnitEntityListReportRequest reportRequest, PersistWrapper persistWrapper)
	{
		this.unitFilter = reportRequest;
        this.persistWrapper = persistWrapper;
    }
	
	public List<UnitEntityQuery> runReport()
	{
		QueryObject qb = getSql();
		return persistWrapper.readList(UnitEntityQuery.class, qb.getQuery(), qb.getParams().toArray());
	}
	
	private QueryObject getSql()
	{
		List<Object> params = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer("select u.pk as pk, "
				+ " part_revision.pk as partRevisionPk, part_revision.revision as partRevision, "
				+ " part.pk as partPk, part.name as partName, part.partNo as partNo, u.supplierFk, u.siteGroupFk, "
				+ " uh.unitName, uh.serialNo, uh.unitDescription as description, u.lastUpdated as lastUpdatedDate, "
				+ " u.createdDate as createDate, uh.status as status, createdByUser.firstName as createdByFirstName, createdByUser.lastName as createdByLastName from "
				+ " TAB_UNIT u"
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " join part on u.partPk = part.pk "
				+ " left join part_revision on u.partRevisionPk = part_revision.pk"
				+ " left join TAB_USER createdByUser on u.createdBy=createdByUser.pk ");
		
		if(unitFilter.getShowManufacturedOnAProject() == true)
		{
			sql.append(" join (select distinct unitPk from unit_project_ref where unitOriginType = 'Manufactured') manufacturedUnits on manufacturedUnits.unitPk = u.pk ");
		}
		if(unitFilter.getShowProjectPartsAssignedOnly() == true)
		{
			sql.append(" join (select distinct partPk from project_part) projectParts on projectParts.partPk = part.pk ");
		}
		sql.append(" where 1 = 1 ");

		if(unitFilter.getSearchString() != null && unitFilter.getSearchString().trim().length() > 0)
		{
			sql.append(" and (lower(uh.unitName) like lower(?) or lower(uh.unitDescription) like lower(?) or lower(uh.serialNo) like lower(?))");
			params.add("%"+unitFilter.getSearchString().trim()+"%");
			params.add("%"+unitFilter.getSearchString().trim()+"%");
			params.add("%"+unitFilter.getSearchString().trim()+"%");
		}
		if(unitFilter.getUnitName() != null && unitFilter.getUnitName().trim().length() > 0)
		{
			sql.append(" and lower(uh.unitName) like lower(?) ");
			params.add("%"+unitFilter.getUnitName().trim()+"%");
		}
		if(unitFilter.getSerialNo() != null && unitFilter.getSerialNo().trim().length() > 0)
		{
			sql.append(" and lower(uh.serialNo) like lower(?) ");
			params.add("%"+unitFilter.getSerialNo().trim()+"%");
		}
		if(unitFilter.getPartOID() != null)
		{
			sql.append(" and u.partPk = ? ");
			params.add(unitFilter.getPartOID().getPk());
		}
		if (unitFilter.getPartNo() != null && unitFilter.getPartNo().trim().length() > 0)
		{
			sql.append(" and lower(part.partNo) like lower(?) ");
			params.add("%" + unitFilter.getPartNo().trim() + "%");
		}

		if(unitFilter.getUnitOID() != null)
		{
			sql.append(" and u.pk = ? ");
			params.add(unitFilter.getUnitOID().getPk());
		}
		sql.append(" order by uh.unitName asc ");
		
		return new QueryObject(sql.toString(), params);
	}
	
}
