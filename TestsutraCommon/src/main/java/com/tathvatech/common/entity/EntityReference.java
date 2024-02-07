package com.tathvatech.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reference")
public class EntityReference extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private Integer referenceFromType;
	private long referenceFromPk;
	private Integer referenceToType;
	private long referenceToPk;
	private String context;
	private long createdBy;
	private Date createdDate;

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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

	public long getReferenceFromPk() {
		return referenceFromPk;
	}

	public void setReferenceFromPk(long referenceFromPk) {
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

	public long getReferenceToPk() {
		return referenceToPk;
	}

	public void setReferenceToPk(long referenceToPk) {
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

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
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
