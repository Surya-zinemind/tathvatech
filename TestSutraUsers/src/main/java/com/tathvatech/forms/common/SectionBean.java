package com.tathvatech.forms.common;

import com.tathvatech.forms.enums.FormItemTypeEnum;

import java.util.ArrayList;


public class SectionBean extends FormItemBase{

	String itemNo;
	String description;
    String instructionFileName;
    String instructionFileDisplayName;
	
	public SectionBean()
	{
		children = new ArrayList();
	}

	public String getItemNo() {
		return itemNo;
	}
	public String getTitle(){
		return itemNo;
	}
	public String getSubTitle(){
		return description;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstructionFileName()
	{
		return instructionFileName;
	}

	public void setInstructionFileName(String instructionFileName)
	{
		this.instructionFileName = instructionFileName;
	}

	public String getInstructionFileDisplayName()
	{
		return instructionFileDisplayName;
	}

	public void setInstructionFileDisplayName(String instructionFileDisplayName)
	{
		this.instructionFileDisplayName = instructionFileDisplayName;
	}

	@Override
	public FormItemTypeEnum getType() {
		return FormItemTypeEnum.Section;
	}
	
}
