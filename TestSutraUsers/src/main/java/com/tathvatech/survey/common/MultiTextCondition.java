/*
 * Created on Jan 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.util.Iterator;
import java.util.List;


import com.tathvatech.common.common.DataTypes;
import com.tathvatech.common.common.Option;
import com.tathvatech.survey.intf.SurveyItemBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MultiTextCondition extends TwoDCondition
{
    private static final Logger logger = LoggerFactory.getLogger(MultiTextCondition.class);
    
    public MultiTextCondition(String subjectId, SurveyDefinition surveyDef)
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
                sb.append(((MultiTextBoxType)sItem).getOptions().getOptionByIndex((key1-1)));
        	}
        	
        	sb.append( " " + operator + " ");
            if(value == null)
            {
                sb.append(" Value not defined ");
            }
            else
            {
                sb.append(value);
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
        MultiTextBoxType tItem = (MultiTextBoxType)subject;
        int dataType = ((SurveySaveItem)tItem).getDataType();
        List operators = DataTypes.getOperators(dataType);
        
        StringBuffer sb = new StringBuffer();
        
        sb.append("<!-- Matrix Choice Condition Start -->\n");
		sb.append("<TABLE cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");

		sb.append("<TR>");
		
		sb.append("<TD colspan=\"2\" valign=\"top\">");
        sb.append("Value of <SELECT id=\"required\" NAME=\"keyId\" SIZE=\"1\">\n");
        for (int i=0; i<tItem.getOptions().size(); i++)
        {
        	Option aOption = tItem.getOptions().getOptionByIndex(i);
            sb.append("<option value=\""+ aOption.getValue() + "\" ");
            if(new Integer(aOption.getValue()).toString().equals(keyId))
            {
                sb.append("selected ");
            }
            sb.append(">" + aOption.getText() + "</option>\n");  
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
		String valueString = "";
		if(value != null)
		{
		    valueString = value;
		}
        sb.append("<INPUT TYPE=\"text\" id=\"f_" + subject.getSurveyItemId() + "-required\" NAME=\"value\" VALUE=\""+valueString+"\" SIZE=\"20\" >");  

		sb.append("</TD>");
   		sb.append("</TR>");

		sb.append("</TABLE>");
        sb.append("<!-- Matrix Condition End -->\n");

   		return sb.toString();
    }
}
