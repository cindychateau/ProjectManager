package com.codingdojo.cynthia.controladores;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	//@RequestMapping(value="/new", method=RequestMethod.GET)
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
	//@RequestMapping(value="/create", method=RequestMethod.POST)
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
			
			Project nuevoProyecto = servicio.save_project(project);//Guardando el proyecto
			
			User myUser = servicio.find_user(currentUser.getId()); //ID de usuario en sesión 
			myUser.getProjects_joined().add(nuevoProyecto);
			servicio.save_user(myUser);
			
			return "redirect:/dashboard";
			
		}
	}
	
	@GetMapping("/edit/{id}")
	public String edit_project(@PathVariable("id") Long id,
							   @ModelAttribute("project") Project project,
							   HttpSession session,
							   Model model) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session"); //Usuario en sesión
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Project project_edit = servicio.find_project(id);
		model.addAttribute("project", project_edit);
		
		return "edit.jsp";
	}
	
	@PutMapping("/update")
	//@RequestMapping(value="/update", method=RequestMethod.PUT)
	public String update_project(@Valid @ModelAttribute("project") Project projectFormulario,
								 BindingResult result) {
		
		if(result.hasErrors()) {
			return "edit.jsp";
		} else {
			Project thisProject = servicio.find_project( projectFormulario.getId() );
			projectFormulario.setUsers(thisProject.getUsers());
			servicio.save_project(projectFormulario);
			
			return "redirect:/dashboard";
		}
		
	}
	
	@GetMapping("/join/{project_id}")
	public String join_project(@PathVariable("project_id") Long project_id, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session"); //Usuario en sesión
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		servicio.save_project_user(currentUser.getId(), project_id);
		
		return "redirect:/dashboard";
	}
	
	@GetMapping("/leave/{project_id}")
	public String leave_project(@PathVariable("project_id") Long project_id, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session"); //Usuario en sesión
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		servicio.remove_project_user(currentUser.getId(), project_id);
		
		return "redirect:/dashboard";
	}
	
	@GetMapping("/{project_id}")
	public String show_project(@PathVariable("project_id") Long project_id, HttpSession session, Model model) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session"); //Usuario en sesión
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Project thisProject = servicio.find_project(project_id);
		model.addAttribute("project", thisProject);
		
		return "show.jsp";		
	}
	
	@DeleteMapping("/delete/{project_id}")
	public String delete_project(@PathVariable("project_id") Long project_id, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session"); //Usuario en sesión
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		servicio.delete_project(project_id);
		
		return "redirect:/dashboard";
	}
	
	
}
