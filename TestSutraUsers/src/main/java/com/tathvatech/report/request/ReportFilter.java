package com.tathvatech.report.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tathvatech.forms.common.TestProcFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({/* Uncomment later @JsonSubTypes.Type(value = OpenItemListReportFilter.class, name = "OpenItemListReportFilter"),
		@JsonSubTypes.Type(value = OpenItemStatusSummaryReportRequest.class, name = "OpenItemStatusSummaryReportFilter"),
		*/@JsonSubTypes.Type(value = TestProcFilter.class, name = "TestProcListReportFilter")/*,
		@JsonSubTypes.Type(value = TestProcStatusSummaryReportRequest.class, name = "TestProcStatusSummaryReportFilter"),
		@JsonSubTypes.Type(value = UserFilter.class, name = "UserListReportFilter"),
		@JsonSubTypes.Type(value = NcrItemListReportFilter.class, name = "NcrItemListReportFilter"),
		@JsonSubTypes.Type(value = NcrSummaryReportFilter.class, name = "NcrSummaryReportFilter"),
		@JsonSubTypes.Type(value = NcrCorrectiveActionReportFilter.class, name = "NcrCorrectiveActionReportFilter"),
		@JsonSubTypes.Type(value = PartListFilter.class, name = "PartListReportFilter"),
		@JsonSubTypes.Type(value = PurchaseOrderFilterNew.class, name = "PurchaseOrderListReportFilter"),
		@JsonSubTypes.Type(value = SupplierListReportFilter.class, name = "SupplierListReportFilter"),
		@JsonSubTypes.Type(value = ProjectWorkstationFilter.class, name = "ProjectWorkstationFilter"),
		@JsonSubTypes.Type(value = HazardMaintenanceQueryFilter.class, name = "HazardMaintenanceListReportFilter"),
		@JsonSubTypes.Type(value = InjuryReportQueryFilter.class, name = "InjuryReportQueryFilter"),
		@JsonSubTypes.Type(value = MrfQueryFilter.class, name = "MrfQueryFilter"),
		@JsonSubTypes.Type(value = NcrGroupQueryFilterNew.class, name = "NcrGroupQueryFilterNew"),
		@JsonSubTypes.Type(value = NearMissQueryFilter.class, name = "NearMissQueryFilter"),
		@JsonSubTypes.Type(value = NearMissSummaryReportRequest.class, name = "NearMissSummaryReportRequest"),
		@JsonSubTypes.Type(value = SupportTicketFilter.class, name = "SupportTicketFilter"),
		@JsonSubTypes.Type(value = EquipmentListFilter.class, name = "EquipmentListFilter"),
		@JsonSubTypes.Type(value = NcrUnitListRequest.class, name = "NcrUnitListRequest"),
		@JsonSubTypes.Type(value = AndonQueryFilter.class, name = "AndonQueryFilter")*/ })

public abstract class ReportFilter
{

}
