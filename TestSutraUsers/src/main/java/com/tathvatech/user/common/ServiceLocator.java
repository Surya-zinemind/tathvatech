/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.user.common;


import com.tathvatech.common.common.EnvironmentInterface;
import com.tathvatech.common.common.ServiceAdaptor;

public class ServiceLocator
{
	public static EnvironmentInterface locate()
    {
        return ServiceAdaptor.getInstance();
    }
}
