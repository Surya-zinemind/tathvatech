package com.tathvatech.common.licence;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.tathvatech.common.wrapper.PersistWrapper;

import org.springframework.beans.factory.annotation.Autowired;

public class LicenseManager 
{
	@Autowired
	static PersistWrapper persistWrapper;

	public static int getLicenseCount()throws Exception
	{
		try 
		{
			Context ctx = new InitialContext();
			License l = (License) ctx.getEnvironment().get("license");
			return l.licenseCount;
		} 
		catch (Exception e) 
		{
			throw new Exception("Could not get the valid license count"); 
		}
	}

	public static int getReadOnlyLicenseCount()throws Exception
	{
		try 
		{
			Context ctx = new InitialContext();
			License l = (License) ctx.getEnvironment().get("license");
			return l.readonlyLicenseCount;
		} 
		catch (Exception e) 
		{
			throw new Exception("Could not get the valid read-only license count"); 
		}
	}
	
	public static int getCurrentRegularUserCount() throws Exception
	{
		int userCount = persistWrapper.read(Integer.class, "select count(pk) from TAB_USER where userType in (?,?, ?) and status = ? ",
				User.USER_ENGINEER, User.USER_MANAGER, User.USER_TECHNICIAN, User.STATUS_ACTIVE);
		return userCount;
	}

	public static int getCurrentReadonlyUserCount() throws Exception
	{
		int userCount = PersistWrapper.read(Integer.class, "select count(pk) from TAB_USER where userType = ? and status = ?", "Readonly User", "Active");
		return userCount;
	}
}
