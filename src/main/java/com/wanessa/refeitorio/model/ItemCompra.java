/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author wanes
 */
@Entity
@Getter
@Setter
public class ItemCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    @JsonBackReference
    @ManyToOne
    private Produto produto;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "compra_id")
    private Compra compra;

    private BigDecimal peso;

    @PrePersist
    @PreUpdate
    public void calcularValorTotal() {

        if (valorUnitario == null) {
            valorTotal = BigDecimal.ZERO;
            return;
        }

        if (peso != null) {

            valorTotal = peso.multiply(valorUnitario);

        } else if (quantidade != null) {

            valorTotal = quantidade.multiply(valorUnitario);

        } else {

            valorTotal = BigDecimal.ZERO;
        }
    }
}
