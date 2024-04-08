package com.tathvatech.ncr.enums;


public class NcrEnum {

	public static String TEMPLATE_FILE_8D_PPS = "ncr_8d_pps_template.xlsx";
	
	public enum NcrGroupListView {
	    MILESTONEVIEW
	}

	public enum NcrUnitItemMode {
		ADD, UPDATE
	}

	public enum NcrFunctionMode {
		EDIT, VIEW,INSPECTIONLISTVIEW
	}

	public enum NcrFunctionAction {
		CURRENT, NEW
	}

	public enum NcrGroupStatus {
		DRAFT, PUBLISHED, APPROVED, REJECTED, COMPLETED, CLOSED , CANCELLED
	}

	public enum NcrItemStatus {
		DRAFT, PUBLISHED, APPROVED, REJECTED, COMPLETED, CLOSED, CANCELLED
	}

	public enum NcrFunctionStatus {
		APPROVED, REJECTED
	}

	public enum NcrNewFormTemplateView{
		NCRGROUP,NCRITEM
	}
	public static enum NcrCorrectiveStatusEnum {

		CORRECTIVECOMPLETED("Completed"), CORRECTIVEREOPEN("Reopened"),OPEN("Open");

		private final String ncrDisplayName;

		NcrCorrectiveStatusEnum(String name)
		{
			this.ncrDisplayName = name;
		}

		public String displayName()
		{
			return this.ncrDisplayName;
		}

		public static NcrCorrectiveStatusEnum fromDisplayString(String displayString)
		{
			NcrCorrectiveStatusEnum[] vals = NcrCorrectiveStatusEnum.values();
			for (int i = 0; i < vals.length; i++)
			{
				if (vals[i].ncrDisplayName.equalsIgnoreCase(displayString))
					return vals[i];
			}
			return null;
		}

		public static String getEnumByString(String code)
		{
			for (NcrCorrectiveStatusEnum e : NcrCorrectiveStatusEnum.values())
			{
				if (code.equals(e.name()))
					return e.displayName();
			}
			return null;
		}

	}
}
