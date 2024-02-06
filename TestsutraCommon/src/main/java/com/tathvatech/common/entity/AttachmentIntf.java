package com.tathvatech.common.entity;


public interface AttachmentIntf 
{
	public long getPk();
	public void setPk(long pk);
	public String getFullFilePath();
	public String getFileName();
	public String getFileDisplayName();
	public String getFileDescription();
	public String getAttachContext();
}
