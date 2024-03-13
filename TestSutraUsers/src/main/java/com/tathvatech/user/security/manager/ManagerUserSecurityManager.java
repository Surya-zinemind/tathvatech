package com.tathvatech.user.security.manager;

import java.util.Iterator;
import java.util.List;

import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.security.config.SecurityCredential;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.PlanSecurityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerUserSecurityManager extends PlanSecurityManager {
	private static final Logger logger = LoggerFactory.getLogger(ManagerUserSecurityManager.class);

//	protected ManagerUserSecurityManager(AccountService accountService) {
//		super(accountService);
//	}

	public boolean checkAccess(int action, SecurityContext sContext)
	{
		switch (action) 
		{
			case PlanSecurityManager.PROJECT_MANAGE_PART:
			case PlanSecurityManager.PROJECT_ADD_REMOVE_SITES:
				/**
				 * Neeed to add this
				 */
//			case PlanSecurityManager.PROJECT_UPDATE_OIL_CUSTODIAN:
//				if(sContext.getProjectOID() == null)
//				return false;
//				try
//				{
//					Project  p = PersistWrapper.readByPrimaryKey(Project.class, sContext.getProjectOID().getPk());
//					if(p != null && p.getManagerPk() != null && p.getManagerPk() == this.userContext.getUser().getPk())
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
				
			case PlanSecurityManager.PROJECT_EDIT:
			case PlanSecurityManager.PROJECT_CLOSE:
			case PlanSecurityManager.PROJECT_OPEN:
			case PlanSecurityManager.PROJECT_MANAGE_WORKSTATION:
			case PlanSecurityManager.PROJECT_CREATE_CAR:
			case PlanSecurityManager.PROJECT_CAR_EDIT:
			case PlanSecurityManager.PROJECT_CAR_DELETE:
			case PlanSecurityManager.PROJECT_MANAGE_SETTINGS:
			case PlanSecurityManager.PROJECT_PRINT_WORKSTATION_DASHBOARD:
	
	 		case PlanSecurityManager.UNIT_CREATE:
			case PlanSecurityManager.UNIT_EDIT:
			case PlanSecurityManager.UNIT_DELETE:
			case PlanSecurityManager.UNIT_OPEN_FROM_PLANNED:	
			case PlanSecurityManager.UNIT_OPEN_UNIT:
			case PlanSecurityManager.UNIT_MARK_COMPLETE:
			case PlanSecurityManager.UNIT_MANAGE_WORKSTATION:
			case PlanSecurityManager.UNIT_CREATE_CAR:
			case PlanSecurityManager.UNIT_CAR_DELETE:
			case PlanSecurityManager.UNIT_CAR_EDIT:
			case PlanSecurityManager.UNIT_MANAGE_CAR:
			case PlanSecurityManager.UNIT_CHANGE_WORKSTATION_SATUS:
			case PlanSecurityManager.FORM_MANAGE_SECTION_LOCKS:
			case PlanSecurityManager.PROJECT_CHANGE_UNIT_PARENT:
				/**
				 * Neeed to add this
				 */
//	 		case PlanSecurityManager.PROJECT_CONFIGURE_SITES:
//				if(sContext.getProjectOID() == null)
//				return false;
//
//			try
//			{
//				Project  p = PersistWrapper.readByPrimaryKey(Project.class, sContext.getProjectOID().getPk());
//				if(p != null && p.getManagerPk() != null && p.getManagerPk() == this.userContext.getUser().getPk())
//				{
//					return true;
//				}
//
//				List<UserPerms> l = PersistWrapper.readList(UserPerms.class, "select * from TAB_USER_PERMS where objectPk=? and objectType=? " +
//						"and userPk=? ", sContext.getProjectOID().getPk(), UserPerms.OBJECTTYPE_PROJECT, this.userContext.getUser().getPk());
//				for (Iterator iterator = l.iterator(); iterator.hasNext();)
//				{
//					UserPerms userPerms = (UserPerms) iterator.next();
//					if(UserPerms.ROLE_MANAGER.equals(userPerms.getRole()))
//					{
//						return true;
//					}
//				}
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				return false;
//			}
//			return false;
				/**
				 * Neeed to add this
				 */
			
//	 		case PlanSecurityManager.OPENITEMLIST_CLEAR_FORM_ENTRIES:
//	 			return false;
//
//	 		case PlanSecurityManager.MOD_ACCESS:
//	 		case PlanSecurityManager.MOD_DASHBOARD_VIEW:
//	 			return true;
//
//	 		case PlanSecurityManager.ANDON_DELETE:
//	 			return false;
//			/**
//			 * Neeed to add this
//			 */
//	 		case PlanSecurityManager.MOD_DELETE_FROM_UNIIT:
//	 			return super.checkAccessCommon(action, sContext);
//
//	 		case PlanSecurityManager.MOD_OPEN:
//	 		case PlanSecurityManager.MOD_CREATE:
//	 		case PlanSecurityManager.MOD_PUSH_TO_UNIT:
//	 		case PlanSecurityManager.MOD_DELETE:
//	 		case PlanSecurityManager.MOD_CLOSE:
//	 		case PlanSecurityManager.MOD_COMPLETE_FROM_MASTER:
//	 		case PlanSecurityManager.MOD_CHANGE_OWNER:
//	 		case PlanSecurityManager.PROJECT_CREATE_HISTORY_BOOK:
//	 		case PlanSecurityManager.PROJECT_GENERATE_ACCEPTANCE_CERTIFICATE:
//	 		case PlanSecurityManager.PROJECT_DOWNLOAD_HISTORY_BOOK:
//	 		case PlanSecurityManager.ANDON_UPDATE:
//	 		case PlanSecurityManager.ANDON_CREATE:
//				if(sContext.getProjectOID() == null)
//				return false;
//
//				try
//				{
//					ProjectQuery  p = ProjectDelegate.getProjectQueryByPk(sContext.getProjectOID().getPk());
//		 			List<User> coordinators = ProjectDelegate.getProjectManagers(p.getOID());
//		 			if(coordinators.contains(userContext.getUser()))
//		 			{
//		 				return true;
//		 			}
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//					return false;
//				}
//				return false;
//
//	 		case PlanSecurityManager.PROJECT_ANDON_LIST_ACCESS:
//	 			return true;
//
//
//	 		case PlanSecurityManager.OPENITEMLIST_DELETEFORM:
//	 		case PlanSecurityManager.OPENITEM_FORMLEVEL_DOWNLOAD:
//	 		case PlanSecurityManager.OPENITEM_FORMLEVEL_UPLOAD:
//	 		case PlanSecurityManager.OPENITEMLIST_CHANGE_FORM_STATUS:
//	 		case PlanSecurityManager.OPENITEMLIST_GLOBALDOWNLOAD:
//	 		case PlanSecurityManager.OPENITEMLIST_GLOBALUPLOAD:
//			case PlanSecurityManager.OPENITEMLIST_DASHBOARD:
//			case PlanSecurityManager.OPENITEMLIST_GLOBALVIEW:
//			case PlanSecurityManager.OPENITEM_CREATE:
//			case PlanSecurityManager.OPENITEMLIST_CREATE:
//			case PlanSecurityManager.OPENITEMLIST_VIEW:
//			case PlanSecurityManager.OPENITEM_REOPEN_CLOSED:
//				try
//				{
//					Unit unit = PersistWrapper.readByPrimaryKey(Unit.class, sContext.getUnitOID().getPk());
//					Project  p = PersistWrapper.readByPrimaryKey(Project.class, sContext.getProjectOID().getPk());
//
//					if(p != null && p.getManagerPk() != null && p.getManagerPk() == this.userContext.getUser().getPk())
//					{
//						return true;
//					}
//
//					List<UserPerms> l = PersistWrapper.readList(UserPerms.class, "select * from TAB_USER_PERMS where objectPk=? and objectType=? " +
//							"and userPk=? ", sContext.getUnitOID().getPk(), UserPerms.OBJECTTYPE_UNIT, this.userContext.getUser().getPk());
//					l.addAll(PersistWrapper.readList(UserPerms.class, "select * from TAB_USER_PERMS where objectPk=? and objectType=? " +
//							"and userPk=? ", p.getPk(), UserPerms.OBJECTTYPE_PROJECT, this.userContext.getUser().getPk()));
//					for (Iterator iterator = l.iterator(); iterator.hasNext();)
//					{
//						UserPerms userPerms = (UserPerms) iterator.next();
//						if(UserPerms.ROLE_MANAGER.equals(userPerms.getRole()) || UserPerms.ROLE_READONLY.equals(userPerms.getRole()))
//						{
//							return true;
//						}
//					}
//
//					User user = (User) userContext.getUser();
//					List us = ProjectDelegate.getUsersForUnitInRole(sContext.getUnitOID().getPk(), sContext.getProjectOID(), DummyWorkstation.getOID(), User.ROLE_TESTER);
//					if(us.contains(user))
//					{
//						return true;
//					}
//					us = ProjectDelegate.getUsersForUnitInRole(sContext.getUnitOID().getPk(), sContext.getProjectOID(), DummyWorkstation.getOID(), User.ROLE_VERIFY);
//					if(us.contains(user))
//					{
//						return true;
//					}
//					us = ProjectDelegate.getUsersForUnitInRole(sContext.getUnitOID().getPk(), sContext.getProjectOID(), DummyWorkstation.getOID(), User.ROLE_READONLY);
//					if(us.contains(user))
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
//			case PlanSecurityManager.OPENITEM_MARK_EXTERNAL:
//				try
//				{
//					Project p = PersistWrapper.readByPrimaryKey(Project.class, sContext.getProjectOID().getPk());
//
//					if(p != null && p.getManagerPk() != null && p.getManagerPk() == this.userContext.getUser().getPk())
//					{
//						return true;
//					}
//					List<UserPerms> l = PersistWrapper.readList(UserPerms.class, "select * from TAB_USER_PERMS where objectPk=? and objectType=? " +
//							"and userPk=? and role in (?, ?)", p.getPk(), UserPerms.OBJECTTYPE_PROJECT, this.userContext.getUser().getPk(),
//							UserPerms.ROLE_MANAGER, UserPerms.ROLE_READONLY);
//					if(l.size() > 0)
//					{
//						return true;
//					}
//
//					User user = (User) userContext.getUser();
//					List us = ProjectDelegate.getUsersForProjectInRole(p.getPk(), DummyWorkstation.getOID(), User.ROLE_TESTER);
//					if(us.contains(user))
//					{
//						return true;
//					}
//					us = ProjectDelegate.getUsersForProjectInRole(p.getPk(), DummyWorkstation.getOID(), User.ROLE_VERIFY);
//					if(us.contains(user))
//					{
//						return true;
//					}
//					us = ProjectDelegate.getUsersForProjectInRole(p.getPk(), DummyWorkstation.getOID(), User.ROLE_READONLY);
//					if(us.contains(user))
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
//	 		case PlanSecurityManager.OPENITEMLIST_DELETE_ITEM:
//				try
//				{
//					Unit unit = PersistWrapper.readByPrimaryKey(Unit.class, sContext.getUnitOID().getPk());
//					Project  p = PersistWrapper.readByPrimaryKey(Project.class, sContext.getProjectOID().getPk());
//					if(p != null && p.getManagerPk() != null &&  p.getManagerPk() == this.userContext.getUser().getPk())
//					{
//						return true;
//					}
//
//					List<UserPerms> l = PersistWrapper.readList(UserPerms.class, "select * from TAB_USER_PERMS where objectPk=? and objectType=? " +
//							"and userPk=? ", sContext.getUnitOID().getPk(), UserPerms.OBJECTTYPE_UNIT, this.userContext.getUser().getPk());
//					for (Iterator iterator = l.iterator(); iterator.hasNext();)
//					{
//						UserPerms userPerms = (UserPerms) iterator.next();
//						if(UserPerms.ROLE_MANAGER.equals(userPerms.getRole()))
//						{
//							return true;
//						}
//					}
//
//					User user = (User) userContext.getUser();
//					List us = ProjectDelegate.getUsersForUnitInRole(sContext.getUnitOID().getPk(), sContext.getProjectOID(), DummyWorkstation.getOID(), User.ROLE_VERIFY);
//					if(us.contains(user))
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
//			case PlanSecurityManager.FORM_MAIN_DELETE:
//				return false;
//
//			case PlanSecurityManager.WORKSTATION_LIST:
//			case PlanSecurityManager.FORM_ACCESS:
//			case PlanSecurityManager.FORM_CREATE:
//			case PlanSecurityManager.FORM_DESIGN:
//			case PlanSecurityManager.FORM_PUBLISH:
//			case PlanSecurityManager.FORM_CLOSE:
//			case PlanSecurityManager.FORM_VERSION_DELETE:
//			case PlanSecurityManager.FORM_CHANGE_EFFECTIVE_DATE:
//			case PlanSecurityManager.FORM_NEW_VERSION:
//				return true;
//
//			case PlanSecurityManager.FORM_PRINT:
//			try
//			{
//				Survey s = SurveyDelegate.getSurveyByPk(sContext.getFormOID().getPk());
//				if(s.getSuperseded() > 0)
//				{
//					return false;
//				}
//				return true;
//			}
//			catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return false;
//			}
//
			case PlanSecurityManager.USER_LIST:
				return false;
				
			case PlanSecurityManager.RESPONSE_EDIT_COMMENT_AND_ATTACHMENT:
			case PlanSecurityManager.RESPONSE_REOPEN_APPROVED_FORM:
			case PlanSecurityManager.USER_DETAIL_VIEW:
			case PlanSecurityManager.RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS:
			case PlanSecurityManager.RESPONSE_COMPARE_RESPONSEITEM_WITH_OLD_VERSIONS:
			case PlanSecurityManager.PROJECT_REPORTS_DASHBOARD:
				return true;
			
			case PlanSecurityManager.DEVICE_ACCESS:
				return true;

			default:
				return false;
		}
	}

	@Override
	public void setSecurityCredientials(SecurityCredential credential) {
		// nothing to be done here
	}
}
