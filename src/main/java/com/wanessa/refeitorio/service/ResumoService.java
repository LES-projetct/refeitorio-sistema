/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.dto.ResumoSistemaDTO;
import com.wanessa.refeitorio.repository.CompraRepository;
import com.wanessa.refeitorio.repository.RegistroAcessoRepository;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author wanes
 */
@Service
public class ResumoService {

    private final UsuarioRepository usuarioRepository;
    private final CompraRepository compraRepository;
    private final RegistroAcessoRepository registroAcessoRepository;

    public ResumoService(
            UsuarioRepository usuarioRepository,
            CompraRepository compraRepository,
            RegistroAcessoRepository registroAcessoRepository) {

        this.usuarioRepository = usuarioRepository;
        this.compraRepository = compraRepository;
        this.registroAcessoRepository = registroAcessoRepository;
    }

    public ResumoSistemaDTO gerarResumo() {

        ResumoSistemaDTO dto = new ResumoSistemaDTO();

        dto.setTotalUsuarios(usuarioRepository.count());

        dto.setTotalCompras(compraRepository.count());

        dto.setFaturamentoTotal(
                compraRepository.calcularFaturamentoTotal());

        dto.setAcessosBloqueados(
                registroAcessoRepository.countByAcessoPermitidoFalse());

        return dto;
    }
}
