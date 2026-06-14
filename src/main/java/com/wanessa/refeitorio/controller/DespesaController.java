/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.enums.StatusDespesa;
import com.wanessa.refeitorio.model.Despesa;
import com.wanessa.refeitorio.service.DespesaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author wanes
 */
@Controller
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService service;

    public DespesaController(DespesaService service) {
        this.service = service;
    }

    @GetMapping("/tela")
    public String telaDespesas(Model model) {

        model.addAttribute("despesas", service.listarTodas());

        return "despesas";
    }

    @GetMapping("/nova")
    public String novaDespesa(Model model) {

        Despesa despesa = new Despesa();

        model.addAttribute("despesa", despesa);
        model.addAttribute("statusDespesa", StatusDespesa.values());

        return "despesa-form";
    }

    @GetMapping("/editar/{id}")
    public String editarDespesa(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("despesa", service.buscarPorId(id));
        model.addAttribute("statusDespesa", StatusDespesa.values());

        return "despesa-form";
    }

    @PostMapping("/salvar")
    public String salvarDespesa(
            @ModelAttribute Despesa despesa,
            Model model) {

        try {

            service.salvar(despesa);

            return "redirect:/despesas/tela";

        } catch (IllegalArgumentException e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("despesa", despesa);
            model.addAttribute("statusDespesa", StatusDespesa.values());

            return "despesa-form";
        }
    }

    @PostMapping("/pagar/{id}")
    public String marcarComoPaga(
            @PathVariable Long id) {

        service.marcarComoPaga(id);

        return "redirect:/despesas/tela";
    }

    @PostMapping("/pendente/{id}")
    public String marcarComoPendente(
            @PathVariable Long id) {

        service.marcarComoPendente(id);

        return "redirect:/despesas/tela";
    }

    @PostMapping("/desativar/{id}")
    public String desativarDespesa(
            @PathVariable Long id) {

        service.desativar(id);

        return "redirect:/despesas/tela";
    }

    @PostMapping("/reativar/{id}")
    public String reativarDespesa(
            @PathVariable Long id) {

        service.reativar(id);

        return "redirect:/despesas/tela";
    }

}
