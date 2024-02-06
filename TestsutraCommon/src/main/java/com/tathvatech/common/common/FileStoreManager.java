package com.tathvatech.common.common;

import java.io.File;



public class FileStoreManager {

	// WARNING.. if test mode is set to true, you should not add new pictures to the application
	// there are places where we check if a file exists, before adding a file. this will fail
	//as the application returns the default file. use this only when testing prints / view pages etc.
	private static boolean testMode = false; 
	
	public static File getFile(String fileHandle)
	{
		File file = new File(ApplicationProperties.getFileRoot()+fileHandle);
		if(testMode == false)
		{
			return file;
		}
		else
		{
			// READ THE WARNING ABOVE
			if(file.exists())
			{
				return file;
			}
			else
			{
				if(fileHandle.endsWith(".pdf") == false)
				{
					return getResourceFile("default_image.jpeg");
				}
				else
				{
					return getResourceFile("invalid_pdf.pdf");
				}
			}			
		}
	}
	
	
//	public static String getAbsoluteFilePath(String fileHandle)
//	{
//		String path = ApplicationProperties.getFileRoot()+fileHandle;
//		return path;
//	}

	public static File getResourceFile(String resourceHandle)
	{
		return new File(ApplicationProperties.getResourceFileRoot()+resourceHandle);
	}
}
