/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.enums.FormaPagamento;
import com.wanessa.refeitorio.model.Pagamento;
import com.wanessa.refeitorio.model.Usuario;
import com.wanessa.refeitorio.repository.PagamentoRepository;
import com.wanessa.refeitorio.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author wanes
 */
@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            UsuarioRepository usuarioRepository) {

        this.pagamentoRepository = pagamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Lista todos os pagamentos registrados.
     * @return 
     */
    @Transactional(readOnly = true)
    public List<Pagamento> listarTodos() {

        return pagamentoRepository
                .findAllByOrderByDataHoraDesc();
    }

    /**
     * Registra um pagamento e atualiza o saldo do usuário.
     * @param usuarioId
     * @param valor
     * @param formaPagamento
     * @param observacao
     * @return 
     */
    @Transactional
    public Pagamento registrarPagamento(
            Long usuarioId,
            BigDecimal valor,
            FormaPagamento formaPagamento,
            String observacao) {

        if (usuarioId == null) {
            throw new IllegalArgumentException(
                    "Usuário não informado"
            );
        }

        if (valor == null
                || valor.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "O valor do pagamento deve ser maior que zero"
            );
        }

        if (formaPagamento == null) {
            throw new IllegalArgumentException(
                    "Forma de pagamento não informada"
            );
        }

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Usuário não encontrado"
                )
                );

        BigDecimal saldoAnterior
                = usuario.getSaldo() != null
                ? usuario.getSaldo()
                : BigDecimal.ZERO;

        BigDecimal saldoAtualizado
                = saldoAnterior.add(valor);

        usuario.setSaldo(saldoAtualizado);

        /*
         * Atualiza a data do último pagamento.
         */
        usuario.setDataUltimoPagamento(
                LocalDate.now()
        );

        usuarioRepository.save(usuario);

        Pagamento pagamento = new Pagamento();
        pagamento.setUsuario(usuario);
        pagamento.setValor(valor);
        pagamento.setSaldoAnterior(saldoAnterior);
        pagamento.setSaldoAtualizado(saldoAtualizado);
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setObservacao(observacao);

        return pagamentoRepository.save(pagamento);
    }
}
