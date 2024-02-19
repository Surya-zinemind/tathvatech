package com.tathvatech.user.service;

import java.io.Serializable;
import java.util.List;

import com.tathvatech.common.common.SecurityManagerBase;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Mode;
import com.tathvatech.user.entity.SurveyPerms;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.security.config.SecurityCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PlanSecurityManager implements SecurityManagerBase, Serializable
{
	private static final Logger logger = LoggerFactory.getLogger(PlanSecurityManager.class);
	private final AccountService accountService;
	
	public static final String ID_ADMINPLAN = "admin";

	protected static final int FOR_VIEW = 0;
	protected static final int FOR_EDIT = 1;
	protected static final int FOR_DATAENTRY = 2;
	
	public static final int PROJECT_CREATE = 200;
	public static final int PROJECT_EDIT = 201;
	public static final int PROJECT_DELETE = 202;
	public static final int PROJECT_CLOSE = 203;
	public static final int PROJECT_OPEN = 204;
	public static final int PROJECT_MANAGE_WORKSTATION = 205;
	public static final int	PROJECT_CREATE_CAR	= 206;
	public static final int	PROJECT_CAR_EDIT	= 207;
	public static final int	PROJECT_CAR_DELETE	= 208;
	public static final int	PROJECT_MANAGE_SETTINGS	= 209;
	public static final int	 PROJECT_REPORTS_DASHBOARD = 210;
	public static final int	 PROJECT_PRINT_WORKSTATION_DASHBOARD = 211;
	public static final int	 PROJECT_GENERATE_ACCEPTANCE_CERTIFICATE = 212;
	public static final int	 PROJECT_CREATE_HISTORY_BOOK = 213;
	public static final int	 PROJECT_VIEW_HISTORY_BOOK = 213;
	public static final int	 PROJECT_DOWNLOAD_HISTORY_BOOK = 214;
	public static final int PROJECT_MANAGE_PART = 215;
	public static final int PROJECT_UPDATE_OIL_CUSTODIAN = 216;
	public static final int PROJECT_CHANGE_UNIT_PARENT = 217;
	public static final int PROJECT_ADD_REMOVE_SITES = 218;
	public static final int PROJECT_CONFIGURE_SITES = 219;
	
	public static final int UNIT_CREATE = 301;
	public static final int UNIT_EDIT = 302;
	public static final int UNIT_DELETE = 303;
	public static final int	UNIT_OPEN_UNIT	= 304;
	public static final int UNIT_MARK_COMPLETE = 305;
	public static final int UNIT_MANAGE_WORKSTATION = 306;
	public static final int	UNIT_CREATE_CAR	= 307;
	public static final int	UNIT_CAR_DELETE	= 308;
	public static final int	UNIT_CAR_EDIT	= 309;
	public static final int	UNIT_MANAGE_CAR	= 310;
	public static final int	UNIT_CHANGE_WORKSTATION_SATUS	= 311;
	public static final int UNIT_OPEN_FROM_PLANNED = 312;
	public static final int ADD_UNIT_TO_PROJECT = 313;
	public static final int REMOVE_UNIT_FROM_PROJECT = 314;
	public static final int CHANGE_UNIT_PARENT = 315;
	public static final int CHANGE_UNIT_ORDER = 316;
	public static final int OPEN_UNIT_IN_PROJECT = 317;
	public static final int CLOSE_UNIT_IN_PROJECT = 318;
	
	public static final int	FORM_CREATE	= 400;
	public static final int	FORM_DESIGN	= 401;
	public static final int	FORM_PUBLISH	= 402;
	public static final int	FORM_CLOSE	= 403;
	public static final int	FORM_VERSION_DELETE	= 404;
	public static final int	FORM_MAIN_DELETE	= 405;
	public static final int	FORM_CHANGE_EFFECTIVE_DATE	= 406;
	public static final int	FORM_NEW_VERSION	= 407;
	public static final int	FORM_MANAGE_SECTION_LOCKS	= 408;
	public static final int FORM_PRINT = 409;
	public static final int FORM_ACCESS = 410;
	
	


	public static final int USER_LIST = 501;
	public static final int	USER_CREATE	= 502;
	public static final int	USER_EDIT	= 503;
	public static final int	USER_RESET_PASSWORD	= 504;
	public static final int	USER_DELETE	= 505;
	public static final int	USER_DETAIL_VIEW	= 506;
	public static final int USER_CHANGE_PASSWORD = 507;
	public static final int	USER_ACTIVATE	= 508;
	public static final int MANAGE_ACTITY_LOG = 509; // view other users activities
	public static final int	USER_CHANGE_USER_TYPE	= 510;
	public static final int	USER_APPROVE_NEWUSER_REQUEST	= 511;
	public static final int	USER_REJECT_NEWUSER_REQUEST	= 512;


	
	public static final int	WORKSTATION_CREATE	= 601;
	public static final int	WORKSTATION_EDIT	= 602;
	public static final int	WORKSTATION_DELETE	= 603;
	public static final int	WORKSTATION_MANAGE_STATUS = 604;
	public static final int	WORKSTATION_LIST	= 605;


	public static final int	RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS	= 701;
	public static final int	RESPONSE_COMPARE_RESPONSEITEM_WITH_OLD_VERSIONS	= 702;
	public static final int RESPONSE_SECTION_SAVE = 703;
	public static final int RESPONSE_SAVE = 704;
	public static final int RESPONSE_SUBMIT = 705;
	public static final int RESPONSE_VERIFY = 706;
	public static final int RESPONSE_APPROVE = 707;
	public static final int RESPONSE_REJECT_VERIFICATION = 708;
	public static final int RESPONSE_REJECT_APPROVAL = 709;
	public static final int RESPONSE_SECTION_LOCK = 710;
	public static final int RESPONSE_SECTION_UNLOCK = 711;
	public static final int RESPONSE_REOPEN_APPROVED_FORM = 712;
	public static final int RESPONSE_EDIT_COMMENT_AND_ATTACHMENT = 713;
	
	public static final int OPENITEMLIST_VIEW = 801;
	public static final int OPENITEMLIST_CREATE = 802;
	public static final int OPENITEM_CREATE = 803;
	public static final int OPENITEMLIST_GLOBALVIEW = 804;
	public static final int OPENITEMLIST_DASHBOARD = 805;
	public static final int OPENITEMLIST_DELETEFORM = 806;
	public static final int OPENITEM_FORMLEVEL_DOWNLOAD = 807;
	public static final int OPENITEM_FORMLEVEL_UPLOAD = 808;
	public static final int OPENITEMLIST_CHANGE_FORM_STATUS = 809;
	public static final int OPENITEMLIST_GLOBALDOWNLOAD = 810;
	public static final int OPENITEMLIST_GLOBALUPLOAD = 811;
	public static final int OPENITEMLIST_DELETE_ITEM = 812;
	public static final int OPENITEMLIST_CLEAR_FORM_ENTRIES = 813;
	public static final int OPENITEM_MARK_EXTERNAL = 814;
	public static final int OPENITEM_REOPEN_CLOSED = 815;

	public static final int MOD_ACCESS = 8001;
	public static final int MOD_CREATE = 8002;
	public static final int MOD_PUSH_TO_UNIT = 8003;
	public static final int MOD_DELETE_FROM_UNIIT = 8004; 
	public static final int MOD_DASHBOARD_VIEW = 8005;
	public static final int MOD_DELETE = 8006;
	public static final int MOD_CLOSE = 8007;
	public static final int MOD_COMPLETE_FROM_MASTER = 8008;
	public static final int MOD_OPEN = 8009;
	public static final int MOD_CHANGE_OWNER = 8010;
	

	
	public static final int PROJECT_ANDON_LIST_ACCESS = 9001;
	public static final int UNIT_ANDON_LIST_ACCESS = 9002;
	public static final int ANDON_CREATE = 9003;
	public static final int ANDON_UPDATE = 9004;
	public static final int ANDON_DELETE = 9005;

//	public static final int DEVICE_LIST = 2001;
//	public static final int DEVICE_CREATE = 2002;
//	public static final int DEVICE_SAVE = 2003;
//	public static final int DEVICE_ASSIGN = 2004;
	public static final int DEVICE_ACCESS = 2005;
//	public static final int DEVICE_DELETE = 2006;
//
//	public static final int DEVICE_ACTIVATE = 2007;



	public static final int	ANNOUNCEMENT_CREATE	= 10001;
	public static final int	ANNOUNCEMENT_DEACTIVATE	= 10002;





	public static final int NCR_NCG_EXPORT = 11000;










	protected UserContext userContext;

    protected PlanSecurityManager(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserContext(UserContext userContext)
	{
		this.userContext = userContext;
	}

	
	public abstract boolean checkAccess(int action, SecurityContext sContext);

	
	public static PlanSecurityManager getSecurityManager(User user)throws Exception
	{
		PlanSecurityManager psManager;
		
		String cName = null;
		
		//user will be null when survey is accessed by external parties
		if(user!= null && User.USER_PRIMARY.equals(user.getUserType()))
		{
			cName = "com.thirdi.surveyside.security.AdminUserSecurityManager";
		}
		else if(user!= null && User.USER_MANAGER.equals(user.getUserType()))
		{
			cName = "com.thirdi.surveyside.security.ManagerUserSecurityManager";
		}
		else if(user!= null && User.USER_ENGINEER.equals(user.getUserType()))
		{
			cName = "com.thirdi.surveyside.security.EngineerUserSecurityManager";
		}
		else if(user!= null && User.USER_TECHNICIAN.equals(user.getUserType()))
		{
			cName = "com.thirdi.surveyside.security.TechnicianUserSecurityManager";
		}
		else if(user!= null && User.USER_GUEST.equals(user.getUserType()))
		{
			cName = "com.thirdi.surveyside.security.GuestUserSecurityManager";
		}
		else
		{
			cName = "com.thirdi.surveyside.security.NoPlanSecurityManager";
		}
		
		try
		{
			psManager = (PlanSecurityManager)Class.forName(cName).newInstance();
		}
		catch(Exception ex)
		{
			logger.error("Could not instantiate security manager - " + cName + " :: " + ex.getMessage());
			if (logger.isDebugEnabled()) 
			{
				logger.debug(ex.getMessage(), ex);
			}
			throw ex;
		}
	
		return psManager;
	}

	public abstract void setSecurityCredientials(SecurityCredential credential);

	protected boolean checkGrantedPermission(UserContext context, int userPk, int surveyPk, int permissionFor)
	{
		try
		{
			SurveyPerms pValue = accountService.getUserPermissionsOnSurvey(context, userPk, surveyPk);
			if(pValue != null)
			{
				if(permissionFor == PlanSecurityManager.FOR_VIEW && pValue.isView())
				{
					return true;
				}
				else if(permissionFor == PlanSecurityManager.FOR_EDIT && pValue.isEdit())
				{
					return true;
				}
				else if(permissionFor == PlanSecurityManager.FOR_DATAENTRY && pValue.isDataEntry())
				{
					return true;
				}
			}
		}
		catch(Exception ex)
		{
		}
		return false;
	}


	/**
	 * This is common for all User types so the method is called from the children
	 * @param action
	 * @param objectType
	 * @param objectPk
	 * @param params
	 * @return
	 */
	public boolean checkAccessCommon(int action, SecurityContext sContext)
	{
		if(action == PlanSecurityManager.MOD_DELETE_FROM_UNIIT)
		{
			try
			{
				Mode mode = ModDelegate.getMode(sContext.getModeOID().getPk());
				if(mode == null)
					return false;
				if(userContext.getUser().getPk() == mode.getOwnerPk())
					return true;
				
	 			List<User> coordinators = ProjectDelegate.getProjectManagers(new ProjectOID(mode.getProjectPk()));
	 			if(coordinators.contains(userContext.getUser()))
	 			{
	 				return true;
	 			}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;	
			}
			return false;	
		}
		return false;
	}
	
	
}
