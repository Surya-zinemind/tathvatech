/*
 * Created on Nov 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.controller;


import com.tathvatech.common.common.ApplicationConstants;
import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.common.ServiceLocator;
import com.tathvatech.common.email.EmailMessageInfo;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.exception.LoginFailedException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.Asynch.AsyncProcessor;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.*;
import com.tathvatech.user.request.*;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.EmailServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@RequestMapping("/account")
@RestController
@RequiredArgsConstructor
public class AccountController
{
    private final  AccountService accountService;

    private final  PersistWrapper persistWrapper;
    

	public User createNewAccount(UserContext context, Account accVal, User uVal)throws Exception
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

	public  void createAddonUser(UserContext context, User userVal, AttachmentIntf profilePicAttachment, boolean sendWelcomeKit)throws Exception
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

	public  User updateCurrentUserProfile(UserContext context, User userVal)throws Exception
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

	public  void saveAddonUser(UserContext context, User userVal, AttachmentIntf profilePicAttachment)throws Exception
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

    @DeleteMapping("/andonUser")
	public  ResponseEntity<?> deleteAddonUser(int userPk)throws Exception
	{
            accountService.deleteAddonUser(userPk);
            return (ResponseEntity<?>) ResponseEntity.noContent();

	}
    @PutMapping("/activateUser")
    public  void activateUser(int userPk)throws Exception
    {

            accountService.activateUser(userPk);

    }

    /**
     * this also backs up the account logo if different and not null
     * @param acc
     */
//    public  void updateAccount(Account acc)throws Exception
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
     * All users including operator users are allowed to login using this call. 
     * this is called by timetracker checkin and checkout to validate the user.
     * @param userName
     * @param password could be passPin or the password
     * @return
     */
    @PostMapping("/loginFromDevice")
    public ResponseEntity<User>  loginFromDevice(@RequestBody LoginWithPassPinRequest loginWithPassPinRequest)throws Exception
    {
    	String loginMessage = null;
        User user = null;
        try
		{
            user = accountService.loginWithPassPin(loginWithPassPinRequest.getAccountNo(), loginWithPassPinRequest.getUsername(), loginWithPassPinRequest.getPassPin());
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
            	user = accountService.login(loginWithPassPinRequest.getAccountNo(), loginWithPassPinRequest.getUsername(), loginWithPassPinRequest.getPassPin());
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
        	return ResponseEntity.ok(user);
    }
    
    /**
     * @param accountPk
     * @return
     */
    @GetMapping
    public ResponseEntity<Account> getAccount(int accountPk)throws Exception
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
       	return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNo}")
	public  ResponseEntity<Account> getAccountByAccountNo(@PathVariable("accountNo") String accountNo) throws Exception
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
       	return ResponseEntity.ok(account);
	}


	/**
     * @param userPk
     * @return
     */
    @GetMapping("/user/{pk}")
    public ResponseEntity<User>  getUser(@PathVariable("pk") int userPk)
    {
        User user = accountService.getUser(userPk);
    	return ResponseEntity.ok(user) ;
    }


    @GetMapping("/user")
    public  ResponseEntity<User> findPrimaryUserByUserName(String userName)throws Exception
    {
        User user = accountService.findPrimaryUserByUserName(userName);

       	return ResponseEntity.ok(user);
    }

    @GetMapping("/accountForUser")
    public  ResponseEntity<Account> getAccountForUser(@RequestBody User user)throws Exception
    {
        Account account = (Account) persistWrapper.readByPrimaryKey(Account.class, user.getAccountPk());
        return ResponseEntity.ok(account);
    }

    @GetMapping("/primaryUser")
	public  ResponseEntity<User>  getPrimaryUser()throws Exception
	{
        User user = (User) accountService.getPrimaryUser();
		return ResponseEntity.ok(user);
	}

	public  List getAllUserPermissionsOnSurvey(UserContext context, String surveyPk)throws Exception
	{
		return accountService.getAllUserPermissionsOnSurvey(context, surveyPk);
	}

	public  SurveyPerms getUserPermissionsOnSurvey(UserContext context, int userPk, int surveyPk)throws Exception
	{
		return accountService.getUserPermissionsOnSurvey(context, userPk, surveyPk);
	}

	public  void removeAllPermissionsOnSurvey(UserContext context, int surveyPk)throws Exception
	{
        accountService.removeAllPermissionsOnSurvey(context, surveyPk);
	}

	public  List getAllUserPermissions(UserContext context, int userPk)throws Exception
	{
		// TODO Auto-generated method stub
		return accountService.getAllUserPermissions(context, userPk);
	}

	public  void setUserPermissions(UserContext context, int userPk, List permsList)throws Exception
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

	public  void setSurveyPermissions(UserContext context, int surveyPk, List permsList)throws Exception
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

    public  void setUserDevices(UserContext context, int managerPk, Integer[] userPks)throws Exception
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
    @PostMapping("/cancelAccount")
    public  void cancelAccount(@RequestBody Account acc)throws Exception
    {
//            PaymentManager.invalidateAccountSubscriptions(acc);
            accountService.cancelAccount(acc);
    }

    /**
     * @param acc
     */
    @PostMapping("/activateAccount")
    public  void activateAccount(@RequestBody Account acc)throws Exception
    {
            accountService.activateAccount(acc);

    }

    /**
     * @param userId
     */
    public  void sendPassword(UserContext context, String userId)throws Exception
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


//    public  int chooseSubscriptionPlan(Account account, PaymentOption selectedOption, PaymentMethod paymentMethod, PaymentMethodConfig methodConfig)throws Exception
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

    @PutMapping("/markAccountAsPaymentPending")
	public  void markAccountAsPaymentPending(@RequestBody Account account)throws Exception
	{
        accountService.markAccountAsPaymentPending(account);
	}

	public  void addAccountNote(UserContext context, AccountNote noteValue)throws Exception
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


    @PostMapping("/addAccountAlert")
	public  void addAccountAlert(@RequestBody AccountAlertRequest accountAlertRequest)throws Exception
	{
    		accountService.addAccountAlert(accountAlertRequest.getAccount(), accountAlertRequest.getText());

	}


	public  void changePassword(UserContext context, String currentPassword, String newPassword)throws Exception
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

	public  void changePassPin(UserContext context, String currentPassPin, String newPassPin)throws Exception
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



	public  void adminChangePassword(UserContext context, User user, String password, boolean notifyUser)throws Exception
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


	public  void adminChangePassPin(UserContext context, User user, String passPin, boolean notifyUser)throws Exception
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


	public  void dismissAlert(UserContext context, String alertPk) throws Exception
	{
		accountService.dismissAlert(context, Long.parseLong(alertPk));
	}

    @PostMapping("/guestUser")
	public ResponseEntity<User>  getGuestUser(Account account)throws Exception
	{
        User user = accountService.getGuestUser(account);
		return ResponseEntity.ok(user);
	}

    @GetMapping("/userByUsername")
	public ResponseEntity<User>  getUser(String userName)
	{
        User user = accountService.getUser(userName);
        return ResponseEntity.ok(user);
	}

    @GetMapping("/formAssignableUsers")
    public ResponseEntity<List<User>>  getFormAssignableUsers()throws Exception
    {
        List<User> users = accountService.getFormAssignableUsers();
    	return ResponseEntity.ok(users);
    }

    public  List getUserAssignableUsers(UserContext context)throws Exception
    {
    	return accountService.getUserAssignableUsers(context);
    }

//	public  List getUserAssignedUsers(UserContext context)throws Exception
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
//	public  List getUserAssignedUsers(UserContext context, int userPk)throws Exception
//	{
//		return AccountManager.getUserAssignedUsers(context, userPk);
//	}

    public  long getCurrentAccountUserCount(UserContext context)throws Exception
    {
        return accountService.getCurrentAccountUserCount(context);
    }

	public  void changeAddonUserPassword(UserContext context,
			User user, String password)throws Exception
	{
		accountService.changeAddonUserPassword(context, user, password);
	}

    @PutMapping("/updateAccount")
	public  void updateAccount(@RequestBody Account account) throws Exception {
		accountService.updateAccount(account);
	}

    @GetMapping("/users")
	public ResponseEntity<List<User>>  getUserList()throws Exception
	{
        List<User> users =  accountService.getUserList();
		return ResponseEntity.ok(users);
	}

    @GetMapping("/validUsers")
	public  ResponseEntity<List<User>> getValidUserList()
	{
        List<User> users = accountService.getValidUserList();
        return ResponseEntity.ok(users);
	}

    @PostMapping("/setUserPermissions")
	public  void setUserPermissions(@RequestBody UserPermissionsRequest userPermissionsRequest)throws Exception
	{
    	    accountService.setUserPermissions(userPermissionsRequest.getEntityPk(), userPermissionsRequest.getEntityType(), userPermissionsRequest.getUserList(), userPermissionsRequest.getRole());

	}

    @PostMapping("/setUserPermissionsByProject")
	public  void setUserPermissions(@RequestBody UserProjectPermissionsRequest userProjectPermissionsRequest)throws Exception
	{

            for (int i = 0; i < userProjectPermissionsRequest.getUserLists().length; i++)
			{
        	    accountService.setUserPermissions(userProjectPermissionsRequest.getProjectPk(), userProjectPermissionsRequest.getUserLists()[i], userProjectPermissionsRequest.getRoles()[i]);
			}

	}

    @PostMapping("/isUserInRole")
	public ResponseEntity<Boolean>  isUserInRole(@RequestBody UserInRoleRequest userInRoleRequest)
	{
        Boolean isUserInRole = accountService.isUserInRole(userInRoleRequest.getUserOID(), userInRoleRequest.getObjectPk(), userInRoleRequest.getObjectType(), userInRoleRequest.getRoles());
		return ResponseEntity.ok(isUserInRole);
	}

    @PostMapping("/acls")
	public ResponseEntity<List<User>>  getACLs(@RequestBody ACLRequest aclRequest) throws Exception
	{
        List<User> users = accountService.getACLs(aclRequest.getPk(), aclRequest.getObjectTypeProject(), aclRequest.getRoleManager());
		return ResponseEntity.ok(users);
	}
	
	/**
	 * To get the count of users with pirticular type and status.
	 * @param userType
	 * @param status
	 * @return int
	 * @throws Exception
	 */
    @PostMapping ("/getUserCount")
	public  ResponseEntity<Integer> getUserCountfromTypeAndStatus(@RequestBody UserCountByTypeRequest userCountByTypeRequest) throws Exception {
        Integer count = accountService.getUserCountfromTypeAndStatus(userCountByTypeRequest.getUserType(), userCountByTypeRequest.getStatus());
		return ResponseEntity.ok(count);
	}

    @PutMapping("/changeUserLicenseType")
	public  void changeUserLicenseType(int userPk) throws Exception
	{
    		accountService.changeUserLicenseType(userPk);
	}

    @GetMapping("/searchUser")
	public ResponseEntity<List<User>> searchUser(String searchString)
	{
        List<User> users = accountService.searchUser(searchString);
        return ResponseEntity.ok(users);
	}

    @GetMapping("/getUserQuery")
	public ResponseEntity<UserQuery>  getUserQuery(int userPk)
	{
        UserQuery userQuery = accountService.getUserQuery(userPk);
		return ResponseEntity.ok(userQuery);
	}

    @PostMapping("/createVerificationCodeForPasswordReset")
	public  void createVerificationCodeForPasswordReset(@RequestBody VerificationCodeRequest verificationCodeRequest)throws Exception
	{


    		UserPasswordResetKey key = accountService.createUserPasswordResetKey(verificationCodeRequest.getUserToResetPasswordFor(), verificationCodeRequest.getSendEmailTo());
    		

            StringBuffer messageTextSb = new StringBuffer("Hi ");
            messageTextSb.append("\n\tA password reset request was submitted on behalf of {NameOfUser} ({userName}). ");
            messageTextSb.append("\n\tThe authentication code for the same is {key}");
            messageTextSb.append("\n\tClick 'Forgot password' on TestSutra login page and choose Reset password with authentication code option to reset your password.");
            messageTextSb.append("\n\t\n\tThank you");
            messageTextSb.append("\n\tTestSutra Support");

            String messageText = messageTextSb.toString();
            messageText = messageText.replace("{NameOfUser}", (verificationCodeRequest.getUserToResetPasswordFor().getFirstName() + " " + verificationCodeRequest.getUserToResetPasswordFor().getLastName()));
            messageText = messageText.replace("{userName}", (verificationCodeRequest.getUserToResetPasswordFor().getUserName()));
            messageText = messageText.replace("{key}", (key.getVerificationCode()));
            
            StringBuffer messageHtmlSb = new StringBuffer("Hi ");
            messageHtmlSb.append("<br/>A password reset request was submitted on behalf of {NameOfUser} ({userName}). ");
            messageHtmlSb.append("<br/>The authentication code for the same is {key}");
            messageHtmlSb.append("<br/>Click 'Forgot password' on TestSutra login page and choose Reset password with authentication code option to reset your password.");
            messageHtmlSb.append("<br/><br/>Thank you");
            messageHtmlSb.append("<br/>TestSutra Support");

            String messageHtml = messageHtmlSb.toString();
            messageHtml = messageHtml.replace("{NameOfUser}", (verificationCodeRequest.getUserToResetPasswordFor().getFirstName() + " " + verificationCodeRequest.getUserToResetPasswordFor().getLastName()));
            messageHtml = messageHtml.replace("{userName}", (verificationCodeRequest.getUserToResetPasswordFor().getUserName()));
            messageHtml = messageHtml.replace("{key}", (key.getVerificationCode()));

            EmailMessageInfo info = new EmailMessageInfo(ApplicationProperties.getEmailFromAddress(), null, new String[]{verificationCodeRequest.getSendEmailTo().getEmail()},
    				"Password reset request", messageText, messageHtml, null);

            EmailServiceManager.scheduleEmail(info);

	}

    @PostMapping("/resetUserPasswordWithVerificationCode")
	public  void resetUserPasswordWithVerificationCode(@RequestBody ResetUserPasswordRequest resetUserPasswordRequest) throws Exception
	{
		//transaction managed inside the manager
    	accountService.resetUserPasswordWithVerificationCode(resetUserPasswordRequest.getUser(), resetUserPasswordRequest.getVerificationKey(), resetUserPasswordRequest.getNewPassword());
	}
	
	public  User updateUserEmail(UserContext context, String newEmail) throws Exception
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
