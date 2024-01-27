package com.tathvatech.user.repo;

import com.tathvatech.user.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends CrudRepository<Project, Long>  {

}
