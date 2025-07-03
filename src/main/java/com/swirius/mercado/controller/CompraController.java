package com.swirius.mercado.controller;

import com.swirius.mercado.model.*;
import com.swirius.mercado.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CompraController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/finalizar-compra")
    public String finalizarCompra(@AuthenticationPrincipal Usuario usuario) {
        List<ItemCarrito> items = carritoService.obtenerItems(usuario);

        if (items.isEmpty()) {
            return "redirect:/carrito?vacio";
        }

        Compra compra = new Compra();
        compra.setFecha(LocalDateTime.now());
        compra.setUsuario(usuario);
        compra.setTotal(carritoService.calcularTotal(usuario));

        List<DetalleCompra> detalles = new ArrayList<>();
        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();

            // Verificar stock disponible
            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoService.guardar(producto);

            // Crear detalle de compra
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());

            detalles.add(detalle);
        }

        compra.setDetalleCompraList(detalles);
        compraService.guardarCompra(compra);
        carritoService.vaciarCarrito(usuario);

        return "redirect:/confirmacion/" + compra.getId();
    }

    @GetMapping("/confirmacion/{id}")
    public String mostrarConfirmacion(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario, Model model) {
        Compra compra = compraService.obtenerPorId(id);

        if (compra == null || !compra.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/";
        }

        model.addAttribute("compra", compra);
        return "confirmacion";
    }

    @GetMapping("/historial")
    public String verHistorial(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<Compra> compras = compraService.obtenerPorUsuario(usuario);
        model.addAttribute("ordenes", compras);
        return "historial";
    }
}
