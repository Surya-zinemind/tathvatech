/*
 * Created on Nov 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.service;


import java.util.Random;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Device;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserUnitPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Service
public class DeviceServiceImpl implements DeviceService {
	private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
	private final PersistWrapper persistWrapper;

    public DeviceServiceImpl(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }

    @Override
	public  String createDeviceSessionKey(UserContext context, User user)throws Exception
	{    logger.debug("within createDeviceSessionKey");
	    //delete the current sessionkey entry
		persistWrapper.delete("delete from TAB_DEVICE where userPk=?", user.getPk());
		
	    int rnd = new Random().nextInt();
	    String sessionKey = Integer.toString(rnd);
	    
	    Device device = new Device();
	    device.setUserPk(user.getPk());
	    device.setSessionKey(sessionKey);
	    persistWrapper.createEntity(device);
	    
	    return sessionKey;
	}
	@Override
	public  Integer getUserPkBySessionKey(String sessionKey)
	{
		Integer userPk = persistWrapper.read(Integer.class, "select userPk from TAB_DEVICE where sessionKey=?", sessionKey);
		return userPk;
	}
	@Override
	public  String getDeviceSessionKey(int userPk)
	{
		String sessionKey = persistWrapper.read(String.class, "select sessionKey from TAB_DEVICE where userPk=?", userPk);
		return sessionKey;
	}

	@Override
	public UserUnitPrefix getUserUnitPrefix(User user, UnitOID aUnitOID) throws Exception
	{
		UserUnitPrefix upr = persistWrapper.read(UserUnitPrefix.class, "select * from TAB_USER_UNIT_PREFIX where userPk=? and unitPk=?", user.getPk(), aUnitOID.getPk());
		if(upr == null)
		{
			upr = createUserUnitPrefix(user, aUnitOID);
		}
		return upr;
	}
	@Override
	public UserUnitPrefix createUserUnitPrefix(User user, UnitOID aUnitOID) throws Exception {
		//I am doing a read inside the synchronized block, to make sure concurency issue is taken care of
		UserUnitPrefix upr = persistWrapper.read(UserUnitPrefix.class, "select * from TAB_USER_UNIT_PREFIX where userPk=? and unitPk=?", user.getPk(), aUnitOID.getPk());
		if (upr == null) {
			Integer a = persistWrapper.read(Integer.class, "select max(refPrefix) from TAB_USER_UNIT_PREFIX where unitPk=?", aUnitOID.getPk());

			UserUnitPrefix nupr = new UserUnitPrefix();
			nupr.setUserPk(user.getPk());
			nupr.setUnitPk(aUnitOID.getPk());
			if (a == null) {
				nupr.setRefPrefix(1);
			} else {
				nupr.setRefPrefix(a + 1);
			}
			long newPk = persistWrapper.createEntity(nupr);

			upr = (UserUnitPrefix) persistWrapper.readByPrimaryKey(UserUnitPrefix.class, newPk);
		}
		return upr;
	}
}
