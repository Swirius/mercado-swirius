package com.swirius.mercado.repository;

import com.swirius.mercado.model.Carrito;
import com.swirius.mercado.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByCarrito(Carrito carrito);
}
