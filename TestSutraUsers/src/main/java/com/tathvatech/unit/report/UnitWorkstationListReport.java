package com.tathvatech.unit.report;

import java.util.ArrayList;
import java.util.List;

import com.tathvatech.testsutra.ncr.common.QueryObject;
import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.DummyWorkstation;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.project.UnitLocation;
import com.thirdi.surveyside.project.UnitInProjectObj;
import com.thirdi.surveyside.project.UnitManager;

public class UnitWorkstationListReport
{
	private UnitWorkstationListReportFilter filter;

	public UnitWorkstationListReport(UserContext context, UnitWorkstationListReportFilter filter)
	{
		this.filter = filter;
	}

	public List<UnitWorkstationListReportResultRow> runReport()
	{
		QueryObject q = getSql();
		return PersistWrapper.readList(UnitWorkstationListReportResultRow.class, q.getQuery(),
				(q.getParams().size() > 0) ? q.getParams().toArray(new Object[q.getParams().size()]) : null);
	}

	public QueryObject getSql()
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("select " + " uw.pk as pk, " + " uw.projectPk as projectPk, "
				+ " p.projectName as projectName, " + " p.projectDescription as projectDescription, "
				+ " part.pk as partPk, " + " part.name as partName, " + " partType.typeName as partType, "
				+ " pp.name as projectPartName, " + " uw.workstationPk as workstationPk, "
				+ " w.workstationName as workstationName, " + " w.description as workstationDescription, "
				+ " case when ul.status is null then '" + UnitLocation.STATUS_WAITING
				+ "' else ul.status end as workstationStatus, " + " site.timeZone as workstationTimezoneId, "
				+ " uw.unitPk as unitPk, " + " uh.unitName as unitName, " + " uh.unitDescription, "
				+ " uh.serialNo as unitSerialNumber, " + " uprh.projectPartPk as projectPartPk, "
				+ " uwes.forecastStartDate as forecastStartDate, " + " uwes.forecastEndDate as forecastEndDate, "
				+ " uwes.estimateHours as forecastHours, " + " uwes.comment as forecastComment, "
				+ " uwes.createdBy as forecastCreatedBy, " + " uwes.createdDate as forecastCreatedDate,"
				+ " uwesUser.firstName as forecastCreatedByFirstName, "
				+ " uwesUser.lastName as forecastCreatedByLastName " + " from tab_unit_workstations uw "
				+ " join TAB_UNIT u on uw.unitPk = u.pk "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " join TAB_PROJECT p on uw.projectPk = p.pk "
				+ " join TAB_WORKSTATION w on uw.workstationPk = w.pk and w.pk != " + DummyWorkstation.getPk() + ""
				+ " join site on w.sitePk = site.pk"
				+ " join unit_project_ref upr on upr.unitPk = uw.unitPk and upr.projectPk = uw.projectPk"
				+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
				+ " left outer join project_part pp on uprh.projectPartPk = pp.pk "
				+ " left outer join part on pp.partPk = part.pk "
				+ " left outer join part_type partType on pp.partTypePk = partType.pk"
				+ " left outer join TAB_UNIT_LOCATION ul on uw.unitPk = ul.unitPk and uw.projectPk = ul.projectPk and uw.workstationPk = ul.workstationPk and ul.current = 1"
				+ " left outer join entity_schedule uwes on uwes.objectPk = uw.pk and uwes.objectType = "
				+ EntityTypeEnum.UnitWorkstation.getValue()
				+ " and now() between uwes.effectiveDateFrom and uwes.effectiveDateTo "
				+ " left outer join TAB_USER uwesUser on uwes.createdBy = uwesUser.pk " + " where 1 = 1 ");

		if (filter.getUnitOID() != null)
		{
			if (filter.isIncludeChildren() == true)
			{
				if (filter.getProjectOID() == null)
				{
					throw new AppException("Project should be specified to include chileren of the selected unit");
				}
				UnitInProjectObj unit = UnitManager.getUnitInProject(filter.getUnitOID(), filter.getProjectOID());
				sb.append(
						" and ( (upr.unitPk = ? and upr.projectPk = ?) or (uprh.rootParentPk = ? and uprh.heiCode like ?) ) ");
				params.add(filter.getUnitOID().getPk());
				params.add(filter.getProjectOID().getPk());
				params.add(unit.getRootParentPk());
				params.add(unit.getHeiCode() + ".%");
			} else
			{
				sb.append(" and u.pk = ?");
				params.add(filter.getUnitOID().getPk());
			}
		}
		if (filter.getProjectOID() != null)
		{
			sb.append(" and uw.projectPk = ?");
			params.add(filter.getProjectOID().getPk());
		}
		if (filter.getWorkstationOID() != null)
		{
			sb.append(" and uw.workstationPk = ?");
			params.add(filter.getWorkstationOID().getPk());
		}
		if (filter.getWorkstationStatus() != null && filter.getWorkstationStatus().size() > 0)
		{
			sb.append(" and (");
			if (filter.getWorkstationStatus().contains(UnitLocation.STATUS_WAITING))
			{
				sb.append(" ul.status is null or ");
			}
			sb.append(" ul.status in ( ");
			String comma = "";
			for (String status : filter.getWorkstationStatus())
			{
				sb.append(comma).append("?");
				comma = ",";
				params.add(status);
			}
			sb.append(" )) ");
		}

		sb.append(" and 1 = 1");
		sb.append(" order by uprh.level, uprh.orderNo, w.orderNo ");

		return new QueryObject(sb.toString(), params);
	}

}
