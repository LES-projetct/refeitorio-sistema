/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Compra;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 *
 * @author wanes
 */
public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByUsuarioIdOrderByDataHoraDesc(
            Long usuarioId
    );

    @Query("SELECT COALESCE(SUM(c.valorTotal), 0) FROM Compra c")
    BigDecimal calcularFaturamentoTotal();

    long count();

    boolean existsByUsuarioId(Long usuarioId);

    @Query("""
           SELECT DISTINCT c
           FROM Compra c
           LEFT JOIN FETCH c.usuario
           LEFT JOIN FETCH c.itens i
           LEFT JOIN FETCH i.produto
           WHERE c.id = :id
           """)
    Optional<Compra> buscarDetalhesPorId(@Param("id") Long id);

    Optional<Compra> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );
}
