package com.tathvatech.pdf.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tathvatech.pdf.common.ItemPrintAreaDef;


public class PdfTemplatePrintLocationConfig
{
	List<ItemPrintAreaDef> itemPrintAreaList = new ArrayList<>();

	@JsonIgnore
	public void addItemPrintArea(ItemPrintAreaDef aItem)
	{
		itemPrintAreaList.add(aItem);
	}
	public List<ItemPrintAreaDef> getItemPrintAreaList()
	{
		return itemPrintAreaList;
	}
	public void setItemPrintAreaList(List<ItemPrintAreaDef> itemPrintAreaList)
	{
		this.itemPrintAreaList = itemPrintAreaList;
	}


	public static void main(String[] args)
	{
		try
		{
			PdfTemplatePrintLocationConfig config = getCable40();

			ObjectMapper m = new ObjectMapper();
			String s = m.writeValueAsString(config);
			System.out.println(s);
			
			
			
			ObjectMapper jm = new ObjectMapper();
			PdfTemplatePrintLocationConfig aBeanRec = jm.readValue(s, PdfTemplatePrintLocationConfig.class);
			
			System.out.println(aBeanRec.getItemPrintAreaList().size());
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static PdfTemplatePrintLocationConfig getBaliseData()
	{
		PdfTemplatePrintLocationConfig config = new PdfTemplatePrintLocationConfig();
		
		config.addItemPrintArea(new ItemPrintAreaDef("Equipment ID", new PdfPrintArea[]{new PdfPrintArea(1,170,550,240,580,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(2,170,610,240,640,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(3,170,610,240,640,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(4,170,610,240,640,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(5,170,623,240,653,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Station Name", new PdfPrintArea[]{new PdfPrintArea(1,245,550,320,580,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(2,245,610,320,640,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(3,245,610,320,640,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(4,245,610,320,640,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER), new PdfPrintArea(5,245,620,320,650,10,Element.ALIGN_LEFT | Element.ALIGN_CENTER)}));

		
		config.addItemPrintArea(new ItemPrintAreaDef("Sector/Zone", new PdfPrintArea[]{new PdfPrintArea(1,320,550,390,580,10), new PdfPrintArea(2,320,610,390,640,10), new PdfPrintArea(3,320,610,390,640,10), new PdfPrintArea(4,320,610,390,640,10), new PdfPrintArea(5,320,620,390,650,10)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Track ID",     				new PdfPrintArea[]{new PdfPrintArea(1,390,550,460,580,10), new PdfPrintArea(2,390,610,460,640,10), new PdfPrintArea(3,390,610,460,640,10), new PdfPrintArea(4,390,610,460,640,10), new PdfPrintArea(5,390,620,460,650,10)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Kilometric Poiint", 			new PdfPrintArea[]{new PdfPrintArea(1,460,550,530,580,10), new PdfPrintArea(2,460,610,530,640,10), new PdfPrintArea(3,460,610,530,640,10), new PdfPrintArea(4,460,610,530,640,10), new PdfPrintArea(5,460,620,530,650,10)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Equipment S/N", 				new PdfPrintArea[]{new PdfPrintArea(1,170,520,241,550,10), new PdfPrintArea(2,170,580,241,610,10), new PdfPrintArea(3,170,580,241,610,10), new PdfPrintArea(4,170,580,241,610,10), new PdfPrintArea(5,170,590,241,620,10)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Installation Procedure No", 	new PdfPrintArea[]{new PdfPrintArea(1,390,520,530,550,10), new PdfPrintArea(2,390,580,530,610,10), new PdfPrintArea(3,390,580,530,610,10), new PdfPrintArea(4,390,580,530,610,10), new PdfPrintArea(5,390,590,530,620,10)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Scheme Plan No.", 			new PdfPrintArea[]{new PdfPrintArea(1,170,490,241,520,10), new PdfPrintArea(2,170,550,241,580,10), new PdfPrintArea(3,170,550,241,580,10), new PdfPrintArea(4,170,550,241,580,10), new PdfPrintArea(5,170,563,241,593,10)}));
		config.addItemPrintArea(new ItemPrintAreaDef("ELARD No.", 					new PdfPrintArea[]{new PdfPrintArea(1,390,490,530,520,10), new PdfPrintArea(2,390,550,530,580,10), new PdfPrintArea(3,390,550,530,580,10), new PdfPrintArea(4,390,550,530,580,10), new PdfPrintArea(5,390,563,530,593,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,430,340,450,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.00-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,430,390,450,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,430,530,450,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,400,340,430,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,400,390,430,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,400,530,430,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,390,340,410,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,390,390,410,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,390,530,410,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,370,340,390,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,370,390,390,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,370,530,390,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,350,340,370,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,350,390,370,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,350,530,370,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.05-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,330,340,350,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.05-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,330,390,350,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.05-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,330,530,350,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.06-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,310,340,330,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.06-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,310,390,330,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.06-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,310,530,330,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.07-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,320,290,340,310,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.07-date", 		new PdfPrintArea[]{new PdfPrintArea(1,345,290,390,310,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.07-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,390,290,530,310,0)}));
		
				
		config.addItemPrintArea(new ItemPrintAreaDef("Page 1 Comments", 		new PdfPrintArea[]{new PdfPrintArea(1,100,190,390,280,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,155,160,240,190,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,155,125,240,145,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.08-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,320,490,340,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.08-date", 		new PdfPrintArea[]{new PdfPrintArea(2,345,490,390,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.08-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,390,490,530,510,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.09-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,320,465,340,485,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.09-date", 		new PdfPrintArea[]{new PdfPrintArea(2,345,465,390,485,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.09-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,390,465,530,485,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.10-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,320,440,340,460,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.10-date", 		new PdfPrintArea[]{new PdfPrintArea(2,345,440,390,460,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.10-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,390,440,530,460,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.11-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,320,405,340,425,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.11-date", 		new PdfPrintArea[]{new PdfPrintArea(2,345,405,390,425,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.11-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,390,405,530,425,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 2 Comments", 		new PdfPrintArea[]{new PdfPrintArea(2,100,190,390,400,10)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,155,175,240,205,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,155,145,240,165,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,320,490,340,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-date", 		new PdfPrintArea[]{new PdfPrintArea(3,345,490,390,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,390,490,530,510,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,320,465,340,485,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-date", 		new PdfPrintArea[]{new PdfPrintArea(3,345,465,390,485,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,390,465,530,485,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,320,440,340,460,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-date", 		new PdfPrintArea[]{new PdfPrintArea(3,345,440,390,460,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,390,440,530,460,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,320,350,340,400,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-date",     	new PdfPrintArea[]{new PdfPrintArea(3,345,350,390,400,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-comment",  	new PdfPrintArea[]{new PdfPrintArea(3,390,350,530,400,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-passFail", 						new PdfPrintArea[]{new PdfPrintArea(3,320,300,340,350,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-date",     						new PdfPrintArea[]{new PdfPrintArea(3,345,300,390,350,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-Torque Wrench S/N",     		new PdfPrintArea[]{new PdfPrintArea(3,450,341,580,361,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-Calibration Certificate No", 	new PdfPrintArea[]{new PdfPrintArea(3,470,322,580,342,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-Date of Expiry",				new PdfPrintArea[]{new PdfPrintArea(3,450,306,580,326,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 3 Comments", 		new PdfPrintArea[]{new PdfPrintArea(3,390,100,515,200,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-sign", 		new PdfPrintArea[]{new PdfPrintArea(3,155,175,240,205,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(3,155,145,240,165,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,320,490,340,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-date",     		new PdfPrintArea[]{new PdfPrintArea(4,345,490,390,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,390,490,530,510,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-passFail",  					new PdfPrintArea[]{new PdfPrintArea(4,320,415,340,435,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-date",        					new PdfPrintArea[]{new PdfPrintArea(4,345,415,390,435,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-Torque Wrench S/N",        		new PdfPrintArea[]{new PdfPrintArea(4,450,410,580,430,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-Calibration Certificate No",   	new PdfPrintArea[]{new PdfPrintArea(4,470,390,580,410,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-Date of Expiry",  				new PdfPrintArea[]{new PdfPrintArea(4,450,370,580,390,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-passFail",  	new PdfPrintArea[]{new PdfPrintArea(4,320,355,340,375,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-date",      	new PdfPrintArea[]{new PdfPrintArea(4,345,355,390,375,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-comment",   	new PdfPrintArea[]{new PdfPrintArea(4,390,355,530,375,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-passFail",  	new PdfPrintArea[]{new PdfPrintArea(4,320,332,340,352,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-date",      	new PdfPrintArea[]{new PdfPrintArea(4,345,332,390,352,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-comment",   	new PdfPrintArea[]{new PdfPrintArea(4,390,332,530,352,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.09-passFail",  	new PdfPrintArea[]{new PdfPrintArea(4,320,310,340,330,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.09-date",      	new PdfPrintArea[]{new PdfPrintArea(4,345,310,390,330,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.09-comment",   	new PdfPrintArea[]{new PdfPrintArea(4,390,310,530,330,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 4 Comments", 		new PdfPrintArea[]{new PdfPrintArea(4,390,100,515,200,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 4-sign", 		new PdfPrintArea[]{new PdfPrintArea(4,160,155,240,185,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 4-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(4,160,125,250,145,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.00-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,320,515,340,535,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.00-date",     		new PdfPrintArea[]{new PdfPrintArea(5,345,515,390,535,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.00-comment",  		new PdfPrintArea[]{new PdfPrintArea(5,390,515,530,535,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-passFail",    					new PdfPrintArea[]{new PdfPrintArea(5,320,490,340,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-date",        					new PdfPrintArea[]{new PdfPrintArea(5,345,490,390,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-Torque Wrench S/N",        		new PdfPrintArea[]{new PdfPrintArea(5,450,492,580,512,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-Calibration Certificate No",   	new PdfPrintArea[]{new PdfPrintArea(5,470,473,580,493,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-Date of Expiry",  				new PdfPrintArea[]{new PdfPrintArea(5,450,455,580,475,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-passFail",    	new PdfPrintArea[]{new PdfPrintArea(5,320,440,340,460,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-date",        	new PdfPrintArea[]{new PdfPrintArea(5,345,440,390,460,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-comment",     	new PdfPrintArea[]{new PdfPrintArea(5,390,440,530,460,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,320,420,340,440,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-date", 			new PdfPrintArea[]{new PdfPrintArea(5,345,420,390,440,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,390,420,530,440,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,320,400,340,420,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-date", 			new PdfPrintArea[]{new PdfPrintArea(5,345,400,390,420,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,390,400,530,420,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,320,365,340,385,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-date", 			new PdfPrintArea[]{new PdfPrintArea(5,345,365,390,385,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,390,365,530,385,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,320,330,340,365,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-date", 			new PdfPrintArea[]{new PdfPrintArea(5,345,330,390,365,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,390,330,530,365,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.07-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,320,310,340,330,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.07-date", 			new PdfPrintArea[]{new PdfPrintArea(5,345,310,390,330,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.07-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,390,310,530,330,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.08-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,320,275,340,310,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.08-date", 			new PdfPrintArea[]{new PdfPrintArea(5,345,275,390,310,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.08-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,390,275,530,310,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - End-sign", 		new PdfPrintArea[]{new PdfPrintArea(5,160,158,250,188,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - End-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(5,160,128,250,148,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 TELP - End-sign", 				new PdfPrintArea[]{new PdfPrintArea(5,295,158,385,188,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 TELP - End-signedBy", 			new PdfPrintArea[]{new PdfPrintArea(5,295,128,385,148,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Operator - End-sign",	 		new PdfPrintArea[]{new PdfPrintArea(5,410,158,500,188,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Operator - End-signedBy", 			new PdfPrintArea[]{new PdfPrintArea(5,425,128,515,148,0)}));
		
		return config;
		
	}

	
	private static PdfTemplatePrintLocationConfig getSignalHeadAndStencilRoute()
	{
		PdfTemplatePrintLocationConfig config = new PdfTemplatePrintLocationConfig();
		
		config.addItemPrintArea(new ItemPrintAreaDef("Eqpt. I.D.", new PdfPrintArea[]{new PdfPrintArea(1,181,534,252,575,0,Element.ALIGN_CENTER), new PdfPrintArea(2,181,596,252,637,0,Element.ALIGN_CENTER), new PdfPrintArea(3,181,596,252,637,0,Element.ALIGN_CENTER), new PdfPrintArea(4,181,596,252,637,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Station Name", new PdfPrintArea[]{new PdfPrintArea(1,259,534,409,575,0,Element.ALIGN_CENTER), new PdfPrintArea(2,259,596,409,637,0,Element.ALIGN_CENTER), new PdfPrintArea(3,259,596,409,637,0,Element.ALIGN_CENTER), new PdfPrintArea(4,259,596,409,637,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Location", new PdfPrintArea[]{new PdfPrintArea(1,416,534,448,575,0,Element.ALIGN_CENTER), new PdfPrintArea(2,416,596,448,637,0,Element.ALIGN_CENTER), new PdfPrintArea(3,416,596,448,637,0,Element.ALIGN_CENTER), new PdfPrintArea(4,416,596,448,637,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Grid Line/Level",	new PdfPrintArea[]{new PdfPrintArea(1,456,534,560,575,0,Element.ALIGN_CENTER), new PdfPrintArea(2,456,596,560,637,0,Element.ALIGN_CENTER), new PdfPrintArea(3,456,596,560,637,0,Element.ALIGN_CENTER), new PdfPrintArea(4,456,596,560,637,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Ref. Drawing No:", new PdfPrintArea[]{new PdfPrintArea(1,181,507,563,519,0,Element.ALIGN_LEFT), new PdfPrintArea(2,181,570,563,582,0,Element.ALIGN_LEFT), new PdfPrintArea(3,181,570,563,582,0,Element.ALIGN_LEFT), new PdfPrintArea(4,181,570,563,582,0,Element.ALIGN_LEFT)}));


		config.addItemPrintArea(new ItemPrintAreaDef("1.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,337,422,362,446,0,Element.ALIGN_LEFT)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.00-date", 		new PdfPrintArea[]{new PdfPrintArea(1,369,422,409,446,0,Element.ALIGN_LEFT)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,415,422,561,446,0,Element.ALIGN_LEFT)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,337,408,362,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-date", 		new PdfPrintArea[]{new PdfPrintArea(1,369,408,409,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,415,408,561,422,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,337,362,362,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-date", 		new PdfPrintArea[]{new PdfPrintArea(1,369,362,409,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,415,362,561,394,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,337,352,362,366,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-date", 		new PdfPrintArea[]{new PdfPrintArea(1,369,352,409,366,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,415,352,561,366,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,337,302,362,336,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-date", 		new PdfPrintArea[]{new PdfPrintArea(1,369,302,409,336,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,415,302,561,336,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 1 Comments", 		new PdfPrintArea[]{new PdfPrintArea(1,100,201,453,302,0,Element.ALIGN_LEFT)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,160,174,250,201,0,Element.ALIGN_LEFT)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,160,147,250,162,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,337,484,360,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-date", 		new PdfPrintArea[]{new PdfPrintArea(2,368,484,408,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,414,484,557,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,337,473,360,486,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-date", 		new PdfPrintArea[]{new PdfPrintArea(2,368,473,408,486,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,414,473,557,486,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,337,453,360,466,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-date", 		new PdfPrintArea[]{new PdfPrintArea(2,368,453,408,466,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,414,453,557,466,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,337,420,360,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-date",     	new PdfPrintArea[]{new PdfPrintArea(2,368,420,408,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-comment",  	new PdfPrintArea[]{new PdfPrintArea(2,414,420,557,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,337,409,360,420,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-date",     		new PdfPrintArea[]{new PdfPrintArea(2,368,409,408,420,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-comment",  		new PdfPrintArea[]{new PdfPrintArea(2,414,409,557,420,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,337,377,360,399,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-date",     		new PdfPrintArea[]{new PdfPrintArea(2,368,377,408,399,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-comment",  		new PdfPrintArea[]{new PdfPrintArea(2,414,377,557,399,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,337,314,360,360,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-date",     		new PdfPrintArea[]{new PdfPrintArea(2,368,314,408,360,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-Torque Wrench S/N", new PdfPrintArea[]{new PdfPrintArea(2,500,358,588,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-Calibration Certificate No", 	new PdfPrintArea[]{new PdfPrintArea(2,470,342,558,355,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-Date of Expiry",				new PdfPrintArea[]{new PdfPrintArea(2,482,329,558,342,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-passFail",  	new PdfPrintArea[]{new PdfPrintArea(2,337,297,360,319,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-date",      	new PdfPrintArea[]{new PdfPrintArea(2,368,297,408,319,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-comment",   	new PdfPrintArea[]{new PdfPrintArea(2,414,297,557,319,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-passFail",  	new PdfPrintArea[]{new PdfPrintArea(2,337,271,360,293,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-date",      	new PdfPrintArea[]{new PdfPrintArea(2,368,271,408,293,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-comment",   	new PdfPrintArea[]{new PdfPrintArea(2,414,271,557,293,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 2 Comments", 		new PdfPrintArea[]{new PdfPrintArea(2,296,192,449,272,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,160,151,245,178,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,160,131,245,143,0)}));
		

		config.addItemPrintArea(new ItemPrintAreaDef("3.00-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,337,472,360,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.00-date",     		new PdfPrintArea[]{new PdfPrintArea(3,368,472,408,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.00-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,414,472,557,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,337,450,360,468,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-date",     		new PdfPrintArea[]{new PdfPrintArea(3,368,450,408,468,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,414,450,557,468,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-passFail",    	new PdfPrintArea[]{new PdfPrintArea(3,337,428,360,446,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-date",        	new PdfPrintArea[]{new PdfPrintArea(3,368,428,408,446,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-comment",     	new PdfPrintArea[]{new PdfPrintArea(3,414,428,557,446,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.03-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,337,407,360,425,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-date", 			new PdfPrintArea[]{new PdfPrintArea(3,368,407,408,425,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-comment", 		new PdfPrintArea[]{new PdfPrintArea(3,414,407,557,425,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,337,386,360,404,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-date", 			new PdfPrintArea[]{new PdfPrintArea(3,368,386,408,404,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-comment", 		new PdfPrintArea[]{new PdfPrintArea(3,414,386,557,404,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,337,355,360,380,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-date", 			new PdfPrintArea[]{new PdfPrintArea(3,368,355,408,380,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-comment", 		new PdfPrintArea[]{new PdfPrintArea(3,414,355,557,380,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-passFail", 						new PdfPrintArea[]{new PdfPrintArea(3,337,269,360,331,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-date",     						new PdfPrintArea[]{new PdfPrintArea(3,368,269,408,331,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-Torque Wrench S/N",     		new PdfPrintArea[]{new PdfPrintArea(3,500,338,573,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-Calibration Certificate No", 	new PdfPrintArea[]{new PdfPrintArea(3,469,317,542,333,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-Date of Expiry",				new PdfPrintArea[]{new PdfPrintArea(3,483,299,556,315,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 3 Comments", 		new PdfPrintArea[]{new PdfPrintArea(3,296,219,449,292,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-sign", 		new PdfPrintArea[]{new PdfPrintArea(3,151,155,289,190,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(3,160,119,298,135,0)}));
		
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.00-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,337,485,360,501,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.00-date",     		new PdfPrintArea[]{new PdfPrintArea(4,368,485,408,501,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.00-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,414,485,557,501,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.01-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,337,466,360,482,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.01-date",     		new PdfPrintArea[]{new PdfPrintArea(4,368,466,408,482,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.01-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,414,466,557,482,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.02-passFail",    	new PdfPrintArea[]{new PdfPrintArea(4,337,447,360,463,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.02-date",        	new PdfPrintArea[]{new PdfPrintArea(4,368,447,408,463,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.02-comment",     	new PdfPrintArea[]{new PdfPrintArea(4,414,447,557,463,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("4.03-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,337,428,360,444,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-date", 			new PdfPrintArea[]{new PdfPrintArea(4,368,428,408,444,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-comment", 		new PdfPrintArea[]{new PdfPrintArea(4,414,428,557,444,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,337,408,360,424,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.04-date", 			new PdfPrintArea[]{new PdfPrintArea(4,368,408,408,424,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.04-comment", 		new PdfPrintArea[]{new PdfPrintArea(4,414,408,557,424,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,337,378,360,401,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.05-date", 			new PdfPrintArea[]{new PdfPrintArea(4,368,378,408,401,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.05-comment", 		new PdfPrintArea[]{new PdfPrintArea(4,414,378,557,401,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,337,355,360,375,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.06-date", 			new PdfPrintArea[]{new PdfPrintArea(4,368,355,408,375,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.06-comment", 		new PdfPrintArea[]{new PdfPrintArea(4,414,355,557,375,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.07-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,337,332,360,352,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.07-date", 			new PdfPrintArea[]{new PdfPrintArea(4,368,332,408,352,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.07-comment", 		new PdfPrintArea[]{new PdfPrintArea(4,414,332,557,352,0)}));
		
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - End-sign", 		new PdfPrintArea[]{new PdfPrintArea(4,148,170,249,201,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - End-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(4,163,149,264,164,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 TELP - End-sign", 				new PdfPrintArea[]{new PdfPrintArea(4,310,170,411,201,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 TELP - End-signedBy", 			new PdfPrintArea[]{new PdfPrintArea(4,319,149,420,164,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Operator - End-sign",	 		new PdfPrintArea[]{new PdfPrintArea(4,461,170,562,201,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Operator - End-signedBy", 			new PdfPrintArea[]{new PdfPrintArea(4,476,149,577,164,0)}));
		
		return config;
		
	}
	
	private static PdfTemplatePrintLocationConfig getMD2000()
	{
		PdfTemplatePrintLocationConfig config = new PdfTemplatePrintLocationConfig();
		
		config.addItemPrintArea(new ItemPrintAreaDef("Equipment. I.D.", new PdfPrintArea[]{new PdfPrintArea(1,173,618,253,638,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Serial Number", new PdfPrintArea[]{new PdfPrintArea(1,257,618,337,638,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Date", new PdfPrintArea[]{new PdfPrintArea(1,370,643,470,663,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Reference Drawing Numbers",	new PdfPrintArea[]{new PdfPrintArea(1,173,565,331,590,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Location", new PdfPrintArea[]{new PdfPrintArea(1,420,615,520,633,0,Element.ALIGN_LEFT)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Track Name", new PdfPrintArea[]{new PdfPrintArea(1,404,593,504,611,0,Element.ALIGN_LEFT)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Chainage", new PdfPrintArea[]{new PdfPrintArea(1,404,572,554,590,0,Element.ALIGN_LEFT)}));


		config.addItemPrintArea(new ItemPrintAreaDef("1.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,488,402,506,0,Element.ALIGN_LEFT)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,411,488,519,506,0,Element.ALIGN_LEFT)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,468,402,484,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,411,468,519,484,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,455,402,467,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,411,455,519,467,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,407,402,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,411,407,519,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,373,402,385,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,411,373,519,385,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,340,402,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,411,340,519,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,310,402,334,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,411,310,519,334,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,342,299,402,311,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-comment",  	new PdfPrintArea[]{new PdfPrintArea(1,411,299,519,311,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(1,342,235,402,277,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-comment",  		new PdfPrintArea[]{new PdfPrintArea(1,411,235,519,277,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(1,342,186,402,228,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-comment",  		new PdfPrintArea[]{new PdfPrintArea(1,411,186,519,228,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(1,342,182,402,194,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-comment",  		new PdfPrintArea[]{new PdfPrintArea(1,411,182,519,194,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-passFail",  	new PdfPrintArea[]{new PdfPrintArea(1,261,118,293,140,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.01-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,567,402,584,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-comment",  		new PdfPrintArea[]{new PdfPrintArea(2,411,567,519,584,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-passFail",    	new PdfPrintArea[]{new PdfPrintArea(2,342,535,402,558,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-comment",     	new PdfPrintArea[]{new PdfPrintArea(2,411,535,519,558,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.03-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,513,402,531,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-comment", 		new PdfPrintArea[]{new PdfPrintArea(2,411,513,519,531,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,490,402,508,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-comment", 		new PdfPrintArea[]{new PdfPrintArea(2,411,490,519,508,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,467,402,485,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-comment", 		new PdfPrintArea[]{new PdfPrintArea(2,411,467,519,485,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,411,402,451,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-comment",     	new PdfPrintArea[]{new PdfPrintArea(2,411,411,519,451,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.07-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,396,402,416,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.07-comment",     	new PdfPrintArea[]{new PdfPrintArea(2,411,396,519,416,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.00-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,350,402,370,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.00-comment",  		new PdfPrintArea[]{new PdfPrintArea(2,411,350,519,370,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.01-passFail", 		new PdfPrintArea[]{new PdfPrintArea(2,342,338,402,350,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.01-comment",  		new PdfPrintArea[]{new PdfPrintArea(2,411,338,519,350,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4.02-passFail",    	new PdfPrintArea[]{new PdfPrintArea(2,342,306,402,331,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.02-comment",     	new PdfPrintArea[]{new PdfPrintArea(2,411,306,519,331,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("4.03-Point A >1Mohm at 500Vdc", 		new PdfPrintArea[]{new PdfPrintArea(2,202,255,226,265,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-Point C >1Mohm at 500Vdc", 			new PdfPrintArea[]{new PdfPrintArea(2,202,246,226,256,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-Point E >1Mohm at 500Vdc", 		new PdfPrintArea[]{new PdfPrintArea(2,202,236,226,246,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-Point F >1Mohm at 500Vdc", 		new PdfPrintArea[]{new PdfPrintArea(2,202,216,226,226,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-Point F >1Mohm at 500Vdc", 		new PdfPrintArea[]{new PdfPrintArea(2,202,205,226,215,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-comment", 		new PdfPrintArea[]{new PdfPrintArea(2,342,131,402,251,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4.03-comment", 		new PdfPrintArea[]{new PdfPrintArea(2,411,131,519,251,0)}));
		
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,342,118,402,136,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,411,118,519,136,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,679,402,697,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,679,519,697,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,635,402,667,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,635,519,667,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,595,402,627,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.03-comment",  	new PdfPrintArea[]{new PdfPrintArea(3,411,595,519,627,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,342,554,402,586,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.04-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,411,554,519,586,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,342,514,402,546,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.05-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,411,514,519,546,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,342,476,402,508,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.06-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,411,476,519,508,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("5.07-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,438,402,470,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.07-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,438,519,470,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.08-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,419,402,439,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.08-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,419,519,439,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.09-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,393,402,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.09-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,393,519,413,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.10-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,369,402,389,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.10-comment",  	new PdfPrintArea[]{new PdfPrintArea(3,411,369,519,389,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.11-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,342,347,402,365,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.11-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,411,347,519,365,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.12-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,342,304,402,335,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.12-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,411,304,519,335,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5.13-passFail", 		new PdfPrintArea[]{new PdfPrintArea(3,342,276,402,301,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.13-comment",  		new PdfPrintArea[]{new PdfPrintArea(3,411,276,519,301,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("5.14-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,154,402,239,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5.14-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,154,519,239,0)}));
				
		config.addItemPrintArea(new ItemPrintAreaDef("6.00-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,129,402,147,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.00-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,129,519,147,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,342,108,402,126,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,411,108,519,126,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(4,342,682,402,699,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(4,411,682,519,699,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(4,342,671,402,681,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.03-comment",  	new PdfPrintArea[]{new PdfPrintArea(4,411,671,519,681,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,342,659,402,669,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.04-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,411,659,519,669,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,342,636,402,652,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.05-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,411,636,519,652,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,342,615,402,631,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.06-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,411,615,519,631,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("6.07-passFail", 	new PdfPrintArea[]{new PdfPrintArea(4,342,604,402,614,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6.07-comment", 	new PdfPrintArea[]{new PdfPrintArea(4,411,604,519,614,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 4 Comments", new PdfPrintArea[]{new PdfPrintArea(4,143,540,518,603,10)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Point Machine Installer - End-sign", 		new PdfPrintArea[]{new PdfPrintArea(4,319,493,417,530,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Point Machine Installer - End-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(4,197,484,295,511,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Representative - End-sign", 		new PdfPrintArea[]{new PdfPrintArea(4,319,430,417,467,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Representative - End-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(4,197,433,295,460,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("LTA Representative - End-sign",	 		new PdfPrintArea[]{new PdfPrintArea(4,319,383,417,420,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("LTA Representative - End-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(4,197,385,295,412,0)}));
		
		return config;
		
	}
	
	private static PdfTemplatePrintLocationConfig getFsJ25()
	{
		PdfTemplatePrintLocationConfig config = new PdfTemplatePrintLocationConfig();
		
		config.addItemPrintArea(new ItemPrintAreaDef("Equipment. I.D.", new PdfPrintArea[]{new PdfPrintArea(1,182,617,251,643,0,Element.ALIGN_CENTER), new PdfPrintArea(2,182,617,251,643,0,Element.ALIGN_CENTER), new PdfPrintArea(3,182,617,251,643,0,Element.ALIGN_CENTER), new PdfPrintArea(4,182,617,251,643,0,Element.ALIGN_CENTER), new PdfPrintArea(5,182,617,251,643,0,Element.ALIGN_CENTER), new PdfPrintArea(6,182,617,251,643,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Depot/Station Name", new PdfPrintArea[]{new PdfPrintArea(1,257,617,319,643,0,Element.ALIGN_CENTER), new PdfPrintArea(2,257,617,319,643,0,Element.ALIGN_CENTER), new PdfPrintArea(3,257,617,319,643,0,Element.ALIGN_CENTER), new PdfPrintArea(4,257,617,319,643,0,Element.ALIGN_CENTER), new PdfPrintArea(5,257,617,319,643,0,Element.ALIGN_CENTER), new PdfPrintArea(6,257,617,319,643,0,Element.ALIGN_CENTER)}));

		
		config.addItemPrintArea(new ItemPrintAreaDef("Sector/Zone", new PdfPrintArea[]{new PdfPrintArea(1,325,617,387,643,0,Element.ALIGN_CENTER), new PdfPrintArea(2,325,617,387,643,0,Element.ALIGN_CENTER), new PdfPrintArea(3,325,617,387,643,0,Element.ALIGN_CENTER), new PdfPrintArea(4,325,617,387,643,0,Element.ALIGN_CENTER), new PdfPrintArea(5,325,617,387,643,0,Element.ALIGN_CENTER), new PdfPrintArea(6,325,617,387,643,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Track I.D.",     				new PdfPrintArea[]{new PdfPrintArea(1,391,617,453,643,0,Element.ALIGN_CENTER), new PdfPrintArea(2,391,617,453,643,0,Element.ALIGN_CENTER), new PdfPrintArea(3,391,617,453,643,0,Element.ALIGN_CENTER), new PdfPrintArea(4,391,617,453,643,0,Element.ALIGN_CENTER), new PdfPrintArea(5,391,617,453,643,0,Element.ALIGN_CENTER), new PdfPrintArea(6,391,617,453,643,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Kilometric Point", 			new PdfPrintArea[]{new PdfPrintArea(1,459,617,521,643,0,Element.ALIGN_CENTER), new PdfPrintArea(2,459,617,521,643,0,Element.ALIGN_CENTER), new PdfPrintArea(3,459,617,521,643,0,Element.ALIGN_CENTER), new PdfPrintArea(4,459,617,521,643,0,Element.ALIGN_CENTER), new PdfPrintArea(5,459,617,521,643,0,Element.ALIGN_CENTER), new PdfPrintArea(6,459,617,521,643,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Equipment S/N:", 				new PdfPrintArea[]{new PdfPrintArea(1,181,595,319,615,0,Element.ALIGN_CENTER), new PdfPrintArea(2,181,595,319,615,0,Element.ALIGN_CENTER), new PdfPrintArea(3,181,595,319,615,0,Element.ALIGN_CENTER), new PdfPrintArea(4,181,595,319,615,0,Element.ALIGN_CENTER), new PdfPrintArea(5,181,595,319,615,0,Element.ALIGN_CENTER), new PdfPrintArea(6,181,595,319,615,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Installation Procedure No:", 	new PdfPrintArea[]{new PdfPrintArea(1,393,595,521,615,0,Element.ALIGN_CENTER), new PdfPrintArea(2,393,595,521,615,0,Element.ALIGN_CENTER), new PdfPrintArea(3,393,595,521,615,0,Element.ALIGN_CENTER), new PdfPrintArea(4,393,595,521,615,0,Element.ALIGN_CENTER), new PdfPrintArea(5,393,595,521,615,0,Element.ALIGN_CENTER), new PdfPrintArea(6,393,595,521,615,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("DTR No:", 			new PdfPrintArea[]{new PdfPrintArea(1,183,569,319,589,0,Element.ALIGN_CENTER), new PdfPrintArea(2,183,569,319,589,0,Element.ALIGN_CENTER), new PdfPrintArea(3,183,569,319,589,0,Element.ALIGN_CENTER), new PdfPrintArea(4,183,569,319,589,0,Element.ALIGN_CENTER), new PdfPrintArea(5,183,569,319,589,0,Element.ALIGN_CENTER), new PdfPrintArea(6,183,569,319,589,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Bonding Plan No:", 	new PdfPrintArea[]{new PdfPrintArea(1,393,569,521,589,0,Element.ALIGN_CENTER), new PdfPrintArea(2,393,569,521,589,0,Element.ALIGN_CENTER), new PdfPrintArea(3,393,569,521,589,0,Element.ALIGN_CENTER), new PdfPrintArea(4,393,569,521,589,0,Element.ALIGN_CENTER), new PdfPrintArea(5,393,569,521,589,0,Element.ALIGN_CENTER), new PdfPrintArea(6,393,569,521,589,0,Element.ALIGN_CENTER)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,330,501,354,525,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-date", 		new PdfPrintArea[]{new PdfPrintArea(1,361,501,385,525,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,501,522,525,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,330,488,354,502,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-date", 		new PdfPrintArea[]{new PdfPrintArea(1,361,488,385,502,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,488,522,502,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,330,469,354,483,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-date", 		new PdfPrintArea[]{new PdfPrintArea(1,361,469,385,483,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.03-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,469,522,483,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,330,450,354,464,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-date", 		new PdfPrintArea[]{new PdfPrintArea(1,361,450,385,464,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.04-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,450,522,464,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.05-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,330,431,354,445,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.05-date", 		new PdfPrintArea[]{new PdfPrintArea(1,361,431,385,445,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.05-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,431,522,445,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.06-passFail", 	new PdfPrintArea[]{new PdfPrintArea(1,330,400,354,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.06-date", 		new PdfPrintArea[]{new PdfPrintArea(1,361,400,385,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.06-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,400,522,422,0)}));
		
				
		config.addItemPrintArea(new ItemPrintAreaDef("Page 1 Comments", 		new PdfPrintArea[]{new PdfPrintArea(1,112,224,512,399,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,157,197,257,227,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,166,165,266,183,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.07-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,334,511,358,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.07-date", 		new PdfPrintArea[]{new PdfPrintArea(2,366,511,390,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.07-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,400,511,532,529,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.08-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,334,493,358,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.08-date", 		new PdfPrintArea[]{new PdfPrintArea(2,366,493,390,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.08-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,400,493,532,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.09-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,334,474,358,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.09-date", 		new PdfPrintArea[]{new PdfPrintArea(2,366,474,390,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.09-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,400,474,532,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1.10-passFail", 	new PdfPrintArea[]{new PdfPrintArea(2,334,454,358,470,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.10-date", 		new PdfPrintArea[]{new PdfPrintArea(2,366,454,390,470,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1.10-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,400,454,532,470,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 2 Comments", 		new PdfPrintArea[]{new PdfPrintArea(2,112,287,512,452,10)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,160,202,294,236,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,165,171,277,188,0)}));
		
		
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,334,474,357,515,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-date", 		new PdfPrintArea[]{new PdfPrintArea(3,363,474,386,515,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.01-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,394,474,521,515,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-passFail", 	new PdfPrintArea[]{new PdfPrintArea(3,334,474,357,484,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-date", 		new PdfPrintArea[]{new PdfPrintArea(3,363,474,386,484,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.02-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,394,474,521,484,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-passFail", 						new PdfPrintArea[]{new PdfPrintArea(3,334,270,357,408,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-date",     						new PdfPrintArea[]{new PdfPrintArea(3,363,270,386,408,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-Torque Wrench S/N",     		new PdfPrintArea[]{new PdfPrintArea(3,457,412,523,425,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-Calibration Certificate No", 	new PdfPrintArea[]{new PdfPrintArea(3,475,393,541,406,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.03-Date of Expiry",				new PdfPrintArea[]{new PdfPrintArea(3,441,375,507,388,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("Page 3 Comments", 		new PdfPrintArea[]{new PdfPrintArea(3,307,206,407,327,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-sign", 		new PdfPrintArea[]{new PdfPrintArea(3,161,176,253,196,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(3,169,157,261,169,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-passFail", 						new PdfPrintArea[]{new PdfPrintArea(4,333,335,356,478,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-date",     						new PdfPrintArea[]{new PdfPrintArea(4,364,335,387,478,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-Torque Wrench S/N",     		new PdfPrintArea[]{new PdfPrintArea(4,463,480,528,492,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-Calibration Certificate No", 	new PdfPrintArea[]{new PdfPrintArea(4,480,461,545,473,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.04-Date of Expiry",				new PdfPrintArea[]{new PdfPrintArea(4,447,442,512,454,0)}));
		
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(4,332,390,359,399,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-date",     		new PdfPrintArea[]{new PdfPrintArea(4,363,390,390,399,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.05-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,395,390,530,399,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-passFail",  					new PdfPrintArea[]{new PdfPrintArea(4,332,368,359,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-date",        					new PdfPrintArea[]{new PdfPrintArea(4,363,368,390,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.06-comment",  		new PdfPrintArea[]{new PdfPrintArea(4,395,368,530,384,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-passFail",  	new PdfPrintArea[]{new PdfPrintArea(4,332,348,359,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-date",      	new PdfPrintArea[]{new PdfPrintArea(4,363,348,390,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.07-comment",   	new PdfPrintArea[]{new PdfPrintArea(4,395,348,530,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-passFail",  	new PdfPrintArea[]{new PdfPrintArea(4,332,337,359,347,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-date",      	new PdfPrintArea[]{new PdfPrintArea(4,363,337,390,347,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.08-comment",   	new PdfPrintArea[]{new PdfPrintArea(4,395,337,530,347,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.09-passFail",  	new PdfPrintArea[]{new PdfPrintArea(4,332,315,359,331,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.09-date",      	new PdfPrintArea[]{new PdfPrintArea(4,363,315,390,331,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.09-comment",   	new PdfPrintArea[]{new PdfPrintArea(4,395,315,530,331,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 4 Comments", 		new PdfPrintArea[]{new PdfPrintArea(4,154,166,253,186,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 4-sign", 		new PdfPrintArea[]{new PdfPrintArea(4,154,166,253,186,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 4-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(4,171,142,270,156,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.10-passFail", 						new PdfPrintArea[]{new PdfPrintArea(5,331,405,355,497,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.10-date",     						new PdfPrintArea[]{new PdfPrintArea(5,362,405,386,497,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.10-Torque Wrench S/N",     		new PdfPrintArea[]{new PdfPrintArea(5,459,498,524,510,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.10-Calibration Certificate No", 	new PdfPrintArea[]{new PdfPrintArea(5,478,479,543,491,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.10-Date of Expiry",				new PdfPrintArea[]{new PdfPrintArea(5,443,461,508,473,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.11-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,331,432,355,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.11-date",     		new PdfPrintArea[]{new PdfPrintArea(5,362,432,386,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.11-comment",  		new PdfPrintArea[]{new PdfPrintArea(5,395,432,526,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.12-passFail",    	new PdfPrintArea[]{new PdfPrintArea(5,331,396,355,421,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.12-date",        	new PdfPrintArea[]{new PdfPrintArea(5,362,396,386,421,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.12-comment",     	new PdfPrintArea[]{new PdfPrintArea(5,395,396,526,421,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.13-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,331,385,355,397,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.13-date", 			new PdfPrintArea[]{new PdfPrintArea(5,362,385,386,397,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.13-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,395,385,526,397,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.14-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,331,356,355,377,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.14-date", 			new PdfPrintArea[]{new PdfPrintArea(5,362,356,386,377,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.14-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,395,356,526,377,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.15-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,331,339,355,353,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.15-date", 			new PdfPrintArea[]{new PdfPrintArea(5,362,339,386,353,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.15-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,395,339,526,353,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2.16-passFail", 		new PdfPrintArea[]{new PdfPrintArea(5,331,300,355,328,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.16-date", 			new PdfPrintArea[]{new PdfPrintArea(5,362,300,386,328,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2.16-comment", 		new PdfPrintArea[]{new PdfPrintArea(5,395,300,526,328,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Page 5 Comments", 		new PdfPrintArea[]{new PdfPrintArea(5,305,180,427,303,10)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 5-sign", 		new PdfPrintArea[]{new PdfPrintArea(5,155,156,246,176,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 5-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(5,165,133,256,146,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-passFail", 						new PdfPrintArea[]{new PdfPrintArea(6,330,464,353,514,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-date",     						new PdfPrintArea[]{new PdfPrintArea(6,361,464,384,514,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-Torque Wrench S/N",     		new PdfPrintArea[]{new PdfPrintArea(6,457,514,524,528,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-Calibration Certificate No", 	new PdfPrintArea[]{new PdfPrintArea(6,476,495,543,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.01-Date of Expiry",				new PdfPrintArea[]{new PdfPrintArea(6,442,479,509,491,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.02-passFail", 		new PdfPrintArea[]{new PdfPrintArea(6,330,465,353,478,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-date", 			new PdfPrintArea[]{new PdfPrintArea(6,361,465,384,478,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.02-comment", 		new PdfPrintArea[]{new PdfPrintArea(6,394,465,524,478,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.03-passFail", 		new PdfPrintArea[]{new PdfPrintArea(6,330,425,353,451,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-date", 			new PdfPrintArea[]{new PdfPrintArea(6,361,425,384,451,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.03-comment", 		new PdfPrintArea[]{new PdfPrintArea(6,394,425,524,451,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.04-passFail", 		new PdfPrintArea[]{new PdfPrintArea(6,330,413,353,426,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-date", 			new PdfPrintArea[]{new PdfPrintArea(6,361,413,384,426,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.04-comment", 		new PdfPrintArea[]{new PdfPrintArea(6,394,413,524,426,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.05-passFail", 		new PdfPrintArea[]{new PdfPrintArea(6,330,383,353,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-date", 			new PdfPrintArea[]{new PdfPrintArea(6,361,383,384,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.05-comment", 		new PdfPrintArea[]{new PdfPrintArea(6,394,383,524,403,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("3.06-passFail", 		new PdfPrintArea[]{new PdfPrintArea(6,330,350,353,375,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-date", 			new PdfPrintArea[]{new PdfPrintArea(6,361,350,384,375,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3.06-comment", 		new PdfPrintArea[]{new PdfPrintArea(6,394,350,524,375,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - End-sign", 		new PdfPrintArea[]{new PdfPrintArea(6,151,187,238,212,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - End-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(6,164,162,240,174,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 TELP - End-sign", 				new PdfPrintArea[]{new PdfPrintArea(6,260,187,347,212,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 TELP - End-signedBy", 			new PdfPrintArea[]{new PdfPrintArea(6,287,162,363,174,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("Operator - End-sign",	 		new PdfPrintArea[]{new PdfPrintArea(6,391,187,478,212,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Operator - End-signedBy", 			new PdfPrintArea[]{new PdfPrintArea(6,417,162,493,174,0)}));
		
		return config;
		
	}
	
	private static PdfTemplatePrintLocationConfig getCable20()
	{
		PdfTemplatePrintLocationConfig config = new PdfTemplatePrintLocationConfig();
		
		config.addItemPrintArea(new ItemPrintAreaDef("From:", new PdfPrintArea[]{new PdfPrintArea(1,146,655,314,670,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("To", new PdfPrintArea[]{new PdfPrintArea(1,350,658,531,671,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Cable Name/No:", new PdfPrintArea[]{new PdfPrintArea(1,147,638,313,652,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("TYPE/SIZE mm:",     				new PdfPrintArea[]{new PdfPrintArea(1,374,639,530,653,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Voltage tested to:", 			new PdfPrintArea[]{new PdfPrintArea(1,170,623,314,635,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CIRCUIT BOOK SHEET NO:", 				new PdfPrintArea[]{new PdfPrintArea(1,414,623,529,635,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Multimeter Serial No:", 	new PdfPrintArea[]{new PdfPrintArea(1,169,602,331,617,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CAL DUE DATE:", 			new PdfPrintArea[]{new PdfPrintArea(1,391,605,528,618,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("INSULATION TESTER SERIAL NO:", 	new PdfPrintArea[]{new PdfPrintArea(1,169,581,329,597,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CAL DUE DATE:", 	new PdfPrintArea[]{new PdfPrintArea(1,391,581,528,597,0,Element.ALIGN_CENTER)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,522,130,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,522,164,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,522,190,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,522,216,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,522,323,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,522,429,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,522,528,529,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,512,130,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,512,164,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,512,190,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,512,216,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,512,323,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,512,429,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,512,528,519,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,502,130,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,502,164,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,502,190,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,502,216,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,502,323,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,502,429,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,502,528,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,492,190,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,492,216,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,492,323,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,492,429,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,492,528,499,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,483,130,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,483,164,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,483,190,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,483,216,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,483,323,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,483,429,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,483,528,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,474,130,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,474,164,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,474,190,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,474,216,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,474,323,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,474,429,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,474,528,481,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("7-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,464,130,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,464,164,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,464,190,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,464,216,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,464,323,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,464,429,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,464,528,471,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("8-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,454,130,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,454,164,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,454,190,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,454,216,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,454,323,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,454,429,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,454,528,461,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("9-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,445,130,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,445,164,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,445,190,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,445,216,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,445,323,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,445,429,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,445,528,452,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("10-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,435,130,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,435,164,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,435,190,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,435,216,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,435,323,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,435,429,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,435,528,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("11-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,425,130,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,425,164,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,425,190,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,425,216,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,425,323,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,425,429,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,425,528,432,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("12-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,415,130,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,415,164,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,415,190,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,415,216,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,415,323,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,415,429,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,415,528,422,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("13-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,406,130,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,406,164,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,406,190,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,406,216,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,406,323,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,406,429,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,406,528,413,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("14-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,396,130,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,396,164,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,396,190,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,396,216,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,396,323,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,396,429,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,396,528,403,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("15-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,387,130,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,387,164,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,387,190,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,387,216,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,387,323,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,387,429,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,387,528,394,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("16-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,377,130,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,377,164,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,377,190,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,377,216,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,377,323,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,377,429,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,377,528,384,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("17-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,367,130,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,367,164,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,367,190,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,367,216,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,367,323,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,367,429,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,367,528,374,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("18-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,357,130,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,357,164,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,357,190,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,357,216,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,357,323,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,357,429,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,357,528,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("19-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,347,130,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,347,164,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,347,190,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,347,216,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,347,323,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,347,429,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,347,528,354,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("20-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,338,130,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,338,164,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,338,190,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,338,216,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,338,323,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,338,429,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,338,528,345,0)}));

				
		config.addItemPrintArea(new ItemPrintAreaDef("P1.1-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,185,317,204,327,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.1-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,224,317,243,327,0)}));
		//config.addItemPrintArea(new ItemPrintAreaDef("P1.1-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,488,522,502,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,174,291,200,303,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,215,292,241,304,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,256,288,529,314,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,173,240,199,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,213,250,239,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,256,228,529,254,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,74,140,185,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,77,101,231,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,251,140,362,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,250,101,404,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,406,140,517,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,406,101,533,113,0)}));
		
		return config;
		
	}


	private static PdfTemplatePrintLocationConfig getCable40()
	{
		PdfTemplatePrintLocationConfig config = new PdfTemplatePrintLocationConfig();
		
		config.addItemPrintArea(new ItemPrintAreaDef("From:", new PdfPrintArea[]{new PdfPrintArea(1,146,655,314,670,0,Element.ALIGN_CENTER), new PdfPrintArea(2,146,655,314,670,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("To:", new PdfPrintArea[]{new PdfPrintArea(1,350,658,531,671,0,Element.ALIGN_CENTER), new PdfPrintArea(2,350,658,531,671,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Cable Name/No:", new PdfPrintArea[]{new PdfPrintArea(1,147,638,313,652,0,Element.ALIGN_CENTER), new PdfPrintArea(2,147,638,313,652,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("TYPE/SIZE mm:",     				new PdfPrintArea[]{new PdfPrintArea(1,374,639,530,653,0,Element.ALIGN_CENTER), new PdfPrintArea(2,374,639,530,653,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Voltage tested to:", 			new PdfPrintArea[]{new PdfPrintArea(1,170,623,314,635,0,Element.ALIGN_CENTER), new PdfPrintArea(2,170,623,314,635,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CIRCUIT BOOK SHEET NO:", 				new PdfPrintArea[]{new PdfPrintArea(1,414,623,529,635,0,Element.ALIGN_CENTER), new PdfPrintArea(2,414,623,529,635,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Multimeter Serial No:", 	new PdfPrintArea[]{new PdfPrintArea(1,169,602,331,617,0,Element.ALIGN_CENTER), new PdfPrintArea(2,169,602,331,617,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CAL DUE DATE:", 			new PdfPrintArea[]{new PdfPrintArea(1,391,605,528,618,0,Element.ALIGN_CENTER), new PdfPrintArea(2,391,605,528,618,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("INSULATION TESTER SERIAL NO:", 	new PdfPrintArea[]{new PdfPrintArea(1,169,581,329,597,0,Element.ALIGN_CENTER), new PdfPrintArea(2,169,581,329,597,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CAL DUE DATE:", 	new PdfPrintArea[]{new PdfPrintArea(1,391,581,528,597,0,Element.ALIGN_CENTER), new PdfPrintArea(2,391,581,528,597,0,Element.ALIGN_CENTER)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,522,130,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,522,164,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,522,190,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,522,216,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,522,323,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,522,429,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,522,528,529,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,512,130,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,512,164,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,512,190,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,512,216,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,512,323,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,512,429,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,512,528,519,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,502,130,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,502,164,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,502,190,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,502,216,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,502,323,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,502,429,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,502,528,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,492,190,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,492,216,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,492,323,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,492,429,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,492,528,499,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,483,130,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,483,164,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,483,190,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,483,216,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,483,323,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,483,429,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,483,528,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,474,130,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,474,164,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,474,190,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,474,216,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,474,323,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,474,429,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,474,528,481,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("7-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,464,130,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,464,164,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,464,190,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,464,216,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,464,323,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,464,429,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,464,528,471,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("8-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,454,130,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,454,164,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,454,190,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,454,216,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,454,323,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,454,429,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,454,528,461,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("9-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,445,130,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,445,164,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,445,190,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,445,216,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,445,323,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,445,429,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,445,528,452,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("10-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,435,130,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,435,164,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,435,190,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,435,216,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,435,323,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,435,429,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,435,528,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("11-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,425,130,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,425,164,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,425,190,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,425,216,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,425,323,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,425,429,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,425,528,432,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("12-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,415,130,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,415,164,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,415,190,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,415,216,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,415,323,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,415,429,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,415,528,422,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("13-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,406,130,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,406,164,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,406,190,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,406,216,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,406,323,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,406,429,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,406,528,413,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("14-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,396,130,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,396,164,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,396,190,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,396,216,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,396,323,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,396,429,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,396,528,403,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("15-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,387,130,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,387,164,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,387,190,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,387,216,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,387,323,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,387,429,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,387,528,394,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("16-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,377,130,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,377,164,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,377,190,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,377,216,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,377,323,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,377,429,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,377,528,384,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("17-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,367,130,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,367,164,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,367,190,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,367,216,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,367,323,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,367,429,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,367,528,374,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("18-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,357,130,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,357,164,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,357,190,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,357,216,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,357,323,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,357,429,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,357,528,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("19-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,347,130,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,347,164,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,347,190,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,347,216,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,347,323,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,347,429,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,347,528,354,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("20-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,338,130,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,338,164,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,338,190,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,338,216,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,338,323,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,338,429,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,338,528,345,0)}));

				
		config.addItemPrintArea(new ItemPrintAreaDef("P1.1-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,185,317,204,327,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.1-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,224,317,243,327,0)}));
		//config.addItemPrintArea(new ItemPrintAreaDef("P1.1-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,488,522,502,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,174,291,200,303,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,215,292,241,304,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,256,288,529,314,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,173,240,199,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,213,250,239,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,256,228,529,254,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,74,140,185,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,77,101,231,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,251,140,362,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,250,101,404,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,406,140,517,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,406,101,533,113,0)}));
		

		
		config.addItemPrintArea(new ItemPrintAreaDef("21-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,522,130,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,522,164,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,522,190,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,522,216,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,522,323,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,522,429,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,522,528,529,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("22-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,512,130,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,512,164,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,512,190,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,512,216,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,512,323,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,512,429,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,512,528,519,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("23-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,502,130,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,502,164,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,502,190,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,502,216,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,502,323,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,502,429,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,502,528,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("24-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,492,190,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,492,216,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,492,323,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,492,429,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,492,528,499,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("25-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,483,130,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,483,164,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,483,190,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,483,216,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,483,323,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,483,429,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,483,528,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("26-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,474,130,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,474,164,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,474,190,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,474,216,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,474,323,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,474,429,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,474,528,481,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("27-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,464,130,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,464,164,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,464,190,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,464,216,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,464,323,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,464,429,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,464,528,471,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("28-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,454,130,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,454,164,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,454,190,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,454,216,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,454,323,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,454,429,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,454,528,461,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("29-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,445,130,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,445,164,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,445,190,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,445,216,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,445,323,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,445,429,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,445,528,452,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("30-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,435,130,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,435,164,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,435,190,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,435,216,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,435,323,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,435,429,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,435,528,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("31-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,425,130,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,425,164,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,425,190,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,425,216,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,425,323,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,425,429,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,425,528,432,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("32-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,415,130,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,415,164,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,415,190,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,415,216,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,415,323,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,415,429,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,415,528,422,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("33-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,406,130,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,406,164,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,406,190,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,406,216,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,406,323,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,406,429,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,406,528,413,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("34-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,396,130,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,396,164,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,396,190,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,396,216,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,396,323,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,396,429,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,396,528,403,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("35-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,387,130,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,387,164,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,387,190,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,387,216,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,387,323,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,387,429,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,387,528,394,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("36-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,377,130,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,377,164,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,377,190,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,377,216,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,377,323,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,377,429,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,377,528,384,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("37-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,367,130,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,367,164,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,367,190,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,367,216,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,367,323,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,367,429,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,367,528,374,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("38-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,357,130,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,357,164,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,357,190,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,357,216,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,357,323,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,357,429,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,357,528,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("39-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,347,130,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,347,164,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,347,190,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,347,216,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,347,323,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,347,429,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,347,528,354,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("40-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,338,130,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,338,164,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,338,190,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,338,216,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,338,323,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,338,429,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,338,528,345,0)}));
				
		config.addItemPrintArea(new ItemPrintAreaDef("P2.1-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,185,317,204,327,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.1-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,224,317,243,327,0)}));
		//config.addItemPrintArea(new ItemPrintAreaDef("P2.1-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,393,488,522,502,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P2.2-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,174,291,200,303,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.2-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,215,292,241,304,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.2-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,256,288,529,314,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P2.3-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,173,240,199,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.3-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,213,250,239,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.3-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,256,228,529,254,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,74,140,185,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,77,101,231,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,251,140,362,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,250,101,404,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,406,140,517,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,406,101,533,113,0)}));
		
		return config;
		
	}


	private static PdfTemplatePrintLocationConfig getCable60()
	{
		PdfTemplatePrintLocationConfig config = new PdfTemplatePrintLocationConfig();
		
		config.addItemPrintArea(new ItemPrintAreaDef("From:", new PdfPrintArea[]{new PdfPrintArea(1,146,655,314,670,0,Element.ALIGN_CENTER), new PdfPrintArea(2,146,655,314,670,0,Element.ALIGN_CENTER), new PdfPrintArea(3,146,655,314,670,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("To", new PdfPrintArea[]{new PdfPrintArea(1,350,658,531,671,0,Element.ALIGN_CENTER), new PdfPrintArea(2,350,658,531,671,0,Element.ALIGN_CENTER), new PdfPrintArea(3,350,658,531,671,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Cable Name/No:", new PdfPrintArea[]{new PdfPrintArea(1,147,638,313,652,0,Element.ALIGN_CENTER), new PdfPrintArea(2,147,638,313,652,0,Element.ALIGN_CENTER), new PdfPrintArea(3,147,638,313,652,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("TYPE/SIZE mm:",     				new PdfPrintArea[]{new PdfPrintArea(1,374,639,530,653,0,Element.ALIGN_CENTER), new PdfPrintArea(2,374,639,530,653,0,Element.ALIGN_CENTER), new PdfPrintArea(3,374,639,530,653,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Voltage tested to:", 			new PdfPrintArea[]{new PdfPrintArea(1,170,623,314,635,0,Element.ALIGN_CENTER), new PdfPrintArea(2,170,623,314,635,0,Element.ALIGN_CENTER), new PdfPrintArea(3,170,623,314,635,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CIRCUIT BOOK SHEET NO:", 				new PdfPrintArea[]{new PdfPrintArea(1,414,623,529,635,0,Element.ALIGN_CENTER), new PdfPrintArea(2,414,623,529,635,0,Element.ALIGN_CENTER), new PdfPrintArea(3,414,623,529,635,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("Multimeter Serial No:", 	new PdfPrintArea[]{new PdfPrintArea(1,169,602,331,617,0,Element.ALIGN_CENTER), new PdfPrintArea(2,169,602,331,617,0,Element.ALIGN_CENTER), new PdfPrintArea(3,169,602,331,617,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CAL DUE DATE:", 			new PdfPrintArea[]{new PdfPrintArea(1,391,605,528,618,0,Element.ALIGN_CENTER), new PdfPrintArea(2,391,605,528,618,0,Element.ALIGN_CENTER), new PdfPrintArea(3,391,605,528,618,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("INSULATION TESTER SERIAL NO:", 	new PdfPrintArea[]{new PdfPrintArea(1,169,581,329,597,0,Element.ALIGN_CENTER), new PdfPrintArea(2,169,581,329,597,0,Element.ALIGN_CENTER), new PdfPrintArea(3,169,581,329,597,0,Element.ALIGN_CENTER)}));
		config.addItemPrintArea(new ItemPrintAreaDef("CAL DUE DATE:", 	new PdfPrintArea[]{new PdfPrintArea(1,391,581,528,597,0,Element.ALIGN_CENTER), new PdfPrintArea(2,391,581,528,597,0,Element.ALIGN_CENTER), new PdfPrintArea(3,391,581,528,597,0,Element.ALIGN_CENTER)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("1-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,522,130,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,522,164,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,522,190,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,522,216,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,522,323,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,522,429,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("1-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,522,528,529,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("2-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,512,130,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,512,164,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,512,190,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,512,216,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,512,323,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,512,429,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("2-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,512,528,519,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("3-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,502,130,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,502,164,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,502,190,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,502,216,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,502,323,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,502,429,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("3-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,502,528,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("4-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,492,190,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,492,216,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,492,323,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,492,429,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("4-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,492,528,499,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("5-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,483,130,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,483,164,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,483,190,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,483,216,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,483,323,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,483,429,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("5-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,483,528,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("6-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,474,130,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,474,164,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,474,190,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,474,216,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,474,323,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,474,429,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("6-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,474,528,481,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("7-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,464,130,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,464,164,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,464,190,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,464,216,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,464,323,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,464,429,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("7-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,464,528,471,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("8-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,454,130,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,454,164,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,454,190,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,454,216,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,454,323,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,454,429,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("8-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,454,528,461,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("9-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,445,130,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,445,164,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,445,190,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,445,216,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,445,323,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,445,429,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("9-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,445,528,452,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("10-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,435,130,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,435,164,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,435,190,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,435,216,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,435,323,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,435,429,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("10-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,435,528,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("11-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,425,130,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,425,164,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,425,190,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,425,216,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,425,323,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,425,429,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("11-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,425,528,432,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("12-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,415,130,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,415,164,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,415,190,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,415,216,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,415,323,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,415,429,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("12-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,415,528,422,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("13-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,406,130,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,406,164,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,406,190,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,406,216,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,406,323,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,406,429,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("13-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,406,528,413,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("14-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,396,130,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,396,164,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,396,190,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,396,216,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,396,323,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,396,429,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("14-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,396,528,403,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("15-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,387,130,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,387,164,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,387,190,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,387,216,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,387,323,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,387,429,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("15-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,387,528,394,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("16-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,377,130,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,377,164,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,377,190,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,377,216,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,377,323,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,377,429,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("16-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,377,528,384,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("17-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,367,130,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,367,164,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,367,190,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,367,216,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,367,323,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,367,429,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("17-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,367,528,374,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("18-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,357,130,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,357,164,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,357,190,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,357,216,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,357,323,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,357,429,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("18-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,357,528,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("19-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,347,130,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,347,164,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,347,190,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,347,216,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,347,323,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,347,429,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("19-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,347,528,354,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("20-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(1,102,338,130,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(1,136,338,164,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,170,338,190,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,196,338,216,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,223,338,323,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(1,329,338,429,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("20-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,436,338,528,345,0)}));

				
		config.addItemPrintArea(new ItemPrintAreaDef("P1.1-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,185,317,204,327,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.1-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,224,317,243,327,0)}));
		//config.addItemPrintArea(new ItemPrintAreaDef("P1.1-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,393,488,522,502,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,174,291,200,303,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,215,292,241,304,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.2-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,256,288,529,314,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-pass", 	new PdfPrintArea[]{new PdfPrintArea(1,173,240,199,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-fail", 	new PdfPrintArea[]{new PdfPrintArea(1,213,250,239,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P1.3-comment", 	new PdfPrintArea[]{new PdfPrintArea(1,256,228,529,254,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,74,140,185,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,77,101,231,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,251,140,362,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,250,101,404,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-sign", 		new PdfPrintArea[]{new PdfPrintArea(1,406,140,517,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 1-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(1,406,101,533,113,0)}));
		

		
		config.addItemPrintArea(new ItemPrintAreaDef("21-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,522,130,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,522,164,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,522,190,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,522,216,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,522,323,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,522,429,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("21-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,522,528,529,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("22-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,512,130,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,512,164,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,512,190,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,512,216,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,512,323,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,512,429,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("22-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,512,528,519,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("23-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,502,130,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,502,164,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,502,190,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,502,216,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,502,323,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,502,429,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("23-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,502,528,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("24-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,492,190,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,492,216,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,492,323,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,492,429,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("24-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,492,528,499,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("25-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,483,130,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,483,164,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,483,190,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,483,216,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,483,323,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,483,429,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("25-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,483,528,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("26-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,474,130,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,474,164,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,474,190,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,474,216,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,474,323,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,474,429,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("26-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,474,528,481,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("27-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,464,130,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,464,164,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,464,190,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,464,216,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,464,323,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,464,429,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("27-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,464,528,471,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("28-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,454,130,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,454,164,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,454,190,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,454,216,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,454,323,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,454,429,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("28-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,454,528,461,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("29-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,445,130,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,445,164,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,445,190,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,445,216,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,445,323,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,445,429,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("29-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,445,528,452,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("30-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,435,130,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,435,164,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,435,190,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,435,216,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,435,323,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,435,429,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("30-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,435,528,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("31-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,425,130,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,425,164,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,425,190,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,425,216,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,425,323,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,425,429,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("31-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,425,528,432,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("32-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,415,130,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,415,164,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,415,190,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,415,216,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,415,323,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,415,429,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("32-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,415,528,422,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("33-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,406,130,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,406,164,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,406,190,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,406,216,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,406,323,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,406,429,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("33-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,406,528,413,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("34-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,396,130,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,396,164,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,396,190,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,396,216,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,396,323,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,396,429,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("34-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,396,528,403,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("35-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,387,130,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,387,164,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,387,190,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,387,216,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,387,323,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,387,429,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("35-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,387,528,394,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("36-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,377,130,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,377,164,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,377,190,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,377,216,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,377,323,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,377,429,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("36-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,377,528,384,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("37-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,367,130,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,367,164,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,367,190,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,367,216,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,367,323,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,367,429,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("37-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,367,528,374,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("38-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,357,130,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,357,164,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,357,190,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,357,216,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,357,323,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,357,429,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("38-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,357,528,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("39-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,347,130,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,347,164,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,347,190,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,347,216,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,347,323,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,347,429,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("39-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,347,528,354,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("40-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(2,102,338,130,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(2,136,338,164,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,170,338,190,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,196,338,216,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,223,338,323,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(2,329,338,429,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("40-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,436,338,528,345,0)}));
				
		config.addItemPrintArea(new ItemPrintAreaDef("P2.1-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,185,317,204,327,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.1-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,224,317,243,327,0)}));
		//config.addItemPrintArea(new ItemPrintAreaDef("P2.1-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,393,488,522,502,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P2.2-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,174,291,200,303,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.2-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,215,292,241,304,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.2-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,256,288,529,314,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P2.3-pass", 	new PdfPrintArea[]{new PdfPrintArea(2,173,240,199,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.3-fail", 	new PdfPrintArea[]{new PdfPrintArea(2,213,250,239,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P2.3-comment", 	new PdfPrintArea[]{new PdfPrintArea(2,256,228,529,254,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,74,140,185,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,77,101,231,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,251,140,362,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,250,101,404,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-sign", 		new PdfPrintArea[]{new PdfPrintArea(2,406,140,517,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 2-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(2,406,101,533,113,0)}));
		
		
		
		config.addItemPrintArea(new ItemPrintAreaDef("41-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,522,130,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("41-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,522,164,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("41-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,522,190,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("41-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,522,216,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("41-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,522,323,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("41-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,522,429,529,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("41-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,522,528,529,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("42-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,512,130,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("42-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,512,164,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("42-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,512,190,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("42-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,512,216,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("42-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,512,323,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("42-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,512,429,519,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("42-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,512,528,519,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("43-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,502,130,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("43-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,502,164,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("43-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,502,190,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("43-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,502,216,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("43-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,502,323,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("43-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,502,429,509,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("43-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,502,528,509,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("44-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("44-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,102,492,130,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("44-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,492,190,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("44-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,492,216,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("44-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,492,323,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("44-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,492,429,499,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("44-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,492,528,499,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("45-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,483,130,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("45-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,483,164,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("45-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,483,190,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("45-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,483,216,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("45-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,483,323,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("45-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,483,429,490,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("45-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,483,528,490,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("46-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,474,130,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("46-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,474,164,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("46-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,474,190,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("46-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,474,216,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("46-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,474,323,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("46-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,474,429,481,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("46-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,474,528,481,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("47-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,464,130,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("47-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,464,164,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("47-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,464,190,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("47-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,464,216,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("47-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,464,323,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("47-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,464,429,471,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("47-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,464,528,471,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("48-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,454,130,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("48-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,454,164,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("48-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,454,190,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("48-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,454,216,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("48-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,454,323,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("48-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,454,429,461,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("48-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,454,528,461,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("49-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,445,130,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("49-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,445,164,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("49-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,445,190,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("49-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,445,216,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("49-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,445,323,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("49-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,445,429,452,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("49-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,445,528,452,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("50-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,435,130,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("50-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,435,164,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("50-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,435,190,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("50-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,435,216,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("50-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,435,323,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("50-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,435,429,442,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("50-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,435,528,442,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("51-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,425,130,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("51-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,425,164,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("51-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,425,190,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("51-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,425,216,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("51-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,425,323,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("51-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,425,429,432,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("51-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,425,528,432,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("52-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,415,130,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("52-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,415,164,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("52-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,415,190,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("52-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,415,216,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("52-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,415,323,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("52-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,415,429,422,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("52-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,415,528,422,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("53-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,406,130,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("53-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,406,164,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("53-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,406,190,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("53-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,406,216,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("53-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,406,323,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("53-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,406,429,413,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("53-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,406,528,413,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("54-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,396,130,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("54-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,396,164,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("54-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,396,190,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("54-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,396,216,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("54-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,396,323,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("54-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,396,429,403,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("54-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,396,528,403,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("55-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,387,130,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("55-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,387,164,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("55-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,387,190,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("55-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,387,216,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("55-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,387,323,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("55-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,387,429,394,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("55-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,387,528,394,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("56-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,377,130,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("56-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,377,164,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("56-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,377,190,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("56-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,377,216,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("56-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,377,323,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("56-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,377,429,384,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("56-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,377,528,384,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("57-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,367,130,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("57-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,367,164,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("57-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,367,190,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("57-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,367,216,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("57-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,367,323,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("57-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,367,429,374,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("57-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,367,528,374,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("58-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,357,130,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("58-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,357,164,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("58-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,357,190,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("58-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,357,216,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("58-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,357,323,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("58-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,357,429,364,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("58-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,357,528,364,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("59-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,347,130,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("59-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,347,164,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("59-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,347,190,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("59-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,347,216,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("59-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,347,323,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("59-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,347,429,354,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("59-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,347,528,354,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("60-Terminal No From", 	new PdfPrintArea[]{new PdfPrintArea(3,102,338,130,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("60-Terminal No From", 		new PdfPrintArea[]{new PdfPrintArea(3,136,338,164,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("60-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,170,338,190,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("60-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,196,338,216,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("60-CORE/CORE MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,223,338,323,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("60-CORE/EARTH MΩ", 	new PdfPrintArea[]{new PdfPrintArea(3,329,338,429,345,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("60-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,436,338,528,345,0)}));

				
		config.addItemPrintArea(new ItemPrintAreaDef("P3.1-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,185,317,204,327,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P3.1-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,224,317,243,327,0)}));
		//config.addItemPrintArea(new ItemPrintAreaDef("P3.1-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,393,488,522,502,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P3.2-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,174,291,200,303,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P3.2-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,215,292,241,304,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P3.2-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,256,288,529,314,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("P3.3-pass", 	new PdfPrintArea[]{new PdfPrintArea(3,173,240,199,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P3.3-fail", 	new PdfPrintArea[]{new PdfPrintArea(3,213,250,239,264,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("P3.3-comment", 	new PdfPrintArea[]{new PdfPrintArea(3,256,228,529,254,0)}));

		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-sign", 		new PdfPrintArea[]{new PdfPrintArea(3,74,140,185,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(3,77,101,231,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-sign", 		new PdfPrintArea[]{new PdfPrintArea(3,251,140,362,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(3,250,101,404,113,0)}));
		
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-sign", 		new PdfPrintArea[]{new PdfPrintArea(3,406,140,517,155,0)}));
		config.addItemPrintArea(new ItemPrintAreaDef("T252 Site Engineer - Page 3-signedBy", 	new PdfPrintArea[]{new PdfPrintArea(3,406,101,533,113,0)}));
		

		
		
		return config;
		
	}
}
