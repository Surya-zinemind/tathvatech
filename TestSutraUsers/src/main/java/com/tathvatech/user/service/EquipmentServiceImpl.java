package com.tathvatech.user.service;


import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.entity.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("equipmentService")
public class EquipmentServiceImpl implements EquipmentService{


	@Autowired
	PersistWrapper persistWrapper;
	
//	@Autowired
//	EquipmentRepo equipmentRepo;

	public Equipment getEquipment(long id)
	{
		return (Equipment) persistWrapper.readByPrimaryKey(Equipment.class, id);
		//return persistWrapper.readList(Equipment.class, "select * from equipment order by pk desc limit 0, 2", null);
	}

	public List<Equipment> getEquipments()
	{
//		return dao.findAll(Equipment.class);
		return persistWrapper.readList(Equipment.class, "select * from equipment order by pk desc limit 0, 2");
	}

	public List<Equipment> getAllEquipments()
	{
//		return dao.findAll(Equipment.class);
		return (List<Equipment>) persistWrapper.readAll(Equipment.class);
	}

	@Transactional
	public void addEquipment() throws Exception {
		Equipment eq = new Equipment();
		eq.setCreatedBy(110);
		eq.setDescription("Testing new Persist wrapper" + new Date().getTime());
		persistWrapper.createEntity(eq);
//		dao.create(eq);
	}

	@Transactional
	public void updateEquipment()  throws Exception{
		Equipment eq = getEquipment(2741);
		eq.setApprovedComment("Updating approved Comment from code.............");
		persistWrapper.update(eq);
	}

	@Transactional
	public void deleteEquipment() throws Exception
	{
		Equipment eq = getEquipment(2741);
		persistWrapper.deleteEntity(eq);
	}

}
