package com.tathvatech.forms.synchronizer;

import java.util.ArrayList;
import java.util.List;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.user.OID.TestProcOID;

public class ResponseUploadSynchronizer
{
	private static int WAIT_MILLISECONDS = 250;
	
	private static ResponseUploadSynchronizer instance;
	private List<TestProcOID> activeList = new ArrayList<TestProcOID>();
	
	private ResponseUploadSynchronizer()
	{
	}
	
	public static ResponseUploadSynchronizer getInstance()
	{
		if(instance != null)
			return instance;
		else
		{
			synchronized (ResponseUploadSynchronizer.class)
			{
				if(instance != null)
					return instance;
				else
				{
					instance = new ResponseUploadSynchronizer();
					return instance;
				}
			}
		}
	}
	
	//this method gets the call into an infinite loop which checks a list for presence of the TestProcOID which this thread is trying to upload on.
	//if the list contains that oid, it means that another thread is working on the same testproc and the thread sleeps for some time.
	public synchronized void startProcess(TestProcOID testProcOID)
	{
		int attempts = 0;
		while(true)
		{
			if(activeList.contains(testProcOID))
			{
				try
				{
					attempts++;
					if(attempts > 50)
					{
						throw new AppException("Server is too busy processing other requests, Please wait and the data will be uploaded automatically soon.");
					}
					Thread.sleep(WAIT_MILLISECONDS);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				activeList.add(testProcOID);
				break;
			}
		}
	}
	
	public synchronized void processComplete(TestProcOID testProcOID)
	{
		activeList.remove(testProcOID);
	}
}
