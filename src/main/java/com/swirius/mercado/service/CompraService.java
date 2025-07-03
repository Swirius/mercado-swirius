package com.swirius.mercado.service;

import com.swirius.mercado.model.Compra;
import com.swirius.mercado.model.Usuario;
import com.swirius.mercado.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepo;

    public void guardarCompra(Compra compra) {
        compraRepo.save(compra);
    }

    public List<Compra> obtenerPorUsuario(Usuario usuario) {
        return compraRepo.findByUsuario(usuario);
    }

    public Compra obtenerPorId(Long id) {
        return compraRepo.findById(id).orElse(null);
    }

}
