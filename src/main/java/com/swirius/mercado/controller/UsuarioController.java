package com.swirius.mercado.controller;

import com.swirius.mercado.model.Usuario;
import com.swirius.mercado.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario) {
        service.save(usuario); // <- Este método debe existir en UsuarioService
        return "redirect:/login?registroExitoso";
    }

    @GetMapping("/login")
    public String formularioLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
