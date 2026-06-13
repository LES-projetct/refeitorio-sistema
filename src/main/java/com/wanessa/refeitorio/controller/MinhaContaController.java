package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.dto.MinhaContaDTO;
import com.wanessa.refeitorio.model.Compra;
import com.wanessa.refeitorio.service.ContaSistemaService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controla a área pessoal do cliente autenticado.
 */
@Controller
public class MinhaContaController {

    private final ContaSistemaService contaSistemaService;

    public MinhaContaController(
            ContaSistemaService contaSistemaService) {

        this.contaSistemaService = contaSistemaService;
    }

    /**
     * Abre a página principal da conta do cliente.
     */
    @GetMapping("/minha-conta")
    public String minhaConta(
            Authentication authentication,
            Model model) {

        try {

            MinhaContaDTO dados
                    = contaSistemaService.consultarMinhaConta(
                            authentication.getName()
                    );

            model.addAttribute(
                    "dados",
                    dados
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "minha-conta";
    }

    /**
     * Lista somente as compras do cliente autenticado.
     */
    @GetMapping("/minha-conta/compras")
    public String minhasCompras(
            Authentication authentication,
            Model model) {

        try {

            List<Compra> compras
                    = contaSistemaService.listarMinhasCompras(
                            authentication.getName()
                    );

            model.addAttribute(
                    "compras",
                    compras
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "minhas-compras";
    }
}
