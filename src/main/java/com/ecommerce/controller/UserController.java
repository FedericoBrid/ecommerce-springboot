package com.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Usuario;
import com.ecommerce.service.IUsuarioService;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IUsuarioService usuarioService;

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
}
