package com.tathvatech.unit.service;

import java.util.Date;


import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.unit.entity.UnitH;
import com.tathvatech.unit.entity.UnitInProject;
import com.tathvatech.unit.entity.UnitInProjectH;
import com.tathvatech.unit.oid.UnitInProjectOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitManagerCore
{
	private final PersistWrapper persistWrapper;
	public  UnitInProject getUnitInProject(UnitOID unitOID, ProjectOID projectOID)
	{
		return persistWrapper.read(UnitInProject.class,
				"select * from unit_project_ref where unitPk = ? and projectPk = ?", unitOID.getPk(), projectOID.getPk());
	}

	public  UnitInProjectH getUnitInProjectH(
			UnitInProjectOID unitInProjectOID, Date date)
	{
		return persistWrapper.read(UnitInProjectH.class,
				"select * from unit_project_ref_h where unitInProjectPk = ? and ? between effectiveDateFrom and effectiveDateTo", unitInProjectOID.getPk(), date);
	}

	public  UnitH getUnitH(UnitOID unitOID, Date date)
	{
		return persistWrapper.read(UnitH.class,
				"select * from TAB_UNIT_H where unitPk = ? and ? between effectiveDateFrom and effectiveDateTo", unitOID.getPk(), date);
	}

}
