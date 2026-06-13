package com.wanessa.refeitorio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author wanes
 */
@Controller
public class LoginController {

    /**
     * Abre a página personalizada de autenticação.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
