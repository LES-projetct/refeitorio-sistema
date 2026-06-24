/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.enums.PerfilAcesso;
import com.wanessa.refeitorio.model.ContaSistema;
import com.wanessa.refeitorio.service.ContaSistemaService;
import com.wanessa.refeitorio.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.wanessa.refeitorio.model.ContaSistema;
import com.wanessa.refeitorio.model.Usuario;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author wanes
 */
/**
 * Controla o cadastro e gerenciamento das contas utilizadas para entrar no
 * sistema.
 *
 * As rotas /contas/** estão protegidas no SecurityConfig e podem ser acessadas
 * somente pelo administrador.
 */
@Controller
@RequestMapping("/contas")
public class ContaSistemaController {

    private final ContaSistemaService contaService;
    private final UsuarioService usuarioService;

    public ContaSistemaController(
            ContaSistemaService contaService,
            UsuarioService usuarioService) {

        this.contaService = contaService;
        this.usuarioService = usuarioService;
    }

    /**
     * Exibe todas as contas cadastradas.
     */
    @GetMapping("/tela")
    public String telaContas(Model model) {

        model.addAttribute(
                "contas",
                contaService.listarTodas()
        );

        return "contas";
    }

    /**
     * Abre o formulário de cadastro.
     */
    @GetMapping("/nova")
    public String novaConta(Model model) {

        ContaSistema conta = new ContaSistema();

        conta.setAtivo(true);

        model.addAttribute(
                "conta",
                conta
        );

        carregarDadosFormulario(model);

        return "conta-form";
    }

    /**
     * Abre o formulário de edição.
     */
    @GetMapping("/editar/{id}")
    public String editarConta(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            ContaSistema existente
                    = contaService.buscarPorId(id);

            /*
             * Cria outro objeto para não colocar
             * a senha criptografada dentro do formulário.
             */
            ContaSistema contaFormulario
                    = new ContaSistema();

            contaFormulario.setId(
                    existente.getId()
            );

            contaFormulario.setNome(
                    existente.getNome()
            );

            contaFormulario.setLogin(
                    existente.getLogin()
            );

            contaFormulario.setPerfil(
                    existente.getPerfil()
            );

            contaFormulario.setAtivo(
                    existente.getAtivo()
            );

            contaFormulario.setUsuarioRelacionado(
                    existente.getUsuarioRelacionado()
            );

            /*
             * Senha vazia significa manter a senha atual.
             */
            contaFormulario.setSenha("");

            model.addAttribute(
                    "conta",
                    contaFormulario
            );

            carregarDadosFormulario(model);

            return "conta-form";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );

            return "redirect:/contas/tela";
        }
    }

    /**
     * Salva uma conta nova ou editada.
     */
    @PostMapping("/salvar")
    public String salvarConta(
            @ModelAttribute ContaSistema conta,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            boolean novaConta
                    = conta.getId() == null;

            contaService.salvar(conta);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    novaConta
                            ? "Conta criada com sucesso."
                            : "Conta atualizada com sucesso."
            );

            return "redirect:/contas/tela";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "conta",
                    conta
            );

            carregarDadosFormulario(model);

            return "conta-form";
        }
    }

    /**
     * Desativa uma conta sem apagá-la.
     */
    @PostMapping("/desativar/{id}")
    public String desativarConta(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            contaService.desativar(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Conta desativada com sucesso."
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "redirect:/contas/tela";
    }

    /**
     * Reativa uma conta desativada.
     */
    @PostMapping("/reativar/{id}")
    public String reativarConta(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            contaService.reativar(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Conta reativada com sucesso."
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "redirect:/contas/tela";
    }

    /**
     * Carrega as opções necessárias para o formulário.
     */
    private void carregarDadosFormulario(Model model) {

        model.addAttribute(
                "perfis",
                PerfilAcesso.values()
        );

        model.addAttribute(
                "usuarios",
                usuarioService.listarTodos()
        );
    }
}
