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
    public String nuevoProducto(Model model) {
        Producto producto = new Producto();
        producto.setStock(0);
        model.addAttribute("producto", producto);
        return "productos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Producto producto = service.buscarPorId(id);
        if (producto == null) {
            return "redirect:/";
        }

        if (producto.getStock() == null) {
            producto.setStock(0);
        }

        model.addAttribute("producto", producto);
        return "productos/formulario";
    }


    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto, BindingResult result,
                          @RequestParam("file") MultipartFile file, Model model) throws IOException {

        // Validación global ficticia de ejemplo: nombre duplicado
        if (producto.getNombre() != null && service.existePorNombre(producto.getNombre(), producto.getId())) {
            result.reject("error.nombreDuplicado", "Ya existe un producto con ese nombre.");
        }

        // Validación de tipo de imagen
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

            if (!extension.matches("jpg|jpeg|png|gif|webp")) {
                result.reject("error.formatoImagen", "Formato de imagen no permitido: " + extension);
            }
        }

        // Si hay errores, volver al formulario
        if (result.hasErrors()) {
            model.addAttribute("producto", producto);
            return "productos/formulario";
        }

        // Guardado de la imagen
        if (!file.isEmpty()) {
            String nombreArchivo = System.currentTimeMillis() + "_" + Math.abs(file.getOriginalFilename().hashCode()) + "." +
                    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            Path ruta = Paths.get(UPLOAD_DIR + nombreArchivo);

            Files.createDirectories(Paths.get(UPLOAD_DIR));
            file.transferTo(ruta);

            producto.setImagen(nombreArchivo);
        } else if (producto.getId() != null) {
            Producto existente = service.buscarPorId(producto.getId());
            if (existente != null) {
                producto.setImagen(existente.getImagen());
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
