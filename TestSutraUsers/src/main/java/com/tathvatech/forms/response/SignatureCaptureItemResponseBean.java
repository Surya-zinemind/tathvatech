package com.tathvatech.forms.response;

import com.tathvatech.forms.enums.FormItemTypeEnum;
import com.tathvatech.forms.response.FormItemResponseBase;

import java.util.Date;

public class SignatureCaptureItemResponseBean extends FormItemResponseBase {

	String imageFileName;
	String imageEncodedString;
	Date signatureTimestamp;
	String signedBy;
	
	public String getImageFileName()
	{
		return imageFileName;
	}
	public void setImageFileName(String imageFileName)
	{
		this.imageFileName = imageFileName;
	}
	public String getImageEncodedString()
	{
		return imageEncodedString;
	}
	public void setImageEncodedString(String imageEncodedString)
	{
		this.imageEncodedString = imageEncodedString;
	}
	public Date getSignatureTimestamp()
	{
		return signatureTimestamp;
	}
	public void setSignatureTimestamp(Date signatureTimestamp)
	{
		this.signatureTimestamp = signatureTimestamp;
	}
	
	public String getSignedBy()
	{
		return signedBy;
	}
	public void setSignedBy(String signedBy)
	{
		this.signedBy = signedBy;
	}
	@Override
	public FormItemTypeEnum getFormItemType() {
		return FormItemTypeEnum.SignatureCaptureAnswerType;
	}
}
