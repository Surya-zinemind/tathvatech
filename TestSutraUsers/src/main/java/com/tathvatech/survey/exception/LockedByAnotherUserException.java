package com.tathvatech.survey.exception;


import com.tathvatech.forms.entity.ObjectLock;

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
