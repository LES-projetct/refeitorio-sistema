/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.service.ContaSistemaService;
import com.wanessa.refeitorio.service.QrCodeService;
import java.security.Principal;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author wanes
 */
@Controller
public class ClienteQrCodeController {

    private final ContaSistemaService contaSistemaService;
    private final QrCodeService qrCodeService;

    public ClienteQrCodeController(
            ContaSistemaService contaSistemaService,
            QrCodeService qrCodeService) {

        this.contaSistemaService = contaSistemaService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/minha-conta/qrcode")
    public ResponseEntity<byte[]> gerarQrCodeCliente(
            Principal principal) {

        Usuario usuario
                = contaSistemaService.buscarUsuarioClientePorLogin(
                        principal.getName()
                );

        String codigoRfid = usuario.getCodigoRfid();

        byte[] imagem = qrCodeService.gerarQrCodePng(
                codigoRfid,
                240,
                240
        );

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imagem);
    }
}
