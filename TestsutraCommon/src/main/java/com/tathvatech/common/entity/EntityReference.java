package com.tathvatech.common.entity;

import java.util.Date;
import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

@Table(name = "reference")
public class EntityReference
{
	private int pk;
	private Integer referenceFromType;
	private Integer referenceFromPk;
	private Integer referenceToType;
	private Integer referenceToPk;
	private String context;
	private Integer createdBy;
	private Date createdDate;

	@Column(autoGenerated = true)
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public Integer getReferenceFromType()
	{
		return referenceFromType;
	}

	public void setReferenceFromType(Integer referenceFromType)
	{
		this.referenceFromType = referenceFromType;
	}

	public Integer getReferenceFromPk()
	{
		return referenceFromPk;
	}

	public void setReferenceFromPk(Integer referenceFromPk)
	{
		this.referenceFromPk = referenceFromPk;
	}

	public Integer getReferenceToType()
	{
		return referenceToType;
	}

	public void setReferenceToType(Integer referenceToType)
	{
		this.referenceToType = referenceToType;
	}

	public Integer getReferenceToPk()
	{
		return referenceToPk;
	}

	public void setReferenceToPk(Integer referenceToPk)
	{
		this.referenceToPk = referenceToPk;
	}

	public String getContext()
	{
		return context;
	}

	public void setContext(String context)
	{
		this.context = context;
	}

	public Integer getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}


}
