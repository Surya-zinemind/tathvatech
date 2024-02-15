package com.tathvatech.user.service;

import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.*;

import java.util.Collection;
import java.util.List;

public interface AccountService {
    Account createNewAccount(Account account) throws Exception;

    User createPrimaryUser(User user) throws Exception;

    User createAddonUser(UserContext context, User _user, AttachmentIntf profilePicAttachment, boolean sendWelcomeKit) throws Exception;

    User saveAddonUser(UserContext context, User user, AttachmentIntf profilePicAttachment) throws Exception;

    User updateCurrentUserProfile(UserContext context, User uVal) throws Exception;

    void activateUser(int userPk) throws Exception;

    void deleteAddonUser(int userPk) throws Exception;

    void deactivateUser(int userPk) throws Exception;

    long getCurrentAccountUserCount(UserContext context) throws Exception;

    void createGuestUserForAccount(Account account) throws Exception;

    User getGuestUser(Account account) throws Exception;

    List<User> getFormAssignableUsers() throws Exception;

    List getUserAssignableUsers(UserContext context) throws Exception;

    List getAllReportFilterUsers(UserContext context) throws Exception;

    List getUserAssignedUsers(UserContext context) throws Exception;
//	public  List getUserAssignedUsers(UserContext context, int userPk)throws Exception
//	{
//        List list = persistWrapper.readList(User.class, "select * from TAB_USER where pk in (select userPk from TAB_USER_MANAGER where managerPk=? and status !=?", userPk, Device.DELETED);
//        return list;
//	}

    User getUser(String userName);

    User getUser(long userPk);

    User login(String accountNo, String userName, String password) throws Exception;

    User loginWithPassPin(String accountNo, String userName, String passPin) throws Exception;

    Account getAccount(int accountPk) throws Exception;

    Account getAccountByAccountNo(String accountNo) throws Exception;

    Account findAccountByUserName(String userName) throws Exception;

    User findPrimaryUserByUserName(String userName) throws Exception;

    User findUserByUserName(String userName);

    User findUserByEmail(String email) throws Exception;

    User getPrimaryUser() throws Exception;

    List getAllUserPermissionsOnSurvey(UserContext context, String surveyPk) throws Exception;

    SurveyPerms getUserPermissionsOnSurvey(UserContext context, int userPk, int surveyPk) throws Exception;

    void removeAllPermissionsOnSurvey(UserContext context, int surveyPk) throws Exception;

    List getAllUserPermissions(UserContext context, int userPk) throws Exception;

    void setUserPermissions(UserContext context, int userPk, List permsList) throws Exception;

    void setSurveyPermissions(UserContext context, int surveyPk, List permsList) throws Exception;

    void removeUsersFromManager(UserContext context, int managerPk) throws Exception;

    void addUsersToManager(UserContext context, int managerPk, Integer[] userPks) throws Exception;

    void cancelAccount(Account acc) throws Exception;

    void activateAccount(Account acc) throws Exception;

    void sendPassword(UserContext context, String userName) throws Exception;

    void markAccountAsPaymentPending(Account account) throws Exception;

    void addAccountNote(UserContext context, AccountNote aNote) throws Exception;

    void changePassword(UserContext context, String currentPassword, String newPassword) throws Exception;

    void changePassPin(UserContext context, String currentPassPin, String newPassPin) throws Exception;

    void adminChangePassword(UserContext context, User user, String password) throws Exception;

    void adminChangePassPin(UserContext context, User user, String pin) throws Exception;

    void addAccountAlert(Account account, String alertString) throws Exception;

    void dismissAlert(UserContext context, long alertPk) throws Exception;

    void changeAddonUserPassword(UserContext context,
                                 User user, String password) throws Exception;

    void updateAccount(Account account) throws Exception;

    List<User> getUserList() throws Exception;

    List<User> getValidUserList();

    void setUserPermissions(int entityPk, int entityType, Collection<User> userList, String role) throws Exception;

    void setUserPermissions(int projectPk, Collection<User> [] userLists, String[] roles) throws Exception;

    void setUserPermissions(int projectPk, Collection userList, String role) throws Exception;

    boolean isUserInRole(UserOID userOID, int objectPk, int objectType, String[] roles);

    List<User> getACLs(int objectPk, int objectType, String role) throws Exception;

    List<Integer> getObjectPksWithRoleForUser(UserOID userOID, int objectType, String role) throws Exception;

    int getUserCountfromTypeAndStatus(String userType,
                                      String status) throws Exception;

    void changeUserLicenseType(int userPk) throws Exception;

    List<User> searchUser(String searchString);

    UserQuery getUserQuery(int userPk);

    UserPasswordResetKey createUserPasswordResetKey(User user, User sendKeyToUser) throws Exception;

    void resetUserPasswordWithVerificationCode(User user, String verificationKey, String newPassword) throws Exception;

    User updateUserEmail(UserContext context, String newEmail) throws Exception;
}
