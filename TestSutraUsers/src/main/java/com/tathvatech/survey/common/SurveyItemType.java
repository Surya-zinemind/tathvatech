/*
 * Created on Mar 1, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyItemType
{
    private String name;
    private String description;
    private String typeClass;
    private String displayImage;
    
    public SurveyItemType()
    {
    }
    
    public SurveyItemType(String name, String desc, String tClass, String displayImage)
    {
    	this.name = name;
    	this.description = desc;
    	this.typeClass = tClass;
    	this.displayImage = displayImage;
    }
    
    public String getDisplayImage() 
    {
		return displayImage;
	}
	public void setDisplayImage(String displayImage) 
	{
		this.displayImage = displayImage;
	}
	/**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return Returns the typeClass.
     */
    public String getTypeClass()
    {
        return typeClass;
    }
    /**
     * @param typeClass The typeClass to set.
     */
    public void setTypeClass(String typeClass)
    {
        this.typeClass = typeClass;
    }
}
