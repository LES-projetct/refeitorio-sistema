/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.model.RegistroAcesso;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.repository.RegistroAcessoRepository;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author wanes
 */
@Service
public class RegistroAcessoService {

    public final RegistroAcessoRepository registroAcessoRepository;
    private final UsuarioRepository usuarioRepository;

    public RegistroAcessoService(RegistroAcessoRepository registroAcessoRepository,
            UsuarioRepository usuarioRepository) {
        this.registroAcessoRepository = registroAcessoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public RegistroAcesso registrarEntradaPorRfid(String codigoRfid) {

        Usuario usuario = usuarioRepository.findByCodigoRfid(codigoRfid)
                .orElseThrow(() -> new RuntimeException("Cartão RFID não encontrado"));

        RegistroAcesso registro = new RegistroAcesso();
        registro.setUsuario(usuario);
        registro.setDataHoraEntrada(LocalDateTime.now());

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            registro.setAcessoPermitido(false);
            registro.setMotivoBloqueio("Usuário inativo");
            return registroAcessoRepository.save(registro);
        }

        BigDecimal limiteCredito = usuario.getLimiteCredito() != null
                ? usuario.getLimiteCredito()
                : BigDecimal.ZERO;

        BigDecimal limiteNegativo = limiteCredito.negate();

        if (usuario.getSaldo() == null
                || usuario.getSaldo().compareTo(limiteNegativo) < 0) {

            registro.setAcessoPermitido(false);
            registro.setMotivoBloqueio("Limite de crédito excedido");

            return registroAcessoRepository.save(registro);
        }

        registro.setAcessoPermitido(true);
        registro.setMotivoBloqueio(null);

        return registroAcessoRepository.save(registro);
    }

    public RegistroAcesso registrarSaidaPorRfid(String codigoRfid) {

        Usuario usuario = usuarioRepository.findByCodigoRfid(codigoRfid)
                .orElseThrow(() -> new RuntimeException("Cartão RFID não encontrado"));

        RegistroAcesso registro = registroAcessoRepository
                .findTopByUsuarioIdAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Nenhuma entrada encontrada"));

        registro.setDataHoraSaida(LocalDateTime.now());

        return registroAcessoRepository.save(registro);
    }

    public List<RegistroAcesso> listarTodos() {
        return registroAcessoRepository
                .findAllByOrderByDataHoraEntradaDesc();
    }

    public List<RegistroAcesso> listarPorUsuario(Long usuarioId) {

        return registroAcessoRepository
                .findByUsuarioIdOrderByDataHoraEntradaDesc(usuarioId);
    }

    public List<RegistroAcesso> listarBloqueados() {

        return registroAcessoRepository
                .findByAcessoPermitidoFalseOrderByDataHoraEntradaDesc();
    }
}
