package com.swirius.mercado.service;

import com.swirius.mercado.model.Producto;
import com.swirius.mercado.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository repo;

	public void guardar(Producto producto) {
		repo.save(producto);
	}

	public List<Producto> listar() {
		return repo.findAll();
	}
}