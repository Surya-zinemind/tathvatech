package com.tathvatech.user.security.manager;

import java.util.List;

import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.security.config.SecurityCredential;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.PlanSecurityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuestUserSecurityManager extends PlanSecurityManager {
	private static final Logger logger = LoggerFactory.getLogger(GuestUserSecurityManager.class);

//	protected GuestUserSecurityManager(AccountService accountService) {
//		super(accountService);
//	}

	public boolean checkAccess(int action, SecurityContext sContext)
	{
		User user = (User) userContext.getUser();
		switch (action) 
		{
			case PlanSecurityManager.RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS:
			case PlanSecurityManager.PROJECT_REPORTS_DASHBOARD:
				return true;
/**
 * Neeed to add this
 */
//			case PlanSecurityManager.FORM_PRINT:
//				try
//				{
//					Survey s = SurveyDelegate.getSurveyByPk(sContext.getFormOID().getPk());
//					if(s.getSuperseded() > 0)
//					{
//						return false;
//					}
//					if(Survey.STATUS_CLOSED.equals(s.getStatus()) || Survey.STATUS_DELETED.equals(s.getStatus()))
//					{
//						return false;
//					}
//					return true;
//				}
//				catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return false;
//				}
//
//	 		case PlanSecurityManager.MOD_ACCESS:
//	 		case PlanSecurityManager.PROJECT_ANDON_LIST_ACCESS:
//				try
//				{
//					List<User> clerkACList = AccountDelegate.getACLs(sContext.getProjectOID().getPk(), UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_DATACLERK);
//					if(clerkACList.contains(user))
//					{
//						return true;
//					}
//					List<User> oilUsers = ProjectDelegate.getUsersForProject(sContext.getProjectOID().getPk(), DummyWorkstation.getOID());
//					if(oilUsers.contains(user))
//					{
//						return true;
//					}
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//					return false;
//				}
//				return false;
//
//	 		case PlanSecurityManager.MOD_DELETE_FROM_UNIIT:
//	 			return super.checkAccessCommon(action, sContext);
//
//
//	 		case PlanSecurityManager.MOD_CREATE:
//	 		case PlanSecurityManager.MOD_PUSH_TO_UNIT:
//	 		case PlanSecurityManager.ANDON_UPDATE:
//				try
//				{
//					List<User> clerkACList = AccountDelegate.getACLs(sContext.getProjectOID().getPk(), UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_DATACLERK);
//					if(clerkACList.contains(user))
//					{
//						return true;
//					}
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//					return false;
//				}
//				return false;
//
//	 		case PlanSecurityManager.MOD_OPEN:
//	 			try
//	 			{
//	 				Mode mode = PersistWrapper.readByPrimaryKey(Mode.class, sContext.getModeOID().getPk());
//					{
//						if(mode == null)
//							return false;
//						if(user.getPk() == mode.getOwnerPk())
//							return true;
//					}
//	 			}
//	 			catch(Exception e)
//	 			{
//	 				return false;
//	 			}
//
//
//	 		case PlanSecurityManager.UNIT_ANDON_LIST_ACCESS:
//				try
//				{
//					List<User> clerkACList = AccountDelegate.getACLs(sContext.getUnitOID().getPk(), UserPerms.OBJECTTYPE_UNIT, UserPerms.ROLE_DATACLERK);
//					if(clerkACList.contains(user))
//					{
//						return true;
//					}
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//					return false;
//				}
//				return false;

	 		case PlanSecurityManager.ANDON_CREATE:
	 		case PlanSecurityManager.ANDON_DELETE:
	 			return false;
			/**
			 * Neeed to add this
			 */
//	 		case PlanSecurityManager.OPENITEM_FORMLEVEL_DOWNLOAD:
//			case PlanSecurityManager.OPENITEMLIST_GLOBALDOWNLOAD:
//			case PlanSecurityManager.OPENITEMLIST_DASHBOARD:
//			case PlanSecurityManager.OPENITEMLIST_GLOBALVIEW:
//			case PlanSecurityManager.OPENITEMLIST_VIEW:
//				try
//				{
//					UnitObj unit = ProjectDelegate.getUnitByPk(sContext.getUnitOID());
//					List us = ProjectDelegate.getUsersForUnitInRole(unit.getPk(), sContext.getProjectOID(), DummyWorkstation.getOID(), User.ROLE_READONLY);
//					if(us.contains(user))
//					{
//						return true;
//					}
//					UnitObj u = ProjectDelegate.getUnitByPk(sContext.getUnitOID());
//					List<User> clerkACList = AccountDelegate.getACLs(sContext.getProjectOID().getPk(), UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_DATACLERK);
//					if(clerkACList.contains(user))
//					{
//						return true;
//					}
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//					return false;
//				}
//				return false;
//
//			case PlanSecurityManager.OPENITEM_CREATE:
//				try
//				{
//					UnitObj u = ProjectDelegate.getUnitByPk(sContext.getUnitOID());
//					List<User> clerkACList = AccountDelegate.getACLs(sContext.getProjectOID().getPk(), UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_DATACLERK);
//					if(clerkACList.contains(user))
//					{
//						return true;
//					}
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//					return false;
//				}
//				return false;
			
			
			default:
				return false;
		}
	}

	@Override
	public void setSecurityCredientials(SecurityCredential credential) {
		// nothing to be done here
	}
}
