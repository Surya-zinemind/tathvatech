/*
 * Created on Dec 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataTypes
{
    public static final int DATATYPE_STRING = 1;
    public static final int DATATYPE_INTEGER = 2;
    public static final int DATATYPE_BOOLEAN = 3;
    public static final int DATATYPE_DATE = 4;
    public static final int DATATYPE_EMAIL = 5;
    
    public static final String OPERATOR_EQUAL = "is";
    public static final String OPERATOR_NOTEQUAL = "is not";
    public static final String OPERATOR_STARTSWITH = "starts with";
    public static final String OPERATOR_CONTAINS = "contains";
    public static final String OPERATOR_GT = "is greater than";
    public static final String OPERATOR_LT = "is less than";
    
    /**
     * returns the list of operators for the datatype
     * @param dataType
     * @return
     */
    public static List getOperators(int dataType)
    {
        List rList = new ArrayList();
        if(dataType == DATATYPE_STRING || dataType == DATATYPE_EMAIL)
        {
            rList.add(OPERATOR_EQUAL);
            rList.add(OPERATOR_STARTSWITH);
            rList.add(OPERATOR_CONTAINS);
        }
        else if(dataType == DATATYPE_INTEGER)
        {
            rList.add(OPERATOR_EQUAL);
            rList.add(OPERATOR_NOTEQUAL);
            rList.add(OPERATOR_GT);
            rList.add(OPERATOR_LT);
        }
        else if(dataType == DATATYPE_DATE)
        {
            rList.add(OPERATOR_EQUAL);
            rList.add(OPERATOR_NOTEQUAL);
            rList.add(OPERATOR_GT);
            rList.add(OPERATOR_LT);
        }
        return rList;
    }

    /**
     * @param operator
     * @return
     */
    public static String getDBOperatorSymbol(String operator)
    {
        if(OPERATOR_EQUAL.equals(operator))
        {
            return "=";
        }
        else if(OPERATOR_NOTEQUAL.equals(operator))
        {
            return "!=";
        }
        else if(OPERATOR_GT.equals(operator))
        {
            return ">";
        }
        else if(OPERATOR_LT.equals(operator))
        {
            return "<";
        }
        else if(OPERATOR_STARTSWITH.equals(operator))
        {
            return " like ";
        }
        else if(OPERATOR_CONTAINS.equals(operator))
        {
            return " like ";
        }
        else 
            return "";
    }
    
    public static String getValuePrefix(String operator)
    {
        if(OPERATOR_CONTAINS.equals(operator))
        {
            return "%";
        }
        else 
            return "";
    }

    public static String getValueSufix(String operator)
    {
        if(OPERATOR_STARTSWITH.equals(operator))
        {
            return "%";
        }
        else if(OPERATOR_CONTAINS.equals(operator))
        {
            return "%";
        }
        else 
            return "";
    }
}
