package com.tathvatech.user.repository;

import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserPasswordResetKey;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Repository
public class UserPasswordResetKeyDAO
{
	private final PersistWrapper persistWrapper;


	public UserPasswordResetKeyDAO(PersistWrapper persistWrapper) {
		this.persistWrapper = persistWrapper;
	}
	@Transactional
	public UserPasswordResetKey createUserPasswordResetKey(User user, User keysentToUser) throws Exception
	{
		//invalidate keys if there is any valid key entry.
		persistWrapper.executeUpdate("update USER_RESET_PASSWORD_KEY set estatus = 9 where userFk = ? ", user.getPk());
		
		UserPasswordResetKey key = new UserPasswordResetKey();
		key.setCreatedDate(new Date());
		key.setEstatus(EStatusEnum.Active.getValue());
		key.setNoOfTries(0);
		key.setResetDone(0);
		key.setVerificationCode(""+new Random().nextInt(9999));
		key.setUserFk(user.getPk());
		key.setKeySentToUserFk(keysentToUser.getPk());
		long pk = persistWrapper.createEntity(key);
		
		return (UserPasswordResetKey) persistWrapper.readByPrimaryKey(UserPasswordResetKey.class, pk);
	}

	/**
	 * max tries is set to 
	 * @param userOID
	 * @return
	 */
	public UserPasswordResetKey getValidUserPasswordResetKey(UserOID userOID, int maxTries, int maxHoursSinceCreated)
	{
		return persistWrapper.read(UserPasswordResetKey.class,
				"select * from USER_RESET_PASSWORD_KEY where userFk = ? and estatus = 1 and resetDone = 0 and noOfTries < ? and timestampdiff(HOUR, createdDate, now()) < ? ", 
				userOID.getPk(), maxTries, maxHoursSinceCreated);
	}

	
	public void invalidateKeys(UserOID userOID) throws Exception
	{
		persistWrapper.executeUpdate("update USER_RESET_PASSWORD_KEY set estatus = 9 where userFk = ? ", userOID.getPk());
	}
	public void updateUserPasswordResetKey(UserPasswordResetKey key) throws Exception {
		persistWrapper.update(key);
	}

}
