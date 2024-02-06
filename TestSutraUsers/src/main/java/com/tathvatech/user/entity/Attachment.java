/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_ATTACHMENT")
public class Attachment  extends AbstractEntity   implements AttachmentIntf , Serializable
{
	private long pk;
	private int objectPk;
	private int objectType;
	private String fullFilePath; // this denotes the absolute path of the file.not a db field
	private String fileName;
	private String fileDisplayName;
	private String fileDescription;
	private String attachContext; // something which can tell why it is attached. managed by the object
	private int createdBy;
	private Date createdDate;
	private String param1; // 3 custom fields.. could be anything which the object manages
	private String param2;
	private String param3;
	private int estatus;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	@Override
	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getObjectPk() {
		return objectPk;
	}

	public void setObjectPk(int objectPk) {
		this.objectPk = objectPk;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}


	public String getFullFilePath() {
		return fullFilePath;
	}

	public void setFullFilePath(String fullFilePath) {
		this.fullFilePath = fullFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDisplayName() {
		return fileDisplayName;
	}

	public void setFileDisplayName(String fileDisplayName) {
		this.fileDisplayName = fileDisplayName;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public String getAttachContext() {
		return attachContext;
	}

	public void setAttachContext(String attachContext) {
		this.attachContext = attachContext;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public int getEstatus()
	{
		return estatus;
	}

	public void setEstatus(int estatus)
	{
		this.estatus = estatus;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	/**
     * 
     */
    public Attachment()
    {
    }

    @Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return (int) pk;
	}

	@Override
	public boolean equals(Object obj) 
    {
    	if(obj == null || !(obj instanceof Attachment))
    		return false;
    	if(((Attachment)obj).getPk() == this.getPk())
    		return true;
    	
    	return false;
    }
}
