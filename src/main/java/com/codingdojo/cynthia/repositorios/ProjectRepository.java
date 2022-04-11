package com.codingdojo.cynthia.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.cynthia.modelos.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
	
	
	List <Project> findAll();
	List<Project> findById(long id);
	
	Project save(Project nuevoProyecto);
	
}
