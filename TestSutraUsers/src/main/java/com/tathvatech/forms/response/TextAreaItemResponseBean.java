package com.tathvatech.forms.response;


import com.tathvatech.forms.enums.FormItemTypeEnum;
import com.tathvatech.forms.response.FormItemResponseBase;

public class TextAreaItemResponseBean extends FormItemResponseBase {

	String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public FormItemTypeEnum getFormItemType() {
		return FormItemTypeEnum.TextAreaAnswerType;
	}
}
