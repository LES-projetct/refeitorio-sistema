/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author wanes
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByCodigoRfid(String codigoRfid);
    
    boolean existsByCodigoRfid(String codigoRfid);
    
    List<Usuario> findBySaldoLessThan(BigDecimal saldo);

    List<Usuario> findByAtivoFalse();
}
