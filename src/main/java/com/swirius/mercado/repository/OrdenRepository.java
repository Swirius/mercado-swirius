package com.swirius.mercado.repository;

import com.swirius.mercado.model.Orden;
import com.swirius.mercado.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByUsuario(Usuario usuario);
    List<Orden> findByUsuarioOrderByFechaDesc(Usuario usuario);
}
