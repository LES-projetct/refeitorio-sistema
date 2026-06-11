/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.dto.ResumoSistemaDTO;
import com.wanessa.refeitorio.service.ResumoService;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author wanes
 */
@RestController
@RequestMapping("/resumo")
public class ResumoController {
    private final ResumoService service;

    public ResumoController(ResumoService service) {
        this.service = service;
    }

    @GetMapping
    public ResumoSistemaDTO resumo() {
        return service.gerarResumo();
    }
}
