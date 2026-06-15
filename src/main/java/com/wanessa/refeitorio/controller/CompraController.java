/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.service.CompraService;
import com.wanessa.refeitorio.service.ProdutoService;
import com.wanessa.refeitorio.service.UsuarioService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.wanessa.refeitorio.model.Compra;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.wanessa.refeitorio.model.Usuario;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

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
        model.addAttribute("produtos", produtoService.listarAtivos());

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

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Compra nº " + compraSalva.getId()
                    + " registrada com sucesso."
            );

            return "redirect:/compras/comprovante/"
                    + compraSalva.getId();

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

    /**
     * Exibe o comprovante da compra, simulando a impressão térmica.
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/comprovante/{id}")
    public String comprovanteCompra(
            @PathVariable Long id,
            Model model) {

        try {

            Compra compra = service.buscarPorId(id);

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

        return "compra-comprovante";
    }

    @ResponseBody
    @GetMapping("/cliente/{codigoRfid}")
    public ResponseEntity<Map<String, Object>> buscarClientePorRfid(
            @PathVariable String codigoRfid) {

        Map<String, Object> resposta = new HashMap<>();

        try {
            Usuario usuario = usuarioService.buscarPorRfid(codigoRfid);

            if (usuario.getAtivo() == null || !usuario.getAtivo()) {
                resposta.put("erro", true);
                resposta.put("mensagem", "Cliente inativo. Não é possível realizar compra.");
                return ResponseEntity.badRequest().body(resposta);
            }

            BigDecimal saldo = usuario.getSaldo() != null
                    ? usuario.getSaldo()
                    : BigDecimal.ZERO;

            BigDecimal limiteCredito = usuario.getLimiteCredito() != null
                    ? usuario.getLimiteCredito()
                    : BigDecimal.ZERO;

            resposta.put("erro", false);
            resposta.put("id", usuario.getId());
            resposta.put("nome", usuario.getNome());
            resposta.put("email", usuario.getEmail());
            resposta.put("codigoRfid", usuario.getCodigoRfid());
            resposta.put("saldo", saldo);
            resposta.put("limiteCredito", limiteCredito);
            resposta.put("totalDisponivel", saldo.add(limiteCredito));

            return ResponseEntity.ok(resposta);

        } catch (RuntimeException e) {
            resposta.put("erro", true);
            resposta.put("mensagem", e.getMessage());

            return ResponseEntity.badRequest().body(resposta);
        }
    }
}
