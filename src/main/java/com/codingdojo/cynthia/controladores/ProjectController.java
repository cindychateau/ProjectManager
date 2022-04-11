package com.codingdojo.cynthia.controladores;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;
import com.codingdojo.cynthia.servicios.AppService;

@Controller
@RequestMapping("/projects")
public class ProjectController {
	
	@Autowired
	private AppService servicio;
	
	@GetMapping("/new")
	public String new_project(@ModelAttribute("project") Project project, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		return "new.jsp";
		
	}
	
	@PostMapping("/create")
	public String create_project(@Valid @ModelAttribute("project") Project project, 
								 BindingResult result,
								 HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session"); //Usuario en sesión
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		if(result.hasErrors()) {
			return "new.jsp";
		} else {
			
			Project nuevoProyecto = servicio.save_project(project);
			User myUser = servicio.find_user(currentUser.getId()); //ID de usuario en sesión 
			myUser.getProjects_joined().add(nuevoProyecto);
			servicio.save_user(myUser);
			
			return "redirect:/dashboard";
			
		}
	}
	
	
	
}
