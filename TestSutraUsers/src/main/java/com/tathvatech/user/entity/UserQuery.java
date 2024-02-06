/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import com.tathvatech.user.OID.UserOID;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class UserQuery implements Serializable, Comparable<UserQuery>
{
	private int pk;
	private int accountPk;
	private int sitePk;
	private String siteName;
	private String userType;
	private String status;
	private String userName;
	private String password;
	private String passPin;
	private String timezone;
//	private String dateFormat;
	private Date createDate;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String avatar;
	private String email;
	private Date lastUpdated;
	
	private HashMap accountData = new HashMap();
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getAccountPk()
	{
		return accountPk;
	}

	public void setAccountPk(int accountPk)
	{
		this.accountPk = accountPk;
	}

	public int getSitePk() {
		return sitePk;
	}

	public void setSitePk(int sitePk) {
		this.sitePk = sitePk;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPassPin()
	{
		return passPin;
	}

	public void setPassPin(String passPin)
	{
		this.passPin = passPin;
	}

	public String getTimezone()
	{
		return timezone;
	}

	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}

//	public String getDateFormat()
//	{
//		return dateFormat;
//	}
//
//	public void setDateFormat(String dateFormat)
//	{
//		this.dateFormat = dateFormat;
//	}
//
	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

    /**
     * 
     */
    public UserQuery()
    {
    }

	public String getDisplayString()
	{
		return firstName + " " + lastName + " / " + siteName +  ((User.STATUS_INACTIVE.equals(status))?" (Inactive)":"");
	}


    public void putAccountDate(String key, String value)
    {
    	accountData.put(key, value);
    }
    

    public String getAccountData(String key)
    {
    	return (String)accountData.get(key);
    }

    public UserOID getOID()
    {
    	return new UserOID(pk, getDisplayString());
    }
    
	@Override
	public boolean equals(Object obj)
	{
		UserQuery in = (UserQuery)obj;
		if(in != null && this.getPk() == in.getPk())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
    
	static String sql = "select u.*, concat(u.firstName, ' ' , u.lastName)as fullName, site.name as siteName from TAB_USER u, site where u.sitePk = site.pk";

	
	@Override
	public int compareTo(UserQuery o)
	{
		return o.getFullName().compareTo(fullName);
	}
    
}
