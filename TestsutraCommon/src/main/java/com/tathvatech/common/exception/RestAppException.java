package com.tathvatech.common.exception;




public class RestAppException extends Exception
{

	private static final long serialVersionUID = 1L;

	public RestAppException()
	{
	}

	public RestAppException(String errorMessage)
	{
		super(errorMessage);
	}



}
