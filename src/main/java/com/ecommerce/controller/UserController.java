package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Orden;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.IOrdenService;
import com.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;

	@GetMapping("/register")
	public String create() {
		
		return "user/registro";
	}
	@PostMapping("/save")
	public String save(Usuario usuario) {
		LOGGER.info("Usuario: {}",usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}
	@GetMapping("/login")
	public String login(Usuario usuario) {
		
		
		return "user/login";
	}
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		LOGGER.info("acceso: {}",usuario);
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		
		if (user.isPresent()) {
			session.setAttribute("idUsuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/admin";
			}else {
				return "redirect:/";
			}
		}else {
			LOGGER.info("usuario inexistente");
		}
		return "redirect:/";
	}
	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		model.addAttribute("ordenes",ordenes);
		return "user/compras";
	}
}
