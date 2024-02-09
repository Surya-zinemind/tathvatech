/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

@Entity
@Table(name = "TAB_ACCOUNT")
public class Account extends AbstractEntity implements AccountBase, Serializable
{
	@Id
	private long pk;
	private String accountNo;
	private String status;
	private String organizationName;
	private Date signUpDate;
	private Date activeDate;
	private Date cancelDate;
	private String currencyCode;
	private String actualCountry;
	private String defaultLanguage;
	private String timeZone;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public String getAccountNo()
	{
		return accountNo;
	}

	public void setAccountNo(String accountNo)
	{
		this.accountNo = accountNo;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getOrganizationName()
	{
		return organizationName;
	}

	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}

	public Date getSignUpDate()
	{
		return signUpDate;
	}

	public void setSignUpDate(Date signUpDate)
	{
		this.signUpDate = signUpDate;
	}

	public Date getActiveDate()
	{
		return activeDate;
	}

	public void setActiveDate(Date activeDate)
	{
		this.activeDate = activeDate;
	}

	public Date getCancelDate()
	{
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate)
	{
		this.cancelDate = cancelDate;
	}

	public String getCurrencyCode()
	{
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode)
	{
		this.currencyCode = currencyCode;
	}

	public String getActualCountry()
	{
		return actualCountry;
	}

	public void setActualCountry(String actualCountry)
	{
		this.actualCountry = actualCountry;
	}

	public String getDefaultLanguage()
	{
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage)
	{
		this.defaultLanguage = defaultLanguage;
	}

	public String getTimeZone()
	{
		return timeZone;
	}

	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}


	@JdbcTypeCode(SqlTypes.JSON)
	private HashMap accountData = new HashMap();
	
    /**
     * 
     */
    public Account()
    {
        super();
    }

    public void putAccountDate(String key, String value)
    {
    	accountData.put(key, value);
    }
    

    public String getAccountData(String key)
    {
    	return (String)accountData.get(key);
    }
    
    public static final String TYPE_Admin = "Admin";
    public static final String TYPE_REGULAR = "Regular";
    public static final String TYPE_TRIAL = "Trial";
	public static final String TYPE_FREEACCOUNT = "Free";

}
