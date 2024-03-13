package com.tathvatech.site.enums;

import java.util.Arrays;
import java.util.List;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.common.RoleRepository;

public enum ProjectSiteConfigRolesEnum implements Role
{
	ProjectProductManagement("ProjectProductManagement","Project / Product Management","Project / Product Management", "FunctionRole"),
	Engineering("Engineering","Engineering","Engineering", "FunctionRole"),
	MethodsProcessEngineering("MethodsProcessEngineering","Methods / Process Engineering","Methods / Process Engineering", "FunctionRole"),
	SupplyChain("SupplyChain","Supply chain","Supply chain", "FunctionRole"), //renamed as on Oct 7th 2021
	Sourcing("Sourcing","Sourcing","Sourcing", "FunctionRole"),
	PlanningControl("PlanningControl","Planning / Control","Planning / Control", "FunctionRole"),
	QualityAssurance("QualityAssurance","Quality Assurance","Quality Assurance", "FunctionRole"),
	Warehouse("Warehouse","Warehouse","Warehouse", "FunctionRole"),
	InternalTransport("InternalTransport","Logistics/ Internal Transport","Logistics/ Internal Transport", "FunctionRole"),
	ComponentProduction("ComponentProduction","Component Production","Component Production", "FunctionRole"),
	Weldingshop("Weldingshop","Welding Assurance","Welding Assurance", "FunctionRole"),
	Carbody("Carbody","Carbody","Carbody", "FunctionRole"),
	Paintshop("Paintshop","Paintshop","Paintshop", "FunctionRole"),
	PreAssembly("PreAssembly","Pre Assembly","Pre Assembly", "FunctionRole"),
	FinalAssembly("FinalAssembly","Final Assembly","Final Assembly", "FunctionRole"),
	Retrofit("Retrofit","Retrofit","Retrofit", "FunctionRole"),
	Refurbishment("Refurbishment","Refurbishment","Refurbishment", "FunctionRole"),
	Testing("Testing","Testing","Testing", "FunctionRole"),
	ShippingToCustomer("ShippingToCustomer","Shipping to customer","Shipping to customer", "FunctionRole"),
	ProductIntrodution("ProductIntrodution","Product Introduction","Product Introduction", "FunctionRole"),
	FieldMaintenanceService("FieldMaintenanceService","Field Maintenance / Service","Field Maintenance / Service", "FunctionRole"),
	SupplierQualityAssurance("SupplierQualityAssurance","Supplier Quality Assurance","Supplier Quality Assurance", "FunctionRole"),
	Finance("Finance","Finance","Finance", "FunctionRole"),
	SiteGM("SiteGM","Site GM","Site GM", "FunctionRole"),
	Installation("Installation","Installation","Installation", "FunctionRole"),
	
	// Special process 
	SP_Welding("A_Welding", "Special Process: Welding", "Special Process: Welding", "SpecialProcessRole"),
	SP_BondingSealing("B_BondingSealing", "Special Process: Bonding and Sealing", "Special Process: Bonding and Sealing", "SpecialProcessRole"),
	SP_SurfaceTreatment("C_SurfaceTreatment", "Special Process: Surface Treatment", "Special Process: Surface Treatment", "SpecialProcessRole"),
	SP_ElectricConnection("D_ElectricalConnection", "Special Process: Electrical Connection", "Special Process: Electrical Connection", "SpecialProcessRole"),
	SP_TorqueTighten("E_TorqueTighten", "Special Process: Torque tightening / bolt tensioning", "Special Process: Torque tightening / bolt tensioning", "SpecialProcessRole"),
	SP_Riveting("F_Riveting", "Special Process: Riveting", "Special Process: Riveting", "SpecialProcessRole"),
	SP_Casting("G_Casting", "Special Process: Casting", "Special Process: Casting", "SpecialProcessRole"),
	SP_Forging("H_Forging", "Special Process: Forging", "Special Process: Forging", "SpecialProcessRole"),
	SP_HeatTreatment("I_HeatTreatment", "Special Process: Heat Treatment", "Special Process: Heat Treatment", "SpecialProcessRole"),
	SP_Moulding("J_Moulding", "Special Process: Moulding (Plastic)", "Special Process: Moulding (Plastic)", "SpecialProcessRole"),
	SP_Laminating("K_Laminating", "Special Process: Laminating (Composites)", "Special Process: Laminating (Composites)", "SpecialProcessRole"),
	SP_ForceShrinkFitting("L_ForceshrinkFitting", "Special Process: Force Fitting or Shrink Fitting", "Special Process: Force Fitting or Shrink Fitting", "SpecialProcessRole"),
	SP_PottingImpregnation("M_PottingImpregnation", "Special Process: Potting / Impregnation", "Special Process: Potting / Impregnation", "SpecialProcessRole"),
	SP_StressReliefTreatment("N_StressReliefTreatment", "Special Process: Stress Relief Treatment", "Special Process: Stress Relief Treatment", "SpecialProcessRole"),
	SP_3DPrinting("O_3DPrinting", "Special Process: 3D Printing", "Special Process: 3D Printing", "SpecialProcessRole"),
	SP_Sintering("P_Sintering", "Special Process: Sintering", "Special Process: Sintering", "SpecialProcessRole"),
	;
	
	String id = null;
	String name = null;
	String description = null;
	String roleType = null;
	
	private ProjectSiteConfigRolesEnum(String id, String name, String description) 
	{
		this(id, name, description, null);
	}
	
	private ProjectSiteConfigRolesEnum(String id, String name, String description, String roleType) 
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.roleType = roleType;
		
		RoleRepository.getInstance().registerSystemRole(this, EntityTypeEnum.ProjectSiteConfig);
	}

	public List<Action> getAllowedActions()
	{
		return Arrays.asList(new Action[]{ProjectSiteConfigActionsEnum.ApproveNCRDisposition, ProjectSiteConfigActionsEnum.RejectNCRDisposition});
	}
	
	
	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getDescription()
	{
		return description;
	}

	@Override
	public String getRoleType()
	{
		return roleType;
	}
	
	@Override
	public String[] getAllowedUserTypes()
	{
		return null;
	}
	
	@Override
	public boolean getUsersWithEmailOnly()
	{
		return false;
	}

}
