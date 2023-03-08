package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Producto;
import com.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private ProductoService productoService;

	@GetMapping("")
	public String home(Model model) {
		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);
		return "admin/home";
	}
}
