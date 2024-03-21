/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.unit.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.unit.enums.Actions;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.common.UserContext;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="entity_actions")
public class EntityActions extends AbstractEntity implements Serializable
{
	private PersistWrapper persistWrapper = null;
	@Id
	private long pk; // this pk goes into the _h records as actionPk, if another object gets an _h records because one object was modified, the same actionId gets both _h records.
	private int objectPk;
	private int objectType;
	private String action1;
	private String action2;
	private String action3;
	private String action4;
	private String action5;
	private Integer performedBy;
	private Date performedDate;
	private Date lastUpdated;

    public EntityActions() {
        this.persistWrapper = persistWrapper;
    }


    @Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getObjectPk()
	{
		return objectPk;
	}

	public void setObjectPk(int objectPk)
	{
		this.objectPk = objectPk;
	}

	public int getObjectType()
	{
		return objectType;
	}

	public void setObjectType(int objectType)
	{
		this.objectType = objectType;
	}

	public String getAction1()
	{
		return action1;
	}

	public void setAction1(String action1)
	{
		this.action1 = action1;
	}

	public String getAction2()
	{
		return action2;
	}

	public void setAction2(String action2)
	{
		this.action2 = action2;
	}

	public String getAction3()
	{
		return action3;
	}

	public void setAction3(String action3)
	{
		this.action3 = action3;
	}

	public String getAction4()
	{
		return action4;
	}

	public void setAction4(String action4)
	{
		this.action4 = action4;
	}

	public String getAction5()
	{
		return action5;
	}

	public void setAction5(String action5)
	{
		this.action5 = action5;
	}

	public Integer getPerformedBy()
	{
		return performedBy;
	}

	public void setPerformedBy(Integer performedBy)
	{
		this.performedBy = performedBy;
	}

	public Date getPerformedDate()
	{
		return performedDate;
	}

	public void setPerformedDate(Date performedDate)
	{
		this.performedDate = performedDate;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public  EntityActions createAction(UserContext context, OID oid, Actions[] actions) throws Exception
	{
		EntityActions act = new EntityActions();
		act.setObjectPk((int) oid.getPk());
		act.setObjectType(oid.getEntityType().getValue());
		
		if(actions.length > 0)
			act.setAction1(actions[0].name());
		if(actions.length > 1)
			act.setAction2(actions[1].name());
		if(actions.length > 2)
			act.setAction3(actions[2].name());
		if(actions.length > 3)
			act.setAction4(actions[3].name());
		if(actions.length > 4)
			act.setAction5(actions[4].name());
		
		act.setPerformedBy((int) context.getUser().getPk());
		act.setPerformedDate(new Date());

		int pk = (int) persistWrapper.createEntity(act);
		return (EntityActions) persistWrapper.readByPrimaryKey(EntityActions.class, pk);
	}
}
