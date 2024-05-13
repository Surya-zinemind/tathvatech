package com.tathvatech.user.enums;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.equipmentcalibration.enums.BaseEnum;
import com.tathvatech.user.OID.TSBeanBase;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public abstract class DBEnum extends TSBeanBase implements BaseEnum
{
	private  final   PersistWrapper persistWrapper;
	private static HashMap<String, DBEnumCacheValues> cache = new HashMap<String, DBEnumCacheValues>();
	protected DBEnum(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }
	@Override
	public abstract long getPk();
	public abstract void setPk(long val);

	@Override
	public Integer getValue()
	{
		return (int) getPk();
	}

	@Override
	public String getDisplayText()
	{
		return getName();
	}
	public abstract String getName();
	public abstract void setName(String string);

	private String description;
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return getDescription();
	}

	public  <T extends DBEnum> DBEnum getEnum(Class<? extends DBEnum> type, Integer value)
	{
		if(value == null)
			return null;
		else
			return getEnum(type, value.intValue());
	}
	public <T extends DBEnum> DBEnum getEnum(Class<? extends DBEnum> type, int value)
	{
		try
		{

			String tableName = type.getAnnotation(Table.class).name();
			DBEnumCacheValues enumMap = cache.get(tableName);
			if(enumMap == null)
			{
				loadDbEnum(type);
				enumMap = cache.get(tableName); // get it again after loading.
			}
			if(enumMap != null && enumMap.values != null)
			{
				return enumMap.values.get(value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public <T extends DBEnum> DBEnum getEnum(Class<? extends DBEnum> type, String name)
	{
		try
		{
			String tableName = type.getAnnotation(Table.class).name();
			DBEnumCacheValues enumMap = cache.get(tableName);
			if(enumMap == null)
			{
				loadDbEnum(type);
				enumMap = cache.get(tableName); // get it again after loading.
			}
			if(enumMap != null && enumMap.values != null)
			{
				LinkedHashMap<Integer, DBEnum> cacheValues = enumMap.values;
				for (Iterator iterator = cacheValues.values().iterator(); iterator.hasNext();)
				{
					DBEnum aEnum = (DBEnum) iterator.next();
					if(aEnum.getDisplayText().equals(name))
						return aEnum;
				}
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public <T extends DBEnum> LinkedHashMap<Integer, DBEnum> getEnumList(Class<? extends DBEnum> type)
	{
		try
		{
			String tableName = type.getAnnotation(Table.class).name();
			DBEnumCacheValues enumMap = cache.get(tableName);
			if(enumMap == null)
			{
				loadDbEnum(type);
				enumMap = cache.get(tableName); // get it again after loading.
			}
			LinkedHashMap<Integer, DBEnum> returnlist = new LinkedHashMap<Integer, DBEnum>();
			for (Iterator iterator = enumMap.values.values().iterator(); iterator.hasNext();)
			{
				DBEnum aEnum = (DBEnum) iterator.next();
				returnlist.put(aEnum.getValue(), aEnum);
			}
			return returnlist;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private  synchronized void loadDbEnum(Class<? extends DBEnum> type)
	{
		String tableName = type.getAnnotation(Table.class).name();
		try
		{
			DBEnumCacheValues enumMap = new DBEnumCacheValues();
			LinkedHashMap<Integer, DBEnum> map = new LinkedHashMap<Integer, DBEnum>();
			List<DBEnum> dbenums = (List<DBEnum>) persistWrapper.readList(Class.forName(type.getName()),"select pk as pk, name as name, description as description from " + tableName + " where estatus = " + EStatusEnum.Active.getValue());
			for (Iterator iterator = dbenums.iterator(); iterator.hasNext();)
			{
				DBEnum aDbEnum = (DBEnum) iterator.next();
				map.put(aDbEnum.getValue(), aDbEnum);
			}
			enumMap.loadedDate = new Date();
			enumMap.values = map;

			cache.put(tableName, enumMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static class DBEnumCacheValues
	{
		Date loadedDate;
		LinkedHashMap<Integer, DBEnum> values;
	}


	@Override
	public boolean equals(Object inVal)
	{
		if(inVal == null)
			return false;

		DBEnum val = (DBEnum)inVal;
		if(inVal.getClass().equals(this.getClass()) && val.getPk() == this.getPk())
			return true;

		return false;
	}
}
