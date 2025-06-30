package com.swirius.mercado.repository;

import com.swirius.mercado.model.Orden;
import com.swirius.mercado.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
	List<Orden> findByUsuarioOrderByFechaDesc(Usuario usuario);
}
