/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Compra;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author wanes
 */
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT COALESCE(SUM(c.valorTotal), 0) FROM Compra c")
    BigDecimal calcularFaturamentoTotal();
    
    long count();
    
    boolean existsByUsuarioId(Long usuarioId);

}
