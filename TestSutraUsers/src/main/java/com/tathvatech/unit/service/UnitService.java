package com.tathvatech.unit.service;

import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.user.OID.UnitOID;

public interface UnitService {

     UnitObj getUnitByPk(UnitOID unitOID);
}
