package com.tathvatech.pdf.common;

public class PdfPrintArea
{
	public int pageNo, left, width, top, height, leading, hAlign, vAlign;

	public PdfPrintArea()
	{
		super();
	}
	public PdfPrintArea(int pageNo, int left, int width, int top, int height, int leading)
	{
		this(pageNo, left, width, top, height, leading, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM);
	}
	public PdfPrintArea(int pageNo, int left, int width, int top, int height, int leading, int hAlign, int vAlign)
	{
		super();
		this.pageNo = pageNo;
		this.left = left;
		this.width = width;
		this.top = top;
		this.height = height;
		this.leading = leading;
		this.hAlign = hAlign;
		this.vAlign = vAlign;
	}

	public int getPageNo()
	{
		return pageNo;
	}
	public void setPageNo(int pageNo)
	{
		this.pageNo = pageNo;
	}
	public int getLeft()
	{
		return left;
	}
	public void setLeft(int left)
	{
		this.left = left;
	}
	public int getTop()
	{
		return top;
	}
	public void setTop(int top)
	{
		this.top = top;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public int gethAlign()
	{
		return hAlign;
	}
	public void sethAlign(int hAlign)
	{
		this.hAlign = hAlign;
	}
	public int getvAlign()
	{
		return vAlign;
	}
	public void setvAlign(int vAlign)
	{
		this.vAlign = vAlign;
	}
	public int getLeading()
	{
		return leading;
	}
	public void setLeading(int leading)
	{
		this.leading = leading;
	}
}
