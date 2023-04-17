package com.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.IUsuarioService;
import com.ecommerce.service.ProductoService;
import com.ecommerce.service.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private UploadFileService upload;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos",productoService.findAll());
		return "productos/show";
	}
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		model.addAttribute("producto", producto);
		LOGGER.info("producto buscado por id {}",producto);
		return "productos/edit";
	}
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto prod = new Producto();
		prod=productoService.get(producto.getId()).get();
		
		if (file.isEmpty()) {//editamos producto pero no cambiamos imagen
			producto.setImagen(prod.getImagen());
		}else {//cuando se edita la imagen
			//eliminar imagen cuando no tenga img x defecto.
			if (!prod.getImagen().equals("default.jpg")) {
				upload.deleteImage(prod.getImagen());
			}
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(prod.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		//eliminar imagen cuando no tenga img x defecto.
		Producto prod = new Producto();
		prod = productoService.get(id).get();
		
		if (!prod.getImagen().equals("default.jpg")) {
			upload.deleteImage(prod.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
	}
	@PostMapping("/save")
	public String save(Producto producto,@RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		LOGGER.info("Es el metodo save de productoController {}", producto);
		
		Usuario user = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		producto.setUsuario(user);
		//este if para guardar img
		if (producto.getId()==null) { //cuando se crea un producto
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		
		productoService.save(producto);
		return "redirect:/productos";
	}
	
}
