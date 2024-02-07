/*
 * Created on Nov 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.controller;


import com.tathvatech.common.Asynch.AsyncProcessor;
import com.tathvatech.common.common.ApplicationConstants;
import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.common.ServiceLocator;
import com.tathvatech.common.email.EmailMessageInfo;
import com.tathvatech.common.email.EmailServiceManager;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.exception.LoginFailedException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.*;
import com.tathvatech.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountController
{
    @Autowired
    static AccountService accountService;

    @Autowired
    static PersistWrapper persistWrapper;
    

	public static User createNewAccount(UserContext context, Account accVal, User uVal)throws Exception
	{
        Connection con = null;
        try
        {
            Account account = accountService.createNewAccount(accVal);

            uVal.setAccountPk(account.getPk());
            uVal.setStatus(User.STATUS_ACTIVE);

            User user = accountService.createPrimaryUser(uVal);


            //and add that info into the AccountData
            AccountData aData = new AccountData();
            aData.setAccountPk(account.getPk());
            aData.setProperty("DefaultMailingListPk");
            persistWrapper.createEntity(aData);

            //create the default Guest Device for this account
            accountService.createGuestUserForAccount(account);

            con.commit();

            AsyncProcessor.scheduleEmail(new EmailMessageInfo(ApplicationConstants.SERVICE_EMAIL_ADDRESS,
            		new String[]{user.getEmail()}, "New account created subject",
            		"New account created message body"));

            return user;

        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static void createAddonUser(UserContext context, User userVal, AttachmentIntf profilePicAttachment, boolean sendWelcomeKit)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            Account account = (Account)context.getAccount();

	        User user = accountService.createAddonUser(context, userVal, profilePicAttachment, sendWelcomeKit);

	        con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static User updateCurrentUserProfile(UserContext context, User userVal)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            User user = accountService.updateCurrentUserProfile(context, userVal);

            con.commit();

            return user;
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static void saveAddonUser(UserContext context, User userVal, AttachmentIntf profilePicAttachment)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            Account account = (Account)context.getAccount();

            userVal.setStatus(User.STATUS_ACTIVE);

            User user = accountService.saveAddonUser(context, userVal, profilePicAttachment);

            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static void deleteAddonUser(int userPk)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            accountService.deleteAddonUser(userPk);

            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

    public static void activateUser(int userPk)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            accountService.activateUser(userPk);

            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
    }

    /**
     * this also backs up the account logo if different and not null
     * @param acc
     */
//    public static void updateAccount(Account acc)throws Exception
//    {
//        Connection con = null;
//        try
//        {
//            con = ServiceLocator.locate().getConnection();
//            con.setAutoCommit(false);
//
//            AccountManager.updateAccount(acc);
//
//            con.commit();
//        }
//        catch(Exception ex)
//        {
//            con.rollback();
//            throw ex;
//        }
//        finally
//        {
//            ServiceLocator.locate().releaseConnection();
//        }
//    }

    /**
     * this is the general login for users except the operator users. 
     * @param userName
     * @param password
     * @return
     */
    public static User login(String accountNo, String userName, String password)throws Exception
    {
        User user = accountService.login(accountNo, userName, password);
        
        if(user != null && User.USER_OPERATOR.equals(user.getUserType()))
        {
        	throw new AppException("User is not authorized to login.");
        }
        return user;
    }

    /**
     * All users including operator users are allowed to login using this call. 
     * this is called by timetracker checkin and checkout to validate the user.
     * @param userName
     * @param password could be passPin or the password
     * @return
     */
    public static User loginFromDevice(String accountNo, String userName, String password)throws Exception
    {
    	String loginMessage = null;
        User user = null;
        try
		{
            user = accountService.loginWithPassPin(accountNo, userName, password);
		}
        catch (LoginFailedException e)
        {
        	loginMessage = e.getMessage();
        }
        catch (Exception e)
		{
		}

        
        if(user == null)
        {
        	try
			{
            	user = accountService.login(accountNo, userName, password);
			} 
            catch (LoginFailedException e)
            {
            	loginMessage = e.getMessage();
            }
        	catch (Exception e)
			{
				// TODO: handle exception
			}
        }
        if(user == null)
        	throw new LoginFailedException(loginMessage);
        else
        	return user;
    }
    
    /**
     * @param accountPk
     * @return
     */
    public static Account getAccount(int accountPk)throws Exception
    {
        Account account = accountService.getAccount(accountPk);
        if(account != null)
        {
	       	List dataList = persistWrapper.readList(AccountData.class, "select * from TAB_ACCOUNT_DATA where accountPk=?", account.getPk());
	       	for (Iterator iter = dataList.iterator(); iter.hasNext();)
	       	{
				AccountData aData = (AccountData) iter.next();
				account.putAccountDate(aData.getProperty(), aData.getValue());
			}
        }
       	return account;
    }

	public static Account getAccountByAccountNo(String accountNo) throws Exception
	{
        Account account = accountService.getAccountByAccountNo(accountNo);
        if(account != null)
        {
	       	List dataList = persistWrapper.readList(AccountData.class, "select * from TAB_ACCOUNT_DATA where accountPk=?", account.getPk());
	       	for (Iterator iter = dataList.iterator(); iter.hasNext();)
	       	{
				AccountData aData = (AccountData) iter.next();
				account.putAccountDate(aData.getProperty(), aData.getValue());
			}
        }
       	return account;
	}


	/**
     * @param userPk
     * @return
     */
    public static User getUser(int userPk)
    {
    	return accountService.getUser(userPk);
    }

    public static User findPrimaryUserByUserName(String userName)throws Exception
    {
        User user = accountService.findPrimaryUserByUserName(userName);

       	return user;
    }

    public static Account getAccountForUser(User user)throws Exception
    {
        return (Account) persistWrapper.readByPrimaryKey(Account.class, user.getAccountPk());
    }

	public static User getPrimaryUser()throws Exception
	{
		return (User) accountService.getPrimaryUser();
	}

	public static List getAllUserPermissionsOnSurvey(UserContext context, String surveyPk)throws Exception
	{
		return accountService.getAllUserPermissionsOnSurvey(context, surveyPk);
	}

	public static SurveyPerms getUserPermissionsOnSurvey(UserContext context, int userPk, int surveyPk)throws Exception
	{
		return accountService.getUserPermissionsOnSurvey(context, userPk, surveyPk);
	}

	public static void removeAllPermissionsOnSurvey(UserContext context, int surveyPk)throws Exception
	{
        accountService.removeAllPermissionsOnSurvey(context, surveyPk);
	}

	public static List getAllUserPermissions(UserContext context, int userPk)throws Exception
	{
		// TODO Auto-generated method stub
		return accountService.getAllUserPermissions(context, userPk);
	}

	public static void setUserPermissions(UserContext context, int userPk, List permsList)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            accountService.setUserPermissions(context, userPk, permsList);

            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static void setSurveyPermissions(UserContext context, int surveyPk, List permsList)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            accountService.setSurveyPermissions(context, surveyPk, permsList);

            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

    public static void setUserDevices(UserContext context, int managerPk, Integer[] userPks)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            User user = (User) persistWrapper.readByPrimaryKey(User.class, managerPk);

            if(user == null)
            	throw new Exception();
            Account account = (Account)context.getAccount();
            if(!(account.getPk() == user.getAccountPk()))
            {
            	//user belongs to another account
            	throw new Exception();
            }

            accountService.removeUsersFromManager(context, managerPk);
            accountService.addUsersToManager(context, managerPk, userPks);

            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}


    /**
     * @param acc
     */
    public static void cancelAccount(Account acc)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

//            PaymentManager.invalidateAccountSubscriptions(acc);
            accountService.cancelAccount(acc);

            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
    }

    /**
     * @param acc
     */
    public static void activateAccount(Account acc)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            accountService.activateAccount(acc);

            con.commit();
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
            con = ServiceLocator.locate().getConnection();
	    }
    }

    /**
     * @param userId
     */
    public static void sendPassword(UserContext context, String userId)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            accountService.sendPassword(context, userId);

            con.commit();
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
    }


//    public static int chooseSubscriptionPlan(Account account, PaymentOption selectedOption, PaymentMethod paymentMethod, PaymentMethodConfig methodConfig)throws Exception
//    {
//        Connection con = null;
//        try
//        {
//            con = ServiceLocator.locate().getConnection();
//            con.setAutoCommit(false);
//
//            int status = AccountManager.chooseSubscriptionPlan(account, selectedOption, paymentMethod, methodConfig);
//
//            con.commit();
//
//            return status;
//	    }
//        catch(Exception ex)
//        {
//            con.rollback();
//            throw ex;
//        }
//	    finally
//	    {
//            ServiceLocator.locate().releaseConnection();
//	    }
//    }

	public static void markAccountAsPaymentPending(Account account)throws Exception
	{
        accountService.markAccountAsPaymentPending(account);
	}

	public static void addAccountNote(UserContext context, AccountNote noteValue)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		accountService.addAccountNote(context, noteValue);

            con.commit();
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}


	public static void addAccountAlert(Account account, String text)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		accountService.addAccountAlert(account, text);

            con.commit();
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}


	public static void changePassword(UserContext context, String currentPassword, String newPassword)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		accountService.changePassword(context, currentPassword, newPassword);

            con.commit();
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}

	public static void changePassPin(UserContext context, String currentPassPin, String newPassPin)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		accountService.changePassPin(context, currentPassPin, newPassPin);

            con.commit();
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}



	public static void adminChangePassword(UserContext context, User user, String password, boolean notifyUser)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		accountService.adminChangePassword(context, user, password);

            con.commit();
            
            if(notifyUser)
            {
            	user = accountService.getUser(user.getPk());
            	NotificationsDelegate.notifyPasswordReset(user, password);
            }
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}


	public static void adminChangePassPin(UserContext context, User user, String passPin, boolean notifyUser)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		accountService.adminChangePassPin(context, user, passPin);

            con.commit();
            
            if(notifyUser)
            {
            	user = accountService.getUser(user.getPk());
            	NotificationsDelegate.notifyPinReset(user, passPin);
            }
	    }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}


	public static void dismissAlert(UserContext context, String alertPk) throws Exception
	{
		accountService.dismissAlert(context, Long.parseLong(alertPk));
	}

	public static User getGuestUser(Account account)throws Exception
	{
		return accountService.getGuestUser(account);
	}

	public static User getUser(String userName)
	{
        return accountService.getUser(userName);
	}

    public static List<User> getFormAssignableUsers()throws Exception
    {
    	return accountService.getFormAssignableUsers();
    }

    public static List getUserAssignableUsers(UserContext context)throws Exception
    {
    	return accountService.getUserAssignableUsers(context);
    }

//	public static List getUserAssignedUsers(UserContext context)throws Exception
//	{
//		return AccountManager.getUserAssignedUsers(context);
//	}

	/**
	 * This method used for assignment management permissions, so it will return only the valid assignments.
	 * Method with the same name with no userPk as the argument is used by individual users for selecting devices
	 * for running reports etc. In that function, if no assignments are found, all devices for the account is returned
	 * @param context
	 * @param
	 * @return
	 * @throws Exception
	 */
//	public static List getUserAssignedUsers(UserContext context, int userPk)throws Exception
//	{
//		return AccountManager.getUserAssignedUsers(context, userPk);
//	}

    public static long getCurrentAccountUserCount(UserContext context)throws Exception
    {
        return accountService.getCurrentAccountUserCount(context);
    }

	public static void changeAddonUserPassword(UserContext context,
			User user, String password)throws Exception
	{
		accountService.changeAddonUserPassword(context, user, password);
	}

	public static void updateAccount(Account account) throws Exception {
		accountService.updateAccount(account);
	}

	public static List<User> getUserList()throws Exception
	{
		return accountService.getUserList();
	}

	public static List<User> getValidUserList()
	{
		return accountService.getValidUserList();
	}

	public static void setUserPermissions(int entityPk, int entityType, Collection userList, String role)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    	    accountService.setUserPermissions(entityPk, entityType, userList, role);
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

	public static void setUserPermissions(int projectPk, Collection[] userLists, String[] roles)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            for (int i = 0; i < userLists.length; i++)
			{
        	    accountService.setUserPermissions(projectPk, userLists[i], roles[i]);
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
	
	public static boolean isUserInRole(UserOID userOID, int objectPk, int objectType, String[] roles)
	{
		return accountService.isUserInRole(userOID, objectPk, objectType, roles);
	}

	public static List<User> getACLs(int pk, int oBJECTTYPE_PROJECT, String rOLE_MANAGER) throws Exception
	{
		return accountService.getACLs(pk, oBJECTTYPE_PROJECT, rOLE_MANAGER);
	}
	
	/**
	 * To get the count of users with pirticular type and status.
	 * @param userType
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public static int getUserCountfromTypeAndStatus(String userType,
			String status) throws Exception {
		
		return accountService.getUserCountfromTypeAndStatus(userType, status);
	}

	public static void changeUserLicenseType(int userPk) throws Exception 
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		accountService.changeUserLicenseType(userPk);

            con.commit();
            
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	}

	public static List<User> searchUser(String searchString) 
	{
		return accountService.searchUser(searchString);
	}

	public static UserQuery getUserQuery(int userPk) 
	{
		return accountService.getUserQuery(userPk);
	}

	public static void createVerificationCodeForPasswordReset(User userToResetPasswordFor, User sendEmailTo)throws Exception
	{
        Connection con = null;
        
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		UserPasswordResetKey key = accountService.createUserPasswordResetKey(userToResetPasswordFor, sendEmailTo);
    		

            StringBuffer messageTextSb = new StringBuffer("Hi ");
            messageTextSb.append("\n\tA password reset request was submitted on behalf of {NameOfUser} ({userName}). ");
            messageTextSb.append("\n\tThe authentication code for the same is {key}");
            messageTextSb.append("\n\tClick 'Forgot password' on TestSutra login page and choose Reset password with authentication code option to reset your password.");
            messageTextSb.append("\n\t\n\tThank you");
            messageTextSb.append("\n\tTestSutra Support");

            String messageText = messageTextSb.toString();
            messageText = messageText.replace("{NameOfUser}", (userToResetPasswordFor.getFirstName() + " " + userToResetPasswordFor.getLastName()));
            messageText = messageText.replace("{userName}", (userToResetPasswordFor.getUserName()));
            messageText = messageText.replace("{key}", (key.getVerificationCode()));
            
            StringBuffer messageHtmlSb = new StringBuffer("Hi ");
            messageHtmlSb.append("<br/>A password reset request was submitted on behalf of {NameOfUser} ({userName}). ");
            messageHtmlSb.append("<br/>The authentication code for the same is {key}");
            messageHtmlSb.append("<br/>Click 'Forgot password' on TestSutra login page and choose Reset password with authentication code option to reset your password.");
            messageHtmlSb.append("<br/><br/>Thank you");
            messageHtmlSb.append("<br/>TestSutra Support");

            String messageHtml = messageHtmlSb.toString();
            messageHtml = messageHtml.replace("{NameOfUser}", (userToResetPasswordFor.getFirstName() + " " + userToResetPasswordFor.getLastName()));
            messageHtml = messageHtml.replace("{userName}", (userToResetPasswordFor.getUserName()));
            messageHtml = messageHtml.replace("{key}", (key.getVerificationCode()));

            EmailMessageInfo info = new EmailMessageInfo(ApplicationProperties.getEmailFromAddress(), null, new String[]{sendEmailTo.getEmail()},
    				"Password reset request", messageText, messageHtml, null);

            EmailServiceManager.scheduleEmail(info);
            
            con.commit();
            
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	}
	
	public static void resetUserPasswordWithVerificationCode(User user, String verificationKey, String newPassword) throws Exception 
	{
		//transaction managed inside the manager
    	accountService.resetUserPasswordWithVerificationCode(user, verificationKey, newPassword);
	}
	
	public static User updateUserEmail(UserContext context, String newEmail) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		User user = accountService.updateUserEmail(context, newEmail);

            con.commit();
            
            return user;
            
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	}
	
}
