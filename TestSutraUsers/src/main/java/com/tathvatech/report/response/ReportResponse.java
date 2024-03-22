package com.tathvatech.report.response;

public class ReportResponse
{
	private long totalRows;
	private long startIndex;
	private Object reportData;
	
	public long getTotalRows()
	{
		return totalRows;
	}
	public void setTotalRows(long totalRows)
	{
		this.totalRows = totalRows;
	}
	public long getStartIndex()
	{
		return startIndex;
	}
	public void setStartIndex(long startIndex)
	{
		this.startIndex = startIndex;
	}
	public Object getReportData()
	{
		return reportData;
	}
	public void setReportData(Object reportData)
	{
		this.reportData = reportData;
	}
}
