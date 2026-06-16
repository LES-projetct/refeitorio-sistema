/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.dto.ResultadoCadastroUsuarioDTO;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.CadastroUsuarioService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author wanes
 */
@Controller
@RequestMapping("/cadastro-cliente")
public class CadastroClienteController {

    private final CadastroUsuarioService cadastroUsuarioService;

    public CadastroClienteController(
            CadastroUsuarioService cadastroUsuarioService) {

        this.cadastroUsuarioService = cadastroUsuarioService;
    }

    @GetMapping
    public String abrirFormulario(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "cadastro-cliente";
    }

    @PostMapping("/salvar")
    public String salvarCadastro(
            @ModelAttribute Usuario usuario,
            Model model) {

        try {
            usuario.setAtivo(true);
            usuario.setSaldo(BigDecimal.ZERO);
            usuario.setLimiteCredito(BigDecimal.valueOf(50));

            ResultadoCadastroUsuarioDTO resultado
                    = cadastroUsuarioService.salvar(usuario);

            model.addAttribute("cadastroRealizado", true);
            model.addAttribute("usuario", resultado.usuario());
            model.addAttribute("credencial", resultado.credencialInicial());

            return "cadastro-cliente-sucesso";

        } catch (RuntimeException e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);

            return "cadastro-cliente";
        }
    }
}
