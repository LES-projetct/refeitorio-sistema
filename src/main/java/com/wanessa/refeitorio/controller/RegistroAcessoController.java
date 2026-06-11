/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.RegistroAcesso;
import com.wanessa.refeitorio.service.RegistroAcessoService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author wanes
 */
@Controller
@RequestMapping("/acessos")
public class RegistroAcessoController {

    private final RegistroAcessoService service;

    public RegistroAcessoController(RegistroAcessoService service) {
        this.service = service;
    }

    @ResponseBody
    @PostMapping("/entrada/{codigoRfid}")
    public RegistroAcesso registrarEntrada(@PathVariable String codigoRfid) {
        return service.registrarEntradaPorRfid(codigoRfid);
    }

    @ResponseBody
    @PostMapping("/saida/{codigoRfid}")
    public RegistroAcesso registrarSaida(@PathVariable String codigoRfid) {
        return service.registrarSaidaPorRfid(codigoRfid);
    }

    @ResponseBody
    @GetMapping
    public List<RegistroAcesso> listar() {
        return service.listarTodos();
    }

    @ResponseBody
    @GetMapping("/usuario/{usuarioId}")
    public List<RegistroAcesso> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return service.listarPorUsuario(usuarioId);
    }

    @ResponseBody
    @GetMapping("/bloqueados")
    public List<RegistroAcesso> listarBloqueados() {

        return service.listarBloqueados();
    }

    @GetMapping("/tela")
    public String telaAcessos(Model model) {

        model.addAttribute(
                "acessos",
                service.listarTodos());

        return "acessos";
    }
}
