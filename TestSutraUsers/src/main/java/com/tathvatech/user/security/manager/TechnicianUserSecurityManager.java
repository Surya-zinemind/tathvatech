package com.tathvatech.user.security.manager;

import java.util.List;

import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.security.config.SecurityCredential;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.PlanSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnicianUserSecurityManager extends PlanSecurityManager
{
	private static final Logger logger = LoggerFactory.getLogger(TechnicianUserSecurityManager.class);

//	protected TechnicianUserSecurityManager(AccountService accountService) {
//		super(accountService);
//	}

	public boolean checkAccess(int action, SecurityContext sContext)
	{
		User user = (User) userContext.getUser();

		if(action > 9999)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Accessing admin actions, Throwing AccessDeniedException");
			}
			return false;
		}

		switch (action) {
		case PlanSecurityManager.WORKSTATION_LIST:
		case PlanSecurityManager.FORM_ACCESS:
		case PlanSecurityManager.FORM_VERSION_DELETE:
		case PlanSecurityManager.FORM_CLOSE:
		case PlanSecurityManager.FORM_CREATE:
		case PlanSecurityManager.FORM_CHANGE_EFFECTIVE_DATE:
		case PlanSecurityManager.FORM_DESIGN:	
		case PlanSecurityManager.FORM_MAIN_DELETE:
		case PlanSecurityManager.FORM_NEW_VERSION:
		case PlanSecurityManager.FORM_PUBLISH:
		case PlanSecurityManager.FORM_MANAGE_SECTION_LOCKS:
			return false;
			/**
			 * need to add this
			 */

//		case PlanSecurityManager.FORM_PRINT:
//		try
//		{
//			Survey s = SurveyDelegate.getSurveyByPk(sContext.getFormOID().getPk());
//			if(s.getSuperseded() > 0)
//			{
//				return false;
//			}
//			return true;
//		}
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}

		case PlanSecurityManager.RESPONSE_EDIT_COMMENT_AND_ATTACHMENT:
			return true;
			
		case PlanSecurityManager.RESPONSE_REOPEN_APPROVED_FORM:
			return false;
		
		case PlanSecurityManager.UNIT_EDIT:
		case PlanSecurityManager.UNIT_CREATE:
		case PlanSecurityManager.UNIT_CREATE_CAR:
		case PlanSecurityManager.UNIT_CHANGE_WORKSTATION_SATUS:
		case PlanSecurityManager.UNIT_DELETE:
		case PlanSecurityManager.UNIT_OPEN_FROM_PLANNED:
		case PlanSecurityManager.UNIT_CAR_DELETE:
		case PlanSecurityManager.UNIT_MANAGE_CAR:
		case PlanSecurityManager.UNIT_MARK_COMPLETE:
		case PlanSecurityManager.UNIT_MANAGE_WORKSTATION:
			return false;
		case PlanSecurityManager.PROJECT_MANAGE_PART:
		case PlanSecurityManager.PROJECT_CHANGE_UNIT_PARENT:	
		case PlanSecurityManager.PROJECT_UPDATE_OIL_CUSTODIAN:
		case PlanSecurityManager.PROJECT_CREATE_CAR:
		case PlanSecurityManager.PROJECT_CAR_DELETE:
		case PlanSecurityManager.PROJECT_CAR_EDIT:
		case PlanSecurityManager.PROJECT_EDIT:
		case PlanSecurityManager.PROJECT_CREATE:
		case PlanSecurityManager.PROJECT_CLOSE:
 		case PlanSecurityManager.PROJECT_MANAGE_WORKSTATION:
 		case PlanSecurityManager.PROJECT_DELETE:
 		case PlanSecurityManager.PROJECT_MANAGE_SETTINGS:	
 		case PlanSecurityManager.PROJECT_CREATE_HISTORY_BOOK:
 		case PlanSecurityManager.PROJECT_GENERATE_ACCEPTANCE_CERTIFICATE:	
 		case PlanSecurityManager.PROJECT_DOWNLOAD_HISTORY_BOOK:	
 		case PlanSecurityManager.PROJECT_ADD_REMOVE_SITES:	
		case PlanSecurityManager.PROJECT_CONFIGURE_SITES:
			return false;

 		case PlanSecurityManager.USER_LIST:	
 		case PlanSecurityManager.USER_CHANGE_PASSWORD:
 		case PlanSecurityManager.USER_CREATE:
 		case PlanSecurityManager.USER_DELETE:
 		case PlanSecurityManager.USER_CHANGE_USER_TYPE:	
 		case PlanSecurityManager.USER_EDIT:
 		case PlanSecurityManager.USER_RESET_PASSWORD:
 		case PlanSecurityManager.MANAGE_ACTITY_LOG:	
 			return false;

 		case PlanSecurityManager.WORKSTATION_CREATE:
 		case PlanSecurityManager.WORKSTATION_EDIT:
 		case PlanSecurityManager.WORKSTATION_DELETE:
 			return false;

 		case PlanSecurityManager.PROJECT_PRINT_WORKSTATION_DASHBOARD:
 			return false;
 			
 		case PlanSecurityManager.PROJECT_REPORTS_DASHBOARD:	
 			return true;

			/**
			 * need to add this
			 */
// 		case PlanSecurityManager.MOD_DELETE_FROM_UNIIT:
// 			return super.checkAccessCommon(action, sContext);
 			
 		case PlanSecurityManager.MOD_DELETE:
 		case PlanSecurityManager.MOD_CLOSE:
 			return false;
			/**
			 * Need to add this
			 */

// 		case PlanSecurityManager.MOD_OPEN:
// 			try
// 			{
// 				Mode mode = PersistWrapper.readByPrimaryKey(Mode.class, sContext.getModeOID().getPk());
//				{
//					if(mode == null)
//						return false;
//					if(user.getPk() == mode.getOwnerPk())
//						return true;
//				}
// 			}
// 			catch(Exception e)
// 			{
// 				return false;
// 			}
//
 		case PlanSecurityManager.MOD_CREATE:
 		case PlanSecurityManager.MOD_PUSH_TO_UNIT:
 		case PlanSecurityManager.MOD_ACCESS:
 		case PlanSecurityManager.MOD_DASHBOARD_VIEW:
 		case PlanSecurityManager.MOD_COMPLETE_FROM_MASTER:
 			return true;

 		case PlanSecurityManager.MOD_CHANGE_OWNER:
 			return false;
 			
 		case PlanSecurityManager.PROJECT_ANDON_LIST_ACCESS:
 			return true;
 			
		case PlanSecurityManager.ANDON_DELETE:
		case PlanSecurityManager.ANDON_CREATE:
		case PlanSecurityManager.ANDON_UPDATE:
			return false;

		case PlanSecurityManager.OPENITEMLIST_CLEAR_FORM_ENTRIES:
 		case PlanSecurityManager.OPENITEMLIST_DELETEFORM:
 		case PlanSecurityManager.OPENITEM_FORMLEVEL_UPLOAD:
 		case PlanSecurityManager.OPENITEMLIST_CHANGE_FORM_STATUS:
 		case PlanSecurityManager.OPENITEMLIST_GLOBALUPLOAD:
 		case PlanSecurityManager.OPENITEMLIST_DELETE_ITEM:	
		case PlanSecurityManager.OPENITEM_MARK_EXTERNAL:
		case PlanSecurityManager.OPENITEM_REOPEN_CLOSED:
 			return false;

 		case PlanSecurityManager.OPENITEMLIST_CREATE:
 		case PlanSecurityManager.OPENITEM_FORMLEVEL_DOWNLOAD:
 		case PlanSecurityManager.OPENITEMLIST_GLOBALDOWNLOAD:
 		case PlanSecurityManager.OPENITEMLIST_DASHBOARD:
		case PlanSecurityManager.OPENITEMLIST_GLOBALVIEW:	
 		case PlanSecurityManager.OPENITEM_CREATE:
			/**
			 * Need tp add this
			 */
//		case PlanSecurityManager.OPENITEMLIST_VIEW:
//			try
//			{
//				List us = ProjectDelegate.getUsersForUnitInRole(sContext.getUnitOID().getPk(), sContext.getProjectOID(), DummyWorkstation.getOID(), User.ROLE_TESTER);
//				if(us.contains(user))
//				{
//					return true;
//				}
//				us = ProjectDelegate.getUsersForUnitInRole(sContext.getUnitOID().getPk(), sContext.getProjectOID(), DummyWorkstation.getOID(), User.ROLE_VERIFY);
//				if(us.contains(user))
//				{
//					return true;
//				}
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				return false;
//			}
//			return false;

			
		default:
			return true;
		}

	}

	@Override
	public void setSecurityCredientials(SecurityCredential credential)
	{
		// nothing to be done here
	}
}
