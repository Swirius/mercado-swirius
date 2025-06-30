package com.swirius.mercado.controller;

import com.swirius.mercado.model.Producto;
import com.swirius.mercado.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Producto producto, @RequestParam("file") MultipartFile file)
			throws IOException {

		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

			// Validar extensiones permitidas
			if (!extension.matches("jpg|jpeg|png|gif|webp")) {
				throw new IOException("Formato de imagen no permitido: " + extension);
			}

			// Nombre único por timestamp + hash
			String nombreArchivo = System.currentTimeMillis() + "_" + Math.abs(originalFilename.hashCode()) + "."
					+ extension;
			Path ruta = Paths.get(UPLOAD_DIR + nombreArchivo);
			Files.createDirectories(ruta.getParent());
			Files.write(ruta, file.getBytes());

			producto.setImagen(nombreArchivo);
		}

		service.guardar(producto);
		return "redirect:/";
	}
}