package com.tathvatech.pdf.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tathvatech.pdf.common.ItemPrintAreaDef;
import com.tathvatech.pdf.common.PdfPageDef;
import com.tathvatech.pdf.common.PdfPrintArea;

public class PdfTemplatePrintLocationConfig
{
	List<PdfPageDef> pageDefs = new ArrayList<PdfPageDef>();
	List<ItemPrintAreaDef> itemPrintAreaList = new ArrayList<>();


	// these 2 are only internal and created only when you get it out.
	@JsonIgnore
	private HashMap<Integer, PdfPageDef>  pageDefMap = null;
	@JsonIgnore
	private HashMap<String, PdfPrintArea[]>  printAreaMap = null;


	@JsonIgnore
	public void addPageDef(PdfPageDef aItem)
	{
		pageDefs.add(aItem);
		pageDefMap = null;
	}
	@JsonIgnore
	public void addItemPrintArea(ItemPrintAreaDef aItem)
	{
		itemPrintAreaList.add(aItem);
		printAreaMap = null;
	}

	public List<PdfPageDef> getPageDefs()
	{
		return pageDefs;
	}
	public void setPageDefs(List<PdfPageDef> pageDefs)
	{
		this.pageDefs = pageDefs;
	}
	public List<ItemPrintAreaDef> getItemPrintAreaList()
	{
		return itemPrintAreaList;
	}
	public void setItemPrintAreaList(List<ItemPrintAreaDef> itemPrintAreaList)
	{
		this.itemPrintAreaList = itemPrintAreaList;
		printAreaMap = null;
	}

	@JsonIgnore
	public PdfPageDef getPageDef(Integer pageNo)
	{
		if(pageDefMap == null)
		{
			pageDefMap = new HashMap<Integer, PdfPageDef>();
			for (Iterator iterator = pageDefs.iterator(); iterator.hasNext();)
			{
				PdfPageDef aPageDef = (PdfPageDef) iterator.next();
				pageDefMap.put(aPageDef.getPageNumber(), aPageDef);
			}
		}
		return pageDefMap.get(pageNo);
	}

	@JsonIgnore
	public PdfPrintArea[] getItemPrintArea(String identifier)
	{
		if(printAreaMap == null)
		{
			printAreaMap = new HashMap<String, PdfPrintArea[]>();
			for (Iterator iterator = itemPrintAreaList.iterator(); iterator.hasNext();)
			{
				ItemPrintAreaDef itemPrintAreaDef = (ItemPrintAreaDef) iterator.next();
				printAreaMap.put(itemPrintAreaDef.getIdentifier(), itemPrintAreaDef.getPrintAreas());
			}
		}
		return printAreaMap.get(identifier);
	}
}
