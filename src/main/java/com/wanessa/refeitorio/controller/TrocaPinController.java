package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.ContaSistema;
import com.wanessa.refeitorio.service.ContaSistemaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TrocaPinController {

    private final ContaSistemaService contaService;

    public TrocaPinController(
            ContaSistemaService contaService) {

        this.contaService = contaService;
    }

    /**
     * Abre a página de definição do novo PIN.
     */
    @GetMapping("/trocar-pin")
    public String telaTrocaPin(
            Authentication authentication) {

        ContaSistema conta = contaService.buscarPorLogin(
                authentication.getName()
        );

        if (!Boolean.TRUE.equals(
                conta.getDeveTrocarSenha())) {

            return "redirect:/inicio";
        }

        return "trocar-pin";
    }

    /**
     * Salva o novo PIN.
     */
    @PostMapping("/trocar-pin")
    public String trocarPin(
            @RequestParam String novoPin,
            @RequestParam String confirmarPin,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            contaService.alterarPinPrimeiroAcesso(
                    authentication.getName(),
                    novoPin,
                    confirmarPin
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "PIN alterado com sucesso."
            );

            return "redirect:/inicio";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            return "trocar-pin";
        }
    }
}