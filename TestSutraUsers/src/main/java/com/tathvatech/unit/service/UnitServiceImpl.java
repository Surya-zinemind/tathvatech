package com.tathvatech.unit.service;

import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.dao.UnitDAO;
import com.tathvatech.user.OID.UnitOID;

public class UnitServiceImpl implements UnitService{

    public UnitObj getUnitByPk(UnitOID unitOID)
    {
        return new UnitDAO().getUnit(unitOID.getPk());
    }


}
