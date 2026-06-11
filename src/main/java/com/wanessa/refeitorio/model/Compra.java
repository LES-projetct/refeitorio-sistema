/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author wanes
 */
@Entity
@Getter
@Setter
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    private BigDecimal valorTotal;

    private BigDecimal saldoAnterior;

    private BigDecimal saldoAtualizado;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", 
            cascade = CascadeType.ALL, 
            orphanRemoval = true)
    private List<ItemCompra> itens;

    @PrePersist
    @PreUpdate
    public void calcularValorTotal() {

        if (itens != null && !itens.isEmpty()) {

            this.valorTotal = itens.stream()
                    .map(ItemCompra::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
