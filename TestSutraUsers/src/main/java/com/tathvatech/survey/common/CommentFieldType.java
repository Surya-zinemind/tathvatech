package com.tathvatech.survey.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommentFieldType 
{
    private String id;
    private String value;

    private static List itemList = new ArrayList();

    /**
     * @param id
     * @param value
     */
    public CommentFieldType(String id, String value)
    {
        this.id = id;
        this.value = value;
        
        itemList.add(this);
    }

    /**
     * @return 
     */
    public String getId()
    {
        return id;
    }
    
    public String getValue()
    {
    	return value;
    }
    
    public static CommentFieldType fromId(String id)
    {
    	for (Iterator iter = itemList.iterator(); iter.hasNext();) 
    	{
			CommentFieldType element = (CommentFieldType) iter.next();
			if(element.getId().equals(id))
			{
				return element;
			}
		}
    	return null;
    }

    /**
     * @return 
     */
    public static List getItemList()
    {
        return itemList;
    }

    public static final CommentFieldType COMMENT_ONELINE = new CommentFieldType("TextField", "Single line text"); 
    public static final CommentFieldType COMMENT_PARAGRAPH = new CommentFieldType("TextArea", "Paragraph"); 
}
