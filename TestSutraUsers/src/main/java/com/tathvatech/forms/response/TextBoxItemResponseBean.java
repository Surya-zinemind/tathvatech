package com.tathvatech.forms.response;

import com.tathvatech.forms.enums.FormItemTypeEnum;

import java.util.Objects;

public class TextBoxItemResponseBean extends FormItemResponseBase {

	String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public FormItemTypeEnum getFormItemType() {
		return FormItemTypeEnum.TextBoxAnswerType;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		return Objects.equals(value, ((TextBoxItemResponseBean)obj).value);
	}
}
