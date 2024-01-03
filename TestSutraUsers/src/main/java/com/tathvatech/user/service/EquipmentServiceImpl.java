package com.tathvatech.user.service;


import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.entity.Equipment;
import com.tathvatech.user.entity.Project;
import com.tathvatech.user.repo.EquipmentRepo;
import com.tathvatech.user.repo.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("equipmentService")
public class EquipmentServiceImpl implements EquipmentService{
	
	@Autowired
	PersistWrapper persistWrapper;
	
	@Autowired
	EquipmentRepo equipmentRepo;
	
	@Autowired
	ProjectRepo projectRepo;
	
	public List<Equipment> getEquipments()
	{
		return persistWrapper.readList(Equipment.class, "select * from equipment", null);
	}

	@Override
	public List<Project> getProjects() {
		return persistWrapper.readList(Project.class, "select * from tab_project where projectName like ?", "%1080%");
	}


	@Override
	public List<TestProcObj> getTestProcs()
	{
		String fetchSql = "select ut.pk as pk, uth.name as name, 0 as appliedByUserFk, uth.projectTestProcPk as projectTestProcPk, "
				+ " uth.projectPk as projectPk, uth.workstationPk as workstationPk, uth.unitPk as unitPk, "
				+ " 0 as formPk, uth.createdBy, uth.createdDate "
				+ " from unit_testproc ut"
				+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
				+ " where 1 = 1 and uth.unitPk = ? ";
		
		return persistWrapper.readList(TestProcObj.class, fetchSql, 7095);
	}
	
	@Override
	public long createProject() throws Exception
	{
		Project p = new Project();
		p.setPk(1368);
		p.setAccountPk(2);
		p.setContractNo("112233");
		p.setCopyrightNotice("Notice");
		p.setCreatedBy(110);
		p.setManagerPk(110);
		p.setLastUpdated(new Date());
		
		p = projectRepo.save(p);
		
		return p.getPk();
	}
	
	@Override
	public void updateProject() throws Exception
	{
		Optional<Project> pO = projectRepo.findById(1368l);
		if(pO.isPresent())
		{
			Project p = pO.get();
			
			p.setContractNo("33333");
			projectRepo.save(p);
		}
	}
}
