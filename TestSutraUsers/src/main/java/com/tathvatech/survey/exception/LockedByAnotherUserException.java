package com.tathvatech.survey.exception;

import com.tathvatech.ts.core.survey.ObjectLock;


public class LockedByAnotherUserException extends Exception
{

	ObjectLock objectLock;
	public LockedByAnotherUserException(ObjectLock objectLock)
	{
		super();
		this.objectLock = objectLock;
	}
	public ObjectLock getObjectLock()
	{
		return objectLock;
	}
}
