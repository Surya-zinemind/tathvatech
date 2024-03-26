package com.tathvatech.report.request;


import com.tathvatech.report.enums.ReportTypes;

public class ReportRequest
{
	public static enum SortOrder{asc, dsc};
	
	private ReportTypes reportType;
	private boolean fetchRowCount;
	private ReportFilter filter;
	private long startIndex;
	private boolean fetchAllRows = false; // if this is set to true, rowsToFetch will be ignored.
	private int rowsToFetch;
	
	public ReportRequest(ReportTypes reportType)
	{
		this.reportType = reportType;
	}
	
	public static ReportRequest getSimpleReportRequestForFetchAllRows(ReportTypes reportType, ReportFilter filter)
	{
		ReportRequest rr = new ReportRequest(reportType);
		rr.setFetchAllRows(true);
		rr.setFilter(filter);
		
		return rr;
	}
	public ReportTypes getReportType()
	{
		return reportType;
	}

	public void setReportType(ReportTypes reportType)
	{
		this.reportType = reportType;
	}

	public boolean isFetchRowCount()
	{
		return fetchRowCount;
	}

	public void setFetchRowCount(boolean fetchRowCount)
	{
		this.fetchRowCount = fetchRowCount;
	}

	public ReportFilter getFilter()
	{
		return filter;
	}
	public void setFilter(ReportFilter filter)
	{
		this.filter = filter;
	}
	public long getStartIndex()
	{
		return startIndex;
	}
	public void setStartIndex(long startIndex)
	{
		this.startIndex = startIndex;
	}
	public boolean getFetchAllRows()
	{
		return fetchAllRows;
	}
	public void setFetchAllRows(boolean fetchAllRows)
	{
		this.fetchAllRows = fetchAllRows;
	}
	public int getRowsToFetch()
	{
		return rowsToFetch;
	}
	public void setRowsToFetch(int rowsToFetch)
	{
		this.rowsToFetch = rowsToFetch;
	}
}
