package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RelatorioController {

    private final UsuarioService usuarioService;

    public RelatorioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    /**
     * Relatório de clientes devedores há mais de 30 dias.
     */
    @GetMapping("/relatorios/devedores")
    public String relatorioDevedores(Model model) {

        List<Usuario> devedores =
                usuarioService.listarDevedoresMaisDe30Dias();

        model.addAttribute(
                "devedores",
                devedores
        );

        return "relatorio-devedores";
    }
}