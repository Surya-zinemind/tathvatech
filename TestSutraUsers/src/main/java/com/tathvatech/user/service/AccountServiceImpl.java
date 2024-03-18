/*
 * Created on Nov 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.service;


import com.tathvatech.user.Asynch.AsyncProcessor;
import com.tathvatech.common.common.ApplicationConstants;
import com.tathvatech.common.common.ServiceLocator;
import com.tathvatech.common.email.EmailMessageInfo;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.exception.LoginFailedException;
import com.tathvatech.common.utils.OnewayEncryptUtils;
import com.tathvatech.common.utils.SequenceIdGenerator;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.*;
import com.tathvatech.user.repository.UserPasswordResetKeyDAO;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.util.*;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	private final PersistWrapper persistWrapper;

	private  final UserPasswordResetKeyDAO userPasswordResetKeyDAO;

	private final SequenceIdGenerator sequenceIdGenerator;

	private final CommonServiceManager commonServiceManager;

    public AccountServiceImpl(PersistWrapper persistWrapper, UserPasswordResetKeyDAO userPasswordResetKeyDAO, SequenceIdGenerator sequenceIdGenerator, CommonServiceManager commonServiceManager) {
        this.persistWrapper = persistWrapper;
        this.userPasswordResetKeyDAO = userPasswordResetKeyDAO;
        this.sequenceIdGenerator = sequenceIdGenerator;
        this.commonServiceManager = commonServiceManager;
    }


	@Transactional
    @Override
	public  Account createNewAccount(Account account)throws Exception
	{

        String nextNo = sequenceIdGenerator.getNext("Account");
        account.setAccountNo(nextNo);

        //new Account Created
        Date accountSignUpDate = new Date();
        account.setSignUpDate(accountSignUpDate);

        account.setStatus(AccountStatusTypes.STATUS_NEWACCOUNT.getId());

        logger.info("creating a new account object");

        long accountPk = persistWrapper.createEntity(account);
        logger.info("new account object created, pk-" + accountPk);

        //now fetch the account back
        account = (Account) persistWrapper.readByPrimaryKey(Account.class, accountPk);

        logger.info("Account fetched back - " + account);

        return account;
	}

	@Override
	public User createPrimaryUser(User user)throws Exception
	{
        user.setUserType(User.USER_PRIMARY);

        //check if userName is alreadyTaken
        User dupliFinderAcc = findPrimaryUserByUserName(user.getUserName());
        if(dupliFinderAcc != null && (!(dupliFinderAcc.getPk() == user.getPk())))
        {
            throw new AppException("Duplicate Username, Please choose another Username.");
        }
        dupliFinderAcc = findUserByEmail(user.getEmail());
        if(dupliFinderAcc != null)
        {
            throw new AppException("Duplicate email, Please provide another email.");
        }

        user.setCreateDate(new Date());

        //encrypt the password
        user.setPassword(OnewayEncryptUtils.encryptString(user.getPassword()));

        long userPk = persistWrapper.createEntity(user);

        //now fetch the account back
        user = (User)persistWrapper.readByPrimaryKey(User.class, userPk);

        return user;
	}

	@Override
	public  User createAddonUser(UserContext context, User _user, AttachmentIntf profilePicAttachment, boolean sendWelcomeKit)throws Exception
	{
		String userPassword = _user.getPassword();
		
		Account account = (Account)context.getAccount();

		//check for license count
		int licenses = LicenseManager.getLicenseCount();
		int readOnlylicenses = LicenseManager.getReadOnlyLicenseCount();

		if(_user.getUserType().equals(User.USER_GUEST))
		{
			int rUserCount = LicenseManager.getCurrentReadonlyUserCount();
			if(rUserCount >= readOnlylicenses)
			{
	            throw new AppException("Your license limit of " + readOnlylicenses + " read-only users have reached, You cannot create any more readonly users.");
			}
		}
		else
		{
			int userCount = LicenseManager.getCurrentRegularUserCount();
			if(userCount >= licenses)
			{
	            throw new AppException("Your license limit of " + licenses + " regular users have reached, You cannot create any more regular users.");
			}
		}
		
		
        //check if userName is alreadyTaken, and that the user is not the same as this one
        User dupliFinderAcc = findUserByUserName(_user.getUserName());
        if(dupliFinderAcc != null)
        {
            throw new AppException("Duplicate Username, Please choose another Username.");
        }
//        if(_user.getEmail()!= null && ! _user.getEmail().trim().isEmpty())
//        {
//        	dupliFinderAcc = findUserByEmail(_user.getEmail());
//        	if(dupliFinderAcc != null && (!(dupliFinderAcc.getPk() == _user.getPk())))
//        	{
//        		throw new AppException("Duplicate email, Please provide another email.");
//        	}
//        }
		User user = null;
        //check if there are any deleted devices, if so they can be reused
        List delList = persistWrapper.readList(User.class, "select * from TAB_USER where status=?", User.STATUS_DELETED);
        if(delList != null && delList.size() > 0)
        {
        	user = (User)delList.get(0);
        	user.setUserName(_user.getUserName());
        	user.setFirstName(_user.getFirstName());
        	user.setLastName(_user.getLastName());
        	user.setUserType(_user.getUserType());
        }
        else
        {
        	user = _user;
        }
        
		user.setAccountPk(account.getPk());
		user.setStatus(User.STATUS_ACTIVE);
        user.setCreateDate(new Date());

        //encrypt the password
        user.setPassword(OnewayEncryptUtils.encryptString(_user.getPassword()));
        if(_user.getPassPin() != null)
            user.setPassPin(OnewayEncryptUtils.encryptString(_user.getPassPin()));
        	

        long userPk = persistWrapper.createEntity(user);

        //now fetch the account back
        user = (User)persistWrapper.readByPrimaryKey(User.class, userPk);

        //if the user is of restricted type, make sure that it has no users assigned to it.
        String userType = user.getUserType();
//        if(User.USER_RESTRICTED.equals(userType))
//        {
//        	removeUsersFromManager(context, user.getPk());
//        }

        
        List<AttachmentIntf> attachments = new ArrayList<AttachmentIntf>();
        if(profilePicAttachment != null)
        {
        	((Attachment)profilePicAttachment).setAttachContext(User.AttachContext_ProfilePic);
        	attachments.add(profilePicAttachment);
        }
        
		//now save the attachments
        commonServiceManager.saveAttachments(context, (int) user.getPk(), EntityTypeEnum.User.getValue(), User.AttachContext_ProfilePic, attachments);
        
		NotificationsDelegate.notifyNewUserCreated(user, userPassword, sendWelcomeKit);
        return user;
	}

	@Deprecated
	// we are not using context.getAccount(), need to remove it
    @Override
	public  User saveAddonUser(UserContext context, User user, AttachmentIntf profilePicAttachment)throws Exception
	{
		Account account = (Account)context.getAccount();

		user.setAccountPk(account.getPk());

		//check for license count
		Context ctx = new InitialContext();
		int licenses = LicenseManager.getLicenseCount();
		int readOnlylicenses = LicenseManager.getReadOnlyLicenseCount();
		User dupliFinderAcc = findUserByUserName(user.getUserName());
		/*if(!user.getUserType().equals(dupliFinderAcc.getUserType()))
        {
		if(user.getUserType().equals(User.USER_GUEST))
		{
			int rUserCount = persistWrapper.read(Integer.class, "select count(pk) from TAB_USER where userType = ?", User.USER_GUEST);
			if(rUserCount >= readOnlylicenses)
			{
	            throw new AppException("Your license limit of " + readOnlylicenses + " read-only users have reached, You cannot change user type.");
			}
		}
		else if(dupliFinderAcc.getUserType().equals(User.USER_GUEST)) 
		{
			int userCount = persistWrapper.read(Integer.class, "select count(pk) from TAB_USER where userType != ?", User.USER_GUEST);
			if(userCount >= licenses)
			{
	            throw new AppException("Your license limit of " + licenses + " regular users have reached, You cannot change user type.");
			}
		}
        }*/
		//check if userName is alreadyTaken, and that the user is not the same as this one
        
       /* if(dupliFinderAcc != null && (!(dupliFinderAcc.getPk() == user.getPk())))
        {
            throw new AppException("Duplicate Username, Please choose another Username.");
        }
        dupliFinderAcc = findUserByEmail(user.getEmail());
        if(dupliFinderAcc != null && (!(dupliFinderAcc.getPk() == user.getPk())))
        {
            throw new AppException("Duplicate email, Please provide another email.");
        }
        //makesure that the userName of the primary user and guest user are not edited
        if((dupliFinderAcc !=  null) && (User.USER_PRIMARY.equals(dupliFinderAcc.getUserType()) || User.USER_GUEST.equals(dupliFinderAcc.getUserType())))
        {
        	user.setUserType(dupliFinderAcc.getUserType());
        	user.setUserName(dupliFinderAcc.getUserName());
        }*/
        
        persistWrapper.update(user);

        //now fetch the account back
        user = (User)persistWrapper.readByPrimaryKey(User.class,user.getPk());
        
        
        
        List<AttachmentIntf> attachments = new ArrayList<AttachmentIntf>();
        if(profilePicAttachment != null)
        {
        	((Attachment)profilePicAttachment).setAttachContext(User.AttachContext_ProfilePic);
        	attachments.add(profilePicAttachment);
        }
        
		//now save the attachments
        commonServiceManager.saveAttachments(context, (int) user.getPk(), EntityTypeEnum.User.getValue(), User.AttachContext_ProfilePic, attachments);
        

        //if the user is of restricted type, make sure that it has no users assigned to it.
        String userType = user.getUserType();
//        if(User.USER_RESTRICTED.equals(userType))
//        {
//        	removeUsersFromManager(context, user.getPk());
//        }

        return user;

	}

	@Transactional
	@Override
	public  User updateCurrentUserProfile(UserContext context, User uVal)throws Exception
	{
		Account account = (Account)context.getAccount();
		User user = (User)context.getUser();

		user.setFirstName(uVal.getFirstName());
		user.setLastName(uVal.getLastName());
		user.setEmail(uVal.getEmail());
		user.setTimezone(uVal.getTimezone());
//		user.setDateFormat(uVal.getDateFormat());

        persistWrapper.update(user);

        //now fetch the account back
        user = (User)persistWrapper.readByPrimaryKey(User.class, user.getPk());

        return user;
	}

	@Transactional
	@Override
	public  void activateUser(int userPk)throws Exception
    {
        User user = (User)persistWrapper.readByPrimaryKey(User.class, userPk);

        if(user != null)
        {
           	user.setStatus(User.STATUS_ACTIVE);
            persistWrapper.update(user);
        }
        else
        {
        	throw new AppException("User not found.");
        }
    }

	/**
	 * deactivate the user,, not deleting
	 * @param userPk
	 * @throws Exception
	 */
	@Transactional
    @Override
	public  void deleteAddonUser(int userPk)throws Exception
    {
    	User user =(User) persistWrapper.readByPrimaryKey(User.class, userPk);

        if(user == null)
        {
        	throw new AppException("User not found");
        }
        if(User.USER_PRIMARY.equals(user.getUserType()))
        {
			throw new AppException("Cannot delete Administrator User");
        }

    	persistWrapper.delete("delete from TAB_PROJECT_USERS where userPk=?", user.getPk());
    	persistWrapper.delete("delete from TAB_UNIT_USERS where userPk=?", user.getPk());
    	persistWrapper.delete("delete from ACL where userPk=?", user.getPk());
    	user.setStatus(User.STATUS_INACTIVE);
    	persistWrapper.update(user);
        
    	//find out if user has made any form entries
//        Integer k = persistWrapper.read(Integer.class, "select count(responseId) from TAB_RESPONSE where userPk=?", userPk);
//    	if(k.intValue() > 0)
//    	{
//    		throw new UserNotificationException("");
//    	}
//        Integer i = persistWrapper.read(Integer.class, "select count(pk) from TAB_FORM_WORKFLOW where performedBy=?", userPk);
//    	if(i.intValue() > 0)
//    	{
//    		throw new UserNotificationException("");
//    	}
//        i = persistWrapper.read(Integer.class, "select count(pk) from TAB_PROJECT where createdBy=?", userPk);
//    	if(i.intValue() > 0)
//    	{
//    		throw new UserNotificationException("");
//    	}
//        i = persistWrapper.read(Integer.class, "select count(pk) from TAB_WORKSTATION where createdBy=?", userPk);
//    	if(i.intValue() > 0)
//    	{
//    		throw new UserNotificationException("");
//    	}
//        i = persistWrapper.read(Integer.class, "select count(pk) from TAB_SURVEY where createdBy=?", userPk);
//    	if(i.intValue() > 0)
//    	{
//    		throw new UserNotificationException("");
//    	}
    
    }

    @Override
	public void deactivateUser(int userPk)throws Exception
    {
    	User user = (User)persistWrapper.readByPrimaryKey(User.class, userPk);

        if(user == null)
        {
        	throw new AppException("User not found");
        }
        if(User.USER_PRIMARY.equals(user.getUserType()))
        {
        	//find if there are more than 1 primary user
        	int count = persistWrapper.read(Integer.class, 
        			"select count(*) from TAB_USER where userType=? and status=?", User.USER_PRIMARY, User.STATUS_ACTIVE);
        	if(count == 1)
        		throw new AppException(new String[]{"Cannot deactivate user, There should be atleast one active Administrator user in the system"});
        }

        user.setStatus(User.STATUS_INACTIVE);
		persistWrapper.update(user);
    }
    /**
     * @param
     * @return
     */

	@Deprecated
     // we are not using context.getAccount(), need to remove it
    @Override
	public  long getCurrentAccountUserCount(UserContext context)throws Exception
    {
    	Account account = (Account)context.getAccount();

        long dCount = persistWrapper.read(long.class, "select count(*) as dCount from TAB_USER where accountPk =? and status=? and userType!=?", account.getPk(), User.STATUS_ACTIVE, User.USER_GUEST);
        return dCount;
    }

	@Override
	public  void createGuestUserForAccount(Account account)throws Exception
	{
        List dList = persistWrapper.readList(User.class, "select * from TAB_USER where accountPk =? and userType=? order by firstName", account.getPk(),User.USER_GUEST);
        if(dList != null && dList.size() > 0 )
        {
        	return;
//	            throw new AppException("MSG-duplicateDeviceId");
        }

        User user = null;
        //check if there are any deleted devices, if so they can be reused
        List delList = persistWrapper.readList(User.class, "select * from TAB_USER where accountPk =? and status=?  order by firstName", account.getPk(),User.STATUS_DELETED);
        if(delList != null && delList.size() > 0)
        {
        	user = (User)delList.get(0);
        }
        else
        {
        	user = new User();
        }
        user.setUserName(User.USER_GUEST);
        user.setUserType(User.USER_GUEST);
        user.setAccountPk(account.getPk());
        user.setStatus(User.STATUS_ACTIVE);

	    long newPk = persistWrapper.createEntity(user);

	}

	@Override
	public  User getGuestUser(Account account)throws Exception
	{
        List list = persistWrapper.readList(User.class, "select * from TAB_USER where accountPk =? and userType=? order by firstName",account.getPk(),User.USER_GUEST);
        if(list == null || list.size() == 0)
        {
        	throw new Exception();
        }
        return (User)list.get(0);
	}

	/**
	 * users to whome forms can be assigned
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Override
	public  List<User> getFormAssignableUsers()throws Exception
    {
        List list = persistWrapper.readList(User.class, "select * from TAB_USER where status =? and userType  not in (?,?, ?)  order by firstName", User.STATUS_ACTIVE, User.USER_PRIMARY, User.USER_GUEST, User.USER_OPERATOR);
        return list;
    }

	/**
	 * users that can be assigned to a manager
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	// we are not using context.getAccount(), need to remove it
    @Override
	public  List getUserAssignableUsers(UserContext context)throws Exception
    {
    	Account account = (Account)context.getAccount();
        List list = persistWrapper.readList(User.class, "select * from TAB_USER where accountPk =? and status =? order by firstName", account.getPk(), User.STATUS_ACTIVE);
        return list;
    }

    /**
     * users that gets displayed in the report filter
     * @param context
     * @return
     * @throws Exception
     */
    @Override
	public  List getAllReportFilterUsers(UserContext context)throws Exception
    {
    	Account account = (Account)context.getAccount();
        List list = persistWrapper.readList(User.class, "select * from TAB_USER where accountPk =? and status !=? order by firstName", account.getPk(), User.STATUS_DELETED);
        return list;
    }

	@Override
	public  List getUserAssignedUsers(UserContext context)throws Exception
	{
		return null;
//		User user = (User)context.getUser();
//
//        List list = persistWrapper.readList(User.class, "select * from TAB_USER where pk in (select userPk from TAB_USER_MANAGER where managerPk=?) and status != ?", user.getPk(), User.STATUS_DELETED);
//        if(list == null || list.size() == 0)
//        {
//        	if(User.USER_RESTRICTED.equals(user.getUserType()))
//        	{
//        		list = new ArrayList();
//        		list.add(user);
//        	}
//        	else
//        	{
//        		list = getAllReportFilterUsers(context);
//        	}
//        }
//        return list;
	}

	/**
	 * This method used for assignment management permissions, so it will return only the valid assignments.
	 * Method with the same name with no userPk as the argument is used by individual users for selecting devices
	 * for running reports etc. In that function, if no assignments are found, all devices for the account is returned
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
//	public  List getUserAssignedUsers(UserContext context, int userPk)throws Exception
//	{
//        List list = persistWrapper.readList(User.class, "select * from TAB_USER where pk in (select userPk from TAB_USER_MANAGER where managerPk=? and status !=?", userPk, Device.DELETED);
//        return list;
//	}

	@Override
	public  User getUser(String userName)
	{
		return findUserByUserName(userName);
	}

	@Override
	public  User getUser(long userPk)
	{
    	return (User)persistWrapper.readByPrimaryKey(User.class, userPk);
	}
	
    /**
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    @Override
	public  User login(String accountNo, String userName, String password) throws Exception
    {
	    User user = null;
	    List uList = null;
    	String p = OnewayEncryptUtils.encryptString(password);
		uList = persistWrapper.readList(User.class, "select * from TAB_USER where userName =? and password = ?", userName, p);
	    if(uList != null && uList.size() > 0)
	    {
	        user = (User)uList.get(0);
	        
	        if(!(User.STATUS_ACTIVE.equals(user.getStatus())))
	        {
	        	throw new LoginFailedException("Login failed; User is not active");
	        }
	    }
	    else
	    {
	        throw new LoginFailedException("Login failed; Invalid username / password. ");
	    }
	    return user;
    }


    @Override
	public  User loginWithPassPin(String accountNo, String userName, String passPin) throws Exception
    {
	    User user = null;
	    List uList = null;
    	String p = OnewayEncryptUtils.encryptString(passPin);
		uList = persistWrapper.readList(User.class, "select * from TAB_USER where userName =? and passPin = ?", userName, p);
	    if(uList != null && uList.size() > 0)
	    {
	        user = (User)uList.get(0);
	        
	        if(!(User.STATUS_ACTIVE.equals(user.getStatus())))
	        {
	        	throw new LoginFailedException("Login failed; User is not active");
	        }
	    }
	    else
	    {
	        throw new LoginFailedException("Login failed; Invalid username / password. ");
	    }
	    return user;
    }

    /**
     * @param accountPk
     * @return
     * @throws Exception
     */
    @Override
	public  Account getAccount(int accountPk) throws Exception
    {
		return (Account)persistWrapper.readByPrimaryKey(Account.class, accountPk);
    }

	@Override
	public  Account getAccountByAccountNo(String accountNo) throws Exception
	{
	    Account ret = null;

	    List acList = persistWrapper.readList(Account.class, "select * from TAB_ACCOUNT where accountNo=?", accountNo);
	    if(acList != null && acList.size() > 0)
	    {
	    	ret = (Account)acList.get(0);
	    }
	    return ret;
	}

	@Override
	public  Account findAccountByUserName(String userName)throws Exception
    {
        Account acc = null;
        List accList = persistWrapper.readList(Account.class, "select * from TAB_ACCOUNT where userName =?", userName);
        if(accList != null && accList.size() > 0)
        {
            acc = (Account)accList.get(0);
        }
        return acc;
    }

    @Override
	public  User findPrimaryUserByUserName(String userName) throws Exception
    {
        User user = null;
        List userList = persistWrapper.readList(User.class, "select * from TAB_USER where userName =? and userType=? order by firstName", userName, User.USER_PRIMARY);
        if(userList != null && userList.size() > 0)
        {
            user = (User)userList.get(0);
        }
        return user;
    }

    @Override
	public  User findUserByUserName(String userName)
    {
        User user = persistWrapper.read(User.class, "select * from TAB_USER where userName =?", userName);
        return user;
    }
    @Override
	public  User findUserByEmail(String email) throws Exception
    {
        User user = persistWrapper.read(User.class, "select * from TAB_USER where email =? order by status limit 0, 1", email);
        return user;
    }
	@Override
	public  User getPrimaryUser()throws Exception
	{
        User user = null;
        List userList = persistWrapper.readList(User.class, "select * from TAB_USER where userType=? order by firstName", User.USER_PRIMARY);
        if(userList != null && userList.size() > 0)
        {
            user = (User)userList.get(0);
        }
        return user;
	}

	@Override
	public  List getAllUserPermissionsOnSurvey(UserContext context, String surveyPk)throws Exception
	{
		return persistWrapper.readList(SurveyPerms.class, "select * from TAB_SURVEY_PERMS where surveyPk=?", surveyPk);
	}

	@Override
	public  SurveyPerms getUserPermissionsOnSurvey(UserContext context, int userPk, int surveyPk)throws Exception
	{
		List list = persistWrapper.readList(SurveyPerms.class, "select * from TAB_SURVEY_PERMS where userPk=? and surveyPk=?", userPk, surveyPk);

		if(list != null && list.size() > 0)
		{
			return (SurveyPerms)((SurveyPerms)list.get(0));
		}

		return null;
	}

	@Override
	public  void removeAllPermissionsOnSurvey(UserContext context, int surveyPk)throws Exception
	{
		persistWrapper.delete("delete from TAB_SURVEY_PERMS where surveyPk=?", surveyPk);
	}

	@Override
	public  List getAllUserPermissions(UserContext context, int userPk)throws Exception
	{
		return persistWrapper.readList(SurveyPerms.class, "select * from TAB_SURVEY_PERMS where userPk=?", userPk);
	}

	@Override
	public  void setUserPermissions(UserContext context, int userPk, List permsList)throws Exception
	{
		//delete current entries
		persistWrapper.delete("delete from TAB_SURVEY_PERMS where userPk=?", userPk);
		for (Iterator iterator = permsList.iterator(); iterator.hasNext();)
		{
			SurveyPerms aPerms = (SurveyPerms)iterator.next();

			aPerms.setUserPk(userPk);

			persistWrapper.createEntity(aPerms);
		}
	}

	@Override
	public  void setSurveyPermissions(UserContext context, int surveyPk, List permsList)throws Exception
	{
		//delete current entries
		persistWrapper.delete("delete from TAB_SURVEY_PERMS where surveyPk=?", surveyPk);

		for (Iterator iterator = permsList.iterator(); iterator.hasNext();)
		{
			SurveyPerms aPerms = (SurveyPerms)iterator.next();

			aPerms.setSurveyPk(surveyPk);

			persistWrapper.createEntity(aPerms);
		}
	}

	@Override
	public  void removeUsersFromManager(UserContext context, int managerPk)throws Exception
	{
    	persistWrapper.delete("delete from TAB_USER_MANAGER where managerPk=?", managerPk);
	}

    @Override
	public  void addUsersToManager(UserContext context, int managerPk, Integer[] userPks)throws Exception
    {
    	for (int i = 0; i < userPks.length; i++)
		{
            int aUserPk = userPks[i].intValue();
            UserManager da = new UserManager();
            da.setManagerPk(managerPk);
            da.setUserPk(aUserPk);
            persistWrapper.createEntity(da);
        }
    }


    /**
     * @param acc
     */
	@Transactional
    @Override
	public  void cancelAccount(Account acc)throws Exception
    {
        acc.setStatus(AccountStatusTypes.STATUS_PENDING_CANCEL.getId());
        acc.setCancelDate(new Date());
        persistWrapper.update(acc);
    }

    /**
     * @param acc
     */
	@Transactional
    @Override
	public  void activateAccount(Account acc)throws Exception
    {
        acc.setStatus(AccountStatusTypes.STATUS_ACTIVE.getId());
        acc.setActiveDate(new Date());
        persistWrapper.update(acc);
    }

	/**
	 * @param userName
	 */
	@Transactional
	@Override
	public  void sendPassword(UserContext context, String userName)throws Exception
	{
	    List uList = persistWrapper.readList(User.class, "select * from TAB_USER where userName=? and userType=?", userName, User.USER_PRIMARY);
	    if(uList.size() == 0)
	    {
	        throw new AppException("MSG-userDoesNotExist");
	    }
	    else
	    {
	        User user = (User)uList.get(0);
	        String emailAddr = user.getEmail();

	        String tempPass = RandomStringUtils.randomAlphabetic(6);
	        logger.debug("New temp password is - " + tempPass);
	        user.setPassword(OnewayEncryptUtils.encryptString(tempPass));
	        persistWrapper.update(user);

	        String message =  "Your password has been reset to " + tempPass;

	        EmailMessageInfo emailInfo = new EmailMessageInfo(ApplicationConstants.SERVICE_EMAIL_ADDRESS, new String[]{emailAddr}, "Password reset request", message);
	        AsyncProcessor.scheduleEmail(emailInfo);
	    }
	}

	@Transactional
	@Override
	public  void markAccountAsPaymentPending(Account account)throws Exception
	{
    	account.setStatus(AccountStatusTypes.STATUS_PENDING_PAYMENTAUTH.getId());
        persistWrapper.update(account);
	}

	@Transactional
	@Override
	public  void addAccountNote(UserContext context, AccountNote aNote)throws Exception
	{
		aNote.setSubmitTime(new Date());
		aNote.setSubmittedBy(((User)context.getUser()).getUserName());
		persistWrapper.createEntity(aNote);
	}

	@Transactional
	@Override
	public  void changePassword(UserContext context, String currentPassword, String newPassword) throws Exception
	{
        User user = (User)context.getUser();
        String currentPassEnc = OnewayEncryptUtils.encryptString(currentPassword);
        if(currentPassEnc.equals(user.getPassword()))
        {
        	user.setPassword(OnewayEncryptUtils.encryptString(newPassword));
            persistWrapper.update(user);
        }
        else
        {
        	throw new AppException("Current password is invalid, Password not changed");
        }
	}

	@Transactional
	@Override
	public  void changePassPin(UserContext context, String currentPassPin, String newPassPin) throws Exception
	{
		try
		{
			if(!(newPassPin.length() == 4))
				throw new Exception();
			int pin = Integer.parseInt(newPassPin);
		}
		catch (Exception e)
		{
			throw new AppException("Invalid PIN, it should be 4 digit numeric value");
		}
		
        User user = (User)context.getUser();
        String currentPassEnc = OnewayEncryptUtils.encryptString(currentPassPin);
        if(currentPassEnc.equals(user.getPassPin()))
        {
        	user.setPassPin(OnewayEncryptUtils.encryptString(newPassPin));
            persistWrapper.update(user);
        }
        else
        {
        	throw new AppException("Current password is invalid, Password not changed");
        }
	}

	@Transactional
	@Override
	public  void adminChangePassword(UserContext context, User user, String password) throws Exception
	{
		user.setPassword(OnewayEncryptUtils.encryptString(password));
        persistWrapper.update(user);
	}

	@Transactional
	@Override
	public  void adminChangePassPin(UserContext context, User user, String pin) throws Exception
	{
		user.setPassPin(OnewayEncryptUtils.encryptString(pin));
        persistWrapper.update(user);
	}

	@Transactional
	@Override
	public  void addAccountAlert(Account account, String alertString)throws Exception
	{
		AccountAlert alert = new AccountAlert();
		alert.setAccountPk(account.getPk());
		alert.setSubmitTime(new Date());
		alert.setText(alertString);
		alert.setStatus(AccountAlert.STATUS_OPEN);

		persistWrapper.createEntity(alert);
	}

	@Transactional
	@Override
	public  void dismissAlert(UserContext context, long alertPk)throws Exception
	{
		AccountAlert alert = (AccountAlert)persistWrapper.readByPrimaryKey(AccountAlert.class, alertPk);
		alert.setStatus(AccountAlert.STATUS_DISMISSED);

		persistWrapper.update(alert);
	}

	@Transactional
	@Override
	public  void changeAddonUserPassword(UserContext context,
										 User user, String password)throws Exception
	{
		user.setPassword(OnewayEncryptUtils.encryptString(password));
        persistWrapper.update(user);
	}

	@Transactional
	@Override
	public  void updateAccount(Account account) throws Exception {
		persistWrapper.update(account);
	}

	@Override
	public  List<User> getUserList()throws Exception
	{
		String sql = "select * from TAB_USER order by createDate desc";
		return persistWrapper.readList(User.class, sql, null);
	}

	@Override
	public  List<User> getValidUserList()
	{
		String sql = "select * from TAB_USER where status = ? and userType not in (?, ?, ?) order by firstName ";
		return persistWrapper.readList(User.class, sql, User.STATUS_ACTIVE, User.USER_PRIMARY, User.USER_OPERATOR, User.USER_TSSUPPORT);
	}

	@Transactional
	@Override
	public  void setUserPermissions(int entityPk, int entityType, Collection<User> userList, String role)throws Exception
	{
		//delete the existing list of users with that role
		persistWrapper.delete("delete from TAB_USER_PERMS where objectType=? and objectPk=? " +
				"and role=? ", entityType, entityPk, role);

		for (Iterator iterator = userList.iterator(); iterator.hasNext();)
		{
			User aUser = (User) iterator.next();
			UserPerms ap = new UserPerms();
			ap.setObjectPk(entityPk);
			ap.setObjectType(entityType);
			ap.setRole(role);
			ap.setUserPk(aUser.getPk());
			persistWrapper.createEntity(ap);
		}
	}

	@Override
	public  void setUserPermissions(int projectPk, Collection<User> [] userLists, String[] roles)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            for (int i = 0; i < userLists.length; i++)
			{
        	    setUserPermissions(projectPk, userLists[i], roles[i]);
			}
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
	}

	@Transactional
	@Override
	public  void setUserPermissions(int projectPk, Collection userList, String role)throws Exception
	{
		//delete the existing list of users with that role
		persistWrapper.delete("delete from TAB_USER_PERMS where objectType=? and objectPk=? " +
				"and role=? ", UserPerms.OBJECTTYPE_PROJECT, projectPk, role);

		for (Iterator iterator = userList.iterator(); iterator.hasNext();)
		{
			User aUser = (User) iterator.next();
			UserPerms ap = new UserPerms();
			ap.setObjectPk(projectPk);
			ap.setObjectType(UserPerms.OBJECTTYPE_PROJECT);
			ap.setRole(role);
			ap.setUserPk(aUser.getPk());
			persistWrapper.createEntity(ap);
		}
	}

	@Override
	public  boolean isUserInRole(UserOID userOID, int objectPk, int objectType, String[] roles)
	{
		try {
			StringBuffer sb = new StringBuffer("select count(*) from TAB_USER_PERMS where userPk = ? and " +
					"objectPk=? and objectType=? and role in (");
			String sep = "";
			for (int i = 0; i < roles.length; i++) 
			{
				sb.append(sep).append("'").append(roles[i]).append("'");
				sep = ", ";
			}
			sb.append(")");
			
			int count = persistWrapper.read(Integer.class, sb.toString(), userOID.getPk(), objectPk, objectType);
			if(count > 0)
				return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public  List<User> getACLs(int objectPk, int objectType, String role) throws Exception
	{
		return persistWrapper.readList(User.class, "select * from TAB_USER where pk in (select userPk from TAB_USER_PERMS where " +
				"objectPk=? and objectType=? and role=?) order by TAB_USER.firstName", 
				objectPk, objectType, role);
	}
	

	/**
	 * this is the list from the TAB_USER_PERMS table. TODO:: We need to merge this table to the ACL table
	 * @param userOID
	 * @param objectType
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@Override
	public  List<Integer> getObjectPksWithRoleForUser(UserOID userOID, int objectType, String role) throws Exception
	{
		return persistWrapper.readList(Integer.class, "select objectPk from TAB_USER_PERMS where " +
				"objectType=? and role=?", 
				objectType, role);
	}
	
	/**
	 * To get the count of users with pirticular type and status.
	 * @param userType
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	@Override
	public  Integer getUserCountfromTypeAndStatus(String userType,
											  String status) throws Exception {
		String query = "SELECT COUNT(*) FROM TAB_USER WHERE userType = ? and status = ?";

		return persistWrapper.read(Integer.class, query, userType, status);
	}

	@Override
	public  void changeUserLicenseType(int userPk) throws Exception
	{
		synchronized (LicenseManager.class) 
		{
			User user = getUser(userPk);
			if(user == null)
			{
				throw new Exception("Invalid user");
			}
			
			if(User.USER_GUEST.equals(user.getUserType()))
			{
				// you are changing to regular user, so check the regular user count
				int licenses = LicenseManager.getLicenseCount();
				int userCount = LicenseManager.getCurrentRegularUserCount();
				if(userCount >= licenses)
				{
		            throw new AppException("Your license limit of " + licenses + " regular users have reached, You cannot create any more regular users.");
				}
				
				user.setUserType(User.USER_TECHNICIAN);
			}
			else
			{
				// you are changing to readonly user, so check the readonly user count
				int readOnlylicenses = LicenseManager.getReadOnlyLicenseCount();
				int rUserCount = LicenseManager.getCurrentReadonlyUserCount();
				if(rUserCount >= readOnlylicenses)
				{
		            throw new AppException("Your license limit of " + readOnlylicenses + " read-only users have reached, You cannot create any more readonly users.");
				}
				
				user.setUserType(User.USER_GUEST);
			}
			
			// now remove all user associations
			persistWrapper.delete("delete from TAB_PROJECT_USERS where userPk=?", userPk);
			persistWrapper.delete("delete from TAB_UNIT_USERS where userPk = ?", userPk);
			persistWrapper.delete("delete from tab_sectionlock where userPk=?", userPk);
			persistWrapper.delete("delete from TAB_USER_PERMS where userPk=?", userPk);

			persistWrapper.update(user);
		}
	}

	@Override
	public  List<User> searchUser(String searchString)
	{
		try 
		{
			String s = searchString.toUpperCase();
			return persistWrapper.readList(User.class, "select * from TAB_USER where status = 'Active' and (upper(userName) like '%" + s + "%' or upper(firstName) like '%" + s + "%' or upper(lastName) like '%" + s + "%')");
		} catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public  UserQuery getUserQuery(int userPk)
	{
		return persistWrapper.read(UserQuery.class, UserQuery.sql + " and u.pk = ?", userPk);
	}
	
	@Override
	public  UserPasswordResetKey createUserPasswordResetKey(User user, User sendKeyToUser) throws Exception
	{


		//invalidate keys if there is any valid key entry.
		userPasswordResetKeyDAO.invalidateKeys(user.getOID());
		
		return userPasswordResetKeyDAO.createUserPasswordResetKey(user, sendKeyToUser);
	}
	
	@Override
	public  void resetUserPasswordWithVerificationCode(User user, String verificationKey, String newPassword) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);


    		UserPasswordResetKey keyEntry = userPasswordResetKeyDAO.getValidUserPasswordResetKey(user.getOID(), 3, 24);
			if(keyEntry == null)
			{
				throw new AppException("Invalid verification code. Password cannot be reset");
			}
			if(!(verificationKey.equals(keyEntry.getVerificationCode())))
			{
				keyEntry.setNoOfTries(keyEntry.getNoOfTries()+1);
				userPasswordResetKeyDAO.updateUserPasswordResetKey(keyEntry);
	            con.commit();
				
				throw new AppException("Invalid verification code. Password cannot be reset");
			}
			else
			{
		        user.setPassword(OnewayEncryptUtils.encryptString(newPassword));
		        persistWrapper.update(user);
		        
		        keyEntry.setNoOfTries(keyEntry.getNoOfTries()+1);
		        keyEntry.setResetDone(1);
				userPasswordResetKeyDAO.updateUserPasswordResetKey(keyEntry);
	            con.commit();
			}
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
            try
			{
				con.rollback();
			}
            catch (Exception e)
			{
			}
			throw ex;
        }
	}

	@Transactional
	@Override
	public  User updateUserEmail(UserContext context, String newEmail) throws Exception {
		User user = (User)persistWrapper.readByPrimaryKey(User.class, context.getUser().getPk());
		user.setEmail(newEmail);
		persistWrapper.update(user);

		user = (User)persistWrapper.readByPrimaryKey(User.class, context.getUser().getPk());
		
		return user;
	}
}