package com.tathvatech.ncr.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.sarvasutra.etest.EtestApplication;
import com.tathvatech.testsutra.ncr.common.FunctionPermissionMapping;
import com.tathvatech.testsutra.ncr.common.NcrCorrectiveActionBean;
import com.tathvatech.testsutra.ncr.common.NcrDispositionBean;
import com.tathvatech.testsutra.ncr.common.NcrEnum;
import com.tathvatech.testsutra.ncr.common.NcrFunctionBean;
import com.tathvatech.testsutra.ncr.common.NcrFunctionQuery;
import com.tathvatech.testsutra.ncr.common.NcrGroupBean;
import com.tathvatech.testsutra.ncr.common.NcrGroupBeanSimple;
import com.tathvatech.testsutra.ncr.common.NcrGroupOID;
import com.tathvatech.testsutra.ncr.common.NcrGroupQuery;
import com.tathvatech.testsutra.ncr.common.NcrGroupQueryFilter;
import com.tathvatech.testsutra.ncr.common.NcrItemActivityRefBean;
import com.tathvatech.testsutra.ncr.common.NcrItemBean;
import com.tathvatech.testsutra.ncr.common.NcrItemNcgExportQuery;
import com.tathvatech.testsutra.ncr.common.NcrItemOID;
import com.tathvatech.testsutra.ncr.common.NcrItemQuery;
import com.tathvatech.testsutra.ncr.common.NcrItemQueryFilter;
import com.tathvatech.testsutra.ncr.common.NcrMilestoneQuery;
import com.tathvatech.testsutra.ncr.common.NcrUnitAllocBean;
import com.tathvatech.testsutra.ncr.common.NcrUnitAssignBean;
import com.tathvatech.testsutra.ncr.common.NcrUnitAssignOID;
import com.tathvatech.testsutra.ncr.common.NcrWhereFoundBean;
import com.tathvatech.testsutra.ncr.common.ResourceRequirementBean;
import com.tathvatech.testsutra.ncr.reports.ncritemlistreport.NcrCorrectiveActionQuery;
import com.tathvatech.testsutra.ncr.reports.ncritemlistreport.NcrCorrectiveActionReportFilter;
import com.tathvatech.testsutra.ncr.reports.ncritemlistreport.NcrItemListReportResultRow;
import com.tathvatech.testsutra.purchaseOrders.common.PurchaseOrderOID;
import com.tathvatech.testsutra.vcr.service.VcrEmailSender;
import com.tathvatech.ts.caf.ApplicationProperties;
import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.UserOID;
import com.tathvatech.ts.core.authorization.AuthorizationManager;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.utils.AsyncProcessor;
import com.tathvatech.ts.core.common.utils.EmailMessageInfo;
import com.tathvatech.ts.core.part.PartOID;
import com.tathvatech.ts.core.part.PartRevisionOID;
import com.tathvatech.ts.core.part.ProjectSiteConfigRolesEnum;
import com.tathvatech.ts.core.part.SupplierOID;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.ProjectSiteConfigOID;
import com.tathvatech.ts.core.sites.SiteOID;
import com.tathvatech.ts.milestonetrack.EntityMilestoneDelegate;
import com.thirdi.surveyside.project.ProjectManager;
import com.thirdi.surveyside.project.ProjectSiteConfig;

public class NcrDelegate
{

	public static String APPROVED_MILESTONE = "Approved";
	public static String APPROVED_MILESTONE_SELECTEDVALUE = "Complete";

	// ncr group
	public static NcrGroupBean saveNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrGroupBean bean = NcrManager.saveNcrGroup(context, nesnItemBean);

			conn.commit();

			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrGroupBean copyNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrGroupBean bean = NcrManager.copyNcrGroup(context, nesnItemBean);

			conn.commit();

			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrGroupBean publishNcrGroup(UserContext context, NcrGroupBean nesnItemBean, String message)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrGroupBean bean = NcrManager.publishNcrGroup(context, nesnItemBean, message);
			conn.commit();
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrGroupBean verifyNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrGroupBean bean = NcrManager.verifyNcrGroup(context, nesnItemBean);
			conn.commit();
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrGroupBean cancelNcrGroup(UserContext context, NcrGroupBean nesnItemBean, String message)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrGroupBean bean = NcrManager.cancelNcrGroup(context, nesnItemBean, message);
			conn.commit();
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrGroupBean closeNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrGroupBean bean = NcrManager.closeNcrGroup(context, nesnItemBean);
			conn.commit();
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static List<NcrGroupQuery> getLastNcrGroupListByPartFk(UserContext context, PartOID partOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getLastNcrGroupList(context, partOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemBean> publishGroupCheck(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			List<NcrItemBean> result = NcrManager.publishGroupCheck(ncrGroupOID);
			conn.commit();
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static List<NcrItemBean> getEmptyDispositionNcrItem(UserContext context, NcrGroupOID ncrGroupOID)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			List<NcrItemBean> result = NcrManager.getEmptyDispositionNcrItem(ncrGroupOID);
			conn.commit();
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static List<NcrItemBean> getPendingApproveNcrList(UserContext context, NcrGroupOID ncrGroupOID)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			List<NcrItemBean> result = NcrManager.getPendingApproveNcrList(ncrGroupOID);
			conn.commit();
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrGroupBean getNcrGroupBean(UserContext context, NcrGroupOID ncrGroupOID)
	{
		return NcrManager.getNcrGroupBean(ncrGroupOID);
	}

	public static NcrGroupBeanSimple getNcrGroupBeanSimple(UserContext context, NcrGroupOID ncrGroupOID)
	{
		return NcrManager.getNcrGroupBeanSimple(ncrGroupOID);
	}

	public static List<String> getDescriptionHistory(PartOID partOID) throws Exception
	{
		try
		{
			return NcrManager.getDescriptionHistory(partOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	public static void approveNcrGroupStatus(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.approveNcrGroupStatus(ncrGroupOID);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrGroupQuery> getSimilarNcrGroupList(UserContext context, String searchString,
			ProjectOID projectOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getSimilarNcrGroupList(context, searchString, projectOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrGroupQuery> getNcrGroupList(UserContext context, PartOID partOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrGroupList(context, partOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrGroupQuery> getNcrGroupList(UserContext context, PartOID partOID,
			PartRevisionOID partRevisionOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrGroupList(context, partOID, partRevisionOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrGroupQuery> getNcrGroupListView(UserContext context, NcrGroupQueryFilter ncrQueryFilter)
			throws Exception
	{
		return NcrManager.getNcrGroupListView(context, ncrQueryFilter);
	}

	public static void deleteNcrGroupByPk(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrGroupByPk(ncrGroupOID);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	// Ncr Item
	public static NcrItemBean saveNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemBean ncrBean = NcrManager.saveNcrItem(context, ncrItemBean);
			conn.commit();
			return ncrBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrItemBean copyNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemBean ncrBean = NcrManager.copyNcrItem(context, ncrItemBean);
			conn.commit();
			return ncrBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrItemBean verifyNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemBean ncrBean = NcrManager.verifyNcrItem(context, ncrItemBean);
			conn.commit();
			return ncrBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrItemBean cancelNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemBean ncrBean = NcrManager.cancelNcrItem(context, ncrItemBean);
			conn.commit();
			return ncrBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static void approveNcrItemStatus(UserContext context, NcrItemOID ncrItemOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.approveNcrItemStatus(context, ncrItemOID);
			NcrItemBean ncrItemBean = NcrManager.getNcrItemBean(ncrItemOID);
			if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString()))
			{
				NcrEmailSender.notifyNcrItemApprove(context, ncrItemBean);
				EntityMilestoneDelegate.setMilestoneStatus(EtestApplication.getInstance().getUserContext(),
						new NcrItemOID(ncrItemBean.getPk(), null), APPROVED_MILESTONE,
						APPROVED_MILESTONE_SELECTEDVALUE);
			}
			if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.REJECTED.toString()))
			{
				NcrEmailSender.notifyNcrItemRejected(context, ncrItemBean);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static NcrItemBean closeNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemBean ncrBean = NcrManager.closeNcrItem(context, ncrItemBean);
			conn.commit();
			return ncrBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrItemBean reopenNcrItem(UserContext context, NcrItemOID ncrItemOID, String comment) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemBean ncrBean = NcrManager.reopenNcrItem(context, ncrItemOID, comment);
			conn.commit();
			return ncrBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrItemBean setOriginTypeAndOriginPkNcrItem(UserContext context, Integer orginPk, String originType,
			NcrItemOID ncrItemOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemBean ncrBean = NcrManager.setOriginTypeAndOriginPkNcrItem(orginPk, originType, ncrItemOID);
			conn.commit();
			return ncrBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrItemQuery getNcrItemQuery(UserContext context, NcrItemOID ncrItemOID)
	{
		return NcrManager.getNcrItemQuery(context, ncrItemOID);
	}

	public static List<NcrUnitAllocBean> getNcrUnitAloc(Integer orginPk, String originType, int unitFk) throws Exception
	{
		return NcrManager.getNcrUnitAloc(orginPk, originType, unitFk);
	}

	public static NcrUnitAssignBean getNcrUnitAssignBean(Integer orginPk, String originType, int unitFk)
			throws Exception
	{
		return NcrManager.getNcrUnitAssignBean(orginPk, originType, unitFk);
	}

	public static NcrItemBean getNcrItemBean(NcrItemOID ncrItemOID)
	{
		return NcrManager.getNcrItemBean(ncrItemOID);
	}

	public static List<NcrItemBean> getNcrItemBeans(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrItemBeans(ncrGroupOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemQuery> getNcrItemQueryList(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrItemQueryList(context, ncrGroupOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemQuery> getNcrItemQueryList(UserContext context, NcrGroupOID ncrGroupOID,
			List<NcrEnum.NcrItemStatus> status) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrItemQueryList(context, ncrGroupOID, status);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemNcgExportQuery> getNcrItemNcgExportQueryList(NcrItemQueryFilter ncrItemQueryFilter)
			throws Exception
	{
		return NcrManager.getNcrItemNcgExportQueryList(ncrItemQueryFilter);
	}

	public static List<NcrItemQuery> getNcrItemQueryListByncrGroupFk(UserContext context,
			NcrItemQueryFilter ncrItemQueryFilter) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrItemQueryListByncrGroupFk(context, ncrItemQueryFilter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemListReportResultRow> getMyNcrApprovalList(UserContext context) throws Exception
	{

		Connection conn = null;

		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);

			return NcrManager.getMyNcrApprovalList(context);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemListReportResultRow> getMyNcrAkcnowledgeList(UserContext context) throws Exception
	{

		Connection conn = null;

		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);

			return NcrManager.getMyNcrAkcnowledgeList(context);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	// public static List<NcrItemQuery> getMyNcrApprovalList(UserContext
	// context) throws Exception
	// {
	//
	// Connection conn = null;
	// List<NcrEnum.NcrItemStatus> ncrItemStatusList = new
	// ArrayList<NcrEnum.NcrItemStatus>();
	// List<NcrEnum.NcrGroupStatus> ncrGroupStatusList = new
	// ArrayList<NcrEnum.NcrGroupStatus>();
	// try
	// {
	// conn = ServiceLocator.locate().getConnection();
	// conn.setAutoCommit(false);
	// NcrItemQueryFilter ncrItemQueryFilter = new NcrItemQueryFilter();
	// ncrItemQueryFilter.setIsPendingMyApproval(true);
	// ncrItemStatusList.add(NcrEnum.NcrItemStatus.PUBLISHED);
	// ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.PUBLISHED);
	// ncrItemQueryFilter.setNcrGroupStatusList(ncrGroupStatusList);
	// ncrItemQueryFilter.setNcrItemStatusList(ncrItemStatusList);
	// return NcrManager.getNcrItemQueryListByncrGroupFk(context,
	// ncrItemQueryFilter);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// conn.rollback();
	// throw e;
	// }
	// finally
	// {
	// conn.commit();
	// }
	// }

	public static List<NcrItemQuery> getNcrItemList(UserContext context, PartOID partOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrItemDetails(context, partOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemQuery> getNcrItemList(UserContext context, NcrItemQueryFilter ncrItemQueryFilter)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrItemList(context, ncrItemQueryFilter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrItem(UserContext context, NcrItemOID ncrItemOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrItem(ncrItemOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrItem(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrItem(ncrGroupOID);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	// Ncr Function
	public static NcrFunctionBean saveNcrFunction(UserContext context, NcrFunctionBean nesnItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrFunctionBean bean = NcrManager.saveNcrFunction(context, nesnItemBean);
			conn.commit();
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static void updatePrevNcrFunction(UserContext context, int objectFk, String objectType) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.updatePrevNcrFunction(objectFk, objectType);
			conn.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrFunctionBean acknowledgeNcrFunction(UserContext context, NcrFunctionBean nesnItemBean)
			throws Exception
	{
		Connection conn = null;
		try
		{
			boolean isapproved = false;
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrFunctionBean ncrPrev = NcrManager.getNcrFunctionBeanByPk(nesnItemBean.getPk());
			if (ncrPrev.getApprovedStatus() == null)
			{
				NcrFunctionBean ncrFunctionBean = NcrManager.approveNcrFunction(context, nesnItemBean);
				if (ncrPrev.getApprovedStatus() == null && ncrFunctionBean.getApprovedStatus() != null)
				{
					if (ncrFunctionBean.getApprovedStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString()))
						isapproved = true;
				}
				if (isapproved)
				{
					if (ncrFunctionBean.getObjectType().equals(EntityTypeEnum.VCRItem.toString()))
					{

						VcrEmailSender.notifyVcrFunctionAcknowldeged(context, ncrFunctionBean);

					} else if (ncrFunctionBean.getObjectType().equals(EntityTypeEnum.NCR.toString()))
					{

						NcrEmailSender.notifyNcrItemFunctionAcknowledged(context, ncrFunctionBean);

					}
				}
				conn.commit();

				return ncrFunctionBean;
			} else
			{
				throw new AppException(
						"The data you are viewing is not current and it cannot be saved, Please re-open the ncr and try again");
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrFunctionBean approveNcrFunction(UserContext context, NcrFunctionBean nesnItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			boolean isapproved = false;
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrFunctionBean ncrPrev = NcrManager.getNcrFunctionBeanByPk(nesnItemBean.getPk());
			if (ncrPrev.getApprovedStatus() == null)
			{
				NcrFunctionBean ncrFunctionBean = NcrManager.approveNcrFunction(context, nesnItemBean);
				if (ncrFunctionBean.getApprovedStatus() != null)
				{
					if (ncrFunctionBean.getApprovedStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString()))
						isapproved = true;
				}
				if (ncrFunctionBean.getObjectType().equals(EntityTypeEnum.VCRItem.toString()))
				{
					if (isapproved)
					{
						VcrEmailSender.notifyVcrFunctionApproved(context, ncrFunctionBean);
					}

				} else if (ncrFunctionBean.getObjectType().equals(EntityTypeEnum.NCR.toString()))
				{
					if (isapproved)
					{
						NcrEmailSender.notifyNcrItemFunctionApproved(context, ncrFunctionBean);

					}

				}
				conn.commit();

				return ncrFunctionBean;
			} else
			{
				throw new AppException(
						"The data you are viewing is not current and it cannot be saved, Please re-open the ncr and try again");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrFunctionBean rejectNcrFunction(UserContext context, NcrFunctionBean nesnItemBean) throws Exception
	{
		Connection conn = null;
		try
		{
			boolean isRejected = false;
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrFunctionBean bean = NcrManager.rejectNcrFunction(context, nesnItemBean);
			conn.commit();
			if (bean.getApprovedStatus() != null)
			{
				if (bean.getApprovedStatus().equals(NcrEnum.NcrItemStatus.REJECTED.toString()))
					isRejected = true;
			}
			if (bean.getObjectType().equals(EntityTypeEnum.VCRItem.toString()))
			{
				if (isRejected)
				{
					// VcrEmailSender.notifyVcrFunctionRejected(context, bean);
				}
			} else if (bean.getObjectType().equals(EntityTypeEnum.NCR.toString()))
			{
				if (isRejected)
				{
					NcrEmailSender.notifyNcrItemFunctionRejected(context, bean);
				}
			}
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrFunctionBean getNcrFunctionBeanByPk(UserContext context, int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrFunctionBeanByPk(pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static NcrFunctionBean getCurrentNcrFunction(int objectFk, String objectType, int functionMasterFk)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getCurrentNcrFunction(objectFk, objectType, functionMasterFk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrFunctionQuery> getHistoryNcrFunctions(int objectFk, String objectType,
			NcrFunctionMaster.TYPE roleType) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getHistoryNcrFunctions(objectFk, objectType, roleType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrFunction(UserContext context, int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrFunction(pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrFunction(UserContext context, int objectFk, String objectType) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrFunction(objectFk, objectType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrFunctionByNcrGroupFk(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrFunction(ncrGroupOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void saveNcrFunctionNotifiers(UserContext context, NcrFunctionNotifiers ncrFunctionNotifiers)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.saveNcrFunctionNotifiers(context, ncrFunctionNotifiers);
			conn.commit();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	// Ncr Unit Assign
	public static NcrUnitAssignBean saveNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrUnitAssignBean bean = NcrManager.saveNcrUnitAssign(context, ncrUnitAssignBean);
			conn.commit();

			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrUnitAssignBean publishNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrUnitAssignBean bean = NcrManager.publishNcrUnitAssign(context, ncrUnitAssignBean);
			conn.commit();

			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrUnitAssignBean completeNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrUnitAssignBean bean = NcrManager.completeNcrUnitAssign(context, ncrUnitAssignBean);
			conn.commit();

			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrUnitAssignBean closeNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrUnitAssignBean bean = NcrManager.closeNcrUnitAssign(context, ncrUnitAssignBean);
			conn.commit();

			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrUnitAssignBean getNcrUnitAssignBeanByPk(UserContext context, int pk) throws Exception
	{
		return NcrManager.getNcrUnitAssignBeanByPk(pk);
	}

	public static NcrUnitAssignBean getNcrUnitAssignBean(UserContext context, NcrUnitAssignOID ncrUnitAssignOID)
			throws Exception
	{
		return NcrManager.getNcrUnitAssignBean(ncrUnitAssignOID);
	}

	public static List<NcrUnitAssignBean> getNcrUnitAssign(NcrItemOID ncrItemOID) throws Exception
	{
		return NcrManager.getNcrUnitAssign(ncrItemOID);
	}

	public static void deleteNcrUnitAssignByPk(UserContext context, int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrUnitAssign(pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrUnitAssign(UserContext context, NcrItemOID ncrItemOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrUnitAssign(ncrItemOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrUnitAssign(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrUnitAssign(ncrGroupOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	// Ncr Unit Item Alloc
	// public static NcrUnitAllocBean saveNcrUnitItemAlloc(UserContext context,
	// NcrUnitAllocBean nesnItemBean)
	// throws Exception {
	// Connection conn = null;
	// try {
	// conn = ServiceLocator.locate().getConnection();
	// conn.setAutoCommit(false);
	// // NcrUnitAllocBean bean = NcrManager.saveNcrUnitItemAlloc(context,
	// nesnItemBean);
	// conn.commit();
	// return bean;
	// } catch (Exception e) {
	// e.printStackTrace();
	// conn.rollback();
	// throw e;
	// }
	// }

	public static List<NcrUnitAllocBean> getNcrUnitAlloc(NcrUnitAssignOID ncrUnitAssignOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrUnitAlloc(ncrUnitAssignOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrUnitAlloc(UserContext context, int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrUnitAlloc(pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static void deleteNcrUnitItemAllocByNcrGroupFk(UserContext context, NcrGroupOID ncrGroupOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrManager.deleteNcrUnitAlloc(ncrGroupOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrItemBean> getNcrDescList(PartOID partOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrDescList(partOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	// public static void deleteNcrPartsRequiredBeanByPk(UserContext context,
	// int pk) throws Exception {
	// Connection conn = null;
	// try {
	// conn = ServiceLocator.locate().getConnection();
	// conn.setAutoCommit(false);
	// NcrManager.deleteNcrPartsRequiredBeanByPk(context, pk);
	// } catch (Exception e) {
	// e.printStackTrace();
	// conn.rollback();
	// throw e;
	// } finally {
	// conn.commit();
	// }
	// }

	private static void sendEmailForFunctionCoordinator(UserContext context, String subject, String text, String html,
			Integer siteFk, Integer projectFk, NcrFunctionBean ncrFunctionBean) throws Exception
	{
		List<Integer> sentList = new ArrayList<>();
		// String tempFile = TempFileUtil.getNewTempFile();
		//
		// List<AttachmentIntf> emailAttachments = new ArrayList();
		// Attachment att = new Attachment();
		// att.setFullFilePath(tempFile);
		// if
		// (ncrFunctionBean.getObjectType().equals(EntityTypeEnum.VCRItem.toString()))
		// {
		// att.setFileDisplayName("VCR_" + new Date().getTime() + ".pdf");
		// } else if
		// (ncrFunctionBean.getObjectType().equals(EntityTypeEnum.NCR.toString()))
		// {
		// att.setFileDisplayName("NCR_" + new Date().getTime() + ".pdf");
		// }
		// emailAttachments.add(att);

		// email to function coordinators

		ProjectOID projectOID = new ProjectOID(projectFk, null);
		SiteOID siteOID = new SiteOID(siteFk, null);
		ProjectSiteConfig projectSiteConfig = ProjectManager.getProjectSiteConfig(projectOID, siteOID);
		Boolean isNotificationRequired = false;

		if (ncrFunctionBean.getNotificationRequired() != null)
		{
			isNotificationRequired = true;
		}
		ProjectSiteConfigRolesEnum roleId = null;
		FunctionPermissionMapping functionPermissionMapping = new FunctionPermissionMapping();
		roleId = functionPermissionMapping.getRollId(ncrFunctionBean.getFunctionMasterFk());

		List<User> users = new AuthorizationManager()
				.getUsersInRole(new ProjectSiteConfigOID(projectSiteConfig.getPk(), null), roleId);
		List<String> userListArr = new ArrayList<String>();
		try
		{
			for (Iterator iterator = users.iterator(); iterator.hasNext();)
			{
				User user = (User) iterator.next();
				if (user != null && user.getEmail() != null)
				{
					if (!(sentList.contains(user.getPk())) && (isNotificationRequired))
					{
						userListArr.add(user.getEmail());
						sentList.add(user.getPk());
					}
				}
			}
			if (userListArr != null && userListArr.size() > 0)
			{
				Object[] objuserListArr = userListArr.toArray();
				String[] userList = Arrays.copyOf(objuserListArr, objuserListArr.length, String[].class);
				EmailMessageInfo emailInfo = new EmailMessageInfo(ApplicationProperties.getEmailFromAddress(), null,
						userList, subject, text, html, null);
				AsyncProcessor.scheduleEmail(emailInfo);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static NcrItemActivityRefBean saveNcrItemActivityRefBean(UserContext context,
			NcrItemActivityRefBean ncrItemActivityRefBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrItemActivityRefBean bean = NcrManager.saveNcrItemActivityRefBean(context, ncrItemActivityRefBean);
			conn.commit();
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrItemActivityRefBean getNcrItemActivityRefBeanByPk(UserContext context, int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrItemActivityRefBeanByPk(pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static ResourceRequirementBean saveResourceRequirementBean(UserContext context,
			ResourceRequirementBean resourceRequirementBean) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			ResourceRequirementBean bean = NcrManager.saveResourceRequirementBean(context, resourceRequirementBean);
			conn.commit();
			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static ResourceRequirementBean getResourceRequirementBeanByPk(UserContext context, int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getResourceRequirementBeanByPk(pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	// public static void alterNcrUnitAssign(UserContext context) throws
	// Exception
	// {
	// Connection conn = null;
	// try
	// {
	// conn = ServiceLocator.locate().getConnection();
	// conn.setAutoCommit(false);
	// NcrManager.alterNcrUnitAssign(context);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// conn.rollback();
	// throw e;
	// }
	// finally
	// {
	// conn.commit();
	// }
	// }

	public static NcrWhereFoundBean getNcrWhereFoundByPk(UserContext context, int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			NcrWhereFoundBean ncrWhereFoundBean = NcrManager.getNcrWhereFoundBeanByPk(pk);
			conn.commit();
			return ncrWhereFoundBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static List<UserOID> getNcrFunctionNotifiersList(int ncrFunctionFk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrFunctionNotifiersList(ncrFunctionFk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrMilestoneQuery> getNcrMilestoneQuery(UserContext context,
			NcrItemQueryFilter ncrItemQueryFilter) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrMilestoneQuery(context, ncrItemQueryFilter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static NcrDispositionBean getNcrDispositionBean(int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrDispositionBean(pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrDispositionBean> getNcrDispositionBean() throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrDispositionBean();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrFunctionBean> getNcrFunctions() throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrFunctions();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrFunctionBean> getNcrFunctionsByProject(ProjectOID projectOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrFunctionsByProject(projectOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrGroupBean> getNcrGroupBeanList(SupplierOID supplierOID, SiteOID siteOID,
			PurchaseOrderOID purchaseOrderOID) throws Exception
	{
		return NcrManager.getNcrGroupBeanList(supplierOID, siteOID, purchaseOrderOID);
	}

	public static NcrCorrectiveStatusActions getCurrentNcrCorrectiveAction(int objectPk,
			NcrEnum.NcrCorrectiveStatusEnum actionName) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getCurrentNcrCorrectiveAction(objectPk, actionName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrCorrectiveActionBean> getNcrCorrectiveAction(NcrItemOID ncrItemOID) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrCorrectiveAction(ncrItemOID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrCorrectiveActionQuery> getNcrCorrectiveAction(UserContext context,
			NcrCorrectiveActionReportFilter filter) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrCorrectiveAction(context, filter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrCorrectiveActionQuery> getMyActiveNcrCorrectiveAction(UserContext context) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getMyActiveNcrCorrectiveAction(context);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrCorrectiveActionQuery> getMyOverduedNcrCorrectiveAction(UserContext context) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getMyOverduedNcrCorrectiveAction(context);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static List<NcrCorrectiveActionH> getNcrCorrectiveActionHistoryByncrCorrectiveActionPk(
			int ncrCorrectiveActionPk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrCorrectiveActionHistoryByncrCorrectiveActionPk(ncrCorrectiveActionPk);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static NcrCorrectiveActionBean correctiveCompleted(UserContext context, int correctiveActionPk,
			String comment) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);

			NcrCorrectiveActionBean ncrActionBean = NcrManager.correctiveCompleted(context, correctiveActionPk,
					comment);

			conn.commit();
			return ncrActionBean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static NcrCorrectiveActionBean correctiveReOpen(UserContext context, int correctiveActionPk, String comment)
			throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);

			NcrCorrectiveActionBean hcabean = null;
			hcabean = NcrManager.correctiveReOpen(context, correctiveActionPk, comment);

			conn.commit();
			return hcabean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	public static List<NcrFunctionMaster> getNcrFunctionMasters(NcrFunctionMaster.TYPE type) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrFunctionMasters(type);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}

	public static NcrFunctionMaster getNcrFunctionMaster(int pk) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ServiceLocator.locate().getConnection();
			conn.setAutoCommit(false);
			return NcrManager.getNcrFunctionMaster(pk);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			conn.commit();
		}
	}
}