package com.tathvatech.workstation.common;


import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.common.wrapper.PersistWrapper;

public class DummyWorkstation {

    public static final String DUMMY = "dummy";
    private static int pk = 0;
    private PersistWrapper persistWrapper;
    public  int getPk()
	{
    	if(pk == 0)
    	{
			try 
			{
				pk =  persistWrapper.read(Integer.class, "select pk from TAB_WORKSTATION where workstationName = ?", DUMMY);
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return pk;
	}

	public  WorkstationOID getOID()
	{
		return new WorkstationOID(getPk(), DUMMY);
	}
}
