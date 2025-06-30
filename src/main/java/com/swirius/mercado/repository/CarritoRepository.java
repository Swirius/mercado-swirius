package com.swirius.mercado.repository;

import com.swirius.mercado.model.Carrito;
import com.swirius.mercado.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
	Optional<Carrito> findByUsuario(Usuario usuario);
}
