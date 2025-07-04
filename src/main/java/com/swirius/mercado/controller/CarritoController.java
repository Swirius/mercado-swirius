package com.swirius.mercado.controller;

import com.swirius.mercado.model.Usuario;
import com.swirius.mercado.service.CarritoService;
import com.swirius.mercado.service.OrdenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

	@Autowired
	private CarritoService carritoService;

	@Autowired
	private OrdenService ordenService;

	@GetMapping
	public String verCarrito(@AuthenticationPrincipal Usuario usuario, Model model) {
		model.addAttribute("items", carritoService.obtenerItems(usuario));
		model.addAttribute("total", carritoService.calcularTotal(usuario));
		return "carrito";
	}

	@PostMapping("/agregar/{id}")
	public String agregarAlCarritoConCantidad(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id,
			@RequestParam int cantidad) {
		if (cantidad > 0) {
			carritoService.agregarProducto(usuario, id, cantidad);
		}
		return "redirect:/";
	}

	@GetMapping("/eliminar/{itemId}")
	public String eliminar(@PathVariable Long itemId) {
		carritoService.eliminarItem(itemId);
		return "redirect:/carrito";
	}

	@GetMapping("/vaciar")
	public String vaciar(@AuthenticationPrincipal Usuario usuario) {
		carritoService.vaciarCarrito(usuario);
		return "redirect:/carrito";
	}

	@GetMapping("/finalizar")
	public String finalizarCompra(@AuthenticationPrincipal Usuario usuario) {
		ordenService.finalizarCompra(usuario);
		return "redirect:/orden/confirmacion";
	}

	@PostMapping("/checkout")
	public String checkout(@AuthenticationPrincipal Usuario usuario, Model model) {
		if (carritoService.obtenerItems(usuario).isEmpty()) {
			return "redirect:/carrito?error=vacio";
		}

		ordenService.finalizarCompra(usuario);
		model.addAttribute("orden", ordenService.obtenerUltimaOrden(usuario));

		return "orden_confirmacion";
	}
}
