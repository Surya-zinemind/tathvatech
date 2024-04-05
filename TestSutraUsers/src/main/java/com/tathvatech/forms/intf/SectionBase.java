package com.tathvatech.forms.intf;

import com.tathvatech.survey.inf.SurveyItemBase;

import java.util.List;


public interface SectionBase {

	String getPageTitle();

	String getDescription();

	void setPageTitle(String pageTitle);

	void setDescription(String description);

	String getFlags();

	void setFlags(String flags);

	void setSurveyItemId(String idString);

	void setOrderNum(float f);

	float getOrderNum();

	void setParent(SurveyItemBase sectionsParent);

	void setChildren(List ChildList);

	String getSurveyItemId();

	List getChildren();

	SurveyItemBase getParent();

}
