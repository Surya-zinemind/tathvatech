package com.tathvatech.pdf.common;

public class ItemPrintAreaDef
{
	String identifier;
	boolean isIdentifierEncoded = false;
	PdfPrintArea[] printAreas;
	
	public ItemPrintAreaDef()
	{
		super();
	}
	public ItemPrintAreaDef(String identifier, PdfPrintArea[] printAreas)
	{
		super();
		this.identifier = identifier;
		this.printAreas = printAreas;
	}
	public String getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	public boolean isIdentifierEncoded()
	{
		return isIdentifierEncoded;
	}
	public void setIdentifierEncoded(boolean isIdentifierEncoded)
	{
		this.isIdentifierEncoded = isIdentifierEncoded;
	}
	public PdfPrintArea[] getPrintAreas()
	{
		return printAreas;
	}
	public void setPrintAreas(PdfPrintArea[] printAreas)
	{
		this.printAreas = printAreas;
	} 
}
