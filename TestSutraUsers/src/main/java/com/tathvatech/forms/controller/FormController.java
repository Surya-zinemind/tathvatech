package com.tathvatech.forms.controller;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.exception.RestAppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.*;
import com.tathvatech.site.service.SiteService;
import com.tathvatech.survey.service.SurveyDefFactory;
import com.tathvatech.forms.entity.FormSection;
import com.tathvatech.forms.entity.ObjectLock;
import com.tathvatech.forms.enums.FormStatusEnum;
import com.tathvatech.forms.report.TestProcListReport;
import com.tathvatech.forms.report.TestProcStatusSummaryReport;
import com.tathvatech.forms.request.*;
import com.tathvatech.forms.response.*;
import com.tathvatech.forms.service.FormDBManager;
import com.tathvatech.forms.service.TestProcService;
import com.tathvatech.ncr.controller.NcrDelegate;
import com.tathvatech.common.entity.EntityReference;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.forms.service.FormService;
import com.tathvatech.ncr.common.NcrItemQuery;
import com.tathvatech.forms.processor.FormUpgradeRevertProcessor;
import com.tathvatech.ncr.oid.NcrItemOID;
import com.tathvatech.project.entity.Project;
import com.tathvatech.project.enums.ProjectPropertyEnum;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.survey.common.SignatureCaptureAnswerType;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.common.SurveyItem;
import com.tathvatech.survey.controller.SurveyDelegate;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.enums.SectionLockStatusEnum;
import com.tathvatech.survey.exception.LockedByAnotherUserException;
import com.tathvatech.survey.service.SurveyMaster;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.timetracker.entity.Workorder;
import com.tathvatech.timetracker.service.WorkorderManager;
import com.tathvatech.unit.common.UnitEntityQuery;
import com.tathvatech.unit.service.UnitManager;
import com.tathvatech.unit.service.UnitService;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.DateRangeFilter;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.CommonServicesDelegate;
import com.tathvatech.user.utils.DateUtils;
import com.tathvatech.workstation.common.DummyWorkstation;
import com.tathvatech.workstation.entity.Workstation;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import com.tathvatech.unit.common.UnitFormQuery;
import org.jdom2.Document;
import org.jdom2.Element;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@RequiredArgsConstructor
public class FormController {

   private final FormService formService;
   private final CommonServicesDelegate commonServicesDelegate;
   private final AccountService accountService;
   private final FormUpgradeRevertProcessor formUpgradeRevertProcessor;
   private final NcrDelegate ncrDelegate;
   private final ProjectService projectService;
   private final WorkorderManager workorderManager;
    private final TestProcService testProcService;
   private final PersistWrapper persistWrapper;

   private final UnitManager unitManager;
   private final UnitService unitService;
   private final DummyWorkstation dummyWorkstation;
   private final SurveyResponseService surveyResponseService;
   private final SurveyMaster surveyMaster;
   private final SurveyDelegate surveyDelegate;
   private final SiteService siteService;
   private final FormDBManager formDBManager;
   private final WorkstationService workstationService;
   private final SurveyDefFactory surveyDefFactory;

    @PostMapping("/saveTestProcSchedule")
    public  void saveTestProcSchedule(@RequestBody SaveTestProcScheduleRequest saveTestProcScheduleRequest) throws Exception
    {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        formService.saveTestProcSchedule(context, saveTestProcScheduleRequest.getTestProcOID(), saveTestProcScheduleRequest.getObjectScheduleRequestBean());
    }

    @PostMapping("/saveTestProcSchedules")
    public  void saveTestProcSchedules( @RequestBody SaveTestProcSchedulesRequest saveTestProcSchedulesRequest) throws Exception
    {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        formService.saveTestProcSchedules(context,saveTestProcSchedulesRequest.getProjectOID(), saveTestProcSchedulesRequest.getRootUnit(), saveTestProcSchedulesRequest.getScheduleList());

    }

    @PostMapping("/moveTestProcsToUnit")
    public  void moveTestProcsToUnit( @RequestBody MoveTestProcsToUnitRequest moveTestProcsToUnitRequest) throws Exception
    {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        formService.moveTestProcsToUnit(userContext, moveTestProcsToUnitRequest.getTestProcsToMove(), moveTestProcsToUnitRequest.getUnitOIDToMoveTo(), moveTestProcsToUnitRequest.getProjectOIDToMoveTo());
    }

    @PutMapping("/renameTestForms")
    public  void renameTestForms(@RequestBody RenameTestFormsRequest  renameTestFormsRequest) throws Exception
    {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        formService.renameTestForms(userContext, renameTestFormsRequest.getSelectedTestProcs(), renameTestFormsRequest.getReferencesToAdd(), renameTestFormsRequest.getName(), renameTestFormsRequest.getRenameOption());

    }

    @GetMapping("/getReferencesForTestProc")
    public  List<EntityReferenceBean> getReferencesForTestProc(@RequestBody TestProcOID oid)
    {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntityReferenceBean> returnList = new ArrayList<EntityReferenceBean>();
        List<EntityReference> refList = commonServicesDelegate.getEntityReferences(oid);
        for (Iterator iterator = refList.iterator(); iterator.hasNext();)
        {
            EntityReference aRef = (EntityReference) iterator.next();

            EntityReferenceBean bean = new EntityReferenceBean();
            aRef.setCreatedDate(aRef.getCreatedDate());

            bean.setReferenceFromOID(oid);

            if (aRef.getReferenceToType() == EntityTypeEnum.NCR.getValue())
            {
                NcrItemQuery ncr =ncrDelegate.getNcrItemQuery(context, new NcrItemOID((int) aRef.getReferenceToPk()));
                if (ncr != null) // load user and add to list only if the
                // referenced object is there.
                {
                    bean.setReferenceToOID(
                            new NcrItemOID((int) aRef.getReferenceToPk(), ncr.getNcrGroupNo() + "." + ncr.getNcrNo()));
                    bean.setReferencedObject(ncr);

                    User user = accountService.getUser(aRef.getCreatedBy());
                    bean.setCreatedBy(user);

                    returnList.add(bean);
                }
            }
        }
        return returnList;
    }

    @PutMapping("/revertFormUpgradeOnTestProc")
    public  void revertFormUpgradeOnTestProc(@RequestBody RevertFormUpgradeOnTestProcRequest revertFormUpgradeOnTestProcRequest) throws Exception
    {
       formUpgradeRevertProcessor.process(revertFormUpgradeOnTestProcRequest.getTestprocOID(), revertFormUpgradeOnTestProcRequest.getCurrentFormOID(), revertFormUpgradeOnTestProcRequest.getRevertToFormOID());
    }
    @GetMapping("/getTestProcsByForm")
    public  List<UnitFormQuery> getTestProcsByForm(@RequestBody FormQuery formQuery) throws Exception
    {
        return formService.getTestProcsByForm(formQuery);
    }

   @PostMapping("/upgradeFormForUnit")
   public  TestProcObj upgradeFormForUnit( @RequestBody UpgradeFormForUnitRequest upgradeFormForUnitRequest)
            throws Exception
    {
        UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return formService.upgradeFormForUnit(context, upgradeFormForUnitRequest.getTestProcOID(), upgradeFormForUnitRequest.getSurveyPk());

    }

   @GetMapping("/getFormList")
   public List<TestProcBean> getFormList() throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            List<AssignedTestsQuery> forms =formService.getAssignedFormsNew(context.getUser().getOID(),
                    User.ROLE_TESTER);

            List<TestProcBean> tList = new ArrayList<TestProcBean>();

            HashMap<ProjectOID, ProjectConfigSettings> projectSettingsMap = new HashMap<ProjectOID, ProjectConfigSettings>();
            for (Iterator iterator = forms.iterator(); iterator.hasNext();)
            {
                AssignedTestsQuery aForm = (AssignedTestsQuery) iterator.next();

                TestProcBean tBean = new TestProcBean();
                tBean.setTestProcName(aForm.getTestProcName());
                tBean.setProjectPk(aForm.getProjectPk());
                tBean.setProjectName(aForm.getProjectName());
                tBean.setProjectDescription((aForm.getProjectDescription() == null)?"" : aForm.getProjectDescription());
                tBean.setItemPk(aForm.getUnitPk());
                tBean.setItemName(aForm.getUnitName());
                tBean.setItemDescription((aForm.getUnitDescription() == null )? "" : aForm.getUnitDescription());
                tBean.setWorkstationPk(aForm.getWorkstationPk());
                tBean.setWorkstationName(aForm.getWorkstationName());
                tBean.setWorkstationDescription((aForm.getWorkstationDescription() == null)?"":aForm.getWorkstationDescription());
                tBean.setWorkstationSiteName(aForm.getWorkstationSiteName());
                tBean.setPk(aForm.getTestProcPk());
                tBean.setFormPk(aForm.getFormPk());
                tBean.setFormName(aForm.getFormName());
                tBean.setFormDescription((aForm.getFormDescription() == null)?"" : aForm.getFormDescription());
                tBean.setVersionNo(aForm.getFormVersion());
                tBean.setFormRevision((aForm.getFormRevision()!=null)?aForm.getFormRevision():"");
                tBean.setAssignedDate(aForm.getAssignedDate());
                tBean.setResponseId(aForm.getResponseId());
                tBean.setStatus((aForm.getResponseStatus() == null) ? ResponseMasterNew.STATUS_NOTSTARTED : aForm.getResponseStatus());
                tBean.setPercentComplete(aForm.getPercentComplete());
                tBean.setNoOfComments(aForm.getCommentCount());
                tBean.setPassCount(aForm.getPassCount());
                tBean.setFailCount(aForm.getFailCount());
                tBean.setDimensionalFailCount(aForm.getDimentionalFailCount());
                tBean.setNaCount(aForm.getNaCount());
                tBean.setComment(aForm.getComment());
                tBean.setTotalQCount(aForm.getTotalQCount());
                tBean.setTotalACount(aForm.getTotalACount());

                //if we load this for one project use it. else get it and save it in map to use it again.
                ProjectConfigSettings projectConfig =  projectSettingsMap.get(new ProjectOID(tBean.getProjectPk()));
                if(projectConfig == null || projectConfig.EnableInterimSubmitForChecksheets == null)
                {
                    Boolean enableInterimSubmitForChecksheets = (Boolean) commonServicesDelegate.getEntityPropertyValue(new ProjectOID(tBean.getProjectPk()), ProjectPropertyEnum.EnableInterimSubmitForChecksheets.getId(), Boolean.class);
                    if(projectConfig == null)
                    {
                        projectConfig = new ProjectConfigSettings();
                        projectSettingsMap.put(new ProjectOID(tBean.getProjectPk()), projectConfig);
                    }
                    projectConfig.EnableInterimSubmitForChecksheets = enableInterimSubmitForChecksheets;
                }

                tBean.setEnableInterimSubmit(projectConfig.EnableInterimSubmitForChecksheets);
                tBean.setPermissions(new String[] {User.ROLE_TESTER});

                tList.add(tBean);
            }

            return tList;
        }
        catch (Exception e)
        {

            throw e;
        }

    }


//commenting methods using vaadin component
    /*public FormBean getFormDetail(  int formPk) throws Exception
    {
         UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            // ResponseMasterNew respMaster =
            // SurveyResponseDelegate.getLatestResponseMasterForTest(testProc.getOID());
            SurveyDefinition surveyDefinition = SurveyDefFactory.getSurveyDefinition(new FormOID(formPk, null));
            FormQuery survey = SurveyDelegate.getFormByPk(formPk);
            if (surveyDefinition == null)
            {
                throw new RestAppException("MSG-FormNotFound");
            }
            FormBean formBean = new FormBean();
            formBean.setPk(survey.getPk());
            formBean.setName(survey.getIdentityNumber());
            formBean.setDescription1(survey.getDescription());
            formBean.setDescription2(survey.getDescription1());
            formBean.setRevision(survey.getRevision());
            formBean.setVersionNo(survey.getVersionNo());
            if (surveyDefinition.getFormInstructionFile() != null)
                formBean.setInstructionFile(surveyDefinition.getFormInstructionFile().getName());

            for (Object element : surveyDefinition.getQuestions())
            {
                SurveyItem sItem = (SurveyItem) element;
                if (sItem instanceof SurveySaveItem && ((SurveySaveItem) sItem).isHidden())
                {
                    continue;
                }

                if (sItem instanceof Section)
                {
                    FormItemBase item = getSectionBean(sItem, (User) context.getUser());
                    formBean.getSections().add((SectionBean) item);
                }
            }

            return formBean;
        }
        catch (Exception e)
        {

            throw e;
        }

    }*/
//commenting methods using vaadin component

   /* public Response getFormDetailLinear(
             int formPk) throws Exception
    {
         UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try
        {
            List<FormItemBase> formItems = new ArrayList<FormItemBase>();
            FormQuery survey = SurveyDelegate.getFormByPk(formPk);
            SurveyDefinition sd = SurveyDefFactory.getSurveyDefinition(new FormOID(formPk, null));
            if (sd == null)
            {
                throw new RestAppException("MSG-FormNotFound");
            }
            FormBeanLinear formBeanLinear = new FormBeanLinear();
            formBeanLinear.setPk(survey.getPk());
            User user = (User) context.getUser();
            if (user == null || survey == null)
            {
                logger.error("Exception while creating surveyXml for tablet, ");
                logger.error("User:" + ((user != null) ? user.getPk() : "null user"));
                logger.error("survey:" + ((survey != null) ? survey.getPk() : "null survey"));
            }
            formBeanLinear.setName(survey.getIdentityNumber());
            formBeanLinear.setDescription1(survey.getDescription());
            formBeanLinear.setDescription2(survey.getDescription1());
            formBeanLinear.setRevision(survey.getRevision());
            formBeanLinear.setVersionNo(survey.getVersionNo());

            if (sd.getFormInstructionFile() != null)
                formBeanLinear.setInstructionFile(sd.getFormInstructionFile().getName());

            for (Object element : sd.getQuestions())
            {
                SurveyItem sItem = (SurveyItem) element;
                if (sItem instanceof SurveySaveItem && ((SurveySaveItem) sItem).isHidden())
                {
                    continue;
                }

                if (sItem instanceof Section)
                {
                    FormItemBase item = getSectionBeanForFormLinear(formItems, sItem, user);
                    formItems.add(item);
                }
            }
            formBeanLinear.setFormItems(formItems);
            return Response.ok(formBeanLinear).build();
        }
        catch (Exception e)
        {

            throw e;
        }
    }*/

//commenting methods using vaadin component
    /*
     * Get form detail response bean
     */

   /* public FormResponseBean getFormDetailResponse(
            int testProcPk, int responseId)
            throws RestAppException, Exception
    {
         UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            // if the user already has a FormResponeBean pending review. he should keep working on that copy only.
            //only after that change is merged into the main response, he should access the main response from db.
            EntityVersionReviewProxy entityVersion = SurveyResponseDelegate.getEntityRevisionReviewProxy(context, responseId);
            if(entityVersion != null)
            {
                FormResponseBean responeBeanRevision = SurveyResponseDelegate.getFormResponseBeanForSyncErrorReview(entityVersion.getPk());
                return responeBeanRevision;
            }

            TestProcObj testProc = (TestProcObj) TestProcManager.getTestProc(testProcPk);
            ResponseMasterNew respMaster = SurveyResponseDelegate.getLatestResponseMasterForTest(testProc.getOID());
            if (testProc.getFormPk() != respMaster.getFormPk())
            {
                throw new RestAppException("Invalid Test form detected. Please contact your administrator");
            }
            if (responseId != respMaster.getResponseId())
            {
                throw new RestAppException("Invalid response detected, Please contact your administrator");
            }

            HashMap secParamsMap = new HashMap();
            secParamsMap.put("surveyPk", testProc.getFormPk());
            SurveyDefinition surveyDefinition = SurveyDefFactory
                    .getSurveyDefinition(new FormOID(testProc.getFormPk(), null));
            Survey survey = surveyDefinition.getSurveyConfig();
            SurveyResponse surveyResponse = SurveyResponseDelegate.getSurveyResponse(surveyDefinition, responseId);
            boolean treatFailsAsNotAnswered = (boolean) new CommonServicesDelegate().getEntityPropertyValue(new ProjectOID(testProc.getProjectPk()), ProjectPropertyEnum.TreatFailsAsNotAnswered.getId(), Boolean.class);
            HashMap<String, FormItemResponse> formItemResponseMap = SurveyResponseDelegate.getFormItemResponses(responseId);
            List<SectionResponseQuery> sectionSummaryList = SurveyResponseDelegate
                    .getSectionResponseSummary(surveyDefinition, responseId);
            FormResponseBean formResponseBean = new FormResponseBean();
            formResponseBean.setObjectVersionKey(respMaster.getLastUpdated().getTime());
            if (surveyResponse != null)
            {
                FormResponseStats stats = SurveyResponseManager
                        .getCommentCountAndPercentComplete(surveyDefinition.getQuestions(), surveyResponse, treatFailsAsNotAnswered);
                formResponseBean.setResponseStats(stats);

                //need to check if we are saving the totalQCount etc for all old forms. for now let us calculate it as above
//           FormResponseStats stats = new FormResponseStats();
//           stats.setCommentsCount(respMaster.getNoOfComments());
//           stats.setDimentionalFailCount(respMaster.getDimentionalFailCount());
//           stats.setFailCount(respMaster.getFailCount());
//           stats.setNaCount(respMaster.getNaCount());
//           stats.setPassCount(respMaster.getPassCount());
//           stats.setPercentComplete(respMaster.getPercentComplete());
//           stats.setTotalACount(respMaster.getTotalACount());
//           stats.setTotalQCount(respMaster.getTotalQCount());
//           formResponseBean.setResponseStats(stats);

                formResponseBean.setResponseId(surveyResponse.getResponseId());
                formResponseBean.setFormPk((int) survey.getPk());
                formResponseBean.setTestProcPk(testProc.getPk());
                formResponseBean.setResponseStartTime(surveyResponse.getResponseStartTime());
                formResponseBean.setResponseEndTime(surveyResponse.getResponseCompleteTime());
                formResponseBean.setResponseSyncTime(surveyResponse.getResponseSyncTime());
                formResponseBean.setSubmittedByUserPk(surveyResponse.getUserPk());
                formResponseBean.setSubmittedByUserDisplayName(
                        surveyResponse.getUser().getFirstName() + " " + surveyResponse.getUser().getLastName());
                formResponseBean.setResponseRefNo(surveyResponse.getResponseRefNo());

                List<FormItemResponseBase> itemResponses = new ArrayList<FormItemResponseBase>();
                for (SectionResponseQuery sectionResponseQuery : sectionSummaryList)
                {
                    SectionResponseBean sectionResponseBean = new SectionResponseBean();
                    sectionResponseBean.setPk(sectionResponseQuery.getPk());
                    sectionResponseBean.setFormItemId(sectionResponseQuery.getSectionId());
                    sectionResponseBean.setResponseId(surveyResponse.getResponseId());
                    sectionResponseBean.setWorkorderFk(sectionResponseQuery.getWorkorderFk());

                    sectionResponseBean.setUpdatedDate(sectionResponseQuery.getLastUpdated());
                    if(sectionResponseQuery.getLastUpdated() != null)
                    {
                        sectionResponseBean.setObjectVersionKey(sectionResponseQuery.getLastUpdated().getTime());
                    }
                    if(sectionResponseQuery.getSubmittedBy() != 0)
                    {
                        UserQuery userQ = UserRepository.getInstance().getUser(sectionResponseQuery.getSubmittedBy());
                        sectionResponseBean.setUpdatedBy(userQ.getOID());
                    }

                    List<SurveyItem> sec = new ArrayList<SurveyItem>();
                    sec.add((SurveyItem) surveyDefinition.getQuestion(sectionResponseQuery.getSectionId()));
                    FormResponseStats sstats = SurveyResponseManager.getCommentCountAndPercentComplete(sec,
                            surveyResponse, treatFailsAsNotAnswered);

                    sectionResponseBean.setTotalQCount(sstats.getTotalQCount());
                    sectionResponseBean.setTotalACount(sstats.getTotalACount());
                    sectionResponseBean.setPassCount(sstats.getPassCount());
                    sectionResponseBean.setFailCount(sstats.getFailCount());
                    sectionResponseBean.setDimentionalFailCount(sstats.getDimentionalFailCount());
                    sectionResponseBean.setNaCount(sstats.getNaCount());
                    sectionResponseBean.setPercentComplete(sstats.getPercentComplete());
                    sectionResponseBean.setNoOfComments(sstats.getCommentsCount());

                    try
                    {
                        ObjectLockQuery lock = SurveyDelegate.getCurrentLock(new FormResponseOID(responseId),
                                sectionResponseQuery.getSectionId());
                        if (lock == null)
                        {
                            sectionResponseBean.setLockStatus(SectionLockStatusEnum.Unlocked);
                        } else if (lock.getUserPk() == context.getUser().getPk())
                        {
                            sectionResponseBean.setLockStatus(SectionLockStatusEnum.LockedBySelf);
                            User lockedUser = AccountDelegate.getUser(lock.getUserPk());
                            sectionResponseBean.setLockedByUserPk((int) lockedUser.getPk());
                            sectionResponseBean.setLockedByUserDisplayName(
                                    lockedUser.getFirstName() + " " + lockedUser.getLastName());
                        } else
                        {
                            *//*
     * show locked by other user..
     *//*
                            sectionResponseBean.setLockStatus(SectionLockStatusEnum.LockedByOther);
                            User lockedUser = AccountDelegate.getUser(lock.getUserPk());
                            sectionResponseBean.setLockedByUserPk((int) lockedUser.getPk());
                            sectionResponseBean.setLockedByUserDisplayName(
                                    lockedUser.getFirstName() + " " + lockedUser.getLastName());
                        }
                    }
                    catch (Exception e)
                    {
                        sectionResponseBean.setLockStatus(SectionLockStatusEnum.LockedByOther);
                        sectionResponseBean.setLockedByUserDisplayName("Unknown error.. please try later");
                    }

                    itemResponses.add(sectionResponseBean);
                }

                DeviceResponseExportProcessor beanExporter = new DeviceResponseExportProcessor(context);
                List<SurveyItemBase> surveyQuestionsL = surveyDefinition.getQuestionsLinear();
                for (int i = 0; i < surveyQuestionsL.size(); i++)
                {
                    SurveyItem sItem = (SurveyItem) surveyQuestionsL.get(i);
                    if (!(sItem instanceof SurveySaveItem))
                    {
                        continue;
                    }
                    SurveyItemResponse siResponse = (SurveyItemResponse) surveyResponse
                            .getAnswer((SurveySaveItem) sItem);
                    FormItemResponse formItemResponse = formItemResponseMap.get(sItem.getSurveyItemId());
                    if (siResponse != null)
                    {
                        FormItemResponseBase rBean = beanExporter.getFormItemResponsesBean(sItem, siResponse, formItemResponse);
                        rBean.setResponseId(surveyResponse.getResponseId());
                        itemResponses.add(rBean);
                    }
                }

                formResponseBean.setFormItemResponses(itemResponses);
            }
            return formResponseBean;
        }
        catch (Exception e)
        {

            throw e;
        }

    }*/

    /*
     * Acquire Lock
     */


   @PostMapping("/acquireLock")
   public SectionLockUnlockRequestBean acquireLock(@RequestBody  FormRequestBean formRequestBean)
            throws RestAppException, Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            TestProcObj testProc =testProcService.getTestProc(formRequestBean.getTestProcPk());
            ResponseMasterNew respMaster =surveyResponseService
                    .getLatestResponseMasterForTest((TestProcOID) testProc.getOID());
            Survey survey = surveyMaster.getSurveyByPk(testProc.getFormPk());
            if (testProc == null)
            {
                throw new RestAppException("MSG-FormNotFound:" + formRequestBean.getTestProcPk());
            }
            if (respMaster == null)
            {
                throw new RestAppException("Form is not open, Sections cannot be locked:" + testProc.getPk());
            }
            if (respMaster.getResponseId() != formRequestBean.getResponseId())
            {
                throw new RestAppException(
                        "Invalid test response, the test is upgraded, submitted or not available anymore.");
            }

            /*
             * check the survey status
             */
            String status = survey.getStatus();
            if (Survey.STATUS_DELETED.equals(status))
            {
                throw new RestAppException("MSG-FormDeleted");
            }

            if (!(ResponseMasterNew.STATUS_INPROGRESS.equals(respMaster.getStatus())
                    || ResponseMasterNew.STATUS_NOTSTARTED.equals(respMaster.getStatus())))
            {
                throw new RestAppException("Form is not open, Sections cannot be locked.");
            }
            String[] sections = formRequestBean.getSections();
            SectionResponseBean[] sectionResponseBeans = new SectionResponseBean[sections.length];
            for (int i = 0; i < sections.length; i++)
            {
                String aSection = sections[i];
                SectionResponseBean aSectionBean = new SectionResponseBean();
                // check if that section is locked by another user
                try
                {
                    ObjectLock objectLock = surveyDelegate.lockSectionToEdit(context, (User) context.getUser(),
                            respMaster.getOID(), aSection);
                    aSectionBean.setLockedByUserPk((int) context.getUser().getPk());
                    aSectionBean.setLockedByUserDisplayName(
                            context.getUser().getFirstName() + " " + context.getUser().getLastName());
                    aSectionBean.setLockStatus(SectionLockStatusEnum.LockedBySelf);

                    // get the workorderfk and set it.
                    FormSection formSection =formDBManager.getFormSection(aSection, testProc.getFormPk());
                    TestProcSectionObj testprocSection = testProcService.getTestProcSection(testProc.getOID(),
                            formSection.getOID());
                    Workorder wo =workorderManager.getWorkorderForEntity(testprocSection.getOID());
                    aSectionBean.setWorkorderFk((int) wo.getPk());

                }
                catch (LockedByAnotherUserException e)
                {
                    aSectionBean.setLockStatus(SectionLockStatusEnum.LockedByOther);
                    aSectionBean.setLockedByUserPk((int) e.getObjectLock().getLockedByUser().getPk());
                    aSectionBean.setLockedByUserDisplayName(e.getObjectLock().getLockedByUser().getFirstName() + " "
                            + e.getObjectLock().getLockedByUser().getLastName());
                }
                /*
                 * in the case of of device, we dont know when a form will be
                 * opened by the user.downloading a form cannot be considered as
                 * an access to the form.
                 *
                 * so I record form access when the lock to acquired.
                 */

                workstationService.recordWorkstationFormAccess(testProc.getOID());

                aSectionBean.setFormItemId(aSection);
                sectionResponseBeans[i] = aSectionBean;
            }
            SectionLockUnlockRequestBean sectionLockBean = new SectionLockUnlockRequestBean();
            sectionLockBean.setSectionsToProcess(sectionResponseBeans);
            sectionLockBean.setTestProcPk(formRequestBean.getTestProcPk());
            sectionLockBean.setResponseId(formRequestBean.getResponseId());
            return sectionLockBean;
        }
        catch (Exception e)
        {


            throw e;
        }
    }

    /*
     * Release Lock
     */


    @PostMapping("/releaseLock")
    public SectionLockUnlockRequestBean releaseLock(@RequestBody FormRequestBean formRequestBean)
            throws RestAppException, Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            TestProcObj testProc = testProcService.getTestProc(formRequestBean.getTestProcPk());
            ResponseMasterNew respMaster =surveyResponseService
                    .getLatestResponseMasterForTest((TestProcOID) testProc.getOID());
            Survey survey = surveyMaster.getSurveyByPk(testProc.getFormPk());
            if (testProc == null)
            {
                throw new RestAppException("MSG-FormNotFound:" + formRequestBean.getTestProcPk());
            }
            if (respMaster == null)
            {
                throw new RestAppException("Form is not open, Sections cannot be locked:" + testProc.getPk());
            }
            if (respMaster.getResponseId() != formRequestBean.getResponseId())
            {
                throw new RestAppException(
                        "Invalid test response, the test is upgraded, submitted or not available anymore.");
            }

            /*
             * check the survey status
             */

            String status = survey.getStatus();
            if (Survey.STATUS_DELETED.equals(status))
            {
                throw new RestAppException("MSG-FormDeleted");
            }

            String[] sections = formRequestBean.getSections();

            SectionResponseBean[] returnSections = new SectionResponseBean[sections.length];
            for (int i = 0; i < sections.length; i++)
            {
                String aSection = sections[i];
                SectionResponseBean aSectionBean = new SectionResponseBean();

                /*
                 * check if that section is locked by another user
                 */

                try
                {
                    User user = (User) context.getUser();
                    surveyDelegate.releaseSectionEditLock(context, user, respMaster.getOID(), aSection);

                    aSectionBean.setLockedByUserPk(0);
                    aSectionBean.setLockedByUserDisplayName(null);
                    aSectionBean.setLockStatus(SectionLockStatusEnum.Unlocked);
                }
                catch (LockedByAnotherUserException e)
                {
                    aSectionBean.setLockStatus(SectionLockStatusEnum.LockedByOther);
                    aSectionBean.setLockedByUserPk((int) e.getObjectLock().getLockedByUser().getPk());
                    aSectionBean.setLockedByUserDisplayName(e.getObjectLock().getLockedByUser().getFirstName() + " "
                            + e.getObjectLock().getLockedByUser().getLastName());
                }

                /*
                 * in the case of of device, we dont know when a form will be
                 * opened by the user.SectionLockUnlockRequestBean
                 * sectionLockBean so I record form access when the lock to
                 * acquired.
                 */

                workstationService.recordWorkstationFormAccess(testProc.getOID());
                aSectionBean.setFormItemId(aSection);
                returnSections[i] = aSectionBean;
            }
            SectionLockUnlockRequestBean sectionLockBean = new SectionLockUnlockRequestBean();
            sectionLockBean.setSectionsToProcess(returnSections);
            sectionLockBean.setTestProcPk(formRequestBean.getTestProcPk());
            sectionLockBean.setResponseId(formRequestBean.getResponseId());
            return sectionLockBean;
        }
        catch (Exception e)
        {

            throw e;
        }
    }

    /*
     * Refresh Lock
     */

    @PostMapping("/refreshLock")
    public SectionLockUnlockRequestBean refreshLock(@RequestBody  FormRequestBean formRequestBean)
            throws RestAppException, Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            SectionLockUnlockRequestBean sectionLockBean = null;
            TestProcObj testProc = testProcService.getTestProc(formRequestBean.getTestProcPk());
            ResponseMasterNew respMaster = surveyResponseService
                    .getLatestResponseMasterForTest((TestProcOID) testProc.getOID());
            if (testProc == null)
            {
                throw new RestAppException("MSG-FormNotFound : " + formRequestBean.getTestProcPk());
            }
            if (respMaster == null)
            {
                throw new RestAppException("MSG-FormNotFound : " + formRequestBean.getResponseId());
            }
            if (testProc.getFormPk() != respMaster.getFormPk())
            {
                throw new RestAppException(
                        "Invalid checksheet, the test is upgraded, submitted or not available anymore:"
                                + formRequestBean.getTestProcPk());
            }
            if (respMaster.getResponseId() != formRequestBean.getResponseId())
            {
                throw new RestAppException(
                        "Invalid test response, the test is upgraded, submitted or not available anymore.");
            }
            SurveyDefinition surveyDefinition = surveyDefFactory
                    .getSurveyDefinition(new FormOID(testProc.getFormPk(), null));
            if (surveyDefinition == null)
            {
                throw new RestAppException("MSG-FormNotFound");
            }

            /*
             * if sections passed from client is empty, get the status for all
             * sections in the form
             */

            String[] sections = formRequestBean.getSections();
            if (sections == null || sections.length == 0)
            {
                List<SurveyItem> sectionsFromDef = surveyDefinition.getQuestions();
                sections = new String[sectionsFromDef.size()];
                for (int i = 0; i < sectionsFromDef.size(); i++)
                    sections[i] = sectionsFromDef.get(i).getSurveyItemId();
            }
            if (testProc != null)
            {
                SectionResponseBean[] sectionResponseBeans = new SectionResponseBean[sections.length];
                for (int i = 0; i < sections.length; i++)
                {
                    String aSectionId = sections[i];
                    SectionResponseBean aSectionBean = new SectionResponseBean();

                    /*
                     * get the workorderfk and set it.
                     */

                    FormSection formSection = formDBManager.getFormSection(aSectionId, testProc.getFormPk());
                    TestProcSectionObj testprocSection = testProcService.getTestProcSection(testProc.getOID(),
                            formSection.getOID());
                    Workorder wo = workorderManager.getWorkorderForEntity(testprocSection.getOID());
                    if (wo != null)
                        aSectionBean.setWorkorderFk((int) wo.getPk());

                    try
                    {
                        ObjectLockQuery lock =surveyMaster.getCurrentLock(respMaster.getOID(), aSectionId);
                        if (lock == null)
                        {

                            /*
                             * i should be able to lock the section.. show
                             * provision to lock.
                             */

                            aSectionBean.setLockedByUserPk(0);
                            aSectionBean.setLockedByUserDisplayName(null);
                            aSectionBean.setLockStatus(SectionLockStatusEnum.Unlocked);
                        } else if (lock.getUserPk() == context.getUser().getPk())
                        {
                            aSectionBean.setLockStatus(SectionLockStatusEnum.LockedBySelf);
                            aSectionBean.setLockedByUserPk((int) context.getUser().getPk());
                            aSectionBean.setLockedByUserDisplayName(
                                    context.getUser().getFirstName() + " " + context.getUser().getLastName());
                        } else
                        {

                            /*
                             * show locked by other user..
                             */

                            aSectionBean.setLockStatus(SectionLockStatusEnum.LockedByOther);
                            aSectionBean.setLockedByUserPk(lock.getUserPk());
                            User lockedUser = accountService.getUser(lock.getUserPk());
                            aSectionBean.setLockedByUserDisplayName(
                                    lockedUser.getFirstName() + " " + lockedUser.getLastName());
                        }
                    }
                    catch (Exception e)
                    {
                        aSectionBean.setLockStatus(SectionLockStatusEnum.LockedByOther);
                        aSectionBean.setLockedByUserPk(0);
                        aSectionBean.setLockedByUserDisplayName("Unknown Error.. Please try later..");
                    }
                    aSectionBean.setFormItemId(aSectionId);
                    sectionResponseBeans[i] = aSectionBean;

                }
                sectionLockBean = new SectionLockUnlockRequestBean();
                sectionLockBean.setSectionsToProcess(sectionResponseBeans);
                sectionLockBean.setTestProcPk(testProc.getPk());
                sectionLockBean.setResponseId(respMaster.getResponseId());
                sectionLockBean.setSectionsToProcess(sectionResponseBeans);
            }
            return sectionLockBean;
        }
        catch (Exception e)
        {

            throw e;
        }
    }

//commenting methods using vaadin component
    /*
     * Save Response
     */

   /* public void saveResponse( FormResponseBean formResponseBean)
            throws Exception
    {


        if((formResponseBean == null || formResponseBean.getFormItemResponses() == null
                || formResponseBean.getFormItemResponses().size() == 0)
                && (formResponseBean.getAttachments() == null || formResponseBean.getAttachments().size() == 0 ))
        {
            logger.info("Save called, but nothing to save; Returning");
            return Response.ok().build();
        }

         UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TestProcOID testProcOID = null;
        try
        {
            testProcOID = new TestProcOID(formResponseBean.getTestProcPk());
            TestProcObj testProc = (TestProcObj) TestProcManager.getTestProc(formResponseBean.getTestProcPk());
            ResponseMasterNew respMaster = SurveyResponseDelegate.getLatestResponseMasterForTest(testProcOID);
            if (testProc.getFormPk() != respMaster.getFormPk())
            {
                throw new RestAppException("Invalid Test form detected. Please contact your administrator");
            }
            if (formResponseBean.getResponseId() != respMaster.getResponseId())
            {
                throw new RestAppException("Invalid response detected, Please contact your administrator");
            }

            //if the user has a Response saved which is pending review. all save should go into that pending review response
//        EntityVersionReviewProxy responeRevision = SurveyResponseDelegate.getEntityRevisionReviewProxy(context, respMaster.getResponseId());
//        if(responeRevision != null)
//        {
//           SurveyResponseDelegate.saveSyncErrorResponse(context, respMaster.getResponseId(), formResponseBean);
//           return Response.ok().build();
//        }
//
//        // if the form was saved after the user fetched in to the tablet. we need to keep that in a review area.
//        if(respMaster.getLastUpdated().getTime() != formResponseBean.getVersionKey())
//        {
//           // This means that the response was updated by someone else. so we need to show a sync error
//           // to the user. save this response to a qurantine area and then let the verifier merge the changes.
//           SurveyResponseDelegate.saveSyncErrorResponse(context, respMaster.getResponseId(), formResponseBean);
//           return Response.ok().build();
//        }
//        else
//        {
            logger.info(String.format("Getting sync key for response save for testprocPk:%d by user:%s",
                    testProcOID.getPk(), context.getUser().getUserName()));
            ResponseUploadSynchronizer.getInstance().startProcess(testProcOID);
            logger.info(String.format("Success Getting sync key for response save for testprocPk:%d by user:%s",
                    testProcOID.getPk(), context.getUser().getUserName()));

            FormResponseSaveProcessor saveProcessor = new FormResponseSaveProcessor(context, formResponseBean);
            saveProcessor.process();

            logger.info(String.format("Success saving response for testprocPk:%d by user:%s", testProcOID.getPk(),
                    context.getUser().getUserName()));

//        }
        }

        catch (Exception e)
        {

            throw e;
        }
        finally
        {
            ResponseUploadSynchronizer.getInstance().processComplete(testProcOID);
            if (testProcOID != null)
                logger.info(String.format("Release sync key for response save for testprocPk:%d by user:%s",
                        testProcOID.getPk(), context.getUser().getUserName()));
        }

    }

*/

//commenting methods using vaadin component
    /*
     * Submit Response
     */

   /* public void submitInterimResponse( FormResponseBean formResponseBean)
            throws RestAppException, Exception
    {

         UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TestProcOID testProcOID = null;
        try
        {
            testProcOID = new TestProcOID(formResponseBean.getTestProcPk());

            logger.info(String.format("Getting sync key for response save for testprocPk:%d by user:%s",
                    testProcOID.getPk(), context.getUser().getUserName()));
            ResponseUploadSynchronizer.getInstance().startProcess(testProcOID);
            logger.info(String.format("Success Getting sync key for response save for testprocPk:%d by user:%s",
                    testProcOID.getPk(), context.getUser().getUserName()));

            FormResponseSaveProcessor saveProcessor = new FormResponseSaveProcessor(context, formResponseBean);
            saveProcessor.process();

            //after save is completed, we call the interim submit.
            SurveyResponse sResponse = SurveyResponseDelegate.getSurveyResponse(formResponseBean.getResponseId());
            SurveyResponseDelegate.submitInterimResponse(context, sResponse);

            logger.info(String.format("Success submitting interim response for testprocPk:%d by user:%s", testProcOID.getPk(),
                    context.getUser().getUserName()));

        }

        catch (Exception e)
        {

            throw e;
        }
        finally
        {
            ResponseUploadSynchronizer.getInstance().processComplete(testProcOID);
            if (testProcOID != null)
                logger.info(String.format("Release sync key for response save for testprocPk:%d by user:%s",
                        testProcOID.getPk(), context.getUser().getUserName()));
        }
    }
*/

//commenting methods using vaadin component
    /*
     * Submit Response
     */

    /*  public Response submitResponse(  FormResponseBean formResponseBean)
              throws RestAppException, Exception
      {

           UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          FormSubmissionStatusBean submitResult = new FormSubmissionStatusBean();
          TestProcObj testProc = null;
          *//*
     * faking a SurveyDefinition so that the responseImportProcessor can
     * work.
     *//*
        try
        {
            TransientSurveyDefinition sd = new TransientSurveyDefinition();
            OpenItemAnswerType aItem = new OpenItemAnswerType();
            aItem.setSurveyItemId("3"); // this 3 is very important.. as all is
            // based on this id need to be defined
            // somewhere
            aItem.setOrderNum(1);
            sd.addItem(aItem);

            SurveyResponse sResponse = new SurveyResponse(sd);
            sResponse.setResponseMode(SurveyForm.RESPONSEMODE_DEVICE_UPLOAD);
            DeviceResponseImportProcessor importProcessor = new DeviceResponseImportProcessor();
            importProcessor.init(sd, context);
            logger.debug(sResponse);

            *//*
     * find survey
     *//*
            int testProcPk = formResponseBean.getTestProcPk();
            testProc = TestProcManager.getTestProc(testProcPk);
            int responseId = formResponseBean.getResponseId();
            int surveyPk = formResponseBean.getFormPk();

            if (testProc.getFormPk() != surveyPk)
            {
                throw new RestAppException("Invalid checksheet, please contact administrator:" + testProcPk);
            }

            Survey survey = SurveyMaster.getSurveyByPk(surveyPk);

            *//*
     * check the survey status
     *//*
            String status = survey.getStatus();
            if (Survey.STATUS_DELETED.equals(status))
            {
                throw new RestAppException("MSG-FormDeleted");
            }

            SurveyDefinition surveyDefinition = SurveyDefinitionCacheSingleton.getInstance()
                    .getSurveyDefinition(new FormOID(surveyPk));

            SurveyResponse surveyResponse = null;
            if (responseId == 0)
            {

                throw new RestAppException("Invalid submission, Response Identifier is not valid");
            }

            logger.info(String.format("Getting sync key for submit response for testprocPk:%d by user:%s",
                    testProc.getPk(), context.getUser().getUserName()));
            ResponseUploadSynchronizer.getInstance().startProcess(testProc.getOID());
            logger.info(String.format("Success Getting sync key for submit response for testprocPk:%d by user:%s",
                    testProc.getPk(), context.getUser().getUserName()));

            surveyResponse = SurveyResponseDelegate.getSurveyResponse(surveyDefinition, responseId);
            surveyResponse.setResponseMode(SurveyForm.RESPONSEMODE_DEVICE_UPLOAD);
            surveyResponse.setUser((User) context.getUser());

            List<SurveySaveItem> itemsSubmittedToSave = new ArrayList<SurveySaveItem>();
            importProcessor.init(surveyDefinition, context);
            importProcessor.getSurveyItemResponses(surveyResponse, formResponseBean.getFormItemResponses(),
                    itemsSubmittedToSave); // the third param will contain the
            // items which were submitted from
            // the device.

            *//*
     * validate the questions under the sections locked by this user
     *//*
            List<SurveyItemBase> surveyQuestionsToValidate = new ArrayList<SurveyItemBase>(); // will
            // contain
            // the
            // sections
            // the
            // user
            // has
            // locked
            List<String> lockedSectionIds = SurveyDelegate.getLockedSectionIds((User) context.getUser(),
                    new FormResponseOID(responseId));
            for (Object element : surveyDefinition.getQuestions())
            {
                Section aSection = (Section) element;
                if (lockedSectionIds.contains(aSection.getSurveyItemId()))
                {
                    surveyQuestionsToValidate.addAll(surveyDefinition.getQuestionsLinear(aSection.getSurveyItemId()));
                }
            }
            LinkedHashMap<String, List<String>> errors = SurveyResponseDelegate.validateResponse(surveyResponse,
                    surveyQuestionsToValidate);

            if (errors.size() > 0)
            {
                submitResult.setErrorMessage("Errors found while validating the form");
                submitResult.setErrors(errors);
                submitResult.setValidationStatus(ValidationStatusEnum.ValidationFailedInLockedSections);
            } else
            {
                SurveyResponseDelegate.finalizeSurveyResponse(context, surveyDefinition, surveyResponse);

                submitResult.setErrorMessage("success");
                submitResult.setValidationStatus(ValidationStatusEnum.Success);
            }
        }
        catch(AppException ax)
        {
            ax.printStackTrace();
            submitResult.setValidationStatus(ValidationStatusEnum.Success);
            submitResult.setErrorMessage(ax.getMessage());
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            submitResult.setValidationStatus(ValidationStatusEnum.Failed);
            submitResult.setErrorMessage(e.getMessage());
        }
        catch (Exception e)
        {
            submitResult.setValidationStatus(ValidationStatusEnum.Failed);
            submitResult.setErrorMessage(e.getMessage());
            APIErrorLogger.logGeneralError(logger, context.getUser().getUserName(), "/forms/submit",
                    "FormResponseBean-" + ((formResponseBean != null) ? formResponseBean.toString() : "null"), e);
            throw e;
        }
        finally
        {
            ResponseUploadSynchronizer.getInstance().processComplete(testProc.getOID());
            logger.info(String.format("Release sync key for response save for testprocPk:%d by user:%s",
                    testProc.getPk(), context.getUser().getUserName()));
        }
        return Response.ok(submitResult).build();
    }

    *//*
     * Form Revision Lookup
     *//*

    public Response formRevisionLookup(
            @PathParam("testProcPk") int testProcPk, @PathParam("formPk") int formPk) throws Exception
    {
         UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            FormRevisionLookupResponseBean formRevisionLookupResponseBean = null;
            TestProcObj testProcForResponse = (TestProcObj) TestProcManager.getTestProc(testProcPk);
            FormQuery formOnDevice = SurveyDelegate.getFormByPk(formPk);
            boolean currentMasterFormStillAssigned = false;
            FormQuery currentAssignedForm = null;
            *//*
     * Check if this testProc still has the same form.
     *//*
            if (testProcForResponse != null)
            {
                currentMasterFormStillAssigned = true;
                currentAssignedForm = SurveyMaster.getFormByPk(testProcForResponse.getFormPk());
            }

            FormQuery latestVerForm = SurveyMaster.getLatestPublishedVersionOfForm(formPk);
            if (currentMasterFormStillAssigned == false)
            {
                *//*
     * the entire form is removed from the workstation for that unit
     *//*
                formRevisionLookupResponseBean = new FormRevisionLookupResponseBean();
                formRevisionLookupResponseBean.setValid(false);
                formRevisionLookupResponseBean.setMessage("Form is not valid any more");
            } else
            {
                *//*
     * the formInHand is still assigned but a new version is
     * available
     *//*
                if (formOnDevice.getVersionNo() == currentAssignedForm.getVersionNo()
                        && currentAssignedForm.getVersionNo() < latestVerForm.getVersionNo())
                {
                    formRevisionLookupResponseBean = new FormRevisionLookupResponseBean();
                    formRevisionLookupResponseBean.setValid(true);
                    formRevisionLookupResponseBean.setLatest(false);
                    formRevisionLookupResponseBean.setUpgradePerformed(false);
                    formRevisionLookupResponseBean.setFormPk(latestVerForm.getPk());
                    formRevisionLookupResponseBean.setVersionNo(latestVerForm.getVersionNo());
                    formRevisionLookupResponseBean
                            .setRevision((latestVerForm.getRevision() == null) ? "" : latestVerForm.getRevision());
                    formRevisionLookupResponseBean.setRevisionDate(latestVerForm.getCreatedDate());
                    formRevisionLookupResponseBean.setRevisionComment(
                            (latestVerForm.getVersionComment() == null) ? "" : latestVerForm.getVersionComment());
                } else if (formOnDevice.getVersionNo() == currentAssignedForm.getVersionNo()
                        && currentAssignedForm.getVersionNo() == latestVerForm.getVersionNo())
                {
                    *//*
     * form in hand is the latest and that is the assigned one
     *//*
                    formRevisionLookupResponseBean = new FormRevisionLookupResponseBean();
                    formRevisionLookupResponseBean.setValid(true);
                    formRevisionLookupResponseBean.setLatest(true);
                    formRevisionLookupResponseBean.setUpgradePerformed(true);
                } else if (formOnDevice.getVersionNo() < currentAssignedForm.getVersionNo()
                        && currentAssignedForm.getVersionNo() == latestVerForm.getVersionNo())
                {
                    *//*
     * form in hand is an older version and current assigned
     * form is same as the latest form means unit was already
     * upgraded to the latest form revision by the
     * manager/another tester
     *//*
                    ResponseMasterNew resM = SurveyResponseDelegate
                            .getLatestResponseMasterForTest(new TestProcOID(testProcForResponse.getPk(), null));

                    formRevisionLookupResponseBean = new FormRevisionLookupResponseBean();
                    formRevisionLookupResponseBean.setValid(true);
                    formRevisionLookupResponseBean.setLatest(false);
                    formRevisionLookupResponseBean.setUpgradePerformed(true);
                    formRevisionLookupResponseBean.setFormPk(latestVerForm.getPk());
                    formRevisionLookupResponseBean.setVersionNo(latestVerForm.getVersionNo());
                    formRevisionLookupResponseBean
                            .setRevision((latestVerForm.getRevision() == null) ? "" : latestVerForm.getRevision());
                    formRevisionLookupResponseBean.setRevisionDate(latestVerForm.getCreatedDate());
                    formRevisionLookupResponseBean.setRevisionComment(
                            (latestVerForm.getVersionComment() == null) ? "" : latestVerForm.getVersionComment());
                    formRevisionLookupResponseBean.setResponseId(resM.getResponseId());
                } else if (formOnDevice.getVersionNo() > currentAssignedForm.getVersionNo()
                        && formOnDevice.getVersionNo() == latestVerForm.getVersionNo())
                {
                    *//*
     * form in hand is latest version than the assigned one. //
     * upgrade performed was not successful but client upgraded
     * the form
     *//*
                    formRevisionLookupResponseBean = new FormRevisionLookupResponseBean();
                    formRevisionLookupResponseBean.setValid(true);
                    formRevisionLookupResponseBean.setLatest(true);
                    formRevisionLookupResponseBean.setUpgradePerformed(false);
                    formRevisionLookupResponseBean.setFormPk(currentAssignedForm.getPk());
                    formRevisionLookupResponseBean.setVersionNo(currentAssignedForm.getVersionNo());
                    formRevisionLookupResponseBean.setRevision(
                            (currentAssignedForm.getRevision() == null) ? "" : currentAssignedForm.getRevision());
                    formRevisionLookupResponseBean.setRevisionDate(currentAssignedForm.getCreatedDate());
                    formRevisionLookupResponseBean
                            .setRevisionComment((currentAssignedForm.getVersionComment() == null) ? ""
                                    : currentAssignedForm.getVersionComment());
                    formRevisionLookupResponseBean.setMessage(
                            "form in hand is a higher version than the current assigned form, Form upgrade could have failed");
                } else
                {
                    *//*
     * unexpected request.
     *//*
                    formRevisionLookupResponseBean = new FormRevisionLookupResponseBean();
                    formRevisionLookupResponseBean.setValid(true);
                    formRevisionLookupResponseBean.setLatest(false);
                    formRevisionLookupResponseBean.setUpgradePerformed(false);
                    formRevisionLookupResponseBean.setFormPk(latestVerForm.getPk());
                    formRevisionLookupResponseBean.setVersionNo(latestVerForm.getVersionNo());
                    formRevisionLookupResponseBean
                            .setRevision((latestVerForm.getRevision() == null) ? "" : latestVerForm.getRevision());
                    formRevisionLookupResponseBean.setRevisionDate(latestVerForm.getCreatedDate());
                    formRevisionLookupResponseBean.setRevisionComment(
                            (latestVerForm.getVersionComment() == null) ? "" : latestVerForm.getVersionComment());
                    formRevisionLookupResponseBean.setMessage("Unexpected request, refetch the form list");
                }
            }
            return Response.ok(formRevisionLookupResponseBean).build();
        }
        catch (Exception e)
        {
            APIErrorLogger.logGeneralError(logger, context.getUser().getUserName(),
                    "/formRevision/{testProcPk}/{formPk}", "testProcPk-" + testProcPk + " ; formPk-" + formPk, e);
            throw e;
        }
    }

*/
    @GetMapping("/getTestListOnItem")
    public Element getTestListOnItem() throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> tests = new ArrayList<String>();
        try
        {
            String status = null;
            String message = null;
            try
            {
                tests.add("OpenItemForm");
                status = "success";
                message = "";
            }
            catch (AppException e)
            {
                e.printStackTrace();

                status = "fail";
                message = e.getMessage();
            }
            Document doc = createReturnXML(status, message, tests);
            return doc.getRootElement();
        }
        catch (Exception e)
        {

            throw e;
        }
    }

    /*
     * Form List With Filters
     */

   @GetMapping("/getFormListWithFilters")
   public List<TestProcBean> getFormListWithFilters(@RequestBody
            TestProcListRequestBean requestBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            HashMap<String, Boolean> permMap = new HashMap<String, Boolean>(); //the key will be "projectPk-workstationPk-tester|verifier|approver"

            TestProcFilter filter = new TestProcFilter(requestBean.getProjectOID());
            filter.setUnitOID(requestBean.getUnitOID());
            filter.setWorkstationOID(requestBean.getWorkstationOID());
            filter.setIncludeChildren(false);
            filter.setFetchWorkstationForecastAsTestForecast(false);
            filter.setTestStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS });
            List<UnitFormQuery> unitFormQueries = new TestProcListReport(context, filter,persistWrapper,unitManager,dummyWorkstation).getTestProcs();
            List<TestProcBean> testProcBeans = new ArrayList<TestProcBean>();
            for (UnitFormQuery unitForm : unitFormQueries)
            {
                TestProcBean tBean = new TestProcBean();
                tBean.setPk(unitForm.getPk());
                tBean.setTestProcName(unitForm.getName());
                tBean.setProjectPk(unitForm.getProjectPk());
                tBean.setProjectName(unitForm.getProjectName());
                tBean.setProjectDescription(
                        (unitForm.getProjectDescription() == null) ? "" : unitForm.getProjectDescription());
                tBean.setItemPk(unitForm.getUnitPk());
                tBean.setItemName(unitForm.getUnitName());
                tBean.setItemDescription((unitForm.getUnitDescription() == null) ? "" : unitForm.getUnitDescription());
                tBean.setWorkstationPk(unitForm.getWorkstationPk());
                tBean.setWorkstationName(unitForm.getWorkstationName());
                tBean.setWorkstationDescription(
                        (unitForm.getWorkstationDescription() == null) ? "" : unitForm.getWorkstationDescription());
                // tBean.setWorkstationSiteName(aForm.get);
                tBean.setFormPk(unitForm.getFormPk());
                tBean.setFormName(unitForm.getFormName());
                tBean.setFormDescription((unitForm.getFormDescription() == null) ? "" : unitForm.getFormDescription());
                tBean.setVersionNo(unitForm.getFormVersion());
                tBean.setFormRevision((unitForm.getFormRevision() != null) ? unitForm.getFormRevision() : "");
                // tBean.setAssignedDate(aForm.getApprovedDate());
                tBean.setResponseId(unitForm.getResponseId());
                tBean.setStatus((unitForm.getResponseStatus() == null) ? ResponseMasterNew.STATUS_NOTSTARTED
                        : unitForm.getResponseStatus());
                tBean.setTotalQCount(unitForm.getTotalQCount());
                tBean.setTotalACount(unitForm.getTotalACount());
                tBean.setPercentComplete(unitForm.getPercentComplete());
                tBean.setNoOfComments(unitForm.getNoOfComments());
                tBean.setPassCount(unitForm.getPassCount());
                tBean.setFailCount(unitForm.getFailCount());
                tBean.setDimensionalFailCount(unitForm.getDimentionalFailCount());
                tBean.setNaCount(unitForm.getNaCount());
                tBean.setUnitPk(unitForm.getUnitPk());
                tBean.setUnitName(unitForm.getUnitName());
                tBean.setUnitDescription((unitForm.getUnitDescription() == null) ? "" : unitForm.getUnitDescription());

                List<String> permList = new ArrayList<String>();
                Boolean permT = permMap.get(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_TESTER);
                if(permT == null)
                {
                    List tusers = unitService.getUsersForUnitInRole(tBean.getUnitPk(), new ProjectOID(tBean.getProjectPk()), new WorkstationOID(tBean.getWorkstationPk()),User.ROLE_TESTER);
                    if(tusers != null && tusers.contains(context.getUser()))
                    {
                        permMap.put(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_TESTER, true);
                    }
                    else
                    {
                        permMap.put(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_TESTER, false);
                    }
                }
                if(permMap.get(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_TESTER))
                    permList.add(User.ROLE_TESTER);


                Boolean permV = permMap.get(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_VERIFY);
                if(permV == null)
                {
                    List tusers = unitService.getUsersForUnitInRole(tBean.getUnitPk(), new ProjectOID(tBean.getProjectPk()), new WorkstationOID(tBean.getWorkstationPk()),User.ROLE_VERIFY);
                    if(tusers != null && tusers.contains(context.getUser()))
                    {
                        permMap.put(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_VERIFY, true);
                    }
                    else
                    {
                        permMap.put(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_VERIFY, false);
                    }
                }
                if(permMap.get(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_VERIFY))
                    permList.add(User.ROLE_VERIFY);


                Boolean permA = permMap.get(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_APPROVE);
                if(permA == null)
                {
                    List tusers = unitService.getUsersForUnitInRole(tBean.getUnitPk(), new ProjectOID(tBean.getProjectPk()), new WorkstationOID(tBean.getWorkstationPk()),User.ROLE_APPROVE);
                    if(tusers != null && tusers.contains(context.getUser()))
                    {
                        permMap.put(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_APPROVE, true);
                    }
                    else
                    {
                        permMap.put(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_APPROVE, false);
                    }
                }
                if(permMap.get(tBean.getUnitPk()+"-"+tBean.getProjectPk()+"-"+tBean.getWorkstationPk()+"-"+User.ROLE_APPROVE))
                    permList.add(User.ROLE_APPROVE);

                if(permList.size() > 0)
                {
                    tBean.setPermissions(permList.toArray(new String[permList.size()]));
                }

                // tBean.setComment(aForm.get);
                testProcBeans.add(tBean);
            }
            return testProcBeans;

        }
        catch (Exception e)
        {

            throw e;
        }

    }

    /*
     * Form Summary
     */

    @GetMapping("/getFormSummary")
    public FormSummaryResponseBean getFormSummary(@RequestBody
            FormSummaryRequestBean formSummaryRequestBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {

            Workstation workstation = new Workstation();
            Project project = new Project();
            UnitEntityQuery unit = new UnitEntityQuery();
            Site site = new Site();
            if (formSummaryRequestBean.getWorkstationOID() != null)
                workstation = workstationService.getWorkstation(new WorkstationOID((int) formSummaryRequestBean.getWorkstationOID().getPk()));
            if (formSummaryRequestBean.getProjectOID() != null)
                project = projectService.getProject(formSummaryRequestBean.getProjectOID().getPk());
            if (formSummaryRequestBean.getUnitOID() != null)
                unit = unitManager.getUnitEntityQueryByPk(new UnitOID((int) formSummaryRequestBean.getUnitOID().getPk()));
            if (formSummaryRequestBean.getSiteOID() != null)
            {
                site = siteService.getSite((int) formSummaryRequestBean.getSiteOID().getPk());
            } else
            {
                site = context.getSite();
            }
            DateUtils dateUtils = new DateUtils(TimeZone.getTimeZone(site.getTimeZone()));
            FormSummaryResponseBean formSummaryResponseBean = new FormSummaryResponseBean();
            formSummaryResponseBean.setWorkstationPk((int) workstation.getPk());
            formSummaryResponseBean.setWorkstationName(workstation.getWorkstationName());
            formSummaryResponseBean.setWorkstationDescription(workstation.getDescription());
            formSummaryResponseBean.setProjectPk((int) project.getPk());
            formSummaryResponseBean.setProjectName(project.getProjectName());
            formSummaryResponseBean.setProjectDescription(project.getProjectDescription());
            formSummaryResponseBean.setUnitPk(unit.getPk());
            formSummaryResponseBean.setUnitName(unit.getUnitName());
            Date beginningOfToday = dateUtils.getBeginningDayNew(new Date());
            TestProcStatusSummaryReportRequest summaryReportRequest = new TestProcStatusSummaryReportRequest();
            summaryReportRequest.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequest.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequest.setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.unit));
            TestProcStatusSummaryReport testProcSummaryReport = new TestProcStatusSummaryReport(persistWrapper, dummyWorkstation, unitManager,context,
                    summaryReportRequest);
            TestProcStatusSummaryReportResult testProcSummaryReportResult = testProcSummaryReport.runReport();
            if (testProcSummaryReportResult != null && testProcSummaryReportResult.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResult.getReportResult())
                {
                    formSummaryResponseBean.setNotStartedCount(row.getNotStartedCount());
                    formSummaryResponseBean.setInProgressCount(row.getInProgressCount());
                    formSummaryResponseBean.setPausedCount(row.getPausedCount());
                    formSummaryResponseBean.setCompletedCount(row.getCompletedCount());
                    formSummaryResponseBean.setVerifiedCount(row.getVerifiedCount());
                    formSummaryResponseBean.setApprovedCount(row.getApprovedCount());
                    formSummaryResponseBean.setTotalFormCount(row.getTotalFormCount());
                    formSummaryResponseBean.setNoOfComments(row.getNoOfComments());
                    formSummaryResponseBean.setFailCount(row.getFailCount());

                }

            }
            /*
             * Form overdue count
             */
            TestProcStatusSummaryReportRequest summaryReportRequestOD = new TestProcStatusSummaryReportRequest();
            summaryReportRequestOD.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestOD.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestOD.setForecastEndDate(
                    new DateRangeFilter(null, beginningOfToday, TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestOD.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.NOTSTARTED,
                    FormStatusEnum.INPROGRESS, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestOD
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportOD = new TestProcStatusSummaryReport(persistWrapper, dummyWorkstation,unitManager,context,
                    summaryReportRequestOD);
            TestProcStatusSummaryReportResult testProcSummaryReportResultOD = testProcSummaryReportOD.runReport();
            if (testProcSummaryReportResultOD != null && testProcSummaryReportResultOD.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultOD.getReportResult())
                {
                    long overdueCount = (row.getNotStartedCount() + row.getInProgressCount() + row.getPausedCount()
                            + row.getVerifiedCount());
                    formSummaryResponseBean.setOverDueCount(overdueCount);

                }

            }

            return formSummaryResponseBean;
        }
        catch (Exception e)
        {

            throw e;
        }

    }

    /*
     * Form Schedule
     */

    @GetMapping("/getFormSchedule")
    public FormScheduleResponseBean getFormSchedule(@RequestBody
            FormSummaryRequestBean formSummaryRequestBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            Workstation workstation = new Workstation();
            Project project = new Project();
            UnitEntityQuery unit = new UnitEntityQuery();
            Site site = new Site();
            if (formSummaryRequestBean.getWorkstationOID() != null)
                workstation = workstationService.getWorkstation(new WorkstationOID((int) formSummaryRequestBean.getWorkstationOID().getPk()));
            if (formSummaryRequestBean.getProjectOID() != null)
                project = projectService.getProject(formSummaryRequestBean.getProjectOID().getPk());
            if (formSummaryRequestBean.getUnitOID() != null)
                unit =unitManager.getUnitEntityQueryByPk(new UnitOID((int) formSummaryRequestBean.getUnitOID().getPk()));
            if (formSummaryRequestBean.getSiteOID() != null)
            {
                site = siteService.getSite((int) formSummaryRequestBean.getSiteOID().getPk());
            } else
            {
                site = context.getSite();
            }
            DateUtils dateUtils = new DateUtils(TimeZone.getTimeZone(site.getTimeZone()));
            FormScheduleResponseBean formScheduleResponseBean = new FormScheduleResponseBean();
            formScheduleResponseBean.setWorkstationPk((int) workstation.getPk());
            formScheduleResponseBean.setWorkstationName(workstation.getWorkstationName());
            formScheduleResponseBean.setWorkstationDescription(workstation.getDescription());
            formScheduleResponseBean.setProjectPk((int) project.getPk());
            formScheduleResponseBean.setProjectName(project.getProjectName());
            formScheduleResponseBean.setProjectDescription(project.getProjectDescription());
            formScheduleResponseBean.setUnitPk(unit.getPk());
            formScheduleResponseBean.setUnitName(unit.getUnitName());

            Date beginningOfToday = dateUtils.getBeginningDayNew(new Date());
            /*
             * Form count for overdued
             */
            TestProcStatusSummaryReportRequest summaryReportRequestOD = new TestProcStatusSummaryReportRequest();
            summaryReportRequestOD.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestOD.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestOD.setForecastEndDate(
                    new DateRangeFilter(null, beginningOfToday, TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestOD.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.NOTSTARTED,
                    FormStatusEnum.INPROGRESS, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestOD
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportOD = new TestProcStatusSummaryReport(persistWrapper,dummyWorkstation,unitManager, context,
                    summaryReportRequestOD);
            TestProcStatusSummaryReportResult testProcSummaryReportResultOD = testProcSummaryReportOD.runReport();
            if (testProcSummaryReportResultOD != null && testProcSummaryReportResultOD.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultOD.getReportResult())
                {
                    long overdueCount = (row.getNotStartedCount() + row.getInProgressCount() + row.getPausedCount()
                            + row.getVerifiedCount());
                    formScheduleResponseBean.setFormOverdued(overdueCount);

                }

            }

            /*
             * Form count for in progress
             */
            TestProcStatusSummaryReportRequest summaryReportRequestInProgress = new TestProcStatusSummaryReportRequest();
            summaryReportRequestInProgress.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestInProgress.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestInProgress.setForecastEndDate(
                    new DateRangeFilter(null, beginningOfToday, TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestInProgress.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS });
            summaryReportRequestInProgress
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.unit));
            TestProcStatusSummaryReport testProcSummaryReportInProgress = new TestProcStatusSummaryReport(persistWrapper,dummyWorkstation, unitManager, context,
                    summaryReportRequestInProgress);
            TestProcStatusSummaryReportResult testProcSummaryReportResultInProgress = testProcSummaryReportInProgress
                    .runReport();
            if (testProcSummaryReportResultInProgress != null
                    && testProcSummaryReportResultInProgress.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultInProgress.getReportResult())
                {
                    formScheduleResponseBean.setFormOverdued(row.getInProgressCount());

                }

            }
            /*
             * Form count for to be start today
             */
            TestProcStatusSummaryReportRequest summaryReportRequestToBeStartToday = new TestProcStatusSummaryReportRequest();
            summaryReportRequestToBeStartToday.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestToBeStartToday.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestToBeStartToday.setForecastStartDate(
                    new DateRangeFilter(beginningOfToday, beginningOfToday, TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestToBeStartToday
                    .setResponseStatus(new FormStatusEnum[] { FormStatusEnum.NOTSTARTED, FormStatusEnum.INPROGRESS,
                            FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestToBeStartToday.setGroupingSet(
                    Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate));
            TestProcStatusSummaryReport testProcSummaryReportToBeStartToday = new TestProcStatusSummaryReport(persistWrapper,dummyWorkstation,unitManager,context,
                    summaryReportRequestToBeStartToday);
            TestProcStatusSummaryReportResult testProcSummaryReportResultToBeStartToday = testProcSummaryReportToBeStartToday
                    .runReport();
            if (testProcSummaryReportResultToBeStartToday != null
                    && testProcSummaryReportResultToBeStartToday.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultToBeStartToday
                        .getReportResult())
                {
                    // long count = row.getNotStartedCount() +
                    // row.getInProgressCount() +row.getPausedCount()
                    // +row.getVerifiedCount()+ row.getCompletedCount();
                    formScheduleResponseBean.setFormToBeStartToday(row.getTotalFormCount());

                }

            }
            /*
             * Form count for to be start today
             */
            TestProcStatusSummaryReportRequest summaryReportRequestStartedToday = new TestProcStatusSummaryReportRequest();
            summaryReportRequestStartedToday.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestStartedToday.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestStartedToday.setForecastStartDate(
                    new DateRangeFilter(beginningOfToday, beginningOfToday, TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestStartedToday.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS,
                    FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestStartedToday.setGroupingSet(
                    Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate));
            TestProcStatusSummaryReport testProcSummaryReportStartedToday = new TestProcStatusSummaryReport(persistWrapper,dummyWorkstation,unitManager, context,
                    summaryReportRequestStartedToday);
            TestProcStatusSummaryReportResult testProcSummaryReportResultStartedToday = testProcSummaryReportStartedToday
                    .runReport();
            if (testProcSummaryReportResultStartedToday != null
                    && testProcSummaryReportResultStartedToday.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultStartedToday
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormStartedToday(row.getTotalFormCount());

                }
            }
            /*
             * Form count to be complete today
             */
            TestProcStatusSummaryReportRequest summaryReportRequestToBeCompleteToday = new TestProcStatusSummaryReportRequest();
            summaryReportRequestToBeCompleteToday
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestToBeCompleteToday.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestToBeCompleteToday.setForecastEndDate(
                    new DateRangeFilter(beginningOfToday, beginningOfToday, TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestToBeCompleteToday.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS,
                    FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestToBeCompleteToday
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportToBeCompleteToday = new TestProcStatusSummaryReport(
                    persistWrapper,dummyWorkstation,unitManager, context, summaryReportRequestToBeCompleteToday);
            TestProcStatusSummaryReportResult testProcSummaryReportResultToBeCompleteToday = testProcSummaryReportToBeCompleteToday
                    .runReport();
            if (testProcSummaryReportResultToBeCompleteToday != null
                    && testProcSummaryReportResultToBeCompleteToday.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultToBeCompleteToday
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormToBeCompleteToday(row.getTotalFormCount());

                }
            }
            /*
             * Form count completed today
             */
            TestProcStatusSummaryReportRequest summaryReportRequestCompletedToday = new TestProcStatusSummaryReportRequest();
            summaryReportRequestCompletedToday.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestCompletedToday.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestCompletedToday.setForecastEndDate(
                    new DateRangeFilter(beginningOfToday, beginningOfToday, TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestCompletedToday.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.COMPLETE });
            summaryReportRequestCompletedToday
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportCompletedToday = new TestProcStatusSummaryReport(persistWrapper, dummyWorkstation,unitManager,context,
                    summaryReportRequestCompletedToday);
            TestProcStatusSummaryReportResult testProcSummaryReportResultCompletedToday = testProcSummaryReportCompletedToday
                    .runReport();
            if (testProcSummaryReportResultCompletedToday != null
                    && testProcSummaryReportResultCompletedToday.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultCompletedToday
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormCompletedToday(row.getTotalFormCount());

                }
            }

            /*
             * Form count to be start in next 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestToBeStartInNext5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestToBeStartInNext5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestToBeStartInNext5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestToBeStartInNext5D.setForecastStartDate(new DateRangeFilter(beginningOfToday,
                    dateUtils.getAdvanceDays(beginningOfToday, 5), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestStartedToday
                    .setResponseStatus(new FormStatusEnum[] { FormStatusEnum.NOTSTARTED, FormStatusEnum.INPROGRESS,
                            FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestStartedToday.setGroupingSet(
                    Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate));
            TestProcStatusSummaryReport testProcSummaryReportToBeStartInNext5D = new TestProcStatusSummaryReport(
                    persistWrapper,dummyWorkstation, unitManager,context, summaryReportRequestToBeStartInNext5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultToBeStartInNext5D = testProcSummaryReportToBeStartInNext5D
                    .runReport();
            if (testProcSummaryReportResultToBeStartInNext5D != null
                    && testProcSummaryReportResultToBeStartInNext5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultToBeStartInNext5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormToBeStartInNext5Days(row.getTotalFormCount());

                }
            }
            /*
             * Form count started in next 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestStartedInNext5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestStartedInNext5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestStartedInNext5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestStartedInNext5D.setForecastStartDate(new DateRangeFilter(beginningOfToday,
                    dateUtils.getAdvanceDays(beginningOfToday, 5), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestStartedToday.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS,
                    FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestStartedToday.setGroupingSet(
                    Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate));
            TestProcStatusSummaryReport testProcSummaryReportStartedInNext5D = new TestProcStatusSummaryReport(persistWrapper,dummyWorkstation,unitManager, context,
                    summaryReportRequestStartedInNext5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultStartedInNext5D = testProcSummaryReportStartedInNext5D
                    .runReport();
            if (testProcSummaryReportResultStartedInNext5D != null
                    && testProcSummaryReportResultStartedInNext5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultStartedInNext5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormStartedInNext5Days(row.getTotalFormCount());

                }
            }

            /*
             * Form count to be complete in next 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestToBeCompleteNext5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestToBeCompleteNext5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestToBeCompleteNext5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestStartedInNext5D.setForecastStartDate(new DateRangeFilter(beginningOfToday,
                    dateUtils.getAdvanceDays(beginningOfToday, 5), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestToBeCompleteNext5D.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS,
                    FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestToBeCompleteNext5D
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportToBeCompleteNext5D = new TestProcStatusSummaryReport(
                    persistWrapper, dummyWorkstation,unitManager,context, summaryReportRequestToBeCompleteNext5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultToBeCompleteNext5D = testProcSummaryReportToBeCompleteNext5D
                    .runReport();
            if (testProcSummaryReportResultToBeCompleteNext5D != null
                    && testProcSummaryReportResultToBeCompleteNext5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultToBeCompleteNext5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormToBeCompleteToday(row.getTotalFormCount());

                }
            }

            /*
             * Form count complete in next 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestCompletedNext5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestCompletedNext5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestCompletedNext5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestStartedInNext5D.setForecastStartDate(new DateRangeFilter(beginningOfToday,
                    dateUtils.getAdvanceDays(beginningOfToday, 5), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestCompletedNext5D.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.COMPLETE });
            summaryReportRequestCompletedNext5D
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportCompletedNext5D = new TestProcStatusSummaryReport(persistWrapper,dummyWorkstation, unitManager, context,
                    summaryReportRequestCompletedNext5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultCompletedNext5D = testProcSummaryReportCompletedNext5D
                    .runReport();
            if (testProcSummaryReportResultCompletedNext5D != null
                    && testProcSummaryReportResultCompletedNext5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultCompletedNext5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormCompletedInNext5Days(row.getCompletedCount());

                }
            }

            /*
             * Form count to be start in last 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestToBeStartInLast5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestToBeStartInLast5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestToBeStartInLast5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestToBeStartInLast5D
                    .setForecastStartDate(new DateRangeFilter(dateUtils.getAdvanceDays(beginningOfToday, -5),
                            dateUtils.getAdvanceDays(beginningOfToday, -1), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestStartedToday
                    .setResponseStatus(new FormStatusEnum[] { FormStatusEnum.NOTSTARTED, FormStatusEnum.INPROGRESS,
                            FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestStartedToday.setGroupingSet(
                    Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate));
            TestProcStatusSummaryReport testProcSummaryReportToBeStartInLast5D = new TestProcStatusSummaryReport(
                    persistWrapper, dummyWorkstation, unitManager, context, summaryReportRequestToBeStartInLast5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultToBeStartInLast5D = testProcSummaryReportToBeStartInLast5D
                    .runReport();
            if (testProcSummaryReportResultToBeStartInLast5D != null
                    && testProcSummaryReportResultToBeStartInLast5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultToBeStartInLast5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormToBeStartInLast5Days(row.getTotalFormCount());

                }
            }

            /*
             * Form count started last 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestStartedInLast5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestStartedInLast5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestStartedInLast5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestStartedInLast5D
                    .setForecastStartDate(new DateRangeFilter(dateUtils.getAdvanceDays(beginningOfToday, -5),
                            dateUtils.getAdvanceDays(beginningOfToday, -1), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestStartedToday.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS,
                    FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestStartedToday.setGroupingSet(
                    Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate));
            TestProcStatusSummaryReport testProcSummaryReportStartedInLast5D = new TestProcStatusSummaryReport(persistWrapper,  dummyWorkstation,  unitManager, context,
                    summaryReportRequestStartedInLast5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultStartedInLast5D = testProcSummaryReportStartedInLast5D
                    .runReport();
            if (testProcSummaryReportResultStartedInLast5D != null
                    && testProcSummaryReportResultStartedInLast5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultStartedInLast5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormStartedInLast5Days(row.getTotalFormCount());

                }
            }

            /*
             * Form count to be complete in last 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestToBeCompleteLast5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestToBeCompleteLast5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestToBeCompleteLast5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestStartedInNext5D
                    .setForecastStartDate(new DateRangeFilter(dateUtils.getAdvanceDays(beginningOfToday, -5),
                            dateUtils.getAdvanceDays(beginningOfToday, -1), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestToBeCompleteLast5D.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.INPROGRESS,
                    FormStatusEnum.COMPLETE, FormStatusEnum.PAUSED, FormStatusEnum.VERIFIED });
            summaryReportRequestToBeCompleteLast5D
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportToBeCompleteLast5D = new TestProcStatusSummaryReport(
                    persistWrapper, dummyWorkstation, unitManager, context, summaryReportRequestToBeCompleteLast5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultToBeCompleteLast5D = testProcSummaryReportToBeCompleteLast5D
                    .runReport();
            if (testProcSummaryReportResultToBeCompleteLast5D != null
                    && testProcSummaryReportResultToBeCompleteLast5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultToBeCompleteLast5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormToBeCompleteInLast5Days(row.getTotalFormCount());

                }
            }

            /*
             * Form count completed in last 5 days
             */
            TestProcStatusSummaryReportRequest summaryReportRequestCompletedLast5D = new TestProcStatusSummaryReportRequest();
            summaryReportRequestCompletedLast5D
                    .setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequestCompletedLast5D.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequestStartedInNext5D
                    .setForecastStartDate(new DateRangeFilter(dateUtils.getAdvanceDays(beginningOfToday, -5),
                            dateUtils.getAdvanceDays(beginningOfToday, -1), TimeZone.getTimeZone(site.getTimeZone())));
            summaryReportRequestCompletedLast5D.setResponseStatus(new FormStatusEnum[] { FormStatusEnum.COMPLETE });
            summaryReportRequestCompletedLast5D
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate));
            TestProcStatusSummaryReport testProcSummaryReportCompletedLast5D = new TestProcStatusSummaryReport(persistWrapper, dummyWorkstation,unitManager,context,
                    summaryReportRequestCompletedLast5D);
            TestProcStatusSummaryReportResult testProcSummaryReportResultCompleteLast5D = testProcSummaryReportCompletedLast5D
                    .runReport();
            if (testProcSummaryReportResultCompleteLast5D != null
                    && testProcSummaryReportResultCompleteLast5D.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResultCompleteLast5D
                        .getReportResult())
                {
                    // long count = row.getInProgressCount()
                    // +row.getPausedCount() +row.getVerifiedCount()+
                    // row.getCompletedCount();
                    formScheduleResponseBean.setFormCompletedInLast5Days(row.getTotalFormCount());

                }
            }
            return formScheduleResponseBean;
        }
        catch (Exception e)
        {

            throw e;
        }
    }

    /*
     * Form workstation summary
     */

    @GetMapping("/getFormWorkstationSummary")
    public List<FormWorkstationSummaryResponseBean> getFormWorkstationSummary(@RequestBody
            FormSummaryRequestBean formSummaryRequestBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            HashMap<Integer, FormWorkstationSummaryResponseBean> result = new HashMap<Integer, FormWorkstationSummaryResponseBean>();
            TestProcStatusSummaryReportRequest summaryReportRequest = new TestProcStatusSummaryReportRequest();
            summaryReportRequest.setProjectOIDList(Arrays.asList(formSummaryRequestBean.getProjectOID()));
            summaryReportRequest.setUnitOID(formSummaryRequestBean.getUnitOID());
            summaryReportRequest
                    .setGroupingSet(Arrays.asList(TestProcStatusSummaryReportRequest.GroupingCol.workstation));
            TestProcStatusSummaryReport testProcSummaryReport = new TestProcStatusSummaryReport(persistWrapper, dummyWorkstation,unitManager,context,
                    summaryReportRequest);
            TestProcStatusSummaryReportResult testProcSummaryReportResult = testProcSummaryReport.runReport();
            if (testProcSummaryReportResult != null && testProcSummaryReportResult.getReportResult() != null)
            {

                for (TestProcStatusSummaryReportResultRow row : testProcSummaryReportResult.getReportResult())
                {
                    FormWorkstationSummaryResponseBean formWorkstationSummaryResponseBean = new FormWorkstationSummaryResponseBean();
                    if (result.containsKey(row.getWorkstationPk()))
                    {
                        formWorkstationSummaryResponseBean = result.get(row.getWorkstationPk());
                    } else
                    {
                        formWorkstationSummaryResponseBean.setWorkstationPk(row.getWorkstationPk());
                        formWorkstationSummaryResponseBean.setWorkstationName(row.getWorkstationName());
                        formWorkstationSummaryResponseBean.setWorkstationDescription(row.getWorkstationDescription());
                        formWorkstationSummaryResponseBean.setProjectPk(row.getProjectPk());
                        formWorkstationSummaryResponseBean.setProjectName(row.getProjectName());
                        formWorkstationSummaryResponseBean.setProjectDescription(row.getProjectDescription());
                        formWorkstationSummaryResponseBean.setUnitPk(row.getUnitPk());
                        formWorkstationSummaryResponseBean.setUnitName(row.getUnitName());
                    }

                    formWorkstationSummaryResponseBean.setInProgressCount(row.getInProgressCount());
                    formWorkstationSummaryResponseBean.setCompletedCount(row.getCompletedCount());
                    formWorkstationSummaryResponseBean.setTotalFormCount(row.getTotalFormCount());
                    formWorkstationSummaryResponseBean.setFailCount(row.getFailCount());
                    formWorkstationSummaryResponseBean.setNoOfComments(row.getNoOfComments());
                    formWorkstationSummaryResponseBean.setPercentComplete(row.getPercentComplete());
                    formWorkstationSummaryResponseBean.setStatus(row.getWorkstationStatus());
                    result.put(row.getWorkstationPk(), formWorkstationSummaryResponseBean);

                }

            }
            List<FormWorkstationSummaryResponseBean> formWorkstationResponse = new ArrayList<FormWorkstationSummaryResponseBean>();
            for (Integer key : result.keySet())
            {
                formWorkstationResponse.add(result.get(key));
            }
            return formWorkstationResponse;

        }
        catch (Exception e)
        {

            throw e;
        }
    }

    private Document createReturnXML(String status, String message, List<String> output)
    {
        Element rootElement = new Element("response");
        Document surveyDoc = new Document(rootElement);

        rootElement.setAttribute("status", status);
        rootElement.setAttribute("message", message);

        if (output!= null)
        {
            rootElement.addContent(createFormListXML(output));
        }

        return surveyDoc;
    }

    public Element createFormListXML(List<String> output)
    {
        int OPENITEM_TESTID = 123;
        Element sRootElement = new Element("tests");
        for (Object element : output)
        {
            String testName = (String) element;
            Element sElement = new Element("test");
            // using DeviceActionServlet.OPENITEM_TESTID
            // sElement.setAttribute("testId", "" + DeviceActionServlet.OPENITEM_TESTID);
            sElement.setAttribute("testId", "" + OPENITEM_TESTID);
            sElement.setAttribute("name", testName);
            sRootElement.addContent(sElement);

        }
        return sRootElement;
    }
//commenting methods using vaadin component
   /* private FormItemBase getSectionBeanForFormLinear(List<FormItemBase> formItems, SurveyItem sItem, User user)
    {
        SectionBean section = new SectionBean();
        section.setFormItemId(sItem.getSurveyItemId());
        section.setOrderNo(sItem.getOrderNum());
        section.setItemNo(((Section) sItem).getPageTitle());
        section.setDescription(((Section) sItem).getDescription());
        section.setInstructionFileName(((Section) sItem).getInstructionFileName());
        section.setInstructionFileDisplayName(((Section) sItem).getInstructionFileDisplayName());

        List<SurveyItem> children = ((Container) sItem).getChildren();
        for (SurveyItem aChildItem : children)
        {
            if (aChildItem instanceof BomInspectItemGroupAnswerType)
            {
                FormItemBase aBean = getBomInspectItemGroupAnswerTypeBeanForFormLinear(formItems, aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            } else if (aChildItem instanceof AdvancedBomInspectItemGroupAnswerType)
            {
                FormItemBase aBean = getAdvBomInspectItemGroupAnswerTypeBeanForFormLinear(formItems, aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            } else if (aChildItem instanceof TextBoxAnswerType)
            {
                FormItemBase aBean = getTextBoxAnswerTypeBean(aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            } else if (aChildItem instanceof TextAreaAnswerType)
            {
                FormItemBase aBean = getTextAreaAnswerTypeBean(aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            } else if (aChildItem instanceof RadioButtonAnswerType)
            {
                FormItemBase aBean = getOptionGroupAnswerTypeBean(aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            } else if (aChildItem instanceof InformationText)
            {
                FormItemBase aBean = getInfoTextTypeBean(aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            } else if (aChildItem instanceof PictureType)
            {
                FormItemBase aBean = getPictureTypeBean(aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            } else if (aChildItem instanceof SignatureCaptureAnswerType)
            {
                FormItemBase aBean = getSignatureCaptureAnswerTypeBean(aChildItem);
                aBean.setParentId(sItem.getSurveyItemId());
                formItems.add(aBean);
            }
        }

        return section;
    }*/

    /* private FormItemBase getBomInspectItemGroupAnswerTypeBeanForFormLinear(List<FormItemBase> formItems,
                                                                            SurveyItem sItem)
     {
         BomInspectItemGroupAnswerType item = (BomInspectItemGroupAnswerType) sItem;

         AdvancedBomInspectionItemGroupItemBean aGroupItem = new AdvancedBomInspectionItemGroupItemBean();
         aGroupItem.setFormItemId(sItem.getSurveyItemId());
         aGroupItem.setOrderNo(sItem.getOrderNum());
         aGroupItem.setItemNo(item.getQuestionText());
         aGroupItem.setDescription(item.getQuestionTextDescription());

         List<SurveyItem> children = item.getChildren();
         for (SurveyItem aChildItem : children)
         {
             if (aChildItem instanceof BomInspectItemAnswerType)
             {
                 FormItemBase aBean = getBomInspectItemAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 formItems.add(aBean);
             }
         }

         return aGroupItem;
     }

     private FormItemBase getAdvBomInspectItemGroupAnswerTypeBeanForFormLinear(List<FormItemBase> formItems,
                                                                               SurveyItem sItem)
     {
         AdvancedBomInspectItemGroupAnswerType item = (AdvancedBomInspectItemGroupAnswerType) sItem;

         AdvancedBomInspectionItemGroupItemBean aGroupItem = new AdvancedBomInspectionItemGroupItemBean();
         aGroupItem.setFormItemId(sItem.getSurveyItemId());
         aGroupItem.setOrderNo(sItem.getOrderNum());
         aGroupItem.setItemNo(item.getQuestionText());
         aGroupItem.setDescription(item.getQuestionTextDescription());

         List<SurveyItem> children = item.getChildren();
         for (SurveyItem aChildItem : children)
         {
             if (aChildItem instanceof AdvancedBomInspectItemAnswerType)
             {
                 FormItemBase aBean = getAdvBomInspectItemAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 formItems.add(aBean);
             }
         }

         return aGroupItem;
     }

     private FormItemBase getSectionBean(SurveyItem sItem, User user)
     {
         SectionBean section = new SectionBean();
         section.setFormItemId(sItem.getSurveyItemId());
         section.setOrderNo(sItem.getOrderNum());
         section.setItemNo(((Section) sItem).getPageTitle());
         section.setDescription(((Section) sItem).getDescription());
         section.setInstructionFileName(((Section) sItem).getInstructionFileName());
         section.setInstructionFileDisplayName(((Section) sItem).getInstructionFileDisplayName());

         List<SurveyItem> children = ((Container) sItem).getChildren();
         for (SurveyItem aChildItem : children)
         {
             if (aChildItem instanceof BomInspectItemGroupAnswerType)
             {
                 FormItemBase aBean = getBomInspectItemGroupAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             } else if (aChildItem instanceof AdvancedBomInspectItemGroupAnswerType)
             {
                 FormItemBase aBean = getAdvBomInspectItemGroupAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             } else if (aChildItem instanceof TextBoxAnswerType)
             {
                 FormItemBase aBean = getTextBoxAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             } else if (aChildItem instanceof TextAreaAnswerType)
             {
                 FormItemBase aBean = getTextAreaAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             } else if (aChildItem instanceof RadioButtonAnswerType)
             {
                 FormItemBase aBean = getOptionGroupAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             } else if (aChildItem instanceof InformationText)
             {
                 FormItemBase aBean = getInfoTextTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             } else if (aChildItem instanceof PictureType)
             {
                 FormItemBase aBean = getPictureTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             } else if (aChildItem instanceof SignatureCaptureAnswerType)
             {
                 FormItemBase aBean = getSignatureCaptureAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 section.getChildren().add(aBean);
             }
         }

         return section;
     }

     private FormItemBase getBomInspectItemGroupAnswerTypeBean(SurveyItem sItem)
     {
         BomInspectItemGroupAnswerType item = (BomInspectItemGroupAnswerType) sItem;

         AdvancedBomInspectionItemGroupItemBean aGroupItem = new AdvancedBomInspectionItemGroupItemBean();
         aGroupItem.setFormItemId(sItem.getSurveyItemId());
         aGroupItem.setOrderNo(sItem.getOrderNum());
         aGroupItem.setItemNo(item.getQuestionText());
         aGroupItem.setDescription(item.getQuestionTextDescription());

         List<SurveyItem> children = item.getChildren();
         for (SurveyItem aChildItem : children)
         {
             if (aChildItem instanceof BomInspectItemAnswerType)
             {
                 FormItemBase aBean = getBomInspectItemAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 aGroupItem.getChildren().add(aBean);
             }
         }

         return aGroupItem;
     }

     private FormItemBase getBomInspectItemAnswerTypeBean(SurveyItem sItem)
     {
         BomInspectItemAnswerType item = (BomInspectItemAnswerType) sItem;

         AdvancedBomInspectionItemBean aBean = new AdvancedBomInspectionItemBean();
         aBean.setFormItemId(sItem.getSurveyItemId());
         aBean.setItemNo(item.getQuestionText());
         aBean.setDescription(item.getQuestionTextDescription());
         aBean.setLocation(item.getLocation());
         aBean.setExpectedValue(item.getExpectedValue());
         aBean.setUnitOfMeasuer(item.getUnit());
         aBean.setImageFileName(item.getImageFileName());
         aBean.setImageFileDisplayName(item.getImageFileDisplayName());

         aBean.setRequired(item.isRequired());
         aBean.setShowCommentBox(item.isShowComments());
         aBean.setShowPassFail(item.isShowPassFail());
         aBean.setShowNotApplicable(item.isShowNotApplicable());

         OptionList flexiFields = item.getFlexiFields();
         for (int i = 0; i < flexiFields.size(); i++)
         {
             Option aOption = flexiFields.getOptionByIndex(i);

             TCell aCell = new TCell();
             aCell.setCellIndex(aOption.getValue());
             aCell.setLabel(aOption.getText());
             aCell.setValue(aOption.getTextDesc());
             if (aOption.getType() == null)
                 aCell.setCellType(BomCellTypeEnum.Label);
             else if (aOption.getType() == BomTypesEnum.CommentBox)
                 aCell.setCellType(BomCellTypeEnum.TextArea);
             else if (aOption.getType() == BomTypesEnum.NumericTextBox)
                 aCell.setCellType(BomCellTypeEnum.NumericText);
             else if (aOption.getType() == BomTypesEnum.RadioGroup)
                 aCell.setCellType(BomCellTypeEnum.Option);
             else if (aOption.getType() == BomTypesEnum.RadioGroupMandatory)
                 aCell.setCellType(BomCellTypeEnum.OptionMandatory);
             else if (aOption.getType() == BomTypesEnum.CheckboxGroup)
                 aCell.setCellType(BomCellTypeEnum.OptionMultiSelect);
             else if (aOption.getType() == BomTypesEnum.CheckboxGroupMandatory)
                 aCell.setCellType(BomCellTypeEnum.OptionMultiSelectMandatory);
             else if (aOption.getType() == BomTypesEnum.Date)
                 aCell.setCellType(BomCellTypeEnum.Date);
             else if (aOption.getType() == BomTypesEnum.DateMandatory)
                 aCell.setCellType(BomCellTypeEnum.DateMandatory);
             else if (aOption.getType() == BomTypesEnum.DateTime)
                 aCell.setCellType(BomCellTypeEnum.DateTime);
             else if (aOption.getType() == BomTypesEnum.DateTimeMandatory)
                 aCell.setCellType(BomCellTypeEnum.DateTimeMandatory);
             else if (aOption.getType() == BomTypesEnum.TextBox)
             {
                 if (item.getDataType() == DataTypes.DATATYPE_INTEGER)
                 {
                     aCell.setCellType(BomCellTypeEnum.NumericText);
                 } else
                 {
                     aCell.setCellType(BomCellTypeEnum.Text);
                 }
             }

             if (aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_ITEMNO)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_DESCRIPTION)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_LOCATION)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_EXPECTED)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_UNIT)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_COMMENTS)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_DATATYPE)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_FLAGS)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_RESULT))
             {

             } else
             {
                 aBean.getCells().add(aCell);
             }
         }

         return aBean;
     }

     private FormItemBase getAdvBomInspectItemGroupAnswerTypeBean(SurveyItem sItem)
     {
         AdvancedBomInspectItemGroupAnswerType item = (AdvancedBomInspectItemGroupAnswerType) sItem;

         AdvancedBomInspectionItemGroupItemBean aGroupItem = new AdvancedBomInspectionItemGroupItemBean();
         aGroupItem.setFormItemId(sItem.getSurveyItemId());
         aGroupItem.setOrderNo(sItem.getOrderNum());
         aGroupItem.setItemNo(item.getQuestionText());
         aGroupItem.setDescription(item.getQuestionTextDescription());

         List<SurveyItem> children = item.getChildren();
         for (SurveyItem aChildItem : children)
         {
             if (aChildItem instanceof AdvancedBomInspectItemAnswerType)
             {
                 FormItemBase aBean = getAdvBomInspectItemAnswerTypeBean(aChildItem);
                 aBean.setParentId(sItem.getSurveyItemId());
                 aGroupItem.getChildren().add(aBean);
             }
         }

         return aGroupItem;
     }

     private FormItemBase getAdvBomInspectItemAnswerTypeBean(SurveyItem sItem)
     {
         AdvancedBomInspectItemAnswerType item = (AdvancedBomInspectItemAnswerType) sItem;

         AdvancedBomInspectionItemBean aBean = new AdvancedBomInspectionItemBean();
         aBean.setFormItemId(sItem.getSurveyItemId());
         aBean.setItemNo(item.getQuestionText());
         aBean.setDescription(item.getQuestionTextDescription());
         aBean.setLocation(item.getLocation());
         aBean.setExpectedValue(item.getExpectedValue());
         aBean.setUnitOfMeasuer(item.getUnit());
         aBean.setImageFileName(item.getImageFileName());
         aBean.setImageFileDisplayName(item.getImageFileDisplayName());

         aBean.setRequired(item.isRequired());
         aBean.setShowCommentBox(item.isShowComments());
         aBean.setShowPassFail(item.isShowPassFail());
         aBean.setShowNotApplicable(item.isShowNotApplicable());
         aBean.setCommentFieldMaxLength(item.getCommentFieldMaxLength());
         aBean.setShowEquipmentSelector(item.isShowEquipmentSelector());
         aBean.setMakeAttachmentMandatory(item.isMakeAttachmentMandatory());

         OptionList flexiFields = item.getFlexiFields();
         for (int i = 0; i < flexiFields.size(); i++)
         {
             Option aOption = flexiFields.getOptionByIndex(i);

             TCell aCell = new TCell();
             aCell.setCellIndex(aOption.getValue());
             aCell.setLabel(aOption.getText());
             aCell.setValue(aOption.getTextDesc());
             if (aOption.getType() == null)
                 aCell.setCellType(BomCellTypeEnum.Label);
             else if (aOption.getType() == BomTypesEnum.CommentBox)
                 aCell.setCellType(BomCellTypeEnum.TextArea);
             else if (aOption.getType() == BomTypesEnum.NumericTextBox)
                 aCell.setCellType(BomCellTypeEnum.NumericText);
             else if (aOption.getType() == BomTypesEnum.RadioGroup)
                 aCell.setCellType(BomCellTypeEnum.Option);
             else if (aOption.getType() == BomTypesEnum.RadioGroupMandatory)
                 aCell.setCellType(BomCellTypeEnum.OptionMandatory);
             else if (aOption.getType() == BomTypesEnum.CheckboxGroup)
                 aCell.setCellType(BomCellTypeEnum.OptionMultiSelect);
             else if (aOption.getType() == BomTypesEnum.CheckboxGroupMandatory)
                 aCell.setCellType(BomCellTypeEnum.OptionMultiSelectMandatory);
             else if (aOption.getType() == BomTypesEnum.Date)
                 aCell.setCellType(BomCellTypeEnum.Date);
             else if (aOption.getType() == BomTypesEnum.DateMandatory)
                 aCell.setCellType(BomCellTypeEnum.DateMandatory);
             else if (aOption.getType() == BomTypesEnum.DateTime)
                 aCell.setCellType(BomCellTypeEnum.DateTime);
             else if (aOption.getType() == BomTypesEnum.DateTimeMandatory)
                 aCell.setCellType(BomCellTypeEnum.DateTimeMandatory);
             else if (aOption.getType() == BomTypesEnum.TextBox)
             {
                 if (item.getDataType() == DataTypes.DATATYPE_INTEGER)
                 {
                     aCell.setCellType(BomCellTypeEnum.NumericText);
                 } else
                 {
                     aCell.setCellType(BomCellTypeEnum.Text);
                 }
             }

             if (aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_ITEMNO)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_DESCRIPTION)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_LOCATION)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_EXPECTED)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_UNIT)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_COMMENTS)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_DATATYPE)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_FLAGS)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE)
                     || aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_RESULT))
             {

             } else
             {
                 aBean.getCells().add(aCell);
             }
         }

         return aBean;
     }
 //commenting methods using vaadin component
     private FormItemBase getTextAreaAnswerTypeBean(SurveyItem sItem)
     {
         TextAreaAnswerType item = (TextAreaAnswerType) sItem;

         TextAreaItemBean aBean = new TextAreaItemBean();
         aBean.setFormItemId(sItem.getSurveyItemId());
         aBean.setOrderNo(sItem.getOrderNum());
         aBean.setTitle(item.getQuestionText());
         aBean.setDescription(item.getQuestionTextDescription());
         aBean.setMaxLength(item.getMaxLength());
         aBean.setRequired(item.isRequired());
         return aBean;
     }

     private FormItemBase getTextBoxAnswerTypeBean(SurveyItem sItem)
     {
         TextBoxAnswerType item = (TextBoxAnswerType) sItem;

         TextBoxItemBean aBean = new TextBoxItemBean();
         aBean.setFormItemId(sItem.getSurveyItemId());
         aBean.setOrderNo(sItem.getOrderNum());
         aBean.setTitle(item.getQuestionText());
         aBean.setDescription(item.getQuestionTextDescription());
         aBean.setMaxLength(item.getMaxLength());
         aBean.setRequired(item.isRequired());
         aBean.setPresetValue(item.getPresetValue());
         return aBean;
     }

     private FormItemBase getPictureTypeBean(SurveyItem sItem)
     {
         PictureType item = (PictureType) sItem;

         PictureItemBean aBean = new PictureItemBean();
         aBean.setFormItemId(sItem.getSurveyItemId());
         aBean.setOrderNo(sItem.getOrderNum());
         aBean.setTitle(item.getQuestionText());
         aBean.setDescription(item.getQuestionTextDescription());
         aBean.setImageFileName(item.getPictureFileName());
         aBean.setImageFileDisplayName(item.getImageFileDisplayName());
         return aBean;
     }
 */
    @GetMapping("/getSignatureCaptureAnswerTypeBean")
    private FormItemBase getSignatureCaptureAnswerTypeBean(@RequestBody  SurveyItem sItem)
    {
        SignatureCaptureAnswerType item = (SignatureCaptureAnswerType) sItem;

        SignatureCaptureItemBean aBean = new SignatureCaptureItemBean();
        aBean.setFormItemId(sItem.getSurveyItemId());
        aBean.setOrderNo(sItem.getOrderNum());
        aBean.setTitle(item.getQuestionText());
        aBean.setDescription(item.getQuestionTextDescription());
        aBean.setRequired(item.isRequired());
        return aBean;
    }
//commenting methods using vaadin component
   /* private FormItemBase getInfoTextTypeBean(SurveyItem sItem)
    {
        InformationText item = (InformationText) sItem;

        InformationTextItemBean aBean = new InformationTextItemBean();
        aBean.setFormItemId(sItem.getSurveyItemId());
        aBean.setOrderNo(sItem.getOrderNum());
        aBean.setInfoText(item.getInfoText());
        return aBean;
    }

    private FormItemBase getOptionGroupAnswerTypeBean(SurveyItem sItem)
    {
        RadioButtonAnswerType item = (RadioButtonAnswerType) sItem;

        OptionGroupItemBean aBean = new OptionGroupItemBean();
        aBean.setFormItemId(sItem.getSurveyItemId());
        aBean.setOrderNo(sItem.getOrderNum());
        aBean.setTitle(item.getQuestionText());
        aBean.setDescription(item.getQuestionTextDescription());
        aBean.setRequired(item.isRequired());
        aBean.setMultiSelect(item.isMultiSelect());
        OptionList opts = item.getOptions();
        for (int i = 0; i < opts.size(); i++)
        {
            TCell aCell = new TCell();
            aCell.setLabel(opts.getOptionByIndex(i).getText());
            aCell.setValue(opts.getOptionByIndex(i).getValue());
            aBean.getOptions().add(aCell);
        }
        return aBean;
    }*/

    private class ProjectConfigSettings
    {
        Boolean EnableInterimSubmitForChecksheets = null;
    }

}
