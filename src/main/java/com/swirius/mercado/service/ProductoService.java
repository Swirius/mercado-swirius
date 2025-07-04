package com.swirius.mercado.service;

import com.swirius.mercado.model.Producto;
import com.swirius.mercado.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
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

    public boolean existePorNombre(String nombre, Long id) {
        Producto existente = productoRepository.findByNombre(nombre);
        return existente != null && (id == null || !existente.getId().equals(id));
    }

    public Page<Producto> buscarAvanzado(String nombre, BigDecimal min, BigDecimal max, String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Producto> base = productoRepository.findAll();

        List<Producto> filtrados = base.stream()
            .filter(p -> nombre == null || p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(p -> categoria == null || categoria.isBlank() || p.getCategoria().equalsIgnoreCase(categoria))
            .filter(p -> min == null || p.getPrecio().compareTo(min) >= 0)
            .filter(p -> max == null || p.getPrecio().compareTo(max) <= 0)
            .toList();

        int start = Math.min(page * size, filtrados.size());
        int end = Math.min((page + 1) * size, filtrados.size());
        List<Producto> sublist = filtrados.subList(start, end);

        return new PageImpl<>(sublist, pageable, filtrados.size());
    }


    // 🔧 Lista de categorías estáticas
    public List<String> obtenerCategorias() {
        return List.of("Tecnología", "Hogar", "Moda", "Deportes", "Otros");
    }

    // 🛠️ En el futuro, si usás BD:
    // public List<String> obtenerCategorias() {
    //     return productoRepository.findDistinctCategorias(); // Necesita custom query
    // }

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
