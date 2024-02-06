/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;




/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name = "tab_usertypes")
public class UserTypes extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private String name;
	private Date lastUpdated;

	/**
     * 
     */
    public UserTypes()
    {
        super();
    }


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	//Not using in project, remove the code after testing it
	/*public static List<UserTypes> getAllUserTypes() throws Exception
	{
		return PersistWrapper.readList(UserTypes.class, "select * from tab_usertypes");
	}*/

}
