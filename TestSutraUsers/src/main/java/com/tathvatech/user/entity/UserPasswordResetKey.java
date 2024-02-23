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
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Hari
 * saves the user reset password keys sent to the user to reset the password
 * noOfTries denotes how many times user attempted to reset the password after a key was generated
 * estatus is set to deleted if a new key is requested. this ensures that only a max of 1 key with estatus 1 at a time for the user.
 * resetDone denotes if a successful reset was performed using the key.
 * createdDate lets us keep the code valid for 24 hours . after which we dont let them use that key.
 * so to see if a valid key exists, we need to check the estatus and the time lapsed after the createdDate.
 */
@Entity
@Table(name="USER_RESET_PASSWORD_KEY")
public class UserPasswordResetKey extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private long userFk;
	private String verificationCode;
	private Date createdDate;
	private long keySentToUserFk;
	private int noOfTries;
	private int estatus;
	private int resetDone;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public long getUserFk() {
		return userFk;
	}

	public void setUserFk(long userFk) {
		this.userFk = userFk;
	}

	public String getVerificationCode()
	{
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode)
	{
		this.verificationCode = verificationCode;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public long getKeySentToUserFk() {
		return keySentToUserFk;
	}

	public void setKeySentToUserFk(long keySentToUserFk) {
		this.keySentToUserFk = keySentToUserFk;
	}

	public int getNoOfTries()
	{
		return noOfTries;
	}

	public void setNoOfTries(int noOfTries)
	{
		this.noOfTries = noOfTries;
	}

	public int getResetDone()
	{
		return resetDone;
	}

	public void setResetDone(int resetDone)
	{
		this.resetDone = resetDone;
	}

	public int getEstatus()
	{
		return estatus;
	}

	public void setEstatus(int estatus)
	{
		this.estatus = estatus;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

}
