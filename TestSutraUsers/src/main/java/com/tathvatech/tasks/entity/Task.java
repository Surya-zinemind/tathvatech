package com.tathvatech.tasks.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


@Table(name="task")
public class Task extends AbstractEntity implements Serializable
{
	
	@Id
	long pk;
	String name; // optional
	int createdBy;
	Date createdDate;
	String taskType;
	Integer objectPk; //associated To object pk .. optional
	Integer objectType; // associated To object type
	Integer assignedToPk; // userPk
	Integer assignedToType; // user/role etc.. in the future.
	Integer performedBy;
	Date performedDate;
	String taskDef;
	String sparam1;
	String sparam2;
	String sparam3;
	Long nparam1;
	Long nparam2;
	Long nparam3;
	int status;
	Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Integer getObjectPk() {
		return objectPk;
	}

	public void setObjectPk(Integer objectPk) {
		this.objectPk = objectPk;
	}

	public Integer getObjectType() {
		return objectType;
	}

	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	public Integer getAssignedToPk() {
		return assignedToPk;
	}

	public void setAssignedToPk(Integer assignedToPk) {
		this.assignedToPk = assignedToPk;
	}

	public Integer getAssignedToType() {
		return assignedToType;
	}

	public void setAssignedToType(Integer assignedToType) {
		this.assignedToType = assignedToType;
	}

	public Integer getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(Integer performedBy) {
		this.performedBy = performedBy;
	}

	public Date getPerformedDate() {
		return performedDate;
	}

	public void setPerformedDate(Date performedDate) {
		this.performedDate = performedDate;
	}

	public String getTaskDef() {
		return taskDef;
	}

	public void setTaskDef(String taskDef) {
		this.taskDef = taskDef;
	}

	public String getSparam1() {
		return sparam1;
	}

	public void setSparam1(String sparam1) {
		this.sparam1 = sparam1;
	}

	public String getSparam2() {
		return sparam2;
	}

	public void setSparam2(String sparam2) {
		this.sparam2 = sparam2;
	}

	public String getSparam3() {
		return sparam3;
	}

	public void setSparam3(String sparam3) {
		this.sparam3 = sparam3;
	}

	public Long getNparam1() {
		return nparam1;
	}

	public void setNparam1(Long nparam1) {
		this.nparam1 = nparam1;
	}

	public Long getNparam2() {
		return nparam2;
	}

	public void setNparam2(Long nparam2) {
		this.nparam2 = nparam2;
	}

	public Long getNparam3() {
		return nparam3;
	}

	public void setNparam3(Long nparam3) {
		this.nparam3 = nparam3;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
