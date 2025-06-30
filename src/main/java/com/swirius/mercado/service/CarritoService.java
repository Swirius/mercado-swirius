package com.swirius.mercado.service;

import com.swirius.mercado.model.*;
import com.swirius.mercado.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

	@Autowired
	private CarritoRepository carritoRepo;

	@Autowired
	private ItemCarritoRepository itemRepo;

	@Autowired
	private ProductoRepository productoRepo;

	public Carrito obtenerCarrito(Usuario usuario) {
		return carritoRepo.findByUsuario(usuario).orElseGet(() -> {
			Carrito nuevo = new Carrito();
			nuevo.setUsuario(usuario);
			return carritoRepo.save(nuevo);
		});
	}

	public void agregarProducto(Usuario usuario, Long productoId, int cantidad) {
		Carrito carrito = obtenerCarrito(usuario);
		Producto producto = productoRepo.findById(productoId).orElseThrow();

		// Ver si ya existe el ítem
		List<ItemCarrito> items = itemRepo.findByCarrito(carrito);
		for (ItemCarrito item : items) {
			if (item.getProducto().getId().equals(productoId)) {
				item.setCantidad(item.getCantidad() + cantidad);
				itemRepo.save(item);
				return;
			}
		}

		if (producto.getStock() < cantidad) {
		    throw new IllegalArgumentException("No hay suficiente stock disponible.");
		}

		// Si no existe, crear nuevo
		ItemCarrito nuevoItem = new ItemCarrito();
		nuevoItem.setCarrito(carrito);
		nuevoItem.setProducto(producto);
		nuevoItem.setCantidad(cantidad);
		nuevoItem.setPrecio(producto.getPrecio());

		itemRepo.save(nuevoItem);
	}

	public List<ItemCarrito> obtenerItems(Usuario usuario) {
		Carrito carrito = obtenerCarrito(usuario);
		return itemRepo.findByCarrito(carrito);
	}

	public double calcularTotal(Usuario usuario) {
		return obtenerItems(usuario).stream().mapToDouble(item -> item.getCantidad() * item.getPrecio()).sum();
	}

	public void eliminarItem(Long itemId) {
		itemRepo.deleteById(itemId);
	}

	public void vaciarCarrito(Usuario usuario) {
		Carrito carrito = obtenerCarrito(usuario);
		itemRepo.findByCarrito(carrito).forEach(item -> itemRepo.deleteById(item.getId()));
	}
}
