package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Compra;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByUsuarioIdOrderByDataHoraDesc(
            Long usuarioId
    );

    @Query("SELECT COALESCE(SUM(c.valorTotal), 0) FROM Compra c")
    BigDecimal calcularFaturamentoTotal();

    boolean existsByUsuarioId(Long usuarioId);

    @Query("""
           SELECT DISTINCT c
           FROM Compra c
           LEFT JOIN FETCH c.usuario u
           LEFT JOIN FETCH c.itens i
           LEFT JOIN FETCH i.produto p
           WHERE c.id = :id
           """)
    Optional<Compra> buscarDetalhesPorId(
            @Param("id") Long id
    );

    Optional<Compra> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );

    @Query("""
           SELECT DISTINCT c
           FROM Compra c
           LEFT JOIN FETCH c.usuario u
           LEFT JOIN FETCH c.itens i
           LEFT JOIN FETCH i.produto p
           WHERE c.id = :compraId
           AND u.id = :usuarioId
           """)
    Optional<Compra> buscarDetalhesDoCliente(
            @Param("compraId") Long compraId,
            @Param("usuarioId") Long usuarioId
    );
}