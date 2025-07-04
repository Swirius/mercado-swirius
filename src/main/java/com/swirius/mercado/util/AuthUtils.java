package com.swirius.mercado.util;

import com.swirius.mercado.model.Usuario;
import com.swirius.mercado.config.UsuarioDetalles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static Usuario getUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UsuarioDetalles) {
            return ((UsuarioDetalles) auth.getPrincipal()).getUsuario();
        }

        return null; // no autenticado
    }
}
