package com.tathvatech.user.service;

import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserUnitPrefix;

public interface DeviceService {
    String createDeviceSessionKey(UserContext context, User user) throws Exception;

    Integer getUserPkBySessionKey(String sessionKey);

    String getDeviceSessionKey(int userPk);

    UserUnitPrefix getUserUnitPrefix(User user, UnitOID aUnitOID) throws Exception;
    UserUnitPrefix createUserUnitPrefix(User user, UnitOID aUnitOID) throws Exception;
}
