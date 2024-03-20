/*
 * Created on Oct 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.user.entity.User;


import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyResponse
{
    private static final Logger logger = LoggerFactory.getLogger(SurveyResponse.class);
    
    public static final String STATUS_INCOMPLETE = "In Progress";
    public static final String STATUS_COMPLETE = "Complete";
	//Uncommet and fix this
    
 private int responseId;
    private int testProcPk;
    private int surveyPk; 
    private int userPk;
    private String responseMode;  // dataEntry or normal
    private Date responseStartTime;
    private Date responseCompleteTime;
    private Date responseSyncTime;
    private int flagPk;
    private String ipaddress;
    private String responseRefNo; // unique no generated at the device 
    private FormResponseStats responseStats;
    private String status;
    private Date lastUpdated;

    
    @JsonIgnore
    private SurveyDefinition surveyDefinition;
    
    
    // we are making this hashmap ignore. but we are creating a hashmap with the surveyItemId and the SimpleSurveyItemResponse into the serialize object to save this
    @JsonIgnore
    private HashMap<SurveySaveItemBase,SurveyItemResponse> answerMap = new HashMap();
    private User user;
    private ResponseFlags flag;

    private User verifiedBy;
    private User approvedBy;

    public SurveyResponse(SurveyDefinition sd)
    {
    	this.surveyDefinition = sd;
    }
    
    public SurveyResponse(SurveyDefinition sd, UnitTestProc unitForm, UnitQuery uq, User user)
    {
		this(sd);
		responseStartTime = new Date();
		ipaddress = "";
		testProcPk = unitForm.getPk();
		userPk = user.getPk();
		this.user = user;
		status = ResponseMasterNew.STATUS_INPROGRESS;
    }
    

    public int getResponseId()
	{
		return responseId;
	}




	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}


	public int getTestProcPk() {
		return testProcPk;
	}

	public void setTestProcPk(int testProcPk) {
		this.testProcPk = testProcPk;
	}

	public int getSurveyPk()
	{
		return surveyPk;
	}




	public void setSurveyPk(int surveyPk)
	{
		this.surveyPk = surveyPk;
	}




	public int getUserPk()
	{
		return userPk;
	}




	public void setUserPk(int userPk)
	{
		this.userPk = userPk;
	}




	public User getVerifiedBy()
	{
		return verifiedBy;
	}

	public void setVerifiedBy(User verifiedBy)
	{
		this.verifiedBy = verifiedBy;
	}

	public User getApprovedBy()
	{
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy)
	{
		this.approvedBy = approvedBy;
	}

	public String getResponseMode()
	{
		return responseMode;
	}




	public void setResponseMode(String responseMode)
	{
		this.responseMode = responseMode;
	}




	public Date getResponseStartTime()
	{
		return responseStartTime;
	}




	public void setResponseStartTime(Date responseStartTime)
	{
		this.responseStartTime = responseStartTime;
	}




	public Date getResponseCompleteTime()
	{
		return responseCompleteTime;
	}




	public void setResponseCompleteTime(Date responseCompleteTime)
	{
		this.responseCompleteTime = responseCompleteTime;
	}




	public Date getResponseSyncTime()
	{
		return responseSyncTime;
	}




	public void setResponseSyncTime(Date responseSyncTime)
	{
		this.responseSyncTime = responseSyncTime;
	}




	public int getFlagPk()
	{
		return flagPk;
	}




	public void setFlagPk(int flagPk)
	{
		this.flagPk = flagPk;
	}




	public String getIpaddress()
	{
		return ipaddress;
	}




	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}




	public String getResponseRefNo()
	{
		return responseRefNo;
	}




	public void setResponseRefNo(String responseRefNo)
	{
		this.responseRefNo = responseRefNo;
	}


	public FormResponseStats getResponseStats() {
		return responseStats;
	}

	public void setResponseStats(FormResponseStats responseStats) {
		this.responseStats = responseStats;
	}

	public String getStatus()
	{
		return status;
	}




	public void setStatus(String status)
	{
		this.status = status;
	}




	public Date getLastUpdated()
	{
		return lastUpdated;
	}




	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}




	@NoColumn
    public SurveyDefinition getSurveyDefinition()
    {
        return surveyDefinition;
    }
    
    public void setSurveyDefinition(SurveyDefinition sDef)
    {
    	this.surveyDefinition = sDef;
    }
    
    *//**
     * @param question
     * @param answer
     * @throws Exception
     *//*
    public void addAnswer(SurveySaveItemBase sItem, SurveyItemResponse answer)
    {
        AnswerPersistor ap = sItem.getPersistor(answer);
        if(ap != null)//if ap is null that answer type need not be persisted
        {
//            answers.add(ap);
        	answerMap.remove(sItem);
            answerMap.put(sItem, answer);
        }
    }

    *//**
     * @param item
     * @return
     *//*
    @NoColumn
    public SurveyItemResponse getAnswer(SurveySaveItemBase sItem)
    {
        return (SurveyItemResponse)answerMap.get(sItem);
    }

    *//**
     * @param ssItem
     *//*
    public void clearAnswer(SurveySaveItemBase ssItem)
    {
        answerMap.remove(ssItem);
    }
    
    public HashMap<SurveySaveItemBase,SurveyItemResponse> getSurveyItemAnswerMap()
    {
    	return answerMap;
    }
    
    *//**
     * use with caution, there could be duplicate surveyItem ids across different forms.
     * @param itemId
     * @return
     *//*
    public SurveyItemResponse getAnswerBySurveyItemId(String itemId)
    {
    	SurveyItemResponse ires = null;
    	for (Iterator iterator = answerMap.keySet().iterator(); iterator.hasNext();) {
			SurveyItemBase aItem = (SurveyItemBase) iterator.next();
			if(aItem.getSurveyItemId().equals(itemId))
			{
				ires = (SurveyItemResponse) answerMap.get(aItem);
				break;
			}
		}
    	return ires;
    }
    
	@NoColumn
    public User getUser() 
	{
		return user;
	}
	public void setUser(User user) 
	{
		this.user = user;
	}
	@NoColumn
	public ResponseFlags getFlag()
	{
		return flag;
	}
	public void setFlag(ResponseFlags flag)
	{
		this.flag = flag;
	}

	public FormResponseOID getOID()
	{
		return new FormResponseOID(responseId);
	}
}
