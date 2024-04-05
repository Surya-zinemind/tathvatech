package com.tathvatech.forms.common;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.ts.core.accounts.User;

import net.sf.persist.annotations.NoTable;

@NoTable
public class EntityReferenceBean implements Serializable
{
	private int pk;
	private OID referenceFromOID;
	private OID referenceToOID;
	private User createdBy;
	private Date createdDate;
	private Object referencedObject;

	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}


	public OID getReferenceFromOID() {
		return referenceFromOID;
	}

	public void setReferenceFromOID(OID referenceFromOID) {
		this.referenceFromOID = referenceFromOID;
	}

	public OID getReferenceToOID() {
		return referenceToOID;
	}

	public void setReferenceToOID(OID referenceToOID) {
		this.referenceToOID = referenceToOID;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Object getReferencedObject() {
		return referencedObject;
	}

	public void setReferencedObject(Object referencedObject) {
		this.referencedObject = referencedObject;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		return (pk == ((EntityReferenceBean) obj).getPk());
	}

}
