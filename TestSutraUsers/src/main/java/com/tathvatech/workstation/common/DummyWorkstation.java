package com.tathvatech.workstation.common;


import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.common.wrapper.PersistWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DummyWorkstation {

    public static final String DUMMY = "dummy";
    private  int pk = 0;
	private final PersistWrapper persistWrapper;

    public DummyWorkstation(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }


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
