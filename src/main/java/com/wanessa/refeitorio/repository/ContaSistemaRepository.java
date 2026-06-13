/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.ContaSistema;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author wanes
 */
@Repository
public interface ContaSistemaRepository extends JpaRepository<ContaSistema, Long> {

    /**
     * Localiza uma conta pelo login, ignorando diferenças entre letras
     * maiúsculas e minúsculas.
     */
    Optional<ContaSistema> findByLoginIgnoreCase(String login);

    /**
     * Verifica se um login já está cadastrado.
     */
    boolean existsByLoginIgnoreCase(String login);

    /**
     * Lista somente as contas ativas.
     */
    List<ContaSistema> findByAtivoTrueOrderByNomeAsc();

    /**
     * Lista todas as contas em ordem alfabética.
     */
    List<ContaSistema> findAllByOrderByNomeAsc();

    /**
     * Verifica se o cliente já possui uma conta de acesso.
     */
    boolean existsByUsuarioRelacionadoId(Long usuarioId);

    /**
     * Localiza a conta vinculada ao cliente.
     */
    Optional<ContaSistema> findByUsuarioRelacionadoId(Long usuarioId);

}
