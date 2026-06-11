/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.RegistroAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

/**
 *
 * @author wanes
 */
public interface RegistroAcessoRepository extends JpaRepository<RegistroAcesso, Long> {
    
    Optional<RegistroAcesso> findTopByUsuarioIdAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(Long usuarioId);
    
    List<RegistroAcesso> findAllByOrderByDataHoraEntradaDesc();

    List<RegistroAcesso> findByUsuarioIdOrderByDataHoraEntradaDesc(Long usuarioId);

    List<RegistroAcesso> findByAcessoPermitidoFalseOrderByDataHoraEntradaDesc();

    long countByAcessoPermitidoFalse();
    
    
}
