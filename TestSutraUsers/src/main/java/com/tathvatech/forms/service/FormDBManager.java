package com.tathvatech.forms.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.project.FormMainOID;
import com.tathvatech.ts.core.project.FormOID;
import com.tathvatech.ts.core.survey.Survey;
import com.tathvatech.ts.core.survey.SurveyDefinition;
import com.tathvatech.ts.core.survey.surveyitem.SurveyItemBase;
import com.thirdi.surveyside.survey.surveyitem.Section;
import com.thirdi.surveyside.wizard.xml.SurveyDefinitionManager;

public class FormDBManager
{

	public void addSurveyToDB(UserContext context, Survey survey) throws Exception
	{
		SurveyDefinition sd = new SurveyDefinitionManager(survey).getSurveyDefinition();
		List<SurveyItemBase> sections = sd.getFormSections();
		
		for (Iterator iterator = sections.iterator(); iterator.hasNext();)
		{
			Section aSec = (Section) iterator.next();
			String itemNo = aSec.getPageTitle();
			String desc = aSec.getDescription();
			
			FormSectionMain sectionMain = getFormSectionMain(new FormMainOID(survey.getFormMainPk()), itemNo, desc);
			if(sectionMain == null)
			{
				sectionMain = createFormSectionMain(context, new FormMainOID(survey.getFormMainPk()), itemNo, desc);
			}
			
			FormSection sec = new FormSection();
			sec.setCreatedDate(new Date());
			sec.setFormPk(survey.getPk());
			sec.setFormSectionMainPk(sectionMain.getPk());
			sec.setOrderNo(aSec.getOrderNum());
			sec.setSectionId(aSec.getSurveyItemId());
			sec.setInstructionFileName(aSec.getInstructionFileName());
			sec.setInstructionFileDisplayName(aSec.getInstructionFileDisplayName());

			PersistWrapper.createEntity(sec);
		}
	}

	private FormSectionMain createFormSectionMain(UserContext context, FormMainOID formMainOID, String itemNo, String description) throws Exception
	{
		FormSectionMain sm = new FormSectionMain();
		sm.setCreatedDate(new Date());
		sm.setFormMainPk(formMainOID.getPk());
		sm.setItemNo(itemNo);
		sm.setDescription(description);
		int pk = PersistWrapper.createEntity(sm);
		return PersistWrapper.readByPrimaryKey(FormSectionMain.class, pk);
	}

	private FormSectionMain getFormSectionMain(FormMainOID formMainOID, String itemNo, String description)
	{
		return PersistWrapper.read(FormSectionMain.class, "select * from form_section_main where formMainPk = ? and itemNo = ? and description = ?", 
				formMainOID.getPk(), itemNo, description);
	}

	public FormSection getFormSection(String surveyItemId, int formPk)
	{
		return PersistWrapper.read(FormSection.class, "select * from form_section where formPk = ? and sectionId = ?",  formPk, surveyItemId);
	}

	public List<FormSection> getFormSections(FormOID formOID)
	{
		return PersistWrapper.readList(FormSection.class, "select * from form_section where formPk = ?",  formOID.getPk());
	}
}
