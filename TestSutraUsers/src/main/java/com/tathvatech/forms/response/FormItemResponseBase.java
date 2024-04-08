package com.tathvatech.forms.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tathvatech.forms.enums.FormItemTypeEnum;
import com.tathvatech.user.OID.UserOID;


@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "formItemType")  
	@JsonSubTypes({  
	    @Type(value = SectionResponseBean.class, name = "Section"),
	    @Type(value = TextBoxItemResponseBean.class, name = "TextBoxAnswerType"),
	    @Type(value = TextAreaItemResponseBean.class, name = "TextAreaAnswerType"),
	    @Type(value = SignatureCaptureItemResponseBean.class, name = "SignatureCaptureAnswerType"),
	    @Type(value = OptionGroupItemResponseBean.class, name = "OptionGroupAnswerType"),
	    @Type(value = AdvancedBomInspectionItemResponseBean.class, name = "AdvancedBomInspectionItemAnswerType") 
	    }) 
public abstract class FormItemResponseBase{

	String formItemId;
	int responseId;
	
	Date updatedDate;
	UserOID updatedBy;
	long objectVersionKey;
	
	
	public String getFormItemId() {
		return formItemId;
	}
	public void setFormItemId(String formItemId) {
		this.formItemId = formItemId;
	}
	
	public int getResponseId() {
		return responseId;
	}
	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}
	public long getObjectVersionKey()
	{
		return objectVersionKey;
	}
	public void setObjectVersionKey(long objectVersionKey)
	{
		this.objectVersionKey = objectVersionKey;
	}
	public Date getUpdatedDate()
	{
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate)
	{
		this.updatedDate = updatedDate;
	}
	public UserOID getUpdatedBy()
	{
		return updatedBy;
	}
	public void setUpdatedBy(UserOID updatedBy)
	{
		this.updatedBy = updatedBy;
	}
	@JsonIgnore
	public abstract FormItemTypeEnum getFormItemType();
	
	
	public static boolean isSame(FormItemResponseBase mergeSource, FormItemResponseBase mergeTarget)
	{
		if(mergeSource == null && mergeTarget == null)
			return true;
		if(mergeSource != null && mergeTarget == null)
			return false;
		if(mergeSource == null && mergeTarget != null)
			return false;
		if(!(mergeSource.getClass().getName().equals(mergeTarget.getClass().getName())))
			return false;
		return mergeSource.equals(mergeTarget);
	}
}
