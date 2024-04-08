package com.tathvatech.forms.response;

import java.util.Date;

public class SignatureCaptureItemResponse{

	String imageFileName;
	String signedBy;
	Date signatureTimestamp;
	
	public String getImageFileName()
	{
		return imageFileName;
	}
	public void setImageFileName(String imageFileName)
	{
		this.imageFileName = imageFileName;
	}
	public String getSignedBy()
	{
		return signedBy;
	}
	public void setSignedBy(String signedBy)
	{
		this.signedBy = signedBy;
	}
	public Date getSignatureTimestamp()
	{
		return signatureTimestamp;
	}
	public void setSignatureTimestamp(Date signatureTimestamp)
	{
		this.signatureTimestamp = signatureTimestamp;
	}
}
