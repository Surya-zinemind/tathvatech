package com.tathvatech.common.email;

public class EmailSenderConfig
{
//	public static int emailThreadFrequencyMilliSeconds = 3*1000*60; // check the queue every 3 mins
	public static int emailThreadFrequencyMilliSeconds = 1*1000*60; // check the queue every 1 mins
	public static int lotSize = 100; // try to send this many emails in 1 connect 
	public static int retryMinsWindow = 15; // if an email fails, retry it after this many mins.
	public static int maxEmailTries = 5; // denotes how many times an email will be retried
	public static int emailExpiryWindowMilliseconds = 1000*60*60*12; // 12hours - when emailQueue entry is created, the expiry is set. An email past expiry will not be sent. 
}
