package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.dto.ResumoSistemaDTO;
import com.wanessa.refeitorio.repository.CompraRepository;
import com.wanessa.refeitorio.repository.RegistroAcessoRepository;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Gera os indicadores apresentados no dashboard.
     */
    @Transactional(readOnly = true)
    public ResumoSistemaDTO gerarResumo() {

        ResumoSistemaDTO dto = new ResumoSistemaDTO();

        /* Todos os usuários cadastrados */
        dto.setTotalUsuarios(
                usuarioRepository.count()
        );

        /* Todas as compras registradas */
        dto.setTotalCompras(
                compraRepository.count()
        );

        /* Soma dos valores de todas as compras */
        BigDecimal faturamento =
                compraRepository.calcularFaturamentoTotal();

        dto.setFaturamentoTotal(
                faturamento != null
                ? faturamento
                : BigDecimal.ZERO
        );

        /*
         * Tentativas de entrada que receberam
         * acessoPermitido = false.
         */
        dto.setAcessosBloqueados(
                registroAcessoRepository
                        .countByAcessoPermitidoFalse()
        );

        return dto;
    }
}