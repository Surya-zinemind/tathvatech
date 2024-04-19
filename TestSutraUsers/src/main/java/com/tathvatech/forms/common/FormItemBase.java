package com.tathvatech.forms.common;

import com.tathvatech.forms.enums.FormItemTypeEnum;

import java.util.List;

public abstract class FormItemBase{

	String formItemId;
	private float orderNo;
	private String parentId;
	boolean isRequired;
	
	protected List<FormItemBase> children;
	
	public String getFormItemId() {
		return formItemId;
	}
	public void setFormItemId(String formItemId) {
		this.formItemId = formItemId;
	}
	
	public abstract String getTitle();
	public abstract String getSubTitle();
	
	public float getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(float orderNo) {
		this.orderNo = orderNo;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public List<FormItemBase> getChildren() {
		return children;
	}
	public void setChildren(List<FormItemBase> children) {
		this.children = children;
	}

	public abstract FormItemTypeEnum getType();
}
