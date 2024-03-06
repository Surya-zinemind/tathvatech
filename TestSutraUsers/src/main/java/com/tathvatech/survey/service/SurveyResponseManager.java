/*
 * Created on Dec 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TimeZone;

import com.tathvatech.common.common.ServiceLocator;
import com.tathvatech.common.utils.Sqls;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.dao.TestProcDAO;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.common.DummyWorkstation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Service
public class SurveyResponseManager
{
	private static final Logger logger = LoggerFactory.getLogger(SurveyResponseManager.class);
	
	private final PersistWrapper persistWrapper;

	private final DummyWorkstation dummyWorkstation;

    public SurveyResponseManager(PersistWrapper persistWrapper, DummyWorkstation dummyWorkstation) {
        this.persistWrapper = persistWrapper;
        this.dummyWorkstation = dummyWorkstation;
    }

    public  void changeResponseStatus(UserContext userContext, int responseId, String responseStatus)
			throws Exception
	{
		String sql = Sqls.changeResponseStatus;
		sql = sql.replaceAll("<tableName>", "TAB_RESPONSE");

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, responseStatus);
			stmt.setLong(2, responseId);

			stmt.execute();

		}
		catch (SQLException e)
		{
			logger.error("Error updating response status" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}
//Fix this while working on forms
	/*private static void getChildrenQuestions(SurveyItem aItem, List surveyQuestions)
	{
		surveyQuestions.add(aItem);
		if (aItem instanceof Container)
		{
			List chs = ((Container) aItem).getChildren();
			for (Iterator iterator = chs.iterator(); iterator.hasNext();)
			{
				SurveyItem aChild = (SurveyItem) iterator.next();
				getChildrenQuestions(aChild, surveyQuestions);
			}
		}
	}
    
	*/
	/**
	 * 
	 */
	/*public static SurveyResponse ceateDummyResponse(UserContext context, SurveyResponse surveyResponse) throws Exception
	{
		SurveyDefinition surveyDef = surveyResponse.getSurveyDefinition();
		String tableName = surveyDef.getSurveyConfig().getDbTable();

		int responseId = surveyResponse.getResponseId();

		Connection conn = null;
		PreparedStatement pStmt = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			if (responseId == 0)
			{
				// initializing the response. write into the main response Table
				Date timeStamp = surveyResponse.getResponseStartTime();
				String ipAddress = surveyResponse.getIpaddress();
				String rootSQL;
				User user = surveyResponse.getUser();
				rootSQL = Sqls.responseMasterInsertWithoutRespondent;
				pStmt = conn.prepareStatement(rootSQL);
				pStmt.setInt(1, surveyDef.getSurveyConfig().getPk());
				pStmt.setObject(2, new Timestamp(timeStamp.getTime()));
				pStmt.setString(3, ipAddress);

				pStmt.setInt(4, user.getPk());
				pStmt.setString(5, surveyResponse.getResponseRefNo());
				pStmt.setString(6, SurveyResponse.STATUS_INCOMPLETE);
				pStmt.setString(7, surveyResponse.getResponseMode());
				pStmt.setInt(8, surveyResponse.getTestProcPk());
				pStmt.setInt(9, 0);
				pStmt.setInt(10, 0);
				pStmt.setInt(11, 0);
				pStmt.setInt(12, 0);
				pStmt.setInt(13, 0);
				pStmt.setInt(14, 0);
				pStmt.setInt(15, 0);
				pStmt.setInt(16, 0);
				pStmt.setInt(17, 1);

				if (logger.isDebugEnabled())
				{
					logger.debug("Root sql - " + rootSQL);
				}

				pStmt.execute();

				ResultSet keySet = pStmt.getGeneratedKeys();
				if (keySet.next())
				{
					responseId = keySet.getInt(1);
				}
				else
				{
					throw new CreateException("Primary could not be set for the new Object");
				}

				pStmt.close();

				surveyResponse.setResponseId(responseId);

				if (logger.isDebugEnabled())
				{
					logger.debug("New responseId - " + responseId);
				}

			}


			// now set the lastShownItem in the response Master Table
			// String updateSQL = Sqls.responseMasterUpdatelastItem;
			// tableName);
			//
			// if (logger.isDebugEnabled())
			// {
			// logger.debug("Update sql - " + updateSQL);
			// }
			// pStmt = conn.prepareStatement(updateSQL);
			// pStmt.setString(1, surveyResponse.getLastSurveyItemId());
			// pStmt.setLong(2, responseId);
			// pStmt.execute();
			
			
			//new reload the surveyResponse 
			surveyResponse = SurveyResponseManager.getSurveyResponse(surveyDef, responseId);

			
			//and get the percent complete and number of comments at the form level and save
			FormResponseStats responseStat = SurveyResponseManager.getCommentCountAndPercentComplete(surveyDef.getQuestions(), surveyResponse); 

			
			pStmt = conn.prepareStatement("update TAB_RESPONSE set userPk=?, "
					+ "percentComplete = ?, "
					+ "noOfComments=?, "
					+ "totalQCount=?, "
					+ "totalACount=?, "
					+ "passCount=?, "
					+ "failCount=?, "
					+ "dimentionalFailCount=?, "
					+ "naCount=? "
					+ "where responseId=?");
			pStmt.setInt(1, context.getUser().getPk());
			pStmt.setInt(2, responseStat.getPercentComplete());
			pStmt.setInt(3, responseStat.getCommentsCount());
			pStmt.setInt(4, responseStat.getTotalQCount());
			pStmt.setInt(5, responseStat.getTotalACount());
			pStmt.setInt(6, responseStat.getPassCount());
			pStmt.setInt(7, responseStat.getFailCount());
			pStmt.setInt(8, responseStat.getDimentionalFailCount());
			pStmt.setInt(9, responseStat.getNaCount());
			pStmt.setLong(10, responseId);
			pStmt.execute();

			surveyResponse.setResponseStats(responseStat);
			
			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.
			
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, context.getUser().getPk(), surveyResponse.getResponseId(), 
					surveyResponse.getTestProcPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

			return surveyResponse;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error saving response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (pStmt != null)
				{
					pStmt.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}*/
	
	/**
     * This function  
     */
	/*public static SurveyResponse updateSurveyResponse(UserContext context, Project project, TestableEntity unit, SurveyDefinition surveyDef,
			int responseId, HashMap<SurveySaveItemBase,SurveyItemResponse> surveyItemResponseMap, List sectionsToSave) throws Exception
	{
		List allQuestionsToSave = new ArrayList();

		// get the questions from the sections to a linear list
		if(sectionsToSave != null)
		{
			for (Iterator iterator = sectionsToSave.iterator(); iterator.hasNext();)
			{
				SurveyItem aItem = (SurveyItem) iterator.next();
				getChildrenQuestions(aItem, allQuestionsToSave);
			}
		}
		saveSurveyItems(responseId, surveyDef, surveyItemResponseMap, allQuestionsToSave);

		//new load the surveyResponse 
		SurveyResponse surveyResponse = SurveyResponseManager.getSurveyResponse(surveyDef, responseId);
		TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());
	
		saveSectionResponses((User) context.getUser(), surveyResponse.getUser(), project, unit, 
				surveyResponse, sectionsToSave, true);
		
		//and get the percent complete and number of comments at the form level and save
		FormResponseStats responseStat = SurveyResponseManager.getCommentCountAndPercentComplete(surveyResponse.getSurveyDefinition().getQuestions(), surveyResponse); 

		ActivityLogQuery aLog = new ActivityLogQuery(context.getUser().getPk(), Actions.saveForm, 
				"Form Saved", new Date(), new Date(), project.getPk(), testProc.getPk(), unit.getPk(), testProc.getWorkstationPk(), 
				surveyResponse.getSurveyPk(), null, surveyResponse.getResponseId());
		
		aLog.setTotalQCount(responseStat.getTotalQCount());
		aLog.setTotalACount(responseStat.getTotalACount());
		aLog.setPassCount(responseStat.getPassCount());
		aLog.setFailCount(responseStat.getFailCount());
		aLog.setDimentionalFailCount(responseStat.getDimentionalFailCount());
		aLog.setNaCount(responseStat.getNaCount());
		aLog.setCommentsCount(responseStat.getCommentsCount());
		ActivityLoggingDelegate.logActivity(aLog);

		
		//new reload the surveyResponse 
		surveyResponse = SurveyResponseManager.getSurveyResponse(surveyResponse.getSurveyDefinition(), surveyResponse.getResponseId());
		
		
		//add the unit to favourites
		UnitManager.addUnitBookMark(context, testProc.getUnitPk(), testProc.getProjectPk(), UnitBookmark.BookmarkModeEnum.Auto);

		return surveyResponse;
	}
	
	*//**
     * Save all the questions with in the sections . 
     *//*
	public static SurveyResponse saveSectionResponses(UserContext context, Project project, TestableEntity unit, 
			SurveyResponse surveyResponse, List sectionsToSave) throws Exception
	{
		List allQuestionsToSave = new ArrayList();
		
		// get the questions from the sections to a linear list
		if(sectionsToSave != null)
		{
			for (Iterator iterator = sectionsToSave.iterator(); iterator.hasNext();)
			{
				SurveyItem aItem = (SurveyItem) iterator.next();
				getChildrenQuestions(aItem, allQuestionsToSave);
			}
		}
		return saveQuestionResponses((User)context.getUser(), (User)context.getUser(), project, unit, 
				surveyResponse, sectionsToSave, allQuestionsToSave);
	}
	
	*//**
     * save specific questions with in the sections, provide the list of questions to be saved.
     * the list should not be sections
     *//*
	public static SurveyResponse saveSpecificQuestionResponse(UserContext context, Project project, TestableEntity mItem, 
			SurveyResponse surveyResponse, List questions) throws Exception
	{
		List<String> lockedSectionIds = SurveyMaster.getLockedSectionIds((User) context.getUser(), 
				surveyResponse.getOID());
		
		List validQuestionsToSave = new ArrayList();
		// find the container sections
		if(questions != null && questions.size() > 0)
		{
			LinkedHashSet<Section> containerSections = new LinkedHashSet();
			SurveyDefinition surveyDef = surveyResponse.getSurveyDefinition();

			for (Iterator iterator = questions.iterator(); iterator.hasNext();)
			{
				SurveyItemBase aItem = (SurveyItem) iterator.next();
				aItem = surveyDef.getQuestion(aItem.getSurveyItemId());
				if(aItem == null || aItem instanceof Section)
				{
					continue;
				}
				if(aItem.getParent() instanceof Section)
				{
					if(lockedSectionIds.contains(((Section) aItem.getParent()).getSurveyItemId()))
					{
						validQuestionsToSave.add(aItem);
						containerSections.add((Section)aItem.getParent());
					}
					else
					{
						throw new AppException("Save failed, Items from sections that are not locked by the user cannot be saved", ErrorCode.SavingUnLockedSections);
					}
				}
				else if( aItem.getParent().getParent() instanceof Section)
				{
					if(lockedSectionIds.contains(((Section) (aItem.getParent().getParent())).getSurveyItemId()))
					{
						validQuestionsToSave.add(aItem);
						containerSections.add((Section) (aItem.getParent().getParent()));
					}
					else
					{
						throw new AppException("Save failed, Items from sections that are not locked by the user cannot be saved", ErrorCode.SavingUnLockedSections);
					}
				}
			}
			
			//save
			return saveQuestionResponses((User)context.getUser(), (User)context.getUser(), project, mItem, 
					surveyResponse, containerSections, validQuestionsToSave);
		}
		
		//nothing to do here
		return surveyResponse;
	}
	
	*//**
     * allQuestionstoSave will be saved.. the sectionsToRecalc should be the list of sections which hold any the questions to save.
     * when ever a question is saved, the section level stats need to be recalculated. I dont want to find the sections from the questionList
     * because, in some cases, the sectionlist is already supplied by the client. so why to re-calculate it.
     * 
     * recordSaveAsUser is the tester who saved the form in most cases, But if the verifier or approver editing a response, the performed by user should not change,
     * but the activity log can note that the action was performed by the verifier or approver. 
     * 
     *//*
	private static SurveyResponse saveQuestionResponses(User actionPerformedByUser, User recordSaveAsUser, Project project, TestableEntity mItem, 
			SurveyResponse surveyResponse, Collection sectionsToReCalc, List allQuestionsToSave) throws Exception
	{
		
		SurveyDefinition surveyDef = surveyResponse.getSurveyDefinition();
		Survey survey = surveyDef.getSurveyConfig();
		TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());

		int responseId = surveyResponse.getResponseId();

		Connection conn = null;
		PreparedStatement pStmt = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();


			for (Iterator iter = allQuestionsToSave.iterator(); iter.hasNext();)
			{
				SurveyItem item = (SurveyItem) iter.next();
				if (!(item instanceof SurveySaveItem))
				{
					continue;
				}

				SurveySaveItem sItem = (SurveySaveItem) item;
				SurveyItemResponse answer = surveyResponse.getAnswer(sItem);

				if (answer != null && answer.getResponseUnits().size() > 0)
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("Answer found for - " + sItem.getIdentifier());
					}

					AnswerPersistor ap = sItem.getPersistor(answer);

					// delete the response to question if exists
					String[] deleteSql = ap.getDeleteResponseSQL(responseId);
					if (logger.isDebugEnabled())
					{
						logger.debug("Delete SQL - " + deleteSql);
					}
					stmt = conn.createStatement();
					for (int i = 0; i < deleteSql.length; i++)
					{
						stmt.execute(deleteSql[i]);
					}


					// now save the answer
					ap.persistAnswer(conn, responseId);
				}
				else
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("No answer found for - " + sItem.getIdentifier());
					}

					AnswerPersistor ap = sItem.getPersistor(answer);

					// delete the response to question if exists
					String[] deleteSql = ap.getDeleteResponseSQL(responseId);
					if (logger.isDebugEnabled())
					{
						logger.debug("Delete SQL - " + deleteSql);
					}
					stmt = conn.createStatement();
					for (int i = 0; i < deleteSql.length; i++)
					{
						stmt.execute(deleteSql[i]);
					}
				}
				
				// let is update the lastUpdated date on the FormItemResponse entity for this. we can base the answerdate on this
				FormItemResponse itemResp = getFormItemResponse(responseId, sItem.getSurveyItemId(), true);
				itemResp.setLastUpdated(new Date());
				persistWrapper.update(itemResp);
			}

			// now set the lastShownItem in the response Master Table
			// String updateSQL = Sqls.responseMasterUpdatelastItem;
			// tableName);
			//
			// if (logger.isDebugEnabled())
			// {
			// logger.debug("Update sql - " + updateSQL);
			// }
			// pStmt = conn.prepareStatement(updateSQL);
			// pStmt.setString(1, surveyResponse.getLastSurveyItemId());
			// pStmt.setLong(2, responseId);
			// pStmt.execute();
			
			
			//new reload the surveyResponse 
			surveyResponse = SurveyResponseManager.getSurveyResponse(surveyDef, responseId);

			//save the SectionResponses for the sections saved in this submit
			if(sectionsToReCalc != null)
			{
				for (Iterator iterator = sectionsToReCalc.iterator(); iterator.hasNext();)
				{
					Section aSection = (Section) iterator.next();
					
					FormSection formSection = new FormDBManager().getFormSection(aSection.getSurveyItemId(), surveyResponse.getSurveyPk());
					
					FormResponseStats responseStat = SurveyResponseManager.getCommentCountAndPercentComplete(aSection.getChildren(), surveyResponse); 
					
					SectionResponse sectionResponse = persistWrapper.read(SectionResponse.class, 
							"select * from TAB_SECTION_RESPONSE where responseId = ? and sectionId = ?", 
							surveyResponse.getResponseId(), aSection.getSurveyItemId());
					
//					persistWrapper.delete("delete from TAB_SECTION_RESPONSE where responseId = ? and sectionId = ?", 
//							surveyResponse.getResponseId(), aSection.getSurveyItemId());
					if(sectionResponse != null)
					{
						//update existing
						sectionResponse.setSubmittedBy(recordSaveAsUser.getPk());
						//start date should not be reset when updating as existing one as it is the date of first save of the section
						if(sectionResponse.getPercentComplete() != 100 && responseStat.getPercentComplete() == 100)
						{
							sectionResponse.setCompletionDate(new Date());
						}
						sectionResponse.setPercentComplete(responseStat.getPercentComplete());
						sectionResponse.setNoOfComments(responseStat.getCommentsCount());
						sectionResponse.setTotalQCount(responseStat.getTotalQCount());
						sectionResponse.setTotalACount(responseStat.getTotalACount());
						sectionResponse.setPassCount(responseStat.getPassCount());
						sectionResponse.setFailCount(responseStat.getFailCount());
						sectionResponse.setDimentionalFailCount(responseStat.getDimentionalFailCount());
						sectionResponse.setNaCount(responseStat.getNaCount());
						persistWrapper.update(sectionResponse);
						
						ActivityLogQuery aLog = new ActivityLogQuery(actionPerformedByUser.getPk(), Actions.saveSection, 
								"Section Saved", new Date(), new Date(), project.getPk(), testProc.getPk(), mItem.getPk(), testProc.getWorkstationPk(), 
								surveyResponse.getSurveyPk(), sectionResponse.getSectionId(), surveyResponse.getResponseId());
						
						aLog.setTotalQCount(responseStat.getTotalQCount());
						aLog.setTotalACount(responseStat.getTotalACount());
						aLog.setPassCount(responseStat.getPassCount());
						aLog.setFailCount(responseStat.getFailCount());
						aLog.setDimentionalFailCount(responseStat.getDimentionalFailCount());
						aLog.setNaCount(responseStat.getNaCount());
						aLog.setCommentsCount(responseStat.getCommentsCount());
						ActivityLoggingDelegate.logActivity(aLog);
					}
					else
					{
						//create new
						sectionResponse = new SectionResponse();
						sectionResponse.setResponseId(surveyResponse.getResponseId());
						sectionResponse.setFormSectionFk(formSection.getPk());
						sectionResponse.setSectionId(aSection.getSurveyItemId());
						sectionResponse.setName(aSection.getQuestionText());
						sectionResponse.setDescription(aSection.getDescription());
						sectionResponse.setSubmittedBy(recordSaveAsUser.getPk());
						sectionResponse.setStartDate(new Date());
						sectionResponse.setPercentComplete(responseStat.getPercentComplete());
						sectionResponse.setNoOfComments(responseStat.getCommentsCount());
						sectionResponse.setTotalQCount(responseStat.getTotalQCount());
						sectionResponse.setTotalACount(responseStat.getTotalACount());
						sectionResponse.setPassCount(responseStat.getPassCount());
						sectionResponse.setFailCount(responseStat.getFailCount());
						sectionResponse.setDimentionalFailCount(responseStat.getDimentionalFailCount());
						sectionResponse.setNaCount(responseStat.getNaCount());
						if(responseStat.getPercentComplete() == 100)
						{
							sectionResponse.setCompletionDate(new Date());
						}
						persistWrapper.createEntity(sectionResponse);

						ActivityLogQuery aLog = new ActivityLogQuery(actionPerformedByUser.getPk(), Actions.saveSection, 
								"Section Saved", new Date(), new Date(), project.getPk(), testProc.getPk(), mItem.getPk(), testProc.getWorkstationPk(), 
								surveyResponse.getSurveyPk(), sectionResponse.getSectionId(), surveyResponse.getResponseId());
						
						aLog.setTotalQCount(responseStat.getTotalQCount());
						aLog.setTotalACount(responseStat.getTotalACount());
						aLog.setPassCount(responseStat.getPassCount());
						aLog.setFailCount(responseStat.getFailCount());
						aLog.setDimentionalFailCount(responseStat.getDimentionalFailCount());
						aLog.setNaCount(responseStat.getNaCount());
						aLog.setCommentsCount(responseStat.getCommentsCount());
						ActivityLoggingDelegate.logActivity(aLog);
					}
					
				}
			}			
			
			//and get the percent complete and number of comments at the form level and save
			FormResponseStats responseStat = SurveyResponseManager.getCommentCountAndPercentComplete(surveyDef.getQuestions(), surveyResponse); 

			pStmt = conn.prepareStatement("update TAB_RESPONSE set userPk=?, "
					+ "percentComplete = ?, "
					+ "noOfComments=?, "
					+ "totalQCount=?, "
					+ "totalACount=?, "
					+ "passCount=?, "
					+ "failCount=?, "
					+ "dimentionalFailCount=?, "
					+ "naCount=? "
					+ "where responseId=?");
			pStmt.setInt(1, recordSaveAsUser.getPk());
			pStmt.setInt(2, responseStat.getPercentComplete());
			pStmt.setInt(3, responseStat.getCommentsCount());
			pStmt.setInt(4, responseStat.getTotalQCount());
			pStmt.setInt(5, responseStat.getTotalACount());
			pStmt.setInt(6, responseStat.getPassCount());
			pStmt.setInt(7, responseStat.getFailCount());
			pStmt.setInt(8, responseStat.getDimentionalFailCount());
			pStmt.setInt(9, responseStat.getNaCount());
			pStmt.setLong(10, responseId);
			pStmt.execute();
			

			ActivityLogQuery aLog = new ActivityLogQuery(actionPerformedByUser.getPk(), Actions.saveForm, 
					"Form Saved", new Date(), new Date(), project.getPk(), testProc.getPk(), mItem.getPk(), testProc.getWorkstationPk(), 
					surveyResponse.getSurveyPk(), null, surveyResponse.getResponseId());
			
			aLog.setTotalQCount(responseStat.getTotalQCount());
			aLog.setTotalACount(responseStat.getTotalACount());
			aLog.setPassCount(responseStat.getPassCount());
			aLog.setFailCount(responseStat.getFailCount());
			aLog.setDimentionalFailCount(responseStat.getDimentionalFailCount());
			aLog.setNaCount(responseStat.getNaCount());
			aLog.setCommentsCount(responseStat.getCommentsCount());
			ActivityLoggingDelegate.logActivity(aLog);

			
			surveyResponse.setResponseStats(responseStat);
			
			//add the unit to favourites
			UserContextImpl context = new UserContextImpl(TimeZone.getTimeZone(actionPerformedByUser.getTimezone()));
			context.setUser(actionPerformedByUser);
			Site site= SiteDelegate.getSite(actionPerformedByUser.getSitePk());
			context.setSite(site);
			UnitManager.addUnitBookMark(context, testProc.getUnitPk(), testProc.getProjectPk(), UnitBookmark.BookmarkModeEnum.Auto);

			return surveyResponse;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error saving response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (pStmt != null)
				{
					pStmt.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	private static void saveSurveyItems(int responseId, SurveyDefinition surveyDef, HashMap<SurveySaveItemBase, SurveyItemResponse> surveyItemResponseMap, List allQuestionsToSave) throws Exception
	{
		String tableName = surveyDef.getSurveyConfig().getDbTable();

		if (responseId == 0)
		{
			throw new Exception("Invalid responseId, cannot save items");
		}

		Connection conn = null;
		PreparedStatement pStmt = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			// now add the survey parameters into the pageQuestions list
			// that way they will also get persisted.
			List surveyParams = surveyDef.getSurveyParams();
			allQuestionsToSave.addAll(0, surveyParams);

			for (Iterator iter = allQuestionsToSave.iterator(); iter.hasNext();)
			{
				SurveyItem item = (SurveyItem) iter.next();
				if (!(item instanceof SurveySaveItem))
				{
					continue;
				}

				SurveySaveItem sItem = (SurveySaveItem) item;
				SurveyItemResponse answer = surveyItemResponseMap.get(sItem);

				if (answer != null && answer.getResponseUnits().size() > 0)
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("Answer found for - " + sItem.getIdentifier());
					}

					AnswerPersistor ap = sItem.getPersistor(answer);

					// delete the response to question if exists
					String[] deleteSql = ap.getDeleteResponseSQL(responseId);
					if (logger.isDebugEnabled())
					{
						logger.debug("Delete SQL - " + deleteSql);
					}
					stmt = conn.createStatement();
					for (int i = 0; i < deleteSql.length; i++)
					{
						stmt.execute(deleteSql[i]);
					}


					// now save the answer
					ap.persistAnswer(conn, responseId);
				}
				else
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("No answer found for - " + sItem.getIdentifier());
					}

					AnswerPersistor ap = sItem.getPersistor(answer);

					// delete the response to question if exists
					String[] deleteSql = ap.getDeleteResponseSQL(responseId);
					if (logger.isDebugEnabled())
					{
						logger.debug("Delete SQL - " + deleteSql);
					}
					stmt = conn.createStatement();
					for (int i = 0; i < deleteSql.length; i++)
					{
						stmt.execute(deleteSql[i]);
					}
				}
				
				
				// let is update the lastUpdated date on the FormItemResponse entity for this. we can base the answerdate on this
				FormItemResponse itemResp = getFormItemResponse(responseId, sItem.getSurveyItemId(), true);
				itemResp.setLastUpdated(new Date());
				persistWrapper.update(itemResp);
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error saving response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (pStmt != null)
				{
					pStmt.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	private static void saveSectionResponses(User actionPerformedByUser, User recordSaveAsUser, Project project, TestableEntity unit, 
			SurveyResponse surveyResponse, List sectionsToSave, boolean dontChangeSubmittedUserAndCompletionDate)throws Exception
	{
		//save the SectionResponses for the sections saved in this submit
		if(sectionsToSave != null)
		{
			TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());
			for (Iterator iterator = sectionsToSave.iterator(); iterator.hasNext();)
			{
				Section aSection = (Section) iterator.next();
				FormSection formSection = new FormDBManager().getFormSection(aSection.getSurveyItemId(), surveyResponse.getSurveyPk());

				FormResponseStats responseStat = SurveyResponseManager.getCommentCountAndPercentComplete(aSection.getChildren(), surveyResponse); 
				
				SectionResponse sectionResponse = persistWrapper.read(SectionResponse.class, 
						"select * from TAB_SECTION_RESPONSE where responseId = ? and sectionId = ?", 
						surveyResponse.getResponseId(), aSection.getSurveyItemId());
				
//				persistWrapper.delete("delete from TAB_SECTION_RESPONSE where responseId = ? and sectionId = ?", 
//						surveyResponse.getResponseId(), aSection.getSurveyItemId());
				if(sectionResponse != null)
				{
					//update existing
					if(dontChangeSubmittedUserAndCompletionDate == false)
					{
						sectionResponse.setSubmittedBy(recordSaveAsUser.getPk());

						if(sectionResponse.getPercentComplete() != 100 && responseStat.getPercentComplete() == 100)
						{
							sectionResponse.setCompletionDate(new Date());
						}
					}
					sectionResponse.setPercentComplete(responseStat.getPercentComplete());
					sectionResponse.setNoOfComments(responseStat.getCommentsCount());
					sectionResponse.setTotalQCount(responseStat.getTotalQCount());
					sectionResponse.setTotalACount(responseStat.getTotalACount());
					sectionResponse.setPassCount(responseStat.getPassCount());
					sectionResponse.setFailCount(responseStat.getFailCount());
					sectionResponse.setDimentionalFailCount(responseStat.getDimentionalFailCount());
					sectionResponse.setNaCount(responseStat.getNaCount());

					//start date should not be reset when updating as existing one as it is the date of first save of the section
					persistWrapper.update(sectionResponse);
					
					ActivityLogQuery aLog = new ActivityLogQuery(actionPerformedByUser.getPk(), Actions.saveSection, 
							"Section Saved", new Date(), new Date(), project.getPk(), testProc.getPk(), unit.getPk(), testProc.getWorkstationPk(), 
							surveyResponse.getSurveyPk(), sectionResponse.getSectionId(), surveyResponse.getResponseId());
					
					aLog.setTotalQCount(responseStat.getTotalQCount());
					aLog.setTotalACount(responseStat.getTotalACount());
					aLog.setPassCount(responseStat.getPassCount());
					aLog.setFailCount(responseStat.getFailCount());
					aLog.setDimentionalFailCount(responseStat.getDimentionalFailCount());
					aLog.setNaCount(responseStat.getNaCount());
					aLog.setCommentsCount(responseStat.getCommentsCount());
					ActivityLoggingDelegate.logActivity(aLog);
				}
				else
				{
					//create new
					sectionResponse = new SectionResponse();
					sectionResponse.setResponseId(surveyResponse.getResponseId());
					sectionResponse.setFormSectionFk(formSection.getPk());
					sectionResponse.setSectionId(aSection.getSurveyItemId());
					sectionResponse.setName(aSection.getQuestionText());
					sectionResponse.setDescription(aSection.getDescription());
					sectionResponse.setSubmittedBy(recordSaveAsUser.getPk());
					sectionResponse.setStartDate(new Date());
					sectionResponse.setPercentComplete(responseStat.getPercentComplete());
					sectionResponse.setNoOfComments(responseStat.getCommentsCount());
					sectionResponse.setTotalQCount(responseStat.getTotalQCount());
					sectionResponse.setTotalACount(responseStat.getTotalACount());
					sectionResponse.setPassCount(responseStat.getPassCount());
					sectionResponse.setFailCount(responseStat.getFailCount());
					sectionResponse.setDimentionalFailCount(responseStat.getDimentionalFailCount());
					sectionResponse.setNaCount(responseStat.getNaCount());
					if(responseStat.getPercentComplete() == 100)
					{
						sectionResponse.setCompletionDate(new Date());
					}
					persistWrapper.createEntity(sectionResponse);

					ActivityLogQuery aLog = new ActivityLogQuery(actionPerformedByUser.getPk(), Actions.saveSection, 
							"Section Saved", new Date(), new Date(), project.getPk(), testProc.getPk(), unit.getPk(), testProc.getWorkstationPk(), 
							surveyResponse.getSurveyPk(), sectionResponse.getSectionId(), surveyResponse.getResponseId());
					
					aLog.setTotalQCount(responseStat.getTotalQCount());
					aLog.setTotalACount(responseStat.getTotalACount());
					aLog.setPassCount(responseStat.getPassCount());
					aLog.setFailCount(responseStat.getFailCount());
					aLog.setDimentionalFailCount(responseStat.getDimentionalFailCount());
					aLog.setNaCount(responseStat.getNaCount());
					aLog.setCommentsCount(responseStat.getCommentsCount());
					ActivityLoggingDelegate.logActivity(aLog);
				}
				
			}
		}			
	}
	
	private static void setResponseStats(FormResponseStats responseStat, User actionPerformedByUser, User recordSaveAsUser, 
			SurveyDefinition surveyDef, SurveyResponse surveyResponse)throws Exception
	{
		int responseId = surveyResponse.getResponseId();
		if (responseId == 0)
		{
			throw new Exception("Invalid responseId, cannot save items");
		}

		Connection conn = null;
		PreparedStatement pStmt = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
	
			pStmt = conn.prepareStatement("update TAB_RESPONSE set userPk=?, "
					+ "percentComplete = ?, "
					+ "noOfComments=?, "
					+ "totalQCount=?, "
					+ "totalACount=?, "
					+ "passCount=?, "
					+ "failCount=?, "
					+ "dimentionalFailCount=?, "
					+ "naCount=? "
					+ "where responseId=?");
			pStmt.setInt(1, recordSaveAsUser.getPk());
			pStmt.setInt(2, responseStat.getPercentComplete());
			pStmt.setInt(3, responseStat.getCommentsCount());
			pStmt.setInt(4, responseStat.getTotalQCount());
			pStmt.setInt(5, responseStat.getTotalACount());
			pStmt.setInt(6, responseStat.getPassCount());
			pStmt.setInt(7, responseStat.getFailCount());
			pStmt.setInt(8, responseStat.getDimentionalFailCount());
			pStmt.setInt(9, responseStat.getNaCount());
			pStmt.setLong(10, responseId);
			pStmt.execute();
			
			surveyResponse.setResponseStats(responseStat);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error saving response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (pStmt != null)
				{
					pStmt.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		
	}
	
	public static void finalizeSurveyResponse(UserContext userContext, SurveyDefinition surveyDef, int responseId)
			throws Exception
	{
		finalizeSurveyResponseImpl(userContext, surveyDef, responseId, new Date());
		
		saveResponseStateAsSubmitRecord(userContext, responseId, SubmissionTypeEnum.Final);
	}

	public static void finalizeSurveyResponse(UserContext userContext, SurveyDefinition surveyDef, int responseId,
			Date responseCompleteTime)throws Exception
	{
		finalizeSurveyResponseImpl(userContext, surveyDef, responseId, responseCompleteTime);
		
		saveResponseStateAsSubmitRecord(userContext, responseId, SubmissionTypeEnum.Final);
	}
	*//**
	 * @param sd
	 * @param responseId
	 *//*
	private static void finalizeSurveyResponseImpl(UserContext userContext, SurveyDefinition surveyDef, int responseId,
			Date responseCompleteTime) throws Exception
	{
		SurveyResponse sResponse = SurveyResponseManager.getSurveyResponse(surveyDef, responseId);

		if (logger.isDebugEnabled())
		{
			logger.debug("SurveyDefinition - " + surveyDef);
			logger.debug("responseId - " + responseId);
		}
		String sql = Sqls.finalizeResponse;
		sql = sql.replaceAll("<tableName>", surveyDef.getSurveyConfig().getDbTable());
		if (logger.isDebugEnabled())
		{
			logger.debug("Sql - " + sql);
		}

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, SurveyResponse.STATUS_COMPLETE);
			stmt.setTimestamp(2, new Timestamp(responseCompleteTime.getTime()));
			stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			stmt.setLong(4, userContext.getUser().getPk());
			stmt.setLong(5, responseId);

			stmt.execute();

			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_SUBMIT, userContext.getUser().getPk(), sResponse.getResponseId(), 
					sResponse.getTestProcPk(), ResponseMasterNew.STATUS_COMPLETE, null);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error finalizing response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}

		//add the unit to favourites
		TestProcObj testProc = TestProcManager.getTestProc(sResponse.getTestProcPk());
		UnitManager.addUnitBookMark(userContext, testProc.getUnitPk(), testProc.getProjectPk(), UnitBookmark.BookmarkModeEnum.Auto);
	}

	public static void markResponseAsOld(UserContext context, ResponseMasterNew response)throws Exception
	{
		String sql = Sqls.markResponseAsOldOrCurrent;
		sql = sql.replaceAll("<tableName>", "TAB_RESPONSE");

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 0);
			stmt.setLong(2, response.getResponseId());

			stmt.execute();
		}
		catch (SQLException e)
		{
			logger.error("Error marking response as old responseId:" + response.getResponseId(), e);
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	
	*//**
	 * saves the current state of the response as a JSON adds it as an attachment to the testproc.
	 * @param context
	 * @param surveyResponse
	 * @return
	 * @throws Exception
	 *//*
    public static void saveResponseStateAsSubmitRecord(UserContext context,
    	    int responseId, ResponseSubmissionBookmark.SubmissionTypeEnum submissionType) throws Exception
    {
		ResponseMasterNew respM = getResponseMaster(responseId);
		TestProcObj tp = new TestProcManager().getTestProc(respM.getTestProcPk());
		Boolean saveResponseState = (Boolean) new CommonServicesDelegate().getEntityPropertyValue(new ProjectOID(tp.getProjectPk()), ProjectPropertyEnum.SaveResponseStateOnFormSubmit.getId(), Boolean.class);
		if(saveResponseState == null || saveResponseState == false)
			return;
		
		
    	SurveyResponse sResponse = getSurveyResponse(responseId);
    	List<SectionResponseQuery> sectionResponses = getSectionResponseSummary(sResponse.getSurveyDefinition(), responseId);
    	
    			
	    //create the ResponseSubmissionHistoryRecord
		String revisionNo = null;
		
	    //get the FormResponseBean and then save this as a backup to save the inetrim/final submitted response state.
		Boolean createRevisionNoOnFormSubmit = (Boolean) new CommonServicesDelegate().getEntityPropertyValue(new ProjectOID(tp.getProjectPk()), ProjectPropertyEnum.CreateRevisionNoForFormSubmit.getId(), Boolean.class);
		if(createRevisionNoOnFormSubmit != null && true == createRevisionNoOnFormSubmit)
		{
			//The sequence order will be like below
			*//*
			 * 1.01 interim
			 * 1.02 interim
			 * 1.03 interim
			 * 1 final
			 * 2.01 interim
			 * 2.02 interim
			 * 2 final
			 *//*
			if(ResponseSubmissionBookmark.SubmissionTypeEnum.Final == submissionType)
			{
				int finalSeq = SequenceIdGenerator.getNextSequence(EntityTypeEnum.ResponseSubmissionBookmark.name(), ""+respM.getTestProcPk(), ResponseSubmissionBookmark.SubmissionTypeEnum.Final.name(), null, null);
				revisionNo = finalSeq+"";
			}
			else if(ResponseSubmissionBookmark.SubmissionTypeEnum.Interim == submissionType)
			{
				Integer currentMainSeq = SequenceIdGenerator.getCurrentSequence(EntityTypeEnum.ResponseSubmissionBookmark.name(), ""+respM.getTestProcPk(), ResponseSubmissionBookmark.SubmissionTypeEnum.Final.name(), null, null);
				if(currentMainSeq == null)
				{
					currentMainSeq = 1; //current main seq starts with 1.
				}
				else
				{
					currentMainSeq++;
				}
				int subSeq = SequenceIdGenerator.getNextSequence(EntityTypeEnum.ResponseSubmissionBookmark.name(), ""+respM.getTestProcPk(), ResponseSubmissionBookmark.SubmissionTypeEnum.Interim.name(), currentMainSeq+"", null);
				String subSeqStr = null;
				if(subSeq < 10)
					subSeqStr = "0"+subSeq;
				else
					subSeqStr = ""+subSeq;
				
				revisionNo = currentMainSeq + "." +  subSeqStr;
			}
		}

		
		
		//prepare the form response serialize bean with all values to save.
		FormResponseStateSerializeBean serializeBean = new FormResponseStateSerializeBean();
		serializeBean.setResponseMaster(respM);
		serializeBean.setSectionResponseList(sectionResponses);
		if(sResponse != null)
		{
			serializeBean.setTestProcPk(sResponse.getTestProcPk());
			serializeBean.setResponseId(sResponse.getResponseId());
			serializeBean.setSurveyPk(sResponse.getSurveyPk());
			serializeBean.setUser(sResponse.getUser());
			serializeBean.setUserPk(sResponse.getUserPk());
			serializeBean.setResponseMode(sResponse.getResponseMode());
			serializeBean.setResponseStartTime(sResponse.getResponseStartTime());
			serializeBean.setResponseCompleteTime(sResponse.getResponseCompleteTime());
			serializeBean.setResponseSyncTime(sResponse.getResponseSyncTime());
			serializeBean.setFlagPk(sResponse.getFlagPk());
			serializeBean.setIpaddress(sResponse.getIpaddress());
			serializeBean.setResponseRefNo(sResponse.getResponseRefNo());
			serializeBean.setResponseStats(sResponse.getResponseStats());
			serializeBean.setStatus(sResponse.getStatus());
			serializeBean.setLastUpdated(sResponse.getLastUpdated());
			serializeBean.setFlag(sResponse.getFlag());
			serializeBean.setVerifiedBy(sResponse.getVerifiedBy());
			serializeBean.setApprovedBy(sResponse.getApprovedBy());

			
			HashMap<String, SimpleSurveyItemResponse> answers = new HashMap<>();
			for (Iterator iterator = sResponse.getSurveyItemAnswerMap().keySet().iterator(); iterator.hasNext();)
			{
				SurveySaveItemBase aSItem = (SurveySaveItemBase) iterator.next();
				SimpleSurveyItemResponse itemResp = (SimpleSurveyItemResponse) sResponse.getSurveyItemAnswerMap().get(aSItem);
				answers.put(aSItem.getSurveyItemId(), itemResp);
			}
			serializeBean.setAnswerMap(answers);
		}
		
		
		//now save this serialized response state into the database.
		ObjectMapper jm = new ObjectMapper();
        String objRep = jm.writeValueAsString(serializeBean);

        Date now = new Date();
		ResponseSubmissionBookmark respSubmission = new ResponseSubmissionBookmark();
		respSubmission.setCreatedBy(context.getUser().getPk());
		respSubmission.setCreatedDate(now);
		respSubmission.setTestProcFk(respM.getTestProcPk());
		respSubmission.setResponseId(respM.getResponseId());
		respSubmission.setRevisionNo(revisionNo);
		respSubmission.setSubmissionType(submissionType.name());
		respSubmission.setResponseJSON(objRep);
		respSubmission.setEstatus(EStatusEnum.Active.getValue());
		int respSubmissionPk = persistWrapper.createEntity(respSubmission);
	}    
	
	
    public static List<ResponseSubmissionBookmark> getResponseSubmissionBookmarks(TestProcOID testprocOID)
    {
    	return persistWrapper.readList(ResponseSubmissionBookmark.class, 
    			"select * from RESPONSE_SUBMISSION_BOOKMARK where testProcFk = ? order by createdDate", testprocOID.getPk());
    }

    public static ResponseSubmissionBookmark getLastResponseSubmissionBookmark(TestProcOID testprocOID)
    {
    	return persistWrapper.read(ResponseSubmissionBookmark.class, 
    			"select * from RESPONSE_SUBMISSION_BOOKMARK where testProcFk = ? order by createdDate desc limit 0, 1", testprocOID.getPk());
    }
    *//**
     * 
     *//*
	public static SurveyResponse copyResponse(UserContext context, int responseIdToCopy) throws Exception
	{
		SurveyResponse surveyResponse = getSurveyResponse(responseIdToCopy);
		SurveyDefinition surveyDef = surveyResponse.getSurveyDefinition();
		String tableName = surveyDef.getSurveyConfig().getDbTable();
		int oldResponseId = surveyResponse.getResponseId();

		int responseId = 0; // new responseId

		List allQuestionsToSave = surveyDef.getQuestionsLinear();

		Connection conn = null;
		PreparedStatement pStmt = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			// initializing the response. write into the main response Table
			Date timeStamp = surveyResponse.getResponseStartTime();
			String ipAddress = surveyResponse.getIpaddress();
			String rootSQL;
			User user = surveyResponse.getUser();
			rootSQL = Sqls.responseMasterInsertWithoutRespondent;
			pStmt = conn.prepareStatement(rootSQL);
			pStmt.setInt(1, surveyDef.getSurveyConfig().getPk());
			pStmt.setObject(2, new Timestamp(timeStamp.getTime()));
			pStmt.setString(3, ipAddress);

			pStmt.setInt(4, user.getPk());
			pStmt.setString(5, surveyResponse.getResponseRefNo());
			pStmt.setString(6, SurveyResponse.STATUS_INCOMPLETE);
			pStmt.setString(7, surveyResponse.getResponseMode());
			pStmt.setInt(8, surveyResponse.getTestProcPk());
			pStmt.setInt(9, surveyResponse.getResponseStats().getPercentComplete());
			pStmt.setInt(10, surveyResponse.getResponseStats().getCommentsCount());
			pStmt.setInt(11, surveyResponse.getResponseStats().getTotalQCount());
			pStmt.setInt(12, surveyResponse.getResponseStats().getTotalACount());
			pStmt.setInt(13, surveyResponse.getResponseStats().getPassCount());
			pStmt.setInt(14, surveyResponse.getResponseStats().getFailCount());
			pStmt.setInt(15, surveyResponse.getResponseStats().getDimentionalFailCount());
			pStmt.setInt(16, surveyResponse.getResponseStats().getNaCount());
			pStmt.setInt(17, 1);

			if (logger.isDebugEnabled())
			{
				logger.debug("Root sql - " + rootSQL);
			}

			pStmt.execute();

			ResultSet keySet = pStmt.getGeneratedKeys();
			if (keySet.next())
			{
				responseId = keySet.getInt(1);
			}
			else
			{
				throw new CreateException("Unexpected error, could not create response");
			}
			pStmt.close();

			surveyResponse.setResponseId(responseId);

			if (logger.isDebugEnabled())
			{
				logger.debug("New responseId - " + responseId);
			}

			// now add the survey parameters into the pageQuestions list
			// that way they will also get persisted.
			List surveyParams = surveyDef.getSurveyParams();
			allQuestionsToSave.addAll(0, surveyParams);

			for (Iterator iter = allQuestionsToSave.iterator(); iter.hasNext();)
			{
				SurveyItem item = (SurveyItem) iter.next();
				if (!(item instanceof SurveySaveItem))
				{
					continue;
				}

				SurveySaveItem sItem = (SurveySaveItem) item;
				SurveyItemResponse answer = surveyResponse.getAnswer(sItem);
				if (answer != null && answer.getResponseUnits().size() > 0)
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("Answer found for - " + sItem.getIdentifier());
					}

					AnswerPersistor ap = sItem.getPersistor(answer);

					// delete the response to question if exists
					String[] deleteSql = ap.getDeleteResponseSQL(responseId);
					if (logger.isDebugEnabled())
					{
						logger.debug("Delete SQL - " + deleteSql);
					}
					stmt = conn.createStatement();
					for (int i = 0; i < deleteSql.length; i++)
					{
						stmt.execute(deleteSql[i]);
					}

					// now save the answer
					ap.persistAnswer(conn, responseId);
				}
				else
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("No answer found for - " + sItem.getIdentifier());
					}

					AnswerPersistor ap = sItem.getPersistor(answer);

					// delete the response to question if exists
					String[] deleteSql = ap.getDeleteResponseSQL(responseId);
					if (logger.isDebugEnabled())
					{
						logger.debug("Delete SQL - " + deleteSql);
					}
					stmt = conn.createStatement();
					for (int i = 0; i < deleteSql.length; i++)
					{
						stmt.execute(deleteSql[i]);
					}
				}
			}

			//new reload the surveyResponse 
			surveyResponse = SurveyResponseManager.getSurveyResponse(surveyDef, responseId);

			
			//copy the SectionResponses to the new Response
			List <SectionResponse> srl= persistWrapper.readList(SectionResponse.class, 
					"select * from TAB_SECTION_RESPONSE where responseId = ?", oldResponseId);
			for (Iterator iterator = srl.iterator(); iterator.hasNext();)
			{
				SectionResponse sectionResponse = (SectionResponse) iterator.next();
				sectionResponse.setResponseId(responseId); // the new responseId
				persistWrapper.createEntity(sectionResponse);
			}
			
			
			if(true)
			{
				//load the current itemresponselist into memory.
				//now update the ones in the database with the new responseId
				//now insert the ones in memory into the database with no changes so that we have the relation with the old id maintained. 
				List<FormItemResponse> currentItemRespList = persistWrapper.readList(FormItemResponse.class, 
						"select * from TAB_ITEM_RESPONSE where responseId = ?", oldResponseId);

				persistWrapper.executeUpdate("update TAB_ITEM_RESPONSE set responseId = ? where responseId = ?", responseId, oldResponseId);

				for (Iterator iterator = currentItemRespList.iterator(); iterator.hasNext();)
				{
					FormItemResponse formItemResponse = (FormItemResponse) iterator.next();
					formItemResponse.setPk(0);
					persistWrapper.createEntity(formItemResponse);
				}
			}
			//the if (false) below was the old code, we were losing the history against the old response. so I have introduced the new block
			// in if(true) above. 
			// all this is changing when we introduce the user level tracking of changes against the line item response.
			if(false)
			{
				// TODO:: hack hack. here since the responseId changes, the FormItemResponse reference will be lost since the
				// responseId is changed.. we should actually have the copy archieved and the current one continued.. but that need to be tested properly.
				// so for now I am changing the responseId to the new one.
				persistWrapper.executeUpdate("update TAB_ITEM_RESPONSE set responseId = ? where responseId = ?", responseId, oldResponseId);
			}
			
			

			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.

		///**** this should be the responsibility of the caller.. else we create these records unnecessarly.	
			
//			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, context.getUser().getPk(), surveyResponse.getResponseId(), 
//					surveyResponse.getUnitPk(), surveyResponse.getWorkstationPk(), 
//					surveyDef.getSurveyConfig().getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

			return surveyResponse;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error saving response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (pStmt != null)
				{
					pStmt.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}
*/
	/*public static SurveyResponse getSurveyResponse(int responseId) throws Exception
	{
		ResponseMasterNew respM = getResponseMaster(responseId);
		SurveyDefinition sd = SurveyDefFactory.getSurveyDefinition(new FormOID(respM.getFormPk(), null));
		SurveyResponse resp = getSurveyResponse(sd, responseId);
		return resp;
	}
	
	public static SurveyResponse getSurveyResponse(SurveyDefinition surveyDef, int responseId) throws Exception
	{
		SurveyResponse response = null;

		String tabName = surveyDef.getSurveyConfig().getDbTable();

		String sql = Sqls.responseMasterSql;
		sql = sql.replaceAll(Sqls.responseIdPattern, String.valueOf(responseId));
		// sql = sql.replaceAll("<surveyPk>",
		// surveyDef.getSurveyConfig().getPk()); // a responseId is for a survey
		// so the query does not contain surveyPk

		String detailSql = Sqls.responseDetailSql;
		detailSql = detailSql.replaceAll(Sqls.responseIdPattern, String.valueOf(responseId));
		// detailSql = detailSql.replaceAll("<surveyPk>",
		// surveyDef.getSurveyConfig().getPk()); // a responseId is for a survey
		// so the query does not contain surveyPk

		// String textSql = Sqls.responseDetailSql;
		// textSql = textSql.replaceAll(Sqls.responseDetailTablePattern,
		// surveyDef.getSurveyConfig().getDBTableName()+SurveyDefinition.TEXT_SUFFIX);
		// textSql = textSql.replaceAll(Sqls.responseIdPattern, responseId);
		// textSql = textSql.replaceAll("<surveyPk>",
		// surveyDef.getSurveyConfig().getPk()); // a responseId is for a survey
		// so the query does not contain surveyPk

		if (logger.isDebugEnabled())
		{
			logger.debug("Sql - " + sql);
			logger.debug("Detail Table Sql - " + detailSql);
			// logger.debug("Text Table Sql - " + textSql);
		}

		// run query
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				response = new SurveyResponse(surveyDef);
				
				response.setResponseId(responseId);
				response.setTestProcPk(rs.getInt("testProcPk"));
				response.setResponseRefNo(rs.getString("responseRefNo"));
				response.setTestProcPk(rs.getInt("testProcPk"));
				response.setSurveyPk(rs.getInt("surveyPk"));
				

				FormResponseStats responseStat = new FormResponseStats();
				responseStat.setCommentsCount(rs.getInt("noOfComments"));
				responseStat.setTotalQCount(rs.getInt("totalQCount"));
				responseStat.setTotalACount(rs.getInt("totalACount"));
				responseStat.setPassCount(rs.getInt("passCount"));
				responseStat.setFailCount(rs.getInt("failCount"));
				responseStat.setDimentionalFailCount(rs.getInt("dimentionalFailCount"));
				responseStat.setNaCount(rs.getInt("naCount"));
				responseStat.setPercentCompleteLegacy(rs.getInt("percentComplete"));

				response.setResponseStats(responseStat);
				
				Date date = new Date(((Timestamp) rs.getObject("responseStartTime")).getTime());
				response.setResponseStartTime(date);

				if (rs.getObject("responseCompleteTime") != null)
				{
					Date rcDate = new Date(((Timestamp) rs.getObject("responseCompleteTime")).getTime());
					response.setResponseCompleteTime(rcDate);
				}
				if (rs.getObject("responseSyncTime") != null)
				{
					Date rcDate = new Date(((Timestamp) rs.getObject("responseSyncTime")).getTime());
					response.setResponseSyncTime(rcDate);
				}
				response.setIpaddress(rs.getString("ipaddress"));
				response.setUserPk(rs.getInt("userPk"));
				response.setStatus(rs.getString("status"));

				User user = new User();
				user.setPk(rs.getInt("userTab.pk"));
				user.setUserName(rs.getString("userTab.userName"));
				user.setFirstName(rs.getString("userTab.firstName"));
				user.setLastName(rs.getString("userTab.lastName"));
				user.setUserType(rs.getString("userTab.userType"));
				response.setUser(user);

				if(rs.getInt("approvedBy") != 0)
				{
					response.setApprovedBy(persistWrapper.readByPrimaryKey(User.class, rs.getInt("approvedBy")));
				}
				if(rs.getInt("verifiedBy") != 0)
				{
					response.setVerifiedBy(persistWrapper.readByPrimaryKey(User.class, rs.getInt("verifiedBy")));
				}
				
				ResponseFlags flag = new ResponseFlags();
				flag.setPk(rs.getInt("flagTab.pk"));
				flag.setFlag(rs.getString("flagTab.flag"));
				response.setFlag(flag);
			}

			// now get the response details
			rs = stmt.executeQuery(detailSql);
			while (rs.next())
			{
				String questionId = rs.getString("questionId");
				SurveySaveItem sItem = (SurveySaveItem) surveyDef.getQuestion(questionId);
				if (sItem == null)
				{
					// this can happend when a question/surveyparam that has
					// responses in the database is later removed from the
					// survey.
					// in this situation, they need not be added to the response
					// anymore.
					continue;
				}
				SurveyItemResponse itemResponse = response.getAnswer(sItem);
				if (itemResponse == null)
				{
					itemResponse = SurveyItemResponseFactory.getSurveyItemResponse(sItem);
					response.addAnswer(sItem, itemResponse);
				}

				ResponseUnit rUnit = new ResponseUnit();
				rUnit.setKey1(rs.getInt("key1"));
				rUnit.setKey2(rs.getInt("key2"));
				rUnit.setKey3(rs.getInt("key3"));
				rUnit.setKey4(rs.getString("key4"));

				itemResponse.addResponseUnit(rUnit);
			}
			// now get the response text table
			// rs = stmt.executeQuery(textSql);
			// while(rs.next())
			// {
			// String questionId = rs.getString("questionId");
			// SurveySaveItem sItem =
			// (SurveySaveItem)surveyDef.getQuestion(questionId);
			//
			// SurveyItemResponse itemResponse = response.getAnswer(sItem);
			// if(itemResponse == null)
			// {
			// itemResponse =
			// SurveyItemResponseFactory.getSurveyItemResponse(sItem);
			// response.addAnswer(sItem, itemResponse);
			// }
			//
			// ResponseUnit rUnit = new ResponseUnit();
			// rUnit.setKey4(rs.getString("answerText"));
			//
			// itemResponse.addResponseUnit(rUnit);
			// }
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error fetching response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		return response;
	}

	public static SurveyItemResponse getSurveyItemResponse(SurveyDefinition surveyDef, int responseId, String surveyItemId) throws Exception
	{
		SurveySaveItem sItem = null;
		SurveyItemResponse itemResponse = SurveyItemResponseFactory.getSurveyItemResponse(sItem);
		
		String tabName = surveyDef.getSurveyConfig().getDbTable();

		String detailSql = Sqls.responseDetailSql;
		detailSql = detailSql.replaceAll(Sqls.responseIdPattern, String.valueOf(responseId));
		detailSql += " and questionId = '" + surveyItemId + "'";
		if (logger.isDebugEnabled())
		{
			logger.debug("Detail Table Sql - " + detailSql);
		}

		// run query
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			stmt = conn.createStatement();

			// now get the response details
			ResultSet rs = stmt.executeQuery(detailSql);
			while (rs.next())
			{
				String questionId = rs.getString("questionId");
				
				if (sItem == null)
				{
					sItem = (SurveySaveItem) surveyDef.getQuestion(questionId);
					itemResponse = SurveyItemResponseFactory.getSurveyItemResponse(sItem);
				}
				ResponseUnit rUnit = new ResponseUnit();
				rUnit.setKey1(rs.getInt("key1"));
				rUnit.setKey2(rs.getInt("key2"));
				rUnit.setKey3(rs.getInt("key3"));
				rUnit.setKey4(rs.getString("key4"));

				itemResponse.addResponseUnit(rUnit);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error fetching response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		return itemResponse;
	}



	public static SurveyItemResponse getSurveyItemResponse(SurveyItem sItem, String surveyItemId,
			int responseId)
	{
		if(!(sItem instanceof SurveySaveItem))
			return null;
		
		String detailSql = Sqls.sSurveyItemResponseDetailSql;

		if (logger.isDebugEnabled())
		{
			logger.debug("Detail Table Sql - " + detailSql);
		}

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			stmt = conn.prepareStatement(detailSql);

			stmt.setLong(1, responseId);
			stmt.setString(2, surveyItemId);

			ResultSet rs = stmt.executeQuery();

			SurveyItemResponse itemResponse = SurveyItemResponseFactory.getSurveyItemResponse((SurveySaveItem) sItem);
			while (rs.next())
			{
				ResponseUnit rUnit = new ResponseUnit();
				rUnit.setKey1(rs.getInt("key1"));
				rUnit.setKey2(rs.getInt("key2"));
				rUnit.setKey3(rs.getInt("key3"));
				rUnit.setKey4(rs.getString("key4"));

				itemResponse.addResponseUnit(rUnit);
			}
			return itemResponse;
		}
		catch (Exception e)
		{
			logger.error("Error fetching response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			return null;
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	public static List getSurveyItemResponse(SurveyDefinition surveyDef, String surveyItemId,
			ResponseMaster[] responseMasterSet) throws Exception
	{
		String detailSql = Sqls.sSurveyItemResponseDetailSql;

		if (logger.isDebugEnabled())
		{
			logger.debug("Detail Table Sql - " + detailSql);
		}

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			stmt = conn.prepareStatement(detailSql);

			List returnList = new ArrayList();
			for (int i = 0; i < responseMasterSet.length; i++)
			{
				long responseId = responseMasterSet[i].getResponseId();

				stmt.setLong(1, responseId);
				stmt.setString(2, surveyItemId);

				ResultSet rs = stmt.executeQuery();

				SurveySaveItem sItem = (SurveySaveItem) surveyDef.getQuestion(surveyItemId);
				SurveyItemResponse itemResponse = SurveyItemResponseFactory.getSurveyItemResponse(sItem);
				while (rs.next())
				{
					ResponseUnit rUnit = new ResponseUnit();
					rUnit.setKey1(rs.getInt("key1"));
					rUnit.setKey2(rs.getInt("key2"));
					rUnit.setKey3(rs.getInt("key3"));
					rUnit.setKey4(rs.getString("key4"));

					itemResponse.addResponseUnit(rUnit);
				}
				returnList.add(itemResponse);
			}
			return returnList;
		}
		catch (SQLException e)
		{
			logger.error("Error fetching response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}*/

	/**
	 * count of responses recieved for a survey between the given dates.
	 * includes the responses recieved on the start and end date
	 * 
	 * @param startDate
	 * @param endDate
	 */
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

	/*public static List<String> getResponseStatusSetForForm(UserContext context, Survey survey) throws Exception
	{
		return persistWrapper.readList(String.class, "select status from TAB_RESPONSE where surveyPk=?", survey.getPk());
	}
*/
	/**
	 * returns all the responseMaster records for a survey by a respondent in
	 * descenting order of responseTime
	 * 
	 * @param surveyPk
	 * @param respondentPk
	 * @return
	 */
	/*public static List getResponseMastersForRespondent(int surveyPk, int respondentPk) throws Exception
	{
		Survey survey = SurveyMaster.getSurveyByPk(surveyPk);

		String surveyTable = survey.getDbTable();
		String sql = Sqls.getResponseMastersForRespondentId;
		sql = sql.replaceAll("<tableName>", survey.getDbTable());
		sql = sql.replaceAll("<respondentPk>", new Integer(respondentPk).toString());
		sql = sql.replaceAll("<surveyPk>", new Integer(survey.getPk()).toString());

		if (logger.isDebugEnabled())
		{
			logger.debug("Query is - " + sql);
		}

		// run query
		List responseMasterList = new ArrayList();
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				ResponseMaster rMaster = new ResponseMaster();
				rMaster.setResponseId(rs.getInt("responseId"));
				rMaster.setResponseRefNo(rs.getString("responseRefNo"));

				Date date = new Date(((Timestamp) rs.getObject("responseTime")).getTime());
				rMaster.setResponseTime(date);
				Date cdate = new Date(((Timestamp) rs.getObject("responseCompleteTime")).getTime());
				rMaster.setResponseCompleteTime(cdate);

				rMaster.setStatus(rs.getString("status"));

				responseMasterList.add(rMaster);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error getting response master records for a respondent" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		return responseMasterList;
	}
*/
	/**
	 * @param
	 * @param
	 * @return
	 */
	/*public static ResponseMaster getResponseMaster(int surveyPk, String responseId) throws Exception
	{
		Survey survey = SurveyMaster.getSurveyByPk(surveyPk);

		String surveyTable = survey.getDbTable();
		String sql = Sqls.getResponseMaster;
		sql = sql.replaceAll("<tableName>", survey.getDbTable());
		sql = sql.replaceAll("<responseId>", responseId);
		sql = sql.replaceAll("<surveyPk>", new Integer(survey.getPk()).toString());

		if (logger.isDebugEnabled())
		{
			logger.debug("Query is - " + sql);
		}

		// run query
		ResponseMaster rMaster = null;
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				rMaster = new ResponseMaster();
				rMaster.setResponseId(rs.getInt("responseId"));
				rMaster.setResponseRefNo(rs.getString("responseRefNo"));

				Date date = new Date(((Timestamp) rs.getObject("responseTime")).getTime());
				rMaster.setResponseTime(date);
				Date cdate = new Date(((Timestamp) rs.getObject("responseCompleteTime")).getTime());
				rMaster.setResponseCompleteTime(cdate);

				rMaster.setStatus(rs.getString("status"));
			}
		}
		catch (SQLException e)
		{
			logger.error("Error getting response master record" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		return rMaster;
	}

*/
//	public static String getLastResponseReferenceNoFromDevice(String devicePk) throws Exception
//	{
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		try
//		{
//			String returnSeq;
//
//			String query = Sqls.getMaxResponseSeqNoForDevice;
//			query = query.replaceFirst("<devicePk>", devicePk);
//
//			conn = ServiceLocator.locate().getConnection();
//
//			stmt = conn.createStatement();
//
//			rs = stmt.executeQuery(query);
//			if (rs.next())
//			{
//				returnSeq = rs.getString(1);
//			}
//			else
//			{
//				returnSeq = "";
//			}
//
//			return returnSeq;
//		}
//		finally
//		{
//			try
//			{
//				if (rs != null)
//				{
//					rs.close();
//				}
//				if (stmt != null)
//				{
//					stmt.close();
//				}
//				if (conn != null)
//				{
//					conn.close();
//				}
//			}
//			catch (Exception e)
//			{
//			}
//		}
//	}

	// ///////////////// for testsutra
	/*public static ResponseMasterNew getResponseMaster(int responseId) throws Exception
	{
		String sql = ResponseMasterNew.fetchQuery+ " and responseId = ? ";

		ResponseMasterNew response =  persistWrapper.read(ResponseMasterNew.class, sql, responseId);
		
		// this is a minor hack.. since the response is created the moment the form is opened (support multiple people working at the same time)
		// if an form is submitted with no questions answered, the prepared by will be shown as empty which would end up as a bug.  
		//TODO.. the real fix would be to have some temp token generated for the response when the form is opened and sync based on that..
		int count = persistWrapper.read(Integer.class, "select count(*) from TAB_RESPONSE_desc where responseId =?", responseId);
		if(response != null && count == 0)
		{
			response.setPreparedByFirstName("");
			response.setPreparedByLastName("");
			response.setPreparedByPk(0);
		}
		
		return response;
	}
*/
	public  ResponseMasterNew getLatestResponseMasterForTest(TestProcOID testProcOID) throws Exception
	{
		try 
		{
			TestProcObj tp = new TestProcDAO().getTestProc((int) testProcOID.getPk());
			if(tp == null)
				logger.error("Null testproc at pk - " + testProcOID.getPk());
			
			String sql = ResponseMasterNew.fetchQuery
					+ " and res.testProcPk=? and res.surveyPk = ? and res.current = 1 order by responseId";

			List<ResponseMasterNew> responses = persistWrapper.readList(ResponseMasterNew.class, sql, testProcOID.getPk(), tp.getFormPk());
			if(responses == null || responses.size() == 0)
				return null;
			else if(responses.size() == 1)
			{
				return responses.get(0);
			}
			else
			{
				logger.warn("Dupliate responses returned for testId:" +testProcOID.getPk());
				return responses.get(responses.size()-1);
			}
		} catch (Exception e) 
		{
			logger.error(String.format(
					"Error returning the current response for testPk:%d", testProcOID.getPk()), e);
			throw e;
		}
	}


	/*public static ResponseMasterNew[] getLatestResponseMastersForUnitInWorkstation(UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID) throws Exception
	{
		String sql	= "select res.responseId, res.responseRefNo, "
				+ " res.surveyPk as formPk, res.testProcPk as testProcPk, "
				+ " form.identityNumber as formName, form.versionNo as versionNo, "
				+ " res.noOfComments as noOfComments, res.percentComplete as percentComplete, "
				+ " res.totalQCount, res.totalACount, res.passCount, res.failCount, res.dimentionalFailCount, res.naCount, "
				+ " responseStartTime, responseCompleteTime, responseSyncTime, res.lastUpdated, "
				+ " res.userPk as preparedByPk, prepUser.firstName as preparedByFirstName, res.status, res.current, "
				+ " prepUser.lastName as preparedByLastName, res.verifiedBy as verifiedByPk, verifyUser.firstName as verifiedByFirstName,"
				+ " verifyUser.lastName as verifiedByLastName, res.approvedBy as approvedByPk, apprUser.firstName as approvedByFirstName,"
				+ " apprUser.lastName as approvedByLastName, res.verifiedDate, res.verifyComment, res.approvedDate, res.approveComment from "
				+ " TAB_RESPONSE res "
				+ " join TAB_SURVEY form on res.surveyPk = form.pk "
				+ " join unit_testproc ut on res.testProcPk = ut.pk "
				+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.formFk = res.surveyPk and tfa.current = 1 "
				+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
				+ " left join TAB_USER prepUser on res.userPk = prepUser.pk "
				+ " left join TAB_USER verifyUser on res.verifiedBy = verifyUser.pk "
				+ " left join TAB_USER apprUser on res.approvedBy = apprUser.pk "
				+ " where res.current = 1 and "
				+ " uth.unitPk=? and uth.projectPk = ? and uth.workstationPk = ? ";

		List responseIdList = persistWrapper.readList(ResponseMasterNew.class, sql, unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());

		return (ResponseMasterNew[]) responseIdList.toArray(new ResponseMasterNew[responseIdList.size()]);
	}

	//returns the Response entry for a previous form revision on a testProc.
	public static ResponseMasterNew getResponseMasterForTestHistoryRecord(TestProcOID testProcOID, FormOID formOID)
	{
		String sql = ResponseMasterNew.fetchQuery
				+ " and res.testProcPk=? and res.surveyPk = ? order by responseId desc limit 0, 1";

		return persistWrapper.read(ResponseMasterNew.class, sql, testProcOID.getPk(), formOID.getPk());
	}

	
	public static void verifyResponse(UserContext userContext, SurveyResponse sResponse, String comments)
			throws Exception
	{
		String sql = Sqls.verityResponse;
		sql = sql.replaceAll("<tableName>", sResponse.getSurveyDefinition().getSurveyConfig().getDbTable());

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userContext.getUser().getPk());
			stmt.setString(2, comments);
			stmt.setLong(3, sResponse.getResponseId());

			stmt.execute();

			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_VERIFIED, userContext.getUser().getPk(), sResponse.getResponseId(), 
					sResponse.getTestProcPk(), ResponseMasterNew.STATUS_VERIFIED, comments);

		}
		catch (SQLException e)
		{
			logger.error("Error finalizing response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}
*/
	/*public static void rejectResponse(UserContext userContext, SurveyResponse sResponse, String comments)
			throws Exception
	{
		String sql = Sqls.changeResponseStatus;
		sql = sql.replaceAll("<tableName>", sResponse.getSurveyDefinition().getSurveyConfig().getDbTable());

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, ResponseMasterNew.STATUS_REJECTED);
			stmt.setLong(2, sResponse.getResponseId());

			stmt.execute();
			
			//when copyResponse is called, the new response is created as current, the existing one is marked as old 
			ResponseMasterNew responseMaster = getResponseMaster(sResponse.getResponseId());
			markResponseAsOld(userContext, responseMaster);
			
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_VERIFY_REJECTED, userContext.getUser().getPk(), sResponse.getResponseId(), 
					sResponse.getTestProcPk(), ResponseMasterNew.STATUS_REJECTED, comments);


			//create a copy of the response and mark it as incomplete so that the tester can
			//carry on editing the form and resubmitting it
			SurveyResponse newResponse = SurveyResponseManager.copyResponse(userContext, sResponse.getResponseId());
			
			//create the FormResponseClientSubmissionRevision entry if its there for the unit.
			updateFormResponseClientSubmissionRevisionOnNewResponse(userContext, sResponse.getResponseId(), newResponse.getResponseId());
			
			//now create the workflow entry for the new copied response to continue
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, userContext.getUser().getPk(), newResponse.getResponseId(), 
					newResponse.getTestProcPk(), ResponseMasterNew.STATUS_INPROGRESS, comments);
		}
		catch (SQLException e)
		{
			logger.error("Error finalizing response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	private static void updateFormResponseClientSubmissionRevisionOnNewResponse(UserContext userContext,
			int currentResponseId, int newResponseId) throws Exception
	{
		FormResponseClientSubmissionRev currentRev = getFormClientSubmissionRevision(currentResponseId);
		if(currentRev == null)
			return;
		else
		{
			Date now = new Date();
			
			currentRev.setEffectiveDateTo(new Date(now.getTime() - 1000));
			persistWrapper.update(currentRev);
			
			FormResponseClientSubmissionRev newRev = new FormResponseClientSubmissionRev();
			newRev.setCreatedBy(currentRev.getCreatedBy());
			newRev.setCreatedDate(now);
			newRev.setEffectiveDateFrom(now);
			newRev.setEffectiveDateTo(DateUtils.getMaxDate());
			newRev.setResponseId(newResponseId);
			newRev.setSubmissionRevision(currentRev.getSubmissionRevision());
			persistWrapper.createEntity(newRev);
		}
	}

	public static void approveResponse(UserContext userContext,  ResponseMasterNew resp, String comments)
			throws Exception
	{
		String sql = Sqls.approveResponse;
		sql = sql.replaceAll("<tableName>", "TAB_RESPONSE");

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userContext.getUser().getPk());
			stmt.setString(2, comments);
			stmt.setLong(3, resp.getResponseId());

			stmt.execute();


			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_APPROVED, userContext.getUser().getPk(), resp.getResponseId(), 
					resp.getTestProcPk(), ResponseMasterNew.STATUS_APPROVED, comments);

		}
		catch (SQLException e)
		{
			logger.error("Error finalizing response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	public static void approveResponseWithComments(UserContext userContext,  ResponseMasterNew resp, String comments)
			throws Exception
	{
		String sql = Sqls.approveResponseWithComments;
		sql = sql.replaceAll("<tableName>", "TAB_RESPONSE");

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userContext.getUser().getPk());
			stmt.setString(2, comments);
			stmt.setLong(3, resp.getResponseId());

			stmt.execute();


			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_APPROVED_WITH_COMMENTS, userContext.getUser().getPk(), resp.getResponseId(), 
					resp.getTestProcPk(), ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS, comments);

		}
		catch (SQLException e)
		{
			logger.error("Error finalizing response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	public static void rejectApproval(UserContext userContext, SurveyResponse sResponse, String comments)
			throws Exception
	{
		String sql = Sqls.changeResponseStatus;
		sql = sql.replaceAll("<tableName>", sResponse.getSurveyDefinition().getSurveyConfig().getDbTable());

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, ResponseMasterNew.STATUS_REJECTED);
			stmt.setLong(2, sResponse.getResponseId());

			stmt.execute();

			//when copyResponse is called, the new response is created as current, the existing one is marked as old 
			ResponseMasterNew responseMaster = getResponseMaster(sResponse.getResponseId());
			markResponseAsOld(userContext, responseMaster);
			
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_APPROVE_REJECTED, userContext.getUser().getPk(), sResponse.getResponseId(), 
					sResponse.getTestProcPk(), ResponseMasterNew.STATUS_REJECTED, comments);

			//create a copy of the response and mark it as incomplete so that the tester can 
			//carry on editing the form and resubmitting it
			SurveyResponse newResponse = copyResponse(userContext, sResponse.getResponseId());
			
			//create the FormResponseClientSubmissionRevision entry if its there for the unit.
			updateFormResponseClientSubmissionRevisionOnNewResponse(userContext, sResponse.getResponseId(), newResponse.getResponseId());
			
			//now create the workflow entry for the new copied response to continue
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, userContext.getUser().getPk(), newResponse.getResponseId(), 
					newResponse.getTestProcPk(), ResponseMasterNew.STATUS_INPROGRESS, comments);
		}
		catch (SQLException e)
		{
			logger.error("Error finalizing response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	public static void reopenApprovedForm(UserContext userContext, SurveyResponse sResponse, String comments)
			throws Exception
	{
		String sql = Sqls.changeResponseStatus;
		sql = sql.replaceAll("<tableName>", sResponse.getSurveyDefinition().getSurveyConfig().getDbTable());

		// run query
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, ResponseMasterNew.STATUS_REJECTED);
			stmt.setLong(2, sResponse.getResponseId());

			stmt.execute();

			//when copyResponse is called, the new response is created as current, the existing one is marked as old 
			ResponseMasterNew responseMaster = getResponseMaster(sResponse.getResponseId());
			markResponseAsOld(userContext, responseMaster);
			
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_APPROVE_REJECTED, userContext.getUser().getPk(), sResponse.getResponseId(), 
					sResponse.getTestProcPk(), ResponseMasterNew.STATUS_REJECTED, comments);

			//create a copy of the response and mark it as incomplete so that the tester can
			//carry on editing the form and resubmitting it
			SurveyResponse newResponse = copyResponse(userContext, sResponse.getResponseId());
			
			//create the FormResponseClientSubmissionRevision entry if its there for the unit.
			updateFormResponseClientSubmissionRevisionOnNewResponse(userContext, sResponse.getResponseId(), newResponse.getResponseId());
			
			//now create the workflow entry for the new copied response to continue
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, userContext.getUser().getPk(), newResponse.getResponseId(), 
					newResponse.getTestProcPk(), ResponseMasterNew.STATUS_INPROGRESS, comments);
			
			// now we have to re-open the workstation
			TestProcObj unitForm = TestProcManager.getTestProc(newResponse.getTestProcPk());
			UnitLocation ul = ProjectManager.getUnitWorkstation(unitForm.getUnitPk(), new ProjectOID(unitForm.getProjectPk(), null), new WorkstationOID(unitForm.getWorkstationPk(), null));
			
			if(ul != null && UnitLocation.STATUS_COMPLETED.equals(ul.getStatus()))
			{
				ProjectDelegate.setUnitWorkstationStatus(
						userContext,
						new ProjectOID(unitForm.getProjectPk(), null), unitForm.getUnitPk(), new WorkstationOID(unitForm.getWorkstationPk(), null), UnitLocation.STATUS_IN_PROGRESS);
			}
				
		}
		catch (SQLException e)
		{
			logger.error("Error finalizing response" + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	*//**
	 * calculates the PercentComplete and Comments count for the sItems.. It takes all the children of the
	 * items and looks at the answer fom the surveyResponse and calculates the value.
	 * This is used to calculate the values at the whole survey level or at a section level (any question level will work).
	 * @param sItems
	 * @param surveyResponse
	 * @return
	 *//*
	public static FormResponseStats getCommentCountAndPercentComplete(List sItems, SurveyResponse surveyResponse)
	{
		int totalQCount = 0;
		int totalACount = 0;
		int commentsCount = 0;
		int passCount = 0;
		int failCount = 0;
		int dimentionalFailCount = 0; // where the item is marked as isNumeric
		int naCount = 0;
		
		List surveyQuestions = new ArrayList();
		surveyQuestions = getAllChildrenRecursive(sItems, surveyQuestions);
		
		for (Iterator iterator = surveyQuestions.iterator(); iterator.hasNext();)
		{
			SurveyItem	sItem = (SurveyItem) iterator.next();
			if(sItem instanceof SurveySaveItem)
			{
				if(((SurveySaveItem) sItem).isRequired())
				{
					totalQCount ++;
				}
				else
				{
					continue;
				}
				
				SurveyItemResponse sItemResponse = (surveyResponse== null)?null:surveyResponse.getAnswer((SurveySaveItem)sItem);
				if(sItemResponse != null)
				{
					if(sItem instanceof BaseInspectionLineItemAnswerType)
					{
						BaseInspectionLineItemAnswerType bom = (BaseInspectionLineItemAnswerType)sItem;
						InspectionLineItemAnswerStatus answerStatus = bom.getAnswerStatus(sItemResponse);
						boolean commentAnswered = answerStatus.isCommentAnswered();
						boolean passFailAnswered = answerStatus.isPassFailAnswered();
						boolean actualAnswered = answerStatus.isActualAnswered();

						// if pass/fail is there the question is answered only if it is checked 
						//else if actual field is there it should be answered if to have the question answered
						//else if comment is there, comment should be entered to consider the question as answered.
						if(bom.isShowPassFail())
						{
							if(passFailAnswered)
								totalACount++;
						}
						else if(bom.isShowActualValue())
						{
							if(actualAnswered)
								totalACount++;
						}
						else if(bom.isShowComments())
						{
							if(commentAnswered)
								totalACount++;
						}
						
						if(commentAnswered)
							commentsCount++;
						if(answerStatus.isPassSelected())
							passCount++;
						if(answerStatus.isFailSelected())
						{
							failCount++;
							if(((SurveySaveItem) sItem).getDataType() == DataTypes.DATATYPE_INTEGER)
							{
								dimentionalFailCount++;
							}
						}
						if(answerStatus.isNaSelected())
							naCount++;
					}
					else
					{
						totalACount++; // percent complete is calculated on mandatory questions, non-mandatory questions does not hit the response lookup part hereh
						// it hits coninue from the top itself
					}
				}
			}
		}
		
		int percentComplete = 0;
		if(totalQCount > 0)
		{
			percentComplete = totalACount*100/totalQCount; 
		}
		
		FormResponseStats ret = new FormResponseStats();
		ret.setTotalACount(totalACount);
		ret.setTotalQCount(totalQCount);
		ret.setPassCount(passCount);
		ret.setFailCount(failCount);
		ret.setDimentionalFailCount(dimentionalFailCount);
		ret.setNaCount(naCount);
		ret.setCommentsCount(commentsCount);
		return ret;
	}
	

	public static QuestionResponseStatus getAnswerStatValue(SurveySaveItem sItem, SurveyItemResponse sItemResponse)
	{
		QuestionResponseStatus resultStatus = new QuestionResponseStatus();
		
		if(sItem instanceof BaseInspectionLineItemAnswerType)
		{
			BaseInspectionLineItemAnswerType bom = (BaseInspectionLineItemAnswerType)sItem;
			if(bom.isShowPassFail() || bom.isShowActualValue() || bom.isShowComments())
			{
				resultStatus.setQuestionCountedInTotalCount(true); // is counted as a question only if there is any input fiends
			}
			else
			{
				return resultStatus; 
			}
		}
		else
		{
			if(((SurveySaveItem) sItem).isRequired())
			{
				resultStatus.setQuestionCountedInTotalCount(true);
			}
			else
			{
				return resultStatus; 
			}
		}
		
		if(sItemResponse != null)
		{
			if(sItem instanceof BaseInspectionLineItemAnswerType)
			{
				BaseInspectionLineItemAnswerType bom = (BaseInspectionLineItemAnswerType)sItem;
				InspectionLineItemAnswerStatus answerStatus = bom.getAnswerStatus(sItemResponse);
				boolean commentAnswered = answerStatus.isCommentAnswered();
				boolean passFailAnswered = answerStatus.isPassFailAnswered();
				boolean actualAnswered = answerStatus.isActualAnswered();

				// if pass/fail is there the question is answered only if it is checked 
				//else if actual field is there it should be answered if to have the question answered
				//else if comment is there, comment should be entered to consider the question as answered.
				if(bom.isShowPassFail())
				{
					if(passFailAnswered)
						resultStatus.setAnswered(true);
				}
				else if(bom.isShowActualValue())
				{
					if(actualAnswered)
						resultStatus.setAnswered(true);
				}
				else if(bom.isShowComments())
				{
					if(commentAnswered)
						resultStatus.setAnswered(true);
				}
				
				if(commentAnswered)
					resultStatus.setCommentEntered(true);
				
				if(answerStatus.isPassSelected())
					resultStatus.setResult(ResultStatusEnum.Pass);
				
				if(answerStatus.isFailSelected())
				{
					resultStatus.setResult(ResultStatusEnum.Fail);
					if(((SurveySaveItem) sItem).getDataType() == DataTypes.DATATYPE_INTEGER)
					{
						resultStatus.setQuestionNumeric(true);
					}
				}
				if(answerStatus.isNaSelected())
					resultStatus.setResult(ResultStatusEnum.NA);
			}
			else
			{
				resultStatus.setAnswered(true); // percent complete is calculated on mandatory questions, non-mandatory questions does not hit the response lookup part hereh
				// it hits return from the top itself
			}
		}
	
		return resultStatus;
	}
	
	
	
	private static List getAllChildrenRecursive(List sItems, List outList)
	{
		for (Iterator iterator = sItems.iterator(); iterator.hasNext();)
		{
			SurveyItem sItem = (SurveyItem) iterator.next();
			if(sItem instanceof Container)
			{
				getAllChildrenRecursive(((Container)sItem).getChildren(), outList);
			}
			else
			{
				outList.add(sItem);
			}
		}
		return outList;
	}

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

	public static List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd)
	{
		List<SectionResponseQuery> returnList = new ArrayList<SectionResponseQuery>();
		
		for (Iterator iterator = sd.getQuestions().iterator(); iterator.hasNext();)
		{
			SurveyItem aItem = (SurveyItem) iterator.next();
			if(aItem instanceof Section)
			{
				SectionResponseQuery sq = new SectionResponseQuery();
				sq.setSectionId(aItem.getSurveyItemId());
				sq.setName(((Section) aItem).getQuestionText());
				sq.setDescription(((Section) aItem).getDescription());
				returnList.add(sq);
			}
		}
		return returnList;
	}

	public static List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd, int responseId)throws Exception
	{
		FormResponseMaster resp = persistWrapper.readByResponseId(FormResponseMaster.class, responseId);
		return persistWrapper.readList(SectionResponseQuery.class, SectionResponseQuery.SQL + " where tfa.testProcFk = ?", resp.getTestProcPk()); 
	}

	public static SectionResponseQuery getSectionResponseSummary(SurveyDefinition sd, int responseId, String sectionId)throws Exception
	{
		SectionResponseQuery sqr = new SectionResponseQuery();

		SurveyItemBase aItem = sd.getQuestion(sectionId);
		sqr.setSectionId(aItem.getSurveyItemId());
		sqr.setName(((Section) aItem).getQuestionText());
		sqr.setDescription(((Section) aItem).getDescription());
		
		SectionResponseQuery sq = persistWrapper.read(SectionResponseQuery.class, SectionResponseQuery.SQL + " where resp.responseId = ? and formSec.sectionId = ?", responseId, sectionId); 
		if(sq != null)
		{
			sqr.setNoOfComments(sq.getNoOfComments());
			sqr.setPercentComplete(sq.getPercentComplete());
			sqr.setPk(sq.getPk());
			sqr.setResponseId(sq.getResponseId());
			sqr.setLastUpdated(sq.getLastUpdated());
			sqr.setSubmittedBy(sq.getSubmittedBy());
			sqr.setSubmittedByFirstName(sq.getSubmittedByFirstName());
			sqr.setSubmittedByLastName(sq.getSubmittedByLastName());
			sqr.setStartDate(sq.getStartDate());
			sqr.setCompletionDate(sq.getCompletionDate());
		}
		return sq;
	}

	public static SectionResponseQuery getSectionResponseSummary(int responseId, String sectionId)throws Exception
	{
		return persistWrapper.read(SectionResponseQuery.class, SectionResponseQuery.SQL + " where resp.responseId = ? and formSec.sectionId = ?", responseId, sectionId); 
	}
	
	public static FormItemResponse getFormItemResponse(FormItemResponseOID formItemResponseOID)
	{
		return	persistWrapper.readByPrimaryKey(FormItemResponse.class, formItemResponseOID.getPk());
	}

	public static HashMap<String, FormItemResponse> getFormItemResponses(int responseId)
	{
		HashMap<String, FormItemResponse> rMap = new HashMap<String, FormItemResponse>();
		List<FormItemResponse> itemResponseList = persistWrapper.readList(FormItemResponse.class, "select * from TAB_ITEM_RESPONSE USE INDEX (responseId_indx) where responseId = ?", responseId);
		for (Iterator iterator = itemResponseList.iterator(); iterator.hasNext();)
		{
			FormItemResponse formItemResponse = (FormItemResponse) iterator.next();
			rMap.put(formItemResponse.getQuestionId(), formItemResponse);
		}
		
		return rMap;
	}
	
	public static FormItemResponse getFormItemResponse(int responseId, String surveyItemId, boolean createIfNoExist) throws Exception
	{
		FormItemResponse itemResponse = persistWrapper.read(FormItemResponse.class, "select * from TAB_ITEM_RESPONSE where responseId = ? and questionId=?", responseId, surveyItemId);
		if(itemResponse != null)
			return itemResponse;
		
		if(itemResponse == null && createIfNoExist)
		{
			itemResponse = new FormItemResponse();
			itemResponse.setQuestionId(surveyItemId);
			itemResponse.setResponseId(responseId);
			int newPk = persistWrapper.createEntity(itemResponse);
			itemResponse = persistWrapper.readByPrimaryKey(FormItemResponse.class, newPk);
		}

		return itemResponse;
	}

	public static FormItemResponse createFormItemResponse(int responseId, String surveyItemId) throws Exception
	{
		return getFormItemResponse(responseId, surveyItemId, true);
	}
	
	*//**
	 * VA stands for Verifier approver comments.
	 * @param userContext
	 * @param responseMaster
	 * @param vCommentToAdd
	 * @param aCommentToAdd
	 * @throws Exception
	 *//*
	public static void addToResponseVAComments(UserContext userContext,	ResponseMasterNew responseMaster, String vCommentToAdd,
			String aCommentToAdd) throws Exception
	{
		if(vCommentToAdd == null && aCommentToAdd == null)
			return;
		
		DateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		dateTimeFormatter.setTimeZone(TimeZone.getTimeZone(ApplicationProperties.getDefaultTimezone()));
		Date now = new Date();
		String newVString = "";
		String newAString = "";
		if(responseMaster.getVerifyComment() != null)
		{
			newVString+= responseMaster.getVerifyComment() + "\n";
		}
		if(vCommentToAdd != null && vCommentToAdd.trim().length() > 0)
		{
			newVString += "-" + userContext.getUser().getFirstName() + " " + userContext.getUser().getLastName() + " (" +
					dateTimeFormatter.format(now) + " )" + "\n" + vCommentToAdd;
		}
		if(responseMaster.getApproveComment() != null)
		{
			newAString+= responseMaster.getApproveComment() + "\n";
		}
		if(aCommentToAdd != null && aCommentToAdd.trim().length() > 0)
		{
			newAString += "-" + userContext.getUser().getFirstName() + " " + userContext.getUser().getLastName() + " (" +
					dateTimeFormatter.format(now) + " ) " + "\n" + aCommentToAdd;
		}

		StringBuffer query = new StringBuffer("update TAB_RESPONSE set");
		List params = new ArrayList();
		if(vCommentToAdd != null && vCommentToAdd.trim().length() > 0)
		{
			query.append(" verifyComment = ? ,");
			params.add(newVString);
		}
		if(aCommentToAdd != null && aCommentToAdd.trim().length() > 0)
		{
			query.append(" approveComment = ? ,");
			params.add(newAString);
		}
		query = new StringBuffer(query.substring(0, query.length() - 1));
		query.append(" where responseId=" + responseMaster.getResponseId());
		
		persistWrapper.delete(query.toString(), params.toArray(new Object[params.size()]));
	}

	public static FormResponseClientSubmissionRev getFormClientSubmissionRevision(int responseId)
	{
		return persistWrapper.read(FormResponseClientSubmissionRev.class, 
				"select * from RESPONSE_CLIENT_SUBMISSION_REV where responseId = ? and now() between effectiveDateFrom and effectiveDateTo", responseId);
	}

	public static FormResponseClientSubmissionRev saveFormClientSubmissionRevision(UserContext context, int responseId, String revision) throws Exception
	{
		Date now = new Date();
		
		FormResponseClientSubmissionRev current = getFormClientSubmissionRevision(responseId);
		if(current != null && current.getSubmissionRevision() != null && current.getSubmissionRevision().equals(revision)) // its the same, dont have to create a new record
			return current;
		
		if(current != null)
		{
			current.setEffectiveDateTo(new Date(now.getTime() - 1000));
			persistWrapper.update(current);
		}
		
		FormResponseClientSubmissionRev newR = new FormResponseClientSubmissionRev();
		newR.setResponseId(responseId);
		newR.setCreatedBy(context.getUser().getPk());
		newR.setCreatedDate(now);
		newR.setEffectiveDateFrom(now);
		newR.setEffectiveDateTo(DateUtils.getMaxDate());
		newR.setSubmissionRevision(revision);
		int newPk = persistWrapper.createEntity(newR);
		
		return persistWrapper.readByPrimaryKey(FormResponseClientSubmissionRev.class, newPk);

	}

	
	public static FormResponseBean getFormResponseBean(UserContext context, int responseId)throws Exception
	{
		try
		{
			ResponseMasterNew respMaster = SurveyResponseDelegate.getResponseMaster(responseId);
			TestProcObj testProc = (TestProcObj) TestProcManager.getTestProc(respMaster.getTestProcPk());
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
			List<SectionResponseQuery> sectionSummaryList = SurveyResponseDelegate
					.getSectionResponseSummary(surveyDefinition, responseId);
			FormResponseBean formResponseBean = new FormResponseBean();
			formResponseBean.setVersionKey(respMaster.getLastUpdated().getTime());
			if (surveyResponse != null)
			{
				FormResponseStats stats = SurveyResponseManager
						.getCommentCountAndPercentComplete(surveyDefinition.getQuestions(), surveyResponse);
				formResponseBean.setResponseId(surveyResponse.getResponseId());
				formResponseBean.setFormPk(survey.getPk());
				formResponseBean.setResponseStartTime(surveyResponse.getResponseStartTime());
				formResponseBean.setResponseEndTime(surveyResponse.getResponseCompleteTime());
				formResponseBean.setResponseSyncTime(surveyResponse.getResponseSyncTime());
				formResponseBean.setSubmittedByUserPk(surveyResponse.getUserPk());
				formResponseBean.setSubmittedByUserDisplayName(
						surveyResponse.getUser().getFirstName() + " " + surveyResponse.getUser().getLastName());
				formResponseBean.setResponseRefNo(surveyResponse.getResponseRefNo());
				formResponseBean.setResponseStats(stats);
				
				List<FormItemResponseBase> itemResponses = new ArrayList<FormItemResponseBase>();
				for (SectionResponseQuery sectionResponseQuery : sectionSummaryList)
				{
					SectionResponseBean sectionResponseBean = new SectionResponseBean();
					sectionResponseBean.setPk(sectionResponseQuery.getPk());
					sectionResponseBean.setFormItemId(sectionResponseQuery.getSectionId());
					sectionResponseBean.setResponseId(surveyResponse.getResponseId());
					sectionResponseBean.setWorkorderFk(sectionResponseQuery.getWorkorderFk());

					List<SurveyItem> sec = new ArrayList<SurveyItem>();
					sec.add((SurveyItem) surveyDefinition.getQuestion(sectionResponseQuery.getSectionId()));
					FormResponseStats sstats = SurveyResponseManager.getCommentCountAndPercentComplete(sec,
							surveyResponse);

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
							sectionResponseBean.setLockedByUserPk(lockedUser.getPk());
							sectionResponseBean.setLockedByUserDisplayName(
									lockedUser.getFirstName() + " " + lockedUser.getLastName());
						} else
						{
							*//*
							 * show locked by other user..
							 *//*
							sectionResponseBean.setLockStatus(SectionLockStatusEnum.LockedByOther);
							User lockedUser = AccountDelegate.getUser(lock.getUserPk());
							sectionResponseBean.setLockedByUserPk(lockedUser.getPk());
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

				DeviceResponseExportProcessor beanExporter = new DeviceResponseExportProcessor();
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
					if (siResponse != null)
					{
						FormItemResponseBase rBean = beanExporter.getFormItemResponsesBean(sItem, siResponse);
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
			e.printStackTrace();
			throw e;
		}

	}
	
	public static void saveSyncErrorResponse(UserContext context, int responseId,
			FormResponseBean formResponseBean)throws Exception
	{
		EntityVersionReview currentObj = persistWrapper.read(EntityVersionReview.class, 
				"select * from entity_review_version where entityPk=? and entityType=? and createdBy = ? and status = ? ",
				responseId, EntityTypeEnum.Response.getValue(), context.getUser().getPk(), 
				EntityVersionReview.ReviewStatus.Pending.name());
		if(currentObj != null)
		{
			ObjectMapper jm = new ObjectMapper();
	        String objRep = jm.writeValueAsString(formResponseBean);

	        currentObj.setUpdatedDate(new Date());
	        currentObj.setObjectJson(objRep);
			
			persistWrapper.update(currentObj);
		}
		else
		{
			ObjectMapper jm = new ObjectMapper();
	        String objRep = jm.writeValueAsString(formResponseBean);

	        EntityVersionReview obj = new EntityVersionReview();
			obj.setEntityPk(responseId);
			obj.setEntityType(EntityTypeEnum.Response.getValue());
			obj.setCreatedBy(context.getUser().getPk());
			obj.setCreatedDate(new Date());
			obj.setUpdatedDate(obj.getCreatedDate());
			obj.setObjectJson(objRep);
	        obj.setStatus(EntityVersionReview.ReviewStatus.Pending.name());
			persistWrapper.createEntity(obj);
		}
	}

	
	public static List<EntityVersionReviewProxy> getEntityRevisionsForReview(int responseId) throws Exception
	{
		List<EntityVersionReviewProxy> revs = persistWrapper.readList(EntityVersionReviewProxy.class, 
				EntityVersionReviewProxy.sql + " and evr.entityPk=? and evr.entityType=? and evr.status = ? ",
				responseId, EntityTypeEnum.Response.getValue(), 
				EntityVersionReview.ReviewStatus.Pending.name());
		
		return revs;
	}

	public static EntityVersionReviewProxy getEntityRevisionForReview(UserOID userOID, int responseId) throws Exception
	{
		EntityVersionReviewProxy currentObj = persistWrapper.read(EntityVersionReviewProxy.class, 
				EntityVersionReviewProxy.sql + " and evr.entityPk=? and evr.entityType=? and evr.createdBy = ? and evr.status = ? ",
				responseId, EntityTypeEnum.Response.getValue(), userOID.getPk(), 
				EntityVersionReview.ReviewStatus.Pending.name());
		
		return currentObj;
	}
	
	public static FormResponseBean getFormResponseBeanForSyncErrorReview(int entityVersionReviewPk) throws Exception
	{
		EntityVersionReview currentObj = persistWrapper.readByPrimaryKey(EntityVersionReview.class, entityVersionReviewPk);
		if(currentObj == null)
		{
			return null;
		}
		ObjectMapper jm = new ObjectMapper();
		FormResponseBean bean = jm.readValue(currentObj.getObjectJson(), FormResponseBean.class);
		return bean;
	}
	
	public static void removeEntityReviewEntry(int reviewEntityPk)
	{
		persistWrapper.deleteEntity(EntityVersionReview.class, reviewEntityPk);
	}*/
}
