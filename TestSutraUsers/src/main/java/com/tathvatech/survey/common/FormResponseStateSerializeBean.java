package com.tathvatech.survey.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tathvatech.forms.response.FormResponseStats;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.survey.entity.ResponseFlags;
import com.tathvatech.survey.inf.SurveySaveItemBase;
import com.tathvatech.survey.response.SimpleSurveyItemResponse;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.user.entity.User;


public class FormResponseStateSerializeBean
{
	private ResponseMasterNew responseMaster;
    private HashMap<String, SimpleSurveyItemResponse> answerMap = new HashMap();
	private List<SectionResponseQuery> sectionResponseList;

	
	//we cannot directly add the SurveyResponse into the serialize bean. as it has map serialize-deserialize issues.
	//so we are taking the fields and the map and adding it into this bean. the map is added with string as the key
	//these are the fields from SurveyResponse we are copying to the serialize bean . 
	//most of theser are available in the ResponseMasterNew object. but we keep it here for now.
	//remove all this when we clean up the ResponesMasterNew classes
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
    private User user;
    private ResponseFlags flag;
    private User verifiedBy;
    private User approvedBy;
    
	
	public int getResponseId()
	{
		return responseId;
	}
	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}
	public int getTestProcPk()
	{
		return testProcPk;
	}
	public void setTestProcPk(int testProcPk)
	{
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
	public FormResponseStats getResponseStats()
	{
		return responseStats;
	}
	public void setResponseStats(FormResponseStats responseStats)
	{
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
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public ResponseFlags getFlag()
	{
		return flag;
	}
	public void setFlag(ResponseFlags flag)
	{
		this.flag = flag;
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
	public ResponseMasterNew getResponseMaster()
	{
		return responseMaster;
	}
	public void setResponseMaster(ResponseMasterNew responseMaster)
	{
		this.responseMaster = responseMaster;
	}
	public List<SectionResponseQuery> getSectionResponseList()
	{
		return sectionResponseList;
	}
	public void setSectionResponseList(List<SectionResponseQuery> sectionResponseList)
	{
		this.sectionResponseList = sectionResponseList;
	}
	public HashMap<String, SimpleSurveyItemResponse> getAnswerMap()
	{
		return answerMap;
	}
	public void setAnswerMap(HashMap<String, SimpleSurveyItemResponse> answerMap)
	{
		this.answerMap = answerMap;
	}
	
	@JsonIgnore
	public SurveyResponse getSurveyResponse(SurveyDefinition sd)
	{
		SurveyResponse sResponse = new SurveyResponse(sd);
		sResponse.setTestProcPk(testProcPk);
		sResponse.setResponseId(responseId);
		sResponse.setSurveyPk(surveyPk);
		sResponse.setUser(user);
		sResponse.setUserPk(userPk);
		sResponse.setResponseMode(responseMode);
		sResponse.setResponseStartTime(responseStartTime);
		sResponse.setResponseCompleteTime(responseCompleteTime);
		sResponse.setResponseSyncTime(responseSyncTime);
		sResponse.setFlagPk(flagPk);
		sResponse.setIpaddress(ipaddress);
		sResponse.setResponseRefNo(responseRefNo);
		sResponse.setResponseStats(responseStats);
		sResponse.setStatus(status);
		sResponse.setLastUpdated(lastUpdated);
		sResponse.setFlag(flag);
		sResponse.setVerifiedBy(verifiedBy);
		sResponse.setApprovedBy(approvedBy);

		
		if(answerMap != null)
		{
			for (Iterator iterator = answerMap.keySet().iterator(); iterator.hasNext();)
			{
				String aItemId = (String) iterator.next();
				SurveySaveItemBase aSItem = (SurveySaveItemBase) sd.getQuestion(aItemId);
				SimpleSurveyItemResponse resp = answerMap.get(aItemId);
				sResponse.addAnswer(aSItem, resp);
			}
		}
		return sResponse;
	}

	
}
