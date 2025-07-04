package com.swirius.mercado.controller;

import com.swirius.mercado.model.Producto;
import com.swirius.mercado.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@Autowired
	private ProductoService productoService;

	@GetMapping("/")
	public String verInicio(@RequestParam(required = false) String q,
	                        @RequestParam(required = false) BigDecimal min,
	                        @RequestParam(required = false) BigDecimal max,
	                        @RequestParam(required = false) String cat,
	                        @RequestParam(defaultValue = "0") int page,
	                        Model model) {

	    int size = 6; // productos por página
	    Page<Producto> productos = productoService.buscarAvanzado(q, min, max, cat, page, size);

	    model.addAttribute("productos", productos);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", productos.getTotalPages());
	    model.addAttribute("q", q);
	    model.addAttribute("cat", cat);
	    model.addAttribute("min", min);
	    model.addAttribute("max", max);

	    List<String> categorias = List.of("Tecnología", "Hogar", "Moda", "Deportes", "Otros");
	    model.addAttribute("categorias", categorias);

	    return "index";
	}
}