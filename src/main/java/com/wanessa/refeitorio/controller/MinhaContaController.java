package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.dto.MinhaContaDTO;
import com.wanessa.refeitorio.model.Compra;
import com.wanessa.refeitorio.service.ContaSistemaService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.wanessa.refeitorio.model.RegistroAcesso;
import org.springframework.web.bind.annotation.PathVariable;
import com.wanessa.refeitorio.model.Pagamento;

import com.wanessa.refeitorio.enums.FormaPagamento;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.PagamentoService;
import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controla a área pessoal do cliente autenticado.
 */
@Controller
public class MinhaContaController {

    private final ContaSistemaService contaSistemaService;
    private final PagamentoService pagamentoService;

    public MinhaContaController(
            ContaSistemaService contaSistemaService,
            PagamentoService pagamentoService) {

        this.contaSistemaService = contaSistemaService;
        this.pagamentoService = pagamentoService;
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
     *
     * @return
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

    /**
     * Exibe o comprovante de uma compra do cliente autenticado.
     *
     * @param id
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/minha-conta/compras/{id}/comprovante")
    public String comprovanteMinhaCompra(
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

        return "minha-compra-comprovante";
    }

    @GetMapping("/minha-conta/recarregar")
    public String abrirRecargaCliente(Model model) {

        model.addAttribute("formasPagamento", new FormaPagamento[]{
            FormaPagamento.PIX,
            FormaPagamento.CARTAO_CREDITO,
            FormaPagamento.CARTAO_DEBITO
        });

        return "minha-conta-recarregar";
    }

    @PostMapping("/minha-conta/recarregar")
    public String recarregarCliente(
            @RequestParam BigDecimal valor,
            @RequestParam FormaPagamento formaPagamento,
            Principal principal,
            Model model) {

        try {
            Usuario usuario
                    = contaSistemaService.buscarUsuarioClientePorLogin(
                            principal.getName()
                    );

            pagamentoService.recarregarCliente(
                    usuario,
                    valor,
                    formaPagamento
            );

            return "redirect:/minha-conta/pagamentos?recargaSucesso";

        } catch (RuntimeException e) {

            model.addAttribute("erro", e.getMessage());

            model.addAttribute("formasPagamento", new FormaPagamento[]{
                FormaPagamento.PIX,
                FormaPagamento.CARTAO_CREDITO,
                FormaPagamento.CARTAO_DEBITO
            });

            return "minha-conta-recarregar";
        }
    }
}
