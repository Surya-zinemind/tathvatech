package com.tathvatech.user.service;

import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.entity.Equipment;
import com.tathvatech.user.entity.Project;

import java.util.List;

public interface EquipmentService {
	public List<Equipment> getEquipments();
	public List<Project> getProjects();
	public List<TestProcObj> getTestProcs();
	public long createProject() throws Exception;
	void updateProject() throws Exception;
}
