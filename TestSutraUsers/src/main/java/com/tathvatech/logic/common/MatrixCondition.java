/*
 * Created on Jan 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.common;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.tathvatech.ts.core.survey.Option;
import com.tathvatech.ts.core.survey.SurveyDefinition;
import com.tathvatech.ts.core.survey.TwoDOptionType;
import com.tathvatech.ts.core.survey.surveyitem.SurveyItemBase;
import com.tathvatech.ts.core.utils.OptionList;
import com.thirdi.surveyside.survey.DataTypes;
import com.thirdi.surveyside.survey.HasOtherType;
import com.thirdi.surveyside.survey.SurveyItem;
import com.thirdi.surveyside.survey.surveyitem.SurveySaveItem;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MatrixCondition extends TwoDCondition
{
    private static final Logger logger = Logger.getLogger(MatrixCondition.class);
    
    public MatrixCondition(String subjectId, SurveyDefinition surveyDef)
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
        	sb.append(sItem.getIdentifier() + " - " );
        	if(keyId != null)
        	{
	            int key1 = Integer.parseInt(keyId);
                sb.append(((TwoDOptionType)sItem).getRowOptions().getOptionByValue(key1).getText());
        	}
        	
        	sb.append( " " + operator + " ");
            if(value == null)
            {
                sb.append(" Value not defined ");
            }
            else
            {
	            int valueInt = Integer.parseInt(value);
                sb.append(((TwoDOptionType)sItem).getColumnOptions().getOptionByValue(valueInt).getText());
            }
        }
        else
        {
            sb.append(" Invalid condition...");
        }
        return sb.toString();
    }

    public String drawConfigurationForm()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Inside drawConfigurationForm for - " + this.toString());
        }
        
        SurveyItemBase subject = this.surveyDef.getQuestion(getSubjectId());
        TwoDOptionType tItem = (TwoDOptionType)subject;
        int dataType = ((SurveySaveItem)tItem).getDataType();
        List operators = DataTypes.getOperators(dataType);
        
        StringBuffer sb = new StringBuffer();
        
        sb.append("<!-- Matrix Choice Condition Start -->\n");
		sb.append("<TABLE cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");

		sb.append("<TR>");
		
		sb.append("<TD colspan=\"2\" valign=\"top\">");
        sb.append("Value of <SELECT id=\"required\" NAME=\"keyId\" SIZE=\"1\">\n");
        for (int i=0; i<tItem.getRowOptions().size(); i++)
        {
        	Option rowOption = tItem.getRowOptions().getOptionByIndex(i);
            String choice = rowOption.getText();
            sb.append("<option value=\""+ rowOption.getValue() + "\" ");
            if(choice.equals(keyId))
            {
                sb.append("selected ");
            }
            sb.append(">" + choice + "</option>\n");  
        }
        sb.append("</SELECT>");
		sb.append("&nbsp;</TD>");

   		sb.append("</TR>");
		
		
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
        OptionList colOptions = tItem.getColumnOptions();
		for (int i=0; i<colOptions.size(); i++)
        {
			Option colOption = colOptions.getOptionByIndex(i);
            sb.append("<option VALUE=\""+ colOption.getValue() + "\" ");
            if(Integer.toString(colOption.getValue()).equals(getValue()))
            {
                sb.append("selected ");
            }
            sb.append(">" + colOption.getText() + "</option>\n");  
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
        sb.append("<!-- Matrix Condition End -->\n");

   		return sb.toString();
    }
}
