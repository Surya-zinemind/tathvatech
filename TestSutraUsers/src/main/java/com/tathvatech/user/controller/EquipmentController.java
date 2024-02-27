package com.tathvatech.user.controller;

import com.tathvatech.user.entity.Equipment;
import com.tathvatech.user.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EquipmentController {

	@Autowired
	EquipmentService service;

	@RequestMapping("/getEquipment")
	public Equipment getEquipment()
	{
		Equipment eq = service.getEquipment(1l);
		return eq;
	}

	@RequestMapping("/equipmentlist")
	public List<Equipment> getEquipmentList()
	{
		List<Equipment> eqList = service.getEquipments();
		return eqList;
	}

	@RequestMapping("/allEquipments")
	public List<Equipment> getAllEquipments()
	{
		List<Equipment> eqList = service.getAllEquipments();
		return eqList;
	}

	@RequestMapping("/addEquipment")
	public void addEquipment() throws Exception{
		service.addEquipment();
	}

	@RequestMapping("/updateEquipment")
	public void updateEquipment() throws Exception{
		service.updateEquipment();
	}

	@RequestMapping("/deleteEquipment")
	public void deleteEquipment() throws Exception{
		service.deleteEquipment();
	}
}
