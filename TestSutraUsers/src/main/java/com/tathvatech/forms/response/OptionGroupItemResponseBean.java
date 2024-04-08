package com.tathvatech.forms.response;


import com.tathvatech.forms.enums.FormItemTypeEnum;
import com.tathvatech.forms.response.FormItemResponseBase;

public class OptionGroupItemResponseBean extends FormItemResponseBase {
	int[] value;
	
	public int[] getValue() {
		return value;
	}

	public void setValue(int[] value) {
		this.value = value;
	}

	@Override
	public FormItemTypeEnum getFormItemType() {
		return FormItemTypeEnum.OptionGroupAnswerType;
	}
}
