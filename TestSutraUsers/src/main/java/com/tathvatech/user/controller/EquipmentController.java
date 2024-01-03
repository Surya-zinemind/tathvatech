package com.tathvatech.user.controller;

import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.entity.Equipment;
import com.tathvatech.user.entity.Project;
import com.tathvatech.user.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EquipmentController {

	@Autowired
	EquipmentService service;
	
	@RequestMapping("/equipmentlist")
	public List<Equipment> getEquipmentList()
	{
		List<Equipment> eqList = service.getEquipments();
		return eqList;
	}

	@RequestMapping("/projectlist")
	public List<Project> getProjectList()
	{
		List<Project> eqList = service.getProjects();
		return eqList;
	}

	@RequestMapping("/testproclist")
	public List<TestProcObj> getTestProcList()
	{
		List<TestProcObj> eqList = service.getTestProcs();
		return eqList;
	}

	@RequestMapping("/createproject")
	public String createProject()
	{
		try {
			long val = service.createProject();
			return val+"";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
	}

	@RequestMapping("/updateproject")
	public String updateProject()
	{
		try {
			service.updateProject();
			return "Success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
	}
}
