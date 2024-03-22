package com.tathvatech.report.enums;

public enum ReportTypes {
	HistoryBook,
	UserListReport,
	FormListReport, 
	NcrItemListReport,
	PartsListReport,
	PurchaseOrderListReport,
	POListReportProcurement, //This is the new po report inside the procurement module
	SupplierListReport,
	SupplierSiteGroupListReport,
	SupplierSiteGroupPartListReport,
	OpenItemListReport,
	TestProcListReport,
	HazardAndMaintenanceListReport,
	InjuryListReport,
	InjurySymmaryReport,
	NearMissListReport,
	NearMissSummaryReport,
	MRFListReport,
	MRFSummaryReport,
	InspectionRegisterListReport,
	InspectedPartListReport,
	InspectionLineItemListReport,
	WorkOrderListReport,
	HelpTopicListReport,
	AndonListReport,
	AndonSummaryReport,
	NcrCorrectionActionListReport,
	NcrCorrectiveActionSummaryReport,
	VCRListReport,
	VCRSummaryReport,
	NCRGroupListReport,
	SuppoerTicketListReport,
	CorrectionActionListReport,
	HMListReportView,
	HMSummaryReportView,
	EquipmentList,
	EquipmentSummaryReport,
	NcrUnitAssignListReport,
	SuggestionListReport,
	SuggestionSummaryReport
	;
	
	public String getValue()
	{
		return toString();
	}
}
