package com.tathvatech.user.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.common.RoleRepository;

public enum SiteRolesEnum implements Role
{
	SiteAdmin("SiteAdmin", "Site Admin", "Site Admin"),
	SiteServiceCoordinator("SiteServiceCoordinator", "Site Service Coordinator", "Site Service Coordinator"),
	SuggestionSchemeCoordinator("SuggestionSchemeCoordinator", "Suggestion Scheme Coordinator", "Suggestion Scheme Coordinator"),
	HSECoordinator("HSECoordinator", "Hazard and Safety Coordinator", "Hazard and Safety Coordinator"),
	HSEDirector("HSEDirector", "Hazard and Safety Director", "Hazard and Safety Director"),
	NCRUser("NCRUser", "NCR Access User", "NCR Access User"),
	VCRUser("VCRUser", "VCR Access User", "VCR Access User"),
	InspectionRegisterUser("InspectionRegisterUser", "InspectionRegister Access User", "InspectionRegister Access User"),
	NCGUser("NCGUser", "NCG Export Access User", "NCG Export Access User"),
	SupervisorForVerification("SupervisorForVerification", "Supervisor for Verification", "Supervisor for Verification", true),
	SiteStatusReportUser("SiteStatusReportUser", "Site Status report access user", "Site Status report access user"),
	CalibrationCoordinator("CalibrationCoordinator", "Calibration Coordinator", "Calibration Coordinator");
	
	String id = null;
	String name = null;
	String description = null;
	boolean getUsersWithEmailOnly = false;
	
	private SiteRolesEnum(String id, String name, String description) 
	{
		this(id, name, description, false);
	}
	
	private SiteRolesEnum(String id, String name, String description, boolean getUsersWithEmailOnly) 
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.getUsersWithEmailOnly = getUsersWithEmailOnly;
		
		RoleRepository.getInstance().registerSystemRole(this, EntityTypeEnum.Site);
	}

	public Collection<? extends javax.swing.Action> getAllowedActions()
	{
		if(this == SiteRolesEnum.SiteAdmin)
			return Arrays.asList(new Action[]{SiteActionsEnum.ResetUserPassword});
		else if(this == SiteRolesEnum.SuggestionSchemeCoordinator)
			return Arrays.asList(new Action[]{SiteActionsEnum.AcknowlegeSuggestion, SiteActionsEnum.ViewAllSuggestions, SiteActionsEnum.CloseSuggestion});
		else if(this == SiteRolesEnum.HSECoordinator)
			return Arrays.asList(new Action[]{SiteActionsEnum.AcknowlegeIncident, SiteActionsEnum.ViewAllIncidents, SiteActionsEnum.CloseIncident});
		else if(this == SiteRolesEnum.HSEDirector)
			return Arrays.asList(new Action[]{SiteActionsEnum.ViewAllIncidents});
		else
			return new ArrayList();
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
		return null;
	}
	
	@Override
	public String[] getAllowedUserTypes()
	{
		return null;
	}

	@Override
	public boolean getUsersWithEmailOnly()
	{
		return getUsersWithEmailOnly;
	}
}
