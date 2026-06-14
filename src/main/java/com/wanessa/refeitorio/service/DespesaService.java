/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.wanessa.refeitorio.enums.StatusDespesa;
import com.wanessa.refeitorio.model.Despesa;
import com.wanessa.refeitorio.repository.DespesaRepository;
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
public class DespesaService {

    private final DespesaRepository repository;

    public DespesaService(DespesaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Despesa> listarTodas() {
        return repository.findAllByOrderByAtivoDescDataVencimentoDesc();
    }

    @Transactional(readOnly = true)
    public Despesa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException("Despesa não encontrada")
                );
    }

    @Transactional
    public Despesa salvar(Despesa despesa) {

        validar(despesa);

        if (despesa.getStatus() == null) {
            despesa.setStatus(StatusDespesa.PENDENTE);
        }

        if (despesa.getStatus() == StatusDespesa.PENDENTE) {
            despesa.setDataPagamento(null);
        }

        if (despesa.getStatus() == StatusDespesa.PAGA
                && despesa.getDataPagamento() == null) {

            despesa.setDataPagamento(LocalDate.now());
        }

        if (despesa.getAtivo() == null) {
            despesa.setAtivo(true);
        }

        return repository.save(despesa);
    }

    @Transactional
    public void marcarComoPaga(Long id) {

        Despesa despesa = buscarPorId(id);

        despesa.setStatus(StatusDespesa.PAGA);
        despesa.setDataPagamento(LocalDate.now());

        repository.save(despesa);
    }

    @Transactional
    public void marcarComoPendente(Long id) {

        Despesa despesa = buscarPorId(id);

        despesa.setStatus(StatusDespesa.PENDENTE);
        despesa.setDataPagamento(null);

        repository.save(despesa);
    }

    @Transactional
    public void desativar(Long id) {

        Despesa despesa = buscarPorId(id);

        despesa.setAtivo(false);

        repository.save(despesa);
    }

    @Transactional
    public void reativar(Long id) {

        Despesa despesa = buscarPorId(id);

        despesa.setAtivo(true);

        repository.save(despesa);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalDespesasPagas() {

        BigDecimal total = repository.calcularTotalDespesasPagas();

        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalDespesasPendentes() {

        BigDecimal total = repository.calcularTotalDespesasPendentes();

        return total != null ? total : BigDecimal.ZERO;
    }

    private void validar(Despesa despesa) {

        if (despesa == null) {
            throw new IllegalArgumentException("Despesa não informada");
        }

        if (despesa.getFornecedor() == null
                || despesa.getFornecedor().isBlank()) {

            throw new IllegalArgumentException("O fornecedor é obrigatório");
        }

        if (despesa.getDescricao() == null
                || despesa.getDescricao().isBlank()) {

            throw new IllegalArgumentException("A descrição é obrigatória");
        }

        if (despesa.getValor() == null
                || despesa.getValor().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }

        if (despesa.getDataVencimento() == null) {
            throw new IllegalArgumentException("A data de vencimento é obrigatória");
        }
    }
}
