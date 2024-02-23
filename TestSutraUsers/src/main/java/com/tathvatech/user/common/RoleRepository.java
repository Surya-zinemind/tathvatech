package com.tathvatech.user.common;


import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class RoleRepository
{
	private static Logger logger = LoggerFactory.getLogger(RoleRepository.class);
	
	private static HashMap<String, Role> roleMap = new HashMap<String, Role>();
	private static HashMap<String, EntityTypeEnum> roleAuthorizableMap = new HashMap<String, EntityTypeEnum>();
	private static RoleRepository instance; 
	private RoleRepository()
	{
		
	}
	
	public static RoleRepository getInstance()
	{
		if(instance == null)
		{
			instance = new RoleRepository();
		}
		
		return instance;
	}
	
	public void registerSystemRole(Role role, EntityTypeEnum authorizableType) 
	{
		if(roleMap.get(role.getId()) != null)
		{
			logger.error("Duplicate roleId defined:" + role.getId() + ", RoleName:" + role.getName());
			return;
		}
		roleMap.put(role.getId(), role);
		roleAuthorizableMap.put(role.getId(), authorizableType);
	}

	public Role fromRoleId(String roleId)
	{
		return roleMap.get(roleId);
	}

	public EntityTypeEnum getAuthorizableEntityTypeForRoleId(String roleId)
	{
		return roleAuthorizableMap.get(roleId);
	}
}
