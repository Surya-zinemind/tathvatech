package com.tathvatech.timetracker.request;

import com.tathvatech.timetracker.common.OpenCheckinListReportResultRow;

import java.util.List;

public class OpenCheckinListReportResult
{
	private List<OpenCheckinListReportResultRow> reportResult;

	public List<OpenCheckinListReportResultRow> getReportResult()
	{
		return reportResult;
	}

	public void setReportResult(List<OpenCheckinListReportResultRow> reportResult)
	{
		this.reportResult = reportResult;
	}
}
