package com.tathvatech.user.repo;

import com.tathvatech.user.entity.Equipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepo extends CrudRepository<Equipment, Long>  {

}
