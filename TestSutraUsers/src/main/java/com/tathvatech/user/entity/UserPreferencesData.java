/*
\ * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="user_preferences_data")
public class UserPreferencesData extends AbstractEntity implements Serializable
{
	public static enum PropertyEnum {UnitLevelOpenItemListFilter, 
		NCRListView,
		NCRListMeetingsView,
		WorkstationStatusSummaryReport,
		HazardAndMaintenanceReport,
		InjuryListReport,
		InjurySummaryReport,
		NearMissListReport,
		NearMissSummaryReport,
		WorkstationReportListView,
		MRFListReport,
		MRFSummaryReport,
		AndonListReport,
		AndonSummaryReport,
		NcrCorrectionActionListView,
		NcrCorrectiveActionSummaryView,
		VCRListReportView,
		VCRSummaryReportView,
		NCRGroupListView,
		NcrRaisedSummaryReportsView,
		NcrPublishedSummaryReportView,
		NcrTopSuppliersSummaryReportView,
		NcrTopPartsSummaryReportView,
		NcrTopCategoriesSummaryReportView,
		NcrAvgDaysToDispositionReportView,
		NcrAvgDaysToPublishReportView,
		NcrAvgDaysToCompleteReportView,
		NcrAvgDaysToCloseReportView,
		SupportTicketListReportView,
		CorrectionActionListView,
		HMListReportView,
		HMSummaryReportView,
		EquipmentListView,
		EquipmentSummaryView,
		SuggestionListReportView,
		SuggestionSummaryReportView
		
		};

	@Id
	private long pk;
	private int userPk;
	private Integer entityPk;
	private Integer entityType;
	private String name;
	private String property;
	private String value;
	private Date createdDate;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getUserPk() {
		return userPk;
	}
	public void setUserPk(int userPk) {
		this.userPk = userPk;
	}
	public Integer getEntityPk() {
		return entityPk;
	}
	public void setEntityPk(Integer entityPk) {
		this.entityPk = entityPk;
	}
	public Integer getEntityType() {
		return entityType;
	}
	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getProperty()
	{
		return property;
	}
	public void setProperty(String property)
	{
		this.property = property;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
}
