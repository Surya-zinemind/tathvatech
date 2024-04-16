package com.tathvatech.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.ts.core.survey.Option;


/**
 * @author Hari
 * A wrapper class for arraylist which is used for list of options
 */
public class OptionList
{
	private List list;
	
	public OptionList()
	{
		this.list = new ArrayList();
	}
	
	public OptionList(List list)
	{
		this.list = list;
	}
	
	public int size()
	{
		return list.size();
	}
	
	public Iterator iterator()
	{
		return list.iterator();
	}
	
	public void add(Option aOption)
	{
		list.add(aOption);
	}
	
	public void removeByName(String name)
	{
		for (int i=0; i<list.size(); i++)
		{
			Option aOption = (Option) list.get(i);
			if(name.equalsIgnoreCase(aOption.getText()))
			{
				list.remove(i);
				break;
			}
		}
	}
	
	public Option getOptionByIndex(int i)
	{
		return (Option)list.get(i);
	}
	
	public Option getOptionByName(String name)
	{
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();
			if(name.equalsIgnoreCase(aOption.getText()))
			{
				return aOption;
			}
		}
		return null;
	}


	public Option getOptionByValue(int value)
	{
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();
			if(aOption.getValue() == value)
			{
				return aOption;
			}
		}
		return null;
	}
}
