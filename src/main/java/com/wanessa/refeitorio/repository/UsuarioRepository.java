/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author wanes
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCodigoRfid(String codigoRfid);

    List<Usuario> findBySaldoLessThan(BigDecimal saldo);

    List<Usuario> findByAtivoFalse();

    Optional<Usuario> findByCodigoRfidIgnoreCase(String codigoRfid);

    Optional<Usuario> findByCodigoRfid(String codigoRfid);

    boolean existsByCodigoRfidIgnoreCase(String codigoRfid);

    /*
 * Lista usuários devedores:
 * saldo negativo e sem pagamento recente.
     */
    List<Usuario> findBySaldoLessThanAndDataUltimoPagamentoBeforeOrderBySaldoAsc(
            BigDecimal saldo,
            LocalDate dataLimite
    );

    /*
 * Lista usuários com saldo negativo
 * que nunca realizaram pagamento.
     */
    List<Usuario> findBySaldoLessThanAndDataUltimoPagamentoIsNullOrderBySaldoAsc(
            BigDecimal saldo
    );

}
