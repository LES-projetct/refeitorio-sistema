/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.service.ResumoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author wanes
 */
@Controller
public class HomeController {

    private final ResumoService resumoService;

    public HomeController(ResumoService resumoService) {
        this.resumoService = resumoService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "resumo",
                resumoService.gerarResumo());

        return "index";
    }
}
