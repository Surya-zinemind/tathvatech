package com.tathvatech.forms.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.Authorizable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="TAB_FORM_WORKFLOW")
public class FormWorkflow extends AbstractEntity implements Serializable
{

    @Id
    private long pk;
    private Date date;
    private int performedBy;
    private int testProcPk;
    private int responseId;
    private String action;
    private String resultStatus;
    private String comments;
    private int current;
    private Date lastUpdated;

    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public Date getDate()
    {
        return date;
    }
    public void setDate(Date date)
    {
        this.date = date;
    }
    public int getPerformedBy()
    {
        return performedBy;
    }
    public void setPerformedBy(int performedBy)
    {
        this.performedBy = performedBy;
    }
    public int getTestProcPk() {
		return testProcPk;
	}
	public void setTestProcPk(int testProcPk) {
		this.testProcPk = testProcPk;
	}
	public int getResponseId()
    {
        return responseId;
    }
    public void setResponseId(int responseId)
    {
        this.responseId = responseId;
    }
    public String getAction()
    {
        return action;
    }
    public void setAction(String action)
    {
        this.action = action;
    }
    public String getResultStatus()
    {
        return resultStatus;
    }
    public void setResultStatus(String resultStatus)
    {
        this.resultStatus = resultStatus;
    }
    public String getComments()
    {
        return comments;
    }
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    public int getCurrent()
	{
		return current;
	}
	public void setCurrent(int current)
	{
		this.current = current;
	}

    public Date getLastUpdated()
    {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public static String ACTION_START = "Start";
    public static String ACTION_SUBMIT = "Completed";
    public static String ACTION_EDIT_RESPONSE = "Edit Response";
    public static String ACTION_VERIFIED = "Verified";
    public static String ACTION_VERIFY_REJECTED = "Rejected Verification";
    public static String ACTION_APPROVED = "Approved";
    public static String ACTION_APPROVED_WITH_COMMENTS = "Approved with comments";
    public static String ACTION_APPROVE_REJECTED = "Rejected Approval";

}


