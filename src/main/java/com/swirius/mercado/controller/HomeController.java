package com.swirius.mercado.controller;

import com.swirius.mercado.model.Producto;
import com.swirius.mercado.service.ProductoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@Autowired
	private ProductoService productoService;

	@GetMapping("/")
	public String inicio(
	        @RequestParam(name = "q", required = false) String nombre,
	        @RequestParam(name = "min", required = false) Double min,
	        @RequestParam(name = "max", required = false) Double max,
	        @RequestParam(name = "cat", required = false) String categoria,
	        Model model) {

	    List<Producto> productos = productoService.buscarAvanzado(nombre, min, max, categoria);

	    model.addAttribute("productos", productos);
	    model.addAttribute("q", nombre);
	    model.addAttribute("min", min);
	    model.addAttribute("max", max);
	    model.addAttribute("cat", categoria);
	    return "index";
	}


}