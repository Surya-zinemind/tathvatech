/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;


import com.tathvatech.user.common.Option;
import com.tathvatech.user.utils.OptionList;
import com.tathvatech.forms.common.ExpectedNumericValue;
import com.tathvatech.forms.common.FormDesignListener;

import com.tathvatech.survey.response.SurveyItemResponse;


/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface BaseInspectionLineItemAnswerType
{
	public String getSurveyItemId();
	
	public int getDataType();
	public String getUnit();
	public boolean isShowPassFail();
	public boolean isShowNotApplicable();
	public boolean isShowActualValue();
	public boolean isShowComments();
	public String getExpectedValue();
	public ExpectedNumericValue getExpectedUpperLower();
	public void setImageAttachCol(String columnName);
	public Section getEnclosingSection();
	
	public OptionList getCustomFields();
	public Option[] getResponseFieldOptions();
	public Option[] getActualFieldOptions();
	public Option[] getAdditionalMandatoryFieldOptions();

	 public void addItemToDesignViewInt(Table table, boolean isPreviewMode, String addAfterItemId, FormDesignListener formDesignListener);
	 public void drawDesignViewInt(String tableRowId, boolean isPreviewMode, FormDesignListener formDesignListener);

	public InspectionLineItemAnswerStatus getAnswerStatus(SurveyItemResponse sItemResponse);

}
