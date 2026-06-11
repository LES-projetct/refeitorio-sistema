/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.Compra;
import com.wanessa.refeitorio.service.CompraService;
import com.wanessa.refeitorio.service.ProdutoService;
import com.wanessa.refeitorio.service.UsuarioService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author wanes
 */
@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService service;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;

    public CompraController(
            CompraService service,
            UsuarioService usuarioService,
            ProdutoService produtoService) {

        this.service = service;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
    }

    @ResponseBody
    @PostMapping
    public Compra salvar(@RequestBody Compra compra) {
        return service.salvarCompra(compra);
    }

    @ResponseBody
    @GetMapping
    public List<Compra> listar() {
        return service.listarTodas();
    }

    @ResponseBody
    @GetMapping("/faturamento")
    public BigDecimal faturamento() {
        return service.calcularFaturamentoTotal();
    }

    @ResponseBody
    @GetMapping("/quantidade")
    public long quantidadeCompras() {
        return service.quantidadeCompras();
    }

    @GetMapping("/tela")
    public String telaCompras(Model model) {

        model.addAttribute(
                "compras",
                service.listarTodas());

        return "compras";
    }

    @GetMapping("/nova")
    public String novaCompra(Model model) {

        model.addAttribute("compra", new Compra());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("produtos", produtoService.listarTodos());

        return "compra-form";
    }

    @PostMapping("/salvar")
    public String salvarCompraForm(
            @ModelAttribute Compra compra,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            Compra compraSalva
                    = service.salvarCompra(compra);

            /*
         * A mensagem permanece disponível
         * depois do redirecionamento.
             */
            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Compra nº " + compraSalva.getId()
                    + " registrada com sucesso."
            );

            return "redirect:/compras/tela";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "compra",
                    compra
            );

            model.addAttribute(
                    "usuarios",
                    usuarioService.listarTodos()
            );

            /*
         * Esta lista também precisa ser carregada novamente,
         * pois a página possui o campo de produtos.
             */
            model.addAttribute(
                    "produtos",
                    produtoService.listarTodos()
            );

            return "compra-form";

        } catch (Exception e) {

            model.addAttribute(
                    "erro",
                    "Não foi possível registrar a compra."
            );

            model.addAttribute(
                    "compra",
                    compra
            );

            model.addAttribute(
                    "usuarios",
                    usuarioService.listarTodos()
            );

            model.addAttribute(
                    "produtos",
                    produtoService.listarTodos()
            );

            return "compra-form";
        }
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesCompra(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            Compra compra = service.buscarPorId(id);

            model.addAttribute("compra", compra);

            return "compra-detalhes";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );

            return "redirect:/compras/tela";
        }
    }
}
