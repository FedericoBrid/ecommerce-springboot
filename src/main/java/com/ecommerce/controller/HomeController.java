package com.ecommerce.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import com.ecommerce.model.Producto;
import com.ecommerce.model.DetalleOrden;
import com.ecommerce.model.Orden;
import com.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class); 
	
	@Autowired
	private ProductoService productoService;
	
	//para almacenar los detalles de la ordenes
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	//almacena datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "user/home";
	}
	@GetMapping("/productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		model.addAttribute("producto", producto);
		
		LOGGER.info("id enviado como parametro {}", id);
		return "user/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal=0;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		
		producto = optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);
		
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		LOGGER.info("producto añadido: {}", optionalProducto.get());
		LOGGER.info("cantidad: {}", cantidad);
		
		return "user/carrito";
		
	}

	@GetMapping("/delete/cart/{id}")
	public String deleteProductCart(@PathVariable Integer id, Model model) {
		List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
		
		
		
		for (DetalleOrden detalleOrden: detalles) {
			if (detalleOrden.getProducto().getId()!=id) {
				ordenesNuevas.add(detalleOrden);
			}
		//nueva lista con productos restantes
		detalles = ordenesNuevas;
		
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		}
		return "user/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "user/carrito";
	}

}
