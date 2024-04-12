/*
 * Created on Jan 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.common;

import com.tathvatech.common.common.DataTypes;
import com.tathvatech.site.service.SiteServiceImpl;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.common.SurveySaveItem;
import com.tathvatech.survey.inf.SurveyItemBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;




/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Matrix3DCondition extends ThreeDCondition
{
    private static final Logger logger = LoggerFactory.getLogger(Matrix3DCondition.class);
    
    public Matrix3DCondition(String subjectId, SurveyDefinition surveyDef)
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
                sb.append(((ThreeDOptionType)sItem).getRowOptions().getOptionByValue(key1).getText());
        	}
        	sb.append(" ");
        	
        	if(key2Id != null)
        	{
	            int key2 = Integer.parseInt(key2Id);
                sb.append(((ThreeDOptionType)sItem).getColumnOptions().getOptionByValue(key2).getText());
        	}

        	sb.append( " " + operator + " ");
            if(value == null)
            {
                sb.append(" Value not defined ");
            }
            else
            {
	            int valueInt = Integer.parseInt(value);
                sb.append(((ThreeDOptionType)sItem).getDropDownOptions().getOptionByValue(valueInt).getText());
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
        ThreeDOptionType tItem = (ThreeDOptionType)subject;
        int dataType = ((SurveySaveItem)tItem).getDataType();
        List operators = DataTypes.getOperators(dataType);
        
        StringBuffer sb = new StringBuffer();
        
        sb.append("<!-- Matrix 3D Choice Condition Start -->\n");
		sb.append("<TABLE cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");

		sb.append("<TR>");
		
		sb.append("<TD colspan=\"2\" valign=\"top\">");
        sb.append("Value of <SELECT id=\"required\" NAME=\"keyId\" SIZE=\"1\">\n");
        for (int i=0; i<tItem.getRowOptions().size(); i++)
        {
            sb.append("<option value=\""+ tItem.getRowOptions().getOptionByIndex(i).getValue() + "\" ");
            if(new Integer(tItem.getRowOptions().getOptionByIndex(i).getValue()).toString().equals(keyId))
            {
                sb.append("selected ");
            }
            sb.append(">" + tItem.getRowOptions().getOptionByIndex(i).getText() + "</option>\n");  
        }
        sb.append("</SELECT>&nbsp;&nbsp;");

        
        sb.append("<SELECT id=\"required\" NAME=\"key2Id\" SIZE=\"1\">\n");
        for (int i=0; i<tItem.getColumnOptions().size(); i++)
        {
            Option colOption = tItem.getColumnOptions().getOptionByIndex(i);
            sb.append("<option value=\""+ colOption.getValue() + "\" ");
            if(new Integer(colOption.getValue()).toString().equals(key2Id))
            {
                sb.append("selected ");
            }
            sb.append(">" + colOption.getText() + "</option>\n");  
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

		for (int i=0; i<tItem.getDropDownOptions().size(); i++)
        {
			Option ddOption = tItem.getDropDownOptions().getOptionByIndex(i);
            sb.append("<option VALUE=\""+ ddOption.getValue() + "\" ");
            if(Integer.toString(ddOption.getValue()).toString().equals(getValue()))
            {
                sb.append("selected ");
            }
            sb.append(">" + ddOption.getText() + "</option>\n");  
        }

		sb.append("</TD>");
   		sb.append("</TR>");

		sb.append("</TABLE>");
        sb.append("<!-- Matrix 3D Condition End -->\n");

   		return sb.toString();
    }
}
