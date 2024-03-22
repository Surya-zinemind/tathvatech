package com.tathvatech.unit.enums;


import com.tathvatech.common.enums.BaseActions;
import com.tathvatech.user.service.PlanSecurityManager;

public enum Actions implements BaseActions {

	createUnit(PlanSecurityManager.UNIT_CREATE, "Create Unit"),
	updateUnit(PlanSecurityManager.UNIT_EDIT, "Update Unit"),
	deleteUnit(PlanSecurityManager.UNIT_EDIT, "Update Unit"),
	addUnitToProject(PlanSecurityManager.ADD_UNIT_TO_PROJECT, "Add Unit to Project"),
	removeUnitFromProject(PlanSecurityManager.REMOVE_UNIT_FROM_PROJECT, "Remove Unit from Project"),
	changeUnitParent(PlanSecurityManager.CHANGE_UNIT_PARENT, "Change Unit Parent"),
	changeUnitOrder(PlanSecurityManager.CHANGE_UNIT_ORDER, "Change Unit Order"),
	openUnitInProject(PlanSecurityManager.OPEN_UNIT_IN_PROJECT, "Open Unit"),
	closeUnitInProject(PlanSecurityManager.CLOSE_UNIT_IN_PROJECT, "Close Unit"),
	

	saveSection(PlanSecurityManager.RESPONSE_SECTION_SAVE, "Save Section"),
	saveForm(PlanSecurityManager.RESPONSE_SAVE, "Save Form"),
	submitForm(PlanSecurityManager.RESPONSE_SUBMIT, "Submit Form"),
	verifyForm(PlanSecurityManager.RESPONSE_VERIFY, "Verify Form"),
	approveForm(PlanSecurityManager.RESPONSE_APPROVE, "Approve Form"),
	rejectVerifyForm(PlanSecurityManager.RESPONSE_REJECT_VERIFICATION, "Reject Form Verification"),
	rejectApproveForm(PlanSecurityManager.RESPONSE_REJECT_APPROVAL, "Reject Form Approval"),
	lockSection(PlanSecurityManager.RESPONSE_SECTION_LOCK, "Lock Section"),
	unlockSection(PlanSecurityManager.RESPONSE_SECTION_UNLOCK, "Unlock Section"),
	ncrNcgExport(PlanSecurityManager.NCR_NCG_EXPORT, "Export NCR Data for NCG"),
	approveNewUserRequest(PlanSecurityManager.USER_APPROVE_NEWUSER_REQUEST, "Approve new user request"),
	rejectNewUserRequest(PlanSecurityManager.USER_REJECT_NEWUSER_REQUEST, "Reject new user request"),
	
	saveMRF(PlanSecurityManager.RESPONSE_SECTION_SAVE, "Save MRF"),
	saveNearMiss(PlanSecurityManager.RESPONSE_SECTION_SAVE, "Save Near Miss"),
	saveHelpTopic(PlanSecurityManager.RESPONSE_SECTION_SAVE, "Save HelpTopic"),
	savePushNotification(PlanSecurityManager.RESPONSE_SECTION_SAVE, "Save PushNotification"),
	saveTicketManagement(PlanSecurityManager.RESPONSE_SAVE, "Save Ticket Management"),

	
	
	;
	
	private int value;
	private String actionName;
	Actions(int actionId, String actionName)
	{
		this.value = actionId;
		this.actionName = actionName;
	}

	public int value()
	{
		return value;
	}
	
	public String getActionName()
	{
		return actionName;
	}
	
	public static Actions fromValue(int value)
	{
		Actions[] vals = Actions.values();
		for (int i = 0; i < vals.length; i++) 
		{
			if(vals[i].value == value)
			{
				return vals[i];
			}
		}
		return null;
	}
}
