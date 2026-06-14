package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.enums.StatusDespesa;
import com.wanessa.refeitorio.model.Despesa;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findAllByOrderByAtivoDescDataVencimentoDesc();

    List<Despesa> findByStatusAndAtivoTrueOrderByDataVencimentoDesc(
            StatusDespesa status
    );

    @Query("""
           SELECT COALESCE(SUM(d.valor), 0)
           FROM Despesa d
           WHERE d.status = 'PAGA'
           AND d.ativo = true
           """)
    BigDecimal calcularTotalDespesasPagas();

    @Query("""
           SELECT COALESCE(SUM(d.valor), 0)
           FROM Despesa d
           WHERE d.status = 'PENDENTE'
           AND d.ativo = true
           """)
    BigDecimal calcularTotalDespesasPendentes();
}