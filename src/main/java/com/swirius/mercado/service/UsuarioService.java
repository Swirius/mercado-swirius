package com.swirius.mercado.service;

import com.swirius.mercado.config.UsuarioDetalles;
import com.swirius.mercado.model.Usuario;
import com.swirius.mercado.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void registrar(Usuario usuario) {
		String passEncriptado = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(passEncriptado);
		repo.save(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		return new UsuarioDetalles(usuario);
	}

}