/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Pagamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author wanes
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    /*
     * Lista todos os pagamentos, começando pelos mais recentes.
     */
    List<Pagamento> findAllByOrderByDataHoraDesc();

    /*
     * Lista pagamentos de um usuário específico.
     */
    List<Pagamento> findByUsuarioIdOrderByDataHoraDesc(
            Long usuarioId
    );

}
