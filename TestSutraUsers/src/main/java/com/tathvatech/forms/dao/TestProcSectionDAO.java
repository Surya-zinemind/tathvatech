package com.tathvatech.forms.dao;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.TestProcSectionObj;
import com.tathvatech.forms.oid.TestProcSectionOID;
import com.tathvatech.user.OID.FormSectionOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.utils.DateUtils;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;


public class TestProcSectionDAO
{
	private final PersistWrapper persistWrapper;
	Date now;
	public TestProcSectionDAO(PersistWrapper persistWrapper)
	{
        this.persistWrapper = persistWrapper;
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
		return persistWrapper.readList(TestProcSectionObj.class, sb.toString(), testProcOID.getPk());
	}

	public TestProcSectionObj getTestProcSection(TestProcSectionOID testProcSectionOID)
	{
		return persistWrapper.read(TestProcSectionObj.class, fetchSql + " where 1 = 1 and tfs.pk = ? ",
				testProcSectionOID.getPk());
	}

	public TestProcSectionObj getTestProcSection(TestProcOID testProcOID, FormSectionOID formSectionOID)
	{
		StringBuilder sb = new StringBuilder(fetchSql);
		sb.append(" join testproc_form_assign tfa on tfs.testprocFormAssignFk = tfa.pk and tfa.current = 1 ");
		sb.append(" and tfa.testProcFk = ? and tfs.formSectionFk = ? ");
		return persistWrapper.read(TestProcSectionObj.class, sb.toString(),
				testProcOID.getPk(), formSectionOID.getPk());
	}

	private static String fetchSql = "select tfs.* from testproc_form_section tfs ";	
}
