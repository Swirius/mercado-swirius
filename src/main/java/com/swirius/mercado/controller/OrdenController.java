package com.swirius.mercado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orden")
public class OrdenController {

	@GetMapping("/confirmacion")
	public String confirmacion() {
		return "orden_confirmacion";
	}
}