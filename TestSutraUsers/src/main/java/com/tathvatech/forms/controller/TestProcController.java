package com.tathvatech.forms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.testsutra.ncr.common.NcrEnum;
import com.tathvatech.testsutra.ncr.common.NcrGroupBean;
import com.tathvatech.testsutra.ncr.common.NcrItemBean;
import com.tathvatech.testsutra.ncr.common.NcrItemBeanTransfer;
import com.tathvatech.testsutra.ncr.common.NcrUnitAssignBean;
import com.tathvatech.testsutra.ncr.web.NcrGroupForm;
import com.tathvatech.testsutra.ncr.web.NcrGroupForm.NcrGroupViewMode;
import com.tathvatech.testsutra.openitemv2.service.OILDelegate;
import com.tathvatech.testsutra.parts.service.PartsDelegate;
import com.tathvatech.ts.core.common.Attachment;
import com.tathvatech.ts.core.common.FileStoreManager;
import com.tathvatech.ts.core.common.utils.AttachmentIntf;
import com.tathvatech.ts.core.part.Part;
import com.tathvatech.ts.core.project.TestProcObj;
import com.tathvatech.ts.core.project.UnitOID;
import com.tathvatech.ts.core.project.UnitObj;
import com.tathvatech.ts.core.project.UnitQuery;
import com.tathvatech.ts.core.survey.response.FormItemResponse;
import com.tathvatech.ts.core.survey.response.ResponseUnit;
import com.tathvatech.ts.core.survey.response.SurveyItemResponse;
import com.tathvatech.ts.core.survey.response.TestItemOILTransferQuery;
import com.thirdi.surveyside.project.Project;
import com.thirdi.surveyside.project.ProjectManager;
import com.thirdi.surveyside.survey.SurveyItem;
import com.thirdi.surveyside.survey.response.SurveyResponseManager;
import com.thirdi.surveyside.survey.surveyitem.AdvancedBomInspectItemAnswerType;
import com.thirdi.surveyside.survey.surveyitem.BomInspectItemAnswerType;

public class TestProcController {
	
	TestProcObj testProc;
	HashMap<String, FormItemResponse> formItemResponseMap;
	HashMap<String, List<TestItemOILTransferQuery>> oilTransferMap = new HashMap<String, List<TestItemOILTransferQuery>>();
	
	ResponseDetailForm responseDetailForm;
	
	HashMap<SurveyItem, FormItemResponse> selectedItems = new HashMap<SurveyItem, FormItemResponse>();
	
	
	public TestProcController(ResponseDetailForm responseDetailForm, TestProcObj testProc) 
	{
		this.responseDetailForm = responseDetailForm;
		this.testProc = testProc;
	}

	public ResponseDetailForm getResponseDetailForm()
	{
		return responseDetailForm;
	}

	public void setResponseDetailForm(ResponseDetailForm responseDetailForm)
	{
		this.responseDetailForm = responseDetailForm;
	}

	public void itemSelected(SurveyItem sItem, FormItemResponse formItemResponse)
	{
		selectedItems.put(sItem, formItemResponse);
	}

	public void itemDeSelected(SurveyItem sItem, FormItemResponse forItemResponse)
	{
		selectedItems.remove(sItem);
	}

	public void createNCROnSelected() 
	{
		UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		Project project = ProjectManager.getProject(testProc.getProjectPk());
		UnitQuery unitQuery = ProjectManager.getUnitQueryByPk(unit.getPk(), project.getOID());

		NcrGroupBean ncrGroupBean = new NcrGroupBean();
		Part part = PartsDelegate.getPart(unit.getPartPk());
		ncrGroupBean.setPartOID(part.getOID());
		
		ncrGroupBean.setProjectOID(project.getOID());
		
		List<NcrItemBean> beanList = new ArrayList<>();
		for (Iterator iterator = selectedItems.keySet().iterator(); iterator.hasNext();) 
		{
			SurveyItem aItem = (SurveyItem) iterator.next();
			FormItemResponse formItemResponse = selectedItems.get(aItem);
			
			NcrItemBeanTransfer ncrItemBean = new NcrItemBeanTransfer();
			ncrItemBean.setNcrStatus(NcrEnum.NcrItemStatus.DRAFT.toString());
			//ncrItemBean.setProjectOID(project.getOID());
			if(aItem instanceof AdvancedBomInspectItemAnswerType)
				ncrItemBean.setNcrDesc(((AdvancedBomInspectItemAnswerType)aItem).buildOpenItemTransferDescription());
			else if(aItem instanceof BomInspectItemAnswerType)
				ncrItemBean.setNcrDesc(((BomInspectItemAnswerType)aItem).buildOpenItemTransferDescription());
			
			ncrItemBean.setQuantity(1f);
			//ncrItemBean.setWhereFoundFk(NCR_WHEREFOUND_FOR_TRANSFER);
			
			
			ncrItemBean.setLocationPk(testProc.getWorkstationPk());
			ncrItemBean.setLocationType("Workstation");
			
			//Attachments
			if(aItem instanceof AdvancedBomInspectItemAnswerType || aItem instanceof BomInspectItemAnswerType)
			{
				List<AttachmentIntf> attachments = new ArrayList<>();
				
				String formImageFileName = aItem.getImageFileName();
				if(formImageFileName != null && formImageFileName.trim().length() > 0)
				{
					File attFile = FileStoreManager.getFile(formImageFileName);

					Attachment attachment = new Attachment();
					attachment.setFileName(attFile.getName());
					attachment.setFileDisplayName("Form image");
					attachments.add(attachment);
				}

				SurveyItemResponse itemResponse = SurveyResponseManager.getSurveyItemResponse(aItem, aItem.getSurveyItemId(), responseDetailForm.responseMaster.getResponseId());
				if(itemResponse != null)
				{
					List<String> responseImageFileNames = new ArrayList<String>();
					for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
					{
						ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
						if(AdvancedBomInspectItemAnswerType.imageKey == aUnit.getKey2())
						{
							responseImageFileNames.add(aUnit.getKey4());
						}
					}
					if(responseImageFileNames != null && responseImageFileNames.size() > 0)
					{
						File attFile = FileStoreManager.getFile(responseImageFileNames.get(0));

						Attachment attachment = new Attachment();
						attachment.setFileName(attFile.getName());
						attachment.setFileDisplayName("Form response attachment");
						attachments.add(attachment);
					}
				}
				
				if(attachments.size() > 0)
					ncrItemBean.setAttachments(attachments);
			}
			
			NcrUnitAssignBean unitQuantityBean = new NcrUnitAssignBean();
			unitQuantityBean.setUnitFk(unitQuery.getUnitPk());
			unitQuantityBean.setQuantity(1f);
			ncrItemBean.setNcrUnitAssignBeanList(Arrays.asList(new NcrUnitAssignBean[]{unitQuantityBean}));
			
			ncrItemBean.setFormItemResponse(selectedItems.get(aItem)); // add the transfer source
			
			beanList.add(ncrItemBean);
		}
		ncrGroupBean.setNcrItemBeanList(beanList);
		
		NcrGroupForm ncrGroupForm = new NcrGroupForm(ncrGroupBean, responseDetailForm, new ObjectEditListener() {
			
			@Override
			public void commited(Object object) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void cancelled() {
				// TODO Auto-generated method stub
				
			}
		}, NcrGroupViewMode.EDIT);		
		
		EtestApplication.getInstance().showPanel(ncrGroupForm);
		
	}

	public void setFormItemResponseMap(HashMap<String, FormItemResponse> formItemResponseMap)
	{
		this.formItemResponseMap = formItemResponseMap;
	}

	public HashMap<String, FormItemResponse> getFormItemResponseMap()
	{
		return formItemResponseMap;
	}

	public void setOilTransferListMap(HashMap<String, List<TestItemOILTransferQuery>> oilTransferMap)
	{
		this.oilTransferMap = oilTransferMap;
	}

	public HashMap<String, List<TestItemOILTransferQuery>> getOilTransferMap()
	{
		return oilTransferMap;
	}

	public void setOilTransferMap(HashMap<String, List<TestItemOILTransferQuery>> oilTransferMap)
	{
		this.oilTransferMap = oilTransferMap;
	}

	public void loadOILTransferMap(int responseId)
	{
		HashMap<String, List<TestItemOILTransferQuery>> oilTransferMap = OILDelegate.getTestProcItemFailureQueryMap(responseId);
		setOilTransferListMap(oilTransferMap);
	}
}
