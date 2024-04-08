/*
 * Created on Jan 1, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;




/**
 * @author Hari
 *
 * Item of this class are displayed on the survey form. 
 * (for eg. Page breakes are SurveyItems but not SurveyDisplayItems)
 */
public interface SurveyDisplayItem
{
	public static int NORMAL_MODE = 0;
	public static int DESIGN_MODE = 1;
	
    public String getQuestionText();
    
    public String getQuestionTextDescription();
    
	
//********************************//	
	
    /**
     *Method to check if the SurveyDisplayItem has anything to display.
	 *This check is required since a PageBreak with an empty title need not be displayed
     *@return if the item need to be displayed on the survey form. 
     */
    public boolean hasSomethingToDisplay();

}
