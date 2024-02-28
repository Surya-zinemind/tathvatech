package com.tathvatech.common.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.UserOID;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class AuthorizationManager 
{
	/**
	 * Method which checks if the user is assigned the roleId on any object.
	 * @param userContext
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public boolean isUserInRole(UserContext userContext, String roleId)
	{
		if(userContext.getUser().getUserType().equals(User.USER_PRIMARY))
			return true;
		
		User user = (User) userContext.getUser();
		List<ACL> acls = PersistWrapper.readList(ACL.class, "select * from ACL where userPk=? and roleId = ?", 
				user.getPk(), roleId);
		if(acls.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 
	 * @param userContext
	 * @param entityType
	 * @param roleId
	 * @return
	 */
	public boolean isUserInRole(UserContext userContext, EntityTypeEnum entityType, String roleId)
	{
		if(userContext.getUser().getUserType().equals(User.USER_PRIMARY))
			return true;
		
		User user = (User) userContext.getUser();
		List<ACL> acls = PersistWrapper.readList(ACL.class, "select * from ACL where userPk=? and objectType=? and roleId = ?", 
				user.getPk(), entityType.getValue(), roleId);
		if(acls.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Method which checks if the user is assigned ANY of the roleIds on any object.
	 * @param userContext
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public boolean isUserInRole(UserContext userContext, String[] roleId)
	{
		if(userContext.getUser().getUserType().equals(User.USER_PRIMARY))
			return true;
		if(roleId == null || roleId.length == 0)
			return false;
		
		StringBuffer sb = new StringBuffer("select * from ACL where userPk=? and roleId in (");
		String sep = "";
		for (int i = 0; i < roleId.length; i++) 
		{
			sb.append(sep);
			sb.append("'").append(roleId[i]).append("'");
			sep = ",";
		}
		sb.append(")");
		
		User user = (User) userContext.getUser();
		List<ACL> acls = PersistWrapper.readList(ACL.class, sb.toString(), 
				user.getPk());
		if(acls.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isUserInRole(UserContext userContext, OID objectOid, String roleId)
	{
		if(userContext.getUser().getUserType().equals(User.USER_PRIMARY))
			return true;
		LinkedHashSet<Action> returnList = new LinkedHashSet<>();
		
		User user = (User) userContext.getUser();
		List<ACL> acls = PersistWrapper.readList(ACL.class, "select * from ACL where objectPk = ? and objectType=? "
				+ "and userPk=? and roleId = ?", 
				objectOid.getPk(), objectOid.getEntityType().getValue(), user.getPk(), roleId);
		
		if(acls.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Method which checks if the user is assigned the ANY of the roleId on the object.
	 * @param userContext
	 * @param objectOid object on which the roles are defined.
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public boolean isUserInRole(UserContext userContext, OID objectOid, String[] roleId)
	{
		if(userContext.getUser().getUserType().equals(User.USER_PRIMARY))
			return true;
		if(roleId == null || roleId.length == 0)
			return false;
		
		StringBuffer sb = new StringBuffer("select * from ACL where objectPk = ? and objectType=? and userPk=? and "
				+ " roleId in (");
		String sep = "";
		for (int i = 0; i < roleId.length; i++) 
		{
			sb.append(sep);
			sb.append("'").append(roleId[i]).append("'");
			sep = ",";
		}
		sb.append(")");
		
		User user = (User) userContext.getUser();
		List<ACL> acls = PersistWrapper.readList(ACL.class, sb.toString(), 
				objectOid.getPk(), objectOid.getEntityType().getValue(), user.getPk());
		if(acls.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * returns the list of roles for which users have been set on an entity
	 * @param objectOid
	 * @return
	 */
	public List<String> getAssignedRoleAssignedIds(OID objectOid)
	{
		return  PersistWrapper.readList(String.class, 
				"select distinct roleId from ACL where objectPk = ? and objectType=? ", 
				objectOid.getPk(), objectOid.getEntityType().getValue());
		
	}

	/**
	 * returns the list of roles set for a user on an entity
	 * @param context context
	 * @param objectOid
	 * @return
	 */
	public List<String> getAssignedRoleAssignedIds(UserContext context, OID objectOid)
	{
		return  PersistWrapper.readList(String.class, 
				"select distinct roleId from ACL where objectPk = ? and objectType=? and userPk=? ", 
				objectOid.getPk(), objectOid.getEntityType().getValue(), context.getUser().getPk());
		
	}
	
	/**
	 * Checks if a role is assigned to any user on an object
	 * @param userContext
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public boolean isRoleAssigned(OID objectOid, String roleId)
	{
		int count = PersistWrapper.read(Integer.class, "select count(*) from ACL where objectPk = ? and objectType=? "
				+ "and roleId = ?", 
				objectOid.getPk(), objectOid.getEntityType().getValue(), roleId);
		
		if(count > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Collection<Action> getAllowedActions(UserContext userContext, OID objectOid)
	{
		LinkedHashSet<Action> returnList = new LinkedHashSet<>();
		
		User user = (User) userContext.getUser();
		List<ACL> acls = PersistWrapper.readList(ACL.class, "select * from ACL where objectPk = ? and objectType=? and userPk=?", 
				objectOid.getPk(), objectOid.getEntityType().getValue(), user.getPk());
		
		for (Iterator iterator = acls.iterator(); iterator.hasNext();) 
		{
			ACL acl = (ACL) iterator.next();
			Role role = RoleRepository.getInstance().fromRoleId(acl.getRoleId());
			returnList.addAll(role.getAllowedActions());
		}
		
		return returnList;
	}
	
	public void setAcl(UserOID userOID, OID objectOid, Role role)throws Exception
	{
		List<ACL> acls = PersistWrapper.readList(ACL.class, "select * from ACL where objectPk = ? and objectType=? and userPk=?", 
				objectOid.getPk(), objectOid.getEntityType().getValue(), userOID.getPk());
		if(acls.size() > 0)
			return;
		
		ACL acl = new ACL();
		acl.setCreatedDate(new Date());
		acl.setObjectPk(objectOid.getPk());
		acl.setObjectType(objectOid.getEntityType().getValue());
		acl.setRoleId(role.getId());
		acl.setUserPk(userOID.getPk());
		PersistWrapper.createEntity(acl);
	}
	
	public void removeAcl(UserOID userOID, OID objectOid, Role role)throws Exception
	{
		PersistWrapper.delete("delete from ACL where objectPk = ? and objectType=? and userPk=? and roleId = ?", 
				objectOid.getPk(), objectOid.getEntityType().getValue(), userOID.getPk(), role.getId());
	}

	public void removeAllAcls(OID objectOid)throws Exception
	{
		PersistWrapper.delete("delete from ACL where objectPk = ? and objectType=?", 
				objectOid.getPk(), objectOid.getEntityType().getValue());
	}

	public void setAcl(Authorizable object, Role role, List<User> users)throws Exception
	{
		List<ACL> acls = PersistWrapper.readList(ACL.class, "select * from ACL where objectPk = ? and objectType=? and roleId = ?", 
				object.getPk(), object.getEntityType().getValue(), role.getId());
		
		HashMap<Integer, ACL> userAclMap = new HashMap<>();
		for (Iterator iterator = acls.iterator(); iterator.hasNext();) 
		{
			ACL acl = (ACL) iterator.next();
			userAclMap.put(acl.getUserPk(), acl);
		}

		for (Iterator iterator = users.iterator(); iterator.hasNext();) 
		{
			User aUser = (User) iterator.next();
			if(userAclMap.remove(aUser.getPk()) == null)
			{
				ACL nAcl = new ACL();
				nAcl.setCreatedDate(new Date());
				nAcl.setObjectPk(object.getPk());
				nAcl.setObjectType(object.getEntityType().getValue());
				nAcl.setRoleId(role.getId());
				nAcl.setUserPk(aUser.getPk());
				PersistWrapper.createEntity(nAcl);
			}
		}
		
		// now what is remaining there is the ones to be removed
		for (Iterator iterator = userAclMap.values().iterator(); iterator.hasNext();) 
		{
			ACL acl = (ACL) iterator.next();
			PersistWrapper.deleteEntity(acl);
		}
	}

	/**
	 * get the users who have the role configured on the authorizable entity.
	 * @param authorizableOID
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsersInRole(Authorizable authorizableOID, Role role) 
	{
		return PersistWrapper.readList(User.class, "select * from TAB_USER where pk in (select userPk from "
				+ "ACL where objectPk=? and objectType = ? and roleId = ?) order by firstName ", 
				authorizableOID.getPk(), authorizableOID.getEntityType().getValue(), role.getId());
	}

	/**
	 * get the users who have the specified role configured on the entity type.
	 * @param authorizableOID
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsersInRole(EntityTypeEnum entityType, Role role) 
	{
		return PersistWrapper.readList(User.class, "select * from TAB_USER where pk in (select userPk from "
				+ "ACL where objectType = ? and roleId = ?) order by firstName ", 
				entityType.getValue(), role.getId());
	}

	public List<Integer> getEntitiesWithRole(UserContext context, EntityTypeEnum entityType, Role role) 
	{
		return PersistWrapper.readList(Integer.class, "select objectPk from "
				+ "ACL where objectType = ? and userPk=? and roleId = ?", 
				entityType.getValue(), context.getUser().getPk(), role.getId());
	}

	public List<Integer> getEntitiesWithRole(UserContext context, EntityTypeEnum entityType, Role[] roles) 
	{
		return getEntitiesWithRole(context.getUser().getOID(), entityType, roles);
	}
	public List<Integer> getEntitiesWithRole(UserOID userOID, EntityTypeEnum entityType, Role[] roles) 
	{
		StringBuffer sb = new StringBuffer("select objectPk from "
				+ "ACL where objectType = ? and userPk=? and roleId in (");
		List<Object> params = new ArrayList();
		params.add(entityType.getValue());
		params.add(userOID.getPk());
		
		String sep = "";
		for (int i = 0; i < roles.length; i++)
		{
			sb.append(sep).append("?");
			sep=",";
			params.add(roles[i].getId());
		}
		sb.append(")");
		
		return PersistWrapper.readList(Integer.class, sb.toString(),params.toArray());
	}

	public List<ACL> getAcls(OID objectOID)
	{
		return PersistWrapper.readList(ACL.class, "select * from ACL where objectPk=? and objectType=?", objectOID.getPk(), objectOID.getEntityType().getValue());
	}
}
