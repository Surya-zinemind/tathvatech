package com.tathvatech.ncr.report;

import java.io.Serializable;
import java.util.Date;

public class NcrCorrectiveActionQuery implements Serializable
{
    private int pk;
    private String ncrNo;
    private String status;
    private String d8StageName;
    private String recommendedActions;
    private Integer custodianPk;
    private String custodianName;
    private Date forecastStartDate;
    private Date forecastCompletionDate;
    private Date completedDate;
    private String completedByName;
    private String completedComment;


    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public String getNcrNo()
    {
        return ncrNo;
    }

    public String getD8StageName()
    {
        return d8StageName;
    }

    public void setD8StageName(String d8StageName)
    {
        this.d8StageName = d8StageName;
    }

    public void setNcrNo(String ncrNo)
    {
        this.ncrNo = ncrNo;
    }

    public String getRecommendedActions()
    {
        return recommendedActions;
    }

    public void setRecommendedActions(String recommendedActions)
    {
        this.recommendedActions = recommendedActions;
    }

    public Integer getCustodianPk()
    {
        return custodianPk;
    }

    public void setCustodianPk(Integer custodianPk)
    {
        this.custodianPk = custodianPk;
    }

    public String getCustodianName()
    {
        return custodianName;
    }

    public void setCustodianName(String custodianName)
    {
        this.custodianName = custodianName;
    }

    public Date getForecastStartDate()
    {
        return forecastStartDate;
    }

    public void setForecastStartDate(Date forecastStartDate)
    {
        this.forecastStartDate = forecastStartDate;
    }

    public Date getForecastCompletionDate()
    {
        return forecastCompletionDate;
    }

    public void setForecastCompletionDate(Date forecastCompletionDate)
    {
        this.forecastCompletionDate = forecastCompletionDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCompletedDate()
    {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate)
    {
        this.completedDate = completedDate;
    }

    public String getCompletedByName()
    {
        return completedByName;
    }

    public void setCompletedByName(String completedByName)
    {
        this.completedByName = completedByName;
    }

    public String getCompletedComment()
    {
        return completedComment;
    }

    public void setCompletedComment(String completedComment)
    {
        this.completedComment = completedComment;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof NcrCorrectiveActionQuery))
            return false;
        return (getPk() == ((NcrCorrectiveActionQuery) obj).getPk());
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

}
