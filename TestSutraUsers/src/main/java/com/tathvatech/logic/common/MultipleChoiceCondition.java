/*
 * Created on Jan 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.common;

import java.util.Iterator;
import java.util.List;

import com.tathvatech.common.common.DataTypes;
import com.tathvatech.site.service.SiteServiceImpl;

import com.tathvatech.survey.common.HasOtherType;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.common.SurveySaveItem;
import com.tathvatech.survey.intf.SurveyItemBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MultipleChoiceCondition extends SimpleCondition
{
    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);
    
    public MultipleChoiceCondition(String subjectId, SurveyDefinition surveyDef)
    {
        super(surveyDef);
		this.setSubjectId(subjectId);
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        SurveyItemBase sItem = surveyDef.getQuestion(subjectId);
        if(sItem != null)
        {
            sb.append(sItem.getIdentifier() + " " + operator + " ");
            if(value == null)
            {
                sb.append(" Value not defined ");
            }
            else
            {
	            int valueInt = Integer.parseInt(value);
	            if(valueInt > 0)
	            {
	                sb.append(((OneDOptionType)sItem).getOptions().getOptionByValue(valueInt).getText());
	            }
	            else
	            {
	                sb.append(((HasOtherType)sItem).getOtherFieldLabel());
	            }
            }
        }
        else
        {
            sb.append(" Invalid condition...");
        }
        return sb.toString();
    }

    /**
     * @return Returns the subjectId.
     */
    public String getSubjectId()
    {
        return super.getSubjectId();
    }
    /**
     * @param subjectId The subjectId to set.
     */
    public void setSubjectId(String subjectId)
    {
        super.setSubjectId(subjectId);
    }

    public String drawConfigurationForm()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Inside drawConfigurationForm for - " + this.toString());
        }
        
        SurveyItemBase subject = this.surveyDef.getQuestion(getSubjectId());
        MultipleChoiceType tItem = (MultipleChoiceType)subject;
        int dataType = ((SurveySaveItem)tItem).getDataType();
        List operators = DataTypes.getOperators(dataType);
        
        StringBuffer sb = new StringBuffer();
        
        sb.append("<!-- Multiple Choice Condition Start -->\n");
		sb.append("<TABLE cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		sb.append("<TR>");
		
		sb.append("<TD valign=\"top\">");
        sb.append("<SELECT id=\"required\" NAME=\"operator\" SIZE=\"1\">\n");
        for (Iterator iter = operators.iterator(); iter.hasNext();)
        {
            String choice = (String) iter.next();
            sb.append("<option value=\""+ choice + "\" ");
            if(choice.equals(operator))
            {
                sb.append("selected ");
            }
            sb.append(">" + choice + "</option>\n");  
        }
        sb.append("</SELECT>");
		sb.append("&nbsp;</TD>");

		sb.append("<TD valign=\"top\">&nbsp;");
		sb.append("<select name=\"value\" id=\"required\" >\n");
        OptionList options = tItem.getOptions();
		for (int i=0; i<options.size(); i++)
        {
			Option aOption = (Option)options.getOptionByIndex(i);
			String optionString = aOption.getText();
			int optionValue = aOption.getValue();

            sb.append("<option VALUE=\""+ optionValue + "\" ");
            if(Integer.toString(optionValue).equals(getValue()))
            {
                sb.append("selected ");
            }
            sb.append(">" + optionString + "</option>\n");  
        }

		//add the other option if applicable
        if(tItem instanceof HasOtherType && ((HasOtherType)tItem).isAddOtherField())
        {
            sb.append("<option VALUE=\""+ HasOtherType.OTHER_KEY + "\" ");
            if("0".equals(getValue()))
            {
                sb.append("selected ");
            }
            sb.append(">" + ((HasOtherType)tItem).getOtherFieldLabel() + "</option>\n<br><br>");  
        }
        
		sb.append("</TD>");
   		sb.append("</TR>");

		sb.append("</TABLE>");
        sb.append("<!-- Multiple Choice Condition End -->\n");

   		return sb.toString();
    }
}
