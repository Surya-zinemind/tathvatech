package com.tathvatech.common.enums;

import java.util.HashMap;

public class EntityTypeEnumMap 
{
	private static HashMap<Integer, EntityType> entityTypeMap = new HashMap<Integer, EntityType>();
	private static EntityTypeEnumMap instance; 
	private EntityTypeEnumMap()
	{
		
	}
	
	public static EntityTypeEnumMap getInstance()
	{
		if(instance == null)
		{
			instance = new EntityTypeEnumMap();
		}
		
		return instance;
	}
	
	public void addEntityType(int value, EntityType type)
	{
		entityTypeMap.put(value, type);
	}
	
	public EntityType fromValue(int value)
	{
		return entityTypeMap.get(value);
	}
}
