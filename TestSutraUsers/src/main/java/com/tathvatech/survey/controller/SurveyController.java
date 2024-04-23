package com.tathvatech.survey.controller;
/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


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
import com.tathvatech.survey.Request.*;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.exception.LockedByAnotherUserException;
import com.tathvatech.survey.service.SurveyMasterService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/survey")
public class SurveyController {

    private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);
    private final SurveyMasterService surveyMasterService;
    private final SurveyResponseService surveyResponseService;
    private final WorkstationService workstationService;

    public SurveyController(SurveyMasterService surveyMasterService, SurveyResponseService surveyResponseService, WorkstationService workstationService) {
        this.surveyMasterService = surveyMasterService;
        this.surveyResponseService = surveyResponseService;
        this.workstationService = workstationService;
    }

    //TODO:: implement a cache with this hashmap
    //private static HashMap surveyMap = new HashMap();

    /**
     * @param
     * @return
     */
    @GetMapping("/getSurveyDefFileName/{_surveyPk}")
    public String getSurveyDefFileName(@PathVariable("_surveyPk") int _surveyPk) throws Exception {
        return surveyMasterService.getSurveyDefFileName(_surveyPk);
    }

    /**
     * @param surveyPk
     * @return
     */
   @GetMapping("/getSurveyByPk/{surveyPk}")
   public Survey getSurveyByPk(@PathVariable("surveyPk") int surveyPk) throws Exception {
        return surveyMasterService.getSurveyByPk(surveyPk);
    }


   @PostMapping("/createSurvey")
   public Survey createSurvey(@RequestBody Survey survey) throws Exception {

       UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Survey sur = surveyMasterService.createSurvey(context, survey);


        return sur;

    }

    @PostMapping("/createSurveyNewVersion")
    public Survey createSurveyNewVersion( @RequestBody CreateSurveyNewVersionRequest createSurveyNewVersionRequest) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Survey sur = surveyMasterService.createSurveyNewVersion(context, createSurveyNewVersionRequest.getNewVersion(), createSurveyNewVersionRequest.getBaseRevision());

        return sur;

    }

   @PostMapping("/createSurveyByCopy")
   public Survey createSurveyByCopy( @RequestBody CreateSurveyByCopyRequest createSurveyByCopyRequest) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Survey survey = surveyMasterService.createSurveyByCopy(context, createSurveyByCopyRequest.getSurvey(), createSurveyByCopyRequest.getSourceSurveyPk());
       return survey;

    }

    /**
     * mark the survey status as deleted.// the survey list will not show these items .
     * a complete removal should be done manually.
     *
     * @param
     */
    @DeleteMapping("/deleteSurvey/{surveyPk}")
    public void deleteSurvey(@PathVariable("surveyPk") int surveyPk) throws Exception {


        surveyMasterService.deleteSurvey(surveyPk);

    }

    @PutMapping("/updateSurvey")
    public Survey updateSurvey(@RequestBody Survey survey) throws Exception {


        survey = surveyMasterService.updateSurvey(survey);
        return survey;

    }


    @GetMapping("/getFormByPk/{formPk}")
    public  FormQuery getFormByPk(@PathVariable("formPk") int formPk) {
        return surveyMasterService.getFormByPk(formPk);
    }

    @GetMapping("/getSurveyList")
    public List<FormQuery> getSurveyList() throws Exception {
        return surveyMasterService.getSurveyList();
    }

    @GetMapping("/getOpenSurveyList")
    public  List<FormQuery> getOpenSurveyList() throws Exception {
        return surveyMasterService.getOpenSurveyList();
    }

    @GetMapping("/getSurveyLists")
    public  List<FormQuery> getSurveyList(@RequestBody FormFilter filter) {
        return surveyMasterService.getSurveyList(filter);
    }

    @GetMapping("/getAllVersionsForForm/{formMainPk}")
    public  List<FormQuery> getAllVersionsForForm(@PathVariable("formMainPk") int formMainPk) {
        return surveyMasterService.getAllVersionsForForm(formMainPk);
    }

    @GetMapping("/getLatestVersionForForm/{formMainPk}")
    public  FormQuery getLatestVersionForForm(@PathVariable("formMainPk") int formMainPk) throws Exception {
        return surveyMasterService.getLatestVersionForForm(formMainPk);
    }

    @GetMapping("/getLatestPublishedVersionForForm/{formMainPk}")
    public  FormQuery getLatestPublishedVersionForForm(@PathVariable("formMainPk") int formMainPk) throws Exception {
        return surveyMasterService.getLatestPublishedVersionForForm(formMainPk);
    }

    @GetMapping("/getLatestPublishedVersionOfForm/{formPk}")
    public  FormQuery getLatestPublishedVersionOfForm(@PathVariable("formPk") int formPk) throws Exception {
        return surveyMasterService.getLatestPublishedVersionOfForm(formPk);
    }

    @DeleteMapping("/deleteSurveyVersion/{surveyVersionPk}")
    public void deleteSurveyVersion(@PathVariable("surveyVersionPk") int surveyVersionPk) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        surveyMasterService.deleteSurveyVersion(context, surveyVersionPk);


    }

    @GetMapping("/getFormMain")
    public FormMain getFormMain(@RequestBody FormQuery formQuery) throws Exception {
        return surveyMasterService.getFormMain(formQuery);
    }

    @PutMapping("/publishSurvey")
    public  void publishSurvey( @RequestBody PublishSurveyRequest publishSurveyRequest) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        surveyMasterService.publishSurvey(context, publishSurveyRequest.getSurveyPk(), publishSurveyRequest.getProjectUpgradeList(),publishSurveyRequest.getProjectNotificationMap(), publishSurveyRequest.getFormsUpgradeMap());


    }

    @PutMapping("/applyFormUpgradePublish")
    public  void applyFormUpgradePublish( @RequestBody ApplyFormUpgradePublishRequest applyFormUpgradePublishRequest) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        surveyMasterService.applyFormUpgradePublish(context,applyFormUpgradePublishRequest.getSurveyPk(), applyFormUpgradePublishRequest.getProjectListToUpgrade(),applyFormUpgradePublishRequest.getUnitFormsListToUpgrade());


    }

    @GetMapping("/getAttributionUser")
    public  UserQuery getAttributionUser(@RequestBody WorkItem workItem) throws Exception {
        return surveyMasterService.getAttributionUser(workItem);
    }

    @PutMapping("/setAttribution")
    public  void setAttribution( @RequestBody SetAttributionRequest setAttributionRequest) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        surveyMasterService.setAttribution(context,setAttributionRequest.getWorkItem(),setAttributionRequest.getAttributeToUserOID());


    }

    @PutMapping("/resetAttribution")
    public void resetAttribution(@RequestBody WorkItem workItem) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        surveyMasterService.resetAttribution(context, workItem);


    }

    @PostMapping("/lockSectionToEdit")
    public synchronized ObjectLock lockSectionToEdit( @RequestBody LockSectionToEditRequest lockSectionToEditRequest) throws LockedByAnotherUserException, Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectLock l = surveyMasterService.lockSectionToEdit(context,lockSectionToEditRequest.getLockForUser(), lockSectionToEditRequest.getResponseOID(),lockSectionToEditRequest.getSectionId());

        //record in workstation
        ResponseMasterNew respMaster = surveyResponseService.getResponseMaster((int)lockSectionToEditRequest.getResponseOID().getPk());
        workstationService.recordWorkstationFormLock(new TestProcOID(respMaster.getTestProcPk()));


        return l;
    }

    @GetMapping("/getCurrentLock")
    public  ObjectLockQuery getCurrentLock(@RequestBody GetCurrentLockRequest getCurrentLockRequest) throws Exception {
        return surveyMasterService.getCurrentLock(getCurrentLockRequest.getResponseOID(), getCurrentLockRequest.getSectionId());
    }

    @GetMapping("/getLockedSectionIds")
    public  List<ObjectLock> getLockedSectionIds(@RequestBody FormResponseOID responseOID) throws Exception {
        return surveyMasterService.getLockedSectionIds(responseOID);
    }

    @GetMapping("/getLockedSectionIdss")
    public  List<String> getLockedSectionIds(@RequestBody GetLockedSectionIdsRequest getLockedSectionIdsRequest) throws Exception {
        return surveyMasterService.getLockedSectionIds(getLockedSectionIdsRequest.getUser(), getLockedSectionIdsRequest.getResponseOID());
    }

    @GetMapping("/isSectionLocked")
    public  boolean isSectionLocked( @RequestBody IsSectionLockedRequest isSectionLockedRequest) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return surveyMasterService.isSectionLocked(isSectionLockedRequest.getUser(), isSectionLockedRequest.getResponseOID(), isSectionLockedRequest.getSectionId());
    }

    @DeleteMapping("/releaseSectionEditLock")
    public  void releaseSectionEditLock( @RequestBody ReleaseSectionEditLocksRequest releaseSectionEditLocksRequest) throws LockedByAnotherUserException, Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            surveyMasterService.releaseSectionEditLock(context, releaseSectionEditLocksRequest.getUser(), releaseSectionEditLocksRequest.getResponseOID(), releaseSectionEditLocksRequest.getSectionId());

            ResponseMasterNew respMaster = surveyResponseService.getResponseMaster((int) releaseSectionEditLocksRequest.getResponseOID().getPk());
            workstationService.recordWorkstationFormUnlock(new TestProcOID(respMaster.getTestProcPk()));



    }

    /**
     * this is called by the admin useronly
     *
     * @param
     * @param
     * @param
     * @param
     * @throws Exception
     */
    @DeleteMapping("/releaseSectionEditLocks")
    public  void releaseSectionEditLock( @RequestBody  ReleaseSectionEditLockRequest releaseSectionEditLockRequest) throws Exception {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        surveyMasterService.releaseSectionEditLock(context, releaseSectionEditLockRequest.getResponseOID(), releaseSectionEditLockRequest.getSectionId());

        ResponseMasterNew respMaster = surveyResponseService.getResponseMaster((int) releaseSectionEditLockRequest.getResponseOID().getPk());

       workstationService.recordWorkstationFormUnlock(new TestProcOID(respMaster.getTestProcPk()));


    }

    @GetMapping("/getFormPrintFormat")
    public  FormPrintFormat getFormPrintFormat(@RequestBody FormOID formOID) {
        return surveyMasterService.getFormPrintFormat(formOID);
    }

    @GetMapping("/getFormPrintTemplateLocationConfig")
    public  PdfTemplatePrintLocationConfig getFormPrintTemplateLocationConfig(@RequestBody FormOID formOID) throws Exception {
        return surveyMasterService.getFormPrintTemplateLocationConfig(formOID);
    }

    @PostMapping("/saveFormPrintTemplateLocationConfig")
    public  PdfTemplatePrintLocationConfig saveFormPrintTemplateLocationConfig( @RequestBody SaveFormPrintTemplateLocationConfigRequest saveFormPrintTemplateLocationConfigRequest
                                                                                ) throws Exception {

        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PdfTemplatePrintLocationConfig c = surveyMasterService.saveFormPrintTemplateLocationConfig(context, saveFormPrintTemplateLocationConfigRequest.getFormOID(), saveFormPrintTemplateLocationConfigRequest.getConfig());


        return c;
    }
}
