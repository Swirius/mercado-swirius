package com.swirius.mercado.controller;

import com.swirius.mercado.model.Producto;
import com.swirius.mercado.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	@Autowired
	private ProductoService service;

	private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

	@GetMapping("/nuevo")
	public String formulario(Model model) {
		model.addAttribute("producto", new Producto());
		return "productos/formulario";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		Producto producto = service.buscarPorId(id);
		if (producto == null) {
			return "redirect:/";
		}
		model.addAttribute("producto", producto);
		return "productos/formulario";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid @ModelAttribute Producto producto, BindingResult result,
			@RequestParam("file") MultipartFile file, Model model) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("producto", producto);
			return "productos/formulario";
		}

		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

			if (!extension.matches("jpg|jpeg|png|gif|webp")) {
				throw new IOException("Formato de imagen no permitido: " + extension);
			}

			String nombreArchivo = System.currentTimeMillis() + "_" + Math.abs(originalFilename.hashCode()) + "."
					+ extension;
			Path ruta = Paths.get(UPLOAD_DIR + nombreArchivo);

			Files.createDirectories(Paths.get(UPLOAD_DIR));
			file.transferTo(ruta);

			producto.setImagen(nombreArchivo);
		} else {
			if (producto.getId() != null) {
				Producto existente = service.buscarPorId(producto.getId());
				if (existente != null) {
					producto.setImagen(existente.getImagen());
				}
			}
		}

		service.guardar(producto);
		return "redirect:/";
	}

	@GetMapping("/detalle/{id}")
	public String detalle(@PathVariable Long id, Model model) {
		Producto producto = service.buscarPorId(id);
		if (producto == null) {
			return "redirect:/";
		}
		model.addAttribute("producto", producto);
		return "productos/detalle";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id) {
		service.eliminarPorId(id);
		return "redirect:/";
	}
}
