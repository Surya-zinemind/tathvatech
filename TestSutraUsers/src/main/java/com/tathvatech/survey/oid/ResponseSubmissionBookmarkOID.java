package com.tathvatech.survey.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;


public class ResponseSubmissionBookmarkOID extends OID {

	public ResponseSubmissionBookmarkOID()
	{
		super();
	}
	
	@JsonCreator
	public ResponseSubmissionBookmarkOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.ResponseSubmissionBookmark, null);
	}
	
	public ResponseSubmissionBookmarkOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.ResponseSubmissionBookmark, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((ResponseSubmissionBookmarkOID)obj).getPk() == getPk())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return (int) super.getPk();
	}

}
