package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.ContaSistema;
import com.wanessa.refeitorio.service.ContaSistemaService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    private final ContaSistemaService contaService;

    public InicioController(
            ContaSistemaService contaService) {

        this.contaService = contaService;
    }

    @GetMapping("/inicio")
    public String inicio(
            Authentication authentication) {

        ContaSistema conta = contaService.buscarPorLogin(
                authentication.getName()
        );

        /*
         * Antes de acessar qualquer área, o cliente
         * precisa substituir o PIN temporário.
         */
        if (Boolean.TRUE.equals(
                conta.getDeveTrocarSenha())) {

            return "redirect:/trocar-pin";
        }

        Set<String> permissoes = authentication
                .getAuthorities()
                .stream()
                .map(autoridade ->
                        autoridade.getAuthority())
                .collect(Collectors.toSet());

        if (permissoes.contains(
                "ROLE_ADMINISTRADOR")) {

            return "redirect:/";
        }

        if (permissoes.contains(
                "ROLE_OPERADOR")) {

            return "redirect:/compras/nova";
        }

        if (permissoes.contains(
                "ROLE_CLIENTE")) {

            return "redirect:/minha-conta";
        }

        return "redirect:/login";
    }
}