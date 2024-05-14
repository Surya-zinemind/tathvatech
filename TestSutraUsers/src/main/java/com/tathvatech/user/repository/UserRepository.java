package com.tathvatech.user.repository;

import com.tathvatech.user.controller.AccountController;
import com.tathvatech.user.entity.UserQuery;
import com.tathvatech.user.service.AccountService;
import org.apache.commons.collections4.map.ReferenceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
@Repository
public class UserRepository {
	private  final AccountService accountService;

	private static UserRepository instance;
	private static long EXPIRY = 1000*60*15; //15 mins
	private boolean isReportRunning = false;
	private ReferenceMap<Integer, UserQuery> map;
	private UserRepository(AccountService accountService)
	{
        this.accountService = accountService;
        map = new ReferenceMap<>();
	}

	public UserRepository getInstance()
	{
		if(instance == null)
		{
			synchronized (UserRepository.class)
			{
				if(instance == null)
				{
					instance = new UserRepository(accountService);
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
			u =accountService.getUserQuery(userPk);
			if(u == null)
				return null;

			map.put(userPk, u);
		}
		return u;
	}
}
