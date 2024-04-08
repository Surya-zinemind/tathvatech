package com.tathvatech.timetracker.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.site.service.SiteServiceImpl;
import com.tathvatech.timetracker.common.OpenCheckinListReportResultRow;
import com.tathvatech.timetracker.request.OpenCheckinListReportRequest;
import com.tathvatech.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tathvatech.common.enums.EntityTypeEnum;


/**
 * returns a list of tasks where users are currently checked in based on the filter.
 * @author hari
 *
 */
public class OpenCheckinListReport
{
	private static final Logger logger = LoggerFactory.getLogger(OpenCheckinListReport.class);

	private OpenCheckinListReportRequest request;
	private final PersistWrapper persistWrapper;

	public OpenCheckinListReport(OpenCheckinListReportRequest request, PersistWrapper persistWrapper)
	{
		this.request = request;
        this.persistWrapper = persistWrapper;
    }

	public OpenCheckinListReportResult runReport()
	{
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		sb.append("select ");

		sb.append(" wo.pk as workorderPk, wo.workorderNumber as workorderNumber, wotime.shiftInstanceFk ");
		sb.append(" , tUser.pk as userPk, tUser.userName as userName, concat(tUser.firstName, ' ', tUser.lastName) as userFullName ");
		sb.append(", tUserProfile.fileName as userProfilePicFileName ");
		sb.append(",  wotime.startTime as timeslotStartTime, wotime.timeType as timeSlotTimeType ");
		
		sb.append("  from workorder_timeentry wotime ");

		sb.append(" join workorder wo on wotime.workorderFk = wo.pk ");
		sb.append(" join tab_user tUser on wotime.userFk = tUser.pk ");
		sb.append(" left outer join tab_attachment tUserProfile on tUserProfile.objectPk = tUser.pk and tUserProfile.objectType = ? and tUserProfile.attachContext = ?  ");
		
		params.add(EntityTypeEnum.User.getValue());
		params.add(User.AttachContext_ProfilePic);
		
		sb.append(" where ");
		sb.append(" endTime is null ");
		
		if(request.getWorkorders() != null)
		{
			sb.append(" and wo.pk in (");
			String sep = "";
			for (int i = 0; i < request.getWorkorders().length; i++)
			{
				sb.append(sep).append(request.getWorkorders()[i].getPk());
				sep=",";
			}
			
			sb.append(" ) ");
		}
		if(request.getUsers()!= null)
		{
			sb.append(" and wotime.userFk in (");
			String sep = "";
			for (int i = 0; i < request.getUsers().length; i++)
			{
				sb.append(sep).append(request.getUsers()[i].getPk());
				sep=",";
			}
			
			sb.append(" ) ");
		}
		if(request.getProjectOID() != null)
		{
			sb.append(" and wotime.projectFk = ? ");
			params.add(request.getProjectOID().getPk());
		}
		if(request.getWorkstationOID() != null)
		{
			sb.append(" and wotime.workstationFk = ? ");
			params.add(request.getWorkstationOID().getPk());
		}
		if(request.getShiftInstanceOID() != null)
		{
			sb.append(" and wotime.shiftInstanceFk = ? ");
			params.add(request.getShiftInstanceOID().getPk());
		}
		
		logger.info(sb.toString() + " " + Arrays.deepToString(params.toArray()));

		List<OpenCheckinListReportResultRow> rows = persistWrapper
				.readList(OpenCheckinListReportResultRow.class, sb.toString(), params.toArray());
		OpenCheckinListReportResult result = new OpenCheckinListReportResult();
		result.setReportResult(rows);

		return result;
	}

}
