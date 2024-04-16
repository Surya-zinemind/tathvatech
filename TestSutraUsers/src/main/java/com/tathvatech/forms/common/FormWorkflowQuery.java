package com.tathvatech.forms.common;

import java.util.Date;

import net.sf.persist.annotations.NoTable;

@NoTable
public class FormWorkflowQuery
{

    private int formWorkflowPk;
    private int testProcPk;
    private Date date;
    private int performedBy;
    private String performedByFirstName;
    private String performedByLastName;
    private int unitPk;
    private String unitName;
    private int workstationPk;
    private String workstationName;
    private String workstationDescription;
    private int formPk;
    private String formIdentityNumber;
    private String formDescription;
    private int formVersionNo;
    private int responseId;
    private String action;
    private String resultStatus;
    private String comments;

    
    public int getFormWorkflowPk()
	{
		return formWorkflowPk;
	}
	public void setFormWorkflowPk(int formWorkflowPk)
	{
		this.formWorkflowPk = formWorkflowPk;
	}
	public int getTestProcPk() {
		return testProcPk;
	}
	public void setTestProcPk(int testProcPk) {
		this.testProcPk = testProcPk;
	}
	public String getUnitName()
	{
		return unitName;
	}
	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}
	public String getWorkstationName()
	{
		return workstationName;
	}
	public void setWorkstationName(String workstationName)
	{
		this.workstationName = workstationName;
	}
	public String getWorkstationDescription()
	{
		return workstationDescription;
	}
	public void setWorkstationDescription(String workstationDescription)
	{
		this.workstationDescription = workstationDescription;
	}
	public String getFormIdentityNumber()
	{
		return formIdentityNumber;
	}
	public void setFormIdentityNumber(String formIdentityNumber)
	{
		this.formIdentityNumber = formIdentityNumber;
	}
	public String getFormDescription()
	{
		return formDescription;
	}
	public void setFormDescription(String formDescription)
	{
		this.formDescription = formDescription;
	}
	public int getFormVersionNo()
	{
		return formVersionNo;
	}
	public void setFormVersionNo(int formVersionNo)
	{
		this.formVersionNo = formVersionNo;
	}
	public Date getDate()
    {
        return date;
    }
    public void setDate(Date date)
    {
        this.date = date;
    }
    public int getPerformedBy()
    {
        return performedBy;
    }
    public void setPerformedBy(int performedBy)
    {
        this.performedBy = performedBy;
    }
    public String getPerformedByFirstName()
	{
		return performedByFirstName;
	}
	public void setPerformedByFirstName(String performedByFirstName)
	{
		this.performedByFirstName = performedByFirstName;
	}
	public String getPerformedByLastName()
	{
		return performedByLastName;
	}
	public void setPerformedByLastName(String performedByLastName)
	{
		this.performedByLastName = performedByLastName;
	}
	public int getUnitPk()
    {
        return unitPk;
    }
    public void setUnitPk(int unitPk)
    {
        this.unitPk = unitPk;
    }
    public int getWorkstationPk()
    {
        return workstationPk;
    }
    public void setWorkstationPk(int workstationPk)
    {
        this.workstationPk = workstationPk;
    }
    public int getFormPk()
    {
        return formPk;
    }
    public void setFormPk(int formPk)
    {
        this.formPk = formPk;
    }
    public int getResponseId()
    {
        return responseId;
    }
    public void setResponseId(int responseId)
    {
        this.responseId = responseId;
    }
    public String getAction()
    {
        return action;
    }
    public void setAction(String action)
    {
        this.action = action;
    }
    public String getResultStatus()
    {
        return resultStatus;
    }
    public void setResultStatus(String resultStatus)
    {
        this.resultStatus = resultStatus;
    }
    public String getComments()
    {
        return comments;
    }
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    
    public static String sql = "select fwf.pk as formWorkflowPk, fwf.testProcPk as testProcPk, fwf.date, fwf.performedBy, " +
    		"user.firstName as performedByFirstName, user.lastName as performedByLastName, uth.unitPk, uh.unitName as unitName, uth.workstationPk, " +
    		"workstation.workstationName, workstation.description as workstationDescription, tfa.formFk as formPk, " +
    		"form.identityNumber as formIdentityNumber, form.description as formDescription, " +
    		"form.versionNo as formVersionNo, fwf.responseId, fwf.action, fwf.resultStatus, " +
    		"fwf.comments from TAB_FORM_WORKFLOW fwf"
    		+ " join unit_testproc ut on fwf.testProcPk = ut.pk"
			+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
    		+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
    		+ " join TAB_UNIT u on uth.unitPk=u.pk "
    		+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
    		+ " join TAB_WORKSTATION workstation on uth.workstationPk=workstation.pk "
    		+ " join TAB_SURVEY form on tfa.formFk = form.pk and form.formType = 1"
    		+ " join TAB_USER user on fwf.performedBy = user.pk "
    		+ " where 1 = 1 ";
}


