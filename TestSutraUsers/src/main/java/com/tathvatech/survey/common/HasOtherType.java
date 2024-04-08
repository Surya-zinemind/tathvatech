/*
 * Created on Mar 22, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;


import net.sf.jsqlparser.statement.ExplainStatement;

/**
 * @author Hari
 *
 * This interface denotes that the question type has an Add other option which adds Other as an option and a textfield to enter the value.
 */
public interface HasOtherType extends ExplainStatement.OptionType
{
	public static final String EXPORT_OTHER_VAL = "0"; //user is Format2 Exportor. (need to show 0 as the value if other option is selected)
    public static final int OTHER_KEY = -1;
    public static final String OTHER = "Other";
	public static final String OTHER_VAL = "otherValue";
    
    public boolean isAddOtherField();
    public String getOtherFieldLabel();
    public String getOtherFieldType(); 
}
