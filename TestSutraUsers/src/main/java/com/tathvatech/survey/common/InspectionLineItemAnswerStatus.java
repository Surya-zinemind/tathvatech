package com.tathvatech.survey.common;

public class InspectionLineItemAnswerStatus
{
	String commentAnswer;
	boolean commentAnswered;
	boolean passFailAnswered;
	boolean actualAnswered;
	boolean allAdditonalMandatoryFieldsAnswered;
	boolean failSelected;
	boolean passSelected;
	boolean naSelected;
	String equipmentPkString;
	boolean equipmentSelected;
	boolean attachmentAdded;
	
	
	public String getCommentAnswer()
	{
		return commentAnswer;
	}
	public void setCommentAnswer(String commentAnswer)
	{
		this.commentAnswer = commentAnswer;
	}
	public boolean isCommentAnswered()
	{
		return commentAnswered;
	}
	public void setCommentAnswered(boolean commentAnswered)
	{
		this.commentAnswered = commentAnswered;
	}
	public boolean isPassFailAnswered()
	{
		return passFailAnswered;
	}
	public void setPassFailAnswered(boolean passFailAnswered)
	{
		this.passFailAnswered = passFailAnswered;
	}
	public boolean isFailSelected()
	{
		return failSelected;
	}
	public void setFailSelected(boolean failSelected)
	{
		this.failSelected = failSelected;
	}
	public boolean isActualAnswered()
	{
		return actualAnswered;
	}
	public void setActualAnswered(boolean actualAnswered)
	{
		this.actualAnswered = actualAnswered;
	}
	public boolean isAllAdditonalMandatoryFieldsAnswered()
	{
		return allAdditonalMandatoryFieldsAnswered;
	}
	public void setAllAdditonalMandatoryFieldsAnswered(boolean allAdditonalMandatoryFieldsAnswered)
	{
		this.allAdditonalMandatoryFieldsAnswered = allAdditonalMandatoryFieldsAnswered;
	}
	public boolean isPassSelected() {
		return passSelected;
	}
	public void setPassSelected(boolean passSelected) {
		this.passSelected = passSelected;
	}
	public boolean isNaSelected() {
		return naSelected;
	}
	public void setNaSelected(boolean naSelected) {
		this.naSelected = naSelected;
	}
	public String getEquipmentPkString()
	{
		return equipmentPkString;
	}
	public void setEquipmentPkString(String equipmentPkString)
	{
		this.equipmentPkString = equipmentPkString;
	}
	public boolean isEquipmentSelected()
	{
		return equipmentSelected;
	}
	public void setEquipmentSelected(boolean equipmentSelected)
	{
		this.equipmentSelected = equipmentSelected;
	}
	public boolean isAttachmentAdded()
	{
		return attachmentAdded;
	}
	public void setAttachmentAdded(boolean attachmentAdded)
	{
		this.attachmentAdded = attachmentAdded;
	}
	
}
