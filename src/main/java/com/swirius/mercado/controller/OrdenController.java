package com.swirius.mercado.controller;

import com.swirius.mercado.model.Orden;
import com.swirius.mercado.model.Usuario;
import com.swirius.mercado.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orden")
public class OrdenController {

	@Autowired
	private OrdenService ordenService;

	@GetMapping("/confirmacion")
	public String mostrarConfirmacion(@AuthenticationPrincipal Usuario usuario, Model model) {
		Orden ultimaOrden = ordenService.obtenerUltimaOrden(usuario);
		model.addAttribute("orden", ultimaOrden);
		return "orden_confirmacion";
	}
}
