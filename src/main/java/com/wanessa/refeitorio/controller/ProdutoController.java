/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.Produto;
import com.wanessa.refeitorio.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 *
 * @author wanes
 */
@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @ResponseBody
    @GetMapping
    public List<Produto> listar() {
        return service.listarTodos();
    }

    @ResponseBody
    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return service.salvar(produto);
    }

    @GetMapping("/tela")
    public String telaProdutos(Model model) {

        model.addAttribute(
                "produtos",
                service.listarTodos());

        return "produtos";
    }

    @GetMapping("/novo")
    public String novoProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto-form";
    }

    @PostMapping("/salvar")
    public String salvarProdutoForm(@ModelAttribute Produto produto) {
        if (produto.getAtivo() == null) {
            produto.setAtivo(true);
        }
        service.salvar(produto);
        return "redirect:/produtos/tela";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {

        service.excluir(id);

        return "redirect:/produtos/tela";
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {

        Produto produto = service.buscarPorId(id);

        model.addAttribute("produto", produto);

        return "produto-form";
    }

}
