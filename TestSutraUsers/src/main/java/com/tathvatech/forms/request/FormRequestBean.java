package com.tathvatech.forms.request;


import com.tathvatech.common.utils.ListStringUtil;

public class FormRequestBean
{

	private int testProcPk;
	private int responseId;
	private String[] sections;
	public int getTestProcPk()
	{
		return testProcPk;
	}
	public void setTestProcPk(int testProcPk)
	{
		this.testProcPk = testProcPk;
	}
	public int getResponseId()
	{
		return responseId;
	}
	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}
	public String[] getSections()
	{
		return sections;
	}
	public void setSections(String[] sections)
	{
		this.sections = sections;
	}
	@Override
	public String toString()
	{
		return "testProcPk:" + ListStringUtil.showString(testProcPk) + "; responseId:"
				+ ListStringUtil.showString(responseId) + "sections:" + ListStringUtil.showString(sections);
	}
	
}
