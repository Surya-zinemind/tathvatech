/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.common.common;

import com.tathvatech.ts.caf.core.envr.EnvironmentInterface;

public class ServiceLocator
{
	public static EnvironmentInterface locate()
    {
        return ServiceAdaptor.getInstance();
    }
}
