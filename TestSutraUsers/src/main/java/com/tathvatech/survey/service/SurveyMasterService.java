package com.tathvatech.survey.service;

import com.tathvatech.common.enums.WorkItem;
import com.tathvatech.forms.common.FormFilter;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ObjectLockQuery;
import com.tathvatech.forms.entity.FormMain;
import com.tathvatech.forms.entity.FormPrintFormat;
import com.tathvatech.forms.entity.ObjectLock;
import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.pdf.config.PdfTemplatePrintLocationConfig;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.exception.LockedByAnotherUserException;
import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserQuery;

import java.util.HashMap;
import java.util.List;

public interface SurveyMasterService {

    String getSurveyDefFileName(int _surveyPk)
            throws Exception;

    boolean isSurveyNameExist(String surveyName)
            throws Exception;

    Survey getSurveyByPk(int surveyPk) throws Exception;

    Survey createSurvey(UserContext context, Survey survey) throws Exception;

    Survey createSurveyNewVersion(UserContext context, Survey newVersion, FormQuery baseRevision) throws Exception;

    Survey createSurveyByCopy(UserContext context, Survey survey, int sourceSurveyPk) throws Exception;

    void deleteSurvey(int surveyPk) throws Exception;

    Survey updateSurvey(Survey survey) throws Exception;

    FormQuery getFormByPk(int formPk);

    List<FormQuery> getSurveyList() throws Exception;

    List<FormQuery> getSurveyList(FormFilter filter);

    List<FormQuery> getOpenSurveyList() throws Exception;

    List<FormQuery> getAllVersionsForForm(int formMainPk);

    FormQuery getLatestVersionForForm(int formMainPk) throws Exception;

    FormQuery getLatestPublishedVersionOfForm(int formPk) throws Exception;

    FormQuery getLatestPublishedVersionForForm(int formMainPk) throws Exception;

    void deleteSurveyVersion(UserContext context, int surveyVersionPk) throws Exception;

    FormMain getFormMain(FormQuery formQuery) throws Exception;

    void publishSurvey(UserContext context, int surveyPk,
                       List<ProjectOID> projectUpgradeList, HashMap<ProjectOID, User> projectNotificationMap, HashMap<ProjectOID, List<Integer>> formsUpgradeMap)throws Exception;

    void applyFormUpgradePublish(UserContext context, int surveyPk, List projectListToUpgrade, List unitFormsListToUpgrade)throws Exception;

    UserQuery getAttributionUser(WorkItem workItem)throws Exception;

    void setAttribution(UserContext context, WorkItem workItem, UserOID attributeToUserOID)throws Exception;

    void resetAttribution(UserContext context, WorkItem workItem)throws Exception;

    ObjectLock getObjectLock(int responseFk, int formSectionFk);

    ObjectLock lockSectionToEdit(UserContext context, User lockForUser, FormResponseOID responseOID, String sectionId)throws LockedByAnotherUserException, Exception;

    ObjectLockQuery getCurrentLock(FormResponseOID responseOID, String sectionId)throws Exception;

    List<String> getLockedSectionIds(User user, FormResponseOID responseOID)throws Exception;

    List<ObjectLock> getLockedSectionIds(FormResponseOID responseOID)throws Exception;

    boolean isSectionLocked(User user, FormResponseOID responseOID, String sectionId)throws Exception;

    void releaseSectionEditLock(UserContext context, User user, FormResponseOID responseOID, String sectionId)throws LockedByAnotherUserException, Exception;

    void releaseSectionEditLock(UserContext context, FormResponseOID responseOID, String sectionId)throws Exception;

    FormPrintFormat getFormPrintFormat(FormOID formOID);

    PdfTemplatePrintLocationConfig getFormPrintTemplateLocationConfig(FormOID formOID) throws Exception;

    PdfTemplatePrintLocationConfig saveFormPrintTemplateLocationConfig(UserContext context, FormOID formOID, PdfTemplatePrintLocationConfig config) throws Exception;
}
