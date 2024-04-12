package com.tathvatech.survey.controller;
/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.sql.Connection;

import java.util.HashMap;
import java.util.List;
import com.tathvatech.common.enums.WorkItem;
import com.tathvatech.forms.common.FormFilter;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ObjectLockQuery;
import com.tathvatech.forms.entity.FormMain;
import com.tathvatech.forms.entity.FormPrintFormat;
import com.tathvatech.forms.entity.ObjectLock;
import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.pdf.config.PdfTemplatePrintLocationConfig;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.exception.LockedByAnotherUserException;
import com.tathvatech.survey.service.SurveyMaster;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserQuery;
import com.tathvatech.workstation.service.WorkstationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SurveyDelegate {

    private static final Logger logger = LoggerFactory.getLogger(SurveyDelegate.class);
    private final SurveyMaster surveyMaster;
    private final SurveyResponseService surveyResponseService;
    private final WorkstationService workstationService;

    public SurveyDelegate(SurveyMaster surveyMaster, SurveyResponseService surveyResponseService, WorkstationService workstationService) {
        this.surveyMaster = surveyMaster;
        this.surveyResponseService = surveyResponseService;
        this.workstationService = workstationService;
    }

    //TODO:: implement a cache with this hashmap
    //private static HashMap surveyMap = new HashMap();

    /**
     * @param
     * @return
     */
    public String getSurveyDefFileName(int _surveyPk) throws Exception {
        return surveyMaster.getSurveyDefFileName(_surveyPk);
    }

    /**
     * @param surveyPk
     * @return
     */
    public Survey getSurveyByPk(int surveyPk) throws Exception {
        return surveyMaster.getSurveyByPk(surveyPk);
    }


    public Survey createSurvey(UserContext context, Survey survey) throws Exception {


        Survey sur = surveyMaster.createSurvey(context, survey);


        return sur;

    }

    public Survey createSurveyNewVersion(UserContext context, Survey newVersion, FormQuery baseRevision) throws Exception {


        Survey sur = surveyMaster.createSurveyNewVersion(context, newVersion, baseRevision);

        return sur;

    }

    public Survey createSurveyByCopy(UserContext context, Survey survey, int sourceSurveyPk) throws Exception {


        survey = surveyMaster.createSurveyByCopy(context, survey, sourceSurveyPk);


        return survey;

    }

    /**
     * mark the survey status as deleted.// the survey list will not show these items .
     * a complete removal should be done manually.
     *
     * @param
     */
    public void deleteSurvey(int surveyPk) throws Exception {


        surveyMaster.deleteSurvey(surveyPk);

    }

    public Survey updateSurvey(Survey survey) throws Exception {


        survey = surveyMaster.updateSurvey(survey);
        return survey;

    }


    public  FormQuery getFormByPk(int formPk) {
        return surveyMaster.getFormByPk(formPk);
    }

    public List<FormQuery> getSurveyList() throws Exception {
        return surveyMaster.getSurveyList();
    }

    public  List<FormQuery> getOpenSurveyList() throws Exception {
        return surveyMaster.getOpenSurveyList();
    }

    public  List<FormQuery> getSurveyList(FormFilter filter) {
        return surveyMaster.getSurveyList(filter);
    }

    public  List<FormQuery> getAllVersionsForForm(int formMainPk) {
        return surveyMaster.getAllVersionsForForm(formMainPk);
    }

    public  FormQuery getLatestVersionForForm(int formMainPk) throws Exception {
        return surveyMaster.getLatestVersionForForm(formMainPk);
    }

    public  FormQuery getLatestPublishedVersionForForm(int formMainPk) throws Exception {
        return surveyMaster.getLatestPublishedVersionForForm(formMainPk);
    }

    public  FormQuery getLatestPublishedVersionOfForm(int formPk) throws Exception {
        return surveyMaster.getLatestPublishedVersionOfForm(formPk);
    }

    public void deleteSurveyVersion(UserContext context, int surveyVersionPk) throws Exception {


        surveyMaster.deleteSurveyVersion(context, surveyVersionPk);


    }

    public FormMain getFormMain(FormQuery formQuery) throws Exception {
        return surveyMaster.getFormMain(formQuery);
    }

    public  void publishSurvey(UserContext context, int surveyPk,
                                     List<ProjectOID> projectUpgradeList, HashMap<ProjectOID, User> projectNotificationMap, HashMap<ProjectOID, List<Integer>> formsUpgradeMap) throws Exception {


        surveyMaster.publishSurvey(context, surveyPk, projectUpgradeList, projectNotificationMap, formsUpgradeMap);


    }

    public  void applyFormUpgradePublish(UserContext context, int surveyPk, List projectListToUpgrade, List unitFormsListToUpgrade) throws Exception {


        surveyMaster.applyFormUpgradePublish(context, surveyPk, projectListToUpgrade, unitFormsListToUpgrade);


    }

    public  UserQuery getAttributionUser(WorkItem workItem) throws Exception {
        return surveyMaster.getAttributionUser(workItem);
    }

    public  void setAttribution(UserContext context, WorkItem workItem, UserOID attributeToUserOID) throws Exception {


        surveyMaster.setAttribution(context, workItem, attributeToUserOID);


    }

    public void resetAttribution(UserContext context, WorkItem workItem) throws Exception {

        surveyMaster.resetAttribution(context, workItem);


    }

    public synchronized ObjectLock lockSectionToEdit(UserContext context, User lockForUser, FormResponseOID responseOID, String sectionId) throws LockedByAnotherUserException, Exception {

        ObjectLock l = surveyMaster.lockSectionToEdit(context, lockForUser, responseOID, sectionId);

        //record in workstation
        ResponseMasterNew respMaster = surveyResponseService.getResponseMaster((int) responseOID.getPk());
        workstationService.recordWorkstationFormLock(new TestProcOID(respMaster.getTestProcPk()));


        return l;
    }

    public  ObjectLockQuery getCurrentLock(FormResponseOID responseOID, String sectionId) throws Exception {
        return surveyMaster.getCurrentLock(responseOID, sectionId);
    }

    public  List<ObjectLock> getLockedSectionIds(FormResponseOID responseOID) throws Exception {
        return surveyMaster.getLockedSectionIds(responseOID);
    }

    public  List<String> getLockedSectionIds(User user, FormResponseOID responseOID) throws Exception {
        return surveyMaster.getLockedSectionIds(user, responseOID);
    }

    public  boolean isSectionLocked(UserContext context, User user, FormResponseOID responseOID, String sectionId) throws Exception {
        return surveyMaster.isSectionLocked(user, responseOID, sectionId);
    }

    public  void releaseSectionEditLock(UserContext context, User user, FormResponseOID responseOID, String sectionId) throws LockedByAnotherUserException, Exception {


            surveyMaster.releaseSectionEditLock(context, user, responseOID, sectionId);

            ResponseMasterNew respMaster = surveyResponseService.getResponseMaster((int) responseOID.getPk());
            workstationService.recordWorkstationFormUnlock(new TestProcOID(respMaster.getTestProcPk()));



    }

    /**
     * this is called by the admin useronly
     *
     * @param
     * @param
     * @param
     * @param sectionId
     * @throws Exception
     */
    public  void releaseSectionEditLock(UserContext context, FormResponseOID responseOID, String sectionId) throws Exception {

        surveyMaster.releaseSectionEditLock(context, responseOID, sectionId);

        ResponseMasterNew respMaster = surveyResponseService.getResponseMaster((int) responseOID.getPk());

       workstationService.recordWorkstationFormUnlock(new TestProcOID(respMaster.getTestProcPk()));


    }

    public  FormPrintFormat getFormPrintFormat(FormOID formOID) {
        return surveyMaster.getFormPrintFormat(formOID);
    }

    public  PdfTemplatePrintLocationConfig getFormPrintTemplateLocationConfig(FormOID formOID) throws Exception {
        return surveyMaster.getFormPrintTemplateLocationConfig(formOID);
    }

    public  PdfTemplatePrintLocationConfig saveFormPrintTemplateLocationConfig(UserContext context, FormOID formOID, PdfTemplatePrintLocationConfig config) throws Exception {


        PdfTemplatePrintLocationConfig c = surveyMaster.saveFormPrintTemplateLocationConfig(context, formOID, config);


        return c;
    }
}
