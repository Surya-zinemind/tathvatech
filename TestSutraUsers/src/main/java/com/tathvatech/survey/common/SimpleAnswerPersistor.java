/*
 * Created on Oct 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;


import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.survey.intf.SurveySaveItemBase;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.unit.response.ResponseUnit;
import com.tathvatech.user.utils.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tathvatech.survey.intf.SurveyItemBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleAnswerPersistor implements AnswerPersistor
{

    private static final Logger logger = LoggerFactory.getLogger(SimpleAnswerPersistor .class);
    private SurveyItemResponse answer;
    private SurveySaveItemBase surveyItem;
    
    /**
     * @param
     * @param
     */
    public SimpleAnswerPersistor(SurveySaveItemBase sItem)
    {
        this.surveyItem = sItem;
    }

    /**
     * @param
     * @param answer
     */
    public SimpleAnswerPersistor(SurveySaveItemBase sItem, SurveyItemResponse answer)
    {
        this.surveyItem = sItem;
        this.answer = answer;
    }
    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.AnswerPersistor#getAnswer()
     */
    public Object getAnswer()
    {
        return answer;
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.response.AnswerPersistor#getPersistSql()
     */
    public String[] getPersistSql(long responseId)
    {
        String tableName = ((SurveyItemBase) this.surveyItem).getSurveyDefinition().getSurveyConfig().getDbTable();
        String pTableName = tableName + SurveyDefinition.DB_SUFFIX;

        List returnSqls = new ArrayList();
        List answerItems = answer.getResponseUnits();
        
        for (Iterator iter = answerItems.iterator(); iter.hasNext();)
        {
            StringBuffer colNames = new StringBuffer();
            StringBuffer values = new StringBuffer();
            
            ResponseUnit aUnit = (ResponseUnit) iter.next();
            if(aUnit.getKey1() != ResponseUnit.DEFAULT_VAL)
            {
                colNames.append("key1, ");
                values.append(""+aUnit.getKey1()+", ");
            }
            if(aUnit.getKey2() != ResponseUnit.DEFAULT_VAL)
            {
                colNames.append("key2, ");
                values.append(""+aUnit.getKey2()+", ");
            }
            if(aUnit.getKey3() != ResponseUnit.DEFAULT_VAL)
            {
                colNames.append("key3, ");
                values.append(""+aUnit.getKey3()+", ");
            }
            if(aUnit.getKey4() != null)
            {
                colNames.append("key4, ");
                values.append("'"+aUnit.getKey4()+"', ");
            }
            
            colNames.append("responseId, surveyPk, questionId");
            values.append("'"+responseId+"', '"+ ((SurveyItemBase) surveyItem).getSurveyDefinition().getSurveyConfig().getPk() +"', '" + surveyItem.getSurveyItemId() + "'");
            
            StringBuffer sql = new StringBuffer();
            sql.append("insert into " + pTableName + "(");
            sql.append(colNames);
            sql.append(") values(");
            sql.append(values);
            sql.append(")");
            
            returnSqls.add(sql.toString());
        }
        return (String[])returnSqls.toArray(new String[returnSqls.size()]);
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.response.AnswerPersistor#getDeleteResponseSQL(java.lang.String, java.lang.String)
     */
    public String[] getDeleteResponseSQL(long responseId)
    {
        List returnSqls = new ArrayList();
        
        String deleteSql = Sqls.deleteResponseSQL;
        deleteSql = deleteSql.replaceAll("<responseId>", String.valueOf(responseId));
        deleteSql = deleteSql.replaceAll("<surveyItemId>", surveyItem.getSurveyItemId());
        returnSqls.add(deleteSql);

        return (String[])returnSqls.toArray(new String[returnSqls.size()]);
    }


	@Override
	/**
	 * inserts the answer into the store for the question
	 */
	public void persistAnswer(Connection conn, int responseId)throws Exception
	{
        String tableName = ((SurveyItemBase) this.surveyItem).getSurveyDefinition().getSurveyConfig().getDbTable();
        String pTableName = tableName + SurveyDefinition.DB_SUFFIX;

        List answerItems = answer.getResponseUnits();

        String sql = "insert into " + pTableName + "(key1, key2, key3, key4, responseId, surveyPk, questionId) values (?,?,?,?,?,?,?)";
        
        PreparedStatement pStmt = null;
        try
		{
			pStmt = conn.prepareStatement(sql);
			for (Iterator iter = answerItems.iterator(); iter.hasNext();)
			{
			    ResponseUnit aUnit = (ResponseUnit) iter.next();
			    if(aUnit.getKey1() != ResponseUnit.DEFAULT_VAL)
			    {
			    	pStmt.setInt(1, aUnit.getKey1());
			    }
			    else
			    {
			    	pStmt.setNull(1, java.sql.Types.INTEGER);
			    }
			    if(aUnit.getKey2() != ResponseUnit.DEFAULT_VAL)
			    {
			    	pStmt.setInt(2, aUnit.getKey2());
			    }
			    else
			    {
			    	pStmt.setNull(2, java.sql.Types.INTEGER);
			    }
			    if(aUnit.getKey3() != ResponseUnit.DEFAULT_VAL)
			    {
			    	pStmt.setInt(3, aUnit.getKey3());
			    }
			    else
			    {
			    	pStmt.setNull(3, java.sql.Types.INTEGER);
			    }
			    if(aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
			    {
			    	pStmt.setString(4, aUnit.getKey4());
			    }
			    else
			    {
			    	pStmt.setNull(4, java.sql.Types.VARCHAR);
			    }
			    
			    pStmt.setInt(5, responseId);
			    pStmt.setInt(6, (int) ((SurveyItemBase) surveyItem).getSurveyDefinition().getSurveyConfig().getPk());
			    pStmt.setString(7, surveyItem.getSurveyItemId());
			    
			    pStmt.execute();
			    
			}
		}
		finally
		{
			if(pStmt != null)
				pStmt.close();
		}
	}
}
