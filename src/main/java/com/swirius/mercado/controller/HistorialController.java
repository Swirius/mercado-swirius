package com.swirius.mercado.controller;

import com.swirius.mercado.model.Usuario;
import com.swirius.mercado.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistorialController {

	@Autowired
	private OrdenRepository ordenRepo;

	@GetMapping("/historial")
	public String historial(@AuthenticationPrincipal Usuario usuario, Model model) {
		model.addAttribute("ordenes", ordenRepo.findByUsuarioOrderByFechaDesc(usuario));
		return "historial";
	}
}
