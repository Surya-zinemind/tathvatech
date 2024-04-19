package com.tathvatech.forms.common;

import java.util.ArrayList;
import java.util.List;

import com.tathvatech.forms.response.BaseResponseBean;

public class FormBeanLinear extends BaseResponseBean {
	
	private int pk;
	private String name;
	private String description1;
	private String description2;
	private String lastModified;
	private int versionNo;
	private String revision;
	private String instructionFile;
	List<FormItemBase> formItems = new ArrayList();
	
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription1() {
		return description1;
	}
	public void setDescription1(String description1) {
		this.description1 = description1;
	}
	public String getDescription2() {
		return description2;
	}
	public void setDescription2(String description2) {
		this.description2 = description2;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	public int getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(int versionNo) {
		this.versionNo = versionNo;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getInstructionFile() {
		return instructionFile;
	}
	public void setInstructionFile(String instructionFile) {
		this.instructionFile = instructionFile;
	}
	public List<FormItemBase> getFormItems() {
		return formItems;
	}
	public void setFormItems(List<FormItemBase> formItems) {
		this.formItems = formItems;
	}
}
