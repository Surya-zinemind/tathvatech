package com.tathvatech.forms.service;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.entity.FormSection;
import com.tathvatech.forms.entity.FormSectionMain;
import com.tathvatech.forms.oid.FormMainOID;
import com.tathvatech.survey.common.Section;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.inf.SurveyItemBase;
import com.tathvatech.survey.service.SurveyDefinitionManager;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.OID.FormOID;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class FormDBManager
{
	private final PersistWrapper persistWrapper;

    public FormDBManager(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }

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
			sec.setFormPk((int) survey.getPk());
			sec.setFormSectionMainPk((int) sectionMain.getPk());
			sec.setOrderNo(aSec.getOrderNum());
			sec.setSectionId(aSec.getSurveyItemId());
			sec.setInstructionFileName(aSec.getInstructionFileName());
			sec.setInstructionFileDisplayName(aSec.getInstructionFileDisplayName());

			persistWrapper.createEntity(sec);
		}
	}

	private FormSectionMain createFormSectionMain(UserContext context, FormMainOID formMainOID, String itemNo, String description) throws Exception
	{
		FormSectionMain sm = new FormSectionMain();
		sm.setCreatedDate(new Date());
		sm.setFormMainPk((int) formMainOID.getPk());
		sm.setItemNo(itemNo);
		sm.setDescription(description);
		int pk = (int) persistWrapper.createEntity(sm);
		return (FormSectionMain) persistWrapper.readByPrimaryKey(FormSectionMain.class, pk);
	}

	private FormSectionMain getFormSectionMain(FormMainOID formMainOID, String itemNo, String description)
	{
		return persistWrapper.read(FormSectionMain.class, "select * from form_section_main where formMainPk = ? and itemNo = ? and description = ?",
				formMainOID.getPk(), itemNo, description);
	}

	public FormSection getFormSection(String surveyItemId, int formPk)
	{
		return persistWrapper.read(FormSection.class, "select * from form_section where formPk = ? and sectionId = ?",  formPk, surveyItemId);
	}

	public List<FormSection> getFormSections(FormOID formOID)
	{
		return persistWrapper.readList(FormSection.class, "select * from form_section where formPk = ?",  formOID.getPk());
	}
}
