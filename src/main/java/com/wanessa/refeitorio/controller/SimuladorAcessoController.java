package com.wanessa.refeitorio.controller;

import com.wanessa.refeitorio.model.RegistroAcesso;
import com.wanessa.refeitorio.service.RegistroAcessoService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/simulador-acesso")
public class SimuladorAcessoController {

    private final RegistroAcessoService registroAcessoService;

    public SimuladorAcessoController(
            RegistroAcessoService registroAcessoService) {

        this.registroAcessoService = registroAcessoService;
    }

    @GetMapping
    public String abrirSimulador() {
        return "simulador-acesso";
    }

    @ResponseBody
    @PostMapping("/entrada/{codigoRfid}")
    public ResponseEntity<Map<String, Object>> registrarEntrada(
            @PathVariable String codigoRfid) {

        try {
            RegistroAcesso registro =
                    registroAcessoService.registrarEntradaPorRfid(codigoRfid);

            boolean permitido =
                    Boolean.TRUE.equals(registro.getAcessoPermitido());

            Map<String, Object> resposta = new HashMap<>();

            resposta.put("permitido", permitido);
            resposta.put("registroId", registro.getId());
            resposta.put("codigoRfid", registro.getUsuario().getCodigoRfid());
            resposta.put("nome", registro.getUsuario().getNome());
            resposta.put("email", registro.getUsuario().getEmail() != null
                    ? registro.getUsuario().getEmail()
                    : "E-mail não cadastrado");

            if (permitido) {
                resposta.put("mensagem",
                        "Acesso liberado para "
                        + registro.getUsuario().getNome() + ".");
            } else {
                resposta.put("mensagem",
                        "Acesso bloqueado: "
                        + registro.getMotivoBloqueio() + ".");
                resposta.put("motivo", registro.getMotivoBloqueio());
            }

            return ResponseEntity.ok(resposta);

        } catch (ResponseStatusException e) {

            Map<String, Object> resposta = new HashMap<>();

            resposta.put("permitido", false);
            resposta.put("mensagem", e.getReason() != null
                    ? e.getReason()
                    : "Não foi possível registrar a entrada.");

            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(resposta);
        }
    }

    @ResponseBody
    @PostMapping("/saida/{codigoRfid}")
    public ResponseEntity<Map<String, Object>> registrarSaida(
            @PathVariable String codigoRfid) {

        try {
            RegistroAcesso registro =
                    registroAcessoService.registrarSaidaPorRfid(codigoRfid);

            Map<String, Object> resposta = new HashMap<>();

            resposta.put("sucesso", true);
            resposta.put("registroId", registro.getId());
            resposta.put("codigoRfid", registro.getUsuario().getCodigoRfid());
            resposta.put("nome", registro.getUsuario().getNome());
            resposta.put("mensagem",
                    "Saída registrada para "
                    + registro.getUsuario().getNome() + ".");

            return ResponseEntity.ok(resposta);

        } catch (ResponseStatusException e) {

            Map<String, Object> resposta = new HashMap<>();

            resposta.put("sucesso", false);
            resposta.put("mensagem", e.getReason() != null
                    ? e.getReason()
                    : "Não foi possível registrar a saída.");

            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(resposta);
        }
    }
}