package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.Produto;
import com.wanessa.refeitorio.service.ProdutoService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        model.addAttribute("produtos", service.listarTodos());

        return "produtos";
    }

    @GetMapping("/novo")
    public String novoProduto(Model model) {

        Produto produto = new Produto();
        produto.setAtivo(true);

        model.addAttribute("produto", produto);

        return "produto-form";
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {

        Produto produto = service.buscarPorId(id);

        model.addAttribute("produto", produto);

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

    @PostMapping("/desativar/{id}")
    public String desativarProduto(@PathVariable Long id) {

        service.desativar(id);

        return "redirect:/produtos/tela";
    }

    @PostMapping("/reativar/{id}")
    public String reativarProduto(@PathVariable Long id) {

        service.reativar(id);

        return "redirect:/produtos/tela";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProdutoAntigo(@PathVariable Long id) {

        service.desativar(id);

        return "redirect:/produtos/tela";
    }
}