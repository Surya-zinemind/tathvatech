package com.tathvatech.user.security.manager;

import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.security.config.SecurityCredential;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.PlanSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPlanSecurityManager extends PlanSecurityManager
{
	private static final Logger logger = LoggerFactory.getLogger(NoPlanSecurityManager.class);

//	protected NoPlanSecurityManager(AccountService accountService) {
//		super(accountService);
//	}

	public boolean checkAccess(int action, SecurityContext sContext)
	{
		return false;
	}

	@Override
	public void setSecurityCredientials(SecurityCredential credential)
	{
		// nothing to be done here
	}
}
