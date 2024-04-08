package com.tathvatech.ncr.common;
import com.tathvatech.injury.common.InjuryLocationMasterQuery;
import com.tathvatech.ncr.enums.NcrEnum;
import com.tathvatech.openitem.andon.entity.OpenItemV2;
import com.tathvatech.user.OID.LocationTypeOID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.tathvatech.user.OID.WhereFoundOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.workstation.common.WorkstationQuery;
import org.apache.commons.lang3.time.DateUtils;


public class NcrItemQueryBuilder
{
	UserContext context;
	private QueryObject query;
	private NcrItemQueryFilter ncrItemQueryFilter;

	public NcrItemQueryBuilder(UserContext context, NcrItemQueryFilter ncrItemQueryFilter)
	{
		super();
		this.context = context;
		this.ncrItemQueryFilter = ncrItemQueryFilter;
	}

	public QueryObject getQuery()
	{
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(getColumns());
		QueryObject queryObject = getConditions();
		if (queryObject == null)
			return null;
		sqlQuery.append(queryObject.getQuery());
		query = new QueryObject();
		query.setQuery(sqlQuery.toString());
		query.setParams(queryObject.getParams());
		return query;
	}

	private String getColumns()
	{
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(" select ");
		sqlQuery.append(" ncr.pk as pk ,");
		sqlQuery.append(" ncr.ncrDesc as ncrDesc ,");
		sqlQuery.append(" ncr.createdBy as createdBy, ");
		sqlQuery.append(" ncr.createdDate as createdDate, ");
		sqlQuery.append(" ncr.cancelledBy as cancelledBy, ");
		sqlQuery.append(" ncr.cancelledDate as cancelledDate,");
		sqlQuery.append(" ncr.locationOther as locationOther, ");
		sqlQuery.append(" ncr.custodianPk as custodianPk , ");
		sqlQuery.append(" ncr.ncrGroupFk as ncrGroupFk , ");
		sqlQuery.append(" ncrGroup.ncrGroupNo as ncrGroupNo , ");
		sqlQuery.append(" ncrGroup.groupDescription as ncrGroupDesc , ");
		sqlQuery.append(" ncrGroup.supplierFk as supplierFk,");
		sqlQuery.append(" ncr.publishedComment as publishedComment, ");
		sqlQuery.append(" ncr.verifiedComment as completedComment, ");
		sqlQuery.append(" ncr.closedComment as closedComment, ");
		sqlQuery.append(" ncr.unitOfMeasureFk as unitOfMeasureFk, ");
		sqlQuery.append(" ncr.dispositionFk as dispositionFk, ");
		sqlQuery.append(" project.pk as projectFk, ");
		sqlQuery.append(" project.projectName as projectName, ");
		sqlQuery.append(" project.projectDescription as projectDescription, ");
		sqlQuery.append(" part.pk as partFk, ");
		sqlQuery.append(" part.partNo as partNo, ");
		sqlQuery.append(" part.name as partName, ");
		sqlQuery.append(" ncrGroup.revisionFk as revisionFk, ");
		sqlQuery.append(" part.description as partDescription, ");
		sqlQuery.append(" supplierDetail.supplierName as supplierName, ");
		sqlQuery.append(" ncr.ncrNo as ncrNo, ");
		sqlQuery.append(" dispositionT.name as disposition, ");
		sqlQuery.append(" case when subcat.parentFk is null then null else subcat.name end as subCategory, "); // if
																												// subcat.parent
																												// is
																												// null,
																												// that
																												// means
																												// the
																												// selected
																												// value
																												// is
																												// a
																												// cat.
		sqlQuery.append(" case when subcat.parentFk is null then subcat.name else cat.name end as category, "); // if
																												// subcat.parent
																												// is
																												// null,
																												// that
																												// means
																												// the
																												// selected
																												// value
																												// is
																												// a
																												// cat.
		sqlQuery.append(" ncr.mrfNo as mrfNo, ");
		sqlQuery.append(" ncr.ncrDesc as nCR_Description, ");
		sqlQuery.append(" ncr.ncrStatus as nCR_Status, ");
		sqlQuery.append(
				" case when locationType = 'Workstation' then workstation.workstationName when locationType = 'LocationType' then injuryLocation.name  when locationType = 'WhereFoundType' then whereFound.name end as Where_Found,");
		sqlQuery.append(" concat (cust.firstName , '', cust.lastName) as custodianName, ");
		sqlQuery.append(" ncr.quantity as quantity,  ");
		sqlQuery.append(" quantityUnit.name as unit_of_Measure,  ");
		sqlQuery.append(" aor.name as area_Of_Responsibility,  ");
		sqlQuery.append(" site.pk as siteFk, ");
		sqlQuery.append(" site.name as siteName, ");
		sqlQuery.append(" site.description as siteDescription, ");
		sqlQuery.append(" supplier.pk as tsSupplierId, ");
		sqlQuery.append(" ssgm.supplierId as supplierId, ");
		sqlQuery.append(" concat (cancelledByT.firstName ,'', cancelledByT.lastName) as cancelledByName,  ");
		sqlQuery.append(" concat (createdByT.firstName , '', createdByT.lastName) as createdByName,  ");
		sqlQuery.append(" ncr.createdDate as createdDate,  ");
		sqlQuery.append(" concat (publishedByT.firstName ,'', publishedByT.lastName) as publishedByName,  ");
		sqlQuery.append(" ncr.publishedDate as publishedDate,  ");
		sqlQuery.append(" concat (completedByT.firstName , '', completedByT.lastName) as completedByName,  ");
		sqlQuery.append(" ncr.verifiedDate as completedDate,  ");
		sqlQuery.append(" concat (closedByT.firstName , '', closedByT.lastName) as closedByName,  ");
		sqlQuery.append(" ncr.closedDate as closedDate,  ");
		sqlQuery.append(" concat (rejectedByT.firstName , '', rejectedByT.lastName) as latestRejectedByName,  ");
		sqlQuery.append(" ncrFunctionRejected.approvedDate as latestRejectedDate, ");
		sqlQuery.append(" ncrFunctionApproved.approvedDate as latestApprovedDate, ");
		sqlQuery.append(" ncrFunctionApproved.approvedBy as latestApprovedBy, ");
		sqlQuery.append(" concat (approvedByT.firstName , '', approvedByT.lastName) as latestApprovedByName,  ");
		sqlQuery.append(" ncr.forecastStartDate as forecastStartDate,  ");
		sqlQuery.append(" ncr.forecastCompletionDate as forecastCompletionDate,  ");
		sqlQuery.append(" ncr.carType as carType,  ");
		sqlQuery.append(" ncr.hours as hours,  ");
		sqlQuery.append(" ncr.severity as severity,  ");
		sqlQuery.append(" ncr.occurrence as occurrence,  ");
		sqlQuery.append(" ncr.detection as detection,  ");
		sqlQuery.append(" ncr.escape as escape,  ");
		sqlQuery.append(" ncr.priority as priority,  ");
		sqlQuery.append(" rwo.reworkOrderNumber as rework_Order,  ");
		sqlQuery.append(" ncr.rootCause as rootCause,  ");
		sqlQuery.append(" ncr.workInstructionOrComment as workInstructionOrComment,  ");
		sqlQuery.append(" ncr.dispositionComment as dispositionComment, ");
		sqlQuery.append(" ncr.areaOfResponsibilityFk as areaOfResponsibilityFk,");
		sqlQuery.append(" ncr.ppsor8d as ppsor8d,  ");
		sqlQuery.append(
				" case when sourceType = 'Workstation' then srcWorkstation.workstationName when sourceType = 'LocationType' then srcInjuryLocation.name when sourceType = 'WhereFoundType' then srcWhereFound.name end as Source , ");
		sqlQuery.append(
				"    case when ncrFunctionApproved.approvedDate is not null then datediff(ncrFunctionApproved.approvedDate , ncr.publishedDate) when ncrFunctionApproved.approvedDate is  null then datediff( now() , ncr.publishedDate) end as noOfDaysOpen  ");
		return sqlQuery.toString();
	}

	private QueryObject getConditions()
	{
		StringBuffer sqlQuery = new StringBuffer("");
		sqlQuery.append(" from");
		sqlQuery.append(" ncr");
		sqlQuery.append(" join ncr_group ncrGroup on ncr.ncrGroupFk = ncrGroup.pk");
		sqlQuery.append(" left join site site on ncrGroup.siteFk=site.pk");
		sqlQuery.append(" join TAB_PROJECT project on ncrGroup.projectFk = project.pk ");
		sqlQuery.append(" left join TAB_USER createdByT on ncr.createdBy = createdByT.pk ");
		sqlQuery.append(" left join TAB_USER publishedByT on ncr.publishedBy = publishedByT.pk");
		sqlQuery.append(" left join TAB_USER completedByT on ncr.verifiedBy = completedByT.pk ");
		sqlQuery.append(" left join TAB_USER closedByT on ncr.closedBy = closedByT.pk");
		sqlQuery.append(" left join TAB_USER cancelledByT on ncr.cancelledBy = cancelledByT.pk");
		sqlQuery.append(" left join part part on ncrGroup.partFk = part.pk ");
		sqlQuery.append(" left join supplier supplier on ncrGroup.supplierFk = supplier.pk ");
		sqlQuery.append(" left join supplier_h supplierDetail on supplierDetail.supplierFk = supplier.pk  and now() between supplierDetail.effectiveFrom and supplierDetail.effectiveTo ");
		sqlQuery.append(" left join supplier_sitegroup_mapping ssgm on ssgm.siteGroupFk = site.siteGroupFk and ssgm.supplierFk = supplier.pk");
		sqlQuery.append(" left join ncr_disposition_master dispositionT on ncr.dispositionFk = dispositionT.pk");
		sqlQuery.append(" left join rework_order_entry rwo on ncr.reworkOrderFk = rwo.pk ");
		sqlQuery.append(" left join ncr_failure_code_master subcat on ncr.ncrFailureCodeFk = subcat.pk ");
		sqlQuery.append(" left join ncr_failure_code_master cat on subcat.parentFk = cat.pk ");
		sqlQuery.append(" left join unit_of_measure quantityUnit on ncr.unitOfMeasureFk = quantityUnit.pk");
		sqlQuery.append(" left join ncr_area_of_responsibility_master aor on ncr.areaOfResponsibilityFk = aor.pk ");
		sqlQuery.append(" left join TAB_USER cust on ncr.custodianPk = cust.pk");
		sqlQuery.append(" left join TAB_WORKSTATION workstation on workstation.pk = ncr.locationPk ");
		sqlQuery.append(" left join injury_location_master injuryLocation on injuryLocation.pk = ncr.locationPk");
		sqlQuery.append(" left join ncr_where_found whereFound on whereFound.pk = ncr.locationPk ");
		sqlQuery.append(" left join TAB_WORKSTATION srcWorkstation on srcWorkstation.pk = ncr.sourcePk ");
		sqlQuery.append(" left join injury_location_master srcInjuryLocation on srcInjuryLocation.pk = ncr.sourcePk ");
		sqlQuery.append(" left join ncr_where_found srcWhereFound on srcWhereFound.pk = ncr.sourcePk");
		sqlQuery.append(
				" left join ncr_function ncrFunctionRejected on  ncrFunctionRejected.pk=( SELECT pk from ncr_function where  ncr_function.objectFk=ncr.pk and ncr_function.objectType ='NCR' and approvedStatus ='REJECTED' order by approvedDate desc Limit 1)");
		sqlQuery.append(
				" left join ncr_function ncrFunctionApproved on ncrFunctionApproved.pk=(SELECT pk from ncr_function where ncr_function.objectFk=ncr.pk and ncr_function.objectType ='NCR' and approvedStatus ='APPROVED' order by approvedDate desc Limit 1 )");
		sqlQuery.append(" left join TAB_USER rejectedByT on ncrFunctionRejected.approvedBy = rejectedByT.pk ");
		sqlQuery.append(" left join TAB_USER approvedByT on ncrFunctionApproved.approvedBy = approvedByT.pk");
		List<Object> params = new ArrayList();
		if (ncrItemQueryFilter.getUnitFks() != null )
		{
			sqlQuery.append(" left join ncr_unit_assign on ncr_unit_assign.ncrFk=ncr.pk");
		}
		if (ncrItemQueryFilter.getIsPendingMyApproval() != null && ncrItemQueryFilter.getIsPendingMyApproval())
		{
			sqlQuery.append(
					" join project_site_config projectSiteConfig on projectSiteConfig.projectFk = ncrGroup.projectFk and projectSiteConfig.siteFk =ncrGroup.siteFk ");
			sqlQuery.append(" join ACL aclT on aclT.objectType = " + EntityTypeEnum.ProjectSiteConfig.getValue()
					+ " and aclT.objectPk = projectSiteConfig.pk and aclT.userPk = " + context.getUser().getPk() + "");
			sqlQuery.append(" join  ncr_function functionT on  functionT.objectFk = ncr.pk and functionT.objectType ='"
					+ EntityTypeEnum.NCR.name()
					+ "' and  functionT.approveRequired = 1 and functionT.approvedStatus is NULL and functionT.isCurrent=true");
			/*
			 * Value is hard coded because role ID in acl table and Name in
			 * Ncr_function_master table mismatch
			 */
			sqlQuery.append(" and aclT.roleId= CASE WHEN functionT.functionMasterFk=3022 THEN 'InternalTransport' "
					+ " WHEN functionT.functionMasterFk=3018 THEN 'PlanningControl' "
					+ " WHEN functionT.functionMasterFk=3001 THEN 'ProjectProductManagement' "
					+ " WHEN functionT.functionMasterFk=3011 THEN 'MethodsProcessEngineering' "
					+ " WHEN functionT.functionMasterFk=3016 THEN 'SupplyChain'"
					+ " WHEN functionT.functionMasterFk=3017 THEN 'Sourcing'"
					+ " WHEN functionT.functionMasterFk=3020 THEN 'Warehouse' "
					+ " WHEN functionT.functionMasterFk=3006 THEN 'Engineering' "
					+ " WHEN functionT.functionMasterFk=3019 THEN 'QualityAssurance' "
					+ " WHEN functionT.functionMasterFk=3030 THEN 'ComponentProduction' "
					+ " WHEN functionT.functionMasterFk=3040 THEN 'Weldingshop' "
					+ " WHEN functionT.functionMasterFk=3050 THEN 'Carbody' "
					+ " WHEN functionT.functionMasterFk=3060 THEN 'Paintshop' "
					+ " WHEN functionT.functionMasterFk=3070 THEN 'PreAssembly' "
					+ " WHEN functionT.functionMasterFk=3080 THEN 'FinalAssembly' "
					+ " WHEN functionT.functionMasterFk=3100 THEN 'Retrofit' "
					+ " WHEN functionT.functionMasterFk=3200 THEN 'Refurbishment' "
					+ " WHEN functionT.functionMasterFk=3300 THEN 'Testing' "
					+ " WHEN functionT.functionMasterFk=3400 THEN 'ShippingToCustomer' "
					+ " WHEN functionT.functionMasterFk=3500 THEN 'ProductIntrodution' "
					+ " WHEN functionT.functionMasterFk=3600 THEN 'FieldMaintenanceService' "
					+ " WHEN functionT.functionMasterFk=3700 THEN 'SupplierQualityAssurance' "
					+ " WHEN functionT.functionMasterFk=3800 THEN 'Finance' "
					+ " WHEN functionT.functionMasterFk=3900 THEN 'SiteGM' "
					+ " WHEN functionT.functionMasterFk=3901 THEN 'Installation' "
					+ " END");
		}
		sqlQuery.append(" where 1=1 ");

		sqlQuery.append(" and ncr.eStatus = ?");
		params.add(EStatusEnum.Active.getValue());

		if (ncrItemQueryFilter.getPk() != 0)
		{
			sqlQuery.append(" and ncr.pk = ?");
			params.add(ncrItemQueryFilter.getPk());
		} else
		{
			if (ncrItemQueryFilter.getNcrGroupFk() != null)
			{
				sqlQuery.append(" and ncr.ncrGroupFk = ?");
				params.add(ncrItemQueryFilter.getNcrGroupFk());
			}
			if (ncrItemQueryFilter.getNcrGroupNo() != null && ncrItemQueryFilter.getNcrGroupNo().trim().length() > 0)
			{
				sqlQuery.append(" and lower(ncrGroupNo) like lower(?)");
				params.add("%" + ncrItemQueryFilter.getNcrGroupNo().trim() + "%");
			}
			if (ncrItemQueryFilter.getNcrGroupDesc() != null
					&& ncrItemQueryFilter.getNcrGroupDesc().trim().length() > 0)
			{
				sqlQuery.append(" and lower(ncrGroupDesc) like lower(?)");
				params.add("%" + ncrItemQueryFilter.getNcrGroupDesc().trim() + "%");
			}
			if (ncrItemQueryFilter.getNcrGroupStatusList() != null
					&& ncrItemQueryFilter.getNcrGroupStatusList().size() > 0)
			{
				sqlQuery.append(" and ncrGroup.status in (");
				String comma = "";
				for (NcrEnum.NcrGroupStatus status : ncrItemQueryFilter.getNcrGroupStatusList())
				{
					sqlQuery.append(comma).append("'").append(status.name()).append("'");
					comma = ",";
				}
				sqlQuery.append(" ) ");
			}

			if (ncrItemQueryFilter.getCreatedDateFrom() != null)
			{
				sqlQuery.append(" and ncrGroup.createdDate > ? ");
				Date dateFrom = DateUtils.truncate(ncrItemQueryFilter.getCreatedDateFrom(), Calendar.DATE);
				params.add(dateFrom);
			}
			if (ncrItemQueryFilter.getCreatedDateTo() != null)
			{
				sqlQuery.append(" and ncrGroup.createdDate  < ?");
				Date dateTo = DateUtils.truncate(ncrItemQueryFilter.getCreatedDateTo(), Calendar.DATE);
				Date d = DateUtils.addDays(dateTo, 1);
				params.add(d);
			}
			if (ncrItemQueryFilter.getPartFk() != null)
			{
				sqlQuery.append(" and ncrGroup.partFk = ?");
				params.add(ncrItemQueryFilter.getPartFk());
			}
			if (ncrItemQueryFilter.getSitePks() != null && ncrItemQueryFilter.getSitePks().size() != 0)
			{
				sqlQuery.append(" and ncrGroup.siteFk in ");
				String filterSitePks = Arrays.deepToString(ncrItemQueryFilter.getSitePks().toArray());
				filterSitePks = filterSitePks.replace('[', '(');
				filterSitePks = filterSitePks.replace(']', ')');
				sqlQuery.append(filterSitePks);
			}
			if (ncrItemQueryFilter.getUnitFks() != null )
			{
				sqlQuery.append(" and ncr_unit_assign.unitFk in ");
				String filterSitePks = Arrays.deepToString(ncrItemQueryFilter.getUnitFks().toArray());
				filterSitePks = filterSitePks.replace('[', '(');
				filterSitePks = filterSitePks.replace(']', ')');
				sqlQuery.append(filterSitePks);
				if (ncrItemQueryFilter.isOpenItemInfoOnly())
				{
					sqlQuery.append(" and ncr_unit_assign.status = ? ");
					params.add(OpenItemV2.StatusEnum.Open.name());
				}
			}
			if (ncrItemQueryFilter.getPartName() != null && ncrItemQueryFilter.getPartName().trim().length() > 0)
			{
				sqlQuery.append(" and (lower(part.partNo) like lower(?) or  lower(part.name) like lower(?)) ");
				params.add("%" + ncrItemQueryFilter.getPartName().trim() + "%");
				params.add("%" + ncrItemQueryFilter.getPartName().trim() + "%");
			}
			if (ncrItemQueryFilter.getSupplierName() != null
					&& ncrItemQueryFilter.getSupplierName().trim().length() > 0)
			{
				sqlQuery.append(
						" and (lower(supplierDetail.supplierName) like lower(?) or  lower(ssgm.supplierId) like lower(?)) ");
				params.add("%" + ncrItemQueryFilter.getSupplierName().trim() + "%");
				params.add("%" + ncrItemQueryFilter.getSupplierName().trim() + "%");
			}
			if (ncrItemQueryFilter.getProjectPks() != null && ncrItemQueryFilter.getProjectPks().size() != 0)
			{
				sqlQuery.append(" and ncrGroup.projectFk in ");
				String proPks = Arrays.deepToString(ncrItemQueryFilter.getProjectPks().toArray());
				proPks = proPks.replace('[', '(');
				proPks = proPks.replace(']', ')');
				sqlQuery.append(proPks);
			}
			if (ncrItemQueryFilter.getSupplierPks() != null && ncrItemQueryFilter.getSupplierPks().size() != 0)
			{
				sqlQuery.append(" and supplier.pk  in");
				String supplierPks = Arrays.deepToString(ncrItemQueryFilter.getSupplierPks().toArray());
				supplierPks = supplierPks.replace('[', '(');
				supplierPks = supplierPks.replace(']', ')');
				sqlQuery.append(supplierPks);
			}
			if (ncrItemQueryFilter.getPartPks() != null && ncrItemQueryFilter.getPartPks().size() != 0)
			{
				sqlQuery.append(" and ncrGroup.partFk in ");
				String partPks = Arrays.deepToString(ncrItemQueryFilter.getPartPks().toArray());
				partPks = partPks.replace('[', '(');
				partPks = partPks.replace(']', ')');
				sqlQuery.append(partPks);
			}
			if (ncrItemQueryFilter.getCustodianPks() != null && ncrItemQueryFilter.getCustodianPks().size() != 0)
			{
				sqlQuery.append(" and ncr.custodianPk in ");
				String custPks = Arrays.deepToString(ncrItemQueryFilter.getCustodianPks().toArray());
				custPks = custPks.replace('[', '(');
				custPks = custPks.replace(']', ')');
				sqlQuery.append(custPks);
			}
			if (ncrItemQueryFilter.getDispositionPks() != null && ncrItemQueryFilter.getDispositionPks().size() != 0)
			{
				sqlQuery.append(" and ncr.dispositionFk in ");
				String dispositionPk = Arrays.deepToString(ncrItemQueryFilter.getDispositionPks().toArray());
				dispositionPk = dispositionPk.replace('[', '(');
				dispositionPk = dispositionPk.replace(']', ')');
				sqlQuery.append(dispositionPk);
			}
			if (ncrItemQueryFilter.getDispositionFk() != null && ncrItemQueryFilter.getDispositionFk() != 0)
			{
				sqlQuery.append(" and ncr.dispositionFk = ?");
				params.add(ncrItemQueryFilter.getDispositionFk());
			}
			if (ncrItemQueryFilter.getNcrAreaOfResponsibilityBean() != null
					&& ncrItemQueryFilter.getNcrAreaOfResponsibilityBean().getPk() != 0)
			{
				if (ncrItemQueryFilter.getNcrAreaOfResponsibilityBean().getParentFk() == null)
				{
					sqlQuery.append(
							" and ncr.areaOfResponsibilityFk in (SELECT pk FROM ncr_area_of_responsibility_master where pk =? or parentFk =?)");
					params.add(ncrItemQueryFilter.getNcrAreaOfResponsibilityBean().getPk());
					params.add(ncrItemQueryFilter.getNcrAreaOfResponsibilityBean().getPk());
				} else
				{
					sqlQuery.append(" and ncr.areaOfResponsibilityFk = ?");
					params.add(ncrItemQueryFilter.getNcrAreaOfResponsibilityBean().getPk());
				}
			}
			if (ncrItemQueryFilter.getNcrFailureCodeMasterBean() != null
					&& ncrItemQueryFilter.getNcrFailureCodeMasterBean().getPk() != 0)
			{
				sqlQuery.append(" and ncr.ncrFailureCodeFk = ?");
				params.add(ncrItemQueryFilter.getNcrFailureCodeMasterBean().getPk());
			}

			if (ncrItemQueryFilter.getCustodian() != null && ncrItemQueryFilter.getCustodian().trim().length() > 0)
			{
				sqlQuery.append(" and lower(concat(cust.firstName,' ',cust.lastName) ) like lower(?)");
				params.add("%" + ncrItemQueryFilter.getCustodian().trim() + "%");
			}
			if (ncrItemQueryFilter.getCreatedByName() != null
					&& ncrItemQueryFilter.getCreatedByName().trim().length() > 0)
			{
				sqlQuery.append(" and lower(concat(createdByT.firstName,' ',createdByT.lastName) ) like lower(?)");
				params.add("%" + ncrItemQueryFilter.getCreatedByName().trim() + "%");
			}
			if (ncrItemQueryFilter.getPublishedByName() != null
					&& ncrItemQueryFilter.getPublishedByName().trim().length() > 0)
			{
				sqlQuery.append(" and lower(concat(publishedByT.firstName,' ',publishedByT.lastName) ) like lower(?)");
				params.add("%" + ncrItemQueryFilter.getPublishedByName().trim() + "%");
			}
			if (ncrItemQueryFilter.getIsApprovalWaiting() != null && ncrItemQueryFilter.getIsApprovalWaiting())
			{
				sqlQuery.append(" and  ncr.ncrStatus='" + NcrEnum.NcrItemStatus.PUBLISHED + "'");
			}

			if (ncrItemQueryFilter.getNcrItemStatusList() != null
					&& ncrItemQueryFilter.getNcrItemStatusList().size() > 0)
			{
				sqlQuery.append(" and ncr.ncrStatus in (");
				String comma = "";
				for (NcrEnum.NcrItemStatus status : ncrItemQueryFilter.getNcrItemStatusList())
				{
					sqlQuery.append(comma).append("'").append(status.name()).append("'");
					comma = ",";
				}
				sqlQuery.append(" ) ");
			}
			if (ncrItemQueryFilter.getSources() != null && ncrItemQueryFilter.getSources().size() > 0)
			{
				List<Long> ncrWhereFoundPks = ncrItemQueryFilter.getSources().stream()
						.filter(location -> (location instanceof WhereFoundOID))
						.map(location -> ((WhereFoundOID) location).getPk()).collect(Collectors.toList()).reversed();
				List<Long> workstationPks = ncrItemQueryFilter.getSources().stream()
						.filter(location -> (location instanceof WorkstationOID))
						.map(location -> ((WorkstationOID) location).getPk()).collect(Collectors.toList()).reversed();
				List<Integer> LocationPks = ncrItemQueryFilter.getSources().stream()
						.filter(location -> (location instanceof LocationTypeOID))
						.map(location -> ((LocationTypeOID) location).getPk()).collect(Collectors.toList()).reversed();
				if (ncrWhereFoundPks != null && ncrWhereFoundPks.size() > 0)
				{
					sqlQuery.append(" and ncr.sourceType = ? and ncr.sourcePk in ");
					params.add(EntityTypeEnum.WhereFoundType.name());
					String locationPkString = Arrays.deepToString(ncrWhereFoundPks.toArray());
					locationPkString = locationPkString.replace('[', '(');
					locationPkString = locationPkString.replace(']', ')');
					sqlQuery.append(locationPkString);
				}
				if (workstationPks != null && workstationPks.size() > 0)
				{
					sqlQuery.append(" and ncr.sourceType = ? and ncr.sourcePk in ");
					params.add(EntityTypeEnum.Workstation.name());
					String locationPkString = Arrays.deepToString(workstationPks.toArray());
					locationPkString = locationPkString.replace('[', '(');
					locationPkString = locationPkString.replace(']', ')');
					sqlQuery.append(locationPkString);
				}
				if (LocationPks != null && LocationPks.size() > 0)
				{
					sqlQuery.append(" and ncr.sourceType = ? and ncr.sourcePk in ");
					params.add(EntityTypeEnum.LocationType.name());
					String locationPkString = Arrays.deepToString(LocationPks.toArray());
					locationPkString = locationPkString.replace('[', '(');
					locationPkString = locationPkString.replace(']', ')');
					sqlQuery.append(locationPkString);
				}

			}
			
			if (ncrItemQueryFilter.getLocations() != null && ncrItemQueryFilter.getLocations().size() > 0)
			{
				List<Integer> ncrWhereFoundPks = ncrItemQueryFilter.getLocations().stream()
						.filter(location -> (location instanceof WhereFoundOID))
						.map(location -> ((WhereFoundOID) location).getPk()).collect(Collectors.toList()).reversed();
				List<Integer> workstationPks = ncrItemQueryFilter.getLocations().stream()
						.filter(location -> (location instanceof WorkstationOID))
						.map(location -> ((WorkstationOID) location).getPk()).collect(Collectors.toList());
				List<Integer> LocationPks = ncrItemQueryFilter.getLocations().stream()
						.filter(location -> (location instanceof LocationTypeOID))
						.map(location -> ((LocationTypeOID) location).getPk()).collect(Collectors.toList());
				if (ncrWhereFoundPks != null && ncrWhereFoundPks.size() > 0)
				{
					sqlQuery.append(" and ncr.locationType = ? and ncr.locationPk in ");
					params.add(EntityTypeEnum.WhereFoundType.name());
					String locationPkString = Arrays.deepToString(ncrWhereFoundPks.toArray());
					locationPkString = locationPkString.replace('[', '(');
					locationPkString = locationPkString.replace(']', ')');
					sqlQuery.append(locationPkString);
				}
				if (workstationPks != null && workstationPks.size() > 0)
				{
					sqlQuery.append(" and ncr.locationType = ? and ncr.locationPk in ");
					params.add(EntityTypeEnum.Workstation.name());
					String locationPkString = Arrays.deepToString(workstationPks.toArray());
					locationPkString = locationPkString.replace('[', '(');
					locationPkString = locationPkString.replace(']', ')');
					sqlQuery.append(locationPkString);
				}
				if (LocationPks != null && LocationPks.size() > 0)
				{
					sqlQuery.append(" and ncr.locationType = ? and ncr.locationPk in ");
					params.add(EntityTypeEnum.LocationType.name());
					String locationPkString = Arrays.deepToString(LocationPks.toArray());
					locationPkString = locationPkString.replace('[', '(');
					locationPkString = locationPkString.replace(']', ')');
					sqlQuery.append(locationPkString);
				}

			}
			
			if (ncrItemQueryFilter.getLocation() != null)
			{
				sqlQuery.append(" and ncr.locationType = ? and ncr.locationPk=?");

				if (ncrItemQueryFilter.getLocation() instanceof NcrWhereFoundBean)
				{
					NcrWhereFoundBean ncrWhereFoundBean = (NcrWhereFoundBean) ncrItemQueryFilter.getLocation();
					params.add(EntityTypeEnum.WhereFoundType.name());
					params.add(ncrWhereFoundBean.getPk());
				} else if (ncrItemQueryFilter.getLocation() instanceof WorkstationQuery)
				{
					WorkstationQuery workstationQuery = (WorkstationQuery) ncrItemQueryFilter.getLocation();
					params.add(EntityTypeEnum.Workstation.name());
					params.add(workstationQuery.getPk());
				} else if (ncrItemQueryFilter.getLocation() instanceof InjuryLocationMasterQuery)
				{
					InjuryLocationMasterQuery injuryLocationMasterQuery = (InjuryLocationMasterQuery) ncrItemQueryFilter
							.getLocation();
					params.add(EntityTypeEnum.LocationType.name());
					params.add(injuryLocationMasterQuery.getPk());
				}

			}
			if (ncrItemQueryFilter.getSource() != null)
			{
				sqlQuery.append(" and ncr.sourceType = ? and ncr.sourcePk=?");
				if (ncrItemQueryFilter.getSource() instanceof NcrWhereFoundBean)
				{
					NcrWhereFoundBean ncrWhereFoundBean = (NcrWhereFoundBean) ncrItemQueryFilter.getSource();
					params.add(EntityTypeEnum.WhereFoundType.name());
					params.add(ncrWhereFoundBean.getPk());
				} else if (ncrItemQueryFilter.getSource() instanceof WorkstationQuery)
				{
					WorkstationQuery workstationQuery = (WorkstationQuery) ncrItemQueryFilter.getSource();
					params.add(EntityTypeEnum.Workstation.name());
					params.add(workstationQuery.getPk());
				} else if (ncrItemQueryFilter.getSource() instanceof InjuryLocationMasterQuery)
				{
					InjuryLocationMasterQuery injuryLocationMasterQuery = (InjuryLocationMasterQuery) ncrItemQueryFilter
							.getSource();
					params.add(EntityTypeEnum.LocationType.name());
					params.add(injuryLocationMasterQuery.getPk());
				}
			}
			if (ncrItemQueryFilter.getReworkOrderOID() != null && ncrItemQueryFilter.getReworkOrderOID().getPk() != 0)
			{
				sqlQuery.append(" and ncr.reworkOrderFk = ?");
				params.add(ncrItemQueryFilter.getReworkOrderOID().getPk());
			}

			if (ncrItemQueryFilter.getPps8DRequired() != null && ncrItemQueryFilter.getPps8DRequired())
			{
				sqlQuery.append(" and ncr.ppsor8d =1 ");
			} else if (ncrItemQueryFilter.getPps8DRequired() != null && !ncrItemQueryFilter.getPps8DRequired())
			{
				sqlQuery.append(" and ncr.ppsor8d is NULL ");
			}

		}
		if (ncrItemQueryFilter.getIsPendingMyApproval() != null && ncrItemQueryFilter.getIsPendingMyApproval())
		{
			sqlQuery.append(" group by ncr.pk");
		}

		if (ncrItemQueryFilter.getGroupSetFilter() == NcrItemQueryFilter.GroupSet.DISPOSITION)
		{
			sqlQuery.append(" and  ncr.dispositionFk is not null and ncr.dispositionFk != 0");
		} else if (ncrItemQueryFilter.getGroupSetFilter() == NcrItemQueryFilter.GroupSet.REWORKORDER)
		{
			sqlQuery.append(" and rwo.reworkOrderNumber is not null");
		}
		if (ncrItemQueryFilter.getUnitFks() != null )
		{
			sqlQuery.append(" GROUP BY ncr.pk ");
		}
		if (ncrItemQueryFilter.getOrderByFilter() != null && ncrItemQueryFilter.getOrderByFilter().length != 0)
		{

			sqlQuery.append(" ORDER BY ");
			Integer commaflag = 0;
			for (String orderString : ncrItemQueryFilter.getOrderByFilter())
			{
				if (commaflag != 0)
					sqlQuery.append(",").append(orderString);
				else
				{
					sqlQuery.append(orderString);
					commaflag++;
				}
			}
		} else
		{
			sqlQuery.append(" ORDER BY ncr.createdDate DESC");
		}
		QueryObject conditionQuery = new QueryObject();
		conditionQuery.setQuery(sqlQuery.toString());
		conditionQuery.setParams(params);
		return conditionQuery;
	}

}
