/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 *
 * @author wanes
 */
@Entity
@Getter
@Setter
public class Produto {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String codigoBarras;

    private BigDecimal precoVenda;

    private BigDecimal precoCusto;

    private Boolean vendidoPorPeso;

    private Boolean ativo;
}
