/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.common.exception;


import com.tathvatech.common.common.ErrorCode;

public class AppException extends BaseException
{
	ErrorCode errorCode;
	String[] messages;
	String additionalMessage = "emptyMessage";
	String[] additionalMessageArgs;
	
    public AppException()
    {
        super();
    }

    public AppException(String s)
    {
        super(s);
    }
    
    public AppException(String s, ErrorCode errorCode)
    {
        super(s); // we have the message string in the errorCode. But for now I am doing this so that I need not test all cases 
        this.errorCode = errorCode;
    }
    
    public AppException(String[] messages)
    {
    	this.messages = messages;
    }
    
    public AppException(String s, String[] args)
    {
    	super(s, args);
    }

    public AppException(String s, String additionalMessage, String[] args, String[] additionalMessageArgs)
    {
    	super(s, args);
    	
    	this.additionalMessage = additionalMessage;
    	this.additionalMessageArgs = additionalMessageArgs;
    }

    public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getMessages()
    {
    	return messages;
    }
    
    public String getMessage()
    {
    	if(getMessageKey() != null)
    	{
    		return getMessageKey();
    	}
    	else if(messages != null && messages.length > 0)
    		return messages[0];
    	else
    		return "Error processing your request, Please try again later";
    }
    
    public String getAdditionalMessage() 
	{
		return additionalMessage;
	}

	public String[] getAdditionalMessageArgs() 
	{
		return additionalMessageArgs;
	}
}
