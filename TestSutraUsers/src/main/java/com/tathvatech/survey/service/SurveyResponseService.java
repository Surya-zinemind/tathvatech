package com.tathvatech.survey.service;

import com.tathvatech.forms.common.EntityVersionReviewProxy;
import com.tathvatech.forms.common.QuestionResponseStatus;
import com.tathvatech.forms.entity.FormItemResponse;
import com.tathvatech.forms.entity.FormResponseClientSubmissionRev;
import com.tathvatech.forms.response.FormResponseBean;
import com.tathvatech.forms.response.FormResponseStats;
import com.tathvatech.forms.response.ResponseMaster;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.project.entity.Project;
import com.tathvatech.survey.common.SectionResponseQuery;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.common.SurveyItem;
import com.tathvatech.survey.common.SurveySaveItem;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.inf.SurveySaveItemBase;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.unit.common.TestableEntity;
import com.tathvatech.user.OID.FormItemResponseOID;
import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;



public interface SurveyResponseService {
    void changeResponseStatus(UserContext userContext, int responseId, String responseStatus)
            throws Exception;

    SurveyResponse ceateDummyResponse(UserContext context, SurveyResponse surveyResponse) throws Exception;

    SurveyResponse updateSurveyResponse(UserContext context, Project project, TestableEntity unit, SurveyDefinition surveyDef,
                                        int responseId, HashMap<SurveySaveItemBase, SurveyItemResponse> surveyItemResponseMap, List sectionsToSave) throws Exception;

    SurveyResponse saveSectionResponses(UserContext context, Project project, TestableEntity unit,
                                        SurveyResponse surveyResponse, List sectionsToSave) throws Exception;

    SurveyResponse saveSpecificQuestionResponse(UserContext context, Project project, TestableEntity mItem,
                                                SurveyResponse surveyResponse, List questions) throws Exception;

    void finalizeSurveyResponse(UserContext userContext, SurveyDefinition surveyDef, int responseId)
            throws Exception;

    void finalizeSurveyResponse(UserContext userContext, SurveyDefinition surveyDef, int responseId,
                                Date responseCompleteTime)throws Exception;

    void finalizeSurveyResponseImpl(UserContext userContext, SurveyDefinition surveyDef, int responseId,
                                    Date responseCompleteTime) throws Exception;

    void markResponseAsOld(UserContext context, ResponseMasterNew response)throws Exception;

    void saveResponseStateAsSubmitRecord(UserContext context,
                                         int responseId, ResponseSubmissionBookmark.SubmissionTypeEnum submissionType) throws Exception;

    SurveyResponse copyResponse(UserContext context, int responseIdToCopy) throws Exception;

    SurveyResponse getSurveyResponse(int responseId) throws Exception;

    SurveyResponse getSurveyResponse(SurveyDefinition surveyDef, int responseId) throws Exception;

    SurveyItemResponse getSurveyItemResponse(SurveyDefinition surveyDef, int responseId, String surveyItemId) throws Exception;

    SurveyItemResponse getSurveyItemResponse(SurveyItem sItem, String surveyItemId,
                                             int responseId);

    List getSurveyItemResponse(SurveyDefinition surveyDef, String surveyItemId,
                               ResponseMaster[] responseMasterSet) throws Exception;

    // public static long getResponseCountByDateRange(Survey survey, Date
    // startDate, Date endDate)throws Exception
    // {
    // long count = 0;
    //
    // String surveyTable = survey.getString("Survey_dbTable");
    // String sql = "select count(" +
    // CommonResponseColumns.ResponseId.getDbColumn() + ") from " + surveyTable
    // + " where " +
    // "surveyPk=? and " +
    // CommonResponseColumns.ResponseStartTimeStamp.getDbColumn() + " >= ? and "
    // +
    // CommonResponseColumns.ResponseStartTimeStamp.getDbColumn() + " <= ?" ;
    //
    // if (logger.isDebugEnabled())
    // {
    // logger.debug("GetResponseCount Query is - " + sql);
    // }
    //
    // //run query
    // Connection conn = null;
    // PreparedStatement stmt = null;
    // try
    // {
    // conn = ServiceLocator.locate().getConnection();
    // stmt = conn.prepareStatement(sql);
    // stmt.setString(1, survey.getPk());
    // stmt.setDate(2, new java.sql.Date(startDate.getTime()));
    // stmt.setDate(3, new java.sql.Date(endDate.getTime()));
    // ResultSet rs = stmt.executeQuery();
    // if(rs.next())
    // {
    // count = rs.getLong(1);
    // }
    // }
    // catch (SQLException E)
    // {
    // logger.error("Error getting response count for survey by date range" +
    // " :: " + E.getMessage());
    // if (logger.isDebugEnabled())
    // {
    // logger.debug(E.getMessage(), E);
    // }
    // throw new Exception();
    // }
    // finally
    // {
    // try
    // {
    // if(stmt != null)
    // {
    // stmt.close();
    // }
    // if(conn != null)
    // {
    // conn.close();
    // }
    // }
    // catch(Exception e)
    // {
    // }
    // }
    // return count;
    // }
    List<String> getResponseStatusSetForForm(UserContext context, Survey survey) throws Exception;

    List getResponseMastersForRespondent(int surveyPk, int respondentPk) throws Exception;

    ResponseMaster getResponseMaster(int surveyPk, String responseId) throws Exception;

    // ///////////////// for testsutra
    ResponseMasterNew getResponseMaster(int responseId) throws Exception;

    ResponseMasterNew getLatestResponseMasterForTest(TestProcOID testProcOID) throws Exception;

    //returns the Response entry for a previous form revision on a testProc.
    ResponseMasterNew getResponseMasterForTestHistoryRecord(TestProcOID testProcOID, FormOID formOID);

    void verifyResponse(UserContext userContext, SurveyResponse sResponse, String comments)
            throws Exception;

    void rejectResponse(UserContext userContext, SurveyResponse sResponse, String comments)
            throws Exception;

    void approveResponse(UserContext userContext, ResponseMasterNew resp, String comments)
            throws Exception;

    void approveResponseWithComments(UserContext userContext, ResponseMasterNew resp, String comments)
                throws Exception;

    void rejectApproval(UserContext userContext, SurveyResponse sResponse, String comments)
                throws Exception;

    void reopenApprovedForm(UserContext userContext, SurveyResponse sResponse, String comments)
            throws Exception;

    FormResponseStats getCommentCountAndPercentComplete(List sItems, SurveyResponse surveyResponse);

    QuestionResponseStatus getAnswerStatValue(SurveySaveItem sItem, SurveyItemResponse sItemResponse);

    //	public static boolean hasResponseCreated(int testProcPk)throws Exception
    //	{
    //		int responseCount = persistWrapper.read(Integer.class,
    //				"select count(responseId) from TAB_RESPONSE where testProcPk=?",
    //				testProcPk);
    //
    //		if(responseCount > 0)
    //			return true;
    //		else
    //			return false;
    //	}
    List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd);

    List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd, int responseId)throws Exception;

    SectionResponseQuery getSectionResponseSummary(SurveyDefinition sd, int responseId, String sectionId)throws Exception;

    SectionResponseQuery getSectionResponseSummary(int responseId, String sectionId)throws Exception;

    FormItemResponse getFormItemResponse(FormItemResponseOID formItemResponseOID);

    HashMap<String, FormItemResponse> getFormItemResponses(int responseId);

    FormItemResponse getFormItemResponse(int responseId, String surveyItemId, boolean createIfNoExist) throws Exception;

    FormItemResponse createFormItemResponse(int responseId, String surveyItemId) throws Exception;

    void addToResponseVAComments(UserContext userContext, ResponseMasterNew responseMaster, String vCommentToAdd,
                                 String aCommentToAdd) throws Exception;

    FormResponseClientSubmissionRev getFormClientSubmissionRevision(int responseId);

    FormResponseClientSubmissionRev saveFormClientSubmissionRevision(UserContext context, int responseId, String revision) throws Exception;

    FormResponseBean getFormResponseBean(UserContext context, int responseId)throws Exception;

    void saveSyncErrorResponse(UserContext context, int responseId,
                               FormResponseBean formResponseBean)throws Exception;

    List<EntityVersionReviewProxy> getEntityRevisionsForReview(int responseId) throws Exception;

    EntityVersionReviewProxy getEntityRevisionForReview(UserOID userOID, int responseId) throws Exception;

    FormResponseBean getFormResponseBeanForSyncErrorReview(int entityVersionReviewPk) throws Exception;

    void removeEntityReviewEntry(int reviewEntityPk);
}
