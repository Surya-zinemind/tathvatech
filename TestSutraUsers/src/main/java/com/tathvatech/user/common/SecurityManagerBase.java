package com.tathvatech.user.common;


public interface SecurityManagerBase {

	boolean checkAccess(int action, SecurityContext sContext);

}
