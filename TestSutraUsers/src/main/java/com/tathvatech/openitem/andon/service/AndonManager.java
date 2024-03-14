package com.tathvatech.openitem.andon.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.openitem.andon.common.AndonBean;
import com.tathvatech.openitem.andon.entity.Andon;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.workstation.entity.Workstation;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AndonManager
{
	private static final Logger logger = LoggerFactory.getLogger(AndonManager.class);

   private final PersistWrapper persistWrapper;

   @Lazy
   @Autowired
   private  WorkstationService workstationService;

	/*public  Terminal getTerminal(int terminalPk) throws Exception
	{
		return persistWrapper.readByPrimaryKey(Terminal.class, terminalPk);
	}

	public  List<Workstation> getWorkstationsForTerminal(int terminalPk) throws Exception
	{
		return persistWrapper.readList(Workstation.class, "select * from TAB_WORKSTATION where pk in "
				+ "(select workstationPk from TERMINAL_WORKSTATION where terminalPk = ? )  order by TAB_WORKSTATION.orderNo",
				terminalPk);
	}

	public  List<Workstation> getWorkstationsForTerminal(int terminalPk, int projectPk) throws Exception
	{

		return persistWrapper.readList(Workstation.class, "select ws.* from TAB_WORKSTATION ws,  "
				+ "TERMINAL_WORKSTATION tw where ws.pk = tw.workstationPk and tw.terminalPk=? and tw.projectPk = ? order by ws.orderNo",
				terminalPk, projectPk);
	}

	public  List<AndonQuery> getAndonListForWorkstationTerminal(UserContext context, Terminal terminal,
																	  int projectPk, Workstation workstation)
	{
		try
		{
			String sql = AndonQuery.sql
					+ " join TERMINAL_WORKSTATION tw on tw.projectPk = andon.projectPk and tw.workstationPk = andon.workstationPk "
					+ " where 1 = 1 " + " and tw.terminalPk=? and tw.projectPk = ? and tw.workstationPk=? "
					+ " and ANDON.status in (?,?) " + " order by ANDON.createdDate desc";
			return persistWrapper.readList(AndonQuery.class, sql, terminal.getPk(), projectPk, workstation.getPk(),
					Andon.STATUS_OPEN, Andon.STATUS_ATTENDED);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new AppException("Error fetching andons for workstation terminal");
		}
	}

	public  List<AndonQuery> getAndonListForDisplayBoard(UserContext context, Terminal terminal)
	{
		try
		{
			String sql = AndonQuery.sql
					+ " join TERMINAL_WORKSTATION tw on tw.projectPk = andon.projectPk and tw.workstationPk = andon.workstationPk "
					+ " where 1 = 1 " + " and tw.terminalPk=? " + " and ANDON.status = ? "
					+ " order by ANDON.createdDate desc ";

			return persistWrapper.readList(AndonQuery.class, sql, terminal.getPk(), Andon.STATUS_OPEN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new AppException("Error fetching andons for workstation display board");
		}
	}

	public  List<AndonQuery> getAndonListForProject(UserContext context, int projectPk) throws Exception
	{
		String sql = AndonQuery.sql
				+ " where 1 = 1 and ANDON.projectPk = ? and ANDON.status not in ('Deleted') order by ANDON.createdDate desc";
		return persistWrapper.readList(AndonQuery.class, sql, projectPk);
	}

	public static List<AndonQuery> getAndonList(UserContext context, AndonListFilter filter) throws Exception
	{
		AndonListFilterProcessor p = new AndonListFilterProcessor(filter);
		List params = new ArrayList();
		String where = p.getWhereClause(params);

		StringBuffer sql = new StringBuffer(AndonQuery.sql).append(" where 1=1 and ANDON.status != 'Deleted'")
				.append(where);

		sql.append(" order by ANDON.createdDate desc");

		return PersistWrapper.readList(AndonQuery.class, sql.toString(), params.toArray(new Object[params.size()]));
	}

	public  OILStats getAndonOpenCompletedClosedCountForUnit(int unitPk) throws Exception
	{
		Long closedCount = 0l;
		Long completedCount = 0l;
		Long openCount = 0l;

		List<NameValuePair> list = persistWrapper.readList(NameValuePair.class,
				"select status as name, count(*)  as value from ANDON where unitPk = ? and status != 'Deleted' group by status",
				unitPk);

		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			NameValuePair aStatus = (NameValuePair) iterator.next();
			if (Andon.STATUS_OPEN.equals(aStatus.getName().toString()))
			{
				openCount =  (Long)aStatus.getValue();
			} else if (Andon.STATUS_ATTENDED.equals(aStatus.getName().toString()))
			{
				completedCount = (Long) aStatus.getValue();
			} else if (Andon.STATUS_CLOSED.equals(aStatus.getName().toString()))
			{
				closedCount = (Long) aStatus.getValue();
			}
		}
		OILStats s = new OILStats();
		s.setOpenCount(openCount.intValue());
		s.setCompletedCount(completedCount.intValue());
		s.setClosedCount(closedCount.intValue());
		return s;
		// return new long[]{openCount, completedCount, closedCount};
	}

	public  OILStats getAndonOpenCompletedClosedCountForUnit(int unitPk, WorkstationOID workstationOID)
			throws Exception
	{
		Long closedCount = 0l;
		Long completedCount = 0l;
		Long openCount = 0l;

		List<NameValuePair> list = persistWrapper.readList(NameValuePair.class,
				"select status as name, count(*)  as value from ANDON where unitPk = ? and workstationPk = ? and status != 'Deleted' group by status",
				unitPk, workstationOID.getPk());

		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			NameValuePair aStatus = (NameValuePair) iterator.next();
			if (Andon.STATUS_OPEN.equals(aStatus.getName().toString()))
			{
				openCount = (Long) aStatus.getValue();
			} else if (Andon.STATUS_ATTENDED.equals(aStatus.getName().toString()))
			{
				completedCount = (Long) aStatus.getValue();
			} else if (Andon.STATUS_CLOSED.equals(aStatus.getName().toString()))
			{
				closedCount = (Long) aStatus.getValue();
			}
		}
		OILStats s = new OILStats();
		s.setOpenCount(openCount.intValue());
		s.setCompletedCount(completedCount.intValue());
		s.setClosedCount(closedCount.intValue());
		return s;
		// return new long[]{openCount, completedCount, closedCount};
	}

	public  OILStats getProjectAndonStatusASOnDate(int projectPk, Date time) throws Exception
	{
		// add one day to toDate. as the values till end of toDate should be
		// picked.. the query runs <
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.DATE, 1);
		time = cal.getTime();

		Integer openCount = persistWrapper.read(Integer.class,
				"select count(*) from ANDON where " + "projectPk =? and status != 'Deleted' and (createdDate < ?)",
				projectPk, time);
		Integer completedCount = persistWrapper.read(Integer.class,
				"select count(*) from ANDON where " + "projectPk =? and status != 'Deleted' and attendedDate < ?",
				projectPk, time);
		Integer closedCount = persistWrapper.read(Integer.class,
				"select count(*) from ANDON where " + "projectPk =? and status != 'Deleted' and closedDate < ? ",
				projectPk, time);

		OILStats s = new OILStats();
		s.setOpenCount(openCount);
		s.setCompletedCount(completedCount);
		s.setClosedCount(closedCount);

		return s;
	}

	public  OILStats getAndonStatusASOnDate(int unitPk, Date time) throws Exception
	{
		// add one day to toDate. as the values till end of toDate should be
		// picked.. the query runs <
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.DATE, 1);
		time = cal.getTime();

		Integer openCount = persistWrapper.read(Integer.class,
				"select count(*) from ANDON where " + "unitPk=? and status != 'Deleted' and " + "(createdDate < ?)",
				unitPk, time);
		Integer completedCount = persistWrapper.read(Integer.class,
				"select count(*) from ANDON where " + "unitPk=? and status != 'Deleted' and attendedDate < ?", unitPk,
				time);
		Integer closedCount = persistWrapper.read(Integer.class,
				"select count(*) from ANDON where " + "unitPk=? and status != 'Deleted' and closedDate < ? ", unitPk,
				time);

		OILStats s = new OILStats();
		s.setOpenCount(openCount);
		s.setCompletedCount(completedCount);
		s.setClosedCount(closedCount);

		return s;
	}

	public  Andon saveAndon(UserContext context, ProjectQuery projectQuery, Andon andon,
								  List<AttachmentIntf> attachments) throws Exception
	{
		if (projectQuery == null)
			throw new AppException("Invalid Project");
		Workstation ws = null;
		if (andon.getWorkstationPk() != null)
			ws = persistWrapper.readByPrimaryKey(Workstation.class, andon.getWorkstationPk());
		UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(andon.getUnitPk()));
		if (ws == null)
			throw new AppException("Andon should be associated to a valid workstation");
		if (unit == null)
			throw new Exception("Andon should be associated to a valid unit");
		if (andon.getPk() == 0)
		{
			int pk;
			synchronized (AndonNoController.class)
			{
				int andonNoPart = 0;
				String lastAndonNo = persistWrapper.read(String.class,
						"select andonNo from ANDON where andonNo like ? order by pk desc limit 0,1",
						ws.getWorkstationName() + "-" + unit.getUnitName() + "-%");
				try
				{
					andonNoPart = Integer.parseInt(lastAndonNo.substring(lastAndonNo.lastIndexOf('-') + 1));
				}
				catch (Exception ex)
				{
					logger.info("Could not generate the next andon No, starting with suffux 1");
				}
				andon.setAndonNo(ws.getWorkstationName() + "-" + unit.getUnitName() + "-" + (andonNoPart + 1));
				andon.setProjectPk(projectQuery.getPk());
				andon.setCreatedDate(new Date());
				andon.setStatus(Andon.STATUS_OPEN);
				pk = persistWrapper.createEntity(andon);
				List<AttachmentIntf> attachmentlist = new ArrayList<AttachmentIntf>();

				if (attachments != null && attachments.size() > 0)
				{
					for (AttachmentIntf attachmentIntf : attachments)
					{

						attachmentlist.add(attachmentIntf);
					}
				}
				CommonServiceManager.saveAttachments(context, pk, EntityTypeEnum.Andon.getValue(), attachmentlist,
						true);
			}
			return getAndon(pk);
		} else
		{
			persistWrapper.update(andon);
			List<AttachmentIntf> attachmentlist = new ArrayList<AttachmentIntf>();

			if (attachments != null && attachments.size() > 0)
			{
				for (AttachmentIntf attachmentIntf : attachments)
				{

					attachmentlist.add(attachmentIntf);
				}
			}
			CommonServiceManager.saveAttachments(context, andon.getPk(), EntityTypeEnum.Andon.getValue(),
					attachmentlist, true);
			return getAndon(andon.getPk());
		}
	}




	public  Andon markAndonAsAttended(UserContext context, Andon andon, List<AttachmentIntf> attachments)
			throws Exception
	{
		andon.setAttendedDate(new Date());
		andon.setStatus(Andon.STATUS_ATTENDED);
		andon.setAttendedBy(context.getUser().getPk());
		persistWrapper.update(andon);
		List<AttachmentIntf> attachmentlist = new ArrayList<AttachmentIntf>();

		if (attachments != null && attachments.size() > 0)
		{
			for (AttachmentIntf attachmentIntf : attachments)
			{

				attachmentlist.add(attachmentIntf);
			}
		}
		CommonServiceManager.saveAttachments(context, andon.getPk(), EntityTypeEnum.Andon.getValue(), attachmentlist,
				true);
		return getAndon(andon.getPk());
	}*/

	public  Andon markAndonAsClosed(UserContext context, Andon andon, List<AttachmentIntf> attachments)
			throws Exception
	{
		if (andon.getSource() == null || andon.getSource().trim().length() < 1)
		{
			logger.info("Invalid source.\nandon - " + andon.getAndonNo() + " source-" + andon.getSource());
			throw new AppException("Invalid Source information. Please try again later.");
		}
		if (andon.STATUS_OPEN.equals(andon.getStatus()))
		{
			// you are directly closing an andon
			andon.setAttendedBy((int) context.getUser().getPk());
			andon.setAttendedDate(new Date());
		}
		andon.setClosedDate(new Date());
		andon.setClosedBy((int) context.getUser().getPk());
		andon.setStatus(Andon.STATUS_CLOSED);

		persistWrapper.update(andon);
		List<AttachmentIntf> attachmentlist = new ArrayList<AttachmentIntf>();

		if (attachments != null && attachments.size() > 0)
		{
			for (AttachmentIntf attachmentIntf : attachments)
			{

				attachmentlist.add(attachmentIntf);
			}
		}
		CommonServiceManager.saveAttachments(context, (int) andon.getPk(), EntityTypeEnum.Andon.getValue(), attachmentlist,
				true);
		return getAndon(andon.getPk());
	}
	public  Andon getAndon(long andonPk) throws Exception
	{
		return (Andon) persistWrapper.readByPrimaryKey(Andon.class, andonPk);
	}


	public  AndonBean getAndonBean(long andonPk) throws Exception
	{
		AndonBean andonBean = null;
		Andon andon = (Andon) persistWrapper.readByPrimaryKey(Andon.class, andonPk);
		if (andon != null && andon.getPk() > 0)
		{
			andonBean = new AndonBean();
			andonBean.setPk((int) andon.getPk());
			andonBean.setType(andon.getType());
			andonBean.setProjectPk(andon.getProjectPk());
			andonBean.setUnitPk(andon.getUnitPk());
			andonBean.setWorkstationPk(andon.getWorkstationPk());
			andonBean.setAndonTypePk(andon.getAndonTypePk());
			andonBean.setAndonNo(andon.getAndonNo());
			andonBean.setDescription(andon.getDescription());
			andonBean.setAssyPartTestNo(andon.getAssyPartTestNo());
			andonBean.setCarType(andon.getCarType());
			andonBean.setPartNo(andon.getPartNo());
			andonBean.setPartDesc(andon.getPartDesc());
			andonBean.setSupplier(andon.getSupplier());
			andonBean.setReferenceNo(andon.getReferenceNo());
			andonBean.setReferenceCreatedDate(andon.getReferenceCreatedDate());
			andonBean.setEscape(andon.getEscape());
			andonBean.setRootCause(andon.getRootCause());
			andonBean.setForecastCompletionDate(andon.getForecastCompletionDate());
			andonBean.setCustodianPk(andon.getCustodianPk());
			andonBean.setDisposition(andon.getDisposition());
			andonBean.setCategory(andon.getCategory());
			andonBean.setSubcategory(andon.getSubcategory());
			andonBean.setPart(andon.getAssyPartTestNo());
			andonBean.setPartRequired(andon.getPartRequired());
			andonBean.setPriority(andon.getPriority());
			andonBean.setQuantity(andon.getQuantity());
			andonBean.setUnitOfMeasure(andon.getUnitOfMeasure());
			andonBean.setHours(andon.getHours());
			andonBean.setReworkOrder(andon.getReworkOrder());
			andonBean.setResourceRequirement(andon.getResourceRequirement());
			andonBean.setSource(andon.getSource());
			andonBean.setSeverity(andon.getSeverity());
			andonBean.setOccurrence(andon.getOccurrence());
			andonBean.setDetection(andon.getDetection());
			andonBean.setCreatedByInitials(andon.getCreatedByInitials());
			andonBean.setCreatedDate(andon.getCreatedDate());
			andonBean.setAttendedBy(andon.getAttendedBy());
			andonBean.setAttendedDate(andon.getAttendedDate());
			andonBean.setAttendedComment(andon.getAttendedComment());
			andonBean.setClosedBy(andon.getClosedBy());
			andonBean.setClosedDate(andon.getClosedDate());
			andonBean.setClosedComment(andon.getClosedComment());
			andonBean.setStatus(andon.getStatus());
			andonBean.setLastUpdated(andon.getLastUpdated());
			andonBean.setAttachments(
					CommonServiceManager.getAttachments((int) andon.getPk(), EntityTypeEnum.Andon.getValue()));

			//Fix later
			/*List<HazardReferenceBean> mrfReference = HazardMaintanenceManager.getCreatedChildReference(andon.getPk(),
					EntityTypeEnum.Andon, EntityTypeEnum.MRF);
			if (mrfReference != null && mrfReference.size() > 0)
			{
				int mrfFk = 0;
				if (mrfReference.get(0).getReferenceFromType().equals(EntityTypeEnum.MRF.getValue()))
				{
					mrfFk = mrfReference.get(0).getReferenceFromPk();
				} else if (mrfReference.get(0).getReferenceToType().equals(EntityTypeEnum.MRF.getValue()))
				{
					mrfFk = mrfReference.get(0).getReferenceToPk();
				}
				Mrf mrf = persistWrapper.readByPrimaryKey(Mrf.class, mrfFk);
				andonBean.setMrfOID(new MRFOID(mrf.getPk(), mrf.getMrfno()));
			}*/
		}
		return andonBean;

	}

	public  void markAllAndonsForUnitOnWorkstationAsClosed(UserContext context, UnitOID unitOID,
																 ProjectOID projectOID, WorkstationOID workstationOID) throws Exception
	{
		// load tehe list of andons on that unit and workstation and project
		StringBuilder sb = new StringBuilder(
				"select * from ANDON where unitPk = ? and projectPk = ? and workstationPk = ? and status in ( ?, ?) ");
		List<Andon> andonList = persistWrapper.readList(Andon.class, sb.toString(), unitOID.getPk(), projectOID.getPk(),
				workstationOID.getPk(), Andon.STATUS_OPEN, Andon.STATUS_ATTENDED);
		for (Iterator iterator = andonList.iterator(); iterator.hasNext();)
		{
			Andon andon = (Andon) iterator.next();
			if (StringUtils.isBlank(andon.getSource()))
			{
				Workstation ws = workstationService.getWorkstation(new WorkstationOID(andon.getWorkstationPk()));
				andon.setSource("WS" + ws.getWorkstationName());
			}
			andon.setClosedComment("Andon closed as the workstation is complete.");
			AndonBean andonBean = getAndonBean(andon.getPk());

			markAndonAsClosed(context, andon, andonBean.getAttachments());
		}
	}

	/*public  List<AndonType> getAndonTypesForWorkstation(int projectPk, int workstationPk) throws Exception
	{
		return persistWrapper.readList(AndonType.class, "select * from ANDON_TYPE where pk in "
				+ "(select andonTypePk from WORKSTATION_ANDON_TYPE where projectPk = ? and workstationPk=?) and status = ?",
				projectPk, workstationPk, AndonType.STATUS_ACTIVE);
	}

	public  List<AndonType> getAndonTypesForProject(int projectPk) throws Exception
	{
		return persistWrapper.readList(AndonType.class,
				"select * from ANDON_TYPE where pk in "
						+ "(select andonTypePk from WORKSTATION_ANDON_TYPE where projectPk = ?) and status = ?",
				projectPk, AndonType.STATUS_ACTIVE);
	}

	public static Terminal getTerminal(String terminalName) throws Exception
	{
		return PersistWrapper.read(Terminal.class, "select * from TERMINAL where name like ?", terminalName);
	}

	public static Terminal getTerminalBySessionKey(String sessionKey) throws Exception
	{
		return PersistWrapper.read(Terminal.class, "select * from TERMINAL where sessionKey = ?", sessionKey);
	}
	public  String createTerminalSessionKey(UserContext context, Terminal terminal)
	{
		String sessionKey = null;
		if (terminal.getSessionKey() == null || terminal.getSessionKey().trim().length() == 0)
		{
			int rnd = new Random().nextInt();
			sessionKey = new Integer(rnd).toString();

			terminal.setSessionKey(sessionKey);
			persistWrapper.update(terminal);
		} else
		{
			// we are allowing one terminal to be logged in from multiple
			// machines for now.. need to change this
			// to logout one if logged in from another.
			sessionKey = terminal.getSessionKey();
		}
		return sessionKey;
	}

	public  List<Terminal> getTerminalList() throws Exception
	{
		return persistWrapper.readList(Terminal.class, "select * from TERMINAL order by createdDate desc",
				new Object[] {});
	}

	public  void saveTerminal(UserContext context, Terminal terminal) throws Exception
	{
		terminal.setStatus(Terminal.STATUS_ACTIVE);
		if (terminal.getPk() == 0)
		{
			terminal.setCreatedDate(new Date());
			terminal.setCreatedBy(context.getUser().getPk());
			persistWrapper.createEntity(terminal);
		} else
		{
			persistWrapper.update(terminal);
		}
	}

	public static List<AndonType> getAndonTypes() throws Exception
	{
		return PersistWrapper.readList(AndonType.class, "select * from ANDON_TYPE order by createdDate desc",
				new Object[] {});
	}

	public  AndonType getAndonType(int pk) throws Exception
	{
		return persistWrapper.readByPrimaryKey(AndonType.class, pk);
	}

	public  void saveAndonType(UserContext context, AndonType andonType) throws Exception
	{
		andonType.setStatus(AndonType.STATUS_ACTIVE);
		if (andonType.getPk() == 0)
		{
			andonType.setCreatedBy(context.getUser().getPk());
			andonType.setCreatedDate(new Date());
			persistWrapper.createEntity(andonType);
		} else
		{
			persistWrapper.update(andonType);
		}

	}

	public  List<TerminalWorkstation> getTerminalWorkstationList(int terminalPk) throws Exception
	{
		return persistWrapper.readList(TerminalWorkstation.class,
				"select * from TERMINAL_WORKSTATION where terminalPk=? ", terminalPk);
	}

	public  List<Terminal> getTerminalListForWorkstation(int projectPk, int workstationPk) throws Exception
	{
		return persistWrapper.readList(Terminal.class, "select * from TERMINAL where pk in "
				+ "(select terminalPk from TERMINAL_WORKSTATION where projectPk = ? and workstationPk=?) and status = ?",
				projectPk, workstationPk, Terminal.STATUS_ACTIVE);
	}

	public  void setAndonTypesForWorkstation(ProjectQuery projectQuery, int workstationPk,
			Collection andonTypePks) throws Exception
	{
		List<WorkstationAndonType> wTs = PersistWrapper.readList(WorkstationAndonType.class,
				"select * from WORKSTATION_ANDON_TYPE where workstationPk=? and projectPk=?",
				new Object[] { workstationPk, projectQuery.getPk() });
		for (Iterator iterator1 = andonTypePks.iterator(); iterator1.hasNext();)
		{
			int aTypePk = (Integer) iterator1.next();
			boolean alreadyConfigured = false;
			for (Iterator iterator = wTs.iterator(); iterator.hasNext();)
			{
				WorkstationAndonType workstationAndonType = (WorkstationAndonType) iterator.next();
				if (aTypePk == workstationAndonType.getAndonTypePk())
				{
					alreadyConfigured = true;
					wTs.remove(workstationAndonType);
					break;
				}
			}
			if (alreadyConfigured)
			{
				continue; // nothing to be done as it is already set
			} else
			{
				// add this from db
				WorkstationAndonType newT = new WorkstationAndonType();
				newT.setProjectPk(projectQuery.getPk());
				newT.setAndonTypePk(aTypePk);
				newT.setWorkstationPk(workstationPk);
				persistWrapper.createEntity(newT);
			}
		}

		// now what ever remaining inthe wTs need to be removed
		for (Iterator iterator = wTs.iterator(); iterator.hasNext();)
		{
			WorkstationAndonType workstationAndonType = (WorkstationAndonType) iterator.next();
			persistWrapper.deleteEntity(workstationAndonType);
		}
	}

	public  void setTerminalsForWorkstation(ProjectQuery projectQuery, int workstationPk, Collection terminalPks)
			throws Exception
	{
		List<TerminalWorkstation> wTs = PersistWrapper.readList(TerminalWorkstation.class,
				"select * from TERMINAL_WORKSTATION where workstationPk=? and projectPk=?",
				new Object[] { workstationPk, projectQuery.getPk() });
		for (Iterator iterator1 = terminalPks.iterator(); iterator1.hasNext();)
		{
			int aTerminalPk = (Integer) iterator1.next();
			boolean alreadyConfigured = false;
			for (Iterator iterator = wTs.iterator(); iterator.hasNext();)
			{
				TerminalWorkstation workstationTerminal = (TerminalWorkstation) iterator.next();
				if (aTerminalPk == workstationTerminal.getTerminalPk())
				{
					alreadyConfigured = true;
					wTs.remove(workstationTerminal);
					break;
				}
			}
			if (alreadyConfigured)
			{
				continue; // nothing to be done as it is already set
			} else
			{
				// add this from db
				TerminalWorkstation newT = new TerminalWorkstation();
				newT.setTerminalPk(aTerminalPk);
				newT.setProjectPk(projectQuery.getPk());
				newT.setWorkstationPk(workstationPk);
				persistWrapper.createEntity(newT);
			}
		}

		// now what ever remaining inthe wTs need to be removed
		for (Iterator iterator = wTs.iterator(); iterator.hasNext();)
		{
			TerminalWorkstation workstationTerminal = (TerminalWorkstation) iterator.next();
			persistWrapper.deleteEntity(workstationTerminal);
		}
	}

	public  void setWorkstationsForTerminal(Project project, Collection workstationPks, int terminalPk)
			throws Exception
	{
		List<TerminalWorkstation> wTs = PersistWrapper.readList(TerminalWorkstation.class,
				"select * from TERMINAL_WORKSTATION where terminalPk=? and projectPk=?",
				new Object[] { terminalPk, project.getPk() });
		for (Iterator iterator1 = workstationPks.iterator(); iterator1.hasNext();)
		{
			int aWorkstationPk = (Integer) iterator1.next();
			boolean alreadyConfigured = false;
			for (Iterator iterator = wTs.iterator(); iterator.hasNext();)
			{
				TerminalWorkstation workstationTerminal = (TerminalWorkstation) iterator.next();
				if (aWorkstationPk == workstationTerminal.getTerminalPk())
				{
					alreadyConfigured = true;
					wTs.remove(workstationTerminal);
					break;
				}
			}
			if (alreadyConfigured)
			{
				continue; // nothing to be done as it is already set
			} else
			{
				// add this from db
				TerminalWorkstation newT = new TerminalWorkstation();
				newT.setWorkstationPk(aWorkstationPk);
				newT.setProjectPk(project.getPk());
				newT.setTerminalPk(terminalPk);
				persistWrapper.createEntity(newT);
			}
		}

		// now what ever remaining inthe wTs need to be removed
		for (Iterator iterator = wTs.iterator(); iterator.hasNext();)
		{
			TerminalWorkstation workstationTerminal = (TerminalWorkstation) iterator.next();
			persistWrapper.deleteEntity(workstationTerminal);
		}
	}

	public  void deleteAndon(int andonPk) throws Exception
	{
		Andon andon = persistWrapper.readByPrimaryKey(Andon.class, andonPk);
		if (andon == null)
			throw new AppException("Invalid Andon, Delete failed");
		if (Andon.STATUS_CLOSED.equals(andon.getStatus()) || Andon.STATUS_ATTENDED.equals(andon.getStatus()))
			throw new AppException("An andon which is in Attended or Closed status cannot be deleted");
		andon.setStatus(Andon.STATUS_DELETED);
		persistWrapper.update(andon);
	}

	public  List<Project> getProjectsAssignedForTerminal(int terminalPk)
	{
		// we have to makesure that if ther are any project - workstation
		// combinations in the TERMINAL_WORKSTATION is
		// is there for workstation which are not there in the project any more,
		// such projects are not returned in the query
		// that is any the join with TAB_PROJECT_WORKSTATIONS is also there.
		try
		{
			return persistWrapper.readList(Project.class,
					"select * from TAB_PROJECT where TAB_PROJECT.status = ? and pk in "
							+ "(select distinct(p.pk) from TAB_PROJECT p, TAB_PROJECT_WORKSTATIONS pw, "
							+ "TERMINAL_WORKSTATION tw where p.pk = pw.projectPk and pw.projectPk = tw.projectPk "
							+ "and pw.workstationPk = tw.workstationPk and tw.terminalPk = ?)",
					Project.STATUS_OPEN, terminalPk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new AppException("Error fetching projects for terminal");
		}
	}

	public static Andon getAndonByAndonNo(String andonNo)
	{
		try
		{
			List<Andon> as = PersistWrapper.readList(Andon.class, "select * from ANDON where andonNo = ?", andonNo);
			if (as == null || as.size() == 0)
				return null;
			else if (as.size() == 1)
				return as.get(0);
			else
				throw new Exception("More than one andon with the same andon number:" + andonNo);
		}
		catch (Exception e)
		{
			logger.error("Error fetching Andon by andon No", e);
		}
		return null;
	}

	public  List<AndonQuery> getAndonListByPk(UserContext context, AndonListFilter filter, StringBuffer sbText)
			throws Exception
	{
		AndonListFilterProcessor p = new AndonListFilterProcessor(filter);
		List params = new ArrayList();
		String where = p.getWhereClause(params);
		StringBuffer sql;
		if (sbText.length() == 0)
		{
			sql = new StringBuffer(AndonQuery.sql)
					.append(" where 1 = 1 and ANDON.status != 'Deleted' and ANDON.pk in (0)").append(where);
		} else
		{
			sql = new StringBuffer(AndonQuery.sql)
					.append(" where 1 = 1 and ANDON.status != 'Deleted' and ANDON.pk in (" + sbText + ")")
					.append(where);
		}

		sql.append(" order by ANDON.createdDate desc");

		return persistWrapper.readList(AndonQuery.class, sql.toString(), params.toArray(new Object[params.size()]));
	}

	public  List<AndonQuery> getTopThreeRPNAndon(List<ProjectOID> projectOIDs,
			List<WorkstationOID> workstationOIDs, Date createdFrom, Date createdTo, List<Integer> unitPks)
			throws Exception
	{
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer(AndonQuery.sql);
		sql.append(" where 1 = 1 and ANDON.status not in ('Deleted') ");
		sql.append(" and ANDON.status =? ");
		params.add(Andon.STATUS_ATTENDED);

		if (Optional.ofNullable(projectOIDs).isPresent() && projectOIDs.size() > 0)
		{

			sql.append(" and ANDON.projectPk in (");
			String comma = "";
			for (ProjectOID projectOID : projectOIDs)
			{
				sql.append(comma).append("?");
				params.add(projectOID.getPk());
				comma = ",";

			}
			sql.append(" ) ");

		}
		if (Optional.ofNullable(workstationOIDs).isPresent() && workstationOIDs.size() > 0)
		{

			sql.append(" and ANDON.workstationPk in (");
			String comma = "";
			for (WorkstationOID locationOID : workstationOIDs)
			{
				sql.append(comma).append("?");
				params.add(locationOID.getPk());
				comma = ",";

			}
			sql.append(" ) ");

		}

		if (createdFrom != null)
		{
			sql.append(" and ANDON.createdDate > ? ");
			Date dateFrom = DateUtils.truncate(createdFrom, Calendar.DATE);
			params.add(dateFrom);
		}
		if (createdTo != null)
		{
			sql.append(" and ANDON.createdDate  < ?");
			Date dateTo = DateUtils.truncate(createdTo, Calendar.DATE);
			Date d = DateUtils.addDays(dateTo, 1);
			params.add(d);
		}
		if (unitPks != null && unitPks.size() > 0)
		{
			sql.append(" and ANDON.unitPk in (");
			String comma = "";
			for (Integer unit : unitPks)
			{
				sql.append(comma).append(unit);
				comma = ",";
			}
			sql.append(" ) ");
		}
		sql.append(" order by RPN desc ");
		sql.append(" LIMIT 3 ");

		return persistWrapper.readList(AndonQuery.class, sql.toString(),
				(params.size() > 0) ? params.toArray(new Object[params.size()]) : null);
	}*/

}
