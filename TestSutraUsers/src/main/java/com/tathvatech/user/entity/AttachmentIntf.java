package com.tathvatech.user.entity;


public interface AttachmentIntf 
{
	public int getPk();
	public void setPk(int pk);
	public String getFullFilePath();
	public String getFileName();
	public String getFileDisplayName();
	public String getFileDescription();
	public String getAttachContext();
}
