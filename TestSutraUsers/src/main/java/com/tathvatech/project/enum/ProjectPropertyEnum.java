package com.tathvatech.project;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.common.RoleRepository;

import java.util.ArrayList;
import java.util.List;



public enum ProjectPropertyEnum implements Role
{
	//TODO Change the name of this enum here and in the DB at the same time.. search in the code base and change the comment where ever it is mentioned 
	SetNewWorkstationsDefaultStatusTo("SetNewWorkstationsDefaultStatusTo", "Change the worksation status to this status set in the DB when a workstation is created under a unit.", 
			"Its Waiting if no value is set,. Can set it to 'In Progress' or 'Completed'"),
	MakeTestNamesForChecksheetsMandatory("MakeTestNamesForChecksheetsMandatory", "Make test names mandatory for forms","Make test names mandatory for forms"),
	EnableSimpleUnitCreation("EnableSimpleUnitCreation", "Enable a simple unit create option by selecting a part type from the unit list screen","Enable a simple unit create option by selecting a part type from the unit list screen"),
	EnableSimpleAddFormToUnitOption("EnableSimpleAddFormToUnitOption", "Enable a simple option to add a form to a unit.","Enable a simple option to add a form to a unit."),
	UnitListDisplayColumns("UnitListDisplayColumns", "Columns to display on the Unit List page", "Columns to display on the Unit List page"),

	TestListDisplayColumns("TestListDisplayColumns", "Columns to display on the Test List page", "Columns to display on the Test List page"),
	
	TestDataExportColumns("TestDataExportColumns", "Data to include in test data export for each checksheet", "Data to include in test data export for each checksheet"),
	
	EnableInterimSubmitForChecksheets("EnableInterimSubmitForChecksheets", "", ""), // Default is false. If enabled, tester will be able to do an intrim submit when filling out forms. 
    SaveResponseStateOnFormSubmit("SaveResponseStateOnFormSubmit", "", ""), // Default is false. If enabled, When an form submit or interim submit is done, the current state of the response is saved as a history record.  
    CreateRevisionNoForFormSubmit("CreateRevisionNoForFormSubmit", "", ""), // Default is false. If enabled, When an form submitis done, revision numbers are generated.  
	
	RestrictUnitFormListToProjectPartLevelSetting("RestrictUnitFormListToProjectPartLevelSetting", "", ""), //If this is set, AddForm on a unit will only show forms that are set at the project level for that project part.
	DisableProjectFormCopyToUnits("DisableProjectFormCopyToUnits", "", ""), //Dont copy project forms to units when creating/opening a new unit.
	DisableProjectUsersCopyToUnit("DisableProjectUsersCopyToUnit", "", ""), //Dont copy project users to units when creating/opening a new unit.
	TreatFailsAsNotAnswered("TreatFailsAsNotAnswered", "", ""), //If set to true; if fail is selected, then it is treated as not answered and does not count towards % completion
	
	
	OpenItemNumberingBasedOn("OpenItemNumberingBasedOn", "", ""), //Denotes how the openItem sequence number should be based on . Currently supports only OpenItemSet
	OpenItemNumberingPrefix("OpenItemNumberingPrefix", "", ""); // The Prefix to use for OpenItemNumbering when the OpenItemNumberingBasedOn property is set. Default is based ont he username and a running seq.
		
	String id = null;
	String name = null;
	String description = null;
	
	private ProjectPropertyEnum(String id, String name, String description) 
	{
		this.id = id;
		this.name = name;
		this.description = description;
		
		RoleRepository.getInstance().registerSystemRole(this, EntityTypeEnum.Project);
	}
	
	public List<Action> getAllowedActions()
	{
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


	public String getRoleType()
	{
		return null;
	}


	public String[] getAllowedUserTypes()
	{
		return null;
	}
	

	public boolean getUsersWithEmailOnly()
	{
		return false;
	}
}
