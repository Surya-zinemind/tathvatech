package com.tathvatech.common.common;

import java.util.List;

public class QueryObject
{
	private String query;
	private List<Object> params;

	public QueryObject()
	{
		
	}
	
	public QueryObject(String query, List<Object> params)
	{
		this.query = query;
		this.params = params;
	}
	
	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public List<Object> getParams()
	{
		return params;
	}

	public void setParams(List<Object> params)
	{
		this.params = params;
	}

}
