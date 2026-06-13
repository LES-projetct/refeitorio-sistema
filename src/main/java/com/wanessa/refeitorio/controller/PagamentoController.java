/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.enums.FormaPagamento;
import com.wanessa.refeitorio.model.Pagamento;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.PagamentoService;
import com.wanessa.refeitorio.service.UsuarioService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author wanes
 */
@Controller
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final UsuarioService usuarioService;

    public PagamentoController(
            PagamentoService pagamentoService,
            UsuarioService usuarioService) {

        this.pagamentoService = pagamentoService;
        this.usuarioService = usuarioService;
    }

    /**
     * Abre a tela de pagamentos e recarga de saldo.
     */
    @GetMapping("/pagamentos/tela")
    public String telaPagamentos(Model model) {

        List<Pagamento> pagamentos
                = pagamentoService.listarTodos();

        List<Usuario> usuarios
                = usuarioService.listarTodos();

        model.addAttribute("pagamentos", pagamentos);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("formasPagamento", FormaPagamento.values());

        return "pagamentos";
    }

    /**
     * Registra pagamento e atualiza saldo do usuário.
     */
    @PostMapping("/pagamentos/registrar")
    public String registrarPagamento(
            @RequestParam Long usuarioId,
            @RequestParam BigDecimal valor,
            @RequestParam FormaPagamento formaPagamento,
            @RequestParam(required = false) String observacao,
            RedirectAttributes redirectAttributes) {

        try {

            pagamentoService.registrarPagamento(
                    usuarioId,
                    valor,
                    formaPagamento,
                    observacao
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Pagamento registrado com sucesso."
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "redirect:/pagamentos/tela";
    }
}
