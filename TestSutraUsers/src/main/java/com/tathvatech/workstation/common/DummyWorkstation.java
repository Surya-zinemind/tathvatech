package com.tathvatech.workstation.common;

import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.project.WorkstationOID;

public class DummyWorkstation {

    public static final String DUMMY = "dummy";
    private static int pk = 0;

    public static int getPk()
	{
    	if(pk == 0)
    	{
			try 
			{
				pk =  PersistWrapper.read(Integer.class, "select pk from TAB_WORKSTATION where workstationName = ?", DUMMY);
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return pk;
	}

	public static WorkstationOID getOID()
	{
		return new WorkstationOID(getPk(), DUMMY);
	}
}
