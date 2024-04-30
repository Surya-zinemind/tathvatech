package com.tathvatech.user.repository;

import com.tathvatech.user.controller.AccountController;
import com.tathvatech.user.entity.UserQuery;
import org.apache.commons.collections4.map.ReferenceMap;
import org.springframework.stereotype.Service;



public class UserRepository {

	private static UserRepository instance;
	private static long EXPIRY = 1000*60*15; //15 mins
	private boolean isReportRunning = false;
	
	private ReferenceMap<Integer, UserQuery> map;
	
	private UserRepository()
	{
		map = new ReferenceMap<>();
	}
	
	public static UserRepository getInstance()
	{
		if(instance == null)
		{
			synchronized (UserRepository.class) 
			{
				if(instance == null)
				{
					instance = new UserRepository();
				}
			}
		}
		
		return instance;
	}
	
	public UserQuery getUser(int userPk)
	{
		UserQuery u = map.get(userPk);
		if(u == null)
		{
//			u = AccountController.getUserQuery(userPk);
			if(u == null)
				return null;
			
			map.put(userPk, u);
		}
		return u;
	}
}
