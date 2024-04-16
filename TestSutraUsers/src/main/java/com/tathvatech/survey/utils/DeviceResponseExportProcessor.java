/*
 * Created on May 10, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.tathvatech.common.common.DataTypes;
import com.tathvatech.common.common.FileStoreManager;
import com.tathvatech.common.common.Option;
import com.tathvatech.common.enums.ResultEnum;
import com.tathvatech.forms.response.*;
import com.tathvatech.survey.common.*;
import com.tathvatech.survey.enums.BomCellTypeEnum;
import com.tathvatech.survey.enums.BomTypesEnum;
import com.tathvatech.survey.intf.OneDOptionType;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.unit.response.ResponseUnit;
import com.tathvatech.user.common.UserContext;
import org.apache.commons.codec.binary.Base64;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeviceResponseExportProcessor
{
	private static final Logger logger = LoggerFactory.getLogger(DeviceResponseExportProcessor.class);

    private SurveyDefinition sd;
    UserContext context;
    
    public void init(SurveyDefinition sd, UserContext context)
    {
    	this.sd = sd;
    	this.context = context;
    }
    /**
	 * @param
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
    public FormItemResponseBase getFormItemResponsesBean(SurveyItem sItem, SurveyItemResponse aItemResponse) throws Exception
    {
        if(sItem instanceof RadioButtonAnswerType)
        {
            return getResponseForMultiChoiceMultiAnswerType((OneDOptionType)sItem, aItemResponse);
        }
        else if(sItem instanceof TextBoxAnswerType)
        {
        	return getResponseForTextBoxAnswerType((TextBoxAnswerType)sItem, aItemResponse);
        }
        else if(sItem instanceof TextAreaAnswerType)
        {
        	return getResponseForTextAreaAnswerType((TextAreaAnswerType)sItem, aItemResponse);
        }
        else if(sItem instanceof BomInspectItemAnswerType)
        {
        	return getResponseForBomAnswerType((SurveySaveItem)sItem, aItemResponse);
        }
        else if( sItem instanceof AdvancedBomInspectItemAnswerType)
        {
        	return getResponseForAdvancedBomAnswerType((SurveySaveItem)sItem, aItemResponse);
        }
        else if(sItem instanceof SignatureCaptureAnswerType)
        {
        	return getResponseForSignatureCaptureAnswerType((SignatureCaptureAnswerType)sItem, aItemResponse);
        }
        return null;
    }

	private AdvancedBomInspectionItemResponseBean getResponseForAdvancedBomAnswerType(
			SurveySaveItem question, SurveyItemResponse itemResponse) throws Exception
    {
		if(itemResponse == null || itemResponse.getResponseUnits() == null || itemResponse.getResponseUnits().size() == 0)
			return null;
		
		AdvancedBomInspectItemAnswerType sItem = (AdvancedBomInspectItemAnswerType)question;
		AdvancedBomInspectionItemResponseBean bean = new AdvancedBomInspectionItemResponseBean();
		bean.setFormItemId(sItem.getSurveyItemId());

		for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
		{
			ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
			if (AdvancedBomInspectItemAnswerType.selectionKey == aUnit.getKey2())
			{
				int selectedValue = aUnit.getKey3();
				if(selectedValue == AdvancedBomInspectItemAnswerType.passVal)
					bean.setResult(ResultEnum.pass.value());
				else if(selectedValue == AdvancedBomInspectItemAnswerType.failVal)
					bean.setResult(ResultEnum.fail.value());
				else if(selectedValue == AdvancedBomInspectItemAnswerType.naVal)
					bean.setResult(ResultEnum.notApplicable.value());
			}
			else if (AdvancedBomInspectItemAnswerType.commentsKey == aUnit.getKey2() && aUnit.getKey4() != null)
			{
				String canswer = aUnit.getKey4();
				if(canswer != null && canswer.trim().length() > 0)
				{
					bean.setComment(canswer);
				}
			}
			else if (AdvancedBomInspectItemAnswerType.imageKey == aUnit.getKey2())
			{
				bean.setImageFileName(aUnit.getKey4());
				bean.setImageFileDisplayName(aUnit.getKey4());
			}
			else if (AdvancedBomInspectItemAnswerType.equipmentSelectorKey == aUnit.getKey2() && aUnit.getKey4() != null)
			{
				try
				{
					bean.setEquipmentFk(Integer.parseInt(aUnit.getKey4()));
				}
				catch (Exception e)
				{
					logger.error("Invalid equipmentFk in Inspection Response:" + aUnit.getKey4());
				}
			}
			
			//now the additional table fields
			else
			{
				int ind = aUnit.getKey1();
				Option aOption = (Option) sItem.getCustomFields().getOptionByValue(ind);
				
				if(aOption == null)
				{
					logger.error("Invalid aUnit.key1 value kwy1:" + aUnit.getKey1());
					continue;
				}
				
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.RadioGroup.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.Option);
					aCell.setValue(aUnit.getKey3());
					bean.getCells().add(aCell);
				}
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.RadioGroupMandatory.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.OptionMandatory);
					aCell.setValue(aUnit.getKey3());
					bean.getCells().add(aCell);
				}
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.CheckboxGroup.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.OptionMultiSelect);
					aCell.setValue(aUnit.getKey4());
					bean.getCells().add(aCell);
				}
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.CheckboxGroupMandatory.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.OptionMultiSelectMandatory);
					aCell.setValue(aUnit.getKey4());
					bean.getCells().add(aCell);
				}
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.Date.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.Date);
					aCell.setValue(aUnit.getKey4());
					bean.getCells().add(aCell);
				}
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.DateMandatory.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.DateMandatory);
					aCell.setValue(aUnit.getKey4());
					bean.getCells().add(aCell);
				}
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.DateTime.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.DateTime);
					aCell.setValue(aUnit.getKey4());
					bean.getCells().add(aCell);
				}
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.DateTimeMandatory.getValue())
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setCellType(BomCellTypeEnum.DateTimeMandatory);
					aCell.setValue(aUnit.getKey4());
					bean.getCells().add(aCell);
				}
				else if(aOption.getType() != null && 
							(aOption.getType().getValue() == BomTypesEnum.CommentBox.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.TextBox.getValue()
							)
						)
				{
					TCell aCell = new TCell();
					aCell.setCellIndex(Integer.parseInt(String.valueOf(aOption.getValue())));
					aCell.setLabel(aOption.getText());
					aCell.setValue(aUnit.getKey4());
					if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.CommentBox.getValue())
					{
						aCell.setCellType(BomCellTypeEnum.TextArea);
					}
					if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue())
					{
						aCell.setCellType(BomCellTypeEnum.NumericText);
					}
					if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.TextBox.getValue())
					{
						if(sItem.getDataType() == DataTypes.DATATYPE_INTEGER)
							aCell.setCellType(BomCellTypeEnum.NumericText);
						else
							aCell.setCellType(BomCellTypeEnum.Text);
					}
					bean.getCells().add(aCell);
				}
			}
		}
		return bean;
 	}

	private AdvancedBomInspectionItemResponseBean getResponseForBomAnswerType(
			SurveySaveItem question, SurveyItemResponse itemResponse) throws Exception
    {
		if(itemResponse == null || itemResponse.getResponseUnits() == null || itemResponse.getResponseUnits().size() == 0)
			return null;
		
		BomInspectItemAnswerType sItem = (BomInspectItemAnswerType)question;
		AdvancedBomInspectionItemResponseBean bean = new AdvancedBomInspectionItemResponseBean();
		bean.setFormItemId(sItem.getSurveyItemId());

		for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
		{
			ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
			if (sItem.selectionKey == aUnit.getKey2())
			{
				int selectedValue = aUnit.getKey3();
				if(selectedValue == AdvancedBomInspectItemAnswerType.passVal)
					bean.setResult(ResultEnum.pass.value());
				else if(selectedValue == AdvancedBomInspectItemAnswerType.failVal)
					bean.setResult(ResultEnum.fail.value());
				else if(selectedValue == AdvancedBomInspectItemAnswerType.naVal)
					bean.setResult(ResultEnum.notApplicable.value());
			}
			else if (sItem.commentsKey == aUnit.getKey2() && aUnit.getKey4() != null)
			{
				String canswer = aUnit.getKey4();
				if(canswer != null && canswer.trim().length() > 0)
				{
					bean.setComment(canswer);
				}
			}
			else if (sItem.actualValueKey == aUnit.getKey2() && aUnit.getKey4() != null)
			{
				String answer = aUnit.getKey4();
				TCell aCell = new TCell();
				aCell.setCellIndex(0);
				aCell.setLabel("Actual Value");
				aCell.setValue(answer);
				if(sItem.getDataType() == DataTypes.DATATYPE_INTEGER)
					aCell.setCellType(BomCellTypeEnum.NumericText);
				else
					aCell.setCellType(BomCellTypeEnum.Text);
				bean.getCells().add(aCell);
			}
			else if(sItem.imageKey == aUnit.getKey2())
			{
				bean.setImageFileName(aUnit.getKey4());
				bean.setImageFileDisplayName(aUnit.getKey4());
			}
		}

		return bean;
    }

	private TextBoxItemResponseBean getResponseForTextBoxAnswerType(
			SurveySaveItem question, SurveyItemResponse itemResponse) throws Exception
    {
		if(itemResponse == null || itemResponse.getResponseUnits() == null || itemResponse.getResponseUnits().size() == 0)
			return null;
		
		TextBoxAnswerType sItem = (TextBoxAnswerType)question;
		TextBoxItemResponseBean bean = new TextBoxItemResponseBean();
		bean.setFormItemId(sItem.getSurveyItemId());

		ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(0);
		String answer = aUnit.getKey4();

		bean.setValue(answer);
		
		return bean;
	}

	private TextAreaItemResponseBean getResponseForTextAreaAnswerType(
			TextAreaAnswerType question,  SurveyItemResponse itemResponse) throws Exception 
    {
		if(itemResponse == null || itemResponse.getResponseUnits() == null || itemResponse.getResponseUnits().size() == 0)
			return null;
		
		TextAreaAnswerType sItem = (TextAreaAnswerType)question;
		TextAreaItemResponseBean bean = new TextAreaItemResponseBean();
		bean.setFormItemId(sItem.getSurveyItemId());
		
		ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(0);
		String answer = aUnit.getKey4();

		bean.setValue(answer);
		
		return bean;
	}

	private SignatureCaptureItemResponseBean getResponseForSignatureCaptureAnswerType(
			SignatureCaptureAnswerType question,  SurveyItemResponse itemResponse) throws Exception 
    {
		if(itemResponse == null || itemResponse.getResponseUnits() == null || itemResponse.getResponseUnits().size() == 0)
			return null;
		
		SignatureCaptureAnswerType sItem = (SignatureCaptureAnswerType)question;
		SignatureCaptureItemResponseBean bean = new SignatureCaptureItemResponseBean();
		bean.setFormItemId(sItem.getSurveyItemId());
		
		ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(0);
		if(aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
		{
			SignatureCaptureItemResponse val = new ObjectMapper().readValue(aUnit.getKey4(), SignatureCaptureItemResponse.class);

			bean.setImageFileName(val.getImageFileName());
			bean.setSignatureTimestamp(val.getSignatureTimestamp());
			bean.setSignedBy(val.getSignedBy());
			
			File file = FileStoreManager.getFile(val.getImageFileName());
			byte[] imgBytes = Files.readAllBytes(file.toPath());
			bean.setImageEncodedString(new String(Base64.encodeBase64(imgBytes)));
			
		}

		
		return bean;
	}

    private OptionGroupItemResponseBean getResponseForMultiChoiceMultiAnswerType(
			OneDOptionType ssItem,  SurveyItemResponse itemResponse) throws Exception
    {
		if(itemResponse == null || itemResponse.getResponseUnits() == null || itemResponse.getResponseUnits().size() == 0)
			return null;
		
		RadioButtonAnswerType sItem = (RadioButtonAnswerType)ssItem;
		OptionGroupItemResponseBean bean = new OptionGroupItemResponseBean();
		bean.setFormItemId(sItem.getSurveyItemId());
		
		List<Integer> answers = new ArrayList<Integer>();
        for(int r=0; r<itemResponse.getResponseUnits().size(); r++)
	    {
		    ResponseUnit aUnit = (ResponseUnit)itemResponse.getResponseUnits().get(r);
		    if(aUnit.getKey1() != 0)
		    {
		        answers.add(aUnit.getKey1());
		    }
	    }
        int[] ans = new int[answers.size()];
        for (int i=0; i<answers.size(); i++) {
        	ans[i] = answers.get(i);
		}
        bean.setValue(ans);
    	return bean;
    }
}