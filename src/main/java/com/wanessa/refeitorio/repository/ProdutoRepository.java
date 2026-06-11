/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.repository;

import com.wanessa.refeitorio.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author wanes
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
}
