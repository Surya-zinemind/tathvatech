package com.tathvatech.common.enums;

public enum EntityTypeEnum implements EntityType {
	Form(1),
    Response(2),
    Project(3),
    Unit(4),
	HistoryBook(5),
	Mode(6),
	OpenItem(7),
	Andon(8), 
	FormItemResponse(9), 
	TestProc(10),
	Part(11),
	ProjectPart(12), 
	ProjectForm(13), 
	Workstation(14),
	Site(15),
	User(16),
	Injury(17),
	Suggestion(18),
	NCRGroup(19),
	NCR(20),
	InspectionRegisterItem(21),
	VCRItem(22),
	ProjectSiteConfig(23),
	Supplier(24),
	SupplierSiteMapping(25),
	Person(27),	
	UnitInProject(28),
	OpenItemSet(29),
	FormMain(30),
	FormSectionMain(31),
	FormSection(32),
	UnitWorkstation(33),
	SiteGroup(34),
	SupplierSiteGroupMapping(35),
	TestProcSection(36),
	SectionResponse(37),
	SupplierSiteGroupPartMapping(38),
	InspectionLineItem(39),
	CorrectiveAction(40),
	Usercompetency(41),
	Equipment(42),
	EquipmentType(43),
	EquipmentCalibration(44),
	Location(45),
	ResponseSubmissionBookmark(46),
	ProjectStage(47),
	ProjectSignatorySet(48),
	
	SupplierPartsMapping(110),
	PartInspectionConfig(210),
	PartInspectionInstruction(211),
	
	
	NCRFunction(301),
	LocationType(302),
	WhereFoundType(303),
	NcrUnitAssign(304),
	NcrCorrectiveAction(305),
	
	
	Workstation5SReport(400),
	Shift(401),
	WorkstationShift(402),
	ShiftInstance(403),
	WorkstationShiftInstance(404),
	
	PartRevision(1100),
	
	PurchaseOrder(1200),
	PurchaseOrderLineItem(1201),
	DeliveryDocket(1202),
	PORevision(1203),
	POLineItemRevision(1204),
	
	HazardNMaintenanceItem(1400),
	HazardImmediateActionRequiredType(1401),
	HazardCorrectiveAction(1402),
	
	WorkOrder(1500), 
	EntityWorkstage(1501),
	
	InjuryAfterTreatment(1600),
	
	MRF(1700),
	MRFPartAssign(1701),
	
	NearMiss(1800),
	NearMissAfterTreatment(1801),
	
	Job(30000),
	
	HelpArticle(50000),
	SupportTicket(50001);
	
	;

	EntityTypeEnum(int value)
	{
		this.value = value;
		EntityTypeEnumMap.getInstance().addEntityType(value, this);
	}
	
	public int getValue()
	{
		return value;
	}
	public static EntityType fromValue(int value)
	{
		return EntityTypeEnumMap.getInstance().fromValue(value);

//		EntityTypeEnum[] vs = EntityTypeEnum.values();
//		for (int i = 0; i < vs.length; i++) 
//		{
//			if(vs[i].value == value)
//			{
//				return vs[i];
//			}
//		}
//		return null;
	}
	
	private int value;
}
