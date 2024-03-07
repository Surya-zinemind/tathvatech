/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_PROJECT_FORMS")
@Data
public class ProjectForm extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private long projectPk;
	private long projectPartPk;
	private long workstationPk;
	private int formPk;
	private String name;
	private long appliedByUserFk;
	private Date lastUpdated;



}
