package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.dto.MinhaContaDTO;
import com.wanessa.refeitorio.model.Compra;
import com.wanessa.refeitorio.service.ContaSistemaService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.wanessa.refeitorio.model.RegistroAcesso;
import org.springframework.web.bind.annotation.PathVariable;
import com.wanessa.refeitorio.model.Pagamento;

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
     *
     * @param authentication
     * @param model
     * @return
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
     *
     * @param authentication
     * @param model
     * @return
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

    /**
     * Exibe os detalhes de uma compra específica pertencente ao cliente
     * autenticado.
     */
    @GetMapping("/minha-conta/compras/{id}")
    public String detalhesMinhaCompra(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        try {

            Compra compra
                    = contaSistemaService.buscarMinhaCompraPorId(
                            authentication.getName(),
                            id
                    );

            model.addAttribute(
                    "compra",
                    compra
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "minha-compra-detalhes";
    }

    /**
     * Lista somente os acessos do cliente autenticado.
     *
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/minha-conta/acessos")
    public String meusAcessos(
            Authentication authentication,
            Model model) {

        try {

            List<RegistroAcesso> acessos
                    = contaSistemaService.listarMeusAcessos(
                            authentication.getName()
                    );

            model.addAttribute(
                    "acessos",
                    acessos
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "meus-acessos";
    }

    /**
     * Lista somente os pagamentos do cliente autenticado.
     */
    @GetMapping("/minha-conta/pagamentos")
    public String meusPagamentos(
            Authentication authentication,
            Model model) {

        try {

            List<Pagamento> pagamentos
                    = contaSistemaService.listarMeusPagamentos(
                            authentication.getName()
                    );

            model.addAttribute(
                    "pagamentos",
                    pagamentos
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "meus-pagamentos";
    }
}
