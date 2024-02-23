package com.tathvatech.user.security.manager;

import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.security.config.SecurityCredential;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.PlanSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminUserSecurityManager extends PlanSecurityManager
{
	private static final Logger logger = LoggerFactory.getLogger(AdminUserSecurityManager.class);

//	protected AdminUserSecurityManager(AccountService accountService) {
//		super(accountService);
//	}

	public boolean checkAccess(int action, SecurityContext sContext)
	{
		switch (action) {
		case PlanSecurityManager.FORM_PRINT:
			return true;

		default:
			return true;
		}
	}

	@Override
	public void setSecurityCredientials(SecurityCredential credential)
	{
		// nothing to be done here
	}
}
