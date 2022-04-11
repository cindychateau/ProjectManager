package com.codingdojo.cynthia.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.codingdojo.cynthia.modelos.LoginUser;
import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;
import com.codingdojo.cynthia.servicios.AppService;

@Controller
public class UserController {
	
	@Autowired
	private AppService servicio;
	
	@GetMapping("/")
	public String index(@ModelAttribute("nuevoUsuario") User nuevoUsuario,
						@ModelAttribute("nuevoLogin") LoginUser nuevoLogin) {
		/*model.addAttribute("nuevoUsuario", new User());
		model.addAttribute("nuevoLogin", new LoginUser());*/
		
		return "index.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("nuevoUsuario") User nuevoUsuario,
						   BindingResult result, Model model, HttpSession session) {
		
		servicio.register(nuevoUsuario, result);
		if(result.hasErrors()) {
			model.addAttribute("nuevoLogin", new LoginUser());
			return "index.jsp";
		}
		
		session.setAttribute("user_session", nuevoUsuario);
		return "redirect:/dashboard";
		
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("nuevoLogin") LoginUser nuevoLogin,
						BindingResult result, Model model, HttpSession session) {
							
		User user = servicio.login(nuevoLogin, result);
		if(result.hasErrors()) {
			model.addAttribute("nuevoUsuario", new User());
			return "index.jsp";
		}
		
		session.setAttribute("user_session", user);
		return "redirect:/dashboard";
							
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user_session");
		return "redirect:/";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		//Revisan sessión
		User currentUser = (User)session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		
		//Buscmaos las dos listas
		List<Project> mis_proyectos = servicio.find_my_projects(currentUser);
		List<Project> resto_proyectos = servicio.find_other_projects(currentUser);
		
		model.addAttribute("mis_proyectos", mis_proyectos);
		model.addAttribute("resto_proyectos", resto_proyectos);

		return "dashboard.jsp";
	}
	
	
}
