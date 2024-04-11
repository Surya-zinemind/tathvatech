/*
 * Created on Jan 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.common;

import java.util.Date;
import java.util.Iterator;
import java.util.List;


import com.tathvatech.common.common.DataTypes;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.inf.SurveyItemBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TextBoxCondition extends SimpleCondition
{
    private static final Logger logger = LoggerFactory.getLogger(TextBoxCondition.class);
    
    public TextBoxCondition(String subjectId, SurveyDefinition surveyDef)
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
            sb.append(sItem.getIdentifier() + " " + operator + " " + value);
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
    
    public String getValue()
    {
    	String retVal = value;
    	
    	MultiDataTypeQuestionType sItem = (MultiDataTypeQuestionType)getSubject();
    	if(((MultiDataTypeQuestionType)sItem).getDataType() == DataTypes.DATATYPE_DATE)
		{
        	try
        	{
				Date aDate = DataConversionUtil.getDateFromString(value, DataConversionUtil.FORMAT_MEDIUM);
				retVal = new Long(aDate.getTime()).toString();
        	}
        	catch(Exception ex)
        	{
        		if (logger.isDebugEnabled())
				{
        			logger.debug(ex.getMessage(), ex);
				}
        	}
		}
    	return retVal;
    }

    public String drawConfigurationForm()
    {
        SurveyItemBase subject = this.surveyDef.getQuestion(getSubjectId());
        MultiDataTypeQuestionType tItem = (MultiDataTypeQuestionType)subject;
        int dataType = tItem.getDataType();
        List operators = DataTypes.getOperators(dataType);
        if (logger.isDebugEnabled())
        {
            logger.debug("Subject - " + subject.getIdentifier());
            logger.debug("DataType - " + dataType);
            logger.debug("Operators.size - " + operators.size());
        }
        
        StringBuffer sb = new StringBuffer();

		sb.append("<TABLE cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		sb.append("<TR>");
		sb.append("<TD valign=\"top\">");
        sb.append("<SELECT id=\"required\" NAME=\"operator\" SIZE=\"1\">\n");
        for (Iterator iter = operators.iterator(); iter.hasNext();)
        {
            String choice = (String) iter.next();
            sb.append("<option value=\""+ choice + "\"");
            if(choice.equals(operator))
            {
                sb.append(" selected ");
            }
            sb.append(">" + choice + "</option>\n");  
        }
        sb.append("</SELECT>");
		sb.append("&nbsp;</TD>");

		String valueString = "";
		if(value != null)
		{
		    valueString = value;
		}
		sb.append("&nbsp;<TD valign=\"top\">");
		sb.append("<INPUT TYPE=\"text\" id=\"f_" + subject.getSurveyItemId() + "-required\" NAME=\"value\" VALUE=\""+valueString+"\" SIZE=\"20\" >");
		
        //show the date picker
        if(dataType== DataTypes.DATATYPE_DATE)
        {
            sb.append("&nbsp;<img src=\"img/cal-img.gif\" id=\"but_" + subject.getSurveyItemId() + "\" style=\"vertical-align:middle;cursor: pointer;\" title=\"Date selector\" alt=\"Date selector\">\n");
            sb.append("&nbsp;&nbsp;\n");
            sb.append("<script type=\"text/javascript\">\n");
            sb.append("Calendar.setup({\n");
            sb.append("inputField     :    \"f_" + subject.getSurveyItemId() + "-required\",     // id of the input field\n");
            sb.append("ifFormat       :    \"%e %b, %Y\",      // format of the input field\n");
            sb.append("button         :    \"but_" + subject.getSurveyItemId() + "\",  // trigger for the calendar (button ID)\n");
            sb.append("align          :    \"BR\",           // alignment (defaults to \"Bl\")\n");
            sb.append("singleClick    :    true\n");
            sb.append("});\n");
            sb.append("</script>    \n");
        }
		
		sb.append("</TD>");
   		sb.append("</TR>");


		sb.append("</TABLE>");

   		return sb.toString();
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#getIdentifier()
     */
    public String getIdentifier()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("If response to ");
        
        SurveyItemBase subject = this.surveyDef.getQuestion(this.getSubjectId());
        if(subject != null)
        {
            sb.append(subject.getIdentifier());
        }
        else
        {
            sb.append("invalid question, please correct the logic");
        }
        sb.append(" " + this.getOperator() + " " + this.value);
        
        return sb.toString();
    }
}
