package com.tathvatech.unit.sync;

import java.util.Iterator;
import java.util.WeakHashMap;

import com.tathvatech.user.OID.UnitOID;

/**
 * The Weekhashmap based synchronized. we are only using the keyset to store the unitOIDs to be synchronized.
 * the value is just a dummy.
 * @author hari
 *
 */
public class UnitChangeSynchronizer
{
	private static UnitChangeSynchronizer instance;
	private WeakHashMap<UnitOID, Integer> map = new WeakHashMap<UnitOID, Integer>();
	
	private UnitChangeSynchronizer()
	{
	}
	
	public static UnitChangeSynchronizer getInstance()
	{
		if(instance != null)
			return instance;
		else
		{
			synchronized (UnitChangeSynchronizer.class)
			{
				if(instance != null)
					return instance;
				else
				{
					instance = new UnitChangeSynchronizer();
					return instance;
				}
			}
		}
	}
	
	public synchronized UnitOID getLocableUnitOID(int unitpk)
	{
		UnitOID passedUnitOID = new UnitOID(unitpk);
		
		//check if the key set contains the rootUnitOID with the same pk and return that rootUnitOID which is stored in the keyset
		if(map.containsKey(passedUnitOID))
		{
			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();)
			{
				UnitOID aUnitOID = (UnitOID) iterator.next();
				if(aUnitOID.getPk() == passedUnitOID.getPk())
					return aUnitOID;
			}
			return null;
		}
		else
		{
			map.put(passedUnitOID, (int) passedUnitOID.getPk());
			return passedUnitOID;
		}
	}
}
