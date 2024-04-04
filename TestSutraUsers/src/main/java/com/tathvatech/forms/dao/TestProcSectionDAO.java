package com.tathvatech.forms.dao;

import java.util.Date;
import java.util.List;

import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.project.FormSectionOID;
import com.tathvatech.ts.core.project.TestProcOID;
import com.tathvatech.ts.core.project.TestProcSectionOID;
import com.tathvatech.ts.core.project.TestProcSectionObj;
import com.thirdi.surveyside.utils.DateUtils;

public class TestProcSectionDAO
{
	Date now;
	public TestProcSectionDAO()
	{
		now = DateUtils.getNowDateForEffectiveDateFrom();
	}

	/**
	 * Not tested code.
	 * @param testProcOID
	 * @return
	 */
	public List<TestProcSectionObj> getTestProcSections(TestProcOID testProcOID)
	{
		StringBuilder sb = new StringBuilder(fetchSql);
		sb.append(" join testproc_form_assign tfa on tfs.testprocFormAssignFk = tfa.pk and tfa.current = 1 ");
		sb.append(" and tfa.testProcFk = ?");
		return PersistWrapper.readList(TestProcSectionObj.class, sb.toString(), testProcOID.getPk());
	}

	public TestProcSectionObj getTestProcSection(TestProcSectionOID testProcSectionOID)
	{
		return PersistWrapper.read(TestProcSectionObj.class, fetchSql + " where 1 = 1 and tfs.pk = ? ", 
				testProcSectionOID.getPk());
	}

	public TestProcSectionObj getTestProcSection(TestProcOID testProcOID, FormSectionOID formSectionOID)
	{
		StringBuilder sb = new StringBuilder(fetchSql);
		sb.append(" join testproc_form_assign tfa on tfs.testprocFormAssignFk = tfa.pk and tfa.current = 1 ");
		sb.append(" and tfa.testProcFk = ? and tfs.formSectionFk = ? ");
		return PersistWrapper.read(TestProcSectionObj.class, sb.toString(), 
				testProcOID.getPk(), formSectionOID.getPk());
	}

	private static String fetchSql = "select tfs.* from testproc_form_section tfs ";	
}
