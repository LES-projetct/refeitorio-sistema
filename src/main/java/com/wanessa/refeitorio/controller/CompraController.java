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
            Model model) {

        try {

            service.salvarCompra(compra);

            return "redirect:/compras/tela";

        } catch (Exception e) {

            model.addAttribute("erro", e.getMessage());

            model.addAttribute("compra", compra);

            model.addAttribute(
                    "usuarios",
                    usuarioService.listarTodos());

            return "compra-form";
        }
    }
}
