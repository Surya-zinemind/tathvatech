/*
 * Created on Jun 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.ts.core.common.EntityTypeEnum;

import net.sf.persist.annotations.NoTable;

/**
 * @author Hari
 * 
 */

@NoTable
public class TestItemOILTransferQuery implements Serializable
{
	private int	pk;
	private String formItemId;
	private int itemResponsePk;
	private int oilItemType;
	private int oilItemPk;
	private String oilReferenceNo;
	private Date createdDate;
	private int createdBy;
	private String createdByName;
	private Date lastUpdated;
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public String getFormItemId()
	{
		return formItemId;
	}

	public void setFormItemId(String formItemId)
	{
		this.formItemId = formItemId;
	}

	public int getItemResponsePk() {
		return itemResponsePk;
	}

	public void setItemResponsePk(int itemResponsePk) {
		this.itemResponsePk = itemResponsePk;
	}

	public int getOilItemPk() {
		return oilItemPk;
	}

	public void setOilItemPk(int oilItemPk) {
		this.oilItemPk = oilItemPk;
	}

	public int getOilItemType() {
		return oilItemType;
	}

	public void setOilItemType(int oilItemType) {
		this.oilItemType = oilItemType;
	}

	public String getOilReferenceNo()
	{
		return oilReferenceNo;
	}

	public void setOilReferenceNo(String oilReferenceNo)
	{
		this.oilReferenceNo = oilReferenceNo;
	}

	public String getCreatedByName()
	{
		return createdByName;
	}

	public void setCreatedByName(String createdByName)
	{
		this.createdByName = createdByName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public static String sql = "select tot.pk, formItemResponse.questionId as formItemId, tot.itemResponsePk, tot.oilItemPk, tot.oilItemType, tot.createdDate, tot.createdBy, tot.lastUpdated,  "
			+ " case when tot.oilItemType = " + EntityTypeEnum.OpenItem.getValue() + " then op.referenceNo "
			+ " when oilItemType = " + EntityTypeEnum.NCR.getValue() + " then concat(ncrGroup.ncrGroupNo, '.', ncr.ncrNo) end as oilReferenceNo,"
			+ " concat(user.firstName, ', ', user.lastName) as createdByName "
			+ " from TESTITEM_OIL_TRANSFER tot "
			+ " join TAB_ITEM_RESPONSE formItemResponse on tot.itemResponsePk = formItemResponse.pk"
			+ " left outer join openitem_v2 op on tot.oilItemPk = op.pk"
			+ " left outer join ncr on tot.oilItemPk = ncr.pk"
			+ " left outer join ncr_group ncrGroup on ncr.ncrGroupFk = ncrGroup.pk"
			+ " join TAB_USER user on tot.createdBy = user.pk"
			+ " where 1 = 1";

//	public static String sql = "select tot.pk, tot.itemResponsePk, tot.oilItemPk, tot.oilItemType, tot.createdDate, tot.createdBy, tot.lastUpdated,  "
//			+ " ncrGroup.ncrGroupNo, "
//			+ " ncr.ncrNo as oilrefno,"
//			+ " concat(user.firstName, ', ', user.lastName) as createdByName "
//			+ " from TESTITEM_OIL_TRANSFER tot "
//			+ " left join TAB_RESPONSE resp on tot.oilItemPk = resp.responseId"
//			+ " left outer join ncr on tot.oilItemPk = ncr.pk"
//			+ " left outer join ncr_group ncrGroup on ncr.ncrGroupFk = ncr.pk"
//			+ " join TAB_USER user on tot.createdBy = user.pk"
//			+ " where 1 = 1";
}
