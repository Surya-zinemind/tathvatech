package com.tathvatech.user.service;

import com.tathvatech.user.entity.Equipment;

import java.util.List;

public interface EquipmentService {
	Equipment getEquipment(long id);

	List<Equipment> getEquipments();

	List<Equipment> getAllEquipments();

	void addEquipment() throws Exception;

	void deleteEquipment() throws Exception;

	void updateEquipment() throws Exception;
}
