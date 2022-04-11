package com.codingdojo.cynthia.servicios;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.cynthia.modelos.LoginUser;
import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;
import com.codingdojo.cynthia.repositorios.ProjectRepository;
import com.codingdojo.cynthia.repositorios.UserRepository;

@Service
public class AppService {
	
	@Autowired
	private UserRepository repositorio_user;
	
	@Autowired 
	private ProjectRepository repositorio_proyectos;
	
	public User register(User nuevoUsuario, BindingResult result) {
		
		String nuevoEmail = nuevoUsuario.getEmail();
		
		//Revisamos si existe el correo electrónico en BD
		if(repositorio_user.findByEmail(nuevoEmail).isPresent()) {
			result.rejectValue("email", "Unique", "El correo fue ingresado previamente.");
		}
		
		if(! nuevoUsuario.getPassword().equals(nuevoUsuario.getConfirm()) ) {
			result.rejectValue("confirm", "Matches", "Las contraseñas no coiniciden");
		}
		
		if(result.hasErrors()) {
			return null;
		} else {
			//Encriptamos contraseña
			String contra_encr = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
			nuevoUsuario.setPassword(contra_encr);
			//Guardo usuario
			return repositorio_user.save(nuevoUsuario);
		}
		
	}
	
	public User login(LoginUser nuevoLogin, BindingResult result) {
		
		if(result.hasErrors()) {
			return null;
		}
		
		//Buscamos por correo
		Optional<User> posibleUsuario = repositorio_user.findByEmail(nuevoLogin.getEmail());
		if(!posibleUsuario.isPresent()) {
			result.rejectValue("email", "Unique", "Correo ingresado no existe");
			return null;
		}
		
		User user_login = posibleUsuario.get();
		
		//Comparamos contraseñas encriptadas
		if(! BCrypt.checkpw(nuevoLogin.getPassword(), user_login.getPassword()) ) {
			result.rejectValue("password", "Matches", "Contraseña inválida");
		}
		
		if(result.hasErrors()) {
			return null;
		} else {
			return user_login; 
		}
		
		
	}
	
	public User save_user(User updatedUser) {
		return repositorio_user.save(updatedUser);
	}
	
	public User find_user(Long id) {
		Optional<User> optionalUser = repositorio_user.findById(id);
		if(optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}
	}
	
	public Project save_project(Project nuevoProyecto) {
		return repositorio_proyectos.save(nuevoProyecto);
	}
	
	public Project find_project(Long id) {
		Optional<Project> optionalProject = repositorio_proyectos.findById(id);
		if(optionalProject.isPresent()) {
			return optionalProject.get();
		} else {
			return null;
		}
	}
	
	public List<Project> find_my_projects(User myUser) {
		return repositorio_proyectos.findAllByUsers(myUser);
	}
	
	public List<Project> find_other_projects(User myUser){
		return repositorio_proyectos.findByUsersNotContains(myUser);
	}
	
	public void save_project_user(Long user_id, Long project_id) {
		Project thisProject = find_project(project_id);
		User thisUser = find_user(user_id);
		
		thisUser.getProjects_joined().add(thisProject);
		repositorio_user.save(thisUser);
	}
	
	public void remove_project_user(Long user_id, Long project_id) {
		Project thisProject = find_project(project_id);
		User thisUser = find_user(user_id);
		
		thisUser.getProjects_joined().remove(thisProject);
		repositorio_user.save(thisUser);
	}
	
}
