package com.tathvatech.common.utils;

public class Base62Util
{
	private static final char[] DIGITS = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").toCharArray();
		private static final int BASE = 62;
		
	public static String encode(int val) 
	{ 
		StringBuffer sb = new StringBuffer();
		if(val == 0)
		{
			sb.append(DIGITS[0]);
		}
		else
		{
			while(val != 0)
			{
				int inx = val%BASE;
				sb.insert(0, DIGITS[inx]);
				val = val/BASE;
				
			}
		}
	    return sb.toString(); 
	} 
	  
	// Driver code 
	public static void main (String[] args)  
	{ 
		for(int i=0; i<20000; i++)
		{
		    String str = encode(i); 
		    System.out.println(i + ":" + str); 
		}
	} 

	
	
}
