package com.tathvatech.forms.common;


import com.tathvatech.forms.enums.FormItemTypeEnum;

public class SignatureCaptureItemBean extends FormItemBase{

	String title;
	String description;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle(){
		return description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public FormItemTypeEnum getType() {
		return FormItemTypeEnum.SignatureCaptureAnswerType;
	}
}
