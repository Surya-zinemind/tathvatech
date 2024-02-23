package com.tathvatech.user.entity;

import java.util.Comparator;

public class UserOrderComparator implements Comparator<User>{

	@Override
	public int compare(User arg1, User arg2) 
	{
		try 
		{
			return arg1.getFirstName().compareToIgnoreCase(arg2.getFirstName());
		} catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}

	}

}
