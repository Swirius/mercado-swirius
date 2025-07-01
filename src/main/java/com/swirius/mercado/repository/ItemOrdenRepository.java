package com.swirius.mercado.repository;

import com.swirius.mercado.model.ItemOrden;
import com.swirius.mercado.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOrdenRepository extends JpaRepository<ItemOrden, Long> {
    List<ItemOrden> findByOrden(Orden orden);
}
