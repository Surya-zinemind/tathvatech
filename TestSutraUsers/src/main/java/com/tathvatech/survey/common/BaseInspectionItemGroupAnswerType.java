/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;


import com.tathvatech.forms.common.FormDesignListener;
import com.tathvatech.user.utils.OptionList;

/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface BaseInspectionItemGroupAnswerType extends Container
{
	public String getQuestionText();
	
	public String getQuestionTextDescription();
	
	public boolean isShowExpectedField();
	
	public boolean isShowPassFail();
	
	public boolean isShowComments();
	
	public OptionList getCustomFields();

	public void addBomItemToView(BaseInspectionLineItemAnswerType aChild, boolean isPreviewMode, String addAfterItemId, FormDesignListener formDesignListener);
	
	public void updateBomItemInView(BaseInspectionLineItemAnswerType aChild, boolean isPreviewMode, FormDesignListener formDesignListener);
	
}
