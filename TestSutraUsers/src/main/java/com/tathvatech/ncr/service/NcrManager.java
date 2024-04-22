package com.tathvatech.ncr.service;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.ncr.common.NcrItemQuery;
import com.tathvatech.ncr.common.NcrItemQueryBuilder;
import com.tathvatech.ncr.common.NcrItemQueryFilter;
import com.tathvatech.ncr.oid.NcrItemOID;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.controller.ProjectController;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.user.OID.PartOID;
import com.tathvatech.user.OID.PartRevisionOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WhereFoundOID;
import com.tathvatech.user.common.UserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class NcrManager
{
    private final PersistWrapper persistWrapper;
	private ProjectController projectController;
	public static String PUBLISHED_MILESTONE = "Published";
	public static String PUBLISHED_MILESTONE_SELECTEDVALUE = "Complete";
	public static String CLOSED_MILESTONE = "Closed";
	public static String CLOSED_MILESTONE_SELECTEDVALUE = "Complete";
	public static int SCRAP_DISPOSITIONFK = 3;
	public static int RETURN_TO_SUPPLIER_DISPOSITIONFK = 5;

    public NcrManager(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;

    }
	//uncomment while working on NCR

   /* // Ncr Group
	public  NcrGroupBean saveNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		NcrGroup nesnItem = null;
		if (nesnItemBean.getPk() != 0)
		{
			nesnItem = persistWrapper.readByPrimaryKey(NcrGroup.class, nesnItemBean.getPk());
		} else
		{
			nesnItem = new NcrGroup();
			nesnItem.setCreatedBy(context.getUser().getPk());
			nesnItem.setCreatedDate(new Date());
			nesnItem.seteStatus(EStatusEnum.Active.getValue());
		}
		nesnItem.setStatus(nesnItemBean.getStatus());
		nesnItem.setPartFk(nesnItemBean.getPartOID().getPk());
		if (nesnItemBean.getPartRevisionOID() != null && nesnItemBean.getPartRevisionOID().getPk() != 0)
		{
			nesnItem.setRevisionFk(nesnItemBean.getPartRevisionOID().getPk());
		}
		nesnItem.setProjectFk(nesnItemBean.getProjectOID().getPk());
		nesnItem.setSiteFk(nesnItemBean.getSiteOID().getPk());
		nesnItem.setSupplierFk(nesnItemBean.getSupplierOID().getPk());
		nesnItem.setGroupDescription(nesnItemBean.getGroupDescription());
		if (nesnItemBean.getPurchaseOrderBean() != null)
		{
			PurchaseOrderBean purchaseOrderEntryBean = null;
			if (nesnItemBean.getPurchaseOrderBean().getPk() > 0)
			{
				purchaseOrderEntryBean = nesnItemBean.getPurchaseOrderBean();
			} else
			{
				purchaseOrderEntryBean = PONManager.getPurchaseOrderEntryBeanByPurchaseOrderNo(
						nesnItemBean.getPurchaseOrderBean().getPurchaseOrderNumber());
			}
			if (purchaseOrderEntryBean != null)
			{
				nesnItem.setPurchaseOrderEntryFk(purchaseOrderEntryBean.getPk());
			}
		}
		if (nesnItemBean.getShipmentReceiveEntryBean() != null)
		{
			ShipmentReceiveEntryBean shipmentReceiveEntryBean = null;
			if (nesnItemBean.getShipmentReceiveEntryBean().getPk() > 0)
			{
				shipmentReceiveEntryBean = nesnItemBean.getShipmentReceiveEntryBean();
			} else
			{
				shipmentReceiveEntryBean = PartsNewManager.getShipmentReceiveEntryBean(
						nesnItemBean.getShipmentReceiveEntryBean().getShipmentNumber(),
						nesnItemBean.getPurchaseOrderBean().getPurchaseOrderNumber());
			}
			if (shipmentReceiveEntryBean != null)
			{
				nesnItem.setShipmentReceiveEntryFk(shipmentReceiveEntryBean.getPk());
			}
		}
		if (nesnItemBean.getPurchaseOrderLineItemBean() != null && nesnItemBean.getPurchaseOrderLineItemBean() != null
				&& nesnItem.getPurchaseOrderEntryFk() != null)
		{
			PurchaseOrderLineItemBean purchaseOrderLineItemBean = PONManager
					.getPurchaseOrderLineItemBeanByLineItemNoAndPurchaseFK(
							nesnItemBean.getPurchaseOrderLineItemBean().getLineItemNumber(),
							nesnItem.getPurchaseOrderEntryFk());
			if (purchaseOrderLineItemBean != null)
			{
				nesnItem.setNcrPurchaseOrderLineItemFk(purchaseOrderLineItemBean.getPk());
			}
		}

		int itemPk;
		if (nesnItemBean.getPk() != 0)
		{
			persistWrapper.update(nesnItem);
			itemPk = nesnItem.getPk();
		} else
		{
			itemPk = persistWrapper.createEntity(nesnItem);
			nesnItem = persistWrapper.readByPrimaryKey(NcrGroup.class, itemPk);
			nesnItem.setPk(itemPk);
			nesnItem.setNcrGroupNo("TNCRGROUP-" + itemPk);
			PersistWrapper.update(nesnItem);
		}
		List<NcrItemBean> prevNcrItemBeans = getNcrItemBeans(new NcrGroupOID(itemPk, ""));
		if (nesnItemBean.getNcrItemBeanList() != null)
		{
			for (NcrItemBean ncrItemBean : nesnItemBean.getNcrItemBeanList())
			{
				if (prevNcrItemBeans.contains(ncrItemBean))
				{
					prevNcrItemBeans.remove(ncrItemBean);
				}
				if (ncrItemBean.getNcrGroupFk() != 0 && ncrItemBean.getNcrGroupFk() != itemPk)
				{
					ncrItemBean.setNcrGroupFk(itemPk);
					ncrItemBean.setPk(0);
					ncrItemBean.setNcrStatus(NcrEnum.NcrItemStatus.DRAFT.toString());
					// ncrItemBean.setPartOID(nesnItemBean.getPartOID());
					// ncrItemBean.setPartRevisionOID(nesnItemBean.getPartRevisionOID());
					// ncrItemBean.setProjectOID(nesnItemBean.getProjectOID());
					ncrItemBean = copyNcrItem(context, ncrItemBean);
				} else
				{
					ncrItemBean.setNcrGroupFk(itemPk);
					// ncrItemBean.setPartRevisionOID(nesnItemBean.getPartRevisionOID());
					// ncrItemBean.setPartOID(nesnItemBean.getPartOID());
					// ncrItemBean.setProjectOID(nesnItemBean.getProjectOID());
					ncrItemBean = saveNcrItem(context, ncrItemBean);
				}

			}
		}
		deleteNcrItem(prevNcrItemBeans);
		setNcrGroupStatus(new NcrGroupOID(itemPk, ""));
		nesnItemBean = getNcrGroupBean(new NcrGroupOID(itemPk, ""));
		return nesnItemBean;
	}

	public  NcrGroupBean copyNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		NcrGroup nesnItem = null;
		nesnItem = new NcrGroup();
		nesnItem.setCreatedBy(context.getUser().getPk());
		nesnItem.setCreatedDate(new Date());
		nesnItem.setStatus(NcrEnum.NcrGroupStatus.DRAFT.toString());
		nesnItem.setPartFk(nesnItemBean.getPartOID().getPk());
		nesnItem.seteStatus(EStatusEnum.Active.getValue());
		if (nesnItemBean.getPartRevisionOID() != null && nesnItemBean.getPartRevisionOID().getPk() != 0)
		{
			nesnItem.setRevisionFk(nesnItemBean.getPartRevisionOID().getPk());
		}
		nesnItem.setProjectFk(nesnItemBean.getProjectOID().getPk());
		nesnItem.setSiteFk(nesnItemBean.getSiteOID().getPk());
		nesnItem.setSupplierFk(nesnItemBean.getSupplierOID().getPk());
		nesnItem.setGroupDescription(nesnItemBean.getGroupDescription());

		if (nesnItemBean.getPurchaseOrderBean() != null)
		{

			PurchaseOrderBean purchaseOrderEntryBean = null;
			if (nesnItemBean.getPurchaseOrderBean().getPk() > 0)
			{
				purchaseOrderEntryBean = nesnItemBean.getPurchaseOrderBean();
			} else
			{
				purchaseOrderEntryBean = PONManager.getPurchaseOrderEntryBeanByPurchaseOrderNo(
						nesnItemBean.getPurchaseOrderBean().getPurchaseOrderNumber());
			}
			if (purchaseOrderEntryBean != null)
			{
				nesnItem.setPurchaseOrderEntryFk(purchaseOrderEntryBean.getPk());
			}
		}
		if (nesnItemBean.getShipmentReceiveEntryBean() != null)
		{
			ShipmentReceiveEntryBean shipmentReceiveEntryBean = null;
			if (nesnItemBean.getShipmentReceiveEntryBean().getPk() > 0)
			{
				shipmentReceiveEntryBean = nesnItemBean.getShipmentReceiveEntryBean();
			} else
			{
				shipmentReceiveEntryBean = PartsNewManager.getShipmentReceiveEntryBeanByDocId(
						nesnItemBean.getShipmentReceiveEntryBean().getShipmentNumber());
			}
			if (shipmentReceiveEntryBean != null)
			{
				nesnItem.setShipmentReceiveEntryFk(shipmentReceiveEntryBean.getPk());
			}
		}
		if (nesnItemBean.getPurchaseOrderLineItemBean() != null && nesnItemBean.getPurchaseOrderLineItemBean() != null
				&& nesnItem.getPurchaseOrderEntryFk() != null)
		{
			PurchaseOrderLineItemBean purchaseOrderLineItemBean = PONManager
					.getPurchaseOrderLineItemBeanByLineItemNoAndPurchaseFK(
							nesnItemBean.getPurchaseOrderLineItemBean().getLineItemNumber(),
							nesnItem.getPurchaseOrderEntryFk());
			if (purchaseOrderLineItemBean != null)
			{
				nesnItem.setNcrPurchaseOrderLineItemFk(purchaseOrderLineItemBean.getPk());
			}
		}
		int itemPk;
		itemPk = persistWrapper.createEntity(nesnItem);
		nesnItem.setPk(itemPk);
		nesnItem.setNcrGroupNo("TNCRGROUP-" + itemPk);
		persistWrapper.update(nesnItem);

		List<NcrItemBean> storeList = new ArrayList<NcrItemBean>();
		if (nesnItemBean.getNcrItemBeanList() != null)
		{
			for (int i = 0; i < nesnItemBean.getNcrItemBeanList().size(); i++)
			{
				NcrItemBean ncrItemBean = (NcrItemBean) nesnItemBean.getNcrItemBeanList().get(i);
				ncrItemBean.setNcrGroupFk(itemPk);
				ncrItemBean.setPk(0);
				ncrItemBean.setNcrStatus(NcrEnum.NcrItemStatus.DRAFT.toString());
				// ncrItemBean.setPartRevisionOID(nesnItemBean.getPartRevisionOID());
				// ncrItemBean.setPartOID(nesnItemBean.getPartOID());
				// ncrItemBean.setProjectOID(nesnItemBean.getProjectOID());
				ncrItemBean = copyNcrItem(context, ncrItemBean);
				storeList.add(ncrItemBean);
			}
		}
		setNcrGroupStatus(new NcrGroupOID(itemPk, ""));
		nesnItemBean = getNcrGroupBean(new NcrGroupOID(itemPk, ""));
		return nesnItemBean;
	}

	public  NcrGroupBean publishNcrGroup(UserContext context, NcrGroupBean nesnItemBean, String message)
			throws Exception
	{
		NcrGroup nesnItem = null;
		if (nesnItemBean.getPk() != 0)
		{
			nesnItem = persistWrapper.readByPrimaryKey(NcrGroup.class, nesnItemBean.getPk());
			nesnItem.setStatus(NcrEnum.NcrGroupStatus.PUBLISHED.toString());
			Integer seqCount = getPublishedSequenceNumber(new NcrGroupOID(nesnItemBean.getPk(), ""));
			if (seqCount == null || seqCount == 0)
			{
				ProjectQuery projectQuery = projectController.getProjectQueryByPk(nesnItemBean.getProjectOID().getPk());
				String projectName = "";
				if (projectQuery != null)
				{
					projectName = projectQuery.getProjectName();
				}
				String seqNo = new NcrGroupSequenceKeyGenerator().getNextSeq(projectName);
				nesnItem.setNcrGroupNo(seqNo);
			}
			// List<NcrItemBean> publishedList = getPublishedNcrItem(new
			// NcrGroupOID(nesnItemBean.getPk(), ""));
			// if (publishedList == null || publishedList.size() == 0)
			// {
			// ProjectQuery projectQuery =
			// ProjectDelegate.getProjectQueryByPk(nesnItemBean.getProjectOID().getPk());
			// String projectName = "";
			// if (projectQuery != null)
			// {
			// projectName = projectQuery.getProjectName();
			// }
			// String seqNo = new
			// NcrGroupSequenceKeyGenerator().getNextSeq(projectName);
			// nesnItem.setNcrGroupNo(seqNo);
			// }

			nesnItem.setPartFk(nesnItemBean.getPartOID().getPk());
			if (nesnItemBean.getPartRevisionOID() != null && nesnItemBean.getPartRevisionOID().getPk() != 0)
			{
				nesnItem.setRevisionFk(nesnItemBean.getPartRevisionOID().getPk());
			}
			nesnItem.setProjectFk(nesnItemBean.getProjectOID().getPk());
			nesnItem.setSiteFk(nesnItemBean.getSiteOID().getPk());
			nesnItem.setSupplierFk(nesnItemBean.getSupplierOID().getPk());
			nesnItem.setGroupDescription(nesnItemBean.getGroupDescription());

			int itemPk;
			if (nesnItemBean.getPk() != 0)
			{
				persistWrapper.update(nesnItem);
				itemPk = nesnItem.getPk();
			} else
			{
				itemPk = persistWrapper.createEntity(nesnItem);
			}

			List<NcrItemBean> prevNcrItemBeans = getNcrItemBeans(new NcrGroupOID(itemPk, ""));
			if (nesnItemBean.getNcrItemBeanList() != null)
			{
				for (NcrItemBean ncrItemBean : nesnItemBean.getNcrItemBeanList())
				{
					if (prevNcrItemBeans.contains(ncrItemBean))
					{
						prevNcrItemBeans.remove(ncrItemBean);
					}
					ncrItemBean.setNcrGroupFk(itemPk);
					ncrItemBean.setPublishedComment(message);
					// ncrItemBean.setPartOID(nesnItemBean.getPartOID());
					// ncrItemBean.setPartRevisionOID(nesnItemBean.getPartRevisionOID());
					ncrItemBean = publishNcrItem(context, ncrItemBean);
				}
			}
			deleteNcrItem(prevNcrItemBeans);
			nesnItemBean = getNcrGroupBean(new NcrGroupOID(itemPk, ""));
		}
		return nesnItemBean;
	}

	public  NcrGroupBean verifyNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		NcrGroup nesnItem = null;
		if (nesnItemBean.getPk() != 0)
		{
			nesnItem = persistWrapper.readByPrimaryKey(NcrGroup.class, nesnItemBean.getPk());

			nesnItem.setStatus(NcrEnum.NcrGroupStatus.COMPLETED.toString());

			int itemPk;
			if (nesnItemBean.getPk() != 0)
			{
				PersistWrapper.update(nesnItem);
				itemPk = nesnItem.getPk();
			} else
			{
				itemPk = persistWrapper.createEntity(nesnItem);
			}
			List<NcrItemBean> storeList = new ArrayList<NcrItemBean>();
			if (nesnItemBean.getNcrItemBeanList() != null)
			{
				for (int i = 0; i < nesnItemBean.getNcrItemBeanList().size(); i++)
				{
					NcrItemBean ncrItemBean = (NcrItemBean) nesnItemBean.getNcrItemBeanList().get(i);
					ncrItemBean.setNcrGroupFk(itemPk);
					// ncrItemBean.setPartOID(nesnItemBean.getPartOID());
					// ncrItemBean.setPartRevisionOID(nesnItemBean.getPartRevisionOID());
					ncrItemBean = verifyNcrItem(context, ncrItemBean);
					storeList.add(ncrItemBean);
				}
			}
			nesnItemBean = getNcrGroupBean(new NcrGroupOID(itemPk, ""));
		}
		return nesnItemBean;
	}

	public  NcrGroupBean cancelNcrGroup(UserContext context, NcrGroupBean nesnItemBean, String message)
			throws Exception
	{
		NcrGroup nesnItem = null;
		if (nesnItemBean.getPk() != 0)
		{
			nesnItem = persistWrapper.readByPrimaryKey(NcrGroup.class, nesnItemBean.getPk());

			nesnItem.setStatus(NcrEnum.NcrGroupStatus.CANCELLED.toString());

			int itemPk;
			if (nesnItemBean.getPk() != 0)
			{
				PersistWrapper.update(nesnItem);
				itemPk = nesnItem.getPk();
			} else
			{
				itemPk = persistWrapper.createEntity(nesnItem);
			}
			List<NcrItemBean> storeList = new ArrayList<NcrItemBean>();
			if (nesnItemBean.getNcrItemBeanList() != null)
			{
				for (int i = 0; i < nesnItemBean.getNcrItemBeanList().size(); i++)
				{
					NcrItemBean ncrItemBean = (NcrItemBean) nesnItemBean.getNcrItemBeanList().get(i);
					ncrItemBean.setNcrGroupFk(itemPk);
					ncrItemBean.setCancelledComment(message);
					// ncrItemBean.setPartOID(nesnItemBean.getPartOID());
					// ncrItemBean.setPartRevisionOID(nesnItemBean.getPartRevisionOID());
					ncrItemBean = cancelNcrItem(context, ncrItemBean);
					storeList.add(ncrItemBean);
				}
			}
			nesnItemBean = getNcrGroupBean(new NcrGroupOID(itemPk, ""));
		}
		return nesnItemBean;
	}

	public  NcrGroupBean closeNcrGroup(UserContext context, NcrGroupBean nesnItemBean) throws Exception
	{
		NcrGroup nesnItem = null;
		if (nesnItemBean.getPk() != 0)
		{
			nesnItem = persistWrapper.readByPrimaryKey(NcrGroup.class, nesnItemBean.getPk());
			nesnItem.setStatus(NcrEnum.NcrGroupStatus.CLOSED.toString());

			int itemPk;
			if (nesnItemBean.getPk() != 0)
			{
				persistWrapper.update(nesnItem);
				itemPk = nesnItem.getPk();
			} else
			{
				itemPk = persistWrapper.createEntity(nesnItem);
			}
			List<NcrItemBean> storeList = new ArrayList<NcrItemBean>();
			if (nesnItemBean.getNcrItemBeanList() != null)
			{
				for (int i = 0; i < nesnItemBean.getNcrItemBeanList().size(); i++)
				{
					NcrItemBean ncrItemBean = (NcrItemBean) nesnItemBean.getNcrItemBeanList().get(i);
					ncrItemBean.setNcrGroupFk(itemPk);
					// ncrItemBean.setPartOID(nesnItemBean.getPartOID());
					// ncrItemBean.setPartRevisionOID(nesnItemBean.getPartRevisionOID());
					ncrItemBean = closeNcrItem(context, ncrItemBean);
					storeList.add(ncrItemBean);
				}
			}
			nesnItemBean = getNcrGroupBean(new NcrGroupOID(itemPk, ""));

		}
		return nesnItemBean;

	}*/

	public  NcrItemQuery getNcrItemQuery(UserContext context, NcrItemOID ncrItemOID)
	{
		NcrItemQueryFilter nfilter = new NcrItemQueryFilter();
		nfilter.setPk((int) ncrItemOID.getPk());
		NcrItemQueryBuilder ncrItemQueryBuilder = new NcrItemQueryBuilder(context, nfilter);
		QueryObject queryObject = ncrItemQueryBuilder.getQuery();
		if (queryObject != null)
			return persistWrapper.read(NcrItemQuery.class, queryObject.getQuery(),
					(queryObject.getParams().size() > 0)
							? queryObject.getParams().toArray(new Object[queryObject.getParams().size()])
							: null);
		else
			return null;
	}

	//uncomment while working on it

	/*public static NcrAreaOfResponsibilityBean getNcrAreaOfResponsibility(int pk)
	{
		String queryString = " select * from ncr_area_of_responsibility_master ";
		return PersistWrapper.read(NcrAreaOfResponsibilityBean.class, queryString + "where pk = ?", pk);
	}

	public  List<NcrAreaOfResponsibilityBean> getNcrAreaOfResponsibility()
	{
		String queryString = " select * from ncr_area_of_responsibility_master where estatus=1";
		return persistWrapper.readList(NcrAreaOfResponsibilityBean.class, queryString);
	}

	public  List<NcrAreaOfResponsibilityBean> getNcrDefectCodes()
	{
		List<NcrAreaOfResponsibilityBean> allList = persistWrapper.readList(NcrAreaOfResponsibilityBean.class,
				"select * from ncr_area_of_responsibility_master where estatus=1");
		HashMap<Integer, NcrAreaOfResponsibilityBean> codeMap = new HashMap<>();
		for (Iterator iterator = allList.iterator(); iterator.hasNext();)
		{
			NcrAreaOfResponsibilityBean ncrAreaOfResponsibilityBean = (NcrAreaOfResponsibilityBean) iterator.next();
			codeMap.put(ncrAreaOfResponsibilityBean.getPk(), ncrAreaOfResponsibilityBean);
		}

		List<NcrAreaOfResponsibilityBean> returnList = new ArrayList<>();
		for (Iterator iterator = allList.iterator(); iterator.hasNext();)
		{
			NcrAreaOfResponsibilityBean ncrAreaOfResponsibilityBean = (NcrAreaOfResponsibilityBean) iterator.next();
			if (ncrAreaOfResponsibilityBean.getParentFk() == null)
			{
				returnList.add(ncrAreaOfResponsibilityBean);
			} else
			{
				NcrAreaOfResponsibilityBean parent = codeMap.get(ncrAreaOfResponsibilityBean.getParentFk());
				if (parent != null)
					parent.getChildren().add(ncrAreaOfResponsibilityBean);

			}
		}

		return returnList;
	}

	public NcrWhereFoundBean getNcrLocation(int pk)
	{
		return PersistWrapper.read(NcrWhereFoundBean.class, "select * from ncr_where_found where pk=?", pk);
	}

	public static List<NcrWhereFoundBean> getNcrLocations()
	{
		List<NcrWhereFoundBean> allList = PersistWrapper.readList(NcrWhereFoundBean.class,
				"select * from ncr_where_found");
		HashMap<Integer, NcrWhereFoundBean> codeMap = new HashMap<>();
		for (Iterator<NcrWhereFoundBean> iterator = allList.iterator(); iterator.hasNext();)
		{
			NcrWhereFoundBean ncrWhereFoundBean = iterator.next();
			codeMap.put(ncrWhereFoundBean.getPk(), ncrWhereFoundBean);
		}
		List<NcrWhereFoundBean> returnList = new ArrayList<>();
		for (Iterator<NcrWhereFoundBean> iterator = allList.iterator(); iterator.hasNext();)
		{
			NcrWhereFoundBean ncrWhereFoundBean = iterator.next();
			if (ncrWhereFoundBean.getParentFk() == null)
			{
				returnList.add(ncrWhereFoundBean);
			} else
			{
				NcrWhereFoundBean parent = codeMap.get(ncrWhereFoundBean.getParentFk());
				if (parent != null)
					parent.getChildren().add(ncrWhereFoundBean);
			}
		}

		return returnList;
	}

	public static List<NcrWhereFoundBean> getWherFoundChilds(WhereFoundOID whereFoundOID)
	{
		List<NcrWhereFoundBean> allList = PersistWrapper.readList(NcrWhereFoundBean.class,
				"select * from ncr_where_found where parentFk=?", whereFoundOID.getPk());
		return allList;
	}

	public static NcrFailureCodeMasterBean getNcrFailureCodeMasterBean(int pk)
	{
		String queryString = " select * from ncr_failure_code_master ";
		return PersistWrapper.read(NcrFailureCodeMasterBean.class, queryString + "where pk = ?", pk);
	}

	public  List<NcrFailureCodeMasterBean> getNcrFailureCodes()
	{
		List<NcrFailureCodeMasterBean> allList = persistWrapper.readList(NcrFailureCodeMasterBean.class,
				"select * from ncr_failure_code_master");
		HashMap<Integer, NcrFailureCodeMasterBean> codeMap = new HashMap<>();
		for (Iterator iterator = allList.iterator(); iterator.hasNext();)
		{
			NcrFailureCodeMasterBean ncrFailureCodeMasterBean = (NcrFailureCodeMasterBean) iterator.next();
			codeMap.put(ncrFailureCodeMasterBean.getPk(), ncrFailureCodeMasterBean);
		}

		List<NcrFailureCodeMasterBean> returnList = new ArrayList<>();
		for (Iterator iterator = allList.iterator(); iterator.hasNext();)
		{
			NcrFailureCodeMasterBean ncrFailureCodeMasterBean = (NcrFailureCodeMasterBean) iterator.next();
			if (ncrFailureCodeMasterBean.getParentFk() == null)
			{
				returnList.add(ncrFailureCodeMasterBean);
			} else
			{
				NcrFailureCodeMasterBean parent = codeMap.get(ncrFailureCodeMasterBean.getParentFk());
				if (parent != null)
					parent.getChildren().add(ncrFailureCodeMasterBean);

			}
		}

		return returnList;
	}

	public  List<NcrItemActivityMasterBean> getNcrItemActivityList()
	{
		return persistWrapper.readList(NcrItemActivityMasterBean.class, "select * from ncr_item_activity_master");
	}

	public  List<NcrItemActivityRefBean> getNcrItemActivityRefList()
	{
		List<NcrItemActivityRefBean> ncrItemActivityRefBeanList = new ArrayList<NcrItemActivityRefBean>();
		List<NcrItemActivityRef> ncrItemActivityRefList = persistWrapper.readList(NcrItemActivityRef.class,
				"select * from ncr_item_activity_ref");
		if (ncrItemActivityRefList != null)
		{
			for (NcrItemActivityRef ncrItemActivityRefItem : ncrItemActivityRefList)
			{
				ncrItemActivityRefBeanList.add(NcrBeanHelper.getNcrItemActivityRefBean(ncrItemActivityRefItem));
			}
		}
		return ncrItemActivityRefBeanList;

	}

	public  List<NcrItemActivityMasterBean> getNcrItemActivityList(NcrItemOID ncrItemOID)
	{
		return persistWrapper.readList(NcrItemActivityMasterBean.class,
				"select ncr_item_activity_master.* from ncr_item_activity_master inner join ncr_item_activity_ref on ncr_item_activity_master.pk=ncr_item_activity_ref.ncrItemActivityMasterFk where ncr_item_activity_ref.ncrFk = ?",
				ncrItemOID.getPk());
	}

	// public static List<NcrItemActivityRefBean>
	// getNcrItemActivityRefBeanListByNcrFk(int ncrFk)
	// {
	// List<NcrItemActivityRefBean> ncrItemActivityRefBeanList = new
	// ArrayList<NcrItemActivityRefBean>();
	// List<NcrItemActivityRef> ncrItemActivityRefList =
	// PersistWrapper.readList(NcrItemActivityRef.class,
	// "select * from ncr_item_activity_ref where ncrFk =?", ncrFk);
	//
	// for (Iterator<NcrItemActivityRef> iterator =
	// ncrItemActivityRefList.iterator(); iterator.hasNext();)
	// {
	// NcrItemActivityRef ncrItemActivityRefItem = iterator.next();
	// //NcrItemActivityRefBean ncrItemActivityRefBean =
	// NcrItemActivityRefBean.getBean(ncrItemActivityRefItem);
	// NcrItemActivityRefBean ncrItemActivityRefBean =
	// NcrBeanHelper.getNcrItemActivityRefBean(ncrItemActivityRefItem);
	// ncrItemActivityRefBeanList.add(ncrItemActivityRefBean);
	// }g
	// return ncrItemActivityRefBeanList;
	// }

	public NcrGroupBean getNcrGroupBean(NcrGroupOID ncrGroupOID)
	{
		NcrGroup ncrGroupItem = persistWrapper.readByPrimaryKey(NcrGroup.class, ncrGroupOID.getPk());
		return NcrBeanHelper.getBean(ncrGroupItem);
	}

	public  NcrGroupBeanSimple getNcrGroupBeanSimple(NcrGroupOID ncrGroupOID)
	{
		NcrGroup ncrGroupItem = persistWrapper.readByPrimaryKey(NcrGroup.class, ncrGroupOID.getPk());
		return NcrBeanHelper.getNcrGroupSimple(ncrGroupItem);
	}

	public  NcrGroupBean getPublishedNcrGroup(NcrGroupOID ncrGroupOID) throws Exception
	{
		NcrGroup ncrGroupItem = persistWrapper.readByPrimaryKey(NcrGroup.class, ncrGroupOID.getPk());
		NcrGroupBean ncrGroupBean = NcrBeanHelper.getBean(ncrGroupItem);
		if (ncrGroupBean != null && ncrGroupBean.getPk() != 0)
		{
			ncrGroupBean.setNcrItemBeanList(getPublishedNcrItem(ncrGroupOID));
		}

		return ncrGroupBean;
	}

	public  List<String> getDescriptionHistory(PartOID partOID) throws Exception
	{
		return persistWrapper.readList(String.class,
				"SELECT distinct(groupDescription) FROM ncr_group where eStatus=? and partFk=? and groupDescription is  not null order by createdBy desc limit 0,20 ",
				EStatusEnum.Active.getValue(), partOID.getPk());
	}

	public  void deleteNcrGroupByPk(NcrGroupOID ncrGroupOID) throws Exception
	{
		deleteNcrItem(ncrGroupOID);
		persistWrapper.executeUpdate("update ncr_group set eStatus=? where pk=?", EStatusEnum.Deleted.getValue(),
				ncrGroupOID.getPk());
	}

	public  List<NcrGroupQuery> getSimilarNcrGroupList(UserContext context, String searchString,
			ProjectOID projectOID)
	{
		List<NcrGroupQuery> ncrGroupQuerys = null;
		try
		{
			NcrGroupQueryFilter ncrQueryFilter = new NcrGroupQueryFilter();
			if (searchString != null && !searchString.equals(""))
			{
				ncrQueryFilter.setSearchString(searchString);
			}
			if (projectOID != null && projectOID.getPk() != 0)
			{
				List<Integer> projectPks = new ArrayList<Integer>();
				projectPks.add((int) projectOID.getPk());
				ncrQueryFilter.setProjectPks(projectPks);
			}
			NcrGroupQueryBuilder nqBuilder = new NcrGroupQueryBuilder(context, ncrQueryFilter);
			QueryObject result = nqBuilder.getQuery();

			ncrGroupQuerys = persistWrapper.readList(NcrGroupQuery.class, result.getQuery(),
					(result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
							: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrGroupQuerys;
	}

	public  List<NcrGroupQuery> getNcrGroupList(UserContext context, PartOID partOID)
	{
		List<NcrGroupQuery> ncrGroupQuerys = null;
		try
		{
			NcrGroupQueryFilter ncrQueryFilter = new NcrGroupQueryFilter();
			ncrQueryFilter.setPartFk(partOID.getPk());
			ncrQueryFilter.setStatusCountRequired(true);
			NcrGroupQueryBuilder nqBuilder = new NcrGroupQueryBuilder(context, ncrQueryFilter);
			QueryObject result = nqBuilder.getQuery();
			ncrGroupQuerys = persistWrapper.readList(NcrGroupQuery.class, result.getQuery(),
					(result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
							: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrGroupQuerys;
	}

	public  List<NcrGroupQuery> getNcrGroupList(UserContext context, PartOID partOID,
			PartRevisionOID partRevisionOID)
	{
		List<NcrGroupQuery> ncrGroupQuerys = null;
		try
		{
			NcrGroupQueryFilter ncrQueryFilter = new NcrGroupQueryFilter();
			ncrQueryFilter.setPartFk(partOID.getPk());
			ncrQueryFilter.setPartRevisionFk(partRevisionOID.getPk());
			ncrQueryFilter.setStatusCountRequired(true);
			NcrGroupQueryBuilder nqBuilder = new NcrGroupQueryBuilder(context, ncrQueryFilter);
			QueryObject result = nqBuilder.getQuery();
			ncrGroupQuerys = persistWrapper.readList(NcrGroupQuery.class, result.getQuery(),
					(result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
							: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrGroupQuerys;
	}

	public  List<NcrGroupQuery> getLastNcrGroupList(UserContext context, PartOID partOID)
	{
		List<NcrGroupQuery> ncrGroupQuerys = null;
		try
		{
			NcrGroupQueryFilter ncrQueryFilter = new NcrGroupQueryFilter();
			ncrQueryFilter.setPartFk(partOID.getPk());
			ncrQueryFilter.setOffset(0);
			ncrQueryFilter.setCount(3);
			ncrQueryFilter.setStatusCountRequired(true);
			List<NcrEnum.NcrGroupStatus> ncrGroupStatusList = new ArrayList<>();
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.DRAFT);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.PUBLISHED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.APPROVED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.REJECTED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.COMPLETED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.CANCELLED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.CLOSED);

			NcrGroupQueryBuilder nqBuilder = new NcrGroupQueryBuilder(context, ncrQueryFilter);
			QueryObject result = nqBuilder.getQuery();

			ncrGroupQuerys = persistWrapper.readList(NcrGroupQuery.class, result.getQuery(),
					(result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
							: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrGroupQuerys;
	}

	public  List<NcrGroupQuery> getNcrGroupListView(UserContext context, NcrGroupQueryFilter ncrQueryFilter)
	{
		List<NcrGroupQuery> ncrGroupQuerys = null;
		try
		{
			NcrGroupQueryBuilder nqBuilder = new NcrGroupQueryBuilder(context, ncrQueryFilter);
			QueryObject result = nqBuilder.getQuery();
			PersistWrapper p = new PersistWrapper();
			ncrGroupQuerys = p.readList(NcrGroupQuery.class, result.getQuery(),
					(result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
							: null);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrGroupQuerys;
	}

	public static void approveNcrGroupStatus(NcrGroupOID ncrGroupOID)
	{
		try
		{
			List<NcrItemBean> totalNcrItemBean = getNcrItemBeans(ncrGroupOID);
			int openCount = 0, publishCount = 0, rejectedCount = 0, approvedCount = 0, completeCount = 0,
					closedCount = 0, cancelledCount = 0;
			if (totalNcrItemBean != null)
			{
				for (Iterator iterator = totalNcrItemBean.iterator(); iterator.hasNext();)
				{
					NcrItemBean ncrItemBean = (NcrItemBean) iterator.next();
					if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString()))
					{
						openCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.PUBLISHED.toString()))
					{
						publishCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.REJECTED.toString()))
					{
						rejectedCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString()))
					{
						approvedCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.COMPLETED.toString()))
					{
						completeCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString()))
					{
						closedCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CANCELLED.toString()))
					{
						cancelledCount++;
					}
				}
				if (openCount > 0)
				{
					PersistWrapper.executeUpdate(
							"update ncr_group set status='" + NcrEnum.NcrGroupStatus.DRAFT.toString() + "' where pk=?",
							ncrGroupOID.getPk());
				} else if (rejectedCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.REJECTED.toString() + "' where pk=?", ncrGroupOID.getPk());

				} else if (publishCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.PUBLISHED.toString() + "' where pk=?", ncrGroupOID.getPk());
				} else if (approvedCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.APPROVED.toString() + "' where pk=?", ncrGroupOID.getPk());
				} else if (completeCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.COMPLETED.toString() + "' where pk=?", ncrGroupOID.getPk());
				} else if (closedCount > 0)
				{
					PersistWrapper.executeUpdate(
							"update ncr_group set status='" + NcrEnum.NcrGroupStatus.CLOSED.toString() + "' where pk=?",
							ncrGroupOID.getPk());
				} else if (cancelledCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.CANCELLED.toString() + "' where pk=?", ncrGroupOID.getPk());
				}
			}
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}

	}

	public static void approveNcrItemStatus(UserContext context, NcrItemOID ncrItemOID)
	{
		try
		{
			NcrItemBean ncrItemBean = getNcrItemBean(ncrItemOID);

			// List<NcrFunctionBean> listNcrFunctionBean =
			// getCurrentNcrFunctions(ncrItemOID.getPk(),
			// EntityTypeEnum.NCR.toString());
			int publishCount = 0, approvedCount = 0, rejectedCount = 0;

			if (ncrItemBean.getNcrFunctionBeanList() != null)
			{
				for (NcrFunctionBean ncrFunctionBean : ncrItemBean.getNcrFunctionBeanList())
				{
					NcrFunctionMaster nfMaster = getNcrFunctionMaster(ncrFunctionBean.getFunctionMasterFk());
					if (ncrFunctionBean.getApprovedStatus() == null && ncrFunctionBean.getApproveRequired() != null
							&& ncrFunctionBean.getApproveRequired() && !nfMaster.getSpecial())
					{
						publishCount++;
					} else if (ncrFunctionBean.getApprovedStatus() != null && ncrFunctionBean.getApprovedStatus()
							.equals(NcrEnum.NcrFunctionStatus.APPROVED.toString()))
					{
						approvedCount++;
					} else if (ncrFunctionBean.getApprovedStatus() != null && ncrFunctionBean.getApprovedStatus()
							.equals(NcrEnum.NcrFunctionStatus.REJECTED.toString()))
					{
						rejectedCount++;
					}
				}
				if (rejectedCount > 0)
				{
					PersistWrapper.executeUpdate(
							"update ncr set ncrStatus='" + NcrEnum.NcrItemStatus.REJECTED.toString() + "' where pk=?",
							ncrItemOID.getPk());
				} else if (publishCount > 0)
				{
					PersistWrapper.executeUpdate(
							"update ncr set ncrStatus='" + NcrEnum.NcrItemStatus.PUBLISHED.toString() + "' where pk=?",
							ncrItemOID.getPk());
					if (!ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.PUBLISHED.toString()))
					{
						List<NcrUnitAssignBean> listBean = getNcrUnitAssign(ncrItemOID);
						if (listBean != null)
						{
							for (NcrUnitAssignBean ncrUnitAssignBean : listBean)
							{
								ncrUnitAssignBean.setStatus(OpenItemV2.StatusEnum.Draft.name());
								saveNcrUnitAssign(context, ncrUnitAssignBean);
							}
						}
					}

				} else if (approvedCount > 0)
				{
					PersistWrapper.executeUpdate(
							"update ncr set ncrStatus='" + NcrEnum.NcrItemStatus.APPROVED.toString() + "' where pk=?",
							ncrItemOID.getPk());

					if (!ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString()))
					{
						List<NcrUnitAssignBean> listBean = getNcrUnitAssign(ncrItemOID);
						if (listBean != null)
						{
							for (NcrUnitAssignBean ncrUnitAssignBean : listBean)
							{
								if (ncrUnitAssignBean.getStatus() == null
										|| ncrUnitAssignBean.getStatus().equals(OpenItemV2.StatusEnum.Draft.name()))
								{
									publishNcrUnitAssign(context, ncrUnitAssignBean);
								}
							}
						}
					}
					// if (ncrItemBean.getOriginType() != null
					// &&
					// ncrItemBean.getOriginType().equals(EntityTypeEnum.MRFPartAssign.name()))
					// {
					// // if NCR approved then MRF should be complete
					// //MRFManager.completeRequest(context, new
					// MRFPartAssignOID(ncrItemBean.getOriginPk()), "");
					// }
				}
			}
			setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}

	}

	public static List<NcrItemBean> publishGroupCheck(NcrGroupOID ncrGroupOID)
	{
		List<NcrItemBean> unAssignedNcrItemBeanList = new ArrayList<NcrItemBean>();
		List<NcrItemBean> listNcrItemBean = getNcrItemBeans(ncrGroupOID);
		for (NcrItemBean ncrItemBean : listNcrItemBean)
		{
			Integer approvedFunctionCount = 0;
			List<NcrFunctionBean> ncrFunctionBeanList = getCurrentNcrFunctions(ncrItemBean.getPk(),
					EntityTypeEnum.NCR.toString());
			if (ncrFunctionBeanList != null && ncrFunctionBeanList.size() != 0)
			{
				for (Iterator iterator = ncrFunctionBeanList.iterator(); iterator.hasNext();)
				{
					NcrFunctionBean singleNcrFunctionBean = (NcrFunctionBean) iterator.next();
					if (singleNcrFunctionBean != null && singleNcrFunctionBean.getApproveRequired() != null
							&& singleNcrFunctionBean.getApproveRequired())
					{
						approvedFunctionCount++;
					}
				}
			}
			if (ncrFunctionBeanList == null || ncrFunctionBeanList.size() == 0 || ncrItemBean.getQuantity() == null
					|| ncrItemBean.getQuantity() == 0 || ncrItemBean.getNcrFailureCodeMasterBean() == null
					|| ncrItemBean.getNcrFailureCodeMasterBean().getPk() == 0 || ncrItemBean.getCustodianOID() == null
					|| ncrItemBean.getNcrAreaOfResponsibilityBean() == null
					|| ncrItemBean.getNcrAreaOfResponsibilityBean().getPk() == 0 || ncrItemBean.getSourcePk() == null
					|| ncrItemBean.getSourcePk() == 0 || ncrItemBean.getUnitOfMeasures() == null
					|| ncrItemBean.getUnitOfMeasures().getPk() == 0 || approvedFunctionCount < 2)
			{
				unAssignedNcrItemBeanList.add(ncrItemBean);

			}
		}
		return unAssignedNcrItemBeanList;

	}

	public static List<NcrItemBean> getEmptyDispositionNcrItem(NcrGroupOID ncrGroupOID)
	{
		List<NcrItemBean> unAssignedNcrItemBeanList = new ArrayList<NcrItemBean>();
		List<NcrItemBean> listNcrItemBean = getNcrItemBeans(ncrGroupOID);
		if (listNcrItemBean != null)
		{
			for (NcrItemBean ncrItemBean : listNcrItemBean)
			{
				List<NcrFunctionBean> ncrFunctionBeanList = getCurrentNcrFunctions(ncrItemBean.getPk(),
						EntityTypeEnum.NCR.toString());
				if (ncrFunctionBeanList == null || ncrFunctionBeanList.size() == 0
						|| ncrItemBean.getNcrDispositionBean() == null
						|| ncrItemBean.getNcrDispositionBean().getPk() == 0 || ncrFunctionBeanList.size() < 2)
				{
					unAssignedNcrItemBeanList.add(ncrItemBean);
				}
			}
		}
		return unAssignedNcrItemBeanList;

	}

	public static List<NcrItemBean> getPendingApproveNcrList(NcrGroupOID ncrGroupOID)
	{
		List<NcrItemBean> unAssignedNcrItemBeanList = new ArrayList<NcrItemBean>();
		List<NcrItemBean> listNcrItemBean = getNcrItemBeans(ncrGroupOID);
		if (listNcrItemBean != null)
		{
			for (NcrItemBean ncrItemBean : listNcrItemBean)
			{
				if (!NcrEnum.NcrItemStatus.DRAFT.toString().equals(ncrItemBean.getNcrStatus())
						&& ncrItemBean.getNcrDispositionBean() != null
						&& ncrItemBean.getNcrDispositionBean().getPk() > 0)
				{
					List<NcrFunctionBean> notApprovedList = ncrItemBean.getNcrFunctionBeanList().stream()
							.filter(ncrFunction -> ncrFunction.getApprovedBy() == null).collect(Collectors.toList());
					if (notApprovedList != null && notApprovedList.size() > 0)
					{
						unAssignedNcrItemBeanList.add(ncrItemBean);
					}
				}
			}
		}
		return unAssignedNcrItemBeanList;

	}

	public static List<NcrItemListReportResultRow> getMyNcrApprovalList(UserContext context)
	{
		NcrItemListReportFilter filter = new NcrItemListReportFilter();
		List<UserOID> listUsers = new ArrayList<UserOID>();
		listUsers.add(context.getUser().getOID());
		filter.setFunctionApprovalPendingFor(listUsers);
		filter.setDispositionIsSet(DispositionIsSet.Set);
		if (filter.getNcrItemStatusList() == null || filter.getNcrItemStatusList().size() == 0)
			filter.setNcrItemStatusList(Arrays.asList(new NcrItemStatus[] { NcrEnum.NcrItemStatus.PUBLISHED }));

		ReportRequest req = new ReportRequest(ReportTypes.NcrItemListReport);
		req.setFetchAllRows(true);
		req.setFilter(filter);
		ReportResponse resp = ReportsDelegate.runReport(context, req);
		if (resp == null && resp.getReportData() == null)
			return null;
		return (List<NcrItemListReportResultRow>) resp.getReportData();
	}

	public static List<NcrItemListReportResultRow> getMyNcrAkcnowledgeList(UserContext context)
	{
		NcrItemListReportFilter filter = new NcrItemListReportFilter();
		List<UserOID> listUsers = new ArrayList<UserOID>();
		listUsers.add(context.getUser().getOID());
		filter.setSpecialProcessPendingFor(listUsers);
		filter.setDispositionIsSet(DispositionIsSet.Set);
		if (filter.getNcrItemStatusList() == null || filter.getNcrItemStatusList().size() == 0)
			filter.setNcrItemStatusList(Arrays.asList(new NcrItemStatus[] { NcrEnum.NcrItemStatus.PUBLISHED,
					NcrEnum.NcrItemStatus.APPROVED, NcrEnum.NcrItemStatus.COMPLETED, NcrEnum.NcrItemStatus.CLOSED }));

		ReportRequest req = new ReportRequest(ReportTypes.NcrItemListReport);
		req.setFetchAllRows(true);
		req.setFilter(filter);
		ReportResponse resp = ReportsDelegate.runReport(context, req);
		if (resp == null && resp.getReportData() == null)
			return null;
		return (List<NcrItemListReportResultRow>) resp.getReportData();
	}

	public static Date getNCRapprovedDate(NcrItemOID ncrItemOID)
	{

		if (ncrItemOID == null || ncrItemOID.getPk() < 1)
			return null;
		StringBuffer sql = new StringBuffer("SELECT ncr_function.approvedDate");
		sql.append(" FROM ncr ");
		sql.append(" inner join ncr_function on ncr.pk=ncr_function.objectFk and ncr_function.objectType='NCR' ");
		sql.append(" where ncr.ncrStatus in ('APPROVED','COMPLETED','CLOSED') ");
		sql.append(" and ncr.pk=? ");
		sql.append(" order by ncr_function.approvedDate desc ");
		sql.append(" Limit 1 ");
		return PersistWrapper.read(Date.class, sql.toString(), ncrItemOID.getPk());
	}

	// Ncr Item
	public static NcrItemBean saveNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		NcrItemBean prevNcrItemBean = null;
		List<NcrCorrectiveActionBean> newCorrectiveActionBeans = new ArrayList<NcrCorrectiveActionBean>();
		boolean addedActionsAfterPublishing = false;
		if (ncrItemBean.getPk() > 0)
		{
			prevNcrItemBean = getNcrItemBean(new NcrItemOID(ncrItemBean.getPk()));
		}
		FormItemResponse formItemResponse = null;
		if (ncrItemBean instanceof NcrItemBeanTransfer)
			formItemResponse = ((NcrItemBeanTransfer) ncrItemBean).getFormItemResponse();

		NcrItem ncrItem = null;
		String oldDisposition = "", newDisposition = "", oldAreaOfResponsibility = "", newAreaOfResponsibility = "",
				oldReworkOrder = "", newReworkOrder = "";
		Boolean isDispositionChange = false, isAreaOfResponsibilityChange = false, isReworkOrderChange = false;

		int ncrItemPk;
		if (ncrItemBean.getPk() != 0)
		{
			ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemBean.getPk());
			if (ncrItem.getLastUpdated().after(ncrItemBean.getLastUpdated()))
				throw new AppException(
						"The data you are viewing is not current and it cannot be saved, Please re-open the ncr and try again");
		} else
		{
			ncrItem = new NcrItem();
			ncrItem.setCreatedBy(context.getUser().getPk());
			ncrItem.seteStatus(EStatusEnum.Active.getValue());
			ncrItem.setCreatedDate(new Date());
		}
		if (ncrItemBean.getNcrDispositionBean() != null && ncrItemBean.getNcrDispositionBean().getPk() != 0
				&& ncrItem.getDispositionFk() != null && ncrItem.getDispositionFk() != 0)
		{
			if (ncrItemBean.getNcrDispositionBean().getPk() != ncrItem.getDispositionFk())
			{
				isDispositionChange = true;
				LinkedHashMap<Integer, DBEnum> dispositions = DBEnum.getEnumList(NcrDispositionTypes.class);
				for (Iterator iterator1 = dispositions.values().iterator(); iterator1.hasNext();)
				{
					DBEnum user = (DBEnum) iterator1.next();
					if (user.getValue() == ncrItemBean.getNcrDispositionBean().getPk())
					{
						newDisposition = user.getName();
					} else if (user.getValue() == ncrItem.getDispositionFk())
					{
						oldDisposition = user.getName();
					}
				}
				updatePrevNcrFunction(ncrItemBean.getPk(), EntityTypeEnum.NCR.toString());
			}
		}

		if (ncrItemBean.getNcrAreaOfResponsibilityBean() != null
				&& ncrItemBean.getNcrAreaOfResponsibilityBean().getPk() != 0
				&& ncrItem.getAreaOfResponsibilityFk() != 0)
		{
			if (ncrItemBean.getNcrAreaOfResponsibilityBean().getPk() != ncrItem.getAreaOfResponsibilityFk())
			{

				isAreaOfResponsibilityChange = true;
				NcrAreaOfResponsibilityBean newAreaOfResponsibilityBean = NcrManager
						.getNcrAreaOfResponsibility(ncrItemBean.getNcrAreaOfResponsibilityBean().getPk());
				if (newAreaOfResponsibilityBean != null && newAreaOfResponsibilityBean.getPk() != 0)
				{
					newAreaOfResponsibility = newAreaOfResponsibilityBean.getName();
				}

				NcrAreaOfResponsibilityBean existAreaOfResponsibilityBean = NcrManager
						.getNcrAreaOfResponsibility(ncrItem.getAreaOfResponsibilityFk());
				if (existAreaOfResponsibilityBean != null && existAreaOfResponsibilityBean.getPk() != 0)
				{
					oldAreaOfResponsibility = existAreaOfResponsibilityBean.getName();
				}

			}
		}
		if (ncrItem.getReworkOrderFk() != null && ncrItemBean.getReworkOrderOID() != null
				&& ncrItem.getReworkOrderFk() != ncrItemBean.getReworkOrderOID().getPk())
		{
			isReworkOrderChange = true;

			newReworkOrder = ncrItemBean.getReworkOrderOID().getDisplayText();

			ReworkOrderBean oldReworkOrderBean = PartsNewManager.getReworkOrderBeanByPk(ncrItem.getReworkOrderFk());
			oldReworkOrder = oldReworkOrderBean.getReworkOrderNumber();
		}

		ncrItem.setOriginPk(ncrItemBean.getOriginPk());
		ncrItem.setOriginType(ncrItemBean.getOriginType());
		ncrItem.setNcrGroupFk(ncrItemBean.getNcrGroupFk());

		if (ncrItemBean.getCustodianOID() != null)
		{
			ncrItem.setCustodianPk(ncrItemBean.getCustodianOID().getPk());
		} else
		{
			ncrItem.setCustodianPk(null);
		}

		ncrItem.setMrfNo(ncrItemBean.getMrfNo());
		ncrItem.setNcrDesc(ncrItemBean.getNcrDesc());
		if (ncrItemBean.getNcrAreaOfResponsibilityBean() != null)
		{
			ncrItem.setAreaOfResponsibilityFk(ncrItemBean.getNcrAreaOfResponsibilityBean().getPk());
		} else
		{
			ncrItem.setAreaOfResponsibilityFk(0);
		}
		if (ncrItemBean.getNcrDispositionBean() != null)
		{
			ncrItem.setDispositionFk(ncrItemBean.getNcrDispositionBean().getPk());
		} else
		{
			ncrItem.setDispositionFk(null);
		}
		if (ncrItemBean.getUnitOfMeasures() != null)
		{
			ncrItem.setUnitOfMeasureFk(ncrItemBean.getUnitOfMeasures().getPk());
		} else
		{
			ncrItem.setUnitOfMeasureFk(null);
		}
		if (ncrItemBean.getNcrFailureCodeMasterBean() != null)
		{
			ncrItem.setNcrFailureCodeFk(ncrItemBean.getNcrFailureCodeMasterBean().getPk());
		} else
		{
			ncrItem.setNcrFailureCodeFk(null);
		}
		ncrItem.setDispositionComment(ncrItemBean.getDispositionComment());

		ncrItem.setSourcePk(ncrItemBean.getSourcePk());
		ncrItem.setSourceType(ncrItemBean.getSourceType());
		ncrItem.setSourceOther(ncrItemBean.getSourceOther());
		ncrItem.setLocationPk(ncrItemBean.getLocationPk());
		ncrItem.setLocationType(ncrItemBean.getLocationType());
		ncrItem.setLocationOther(ncrItemBean.getLocationOther());
		ncrItem.setNcrStatus(ncrItemBean.getNcrStatus());
		ncrItem.setQuantity(ncrItemBean.getQuantity());

		ncrItem.setForecastStartDate(ncrItemBean.getForecastStartDate());
		ncrItem.setForecastCompletionDate(ncrItemBean.getForecastCompletionDate());
		ncrItem.setCarType(ncrItemBean.getCarType());
		ncrItem.setHours(ncrItemBean.getHours());
		ncrItem.setSeverity(ncrItemBean.getSeverity());
		ncrItem.setOccurrence(ncrItemBean.getOccurrence());
		ncrItem.setDetection(ncrItemBean.getDetection());
		ncrItem.setEscape(ncrItemBean.getEscape());
		ncrItem.setPpsor8d(ncrItemBean.getPpsor8d());

		ncrItem.setPriority(ncrItemBean.getPriority());

		if (ncrItemBean.getReworkOrderOID() != null)
			ncrItem.setReworkOrderFk(ncrItemBean.getReworkOrderOID().getPk());
		else
			ncrItem.setReworkOrderFk(null);

		ncrItem.setRootCause(ncrItemBean.getRootCause());
		ncrItem.setWorkInstructionOrComment(ncrItemBean.getWorkInstructionOrComment());
		ncrItem.setWorkOrderRef(ncrItemBean.getWorkOrderRef());
		ncrItem.setErpReference(ncrItemBean.getErpReference());
		ncrItem.setSqpid(ncrItemBean.getSqpid());
		if (ncrItemBean.getPk() != 0)
		{
			ncrItem.setLastUpdated(new Date()); // TODO:: for some reason the
												// auto timestamp update in the
												// DB has stopped working.
			PersistWrapper.update(context, ncrItem);
			ncrItemPk = ncrItem.getPk();
		} else
		{
			ncrItemPk = PersistWrapper.createEntity(ncrItem);
			ncrItem.setPk(ncrItemPk);
			ncrItem.setNcrNo("TNCR-" + ncrItemPk);
			ncrItem.setLastUpdated(new Date()); // TODO:: for some reason the
												// auto timestamp update in the
												// DB has stopped working.
			PersistWrapper.update(context, ncrItem);

			// Notify the NcrEventsListener
			NcrEventsListener.notifyNcrItemCreated(context, ncrItem, formItemResponse);

		}
		boolean isApproved = true;
		List<NcrFunctionBean> previousFunctionBeans = getCurrentNcrFunctions(ncrItemPk, EntityTypeEnum.NCR.toString());
		if (previousFunctionBeans == null)
		{
			previousFunctionBeans = new ArrayList<NcrFunctionBean>();
		}
		if (ncrItemBean.getNcrFunctionBeanList() != null)
		{
			for (NcrFunctionBean ncrFunctionBean : ncrItemBean.getNcrFunctionBeanList())
			{
				if (ncrFunctionBean != null)
				{
					if (previousFunctionBeans.contains(ncrFunctionBean))
					{
						previousFunctionBeans.remove(ncrFunctionBean);
					}
					if (ncrFunctionBean.getPk() == 0)
					{
						ncrFunctionBean.setObjectFk(ncrItemPk);
						ncrFunctionBean.setObjectType(EntityTypeEnum.NCR.toString());
					}
					if (ncrItemBean.getNcrDispositionBean() != null)
					{
						ncrFunctionBean.setDispositionFk(ncrItemBean.getNcrDispositionBean().getPk());
					}
					ncrFunctionBean.setDispositionComment(ncrItemBean.getDispositionComment());
					ncrFunctionBean = saveNcrFunction(context, ncrFunctionBean);
					if ((ncrFunctionBean.getApprovedStatus() == null
							|| ncrFunctionBean.getApprovedStatus().equals(NcrEnum.NcrFunctionStatus.REJECTED.name()))
							&& (ncrFunctionBean.getApproveRequired() != null && ncrFunctionBean.getApproveRequired()))
					{
						isApproved = false;
					}
				}
			}
		}
		deleteNcrFunctions(previousFunctionBeans);

		List<NcrUnitAssignBean> previousUnitAssignList = getNcrUnitAssign(new NcrItemOID(ncrItemPk, ""));
		if (previousUnitAssignList == null)
		{
			previousUnitAssignList = new ArrayList<NcrUnitAssignBean>();
		}
		boolean isUnitUpdatedAftercompletNCR = false;
		if (ncrItemBean.getNcrUnitAssignBeanList() != null)
		{
			for (NcrUnitAssignBean ncrUnitAssignBean : ncrItemBean.getNcrUnitAssignBeanList())
			{
				if (ncrUnitAssignBean.getQuantity() == null)
				{
					ncrUnitAssignBean.setQuantity(0f);
				}
				ncrUnitAssignBean.setNcrFk(ncrItemPk);
				// ncrUnitAssignBean.setPartFk(ncrItemBean.getPartOID().getPk());
				// if (ncrItemBean.getPartRevisionOID() != null &&
				// ncrItemBean.getPartRevisionOID().getPk() != 0)
				// {
				// ncrUnitAssignBean.setRevisionFk(ncrItemBean.getPartRevisionOID().getPk());
				// }
				// ncrUnitAssignBean.setProjectFk(ncrItemBean.getProjectOID().getPk());
				boolean unitAddedAfterCompleteOrClosed = false;
				String targetStatus = null;
				if (ncrItemBean.getNcrStatus() != null)
				{
					if (ncrUnitAssignBean.getPk() == 0
							&& (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString())
									|| ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.PUBLISHED.toString())))
					{
						ncrUnitAssignBean.setStatus(OpenItemV2.StatusEnum.Draft.name());
					} else if (ncrUnitAssignBean.getPk() == 0
							&& ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString()))
					{
						if (ncrUnitAssignBean.getStatus() == null || (ncrUnitAssignBean.getStatus() != null
								&& ncrUnitAssignBean.getStatus().equals(OpenItemV2.StatusEnum.Draft.name())))
							ncrUnitAssignBean.setStatus(OpenItemV2.StatusEnum.Open.name());
					} else if (ncrUnitAssignBean.getPk() == 0 && ncrUnitAssignBean.getStatus() != null
							&& (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.COMPLETED.toString())
									|| ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString())))
					{
						unitAddedAfterCompleteOrClosed = true;
						targetStatus = ncrUnitAssignBean.getStatus();
						ncrUnitAssignBean.setStatus(OpenItemV2.StatusEnum.Open.name());
					}
				}
				ncrUnitAssignBean = saveNcrUnitAssign(context, ncrUnitAssignBean);
				if (unitAddedAfterCompleteOrClosed && targetStatus != null)
				{
					isUnitUpdatedAftercompletNCR = true;
					if (targetStatus.equals(OpenItemV2.StatusEnum.Completed.name()))
					{
						ncrUnitAssignBean.setCompletedComment("Created as Completed");
						ncrUnitAssignBean = completeNcrUnitAssign(context, ncrUnitAssignBean);

					} else if (targetStatus.equals(OpenItemV2.StatusEnum.Closed.name()))
					{
						ncrUnitAssignBean.setCompletedComment("Completed and closed while creating It");
						ncrUnitAssignBean = completeNcrUnitAssign(context, ncrUnitAssignBean);
						ncrUnitAssignBean.setClosedComment("Completed and closed while creating It");
						ncrUnitAssignBean = closeNcrUnitAssign(context, ncrUnitAssignBean);

					}
					User user = AccountManager.getUser(context.getUser().getPk());
					ProjectManager
							.addComment(EtestApplication.getInstance().getUserContext(), ncrItemPk,
									EntityTypeEnum.NCR.getValue(), ncrUnitAssignBean.getUnitName().toString()
											+ " added to this NCR by " + user.getDisplayString(),
									Mode.COMMENTCONTEXT_GENERAL);
					PersistWrapper.update(context, ncrItem);

				}
				if (previousUnitAssignList.contains(ncrUnitAssignBean))
				{
					previousUnitAssignList.remove(ncrUnitAssignBean);
				}

			}
		}
		for (NcrUnitAssignBean ncrUnitAllocBean : previousUnitAssignList)
		{
			deleteNcrUnitAssign(ncrUnitAllocBean.getPk());
		}
		if (isApproved && ncrItemBean.getNcrStatus() != null
				&& (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.PUBLISHED.toString())))
		{
			approveNcrItemStatus(context, new NcrItemOID(ncrItemPk, ""));
		}
		List<NcrItemActivityRefBean> prevNcrItemActivityList = getNcrItemActivityRefBeanListByObjectPkAndObjectType(
				ncrItemPk, EntityTypeEnum.NCR.toString());

		if (prevNcrItemActivityList == null)
		{
			prevNcrItemActivityList = new ArrayList<NcrItemActivityRefBean>();
		}
		if (ncrItemBean.getNcrItemActivityRefBeanList() != null
				&& ncrItemBean.getNcrItemActivityRefBeanList().size() > 0)
		{
			for (NcrItemActivityRefBean ncrItemActivityRefBean : ncrItemBean.getNcrItemActivityRefBeanList())
			{
				if (prevNcrItemActivityList.contains(ncrItemActivityRefBean))
				{
					prevNcrItemActivityList.remove(ncrItemActivityRefBean);
				}
				ncrItemActivityRefBean.setObjectPk(ncrItemPk);
				ncrItemActivityRefBean.setObjectType(EntityTypeEnum.NCR.toString());
				ncrItemActivityRefBean = saveNcrItemActivityRefBean(context, ncrItemActivityRefBean);
			}
		}
		if (prevNcrItemActivityList.size() > 0)
		{
			for (NcrItemActivityRefBean ncrItemActivityRefBean : prevNcrItemActivityList)
			{
				deleteNcrItemActivityRefByPk(ncrItemActivityRefBean.getPk());
			}

		}
		// resource requirement

		List<ResourceRequirementBean> previousResourceRequirementList = getResourceRequirementBeanListByObjectPkAndObjectType(
				ncrItemPk, EntityTypeEnum.NCR.toString());

		if (previousResourceRequirementList == null)
		{
			previousResourceRequirementList = new ArrayList<ResourceRequirementBean>();
		}
		if (ncrItemBean.getResourceRequirementBeanList() != null
				&& ncrItemBean.getResourceRequirementBeanList().size() > 0)
		{
			for (ResourceRequirementBean resourceRequirementBean : ncrItemBean.getResourceRequirementBeanList())
			{
				resourceRequirementBean.setObjectPk(ncrItemPk);
				resourceRequirementBean.setObjectType(EntityTypeEnum.NCR.toString());
				int pos = previousResourceRequirementList.indexOf(resourceRequirementBean);
				if (previousResourceRequirementList.contains(resourceRequirementBean))
				{
					previousResourceRequirementList.remove(resourceRequirementBean);
				}
				resourceRequirementBean = saveResourceRequirementBean(context, resourceRequirementBean);
			}
		}

		if (previousResourceRequirementList.size() > 0)
		{
			for (ResourceRequirementBean resourceRequirementBean : previousResourceRequirementList)
			{
				deleteResourceRequirementByPk(resourceRequirementBean.getPk());
			}

		}

		if (ncrItemBean.getAttachments() != null)
		{
			CommonServiceManager.saveAttachments(EtestApplication.getInstance().getUserContext(), ncrItemPk,
					EntityTypeEnum.NCR.getValue(), ncrItemBean.getAttachments(), true);
		}
		if (ncrItemBean.getPpsOr8DAttachment() != null)
		{
			for (Iterator iterator = ncrItemBean.getPpsOr8DAttachment().iterator(); iterator.hasNext();)
			{
				Attachment attachment = (Attachment) iterator.next();
				attachment.setAttachContext(NcrItem.NCR_PPSOR8D);
			}
			CommonServiceManager.saveAttachments(EtestApplication.getInstance().getUserContext(), ncrItemPk,
					EntityTypeEnum.NCR.getValue(), NcrItem.NCR_PPSOR8D, ncrItemBean.getPpsOr8DAttachment());
		}

		List<AdditionalPartsBean> previousPartRequiredBean = AdditionalPartsManager.getList(ncrItemPk,
				EntityTypeEnum.NCR);
		if (previousPartRequiredBean == null)
		{
			previousPartRequiredBean = new ArrayList<AdditionalPartsBean>();
		}
		if (ncrItemBean.getNcrPartsRequiredBeanList() != null)
		{
			for (AdditionalPartsBean ncrPartsRequiredBean : ncrItemBean.getNcrPartsRequiredBeanList())
			{
				if (ncrPartsRequiredBean.getPartName().trim().length() > 0)
				{
					ncrPartsRequiredBean.setObjectPk(ncrItemPk);
					ncrPartsRequiredBean.setObjectType(Integer.toString(EntityTypeEnum.NCR.getValue()));
					ncrPartsRequiredBean = AdditionalPartsManager.save(context, ncrPartsRequiredBean);
					if (previousPartRequiredBean.contains(ncrPartsRequiredBean))
					{
						previousPartRequiredBean.remove(ncrPartsRequiredBean);
					}
				}
			}
		}
		AdditionalPartsManager.delete(previousPartRequiredBean);
		*//*
		 * Corrective Action start
		 *//*

		List<NcrCorrectiveActionBean> prevNcrCorrctive = getNcrCorrectiveAction(new NcrItemOID(ncrItemPk));

		if (prevNcrCorrctive == null)
			prevNcrCorrctive = new ArrayList<NcrCorrectiveActionBean>();

		if (ncrItemBean.getNcrCorrectiveActionBeans() != null)
		{
			for (NcrCorrectiveActionBean bean : ncrItemBean.getNcrCorrectiveActionBeans())
			{
				boolean sendMail = false;
				if (bean.getPk() == 0 && !ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString()))
				{
					sendMail = true;
					addedActionsAfterPublishing = true;
				}
				bean.setNcrFk(ncrItemPk);
				NcrCorrectiveActionBean cbean = saveNcrCorrectiveAction(context, bean);
				if (sendMail)
				{
					newCorrectiveActionBeans.add(cbean);
				}

				prevNcrCorrctive.remove(bean);
			}
		}

		if (prevNcrCorrctive != null)
		{
			for (NcrCorrectiveActionBean bean : prevNcrCorrctive)
			{
				deleteNcrCorrectiveAction(context, bean.getPk());
			}
		}
		*//*
		 * Corrective action end
		 *//*

		// for (int i = 0; i < ncrItemBean.getNcrPartsRequiredBeanList().size();
		if (ncrItemBean.getNcrStatus() != null
				&& (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.REJECTED.toString())
						|| ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString())))
		{
			approveNcrItemStatus(context, new NcrItemOID(ncrItemPk, ""));
		}

		if (isUnitUpdatedAftercompletNCR)
		{
			*//*
			 * If we add effected unit after complete or close NCR
			 *//*
			updateNCRItemStatus(context, prevNcrItemBean, new NcrItemOID(ncrItemPk));

		}
		ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItemPk, ""));

		*//*
		 * Sent a mail to quality users if the D8 values updated. - start 8D
		 * update information checked in the notifyD8Updated method.
		 * prevNcrItemBean is null only if we create a new NCR item
		 *//*
		if (prevNcrItemBean != null && prevNcrItemBean.getPk() > 0)
		{
			NcrEmailSender.notifyD8Updated(context, prevNcrItemBean, ncrItemBean);
		}
		*//*
		 * Sent a mail to quality users if the D8 values updated - end
		 *//*
		setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));

		if (isDispositionChange && ncrItemBean.getNcrStatus() != null
				&& !ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString()))
		{
			NcrEmailSender.notifyNcrDispositionChange(context, ncrItemBean, oldDisposition, newDisposition);
		}
		if (isAreaOfResponsibilityChange && ncrItemBean.getNcrStatus() != null
				&& !ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString()))
		{
			NcrEmailSender.notifyNcrAreaOfResponsibilityChange(context, ncrItemBean, oldAreaOfResponsibility,
					newAreaOfResponsibility);
		}
		if (isReworkOrderChange && ncrItemBean.getNcrStatus() != null
				&& !ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString()))
		{
			NcrEmailSender.notifyNcrReworkOrderChange(context, ncrItemBean, oldReworkOrder, newReworkOrder);
		}

		if (addedActionsAfterPublishing)
		{

			NcrEmailSender.notifyRecommendedActionCreated(context, ncrItemBean, newCorrectiveActionBeans);

		}

		return ncrItemBean;

	}

	private static void updateNCRItemStatus(UserContext context, NcrItemBean prevNcrItemBean, NcrItemOID ncrItemOID)
			throws Exception
	{
		List<NcrUnitAssignBean> ncrUnitAssignBeans = getNcrUnitAssign(ncrItemOID);
		User updatedUser = AccountManager.getUser(context.getUser().getPk());
		if (ncrItemOID == null || ncrItemOID.getPk() < 0 || ncrUnitAssignBeans == null
				|| ncrUnitAssignBeans.size() == 0)
		{
			return;
		}
		int opencount = 0, completedCount = 0, closedCount = 0;

		StringBuffer units = new StringBuffer();
		String comma = "";
		if (ncrUnitAssignBeans != null)
		{
			for (NcrUnitAssignBean bean : ncrUnitAssignBeans)
			{
				units.append(comma).append(bean.getUnitName());
				comma = ",";
				if (OpenItemV2.StatusEnum.Open.name().equals(bean.getStatus()))
				{
					opencount++;
				} else if (OpenItemV2.StatusEnum.Completed.name().equals(bean.getStatus()))
				{
					completedCount++;
				} else if (OpenItemV2.StatusEnum.Closed.name().equals(bean.getStatus()))
				{
					closedCount++;
				}
			}
		}
		StringBuffer comment = new StringBuffer();
		NcrItem ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemOID.getPk());
		if (ncrItem != null && (ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.COMPLETED.toString())
				|| ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString())))
		{
			if (opencount > 0 && (ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.COMPLETED.toString())
					|| ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString())))
			{
				ncrItem.setVerifiedBy(null);
				ncrItem.setVerifiedDate(null);
				ncrItem.setVerifiedComment(null);
				ncrItem.setClosedBy(null);
				ncrItem.setClosedDate(null);
				ncrItem.setClosedComment(null);
				ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.APPROVED.toString());
				comment.append("Status of NCR changed from ").append(prevNcrItemBean.getNcrStatus()).append(" to ")
						.append(NcrEnum.NcrItemStatus.APPROVED.toString()).append(" by ")
						.append(updatedUser.getDisplayString());

			} else if (completedCount > 0 && ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString()))
			{
				ncrItem.setClosedBy(null);
				ncrItem.setClosedDate(null);
				ncrItem.setClosedComment(null);
				ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.COMPLETED.toString());
				comment.append("Status of NCR changed from ").append(prevNcrItemBean.getNcrStatus()).append(" to ")
						.append(NcrEnum.NcrItemStatus.COMPLETED.toString()).append(" by ")
						.append(updatedUser.getDisplayString());
			} else if (closedCount > 0)
			{
				comment = null;
			}
			PersistWrapper.update(context, ncrItem);
			setNcrGroupStatus(new NcrGroupOID(ncrItem.getNcrGroupFk(), ""));
			NcrEmailSender.notifyNcrItemUnitsUpdated(context, prevNcrItemBean, getNcrItemBean(ncrItemOID));
			if (comment != null)
			{
				ProjectManager.addComment(EtestApplication.getInstance().getUserContext(), ncrItem.getPk(),
						EntityTypeEnum.NCR.getValue(), comment.toString(), Mode.COMMENTCONTEXT_GENERAL);

			}

		}

	}

	public static NcrItemBean copyNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		NcrItem ncrItem = null;
		int ncrItemPk;
		ncrItem = new NcrItem();
		ncrItem.seteStatus(EStatusEnum.Active.getValue());
		ncrItem.setCreatedBy(context.getUser().getPk());
		ncrItem.setCreatedDate(new Date());
		ncrItem.setOriginPk(ncrItemBean.getOriginPk());
		ncrItem.setOriginType(ncrItemBean.getOriginType());
		ncrItem.setNcrGroupFk(ncrItemBean.getNcrGroupFk());
		ncrItem.setMrfNo(ncrItemBean.getMrfNo());
		ncrItem.setNcrDesc(ncrItemBean.getNcrDesc());
		if (ncrItemBean.getNcrAreaOfResponsibilityBean() != null)
		{
			ncrItem.setAreaOfResponsibilityFk(ncrItemBean.getNcrAreaOfResponsibilityBean().getPk());
		}
		// if (ncrItemBean.getNcrDispositionBean() != null)
		// {
		// ncrItem.setDispositionFk(ncrItemBean.getNcrDispositionBean().getPk());
		// }
		if (ncrItemBean.getUnitOfMeasures() != null)
		{
			ncrItem.setUnitOfMeasureFk(ncrItemBean.getUnitOfMeasures().getPk());
		}
		if (ncrItemBean.getNcrFailureCodeMasterBean() != null)
		{
			ncrItem.setNcrFailureCodeFk(ncrItemBean.getNcrFailureCodeMasterBean().getPk());
		}

		// ncrItem.setDispositionComment(ncrItemBean.getDispositionComment());
		ncrItem.setSourcePk(ncrItemBean.getSourcePk());
		ncrItem.setSourceType(ncrItemBean.getSourceType());
		ncrItem.setSourceOther(ncrItemBean.getSourceOther());
		ncrItem.setLocationPk(ncrItemBean.getLocationPk());
		ncrItem.setLocationType(ncrItemBean.getLocationType());
		ncrItem.setLocationOther(ncrItemBean.getLocationOther());
		ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.DRAFT.toString());
		ncrItem.setQuantity(ncrItemBean.getQuantity());
		ncrItem.setCustodianPk(ncrItemBean.getCustodianOID().getPk());
		// ncrItem.setForecastStartDate(ncrItemBean.getForecastStartDate());
		// ncrItem.setForecastCompletionDate(ncrItemBean.getForecastCompletionDate());
		ncrItem.setCarType(ncrItemBean.getCarType());
		ncrItem.setHours(ncrItemBean.getHours());
		ncrItem.setSeverity(ncrItemBean.getSeverity());
		ncrItem.setOccurrence(ncrItemBean.getOccurrence());
		ncrItem.setDetection(ncrItemBean.getDetection());
		ncrItem.setEscape(ncrItemBean.getEscape());
		ncrItem.setPpsor8d(ncrItemBean.getPpsor8d());
		ncrItem.setWorkOrderRef(ncrItemBean.getWorkOrderRef());
		ncrItem.setErpReference(ncrItemBean.getErpReference());
		ncrItem.setPriority(ncrItemBean.getPriority());
		// ncrItem.setReworkOrderFk(ncrItemBean.getReworkOrderFk());
		ncrItem.setRootCause(ncrItemBean.getRootCause());
		ncrItem.setWorkInstructionOrComment(ncrItemBean.getWorkInstructionOrComment());
		ncrItemPk = PersistWrapper.createEntity(ncrItem);
		ncrItem.setPk(ncrItemPk);
		ncrItem.setNcrNo("TNCR-" + ncrItemPk);
		PersistWrapper.update(ncrItem);

		if (ncrItemBean.getNcrFunctionBeanList() != null)
		{
			for (NcrFunctionBean functionBean : ncrItemBean.getNcrFunctionBeanList())
			{
				if (functionBean != null)
				{
					NcrFunctionBean ncrFunctionBean = new NcrFunctionBean();
					ncrFunctionBean.setNotificationRequired(functionBean.getNotificationRequired());
					ncrFunctionBean.setApproveRequired(functionBean.getApproveRequired());
					ncrFunctionBean.setFunctionMasterFk(functionBean.getFunctionMasterFk());
					ncrFunctionBean.setObjectFk(functionBean.getObjectFk());
					ncrFunctionBean.setObjectType(functionBean.getObjectType());
					ncrFunctionBean.setApprovalOrder(functionBean.getApprovalOrder());
					ncrFunctionBean.setPk(0);
					ncrFunctionBean.setObjectFk(ncrItemPk);
					ncrFunctionBean.setObjectType(EntityTypeEnum.NCR.toString());
					if (ncrItemBean.getNcrDispositionBean() != null)
					{
						ncrFunctionBean.setDispositionFk(ncrItemBean.getNcrDispositionBean().getPk());
					}
					ncrFunctionBean.setDispositionComment(ncrItemBean.getDispositionComment());
					ncrFunctionBean = saveNcrFunction(context, ncrFunctionBean);
				}
			}
		}

		// if (ncrItemBean.getNcrUnitAssignBeanList() != null)
		// {
		// for (NcrUnitAssignBean ncrUnitAssignBean :
		// ncrItemBean.getNcrUnitAssignBeanList())
		// {
		// if (ncrUnitAssignBean.getQuantity() != null &&
		// ncrUnitAssignBean.getQuantity() != 0)
		// {
		// ncrUnitAssignBean.setPk(0);
		// ncrUnitAssignBean.setNcrUnitAlloc(new ArrayList<NcrUnitAllocBean>());
		// ncrUnitAssignBean.setNcrFk(ncrItemPk);
		// ncrUnitAssignBean.setPartFk(ncrItemBean.getPartOID().getPk());
		// if (ncrItemBean.getPartRevisionOID() != null &&
		// ncrItemBean.getPartRevisionOID().getPk() != 0)
		// {
		// ncrUnitAssignBean.setRevisionFk(ncrItemBean.getPartRevisionOID().getPk());
		// }
		// ncrUnitAssignBean.setProjectFk(ncrItemBean.getProjectOID().getPk());
		// ncrUnitAssignBean = saveNcrUnitAssign(context, ncrUnitAssignBean);
		// }
		// }
		// }

		if (ncrItemBean.getNcrItemActivityRefBeanList() != null
				&& ncrItemBean.getNcrItemActivityRefBeanList().size() > 0)
		{
			for (int i = 0; i < ncrItemBean.getNcrItemActivityRefBeanList().size(); i++)
			{
				NcrItemActivityRefBean ncrItemActivityRefBean = (NcrItemActivityRefBean) ncrItemBean
						.getNcrItemActivityRefBeanList().get(i);
				ncrItemActivityRefBean.setObjectPk(ncrItemPk);
				ncrItemActivityRefBean.setObjectType(EntityTypeEnum.NCR.toString());
				ncrItemActivityRefBean = saveNcrItemActivityRefBean(context, ncrItemActivityRefBean);

			}
		}

		if (ncrItemBean.getResourceRequirementBeanList() != null
				&& ncrItemBean.getResourceRequirementBeanList().size() > 0)
		{
			for (int i = 0; i < ncrItemBean.getResourceRequirementBeanList().size(); i++)
			{
				ResourceRequirementBean resourceRequirementBean = (ResourceRequirementBean) ncrItemBean
						.getResourceRequirementBeanList().get(i);
				resourceRequirementBean.setObjectPk(ncrItemPk);
				resourceRequirementBean.setObjectType(EntityTypeEnum.NCR.toString());
				resourceRequirementBean = saveResourceRequirementBean(context, resourceRequirementBean);
			}
		}

		if (ncrItemBean.getAttachments() != null)
		{
			for (AttachmentIntf attachmentIntf : ncrItemBean.getAttachments())
			{
				attachmentIntf.setPk(0);
			}
			CommonServiceManager.saveAttachments(EtestApplication.getInstance().getUserContext(), ncrItemPk,
					EntityTypeEnum.NCR.getValue(), ncrItemBean.getAttachments(), true);
		}

		if (ncrItemBean.getNcrPartsRequiredBeanList() != null)
		{
			for (int i = 0; i < ncrItemBean.getNcrPartsRequiredBeanList().size(); i++)
			{
				AdditionalPartsBean ncrPartsRequiredBean = (AdditionalPartsBean) ncrItemBean
						.getNcrPartsRequiredBeanList().get(i);
				if (ncrPartsRequiredBean != null)
				{
					if (!ncrPartsRequiredBean.getPartName().equals(""))
					{
						ncrPartsRequiredBean.setPk(0);
						ncrPartsRequiredBean.setObjectPk(ncrItemPk);
						ncrPartsRequiredBean.setObjectType(Integer.toString(EntityTypeEnum.NCR.getValue()));
						ncrPartsRequiredBean = AdditionalPartsManager.save(context, ncrPartsRequiredBean);
					}
				}
			}
		}
		setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
		ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItemPk, ""));
		return ncrItemBean;
	}

	public static void setNcrGroupStatus(NcrGroupOID ncrGroupOID)
	{
		try
		{
			List<NcrItemBean> totalNcrItemBean = getNcrItemBeans(ncrGroupOID);
			int openCount = 0, publishCount = 0, rejectedCount = 0, approvedCount = 0, completeCount = 0,
					closedCount = 0, cancelledCount = 0;
			if (ncrGroupOID != null)
			{
				for (Iterator iterator = totalNcrItemBean.iterator(); iterator.hasNext();)
				{
					NcrItemBean ncrItemBean = (NcrItemBean) iterator.next();
					if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString()))
					{
						openCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.PUBLISHED.toString()))
					{
						publishCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.REJECTED.toString()))
					{
						rejectedCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.APPROVED.toString()))
					{
						approvedCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.COMPLETED.toString()))
					{
						completeCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString()))
					{
						closedCount++;
					} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CANCELLED.toString()))
					{
						cancelledCount++;
					}
				}
				if (openCount > 0)
				{
					PersistWrapper.executeUpdate(
							"update ncr_group set status='" + NcrEnum.NcrGroupStatus.DRAFT.toString() + "' where pk=?",
							ncrGroupOID.getPk());
				} else if (rejectedCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.REJECTED.toString() + "' where pk=?", ncrGroupOID.getPk());
				} else if (publishCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.PUBLISHED.toString() + "' where pk=?", ncrGroupOID.getPk());
				} else if (approvedCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.APPROVED.toString() + "' where pk=?", ncrGroupOID.getPk());
				} else if (completeCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.COMPLETED.toString() + "' where pk=?", ncrGroupOID.getPk());
				} else if (closedCount > 0)
				{
					PersistWrapper.executeUpdate(
							"update ncr_group set status='" + NcrEnum.NcrGroupStatus.CLOSED.toString() + "' where pk=?",
							ncrGroupOID.getPk());
				} else if (cancelledCount > 0)
				{
					PersistWrapper.executeUpdate("update ncr_group set status='"
							+ NcrEnum.NcrGroupStatus.CANCELLED.toString() + "' where pk=?", ncrGroupOID.getPk());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static NcrItemBean publishNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		NcrItem ncrItem = null;
		int ncrItemPk;
		if (ncrItemBean.getPk() != 0)
		{
			ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemBean.getPk());
			if (ncrItem.getNcrStatus() != null && ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.DRAFT.toString()))
			{
				// throw new Exception("The NCR is not in DRAFT stage");
				// }
				// sequence key generatorF
				// NcrGroupBean ncrGroupBean = getPublishedNcrGroup(new
				// NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
				// String seqNo = "";
				// int NcrNumber = 0;
				// if (ncrGroupBean != null)
				// {
				// if (ncrGroupBean.getNcrItemBeanList() != null &&
				// ncrGroupBean.getNcrItemBeanList().size() > 0)
				// {
				// NcrNumber = ncrGroupBean.getNcrItemBeanList().size();
				// }
				// }
				// NcrNumber++;
				Integer seqCount = getPublishedSequenceNumber(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
				int NcrNumber = 0;
				if (seqCount != null && seqCount > 0)
				{
					NcrNumber = seqCount;
				}
				NcrNumber++;
				String seqNo = "";
				seqNo = Integer.toString(NcrNumber);
				// sequence key generator
				ncrItem.setNcrNo(seqNo);
				ncrItem.setPublishedBy(context.getUser().getPk());
				ncrItem.setPublishedDate(new Date());
				ncrItem.setPublishedComment(ncrItemBean.getPublishedComment());
				ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.PUBLISHED.toString());
				PersistWrapper.update(ncrItem);
				ncrItemPk = ncrItem.getPk();
				Comment newComment = ProjectDelegate.addComment(EtestApplication.getInstance().getUserContext(),
						ncrItemBean.getPk(), EntityTypeEnum.NCR, "NCR Published:- " + ncrItemBean.getPublishedComment(),
						Mode.COMMENTCONTEXT_GENERAL);

				ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemBean.getPk());
				NcrEventsListener.notifyNcrItemPublished(context, ncrItem);

				ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItemPk, ""));
				setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
				if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.PUBLISHED.toString()))
				{
					if (ncrItemBean.getOriginType() != null
							&& ncrItemBean.getOriginType().equals(EntityTypeEnum.MRFPartAssign.name()))
					{
						// NCR Created from MRF
						MRFManager.updateAndon(context, new MRFPartAssignOID(ncrItemBean.getOriginPk()),
								ncrItemBean.getPublishedComment());
					}

					NcrEmailSender.notifyNcrItemPublish(context, ncrItemBean);
					EntityMilestoneDelegate.setMilestoneStatus(EtestApplication.getInstance().getUserContext(),
							new NcrItemOID(ncrItemBean.getPk(), null), PUBLISHED_MILESTONE,
							PUBLISHED_MILESTONE_SELECTEDVALUE);
				}
			}

		}
		return ncrItemBean;

	}

	public static NcrItemBean verifyNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		NcrItem ncrItem = null;
		int ncrItemPk;
		if (ncrItemBean.getPk() != 0)
		{
			ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemBean.getPk());
			ncrItem.setVerifiedBy(context.getUser().getPk());
			ncrItem.setVerifiedDate(new Date());
			ncrItem.setVerifiedComment(ncrItemBean.getVerifiedComment());
			ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.COMPLETED.toString());
			PersistWrapper.update(ncrItem);
			ncrItemPk = ncrItem.getPk();
			Comment newComment = ProjectDelegate.addComment(EtestApplication.getInstance().getUserContext(),
					ncrItemBean.getPk(), EntityTypeEnum.NCR, "NCR Verified:- " + ncrItemBean.getVerifiedComment(),
					Mode.COMMENTCONTEXT_GENERAL);

			if (ncrItemBean.getNcrUnitAssignBeanList() != null)
			{
				for (NcrUnitAssignBean ncrUnitAssignBean : ncrItemBean.getNcrUnitAssignBeanList())
				{
					if (ncrUnitAssignBean.getQuantity() == null || ncrUnitAssignBean.getQuantity() == 0)
					{
						ncrUnitAssignBean.setNcrFk(ncrItemPk);
						// ncrUnitAssignBean.setPartFk(ncrItemBean.getPartOID().getPk());
						// if (ncrItemBean.getPartRevisionOID() != null &&
						// ncrItemBean.getPartRevisionOID().getPk() != 0)
						// {
						// ncrUnitAssignBean.setRevisionFk(ncrItemBean.getPartRevisionOID().getPk());
						// }
						// ncrUnitAssignBean.setProjectFk(ncrItemBean.getProjectOID().getPk());
						ncrUnitAssignBean = completeNcrUnitAssign(context, ncrUnitAssignBean);
					}
				}
			}

			ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItemPk, ""));
			setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
			if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.COMPLETED.toString()))
			{
				NcrEmailSender.notifyNcrItemComplete(context, ncrItemBean);
			}
			NcrEventsListener.notifyNcrItemCompleted(context, ncrItem);
		}
		return ncrItemBean;
	}

	public static NcrItemBean cancelNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		NcrItem ncrItem = null;
		int ncrItemPk;
		if (ncrItemBean.getPk() != 0)
		{
			ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemBean.getPk());
			ncrItem.setCancelledBy(context.getUser().getPk());
			ncrItem.setCancelledDate(new Date());
			ncrItem.setCancelledComment(ncrItemBean.getCancelledComment());
			ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.CANCELLED.toString());
			PersistWrapper.update(ncrItem);
			ncrItemPk = ncrItem.getPk();
			Comment newComment = ProjectDelegate.addComment(EtestApplication.getInstance().getUserContext(),
					ncrItemBean.getPk(), EntityTypeEnum.NCR, "NCR Cancelled:- " + ncrItemBean.getClosedComment(),
					Mode.COMMENTCONTEXT_GENERAL);

			ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItemPk, ""));

			setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
			if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CANCELLED.toString()))
			{
				NcrEmailSender.notifyNcrItemCancelled(context, ncrItemBean);
			}
			NcrGroupBean ncrGroupBean = NcrManager.getNcrGroupBean(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
			if (ncrGroupBean.getStatus() != null
					&& ncrGroupBean.getStatus().equals(NcrEnum.NcrGroupStatus.CANCELLED.toString()))
			{
				NcrEmailSender.notifyNcrGroupCancelled(context, ncrGroupBean);
			}

		}
		return ncrItemBean;

	}

	public static NcrItemBean reopenNcrItem(UserContext context, NcrItemOID ncrItemOID, String comment) throws Exception
	{
		NcrItem ncrItem = null;
		int ncrItemPk;
		NcrItemBean ncrItemBean = null;
		if (ncrItemOID.getPk() != 0)
		{
			ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemOID.getPk());
			if (ncrItem != null && (ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString())
					|| ncrItem.getNcrStatus().equals(NcrEnum.NcrItemStatus.COMPLETED.toString())))
			{
				ncrItem.setVerifiedBy(null);
				ncrItem.setVerifiedDate(null);
				ncrItem.setVerifiedComment(null);

				ncrItem.setClosedBy(null);
				ncrItem.setClosedDate(null);
				ncrItem.setClosedComment(null);
				Comment newComment = ProjectDelegate.addComment(EtestApplication.getInstance().getUserContext(),
						ncrItemOID.getPk(), EntityTypeEnum.NCR, "NCR Reopened:- " + comment,
						Mode.COMMENTCONTEXT_GENERAL);

				ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.APPROVED.toString());
				PersistWrapper.update(ncrItem);
				ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItem.getPk(), ""));
				setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
			}
		}
		return ncrItemBean;

	}

	public static NcrItemBean closeNcrItem(UserContext context, NcrItemBean ncrItemBean) throws Exception
	{
		NcrItem ncrItem = null;
		String previousStatus = null;
		int ncrItemPk;
		if (ncrItemBean.getPk() != 0)
		{
			ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemBean.getPk());
			if (ncrItem.getNcrStatus() != null)
			{
				previousStatus = ncrItem.getNcrStatus();
			}
			if (ncrItem.getVerifiedBy() == null)
			{
				ncrItem.setVerifiedBy(context.getUser().getPk());
				ncrItem.setVerifiedDate(new Date());
				ncrItem.setVerifiedComment(ncrItemBean.getClosedComment());
			}
			ncrItem.setClosedBy(context.getUser().getPk());
			ncrItem.setClosedDate(new Date());
			ncrItem.setClosedComment(ncrItemBean.getClosedComment());
			ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.CLOSED.toString());
			PersistWrapper.update(ncrItem);
			ncrItemPk = ncrItem.getPk();
			Comment newComment = ProjectDelegate.addComment(EtestApplication.getInstance().getUserContext(),
					ncrItemBean.getPk(), EntityTypeEnum.NCR, "NCR Closed:- " + ncrItemBean.getClosedComment(),
					Mode.COMMENTCONTEXT_GENERAL);

			if (ncrItemBean.getNcrUnitAssignBeanList() != null)
			{
				for (NcrUnitAssignBean ncrUnitAssignBean : ncrItemBean.getNcrUnitAssignBeanList())
				{
					if (ncrUnitAssignBean.getQuantity() == null || ncrUnitAssignBean.getQuantity() == 0)
					{
						ncrUnitAssignBean.setNcrFk(ncrItemPk);
						// ncrUnitAssignBean.setPartFk(ncrItemBean.getPartOID().getPk());
						// if (ncrItemBean.getPartRevisionOID() != null &&
						// ncrItemBean.getPartRevisionOID().getPk() != 0)
						// {
						// ncrUnitAssignBean.setRevisionFk(ncrItemBean.getPartRevisionOID().getPk());
						// }
						// ncrUnitAssignBean.setProjectFk(ncrItemBean.getProjectOID().getPk());
						ncrUnitAssignBean = closeNcrUnitAssign(context, ncrUnitAssignBean);
					}
				}
			}

			ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItemPk, ""));
			setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
			if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString())
					&& (previousStatus.equals(NcrEnum.NcrItemStatus.COMPLETED.toString())))
			{
				NcrEmailSender.notifyNcrItemClosedAfterComplete(context, ncrItemBean);
				EntityMilestoneDelegate.setMilestoneStatus(EtestApplication.getInstance().getUserContext(),
						new NcrItemOID(ncrItemBean.getPk(), null), CLOSED_MILESTONE, CLOSED_MILESTONE_SELECTEDVALUE);
			} else if (ncrItemBean.getNcrStatus().equals(NcrEnum.NcrItemStatus.CLOSED.toString())
					&& !(previousStatus.equals(NcrEnum.NcrItemStatus.COMPLETED.toString())))
			{
				NcrEmailSender.notifyNcrItemClosed(context, ncrItemBean);
				EntityMilestoneDelegate.setMilestoneStatus(EtestApplication.getInstance().getUserContext(),
						new NcrItemOID(ncrItemBean.getPk(), null), CLOSED_MILESTONE, CLOSED_MILESTONE_SELECTEDVALUE);
			}
			NcrGroupBean ncrGroupBean = NcrManager.getNcrGroupBean(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
			if (ncrGroupBean.getStatus() != null
					&& ncrGroupBean.getStatus().equals(NcrEnum.NcrGroupStatus.CLOSED.toString()))
			{
				NcrEmailSender.notifyNcrGroupClosed(context, ncrGroupBean);
			}
			NcrEventsListener.notifyNcrItemClosed(context, ncrItem);
		}
		return ncrItemBean;
		// NcrItem ncrItem = null;
		// int ncrItemPk;
		// if (ncrItemBean.getPk() != 0)
		// {
		// ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class,
		// ncrItemBean.getPk());
		// if (ncrItem.getNcrStatus() != null)
		// {
		// previousStatus = ncrItem.getNcrStatus();
		// }
		// if (ncrItem.getVerifiedBy() == null && ncrItem.getVerifiedDate() ==
		// null)
		// {
		// ncrItem.setVerifiedBy(context.getUser().getPk());
		// ncrItem.setVerifiedDate(new Date());
		// }
		//
		// ncrItem.setClosedBy(context.getUser().getPk());
		// ncrItem.setClosedDate(new Date());
		// ncrItem.setOriginPk(ncrItemBean.getOriginPk());
		// ncrItem.setOriginType(ncrItemBean.getOriginType());
		// ncrItem.setClosedComment(ncrItemBean.getClosedComment());
		// ncrItem.setNcrGroupFk(ncrItemBean.getNcrGroupFk());
		// ncrItem.setCustodianPk(ncrItemBean.getCustodianPk());
		// ncrItem.setMrfNo(ncrItemBean.getMrfNo());
		// ncrItem.setNcrDesc(ncrItemBean.getNcrDesc());
		// if (ncrItemBean.getNcrAreaOfResponsibilityBean() != null)
		// {
		// ncrItem.setAreaOfResponsibilityFk(ncrItemBean.getNcrAreaOfResponsibilityBean().getPk());
		// }
		// if (ncrItemBean.getNcrDispositionBean() != null)
		// {
		// ncrItem.setDispositionFk(ncrItemBean.getNcrDispositionBean().getPk());
		// }
		// if (ncrItemBean.getUnitOfMeasuresBean() != null)
		// {
		// ncrItem.setUnitOfMeasureFk(ncrItemBean.getUnitOfMeasuresBean().getPk());
		// }
		// if (ncrItemBean.getNcrFailureCodeMasterBean() != null)
		// {
		// ncrItem.setNcrFailureCodeFk(ncrItemBean.getNcrFailureCodeMasterBean().getPk());
		// }
		//
		// ncrItem.setDispositionComment(ncrItemBean.getDispositionComment());
		// ncrItem.setWorkInstructionOrComment(ncrItemBean.getWorkInstructionOrComment());
		// ncrItem.setSourcePk(ncrItemBean.getSourcePk());
		// ncrItem.setSourceType(ncrItemBean.getSourceType());
		// ncrItem.setSourceOther(ncrItemBean.getSourceOther());
		//
		// ncrItem.setLocationPk(ncrItemBean.getLocationPk());
		// ncrItem.setLocationType(ncrItemBean.getLocationType());
		// ncrItem.setLocationOther(ncrItemBean.getLocationOther());
		// ncrItem.setNcrStatus(NcrEnum.NcrItemStatus.CLOSED.toString());
		// ncrItem.setQuantity(ncrItemBean.getQuantity());
		//
		// ncrItem.setForecastDate(ncrItemBean.getForecastDate());
		// ncrItem.setCarType(ncrItemBean.getCarType());
		// ncrItem.setHours(ncrItemBean.getHours());
		// ncrItem.setSeverity(ncrItemBean.getSeverity());
		// ncrItem.setOccurrence(ncrItemBean.getOccurrence());
		// ncrItem.setDetection(ncrItemBean.getDetection());
		// ncrItem.setEscape(ncrItemBean.getEscape());
		// ncrItem.setPpsor8d(ncrItemBean.getPpsor8d());
		//
		// ncrItem.setPriority(ncrItemBean.getPriority());
		// ncrItem.setReworkOrderFk(ncrItemBean.getReworkOrderFk());
		// ncrItem.setRootCause(ncrItemBean.getRootCause());
		// if (ncrItemBean.getPk() != 0)
		// {
		// PersistWrapper.update(ncrItem);
		// ncrItemPk = ncrItem.getPk();
		// } else
		// {
		// ncrItemPk = PersistWrapper.createEntity(ncrItem);
		// }
		// if (ncrItemBean.getNcrFunctionBeanList() != null)
		// {
		// for (int i = 0; i < ncrItemBean.getNcrFunctionBeanList().size(); i++)
		// {
		// NcrFunctionBean ncrFunctionBean = (NcrFunctionBean)
		// ncrItemBean.getNcrFunctionBeanList().get(i);
		// if (ncrFunctionBean != null)
		// {
		// if (ncrFunctionBean.getPk() == 0)
		// {
		// ncrFunctionBean.setObjectFk(ncrItemPk);
		// ncrFunctionBean.setObjectType(EntityTypeEnum.NCR.toString());
		// }
		// ncrFunctionBean.setDispositionComment(ncrItemBean.getDispositionComment());
		// if (ncrItemBean.getNcrDispositionBean() != null)
		// {
		// ncrFunctionBean.setDispositionFk(ncrItemBean.getNcrDispositionBean().getPk());
		// }
		// ncrFunctionBean = saveNcrFunction(context, ncrFunctionBean);
		// }
		// }
		// }
		// if (ncrItemBean.getNcrUnitAssignBeanList() != null)
		// {
		// for (int i = 0; i < ncrItemBean.getNcrUnitAssignBeanList().size();
		// i++)
		// {
		// NcrUnitAssignBean ncrUnitAssignBean = (NcrUnitAssignBean)
		// ncrItemBean.getNcrUnitAssignBeanList()
		// .get(i);
		// if (ncrUnitAssignBean.getQuantity() != null &&
		// ncrUnitAssignBean.getQuantity() != 0)
		// {
		// ncrUnitAssignBean.setNcrFk(ncrItemPk);
		// ncrUnitAssignBean.setPartFk(ncrItemBean.getPartOID().getPk());
		// if (ncrItemBean.getPartRevisionOID() != null &&
		// ncrItemBean.getPartRevisionOID().getPk() != 0)
		// {
		// ncrUnitAssignBean.setRevisionFk(ncrItemBean.getPartRevisionOID().getPk());
		// }
		// ncrUnitAssignBean = saveNcrUnitAssign(context, ncrUnitAssignBean);
		// }
		// }
		// }
		// if (ncrItemBean.getNcrPartsRequiredBeanList() != null)
		// {
		// for (int i = 0; i < ncrItemBean.getNcrPartsRequiredBeanList().size();
		// i++)
		// {
		// NcrPartsRequiredBean ncrPartsRequiredBean = (NcrPartsRequiredBean)
		// ncrItemBean
		// .getNcrPartsRequiredBeanList().get(i);
		// if (ncrPartsRequiredBean != null)
		// {
		// ncrPartsRequiredBean.setNcrFk(ncrItemPk);
		// ncrPartsRequiredBean = saveNcrPartsRequired(context,
		// ncrPartsRequiredBean);
		// }
		// }
		// }
		// // Ncr Item Activity Ref
		// List<NcrItemActivityRefBean> currentNcrItemActivityRefBean =
		// getNcrItemActivityRefBeanListByObjectPkAndObjectType(
		// ncrItemPk, EntityTypeEnum.NCR.toString());
		//
		// if (currentNcrItemActivityRefBean == null)
		// {
		// currentNcrItemActivityRefBean = new
		// ArrayList<NcrItemActivityRefBean>();
		// }
		// if (ncrItemBean.getNcrItemActivityRefBeanList() != null
		// && ncrItemBean.getNcrItemActivityRefBeanList().size() > 0)
		// {
		// for (int i = 0; i <
		// ncrItemBean.getNcrItemActivityRefBeanList().size(); i++)
		// {
		// NcrItemActivityRefBean ncrItemActivityRefBean =
		// (NcrItemActivityRefBean) ncrItemBean
		// .getNcrItemActivityRefBeanList().get(i);
		// int pos =
		// currentNcrItemActivityRefBean.indexOf(ncrItemActivityRefBean);
		// if (currentNcrItemActivityRefBean.contains(ncrItemActivityRefBean))
		// {
		// currentNcrItemActivityRefBean.remove(ncrItemActivityRefBean);
		// }
		// ncrItemActivityRefBean.setObjectPk(ncrItemPk);
		// ncrItemActivityRefBean.setObjectType(EntityTypeEnum.NCR.toString());
		// ncrItemActivityRefBean = saveNcrItemActivityRefBean(context,
		// ncrItemActivityRefBean);
		//
		// }
		// }
		// if (currentNcrItemActivityRefBean.size() > 0)
		// {
		// for (Iterator<NcrItemActivityRefBean> iterator =
		// currentNcrItemActivityRefBean.iterator(); iterator
		// .hasNext();)
		// {
		// deleteNcrItemActivityRefByPk(iterator.next().getPk());
		// }
		//
		// }
		// // resource requirement
		//
		// List<ResourceRequirementBean> currentResourceRequirementBean =
		// getResourceRequirementBeanListByObjectPkAndObjectType(
		// ncrItemPk, EntityTypeEnum.NCR.toString());
		//
		// if (currentResourceRequirementBean == null)
		// {
		// currentResourceRequirementBean = new
		// ArrayList<ResourceRequirementBean>();
		// }
		// if (ncrItemBean.getResourceRequirementBeanList() != null
		// && ncrItemBean.getResourceRequirementBeanList().size() > 0)
		// {
		// for (int i = 0; i <
		// ncrItemBean.getResourceRequirementBeanList().size(); i++)
		// {
		// ResourceRequirementBean resourceRequirementBean =
		// (ResourceRequirementBean) ncrItemBean
		// .getResourceRequirementBeanList().get(i);
		// resourceRequirementBean.setObjectPk(ncrItemPk);
		// resourceRequirementBean.setObjectType(EntityTypeEnum.NCR.toString());
		// int pos =
		// currentResourceRequirementBean.indexOf(resourceRequirementBean);
		// if (currentResourceRequirementBean.contains(resourceRequirementBean))
		// {
		// currentResourceRequirementBean.remove(resourceRequirementBean);
		// }
		//
		// resourceRequirementBean = saveResourceRequirementBean(context,
		// resourceRequirementBean);
		// }
		// }
		//
		// if (currentResourceRequirementBean.size() > 0)
		// {
		// for (Iterator<ResourceRequirementBean> iterator =
		// currentResourceRequirementBean.iterator(); iterator
		// .hasNext();)
		// {
		// deleteResourceRequirementByPk(iterator.next().getPk());
		// }
		//
		// }
		// if (ncrItemBean.getAttachments() != null)
		// {
		// SurveyMaster.saveAttachments(EtestApplication.getInstance().getUserContext(),
		// ncrItemPk,
		// EntityTypeEnum.NCR.getValue(), ncrItemBean.getAttachments(), true);
		// }
		// if (ncrItemBean.getPpsOr8DAttachment() != null)
		// {
		// for (Iterator iterator =
		// ncrItemBean.getPpsOr8DAttachment().iterator(); iterator.hasNext();)
		// {
		// Attachment attachment = (Attachment) iterator.next();
		// attachment.setAttachContext(NcrItem.NCR_PPSOR8D);
		// }
		// SurveyMaster.saveAttachments(EtestApplication.getInstance().getUserContext(),
		// ncrItemPk,
		// EntityTypeEnum.NCR.getValue(), NcrItem.NCR_PPSOR8D,
		// ncrItemBean.getPpsOr8DAttachment());
		// }
		// setNcrGroupStatus(ncrItemBean.getNcrGroupFk());
		// ncrItemBean = getNcrItemBeanByPk(ncrItemPk);
		// ncrItemBean.setNcrFunctionBeanList(
		// getNcrCurrentFunctionByObjectFkAndObjectType(ncrItemPk,
		// EntityTypeEnum.NCR.toString()));
		// ncrItemBean.setNcrUnitAssignBeanList(getNcrUnitAssignByNcrFk(ncrItemPk));
		// }

	}

	public static NcrItemBean getNcrItemBean(NcrItemOID ncrItemOID)
	{
		try
		{
			NcrItem ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemOID.getPk());
			return NcrBeanHelper.getNcrItemBean(ncrItem);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	public static Integer getPublishedSequenceNumber(NcrGroupOID ncrGroupOID)
	{
		Integer sequenceNum = null;
		try
		{
			sequenceNum = PersistWrapper.read(Integer.class,
					" select count(pk) as pk from ncr where ncrGroupFk=? and ncrStatus <>? and eStatus=?",
					ncrGroupOID.getPk(), NcrEnum.NcrItemStatus.DRAFT.toString(), EStatusEnum.Active.getValue());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sequenceNum;
	}

	public static List<NcrItemBean> getPublishedNcrItem(NcrGroupOID ncrGroupOID)
	{
		List<NcrItem> ncrItems = null;
		List<NcrItemBean> ncrItemBeanList = new ArrayList<NcrItemBean>();
		try
		{
			ncrItems = PersistWrapper.readList(NcrItem.class,
					"select * from ncr where ncrGroupFk=? and ncrStatus=? and eStatus=?", ncrGroupOID.getPk(),
					NcrEnum.NcrItemStatus.PUBLISHED.toString(), EStatusEnum.Active.getValue());
			if (ncrItems != null)
			{
				for (NcrItem ncrItem : ncrItems)
				{
					ncrItemBeanList.add(NcrBeanHelper.getNcrItemBean(ncrItem));
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemBeanList;
	}

	public static List<NcrItemBean> getNcrItemBeans(NcrGroupOID ncrGroupOID)
	{
		List<NcrItem> ncrItems = null;
		List<NcrItemBean> ncrItemBeanList = new ArrayList<NcrItemBean>();
		try
		{
			ncrItems = PersistWrapper.readList(NcrItem.class, "select * from ncr where ncrGroupFk=? and eStatus=?",
					ncrGroupOID.getPk(), EStatusEnum.Active.getValue());
			if (ncrItems != null)
			{
				for (NcrItem ncrItem : ncrItems)
				{
					ncrItemBeanList.add(NcrBeanHelper.getNcrItemBean(ncrItem));
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemBeanList;
	}

	public static NcrItemBean setOriginTypeAndOriginPkNcrItem(Integer orginPk, String originType, NcrItemOID ncrItemOID)
			throws Exception
	{
		NcrItemBean ncrItemBean = new NcrItemBean();
		try
		{
			NcrItem ncrItem = null;
			int ncrItemPk = 0;
			if (ncrItemOID != null && ncrItemOID.getPk() != 0)
			{
				ncrItem = PersistWrapper.readByPrimaryKey(NcrItem.class, ncrItemOID.getPk());
				ncrItem.setOriginPk(orginPk);
				ncrItem.setOriginType(originType);
				PersistWrapper.update(ncrItem);
				ncrItemPk = ncrItem.getPk();
			}
			ncrItemBean = getNcrItemBean(new NcrItemOID(ncrItemPk, ""));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrItemBean;
	}

	public static List<NcrItemBean> getNcrItemBean(Integer orginPk, String originType) throws Exception
	{
		List<NcrItemBean> result = null;
		try
		{
			List<NcrItem> listNcrItems = PersistWrapper.readList(NcrItem.class,
					"SELECT * FROM ncr where originPk=? and originType=?", orginPk, originType);
			if (listNcrItems != null)
			{
				result = new ArrayList<NcrItemBean>();
				for (NcrItem ncrItem : listNcrItems)
				{
					result.add(getNcrItemBean(new NcrItemOID(ncrItem.getPk(), ncrItem.getNcrNo())));
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static NcrUnitAssignBean getNcrUnitAssignBean(Integer orginPk, String originType, int unitFk)
			throws Exception
	{
		try
		{
			NcrUnitAssign ncrUnitAssign = PersistWrapper.read(NcrUnitAssign.class,
					"SELECT nua.* FROM ncr inner join ncr_unit_assign nua on ncr.pk=nua.ncrFk where nua.unitFk=? and ncr.originType=? and ncr.originPk=?;",
					unitFk, originType, orginPk);
			if (ncrUnitAssign != null)
			{
				return NcrBeanHelper.getNcrUnitAssignBean(ncrUnitAssign);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static List<NcrUnitAllocBean> getNcrUnitAloc(Integer orginPk, String originType, int unitFk) throws Exception
	{
		List<NcrUnitAllocBean> result = null;
		try
		{
			List<NcrUnitAlloc> listUnitAlloc = PersistWrapper.readList(NcrUnitAlloc.class,
					"SELECT nualloc.* FROM ncr inner join ncr_unit_assign nua on ncr.pk=nua.ncrFk inner join ncr_unit_alloc nualloc on nua.pk=nualloc.ncrUnitAssignFk where nua.unitFk=? and ncr.originType=? and ncr.originPk=?;",
					unitFk, originType, orginPk);
			if (listUnitAlloc != null)
			{
				result = new ArrayList<NcrUnitAllocBean>();
				for (NcrUnitAlloc ncrUnitAlloc : listUnitAlloc)
				{
					result.add(NcrBeanHelper.getNcrUnitAllocBean(ncrUnitAlloc));
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static List<NcrItemOID> getNcrItemOID(Integer orginPk, String originType) throws Exception
	{
		List<NcrItemOID> result = null;
		try
		{
			List<NcrItem> listNcrItems = PersistWrapper.readList(NcrItem.class,
					"SELECT * FROM ncr where originPk=? and originType=?", orginPk, originType);
			if (listNcrItems != null)
			{
				result = new ArrayList<NcrItemOID>();
				for (NcrItem ncrItem : listNcrItems)
				{
					result.add(new NcrItemOID(ncrItem.getPk(), ncrItem.getNcrNo()));
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static List<NcrItemBean> getNcrItemBeans(Integer orginPk, String originType) throws Exception
	{
		List<NcrItemBean> result = null;
		try
		{
			List<NcrItem> listNcrItems = PersistWrapper.readList(NcrItem.class,
					"SELECT * FROM ncr where originPk=? and originType=?", orginPk, originType);
			if (listNcrItems != null)
			{
				result = new ArrayList<NcrItemBean>();
				for (NcrItem ncrItem : listNcrItems)
				{
					// NcrGroup ncrGroup =
					// PersistWrapper.readByPrimaryKey(NcrGroup.class,
					// ncrItem.getNcrGroupFk());
					result.add(NcrBeanHelper.getNcrItemBean(ncrItem));
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static List<NcrItemQuery> getNcrItemQueryList(UserContext context, NcrGroupOID ncrGroupOID)
	{

		List<NcrItemQuery> ncrItemQuery = null;
		try
		{
			NcrItemQueryFilter ncrItemQueryFilter = new NcrItemQueryFilter();
			ncrItemQueryFilter.setNcrGroupFk(ncrGroupOID.getPk());
			NcrItemQueryBuilder ncrItemQueryBuilder = new NcrItemQueryBuilder(context, ncrItemQueryFilter);
			QueryObject queryObject = ncrItemQueryBuilder.getQuery();
			if (queryObject != null)
				ncrItemQuery = PersistWrapper.readList(NcrItemQuery.class, queryObject.getQuery(),
						(queryObject.getParams().size() > 0)
								? queryObject.getParams().toArray(new Object[queryObject.getParams().size()])
								: null);

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemQuery;
	}

	public static List<NcrItemQuery> getNcrItemQueryList(UserContext context, NcrGroupOID ncrGroupOID,
			List<NcrEnum.NcrItemStatus> status)
	{

		List<NcrItemQuery> ncrItemQuery = null;
		try
		{
			NcrItemQueryFilter ncrItemQueryFilter = new NcrItemQueryFilter();
			ncrItemQueryFilter.setNcrGroupFk(ncrGroupOID.getPk());
			ncrItemQueryFilter.setNcrItemStatusList(status);
			NcrItemQueryBuilder ncrItemQueryBuilder = new NcrItemQueryBuilder(context, ncrItemQueryFilter);
			QueryObject queryObject = ncrItemQueryBuilder.getQuery();
			if (queryObject != null)
				ncrItemQuery = PersistWrapper.readList(NcrItemQuery.class, queryObject.getQuery(),
						(queryObject.getParams().size() > 0)
								? queryObject.getParams().toArray(new Object[queryObject.getParams().size()])
								: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemQuery;
	}

	public static List<NcrItemNcgExportQuery> getNcrItemNcgExportQueryList(NcrItemQueryFilter ncrItemQueryFilter)
	{

		List<NcrItemNcgExportQuery> ncrItemQuery = null;
		try
		{

			NcrItemNcgExportQueryBuilder ncrItemQueryBuilder = new NcrItemNcgExportQueryBuilder(ncrItemQueryFilter);
			QueryObject queryObject = ncrItemQueryBuilder.getQuery();
			if (queryObject != null)
				ncrItemQuery = PersistWrapper.readList(NcrItemNcgExportQuery.class, queryObject.getQuery(),
						(queryObject.getParams().size() > 0)
								? queryObject.getParams().toArray(new Object[queryObject.getParams().size()])
								: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemQuery;
	}

	public static List<NcrItemQuery> getNcrItemQueryListByncrGroupFk(UserContext context,
			NcrItemQueryFilter ncrItemQueryFilter)
	{

		List<NcrItemQuery> ncrItemQuery = null;
		try
		{

			NcrItemQueryBuilder ncrItemQueryBuilder = new NcrItemQueryBuilder(context, ncrItemQueryFilter);
			QueryObject queryObject = ncrItemQueryBuilder.getQuery();
			if (queryObject != null)
				ncrItemQuery = PersistWrapper.readList(NcrItemQuery.class, queryObject.getQuery(),
						(queryObject.getParams().size() > 0)
								? queryObject.getParams().toArray(new Object[queryObject.getParams().size()])
								: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemQuery;
	}

	public static List<NcrItemQuery> getNcrItemDetails(UserContext context, PartOID partOID)
	{
		List<NcrItemQuery> ncrItemQuery = null;
		try
		{
			List<NcrEnum.NcrGroupStatus> ncrGroupStatusList = new ArrayList<NcrEnum.NcrGroupStatus>();
			NcrItemQueryFilter ncrItemQueryFilter = new NcrItemQueryFilter();
			ncrItemQueryFilter.setPartFk(partOID.getPk());
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.DRAFT);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.PUBLISHED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.APPROVED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.REJECTED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.COMPLETED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.CLOSED);
			ncrGroupStatusList.add(NcrEnum.NcrGroupStatus.CANCELLED);
			ncrItemQueryFilter.setNcrGroupStatusList(ncrGroupStatusList);
			NcrItemQueryBuilder ncrItemQueryBuilder = new NcrItemQueryBuilder(context, ncrItemQueryFilter);
			QueryObject queryObject = ncrItemQueryBuilder.getQuery();
			if (queryObject != null)
				ncrItemQuery = PersistWrapper.readList(NcrItemQuery.class, queryObject.getQuery(),
						(queryObject.getParams().size() > 0)
								? queryObject.getParams().toArray(new Object[queryObject.getParams().size()])
								: null);
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemQuery;
	}

	public static List<NcrItemQuery> getNcrItemList(UserContext context, NcrItemQueryFilter ncrItemQueryFilter)
	{
		List<NcrItemQuery> ncrItemQuery = null;
		try
		{
			NcrItemQueryBuilder ncrItemQueryBuilder = new NcrItemQueryBuilder(context, ncrItemQueryFilter);
			QueryObject queryObject = ncrItemQueryBuilder.getQuery();
			if (queryObject != null)
				ncrItemQuery = PersistWrapper.readList(NcrItemQuery.class, queryObject.getQuery(),
						(queryObject.getParams().size() > 0)
								? queryObject.getParams().toArray(new Object[queryObject.getParams().size()])
								: null);

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrItemQuery;
	}

	public static void deleteNcrItem(NcrItemOID ncrItemOID) throws Exception
	{
		NcrItemBean ncrItemBean = getNcrItemBean(ncrItemOID);
		deleteNcrFunction(ncrItemOID.getPk(), EntityTypeEnum.NCR.toString());
		deleteNcrUnitAssign(ncrItemOID);
		PersistWrapper.executeUpdate("update ncr set eStatus=? where pk=?", EStatusEnum.Deleted.getValue(),
				ncrItemOID.getPk());
		setNcrGroupStatus(new NcrGroupOID(ncrItemBean.getNcrGroupFk(), ""));
	}

	public static void deleteNcrItem(NcrGroupOID ncrGroupOID) throws Exception
	{
		deleteNcrFunction(ncrGroupOID);
		deleteNcrUnitAssign(ncrGroupOID);
		AdditionalPartsManager.deleteNcrPartsRequired(ncrGroupOID);
		PersistWrapper.executeUpdate("update ncr set eStatus=? where ncrGroupFk=?", EStatusEnum.Deleted.getValue(),
				ncrGroupOID.getPk());
	}

	public static void deleteNcrItem(List<NcrItemBean> ncrItemBeans) throws Exception
	{
		if (ncrItemBeans != null)
		{
			for (NcrItemBean ncrItemBean : ncrItemBeans)
			{
				deleteNcrItem(new NcrItemOID(ncrItemBean.getPk(), ""));
			}
		}

	}

	// Ncr Function
	public static NcrFunctionBean saveNcrFunction(UserContext context, NcrFunctionBean nesnItemBean) throws Exception
	{
		NcrFunction nesnItem = null;
		if (nesnItemBean.getPk() != 0)
		{
			nesnItem = PersistWrapper.readByPrimaryKey(NcrFunction.class, nesnItemBean.getPk());
		} else
		{
			nesnItem = new NcrFunction();
			nesnItem.setCreatedBy(context.getUser().getPk());
			nesnItem.setCreatedDate(new Date());
		}
		nesnItem.setApproveRequired(nesnItemBean.getApproveRequired());
		nesnItem.setNotificationRequired(nesnItemBean.getNotificationRequired());
		nesnItem.setDispositionFk(nesnItemBean.getDispositionFk());
		nesnItem.setObjectFk(nesnItemBean.getObjectFk());
		nesnItem.setObjectType(nesnItemBean.getObjectType());
		nesnItem.setFunctionMasterFk(nesnItemBean.getFunctionMasterFk());
		nesnItem.setDispositionComment(nesnItemBean.getDispositionComment());
		nesnItem.setLastUpdated(new Date());
		nesnItem.setApprovalOrder(nesnItemBean.getApprovalOrder());
		Integer iterator = getMaxNcrFunctionIteration(nesnItemBean.getObjectFk(), nesnItemBean.getObjectType());
		if (iterator != null)
		{
			iterator++;
			nesnItem.setIteration(iterator);
		} else
		{
			nesnItem.setIteration(1);
		}
		nesnItem.setIsCurrent(true);
		int itemPk;
		if (nesnItemBean.getPk() != 0)
		{
			PersistWrapper.update(nesnItem);
			itemPk = nesnItem.getPk();
		} else
		{
			itemPk = PersistWrapper.createEntity(nesnItem);
		}
		// saving to Ncr function Notifiers

		List<UserOID> previousNcrFunctionNotifiersList = getNcrFunctionNotifiersList(itemPk);

		if (previousNcrFunctionNotifiersList == null)
		{
			previousNcrFunctionNotifiersList = new ArrayList<UserOID>();
		}
		if (nesnItemBean.getNotifiersList() != null && nesnItemBean.getNotifiersList().size() > 0)
		{
			for (UserOID ncrFunctionNotifiers : nesnItemBean.getNotifiersList())
			{
				if (previousNcrFunctionNotifiersList.contains(ncrFunctionNotifiers))
				{
					previousNcrFunctionNotifiersList.remove(ncrFunctionNotifiers);
				}
				if (ncrFunctionNotifiers.getPk() != 0)
				{

					NcrFunctionNotifiers notifiers = getNcrFunctionNotifiersItem(itemPk, ncrFunctionNotifiers.getPk());
					if (notifiers == null)
					{
						notifiers = new NcrFunctionNotifiers();
						notifiers.setNcrFunctionFk(itemPk);
						notifiers.setUserFk(ncrFunctionNotifiers.getPk());
						saveNcrFunctionNotifiers(context, notifiers);
					}
				}
			}
		}

		for (UserOID userOID : previousNcrFunctionNotifiersList)
		{
			deleteNcrFunctionNotifiers(userOID, itemPk);
		}

		if (nesnItemBean.getPk() == 0 && nesnItemBean.getObjectType().equals(EntityTypeEnum.NCR.toString()))
		{
			approveNcrGroupStatus(new NcrGroupOID(itemPk, ""));
		}
		return getNcrFunctionBeanByPk(itemPk);

	}

	public static void updatePrevNcrFunction(int objectFk, String objectType) throws Exception
	{
		PersistWrapper.executeUpdate("update ncr_function set isCurrent=false where objectFk=? and objectType=?",
				objectFk, objectType);
	}

	public static NcrFunctionBean approveNcrFunction(UserContext context, NcrFunctionBean nesnItemBean) throws Exception
	{
		int itemPk = 0;
		if (nesnItemBean.getPk() != 0)
		{
			NcrFunction nesnItem = null;
			nesnItem = PersistWrapper.readByPrimaryKey(NcrFunction.class, nesnItemBean.getPk());
			nesnItem.setApprovedStatus(NcrEnum.NcrFunctionStatus.APPROVED.toString());
			nesnItem.setApprovedBy(context.getUser().getPk());
			nesnItem.setApprovedDate(new Date());
			nesnItem.setApprovedComment(nesnItemBean.getApprovedComment());
			PersistWrapper.update(nesnItem);
			itemPk = nesnItem.getPk();
		}
		return getNcrFunctionBeanByPk(itemPk);
	}

	public static NcrFunctionBean rejectNcrFunction(UserContext context, NcrFunctionBean nesnItemBean) throws Exception
	{
		int itemPk = 0;
		if (nesnItemBean.getPk() != 0)
		{
			NcrFunction nesnItem = null;
			nesnItem = PersistWrapper.readByPrimaryKey(NcrFunction.class, nesnItemBean.getPk());
			nesnItem.setApprovedStatus(NcrEnum.NcrFunctionStatus.REJECTED.toString());
			nesnItem.setApprovedBy(context.getUser().getPk());
			nesnItem.setApprovedDate(new Date());
			nesnItem.setApprovedComment(nesnItemBean.getApprovedComment());
			PersistWrapper.update(nesnItem);
			itemPk = nesnItem.getPk();
		}
		return getNcrFunctionBeanByPk(itemPk);
	}

	public static NcrFunctionBean getNcrFunctionBeanByPk(int pk) throws Exception
	{
		NcrFunction ncrFunction = PersistWrapper.readByPrimaryKey(NcrFunction.class, pk);
		return NcrBeanHelper.getNcrFunctionBean(ncrFunction);
	}

	// public static NcrFunctionNotifiersBean
	// getNcrFunctionNotifiersBeanByPk(int pk) throws Exception
	// {
	// NcrFunctionNotifiers ncrFunctionNotifiers =
	// PersistWrapper.readByPrimaryKey(NcrFunctionNotifiers.class, pk);
	// return NcrBeanHelper.getNcrFunctionNotifiersBean(ncrFunctionNotifiers);
	// }

	public static void deleteNcrFunctions(List<NcrFunctionBean> ncrFunctionBeans) throws Exception
	{
		if (ncrFunctionBeans != null)
		{
			for (NcrFunctionBean ncrFunctionBean : ncrFunctionBeans)
			{
				deleteNcrFunction(ncrFunctionBean.getPk());
			}
		}
	}

	public static Integer getMaxNcrFunctionIteration(int objectFk, String objectType) throws Exception
	{
		return PersistWrapper.read(Integer.class,
				"select max(iteration) as iteration from ncr_function  where objectFk=? and objectType=? and isCurrent=false",
				objectFk, objectType);
	}

	public static NcrFunctionBean getCurrentNcrFunction(int objectFk, String objectType, int functionMasterFk)
			throws Exception
	{
		NcrFunction ncrFunction = PersistWrapper.read(NcrFunction.class,
				"select * from ncr_function where objectFk=? and objectType=? and functionMasterFk=? and isCurrent=true",
				objectFk, objectType, functionMasterFk);
		return NcrBeanHelper.getNcrFunctionBean(ncrFunction);
	}

	public static List<NcrFunctionBean> getCurrentNcrFunctions(int objectFk, String objectType)
	{
		List<NcrFunctionBean> ncrFunctionBean = new ArrayList<NcrFunctionBean>();
		try
		{
			List<NcrFunction> ncrFunctions = PersistWrapper.readList(NcrFunction.class,
					"select ncr_function.* from ncr_function inner join ncr_function_master on ncr_function_master.pk= ncr_function.functionMasterFk where ncr_function.objectFk=? and ncr_function.objectType=? and ncr_function.isCurrent=true order by ncr_function.functionMasterFk ASC,ncr_function_master.special ASC",
					objectFk, objectType);
			if (ncrFunctions != null)
			{
				for (NcrFunction ncrFunction : ncrFunctions)
				{
					ncrFunctionBean.add(NcrBeanHelper.getNcrFunctionBean(ncrFunction));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrFunctionBean;
	}

	public static List<NcrFunctionQuery> getHistoryNcrFunctions(int objectFk, String objectType,
			NcrFunctionMaster.TYPE roleType)
	{
		List<NcrFunctionQuery> ncrFunctionQuery = null;
		try
		{
			if (roleType == null || NcrFunctionMaster.TYPE.ALL.equals(roleType))
			{

				ncrFunctionQuery = PersistWrapper.readList(NcrFunctionQuery.class, NcrFunctionQuery.getSqlQuery()
						+ " and  objectFk=? and objectType=? order by ncr_function.iteration DESC, ncr_function.createdDate asc",
						objectFk, objectType);
			} else
			{
				if (NcrFunctionMaster.TYPE.SPECIAL.equals(roleType))
				{
					ncrFunctionQuery = PersistWrapper.readList(NcrFunctionQuery.class, NcrFunctionQuery.getSqlQuery()
							+ " and ncr_function_master.special=? and  objectFk=? and objectType=? order by ncr_function.iteration DESC, ncr_function.createdDate asc",
							true, objectFk, objectType);
				} else if (NcrFunctionMaster.TYPE.NONSPECIAL.equals(roleType))
				{
					ncrFunctionQuery = PersistWrapper.readList(NcrFunctionQuery.class, NcrFunctionQuery.getSqlQuery()
							+ " and ncr_function_master.special=? and  objectFk=? and objectType=? order by ncr_function.iteration DESC, ncr_function.createdDate asc",
							false, objectFk, objectType);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrFunctionQuery;
	}

	public static void deleteNcrFunction(int pk) throws Exception
	{
		deleteNcrFunctionNotifiersByncrFunctionFk(pk);
		PersistWrapper.delete("delete from  ncr_function where pk=? and isCurrent=true", pk);
	}

	public static void deleteNcrFunction(int objectFk, String objectType) throws Exception
	{
		PersistWrapper.delete("delete from  ncr_function where  objectFk=? and objectType=?  ", objectFk, objectType);
	}

	public static void deleteNcrFunction(NcrGroupOID ncrGroupOID) throws Exception
	{
		PersistWrapper.delete(
				" delete ncr_function .* FROM  ncr_function inner join ncr on ncr_function.objectFk=ncr.pk and ncr_function.objectType='"
						+ EntityTypeEnum.NCR.toString() + "'  where ncr.ncrGroupFk=?",
				ncrGroupOID.getPk());
	}

	public static void saveNcrFunctionNotifiers(UserContext context, NcrFunctionNotifiers ncrFunctionNotifiersBean)
			throws Exception
	{
		NcrFunctionNotifiers ncrFunctionNotifiersItem = null;
		if (ncrFunctionNotifiersBean.getPk() != 0)
		{
			ncrFunctionNotifiersItem = PersistWrapper.readByPrimaryKey(NcrFunctionNotifiers.class,
					ncrFunctionNotifiersBean.getPk());
		} else

		{
			ncrFunctionNotifiersItem = new NcrFunctionNotifiers();
			ncrFunctionNotifiersItem.setCreatedBy(context.getUser().getPk());
			ncrFunctionNotifiersItem.setCreatedDate(new Date());
		}
		ncrFunctionNotifiersItem.setNcrFunctionFk(ncrFunctionNotifiersBean.getNcrFunctionFk());
		ncrFunctionNotifiersItem.setUserFk(ncrFunctionNotifiersBean.getUserFk());

		int itemPk;
		if (ncrFunctionNotifiersBean.getPk() != 0)
		{
			PersistWrapper.update(ncrFunctionNotifiersItem);
			itemPk = ncrFunctionNotifiersItem.getPk();
		} else
		{
			itemPk = PersistWrapper.createEntity(ncrFunctionNotifiersItem);
		}

		// return getNcrFunctionNotifiersList(itemPk);
	}

	// Ncr Unit Assign
	public static NcrUnitAssignBean saveNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		NcrUnitAssign ncrUnitAssignItem = null;
		if (ncrUnitAssignBean.getPk() != 0)
		{
			ncrUnitAssignItem = PersistWrapper.readByPrimaryKey(NcrUnitAssign.class, ncrUnitAssignBean.getPk());
		} else
		{
			ncrUnitAssignItem = new NcrUnitAssign();
			ncrUnitAssignItem.setCreatedBy(context.getUser().getPk());
			ncrUnitAssignItem.setCreatedDate(new Date());
		}
		ncrUnitAssignItem.setStatus(ncrUnitAssignBean.getStatus());
		ncrUnitAssignItem.setQuantity(ncrUnitAssignBean.getQuantity());
		ncrUnitAssignItem.setUnitFk(ncrUnitAssignBean.getUnitFk());
		ncrUnitAssignItem.setNcrFk(ncrUnitAssignBean.getNcrFk());
		if (ncrUnitAssignBean.getProjectOID() != null)
			ncrUnitAssignItem.setProjectFk(ncrUnitAssignBean.getProjectOID().getPk());
		ncrUnitAssignItem.setPriority(ncrUnitAssignBean.getPriority());
		ncrUnitAssignItem.setForecastStartDate(ncrUnitAssignBean.getForecastStartDate());
		ncrUnitAssignItem.setForecastCompletionDate(ncrUnitAssignBean.getForecastCompletionDate());
		ncrUnitAssignItem.setHours(ncrUnitAssignBean.getHours());

		ncrUnitAssignItem.setEscape(ncrUnitAssignBean.getEscape());

		ncrUnitAssignItem.setEscape(ncrUnitAssignBean.getEscape());
		// Rework Order
		if (ncrUnitAssignBean.getReworkOrderOid() != null)
		{
			ncrUnitAssignItem.setReworkOrderFk(ncrUnitAssignBean.getReworkOrderOid().getPk());
		} else
		{
			ncrUnitAssignItem.setReworkOrderFk(null);
		}

		if (ncrUnitAssignBean.getCustodianOID() != null)
		{
			ncrUnitAssignItem.setCustodianFk(ncrUnitAssignBean.getCustodianOID().getPk());
		} else
		{
			ncrUnitAssignItem.setCustodianFk(null);
		}

		ncrUnitAssignItem.setIsInternalIssue(ncrUnitAssignBean.getIsInternalIssue());
		ncrUnitAssignItem.setExternalComment(ncrUnitAssignBean.getExternalComment());
		ncrUnitAssignItem.setExternalDescription(ncrUnitAssignBean.getExternalDescription());
		ncrUnitAssignItem.setMarkedInternalBy(ncrUnitAssignBean.getMarkedInternalBy());
		ncrUnitAssignItem.setMarkedInternalDate(ncrUnitAssignBean.getMarkedInternalDate());
		ncrUnitAssignItem.setDispatchNoteNo(ncrUnitAssignBean.getDispatchNoteNo());

		// if (ncrUnitAssignBean.getStatus() != null
		// &&
		// ncrUnitAssignBean.getStatus().equals(OpenItemQuery.STATUS_COMPLETED))
		// {
		// ncrUnitAssignItem.setCompletedBy(context.getUser().getPk());
		// ncrUnitAssignItem.setCompletedDate(new Date());
		// }
		// ncrUnitAssignItem.setCompletedComment(ncrUnitAssignBean.getCompletedComment());
		// if (ncrUnitAssignBean.getStatus() != null &&
		// ncrUnitAssignBean.getStatus().equals(OpenItemQuery.STATUS_CLOSED))
		// {
		// ncrUnitAssignItem.setClosedBy(context.getUser().getPk());
		// ncrUnitAssignItem.setClosedDate(new Date());
		// }
		//
		// ncrUnitAssignItem.setClosedComment(ncrUnitAssignBean.getClosedComment());
		int itemPk = 0;
		if (ncrUnitAssignBean.getPk() != 0)
		{
			PersistWrapper.update(ncrUnitAssignItem);
			itemPk = ncrUnitAssignItem.getPk();
		} else
		{
			itemPk = PersistWrapper.createEntity(ncrUnitAssignItem);
		}

		// adding activity list
		List<NcrItemActivityRefBean> previousNcrItemActivityList = getNcrItemActivityRefBeanListByObjectPkAndObjectType(
				itemPk, EntityTypeEnum.NcrUnitAssign.toString());

		if (previousNcrItemActivityList == null)
		{
			previousNcrItemActivityList = new ArrayList<NcrItemActivityRefBean>();
		}
		if (ncrUnitAssignBean.getNcrItemActivityRefBeanList() != null
				&& ncrUnitAssignBean.getNcrItemActivityRefBeanList().size() > 0)
		{
			for (NcrItemActivityRefBean ncrItemActivityRefBean : ncrUnitAssignBean.getNcrItemActivityRefBeanList())
			{
				if (previousNcrItemActivityList.contains(ncrItemActivityRefBean))
				{
					previousNcrItemActivityList.remove(ncrItemActivityRefBean);
				}
				ncrItemActivityRefBean.setObjectPk(itemPk);
				ncrItemActivityRefBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
				ncrItemActivityRefBean = saveNcrItemActivityRefBean(context, ncrItemActivityRefBean);
			}
		}
		if (previousNcrItemActivityList.size() > 0)
		{
			for (NcrItemActivityRefBean ncrItemActivityRefBean : previousNcrItemActivityList)
			{
				deleteNcrItemActivityRefByPk(ncrItemActivityRefBean.getPk());
			}

		}

		// Resource Requirement
		List<ResourceRequirementBean> previousResourceRequirementList = getResourceRequirementBeanListByObjectPkAndObjectType(
				itemPk, EntityTypeEnum.NcrUnitAssign.toString());

		if (previousResourceRequirementList == null)
		{
			previousResourceRequirementList = new ArrayList<ResourceRequirementBean>();
		}
		if (ncrUnitAssignBean.getResourceRequirementBeanList() != null
				&& ncrUnitAssignBean.getResourceRequirementBeanList().size() > 0)
		{
			for (ResourceRequirementBean resourceRequirementBean : ncrUnitAssignBean.getResourceRequirementBeanList())
			{
				if (previousResourceRequirementList.contains(resourceRequirementBean))
				{
					previousResourceRequirementList.remove(resourceRequirementBean);
				}
				resourceRequirementBean.setObjectPk(itemPk);
				resourceRequirementBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
				resourceRequirementBean = saveResourceRequirementBean(context, resourceRequirementBean);
			}
		}

		if (previousResourceRequirementList.size() > 0)
		{
			for (ResourceRequirementBean resourceRequirementBean : previousResourceRequirementList)
			{
				deleteResourceRequirementByPk(resourceRequirementBean.getPk());
			}

		}

		List<NcrUnitAllocBean> previousUnitAllocBeanList = getNcrUnitAlloc(new NcrUnitAssignOID(itemPk, ""));
		if (previousUnitAllocBeanList == null)
		{
			previousUnitAllocBeanList = new ArrayList<NcrUnitAllocBean>();
		}
		if (ncrUnitAssignBean.getNcrUnitAlloc() != null && ncrUnitAssignBean.getNcrUnitAlloc().size() > 0)
		{
			for (NcrUnitAllocBean ncrUnitAllocBean : ncrUnitAssignBean.getNcrUnitAlloc())
			{
				// ncrUnitAllocBean.setProjectFk(ncrUnitAssignBean.getProjectFk());
				ncrUnitAllocBean.setNcrUnitAssignFk(itemPk);
				// ncrUnitAllocBean.setPartFk(ncrUnitAssignBean.getPartFk());
				// ncrUnitAllocBean.setRevisionFk(ncrUnitAssignBean.getRevisionFk());
				// ncrUnitAllocBean.setSupplierSiteMappingFk(ncrUnitAssignBean.getSupplierSiteMappingFk());
				// if (ncrUnitAssignBean.getUnitFk() != null)
				// {
				// ncrUnitAllocBean.setParentFk(ncrUnitAssignBean.getUnitFk());
				// }
				if ((ncrUnitAllocBean.getRemovedSerialNo() != null
						&& ncrUnitAllocBean.getRemovedSerialNo().trim().length() > 0)
						|| (ncrUnitAllocBean.getFittedSerialNumber() != null
								&& ncrUnitAllocBean.getFittedSerialNumber().trim().length() > 0))
				{
					if (previousUnitAllocBeanList.contains(ncrUnitAllocBean))
					{
						previousUnitAllocBeanList.remove(ncrUnitAllocBean);
					}
					ncrUnitAllocBean = saveNcrUnitAlloc(context, ncrUnitAllocBean);

				}
			}
		}
		deleteNcrUnitAlloc(previousUnitAllocBeanList);
		return getNcrUnitAssignBeanByPk(itemPk);
	}

	public static NcrUnitAssignBean publishNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		NcrUnitAssign nesnItem = null;
		int itemPk = 0;
		if (ncrUnitAssignBean.getPk() != 0)
		{
			nesnItem = PersistWrapper.readByPrimaryKey(NcrUnitAssign.class, ncrUnitAssignBean.getPk());
			nesnItem.setStatus(OpenItemV2.StatusEnum.Open.name());

			if (ncrUnitAssignBean.getPk() != 0)
			{
				PersistWrapper.update(nesnItem);
				itemPk = nesnItem.getPk();
			} else
			{
				itemPk = PersistWrapper.createEntity(nesnItem);
			}

			// adding activity
			List<NcrItemActivityRefBean> previousNcrItemActivityList = getNcrItemActivityRefBeanListByObjectPkAndObjectType(
					itemPk, EntityTypeEnum.NcrUnitAssign.toString());

			if (previousNcrItemActivityList == null)
			{
				previousNcrItemActivityList = new ArrayList<NcrItemActivityRefBean>();
			}
			if (ncrUnitAssignBean.getNcrItemActivityRefBeanList() != null
					&& ncrUnitAssignBean.getNcrItemActivityRefBeanList().size() > 0)
			{
				for (NcrItemActivityRefBean ncrItemActivityRefBean : ncrUnitAssignBean.getNcrItemActivityRefBeanList())
				{
					if (previousNcrItemActivityList.contains(ncrItemActivityRefBean))
					{
						previousNcrItemActivityList.remove(ncrItemActivityRefBean);
					}
					ncrItemActivityRefBean.setObjectPk(itemPk);
					ncrItemActivityRefBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
					ncrItemActivityRefBean = saveNcrItemActivityRefBean(context, ncrItemActivityRefBean);
				}
			}
			if (previousNcrItemActivityList.size() > 0)
			{
				for (NcrItemActivityRefBean ncrItemActivityRefBean : previousNcrItemActivityList)
				{
					deleteNcrItemActivityRefByPk(ncrItemActivityRefBean.getPk());
				}

			}
			// Resource Requirement
			List<ResourceRequirementBean> previousResourceRequirementList = getResourceRequirementBeanListByObjectPkAndObjectType(
					itemPk, EntityTypeEnum.NcrUnitAssign.toString());

			if (previousResourceRequirementList == null)
			{
				previousResourceRequirementList = new ArrayList<ResourceRequirementBean>();
			}
			if (ncrUnitAssignBean.getResourceRequirementBeanList() != null
					&& ncrUnitAssignBean.getResourceRequirementBeanList().size() > 0)
			{
				for (ResourceRequirementBean resourceRequirementBean : ncrUnitAssignBean
						.getResourceRequirementBeanList())
				{
					if (previousResourceRequirementList.contains(resourceRequirementBean))
					{
						previousResourceRequirementList.remove(resourceRequirementBean);
					}
					resourceRequirementBean.setObjectPk(itemPk);
					resourceRequirementBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
					resourceRequirementBean = saveResourceRequirementBean(context, resourceRequirementBean);
				}
			}

			if (previousResourceRequirementList.size() > 0)
			{
				for (ResourceRequirementBean resourceRequirementBean : previousResourceRequirementList)
				{
					deleteResourceRequirementByPk(resourceRequirementBean.getPk());
				}

			}

		}
		return getNcrUnitAssignBeanByPk(itemPk);
	}

	public static NcrUnitAssignBean completeNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		NcrUnitAssign nesnItem = null;
		int itemPk = 0;
		if (ncrUnitAssignBean.getPk() != 0)
		{
			nesnItem = PersistWrapper.readByPrimaryKey(NcrUnitAssign.class, ncrUnitAssignBean.getPk());

			if (ncrUnitAssignBean.getCustodianOID() != null)
			{
				nesnItem.setCustodianFk(ncrUnitAssignBean.getCustodianOID().getPk());
			} else
			{
				nesnItem.setCustodianFk(null);
			}

			nesnItem.setStatus(OpenItemV2.StatusEnum.Completed.name());
			nesnItem.setCompletedBy(context.getUser().getPk());
			nesnItem.setCompletedComment(ncrUnitAssignBean.getCompletedComment());
			nesnItem.setCompletedDate(new Date());
			if (ncrUnitAssignBean.getPk() != 0)
			{
				PersistWrapper.update(nesnItem);
				itemPk = nesnItem.getPk();
			} else
			{
				itemPk = PersistWrapper.createEntity(nesnItem);
			}
			// adding activity list
			List<NcrItemActivityRefBean> previousNcrItemActivityList = getNcrItemActivityRefBeanListByObjectPkAndObjectType(
					itemPk, EntityTypeEnum.NcrUnitAssign.toString());

			if (previousNcrItemActivityList == null)
			{
				previousNcrItemActivityList = new ArrayList<NcrItemActivityRefBean>();
			}
			if (ncrUnitAssignBean.getNcrItemActivityRefBeanList() != null
					&& ncrUnitAssignBean.getNcrItemActivityRefBeanList().size() > 0)
			{
				for (NcrItemActivityRefBean ncrItemActivityRefBean : ncrUnitAssignBean.getNcrItemActivityRefBeanList())
				{
					if (previousNcrItemActivityList.contains(ncrItemActivityRefBean))
					{
						previousNcrItemActivityList.remove(ncrItemActivityRefBean);
					}
					ncrItemActivityRefBean.setObjectPk(itemPk);
					ncrItemActivityRefBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
					ncrItemActivityRefBean = saveNcrItemActivityRefBean(context, ncrItemActivityRefBean);
				}
			}
			if (previousNcrItemActivityList.size() > 0)
			{
				for (NcrItemActivityRefBean ncrItemActivityRefBean : previousNcrItemActivityList)
				{
					deleteNcrItemActivityRefByPk(ncrItemActivityRefBean.getPk());
				}

			}

			// Resource Requirement
			List<ResourceRequirementBean> previousResourceRequirementList = getResourceRequirementBeanListByObjectPkAndObjectType(
					itemPk, EntityTypeEnum.NcrUnitAssign.toString());

			if (previousResourceRequirementList == null)
			{
				previousResourceRequirementList = new ArrayList<ResourceRequirementBean>();
			}
			if (ncrUnitAssignBean.getResourceRequirementBeanList() != null
					&& ncrUnitAssignBean.getResourceRequirementBeanList().size() > 0)
			{
				for (ResourceRequirementBean resourceRequirementBean : ncrUnitAssignBean
						.getResourceRequirementBeanList())
				{
					if (previousResourceRequirementList.contains(resourceRequirementBean))
					{
						previousResourceRequirementList.remove(resourceRequirementBean);
					}
					resourceRequirementBean.setObjectPk(itemPk);
					resourceRequirementBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
					resourceRequirementBean = saveResourceRequirementBean(context, resourceRequirementBean);
				}
			}

			if (previousResourceRequirementList.size() > 0)
			{
				for (ResourceRequirementBean resourceRequirementBean : previousResourceRequirementList)
				{
					deleteResourceRequirementByPk(resourceRequirementBean.getPk());
				}

			}
			List<NcrUnitAllocBean> previousUnitAllocBeanList = getNcrUnitAlloc(new NcrUnitAssignOID(itemPk, ""));
			if (previousUnitAllocBeanList == null)
			{
				previousUnitAllocBeanList = new ArrayList<NcrUnitAllocBean>();
			}

			if (ncrUnitAssignBean.getNcrUnitAlloc() != null && ncrUnitAssignBean.getNcrUnitAlloc().size() > 0)
			{
				for (NcrUnitAllocBean ncrUnitAllocBean : ncrUnitAssignBean.getNcrUnitAlloc())
				{
					// ncrUnitAllocBean.setProjectFk(ncrUnitAssignBean.getProjectFk());
					ncrUnitAllocBean.setNcrUnitAssignFk(itemPk);
					// ncrUnitAllocBean.setPartFk(ncrUnitAssignBean.getPartFk());
					// ncrUnitAllocBean.setRevisionFk(ncrUnitAssignBean.getRevisionFk());
					// ncrUnitAllocBean.setSupplierSiteMappingFk(ncrUnitAssignBean.getSupplierSiteMappingFk());
					// if (ncrUnitAssignBean.getUnitFk() != null)
					// {
					// ncrUnitAllocBean.setParentFk(ncrUnitAssignBean.getUnitFk());
					// }
					if ((ncrUnitAllocBean.getRemovedSerialNo() != null
							&& ncrUnitAllocBean.getRemovedSerialNo().trim().length() > 0)
							|| (ncrUnitAllocBean.getFittedSerialNumber() != null
									&& ncrUnitAllocBean.getFittedSerialNumber().trim().length() > 0))
					{
						ncrUnitAllocBean = saveNcrUnitAlloc(context, ncrUnitAllocBean);
						if (previousUnitAllocBeanList.contains(ncrUnitAllocBean))
						{
							previousUnitAllocBeanList.remove(ncrUnitAllocBean);
						}
					}
				}
			}
			deleteNcrUnitAlloc(previousUnitAllocBeanList);
		}
		return getNcrUnitAssignBeanByPk(itemPk);
	}

	public static NcrUnitAssignBean closeNcrUnitAssign(UserContext context, NcrUnitAssignBean ncrUnitAssignBean)
			throws Exception
	{
		NcrUnitAssign ncrUnitAssign = null;
		int itemPk = 0;
		if (ncrUnitAssignBean.getPk() != 0)
		{
			ncrUnitAssign = PersistWrapper.readByPrimaryKey(NcrUnitAssign.class, ncrUnitAssignBean.getPk());

			if (ncrUnitAssignBean.getCustodianOID() != null)
			{
				ncrUnitAssign.setCustodianFk(ncrUnitAssignBean.getCustodianOID().getPk());
			} else
			{
				ncrUnitAssign.setCustodianFk(null);
			}

			ncrUnitAssign.setStatus(OpenItemV2.StatusEnum.Closed.name());
			ncrUnitAssign.setClosedBy(context.getUser().getPk());
			ncrUnitAssign.setClosedComment(ncrUnitAssignBean.getClosedComment());
			ncrUnitAssign.setClosedDate(new Date());
			if (!ncrUnitAssignBean.getStatus().equals(OpenItemV2.StatusEnum.Completed.name()))
			{
				ncrUnitAssign.setCompletedBy(context.getUser().getPk());
				ncrUnitAssign.setCompletedComment(ncrUnitAssignBean.getClosedComment());
				ncrUnitAssign.setCompletedDate(new Date());
			}

			if (ncrUnitAssignBean.getPk() != 0)
			{
				PersistWrapper.update(ncrUnitAssign);
				itemPk = ncrUnitAssign.getPk();
			} else
			{
				itemPk = PersistWrapper.createEntity(ncrUnitAssign);
			}

			// adding activityList
			List<NcrItemActivityRefBean> previousActivityRefList = getNcrItemActivityRefBeanListByObjectPkAndObjectType(
					itemPk, EntityTypeEnum.NcrUnitAssign.toString());

			if (previousActivityRefList == null)
			{
				previousActivityRefList = new ArrayList<NcrItemActivityRefBean>();
			}
			if (ncrUnitAssignBean.getNcrItemActivityRefBeanList() != null
					&& ncrUnitAssignBean.getNcrItemActivityRefBeanList().size() > 0)
			{
				for (int i = 0; i < ncrUnitAssignBean.getNcrItemActivityRefBeanList().size(); i++)
				{
					NcrItemActivityRefBean ncrItemActivityRefBean = (NcrItemActivityRefBean) ncrUnitAssignBean
							.getNcrItemActivityRefBeanList().get(i);
					if (previousActivityRefList.contains(ncrItemActivityRefBean))
					{
						previousActivityRefList.remove(ncrItemActivityRefBean);
					}
					ncrItemActivityRefBean.setObjectPk(itemPk);
					ncrItemActivityRefBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
					ncrItemActivityRefBean = saveNcrItemActivityRefBean(context, ncrItemActivityRefBean);
				}
			}
			if (previousActivityRefList.size() > 0)
			{
				for (NcrItemActivityRefBean ncrItemActivityRefBean : previousActivityRefList)
				{
					deleteNcrItemActivityRefByPk(ncrItemActivityRefBean.getPk());
				}
			}

			// Resource Requirement
			List<ResourceRequirementBean> previousResourceRequirementList = getResourceRequirementBeanListByObjectPkAndObjectType(
					itemPk, EntityTypeEnum.NcrUnitAssign.toString());

			if (previousResourceRequirementList == null)
			{
				previousResourceRequirementList = new ArrayList<ResourceRequirementBean>();
			}
			if (ncrUnitAssignBean.getResourceRequirementBeanList() != null
					&& ncrUnitAssignBean.getResourceRequirementBeanList().size() > 0)
			{
				for (int i = 0; i < ncrUnitAssignBean.getResourceRequirementBeanList().size(); i++)
				{
					ResourceRequirementBean resourceRequirementBean = (ResourceRequirementBean) ncrUnitAssignBean
							.getResourceRequirementBeanList().get(i);
					if (previousResourceRequirementList.contains(resourceRequirementBean))
					{
						previousResourceRequirementList.remove(resourceRequirementBean);
					}
					resourceRequirementBean.setObjectPk(itemPk);
					resourceRequirementBean.setObjectType(EntityTypeEnum.NcrUnitAssign.toString());
					resourceRequirementBean = saveResourceRequirementBean(context, resourceRequirementBean);
				}
			}

			if (previousResourceRequirementList.size() > 0)
			{
				for (ResourceRequirementBean resourceRequirementBean : previousResourceRequirementList)
				{
					deleteResourceRequirementByPk(resourceRequirementBean.getPk());
				}
			}
			List<NcrUnitAllocBean> previousUnitAllocBeanList = getNcrUnitAlloc(new NcrUnitAssignOID(itemPk, ""));
			if (previousUnitAllocBeanList == null)
			{
				previousUnitAllocBeanList = new ArrayList<NcrUnitAllocBean>();
			}

			if (ncrUnitAssignBean.getNcrUnitAlloc() != null && ncrUnitAssignBean.getNcrUnitAlloc().size() > 0)
			{
				for (NcrUnitAllocBean ncrUnitAllocBean : ncrUnitAssignBean.getNcrUnitAlloc())
				{
					// ncrUnitAllocBean.setProjectFk(ncrUnitAssignBean.getProjectFk());
					ncrUnitAllocBean.setNcrUnitAssignFk(itemPk);
					// ncrUnitAllocBean.setPartFk(ncrUnitAssignBean.getPartFk());
					// ncrUnitAllocBean.setRevisionFk(ncrUnitAssignBean.getRevisionFk());
					// ncrUnitAllocBean.setSupplierSiteMappingFk(ncrUnitAssignBean.getSupplierSiteMappingFk());
					// if (ncrUnitAssignBean.getUnitFk() != null)
					// {
					// ncrUnitAllocBean.setParentFk(ncrUnitAssignBean.getUnitFk());
					// }
					if ((ncrUnitAllocBean.getRemovedSerialNo() != null
							&& ncrUnitAllocBean.getRemovedSerialNo().trim().length() > 0)
							|| (ncrUnitAllocBean.getFittedSerialNumber() != null
									&& ncrUnitAllocBean.getFittedSerialNumber().trim().length() > 0))
					{
						ncrUnitAllocBean = saveNcrUnitAlloc(context, ncrUnitAllocBean);
						if (previousUnitAllocBeanList.contains(ncrUnitAllocBean))
						{
							previousUnitAllocBeanList.remove(ncrUnitAllocBean);
						}
					}
				}
			}
			deleteNcrUnitAlloc(previousUnitAllocBeanList);
		}
		return getNcrUnitAssignBeanByPk(itemPk);
	}

	public static NcrUnitAssignBean getNcrUnitAssignBeanByPk(int pk) throws Exception
	{
		NcrUnitAssign ncrUnitAssign = PersistWrapper.readByPrimaryKey(NcrUnitAssign.class, pk);
		return NcrBeanHelper.getNcrUnitAssignBean(ncrUnitAssign);
	}

	public static List<NcrUnitAssignBean> getNcrUnitAssign(NcrItemOID ncrItemOID)
	{
		List<NcrUnitAssignBean> ncrUnitAssignBeans = new ArrayList<NcrUnitAssignBean>();
		try
		{
			List<NcrUnitAssign> ncrUnitAssigns = PersistWrapper.readList(NcrUnitAssign.class,
					"SELECT * FROM ncr_unit_assign where ncrFk=? ", ncrItemOID.getPk());
			if (ncrUnitAssigns != null)
			{
				for (NcrUnitAssign ncrUnitAssign : ncrUnitAssigns)
				{
					ncrUnitAssignBeans.add(NcrBeanHelper.getNcrUnitAssignBean(ncrUnitAssign));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrUnitAssignBeans;
	}

	public static void deleteNcrUnitAssign(int pk) throws Exception
	{
		deleteNcrUnitAlloc(new NcrUnitAssignOID(pk, ""));
		PersistWrapper.delete("delete from ncr_unit_assign where pk=?", pk);
	}

	public static void deleteNcrUnitAssign(NcrItemOID ncrItemOID) throws Exception
	{
		deleteNcrUnitAlloc(ncrItemOID);
		PersistWrapper.delete("delete ncr_unit_assign.* from ncr_unit_assign where ncrFk=?", ncrItemOID.getPk());
	}

	public static void deleteNcrUnitAssign(NcrGroupOID ncrGroupOID) throws Exception
	{
		deleteNcrUnitAlloc(ncrGroupOID);
		PersistWrapper.delete(
				" delete ncr_unit_assign.* FROM ncr_unit_assign inner join ncr on ncr_unit_assign.ncrFk=ncr.pk where ncr.ncrGroupFk=?",
				ncrGroupOID.getPk());
	}

	public static NcrUnitAssign getNcrUnitAssign(NcrUnitAssignOID ncrUnitAssignOID)
	{
		return PersistWrapper.readByPrimaryKey(NcrUnitAssign.class, ncrUnitAssignOID.getPk());
	}

	public static NcrUnitAssignBean getNcrUnitAssignBean(NcrUnitAssignOID ncrUnitAssignOID) throws Exception
	{
		NcrUnitAssign ncrUnitAssign = PersistWrapper.readByPrimaryKey(NcrUnitAssign.class, ncrUnitAssignOID.getPk());
		return NcrBeanHelper.getNcrUnitAssignBean(ncrUnitAssign);
	}

	// Ncr Unit Alloc
	public static NcrUnitAllocBean saveNcrUnitAlloc(UserContext context, NcrUnitAllocBean ncrUnitAllocBean)
			throws Exception
	{
		Boolean replaceFlag = false;
		NcrUnitAlloc nesnItem = null;
		if (ncrUnitAllocBean.getPk() > 0)
		{
			nesnItem = PersistWrapper.readByPrimaryKey(NcrUnitAlloc.class, ncrUnitAllocBean.getPk());
		} else
		{
			nesnItem = new NcrUnitAlloc();
			nesnItem.setCreatedBy(context.getUser().getPk());
			nesnItem.setCreatedDate(new Date());
		}
		if (ncrUnitAllocBean.getRemovedUnitFk() == null)
		{
			ncrUnitAllocBean.setRemovedUnitFk(0);
		}

		// check the serial number of the unitItem
		if (ncrUnitAllocBean != null && ncrUnitAllocBean.getRemovedSerialNo() != null
				&& !ncrUnitAllocBean.getRemovedSerialNo().trim().isEmpty())
		{
			NcrUnitAssign unitAssign = PersistWrapper.read(NcrUnitAssign.class,
					"SELECT * FROM ncr_unit_assign where pk=?", ncrUnitAllocBean.getNcrUnitAssignFk());
			Unit unit = UnitDelegate.getUnitBySerialNo(ncrUnitAllocBean.getRemovedSerialNo());
			NcrGroup ncrGroup = PersistWrapper.read(NcrGroup.class,
					"select ncr_group.* from ncr_group inner join ncr on ncr.ncrGroupFk=ncr_group.pk inner join ncr_unit_assign on ncr_unit_assign.ncrFk=ncr.pk where ncr_unit_assign.pk=? group by ncr_group.pk;",
					ncrUnitAllocBean.getNcrUnitAssignFk());
			if (unit == null || unit.getPk() == 0)
			{

				UnitBean unitBean = new UnitBean();
				if (unitAssign.getUnitFk() != null)
					unitBean.setParentPk(unitAssign.getUnitFk());
				unitBean.setSerialNo(ncrUnitAllocBean.getRemovedSerialNo());
				unitBean.setUnitName(ncrUnitAllocBean.getRemovedSerialNo());
				unitBean.setUnitDescription(ncrUnitAllocBean.getRemovedSerialNo());
				unitBean.setStatus("Open");
				unitBean.setUnitOriginType(UnitOriginType.Procured);
				unitBean.setSupplierOID(new SupplierOID(ncrGroup.getSupplierFk()));

				Site site = SiteDelegate.getSite(ncrGroup.getSiteFk());
				SiteGroup sg = SiteDelegate.getSiteGroup(site.getSiteGroupFk());
				unitBean.setSiteGroupOID(sg.getOID());

				unitBean.setPartPk(ncrGroup.getPartFk());
				unitBean.setPartRevisionPk(ncrGroup.getRevisionFk());
				ProjectDelegate.createUnit(context, ncrGroup.getProjectFk(), unitBean, false);
				Unit unitItem = UnitDelegate.getUnitBySerialNo(ncrUnitAllocBean.getRemovedSerialNo());
				if (unitItem != null)
				{
					nesnItem.setRemovedUnitFk(unitItem.getPk());
				} else
				{
					nesnItem.setRemovedUnitFk(0);
				}
			} else
			{
				if (unit.getPk() != 0)
				{
					if (unitAssign.getUnitFk() == null)
					{
						// TODO doing new UnitOID() without setting pk will
						// created problems. so i have removed empty constructor
						// for OID.
						UnitDelegate.changeUnitParent(context, null, new UnitOID(unit.getPk(), ""),
								new ProjectOID(ncrGroup.getProjectFk()));

					} else
					{
						UnitDelegate.changeUnitParent(context, new UnitOID(unitAssign.getUnitFk(), ""),
								new UnitOID(unit.getPk(), ""), new ProjectOID(ncrGroup.getProjectFk()));
						// UnitDelegate.changeUnitParent(context,
						// new
						// UnitOID(ProjectDelegate.getUnitByPk(unitAssign.getUnitFk()).getPk(),
						// ""),
						// new UnitOID(unit.getPk(), ""), new
						// ProjectOID(ncrGroup.getProjectFk()));
					}
					if (nesnItem != null)
					{
						nesnItem.setRemovedUnitFk(unit.getPk());
					}
				}
			}
		} else
		{
			nesnItem.setRemovedUnitFk(0);
		}
		// check the serial number of the replaced item
		if (ncrUnitAllocBean.getFittedSerialNumber() != null
				&& !ncrUnitAllocBean.getFittedSerialNumber().trim().isEmpty())
		{
			NcrUnitAssign unitAssign = PersistWrapper.read(NcrUnitAssign.class,
					"SELECT * FROM ncr_unit_assign where pk=?", ncrUnitAllocBean.getNcrUnitAssignFk());
			Unit unitreplace = UnitDelegate.getUnitBySerialNo(ncrUnitAllocBean.getFittedSerialNumber());
			NcrGroup ncrGroup = PersistWrapper.read(NcrGroup.class,
					"select ncr_group.* from ncr_group inner join ncr on ncr.ncrGroupFk=ncr_group.pk inner join ncr_unit_assign on ncr_unit_assign.ncrFk=ncr.pk where ncr_unit_assign.pk=? group by ncr_group.pk;",
					ncrUnitAllocBean.getNcrUnitAssignFk());

			if (unitreplace == null || unitreplace.getPk() == 0)
			{
				UnitBean unitBean = new UnitBean();
				if (unitAssign.getUnitFk() != null)
					unitBean.setParentPk(unitAssign.getUnitFk());
				unitBean.setSerialNo(ncrUnitAllocBean.getFittedSerialNumber());
				unitBean.setUnitName(ncrUnitAllocBean.getFittedSerialNumber());
				unitBean.setUnitDescription(ncrUnitAllocBean.getFittedSerialNumber());
				unitBean.setStatus("Open");
				unitBean.setUnitOriginType(UnitOriginType.Procured);
				unitBean.setPartPk(ncrGroup.getPartFk());
				unitBean.setPartRevisionPk(ncrGroup.getRevisionFk());
				unitBean.setSupplierOID(new SupplierOID(ncrGroup.getSupplierFk()));

				Site site = SiteDelegate.getSite(ncrGroup.getSiteFk());
				SiteGroup sg = SiteDelegate.getSiteGroup(site.getSiteGroupFk());
				unitBean.setSiteGroupOID(sg.getOID());

				ProjectDelegate.createUnit(context, ncrGroup.getProjectFk(), unitBean, false);
				Unit unitItem = UnitDelegate.getUnitBySerialNo(ncrUnitAllocBean.getFittedSerialNumber());
				if (unitItem != null)
				{
					nesnItem.setFittedUnitFk(unitItem.getPk());
				} else
				{
					nesnItem.setFittedUnitFk(0);
				}
				replaceFlag = true;
			} else
			{
				if (unitreplace != null)
				{
					if (unitAssign.getUnitFk() == null)
					{
						UnitDelegate.changeUnitParent(context, null, new UnitOID(unitreplace.getPk(), ""),
								new ProjectOID(ncrGroup.getProjectFk()));

					} else
					{
						UnitDelegate.changeUnitParent(context, new UnitOID(unitAssign.getUnitFk(), ""),
								new UnitOID(unitreplace.getPk(), ""), new ProjectOID(ncrGroup.getProjectFk()));
					}
				}

				if ((nesnItem.getFittedUnitFk() == null)
						|| (nesnItem.getFittedUnitFk() != null && unitreplace.getPk() != nesnItem.getFittedUnitFk()))
				{
					replaceFlag = true;
				}
				nesnItem.setFittedUnitFk(unitreplace.getPk());
			}
		} else
		{
			nesnItem.setFittedUnitFk(0);
		}
		if (replaceFlag)
		{
			nesnItem.setReplacedBy(context.getUser().getPk());
			nesnItem.setReplacedDate(new Date());
		}
		nesnItem.setNcrUnitAssignFk(ncrUnitAllocBean.getNcrUnitAssignFk());

		int itemPk;
		if (ncrUnitAllocBean.getPk() != 0)
		{
			PersistWrapper.update(nesnItem);
			itemPk = nesnItem.getPk();
		} else
		{
			itemPk = PersistWrapper.createEntity(nesnItem);
		}
		return getNcrUnitAllocBeanByPk(itemPk);
	}

	public static NcrUnitAllocBean getNcrUnitAllocBeanByPk(int pk) throws Exception
	{
		NcrUnitAlloc ncrUnitAlloc = PersistWrapper.readByPrimaryKey(NcrUnitAlloc.class, pk);
		return NcrBeanHelper.getNcrUnitAllocBean(ncrUnitAlloc);
	}

	public static List<NcrUnitAllocBean> getNcrUnitAlloc(NcrUnitAssignOID ncrUnitAssignOID)
	{
		List<NcrUnitAlloc> listNcrUnitAlloc = PersistWrapper.readList(NcrUnitAlloc.class,
				"SELECT ncr_unit_alloc.* FROM ncr_unit_alloc where ncr_unit_alloc.ncrUnitAssignFk=?",
				ncrUnitAssignOID.getPk());
		List<NcrUnitAllocBean> listNcrUnitAllocBean = new ArrayList<NcrUnitAllocBean>();
		if (listNcrUnitAlloc != null)
		{
			for (NcrUnitAlloc ncrUnitAlloc : listNcrUnitAlloc)
			{
				listNcrUnitAllocBean.add(NcrBeanHelper.getNcrUnitAllocBean(ncrUnitAlloc));
			}
		}
		return listNcrUnitAllocBean;
	}

	public static void deleteNcrUnitAlloc(int pk) throws Exception
	{
		PersistWrapper.delete("delete ncr_unit_alloc.* from ncr_unit_alloc where pk=?", pk);

	}

	public static void deleteNcrUnitAlloc(NcrUnitAssignOID ncrUnitAssignOID) throws Exception
	{
		PersistWrapper.delete("delete ncr_unit_alloc.* from ncr_unit_alloc where ncr_unit_alloc.ncrUnitAssignFk =?",
				ncrUnitAssignOID.getPk());
	}

	public static void deleteNcrUnitAlloc(NcrItemOID ncrItemOID) throws Exception
	{
		PersistWrapper.delete(
				"delete ncr_unit_alloc.* from ncr_unit_alloc inner join ncr_unit_assign on ncr_unit_assign.pk=ncr_unit_alloc.ncrUnitAssignFk where ncr_unit_assign.ncrFk=?",
				ncrItemOID.getPk());
	}

	public static void deleteNcrUnitAlloc(NcrGroupOID ncrGroupOID) throws Exception
	{
		PersistWrapper.delete(
				"delete ncr_unit_alloc.* from ncr_unit_alloc inner join ncr_unit_assign on ncr_unit_assign.pk=ncr_unit_alloc.ncrUnitAssignFk inner join  ncr on ncr_unit_assign.ncrFk=ncr.pk where ncr.ncrGroupFk=?",
				ncrGroupOID.getPk());
	}

	public static void deleteNcrUnitAlloc(List<NcrUnitAllocBean> ncrUnitAllocBeans) throws Exception
	{
		if (ncrUnitAllocBeans != null && ncrUnitAllocBeans.size() > 0)
		{
			for (NcrUnitAllocBean ncrUnitAllocBean : ncrUnitAllocBeans)
			{
				deleteNcrUnitAlloc(ncrUnitAllocBean.getPk());
			}
		}
	}

	// activity list

	public static NcrItemActivityRefBean saveNcrItemActivityRefBean(UserContext context,
			NcrItemActivityRefBean ncrItemActivityRefBean) throws Exception
	{
		NcrItemActivityRef ncrItemActivityRefItem = null;
		int itemPk;
		if (ncrItemActivityRefBean.getPk() != 0)
		{
			ncrItemActivityRefItem = PersistWrapper.readByPrimaryKey(NcrItemActivityRef.class,
					ncrItemActivityRefBean.getPk());
		} else
		{
			ncrItemActivityRefItem = new NcrItemActivityRef();
			ncrItemActivityRefItem.setCreatedBy(context.getUser().getPk());
			ncrItemActivityRefItem.setCreatedDate(new Date());
		}

		ncrItemActivityRefItem.setObjectPk(ncrItemActivityRefBean.getObjectPk());
		ncrItemActivityRefItem.setObjectType(ncrItemActivityRefBean.getObjectType());
		ncrItemActivityRefItem.setNcrItemActivityMasterFk(ncrItemActivityRefBean.getNcrItemActivityMasterFk());
		if (ncrItemActivityRefBean.getPk() != 0)
		{
			PersistWrapper.update(ncrItemActivityRefItem);
			itemPk = ncrItemActivityRefItem.getPk();

		} else
		{
			itemPk = PersistWrapper.createEntity(ncrItemActivityRefItem);
		}
		ncrItemActivityRefBean = getNcrItemActivityRefBeanByPk(itemPk);
		return ncrItemActivityRefBean;
	}

	public static NcrItemActivityRefBean getNcrItemActivityRefBeanByPk(int pk) throws Exception
	{
		NcrItemActivityRef ncrItemActivityRefItem = PersistWrapper.readByPrimaryKey(NcrItemActivityRef.class, pk);
		return NcrBeanHelper.getNcrItemActivityRefBean(ncrItemActivityRefItem);
	}

	// resource Requirement
	public static List<NcrItemActivityRefBean> getNcrItemActivityRefBeanListByObjectPkAndObjectType(int objectPk,
			String objectType)
	{
		List<NcrItemActivityRefBean> ncrItemActivityRefBeanList = new ArrayList<NcrItemActivityRefBean>();
		List<NcrItemActivityRef> ncrItemActivityRefList = PersistWrapper.readList(NcrItemActivityRef.class,
				"select * from ncr_item_activity_ref where objectPk =? and objectType=?", objectPk, objectType);
		if (ncrItemActivityRefList != null)
		{
			for (NcrItemActivityRef ncrItemActivityRefItem : ncrItemActivityRefList)
			{
				ncrItemActivityRefBeanList.add(NcrBeanHelper.getNcrItemActivityRefBean(ncrItemActivityRefItem));
			}
		}
		return ncrItemActivityRefBeanList;
	}

	private static void deleteNcrItemActivityRefByPk(int pk)
	{
		try
		{
			PersistWrapper.delete("delete from ncr_item_activity_ref where pk=?", pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static ResourceRequirementBean saveResourceRequirementBean(UserContext context,
			ResourceRequirementBean resourceRequirementBean) throws Exception
	{
		ResourceRequirement resourceRequirementItem = null;
		int itemPk;
		if (resourceRequirementBean.getPk() != 0)
		{
			resourceRequirementItem = PersistWrapper.readByPrimaryKey(ResourceRequirement.class,
					resourceRequirementBean.getPk());
		} else
		{
			resourceRequirementItem = new ResourceRequirement();
			resourceRequirementItem.setCreatedBy(context.getUser().getPk());
			resourceRequirementItem.setCreatedDate(new Date());
		}
		resourceRequirementItem.setObjectPk(resourceRequirementBean.getObjectPk());
		resourceRequirementItem.setObjectType(resourceRequirementBean.getObjectType());
		resourceRequirementItem.setResourceName(resourceRequirementBean.getResourceName());
		resourceRequirementItem.setHours(resourceRequirementBean.getHours());

		if (resourceRequirementBean.getPk() != 0)
		{
			PersistWrapper.update(resourceRequirementItem);
			itemPk = resourceRequirementItem.getPk();

		} else
		{
			itemPk = PersistWrapper.createEntity(resourceRequirementItem);
		}
		resourceRequirementBean = getResourceRequirementBeanByPk(itemPk);
		return resourceRequirementBean;
	}

	public static ResourceRequirementBean getResourceRequirementBeanByPk(int pk) throws Exception
	{
		ResourceRequirement resourceRequirementItem = PersistWrapper.readByPrimaryKey(ResourceRequirement.class, pk);
		return NcrBeanHelper.getResourceRequirementBean(resourceRequirementItem);
	}

	public static List<ResourceRequirementBean> getResourceRequirementBeanListByObjectPkAndObjectType(int objectPk,
			String objectType)
	{
		List<ResourceRequirementBean> resourceRequirementBeanList = new ArrayList<ResourceRequirementBean>();
		List<ResourceRequirement> resourceRequirementList = PersistWrapper.readList(ResourceRequirement.class,
				"select * from resource_requirement where objectPk =? and objectType=?", objectPk, objectType);
		if (resourceRequirementList != null)
		{
			for (ResourceRequirement resourceRequirementItem : resourceRequirementList)
			{
				resourceRequirementBeanList.add(NcrBeanHelper.getResourceRequirementBean(resourceRequirementItem));
			}
		}
		return resourceRequirementBeanList;
	}

	private static void deleteResourceRequirementByPk(int pk)
	{
		try
		{
			PersistWrapper.delete("delete from resource_requirement where pk=?", pk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static List<NcrItemBean> getNcrDescList(PartOID partOID)
	{
		List<NcrItemBean> listNcrDescRequired = null;
		try
		{
			listNcrDescRequired = PersistWrapper.readList(NcrItemBean.class,
					"SELECT distinct ncrDesc FROM ncr inner join ncr_group  on  (ncr.ncrGroupFk =ncr_group.pk ) where ncr.eStatus=? and partFk=? and ncrDesc is  not null order by ncr.createdDate  desc limit 0,20",
					EStatusEnum.Active.getValue(), partOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return listNcrDescRequired;
	}

	// alter the ncr unit alloc table.
	// public static void alterNcrUnitAssign(UserContext context)
	// {
	// try
	// {
	// List<NcrGroup> ncrGroups = PersistWrapper.readList(NcrGroup.class,
	// "select * from ncr_group where eStatus=?",
	// EStatusEnum.Active.getValue());
	// for (NcrGroup ncrGroup : ncrGroups)
	// {
	// List<NcrItem> listNcrItem = PersistWrapper.readList(NcrItem.class,
	// "select * from ncr where ncrGroupFk=? and eStatus=?", ncrGroup.getPk(),
	// EStatusEnum.Active.getValue());
	// if (listNcrItem != null)
	// {
	// for (NcrItem ncrItem : listNcrItem)
	// {
	// List<NcrUnitAssign> ncrUnitAssigns =
	// PersistWrapper.readList(NcrUnitAssign.class,
	// "select * from ncr_unit_assign where ncrFk=?", ncrItem.getPk());
	// Boolean flag = false;
	// float allocatedQuantity = 0f;
	// if (ncrUnitAssigns != null && ncrUnitAssigns.size() > 0)
	// {
	// for (NcrUnitAssign ncrUnitAssign : ncrUnitAssigns)
	// {
	// allocatedQuantity = allocatedQuantity + ncrUnitAssign.getQuantity();
	// if (ncrUnitAssign.getUnitFk() == null)
	// {
	// flag = true;
	// break;
	// }
	// }
	// }
	// if (!flag)
	// {
	// NcrUnitAssignBean newNcrUnitAssignBean = new NcrUnitAssignBean();
	// newNcrUnitAssignBean.setNcrFk(ncrItem.getPk());
	// newNcrUnitAssignBean.setUnitFk(null);
	// if (ncrItem.getQuantity() != null)
	// {
	// allocatedQuantity = ncrItem.getQuantity() - allocatedQuantity;
	// } else
	// {
	// allocatedQuantity = 0;
	// }
	// newNcrUnitAssignBean.setQuantity(allocatedQuantity);
	// PersistWrapper.executeUpdate(
	// "INSERT INTO ncr_unit_assign (`ncrFk`, `quantity`, `createdBy`,
	// `createdDate`,`status`) VALUES (?,?,?,?,?);",
	// ncrItem.getPk(), allocatedQuantity, context.getUser().getPk(),
	// ncrItem.getCreatedDate(), OpenItemV2.StatusEnum.Draft.name());
	//
	// }
	// }
	// }
	// }
	//
	// List<Integer> ncrUnitAllocPks = PersistWrapper.readList(Integer.class,
	// "select ncr_unit_alloc.pk from ncr_unit_alloc", null);
	// PersistWrapper.executeUpdate(
	// "CREATE TABLE `ncr_unit_alloc_temp` (`pk` int(11) NOT NULL
	// AUTO_INCREMENT,`ncrUnitAssignFk` int(11) DEFAULT NULL,`removedUnitFk`
	// int(11) DEFAULT NULL,`fittedUnitFk` int(11) DEFAULT NULL,`replacedBy`
	// int(11) DEFAULT NULL,`replacedDate` datetime DEFAULT NULL,`createdBy`
	// int(11) DEFAULT NULL,`createdDate` datetime DEFAULT NULL,`lastUpdated`
	// timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE
	// CURRENT_TIMESTAMP,PRIMARY KEY (`pk`)) ",
	// null);
	// for (Integer pk : ncrUnitAllocPks)
	// {
	// int ncrFk = PersistWrapper.read(Integer.class,
	// "select ncr_unit_alloc.ncrFk from ncr_unit_alloc where pk=?", pk);
	// Integer ncrUnitAssignFk;
	// Integer removedUnitFk = PersistWrapper.read(Integer.class,
	// "select ncr_unit_alloc.unitFk from ncr_unit_alloc where pk=?", pk);
	// Integer fittedUnitFk = PersistWrapper.read(Integer.class,
	// "select ncr_unit_alloc.replacedUnitFk from ncr_unit_alloc where pk=?",
	// pk);
	// Integer replacedBy = PersistWrapper.read(Integer.class,
	// "select ncr_unit_alloc.replacedBy from ncr_unit_alloc where pk=?", pk);
	// Date replacedDate = PersistWrapper.read(Date.class,
	// "select ncr_unit_alloc.replacedDate from ncr_unit_alloc where pk=?", pk);
	// Integer createdBy = PersistWrapper.read(Integer.class,
	// "select ncr_unit_alloc.createdBy from ncr_unit_alloc where pk=?", pk);
	// Date createdDate = PersistWrapper.read(Date.class,
	// "select ncr_unit_alloc.createdDate from ncr_unit_alloc where pk=?", pk);
	//
	// UnitObj unit = null;
	// if (removedUnitFk != null)
	// {
	// unit = ProjectDelegate.getUnitByPk(removedUnitFk);
	// } else if (fittedUnitFk != null)
	// {
	// unit = ProjectDelegate.getUnitByPk(fittedUnitFk);
	// }
	// if (unit != null)
	// {
	// StringBuffer sqlQuery = new StringBuffer("SELECT * FROM
	// ncr_unit_assign");
	// sqlQuery.append(" where ncrFk=" + ncrFk);
	// if (unit.getParentPk() == null)
	// {
	// sqlQuery.append(" and unitFk is null");
	// } else
	// {
	// sqlQuery.append(" and unitFk = " + unit.getParentPk());
	// }
	// NcrUnitAssignBean ncrUnitAssignBean =
	// PersistWrapper.read(NcrUnitAssignBean.class,
	// sqlQuery.toString(), null);
	// PersistWrapper.executeUpdate(
	// "INSERT INTO `ncr_unit_alloc_temp` ( `ncrUnitAssignFk`, `removedUnitFk`,
	// `fittedUnitFk`, `replacedBy`, `replacedDate`, `createdBy`,`createdDate`)
	// VALUES (?,?,?,?,?,?,? )",
	// ncrUnitAssignBean.getPk(), removedUnitFk, fittedUnitFk, replacedBy,
	// replacedDate, createdBy,
	// createdDate);
	// }
	//
	// }
	//
	// PersistWrapper.executeUpdate("RENAME TABLE ncr_unit_alloc TO
	// ncr_unit_alloc_bkp", null);
	// PersistWrapper.executeUpdate("RENAME TABLE ncr_unit_alloc_temp TO
	// ncr_unit_alloc", null);
	//
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public static NcrWhereFoundBean getNcrWhereFoundBeanByPk(int pk)
	{
		try
		{
			NcrWhereFoundList ncrWhereFoundList = PersistWrapper.readByPrimaryKey(NcrWhereFoundList.class, pk);
			return NcrBeanHelper.getNcrWhereFoundBean(ncrWhereFoundList);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static List<UserOID> getNcrFunctionNotifiersList(int ncrFunctionFk)
	{
		List<User> userList = null;
		List<UserOID> ncrFunctionNotifiersList = new ArrayList<UserOID>();
		try
		{
			userList = PersistWrapper.readList(User.class,
					"select u.* from TAB_USER u inner join ncr_function_notifiers nfn on nfn.userFk = u.pk where nfn.ncrFunctionFk =?",
					ncrFunctionFk);
			if (userList != null)
			{
				for (Iterator iterator = userList.iterator(); iterator.hasNext();)
				{
					User user = (User) iterator.next();
					ncrFunctionNotifiersList.add(user.getOID());
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrFunctionNotifiersList;
	}

	private static void deleteNcrFunctionNotifiers(UserOID userOID, int functionFk)
	{
		try
		{
			PersistWrapper.delete("delete from ncr_function_notifiers where ncrFunctionFk=? and userFk=?", functionFk,
					userOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void deleteNcrFunctionNotifiersByncrFunctionFk(int ncrFunctionFk)
	{
		try
		{
			PersistWrapper.delete("delete from ncr_function_notifiers where ncrFunctionFk=?", ncrFunctionFk);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static NcrFunctionNotifiers getNcrFunctionNotifiersItem(int ncrFunctionFk, int userFk)
	{
		NcrFunctionNotifiers ncrFunctionNotifiersList = null;

		try
		{
			ncrFunctionNotifiersList = PersistWrapper.read(NcrFunctionNotifiers.class,
					"select * from ncr_function_notifiers where ncrFunctionFk =? and userFk=?", ncrFunctionFk, userFk);
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return ncrFunctionNotifiersList;
	}

	public static List<NcrMilestoneQuery> getNcrMilestoneQuery(UserContext context,
			NcrItemQueryFilter ncrItemQueryFilter)
	{
		List<NcrMilestoneQuery> ncrMilestoneQuery = null;
		try
		{
			NcrMileStoneQueryBuilder nqBuilder = new NcrMileStoneQueryBuilder(context, ncrItemQueryFilter);
			QueryObject result = nqBuilder.getQuery();
			PersistWrapper p = new PersistWrapper();
			ncrMilestoneQuery = p.readList(NcrMilestoneQuery.class, result.getQuery(),
					(result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
							: null);

			// if (ncrMilestoneQuery != null)
			// {
			// for (NcrMilestoneQuery query : ncrMilestoneQuery)
			// {
			// query.setNcrUnitAssignBeans(getNcrUnitAssign(new
			// NcrItemOID(query.getPk(),
			// "")));
			// }
			// }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ncrMilestoneQuery;
	}

	public static NcrDispositionBean getNcrDispositionBean(int pk)
	{
		String queryString = " select * from ncr_disposition_master where 1 = 1 and pk=?";
		NcrDispositionTypes aDisp = PersistWrapper.read(NcrDispositionTypes.class, queryString, pk);
		if (aDisp != null)
			return NcrDispositionBean.getBean(aDisp);
		return null;
	}

	public static List<NcrDispositionBean> getNcrDispositionBean()
	{
		String queryString = " select * from ncr_disposition_master where 1 = 1 ";
		List<NcrDispositionTypes> list = PersistWrapper.readList(NcrDispositionTypes.class, queryString);
		if (list != null)
		{
			List<NcrDispositionBean> dispositionBeans = new ArrayList<NcrDispositionBean>();
			for (NcrDispositionTypes aDisp : list)
			{
				dispositionBeans.add(NcrDispositionBean.getBean(aDisp));
			}
			return dispositionBeans;
		}
		return null;
	}

	public static List<NcrFunctionBean> getNcrFunctions()
	{
		String queryString = " select ncr_function_master.pk as functionMasterFk,ncr_function_master.name as functionName from ncr_function_master where 1 = 1 ";
		return PersistWrapper.readList(NcrFunctionBean.class, queryString);
	}

	public static List<NcrFunctionBean> getNcrFunctionsByProject(ProjectOID projectOID)
	{
		String queryString = " select ncr_function_master.pk as functionMasterFk,ncr_function_master.name as functionName"
				+ " from ncr join ncr_group ncrGroup on ncr.ncrGroupFk = ncrGroup.pk "
				+ " left join  ncr_function functionT on  functionT.objectFk = ncr.pk and functionT.objectType ='"
				+ EntityTypeEnum.NCR.name() + "' and functionT.isCurrent=true"
				+ " left join ncr_function_master on functionT.functionMasterFk =ncr_function_master.pk where 1= 1 and ncrGroup.projectFk =? group by ncr_function_master.pk";
		return PersistWrapper.readList(NcrFunctionBean.class, queryString, projectOID.getPk());
	}

	public static List<NcrGroupBean> getNcrGroupBeanList(SupplierOID supplierOID, SiteOID siteOID,
			PurchaseOrderOID purchaseOrderOID) throws Exception
	{
		List<NcrGroupBean> ncrGroupBeanList = new ArrayList<>();
		List<NcrGroup> ncrGroupItemList = PersistWrapper.readList(NcrGroup.class,
				"select * from ncr_group where supplierFk = ? and siteFk = ? and purchaseOrderEntryFk = ? and eStatus=?",
				supplierOID.getPk(), siteOID.getPk(), purchaseOrderOID.getPk(), EStatusEnum.Active.getValue());
		for (Iterator<NcrGroup> iterator = ncrGroupItemList.iterator(); iterator.hasNext();)
		{
			NcrGroup ncrGroupItem = iterator.next();
			NcrGroupBean ncrGroupBean = NcrBeanHelper.getBean(ncrGroupItem);
			ncrGroupBeanList.add(ncrGroupBean);
		}

		return ncrGroupBeanList;
	}

	public static List<NcrGroupBean> getNcrGroupBeanList(PurchaseOrderOID purchaseOrderOID) throws Exception
	{
		List<NcrGroupBean> ncrGroupBeanList = new ArrayList<>();
		List<NcrGroup> ncrGroupItemList = PersistWrapper.readList(NcrGroup.class,
				"select * from ncr_group where purchaseOrderEntryFk = ? and eStatus=?", purchaseOrderOID.getPk(),
				EStatusEnum.Active.getValue());
		for (Iterator<NcrGroup> iterator = ncrGroupItemList.iterator(); iterator.hasNext();)
		{
			NcrGroup ncrGroupItem = iterator.next();
			NcrGroupBean ncrGroupBean = NcrBeanHelper.getBean(ncrGroupItem);
			ncrGroupBeanList.add(ncrGroupBean);
		}

		return ncrGroupBeanList;
	}

	public static List<NcrGroupBean> getNcrGroupBeanList(PurchaseOrderLineItemOID purchaseOrderLineItemOID)
			throws Exception
	{
		List<NcrGroupBean> ncrGroupBeanList = new ArrayList<>();
		List<NcrGroup> ncrGroupItemList = PersistWrapper.readList(NcrGroup.class,
				"select * from ncr_group where ncrPurchaseOrderLineItemFk = ? ", purchaseOrderLineItemOID.getPk());
		for (Iterator<NcrGroup> iterator = ncrGroupItemList.iterator(); iterator.hasNext();)
		{
			NcrGroup ncrGroupItem = iterator.next();
			NcrGroupBean ncrGroupBean = NcrBeanHelper.getBean(ncrGroupItem);
			ncrGroupBeanList.add(ncrGroupBean);
		}

		return ncrGroupBeanList;
	}

	public static List<ResourceRequirementBean> getResourceRequirementList()
	{
		// String queryString = " select * from resource_requirement where 1 = 1
		// ";
		String queryString = "select * from resource_requirement where 1 = 1 group by resourceName order by resourceName asc";
		return PersistWrapper.readList(ResourceRequirementBean.class, queryString);
	}

	public static float getScarpedNcredQty(PartOID partOID, PurchaseOrderLineItemOID purchaseOrderLineItemOID)
	{
		if (partOID == null || purchaseOrderLineItemOID == null)
			return 0f;
		Float totalQuantity = PersistWrapper.read(Float.class,
				" select sum(ncr.quantity) from ncr inner join ncr_group  on (ncr.ncrGroupFk = ncr_group.pk) where partFk =? and ncrPurchaseOrderLineItemFk =?"
						+ " and ncr.ncrStatus in ('" + NcrEnum.NcrItemStatus.APPROVED.toString() + "','"
						+ NcrEnum.NcrItemStatus.COMPLETED.toString() + "','" + NcrEnum.NcrItemStatus.CLOSED.toString()
						+ "')" + " and ncr.dispositionFk  in ( " + SCRAP_DISPOSITIONFK + ","
						+ RETURN_TO_SUPPLIER_DISPOSITIONFK + " )",
				partOID.getPk(), purchaseOrderLineItemOID.getPk());
		return (totalQuantity != null) ? totalQuantity : 0f;
	}

	public static Float getScarpedNcredQty(PartOID partOID, int shipmentRecFk,
			PurchaseOrderLineItemOID purchaseOrderLineItemOID)
	{
		Float totalQuantity = PersistWrapper.read(Float.class,
				" select sum(ncr.quantity) from ncr inner join ncr_group  on (ncr.ncrGroupFk = ncr_group.pk) where partFk =? and ncrPurchaseOrderLineItemFk =? and shipmentReceiveEntryFk =?"
						+ " and ncr.ncrStatus in ('" + NcrEnum.NcrItemStatus.APPROVED.toString() + "','"
						+ NcrEnum.NcrItemStatus.COMPLETED.toString() + "','" + NcrEnum.NcrItemStatus.CLOSED.toString()
						+ "')" + " and ncr.dispositionFk  in ( " + SCRAP_DISPOSITIONFK + ","
						+ RETURN_TO_SUPPLIER_DISPOSITIONFK + " )",
				partOID.getPk(), purchaseOrderLineItemOID.getPk(), shipmentRecFk);
		return totalQuantity;
	}

	public static NcrCorrectiveStatusActions getCurrentNcrCorrectiveAction(int objectPk,
			NcrEnum.NcrCorrectiveStatusEnum actionName)
	{
		NcrCorrectiveStatusActions action = PersistWrapper.read(NcrCorrectiveStatusActions.class,
				"select * from ncr_corrective_status_actions where ncrCorrectiveActionPk=? and actionName=? and isCurrent=1",
				objectPk, actionName.toString());
		return action;
	}

	// Corrective Action
	public static NcrCorrectiveActionBean saveNcrCorrectiveAction(UserContext context,
			NcrCorrectiveActionBean ncrCorrectiveActionBean) throws Exception
	{
		NcrCorrectiveAction ncrCorrectiveAction = null;
		if (ncrCorrectiveActionBean.getPk() != 0)
		{
			ncrCorrectiveAction = PersistWrapper.readByPrimaryKey(NcrCorrectiveAction.class,
					ncrCorrectiveActionBean.getPk());
		} else
		{
			ncrCorrectiveAction = new NcrCorrectiveAction();
			ncrCorrectiveAction.setCreatedBy(context.getUser().getPk());
			ncrCorrectiveAction.setCreatedDate(new Date());
		}
		ncrCorrectiveAction.setNcrFk(ncrCorrectiveActionBean.getNcrFk());
		int itemPk = 0;
		if (ncrCorrectiveAction.getPk() != 0)
		{
			PersistWrapper.update(context, ncrCorrectiveAction);
			itemPk = ncrCorrectiveAction.getPk();
		} else
		{
			itemPk = PersistWrapper.createEntity(ncrCorrectiveAction);
		}
		if (correctiveActionUpdateRequired(ncrCorrectiveActionBean))
		{
			// to change the previous informations as histary
			if (ncrCorrectiveActionBean.getPk() > 0)
			{
				NcrCorrectiveActionBean bean = getCorrectiveActionBeanByPk(ncrCorrectiveActionBean.getPk());
				bean.setEffectiveTo(new Date(new Date().getTime() - 1000));
				saveNcrCorrectiveActionH(context, bean);
			}
			// add the new entry
			ncrCorrectiveActionBean.setPk(itemPk);
			ncrCorrectiveActionBean.setNcrCorrectiveActionHFk(0);
			ncrCorrectiveActionBean.setEffectiveFrom(new Date(new Date().getTime()));
			ncrCorrectiveActionBean.setEffectiveTo(DateUtils.getMaxDate());
		}

		saveNcrCorrectiveActionH(context, ncrCorrectiveActionBean);
		NcrCorrectiveActionBean bean = getCorrectiveActionBeanByPk(itemPk);

		return bean;
	}

	public static NcrCorrectiveActionBean getCorrectiveActionBeanByPk(int pk) throws Exception
	{
		NcrCorrectiveActionBean bean = null;
		NcrCorrectiveAction ncrCorrectiveAction = PersistWrapper.readByPrimaryKey(NcrCorrectiveAction.class, pk);
		if (ncrCorrectiveAction != null)
		{
			NcrCorrectiveActionH ncrCorrectiveActionH = PersistWrapper.read(NcrCorrectiveActionH.class,
					"select * from ncr_corrective_action_h where ncr_corrective_action_h.ncrCorrectiveActionPk=? and ? BETWEEN ncr_corrective_action_h.effectiveFrom and ncr_corrective_action_h.effectiveTo order by ncr_corrective_action_h.pk DESC",
					ncrCorrectiveAction.getPk(), new Date());
			if (ncrCorrectiveActionH != null && ncrCorrectiveActionH.getPk() > 0)
			{
				bean = NcrBeanHelper.getCorrectiveActionBean(ncrCorrectiveAction, ncrCorrectiveActionH);
			}
		}
		return bean;
	}

	private static boolean correctiveActionUpdateRequired(NcrCorrectiveActionBean ncrCorrectiveActionBean)
	{
		if (ncrCorrectiveActionBean.getPk() == 0)
			return true;
		NcrCorrectiveActionH ncrH = PersistWrapper.readByPrimaryKey(NcrCorrectiveActionH.class,
				ncrCorrectiveActionBean.getNcrCorrectiveActionHFk());
		if ((ncrH.getCorrectiveAction() != null && ncrCorrectiveActionBean.getCorrectiveAction() == null)
				|| (ncrH.getCorrectiveAction() == null && ncrCorrectiveActionBean.getCorrectiveAction() != null)
				|| (ncrH.getCorrectiveAction() != null && ncrCorrectiveActionBean.getCorrectiveAction() != null && !ncrH
						.getCorrectiveAction().trim().equals(ncrCorrectiveActionBean.getCorrectiveAction().trim())))
		{
			return true;
		}
		if ((ncrH.getStartForecastDate() != null && ncrCorrectiveActionBean.getStartforecastDate() == null)
				|| (ncrH.getStartForecastDate() == null && ncrCorrectiveActionBean.getStartforecastDate() != null)
				|| (ncrH.getStartForecastDate() != null && ncrCorrectiveActionBean.getStartforecastDate() != null
						&& !ncrH.getStartForecastDate().equals(ncrCorrectiveActionBean.getStartforecastDate())))
		{
			return true;
		}

		if ((ncrH.getCompletionForecastDate() != null && ncrCorrectiveActionBean.getCompletionforecastDate() == null)
				|| (ncrH.getCompletionForecastDate() == null
						&& ncrCorrectiveActionBean.getCompletionforecastDate() != null)
				|| (ncrH.getCompletionForecastDate() != null
						&& ncrCorrectiveActionBean.getCompletionforecastDate() != null
						&& !ncrH.getCompletionForecastDate()
								.equals(ncrCorrectiveActionBean.getCompletionforecastDate())))
		{
			return true;
		}

		if ((ncrH.getCustodianPk() != null && ncrCorrectiveActionBean.getCustodianOID() == null)
				|| (ncrH.getCustodianPk() == null && ncrCorrectiveActionBean.getCustodianOID() != null)
				|| (ncrH.getCustodianPk() != null && ncrCorrectiveActionBean.getCustodianOID() != null
						&& !ncrH.getCustodianPk().equals(ncrCorrectiveActionBean.getCustodianOID().getPk())))
		{
			return true;
		}

		if ((ncrH.getD8stageFk() != null && ncrCorrectiveActionBean.getD8Stage() == null)
				|| (ncrH.getD8stageFk() != null
						&& !ncrH.getD8stageFk().equals(ncrCorrectiveActionBean.getD8Stage().getPk())))
		{
			return true;
		}

		return false;
	}

	private static void saveNcrCorrectiveActionH(UserContext context, NcrCorrectiveActionBean ncrcorrectiveActionBean)
			throws Exception
	{

		NcrCorrectiveActionH ncrCorrectiveActionH = null;
		if (ncrcorrectiveActionBean.getNcrCorrectiveActionHFk() != 0)
		{
			ncrCorrectiveActionH = PersistWrapper.readByPrimaryKey(NcrCorrectiveActionH.class,
					ncrcorrectiveActionBean.getNcrCorrectiveActionHFk());
		} else
		{
			ncrCorrectiveActionH = new NcrCorrectiveActionH();
			ncrCorrectiveActionH.setCreatedBy(context.getUser().getPk());
			ncrCorrectiveActionH.setCreatedDate(new Date());
		}
		if (ncrcorrectiveActionBean.getD8Stage() != null)
		{
			ncrCorrectiveActionH.setD8stageFk(ncrcorrectiveActionBean.getD8Stage().getPk());
		}
		ncrCorrectiveActionH.setNcrCorrectiveActionPk(ncrcorrectiveActionBean.getPk());
		ncrCorrectiveActionH.setCorrectiveAction(ncrcorrectiveActionBean.getCorrectiveAction());
		ncrCorrectiveActionH.setStartForecastDate(ncrcorrectiveActionBean.getStartforecastDate());
		ncrCorrectiveActionH.setCompletionForecastDate(ncrcorrectiveActionBean.getCompletionforecastDate());

		ncrCorrectiveActionH.setCustodianPk(ncrcorrectiveActionBean.getCustodianOID().getPk());

		ncrCorrectiveActionH.setEffectiveFrom(ncrcorrectiveActionBean.getEffectiveFrom());
		ncrCorrectiveActionH.setEffectiveTo(ncrcorrectiveActionBean.getEffectiveTo());
		int itemPk;
		if (ncrCorrectiveActionH.getPk() != 0)
		{
			PersistWrapper.update(context, ncrCorrectiveActionH);
			itemPk = ncrCorrectiveActionH.getPk();
		} else
		{
			itemPk = PersistWrapper.createEntity(ncrCorrectiveActionH);
		}

	}

	private static void deleteNcrCorrectiveAction(UserContext context, int pk)
	{
		try
		{
			NcrCorrectiveActionBean bean = getCorrectiveActionBeanByPk(pk);
			if (bean != null)
			{
				bean.setEffectiveTo(new Date(new Date().getTime() - 1000));
				saveNcrCorrectiveActionH(context, bean);
			}

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static NcrCorrectiveActionBean correctiveCompleted(UserContext context, int correctiveActionPk,
			String comment) throws Exception
	{
		NcrCorrectiveActionBean currentBean = getCorrectiveActionBeanByPk(correctiveActionPk);
		if (currentBean.getStatus() == null
				|| !currentBean.getStatus().equals(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVECOMPLETED.toString()))
		{
			if (correctiveActionPk > 0)
			{
				Integer iterateAction = PersistWrapper.read(Integer.class,
						"select max(iterator) from ncr_corrective_status_actions where ncrCorrectiveActionPk=? ",
						correctiveActionPk);

				NcrCorrectiveStatusActions ncrAcrtions = new NcrCorrectiveStatusActions();
				ncrAcrtions.setNcrCorrectiveActionPk(correctiveActionPk);
				ncrAcrtions.setActionName(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVECOMPLETED.toString());
				ncrAcrtions.setActionBy(context.getUser().getPk());
				ncrAcrtions.setActionDate(new Date());
				ncrAcrtions.setActionComment(comment);
				if (iterateAction != null)
				{
					ncrAcrtions.setIterator(iterateAction);
				} else
				{
					ncrAcrtions.setIterator(1);
				}
				ncrAcrtions.setIsCurrent(1);

				int pk = PersistWrapper.createEntity(ncrAcrtions);
				if (pk != 0)
				{
					NcrCorrectiveAction ncaItem = PersistWrapper.readByPrimaryKey(NcrCorrectiveAction.class,
							correctiveActionPk);
					ncaItem.setStatus(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVECOMPLETED.toString());
					PersistWrapper.update(context, ncaItem);
					Comment newComment = ProjectDelegate.addComment(EtestApplication.getInstance().getUserContext(),
							currentBean.getPk(), EntityTypeEnum.NcrCorrectiveAction,
							"Completed:- " + ncrAcrtions.getActionComment(), Mode.COMMENTCONTEXT_GENERAL);
				}

			}
			NcrCorrectiveActionBean ncrCorrectiveActionBean = getCorrectiveActionBeanByPk(correctiveActionPk);

			NcrItemBean ncrItemBean = getNcrItemBean(new NcrItemOID(ncrCorrectiveActionBean.getNcrFk()));
			if (ncrCorrectiveActionBean.getCorrectiveCompletedBy() != null)
			{
				NcrEmailSender.notifyRecommendedActionComplete(context, ncrItemBean, ncrCorrectiveActionBean);
			}
			return getCorrectiveActionBeanByPk(correctiveActionPk);
		} else
		{
			return currentBean;
		}

	}

	public static NcrCorrectiveActionBean correctiveReOpen(UserContext context, int correctiveActionPk, String comment)
			throws Exception
	{

		NcrCorrectiveActionBean currentBean = getCorrectiveActionBeanByPk(correctiveActionPk);
		if (currentBean.getStatus() == null
				|| !currentBean.getStatus().equals(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVEREOPEN.toString()))
		{
			if (correctiveActionPk > 0)
			{
				Integer iterateAction = PersistWrapper.read(Integer.class,
						"select max(iterator) from ncr_corrective_status_actions where ncrCorrectiveActionPk=? ",
						correctiveActionPk);
				PersistWrapper.executeUpdate(
						"update ncr_corrective_status_actions set isCurrent=0 where ncrCorrectiveActionPk=?",
						correctiveActionPk);
				NcrCorrectiveStatusActions ncrAcrtions = new NcrCorrectiveStatusActions();
				ncrAcrtions.setNcrCorrectiveActionPk(correctiveActionPk);
				ncrAcrtions.setActionName(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVEREOPEN.toString());
				ncrAcrtions.setActionBy(context.getUser().getPk());
				ncrAcrtions.setActionDate(new Date());
				ncrAcrtions.setActionComment(comment);
				if (iterateAction != null)
				{
					ncrAcrtions.setIterator(++iterateAction);
				} else
				{
					ncrAcrtions.setIterator(1);
				}
				ncrAcrtions.setIsCurrent(1);
				int pk = PersistWrapper.createEntity(ncrAcrtions);
				if (pk != 0)
				{

					NcrCorrectiveAction ncaItem = PersistWrapper.readByPrimaryKey(NcrCorrectiveAction.class,
							correctiveActionPk);
					ncaItem.setStatus(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVEREOPEN.toString());
					PersistWrapper.update(context, ncaItem);
					Comment newComment = ProjectDelegate.addComment(EtestApplication.getInstance().getUserContext(),
							currentBean.getPk(), EntityTypeEnum.NcrCorrectiveAction,
							"Reopened:- " + ncrAcrtions.getActionComment(), Mode.COMMENTCONTEXT_GENERAL);

				}
			}
			NcrCorrectiveActionBean ncrCorrectiveActionBean = getCorrectiveActionBeanByPk(correctiveActionPk);

			NcrItemBean ncrItemBean = getNcrItemBean(new NcrItemOID(ncrCorrectiveActionBean.getNcrFk()));
			if (ncrCorrectiveActionBean.getCorrectiveReviewedBy() != null)
			{
				NcrEmailSender.notifyRecommendedActionReOpen(context, ncrItemBean, ncrCorrectiveActionBean);
			}
			return getCorrectiveActionBeanByPk(correctiveActionPk);
		} else
		{
			return currentBean;
		}

	}

	public static List<NcrCorrectiveActionQuery> getMyActiveNcrCorrectiveAction(UserContext context) throws Exception
	{
		NcrCorrectiveActionReportFilter filter = new NcrCorrectiveActionReportFilter();
		List<UserOID> users = new ArrayList<UserOID>();
		users.add(context.getUser().getOID());
		filter.setActiveActionsCompletionWaitingFor(users);
		ReportRequest req = new ReportRequest(ReportTypes.NcrCorrectionActionListReport);
		req.setFetchAllRows(true);
		req.setFilter(filter);
		ReportResponse resp = ReportsDelegate.runReport(context, req);
		if (resp == null || resp.getReportData() == null)
			return null;
		return (List<NcrCorrectiveActionQuery>) resp.getReportData();
	}

	public static List<NcrCorrectiveActionQuery> getMyOverduedNcrCorrectiveAction(UserContext context) throws Exception
	{
		NcrCorrectiveActionReportFilter filter = new NcrCorrectiveActionReportFilter();
		List<UserOID> users = new ArrayList<UserOID>();
		users.add(context.getUser().getOID());
		filter.setOverduedActionsCompletionWaitingFor(users);
		ReportRequest req = new ReportRequest(ReportTypes.NcrCorrectionActionListReport);
		req.setFetchAllRows(true);
		req.setFilter(filter);
		ReportResponse resp = ReportsDelegate.runReport(context, req);
		if (resp == null || resp.getReportData() == null)
			return null;
		return (List<NcrCorrectiveActionQuery>) resp.getReportData();
	}

	public static List<NcrCorrectiveActionQuery> getNcrCorrectiveAction(UserContext context,
			NcrCorrectiveActionReportFilter filter) throws Exception
	{
		if (filter == null)
			filter = new NcrCorrectiveActionReportFilter();
		ReportRequest req = new ReportRequest(ReportTypes.NcrCorrectionActionListReport);
		req.setFetchAllRows(true);
		req.setFilter(filter);
		ReportResponse resp = ReportsDelegate.runReport(context, req);
		if (resp == null || resp.getReportData() == null)
			return null;

		return (List<NcrCorrectiveActionQuery>) resp.getReportData();
	}

	public static List<NcrCorrectiveActionBean> getNcrCorrectiveAction(NcrItemOID ncrItemOID) throws Exception
	{
		List<NcrCorrectiveActionBean> resultBean = new ArrayList<NcrCorrectiveActionBean>();
		List<NcrCorrectiveAction> ncrCorrectiveActionList = PersistWrapper.readList(NcrCorrectiveAction.class,
				"SELECT ncr_corrective_action.* FROM ncr_corrective_action inner join ncr_corrective_action_h on ncr_corrective_action_h.ncrCorrectiveActionPk=ncr_corrective_action.pk and ? BETWEEN ncr_corrective_action_h.effectiveFrom and ncr_corrective_action_h.effectiveTo  where ncrFk=? order by ncr_corrective_action_h.d8stageFk ASC,ncr_corrective_action_h.createdDate ASC",
				new Date(), ncrItemOID.getPk());
		for (NcrCorrectiveAction action : ncrCorrectiveActionList)
		{
			NcrCorrectiveActionH ncrCorrectiveActionH = PersistWrapper.read(NcrCorrectiveActionH.class,
					"select * from ncr_corrective_action_h where ncr_corrective_action_h.ncrCorrectiveActionPk=? and ? BETWEEN ncr_corrective_action_h.effectiveFrom and ncr_corrective_action_h.effectiveTo order by ncr_corrective_action_h.pk DESC",
					action.getPk(), new Date());
			if (ncrCorrectiveActionH != null && ncrCorrectiveActionH.getPk() > 0)
			{
				resultBean.add(NcrBeanHelper.getCorrectiveActionBean(action, ncrCorrectiveActionH));
			}
		}
		return resultBean;
	}

	public static List<NcrCorrectiveActionH> getNcrCorrectiveActionHistoryByncrCorrectiveActionPk(
			int ncrCorrectiveActionPk) throws Exception
	{

		return PersistWrapper.readList(NcrCorrectiveActionH.class,
				"select * from ncr_corrective_action_h where ncr_corrective_action_h.ncrCorrectiveActionPk=? order by ncr_corrective_action_h.pk DESC",
				ncrCorrectiveActionPk);
	}

	public static List<NcrFunctionMaster> getNcrFunctionMasters(NcrFunctionMaster.TYPE type)
	{
		if (NcrFunctionMaster.TYPE.ALL.equals(type) || type == null)
		{
			return PersistWrapper.readList(NcrFunctionMaster.class,
					"SELECT `ncr_function_master`.`pk`,`ncr_function_master`.`name`,`ncr_function_master`.`description`,`ncr_function_master`.`special`,`ncr_function_master`.`estatus`,`ncr_function_master`.`createdDate`,`ncr_function_master`.`createdBy`,`ncr_function_master`.`lastUpdated` FROM `ncr_function_master`");
		} else
		{
			boolean special = false;
			if (NcrFunctionMaster.TYPE.SPECIAL.equals(type))
			{
				special = true;
			}
			return PersistWrapper.readList(NcrFunctionMaster.class,
					"SELECT `ncr_function_master`.`pk`,`ncr_function_master`.`name`,`ncr_function_master`.`description`,`ncr_function_master`.`special`,`ncr_function_master`.`estatus`,`ncr_function_master`.`createdDate`,`ncr_function_master`.`createdBy`,`ncr_function_master`.`lastUpdated` FROM `ncr_function_master` where special=?",
					special);

		}
	}

	public static NcrFunctionMaster getNcrFunctionMaster(int pk) throws Exception
	{
		return PersistWrapper.readByPrimaryKey(NcrFunctionMaster.class, pk);
	}*/

}