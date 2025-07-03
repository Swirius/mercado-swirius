package com.swirius.mercado.repository;

import com.swirius.mercado.model.Compra;
import com.swirius.mercado.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuario(Usuario usuario);
}
