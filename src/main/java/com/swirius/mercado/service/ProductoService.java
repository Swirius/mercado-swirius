package com.swirius.mercado.service;

import com.swirius.mercado.model.Producto;
import com.swirius.mercado.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public void guardar(Producto producto) {
        productoRepository.save(producto);
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> buscarAvanzado(String nombre, Double min, Double max, String categoria) {
        List<Producto> base = productoRepository.findAll();

        return base.stream()
            .filter(p -> nombre == null || p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(p -> categoria == null || categoria.isBlank() || p.getCategoria().equalsIgnoreCase(categoria))
            .filter(p -> min == null || p.getPrecio() >= min)
            .filter(p -> max == null || p.getPrecio() <= max)
            .toList();
    }

    public void eliminarPorId(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        productoOpt.ifPresent(producto -> {
            if (producto.getImagen() != null) {
                Path ruta = Paths.get("src/main/resources/static/uploads/" + producto.getImagen());
                try {
                    Files.deleteIfExists(ruta);
                } catch (IOException e) {
                    System.err.println("No se pudo borrar la imagen: " + e.getMessage());
                }
            }
            productoRepository.deleteById(id);
        });
    }
}
