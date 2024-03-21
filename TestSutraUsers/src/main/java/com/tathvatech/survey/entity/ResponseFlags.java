/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import java.io.Serializable;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

@Entity
@Table(name="TAB_RESPONSE_FLAGS")
public class ResponseFlags extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int surveyPk;
	private String flag;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getSurveyPk()
	{
		return surveyPk;
	}
	public void setSurveyPk(int surveyPk)
	{
		this.surveyPk = surveyPk;
	}

	public String getFlag()
	{
		return flag;
	}
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
}
