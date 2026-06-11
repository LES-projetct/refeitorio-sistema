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
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Map<String, Object>> registrarEntrada(
            @PathVariable String codigoRfid) {

        try {

            RegistroAcesso registro
                    = service.registrarEntradaPorRfid(codigoRfid);

            boolean permitido
                    = Boolean.TRUE.equals(registro.getAcessoPermitido());

            String mensagem;

            if (permitido) {
                mensagem = "Entrada liberada para "
                        + registro.getUsuario().getNome() + ".";
            } else {
                mensagem = "Acesso bloqueado: "
                        + registro.getMotivoBloqueio() + ".";
            }

            return ResponseEntity.ok(
                    Map.of(
                            "permitido", permitido,
                            "mensagem", mensagem,
                            "registroId", registro.getId()
                    )
            );

        } catch (ResponseStatusException e) {

            String mensagem = e.getReason() != null
                    ? e.getReason()
                    : "Não foi possível registrar a entrada.";

            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(
                            Map.of(
                                    "permitido", false,
                                    "mensagem", mensagem
                            )
                    );
        }
    }

    @ResponseBody
    @PostMapping("/saida/{codigoRfid}")
    public ResponseEntity<Map<String, Object>> registrarSaida(
            @PathVariable String codigoRfid) {

        try {

            RegistroAcesso registro
                    = service.registrarSaidaPorRfid(codigoRfid);

            return ResponseEntity.ok(
                    Map.of(
                            "sucesso", true,
                            "mensagem",
                            "Saída registrada para "
                            + registro.getUsuario().getNome() + ".",
                            "registroId", registro.getId()
                    )
            );

        } catch (ResponseStatusException e) {

            String mensagem = e.getReason() != null
                    ? e.getReason()
                    : "Não foi possível registrar a saída.";

            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(
                            Map.of(
                                    "sucesso", false,
                                    "mensagem", mensagem
                            )
                    );
        }
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
