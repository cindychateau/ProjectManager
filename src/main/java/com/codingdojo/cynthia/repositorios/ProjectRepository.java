package com.codingdojo.cynthia.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
	
	
	List <Project> findAll();
	List<Project> findById(long id);
	
	Project save(Project nuevoProyecto);
	
	List<Project> findByUsersNotContains(User user); //Selecciono todos los proyectos de los cuales NO SOY PARTE
	
	List<Project> findAllByUsers(User user); //Selecciono todos los proyectos a los que mi usuario pertenece
	
}
