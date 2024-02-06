/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.ts.caf.DateFormats;
import com.tathvatech.ts.core.UserBase;
import com.tathvatech.ts.core.common.EntitySelectorItem;
import com.tathvatech.ts.core.sites.Site;
import com.tathvatech.ts.core.sites.SiteCache;
import com.tathvatech.ts.core.sites.SiteOID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="TAB_USER")
public class User extends AbstractEntity implements UserBase, Serializable, EntitySelectorItem
{
	public static String AttachContext_ProfilePic = "ProfilePic";

	@Id
	private long pk;
	private int accountPk;
	private int sitePk;
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
	private String email;
	private Date lastUpdated;
	
	private HashMap accountData = new HashMap();


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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
		if (userName != null){
			this.userName = userName.trim();
		}
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

	public String getDateFormat()
	{
		return DateFormats.ddMMyyyy.getFormat();
	}

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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		if (email != null){
			this.email = email.trim();
		}

	}

	@JsonIgnore

	public String getSiteName()
	{
		Site site = SiteCache.getInstance().getSite(new SiteOID(this.getSitePk(), null));
		return site.getName();
	}
	
	@JsonIgnore

	public String getDisplayString()
	{
		Site site = SiteCache.getInstance().getSite(new SiteOID(this.getSitePk(), null));
		String siteName = "NA";
		if(site != null)
			siteName = site.getName();
		return firstName + " " + lastName + " / " + siteName +  ((User.STATUS_INACTIVE.equals(status))?" (Inactive)":"");
	}

	@JsonIgnore
	public String getDisplayStringForPrints()
	{
		Site site = SiteCache.getInstance().getSite(new SiteOID(this.getSitePk(), null));
		String siteName = "NA";
		if(site != null)
			siteName = site.getName();
		return firstName + " " + lastName + " / " + siteName;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public static String[] getAdminCreatableUserTypes()
	{
		return new String[]{User.USER_MANAGER, User.USER_ENGINEER, User.USER_TECHNICIAN, User.USER_GUEST, User.USER_OPERATOR, User.USER_TSSUPPORT};
	}

	//The user types that are shown when a user tries to submit a request for creating a new user.
	public static String[] getUserCreateRequestableUserTypes()
	{
		return new String[]{User.USER_MANAGER, User.USER_ENGINEER, User.USER_TECHNICIAN, User.USER_GUEST, User.USER_OPERATOR};
	}

	public static final String USER_PRIMARY = "Administrator";
	public static final String USER_MANAGER = "Manager";
	public static final String USER_ENGINEER = "Engineer";
	public static String USER_TECHNICIAN = "Technician";
	public static String USER_GUEST = "Readonly User";
	public static String USER_OPERATOR = "Operator";
	public static String USER_TSSUPPORT = "TS Support";

	public static final String STATUS_ACTIVE = "Active";
	public static String STATUS_INACTIVE = "Inactive";
	public static final String STATUS_DELETED = "Deleted";
	
	public static String ROLE_TESTER = "Test Engineer";
	public static String ROLE_VERIFY = "Verifier";
	public static String ROLE_APPROVE = "Approver";
	public static String ROLE_READONLY = "Readonly";
	
    /**
     * 
     */
    public User()
    {
    }

    @JsonIgnore
    public UserOID getOID()
    {
    	return new UserOID(pk, getDisplayString());
    }
    

    public void putAccountDate(String key, String value)
    {
    	accountData.put(key, value);
    }
    

    public String getAccountData(String key)
    {
    	return (String)accountData.get(key);
    }

	@Override
	public boolean equals(Object obj)
	{
		User u = (User)obj;
		if(u != null && this.getPk() == u.getPk())
		{
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() 
	{
		return (int) getPk();
	}

	@Override
	public String toString() 
	{
		return getDisplayString();
	}

	@Override
	public String getShortString() {
		return getDisplayString();
	}

	@Override
	public String getLongString() {
		return getDisplayString();
	}
    
}
