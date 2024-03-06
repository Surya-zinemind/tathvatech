package com.tathvatech.unit.service;

import java.util.Date;

import com.tathvatech.ts.caf.db.PersistWrapper;

public class UnitManagerCore
{
	public static UnitInProject getUnitInProject(UnitOID unitOID, ProjectOID projectOID)
	{
		return PersistWrapper.read(UnitInProject.class, 
				"select * from unit_project_ref where unitPk = ? and projectPk = ?", unitOID.getPk(), projectOID.getPk());
	}

	public static UnitInProjectH getUnitInProjectH(UnitInProjectOID unitInProjectOID, Date date)
	{
		return PersistWrapper.read(UnitInProjectH.class, 
				"select * from unit_project_ref_h where unitInProjectPk = ? and ? between effectiveDateFrom and effectiveDateTo", unitInProjectOID.getPk(), date);
	}

	public static UnitH getUnitH(UnitOID unitOID, Date date)
	{
		return PersistWrapper.read(UnitH.class, 
				"select * from TAB_UNIT_H where unitPk = ? and ? between effectiveDateFrom and effectiveDateTo", unitOID.getPk(), date);
	}

}
