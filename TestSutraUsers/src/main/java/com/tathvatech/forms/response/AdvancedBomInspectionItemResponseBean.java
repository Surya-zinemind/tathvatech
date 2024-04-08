package com.tathvatech.forms.response;

import com.tathvatech.forms.enums.FormItemTypeEnum;
import com.tathvatech.survey.common.TCell;
import com.tathvatech.user.common.AttachmentInfoBean;

import java.util.ArrayList;
import java.util.List;

public class AdvancedBomInspectionItemResponseBean extends FormItemResponseBase
{
	Integer result; // value on ResultEnum
	String comment;
	boolean complete;
	Integer equipmentFk;
	
	List<TCell> cells = new ArrayList();

	List<AttachmentInfoBean> attachments = new ArrayList<AttachmentInfoBean>();
	
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getEquipmentFk()
	{
		return equipmentFk;
	}
	public void setEquipmentFk(Integer equipmentFk)
	{
		this.equipmentFk = equipmentFk;
	}
	public List<TCell> getCells() {
		return cells;
	}
	public void setCells(List<TCell> cells) {
		this.cells = cells;
	}
	public List<AttachmentInfoBean> getAttachments()
	{
		return attachments;
	}
	public void setAttachments(List<AttachmentInfoBean> attachments)
	{
		this.attachments = attachments;
	}

	@Override
	public FormItemTypeEnum getFormItemType() {
		return FormItemTypeEnum.AdvancedBomInspectionItemAnswerType;
	}
}
