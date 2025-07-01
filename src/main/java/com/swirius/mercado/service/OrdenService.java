package com.swirius.mercado.service;

import com.swirius.mercado.model.*;
import com.swirius.mercado.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepo;

    @Autowired
    private ItemOrdenRepository itemOrdenRepo;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoRepository productoRepo;

    public void finalizarCompra(Usuario usuario) {
        List<ItemCarrito> itemsCarrito = carritoService.obtenerItems(usuario);
        if (itemsCarrito.isEmpty())
            return;

        Orden orden = new Orden();
        orden.setFecha(LocalDateTime.now());
        orden.setUsuario(usuario);
        orden.setTotal(carritoService.calcularTotal(usuario));
        orden = ordenRepo.save(orden);

        for (ItemCarrito item : itemsCarrito) {
            ItemOrden itemOrden = new ItemOrden();
            itemOrden.setOrden(orden);
            itemOrden.setProducto(item.getProducto());
            itemOrden.setCantidad(item.getCantidad());
            itemOrden.setPrecio(item.getPrecio());
            itemOrdenRepo.save(itemOrden);

            // ↓ Descontar stock del producto
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepo.save(producto);
        }

        carritoService.vaciarCarrito(usuario);
    }

    public Orden obtenerUltimaOrden(Usuario usuario) {
        List<Orden> ordenes = ordenRepo.findByUsuarioOrderByFechaDesc(usuario);
        return ordenes.isEmpty() ? null : ordenes.get(0);
    }

}
